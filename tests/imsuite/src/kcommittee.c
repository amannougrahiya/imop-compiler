#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#include "ompdist/vector.h"
#include "ompdist/graph.h"
#include "ompdist/graph_gen.h"
#include "ompdist/queues.h"
#include "ompdist/utils.h"
#include "ompdist/msr.h"
#include "config.h"

typedef struct {
    int x;
    int y;
} invitation;

typedef struct {
    int default_leader;
    int leader;
    int committee;
    int min_active;
    invitation invite;
} payload;

int min(int a, int b) {
    return a < b ? a : b;
}

/**
 * min_invitation - computes and sets the lexicographically smaller invitation
 * in `a`.
 *
 * @a: one invitation
 * @b: the other invitation
 */
void min_invitation(invitation* a, invitation* b) {
    if ((b->x < a->x) || (b->x == a->x && b->y < a->y)) {
        a->x = b->x;
        a->y = b->y;
        return;
    }
}

/**
 * initialize_graph - Initializes the graph with basic data.
 *
 * @g:     a pointer to the graph object
 * @kvals: the initial leader values
 */
void initialize_graph(graph* g, int* kvals) {
    invitation default_invite = { g->N, g->N };

    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = malloc(sizeof(payload));

        data->leader = kvals[i];
        data->default_leader = kvals[i];
        data->committee = g->N+1;
        data->min_active = g->N+1;
        data->invite = default_invite;

        cur->data = data;
    }
}

/**
 * do_polling - Performs the polling phase of the k-committee algorithm
 *
 * @g:         the graph itself
 * @K:         the maximum number of elements in a committee
 * @active_ql: a pointer to a queuelist object that's going to store `min_active`s
 */
void do_polling(graph* g, int K, queuelist* active_ql) {
    DEBUG("starting polling\n");

    DEBUG("starting K-1 rounds\n");
    for (int k = 0; k < K-1; k++) {
        DEBUG("round k = %d\n", k);

        /**
         * Broadcast each node's `min_active` to its neighbors.
         */
        DEBUG("broadcasting `min_active`s\n");
        #pragma omp parallel for schedule(SCHEDULING_METHOD)
        for (int i = 0; i < g->N; i++) {
            node* cur = elem_at(&g->vertices, i);
            payload* data = cur->data;

            if (data->committee == g->N+1)
                data->min_active = data->default_leader;
            else
                data->min_active = g->N+1;

            for (int j = 0; j < cur->degree; j++) {
                node* neighbor = *((node**) elem_at(&cur->neighbors, j));
                enqueue(active_ql, neighbor->label, &data->min_active);
            }
        }

        /**
         * Receive all the broadcasted `min_active`s.
         */
        DEBUG("receiving broadcasted transmissions\n");
        #pragma omp parallel for schedule(SCHEDULING_METHOD)
        for (int i = 0; i < g->N; i++) {
            node* cur = elem_at(&g->vertices, i);
            payload* data = cur->data;

            while(!is_ql_queue_empty(active_ql, i)) {
                int* active = dequeue(active_ql, i);
                data->min_active = min(data->min_active, *active);
            }

            data->leader = min(data->leader, data->min_active);
        }
    }
}

/**
 * do_selection - Performs the selection phase of the k-committee algorithm
 *
 * @g:         the graph itself
 * @K:         the maximum number of elements in a committee
 * @invite_ql: a pointer to a queuelist object that's going to store invitations
 */
void do_selection(graph* g, int K, queuelist* invite_ql) {
    DEBUG("starting selection\n");

    DEBUG("creating initial invitations\n");
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = cur->data;

        if (data->leader == data->default_leader) {
            data->invite.x = i;
            data->invite.y = data->min_active;
        }
        else {
            data->invite.x = g->N+1;
            data->invite.y = g->N+1;
        }
    }

    DEBUG("starting K-1 rounds\n");
    for (int k = 0; k < K-1; k++) {
        DEBUG("round k = %d\n", k);

        /**
         * Broadcast invitations to neighbors.
         */
        DEBUG("broadcasting invitations\n");
        #pragma omp parallel for schedule(SCHEDULING_METHOD)
        for (int i = 0; i < g->N; i++) {
            node* cur = elem_at(&g->vertices, i);
            payload* data = cur->data;

            for (int j = 0; j < cur->degree; j++) {
                node* neighbor = *((node**) elem_at(&cur->neighbors, j));
                enqueue(invite_ql, neighbor->label, &data->invite);
            }
        }

        /**
         * Of all the invitations we've received, choose the lexicographically
         * smallest one.
         */
        DEBUG("receiving broadcasted invitations\n");
        #pragma omp parallel for schedule(SCHEDULING_METHOD)
        for (int i = 0; i < g->N; i++) {
            node* cur = elem_at(&g->vertices, i);
            payload* data = cur->data;

            while (!is_ql_queue_empty(invite_ql, i)) {
                invitation* invite = dequeue(invite_ql, i);
                min_invitation(&data->invite, invite);
            }

            // make sure the invite is for us
            if (data->invite.y == data->default_leader && data->invite.x == data->leader)
                data->committee = data->leader;
        }
    }
}

/**
 * legalize_committees - A final sanity check to make sure there aren't any
 * illegal committees with IDs larger than the number of vertices.
 *
 * @g: the graph itself
 */
void legalize_committees(graph* g) {
    DEBUG("making sure there aren't any illegal committees\n");
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = cur->data;

        if (data->committee >= g->N)
            data->committee = i;
    }
}

/**
 * verify_and_print_solution - Verifies that the generated committee is legal
 * and prints the solution.
 *
 * @g: the graph itself
 * @K: the maximum number of elements in a committee
 *
 * Returns 0 if the produced partitioning is correct. Returns 1 otherwise.
 */
int verify_and_print_solution(graph* g, int K) {
    int correct = 1;

    int* committee_count = malloc(g->N * sizeof(int));
    for (int i = 0; i < g->N; i++)
        committee_count[i] = 0;

    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = cur->data;

        if (data->committee >= g->N) {
            correct = 0;
            WARN("%d apparently belongs to a non-existant committee %d\n", i, data->committee);
            goto end;
        }

        committee_count[data->committee]++;
        INFO("%d->committee = %d\n", i, data->committee);
    }

    for (int i = 0; i < g->N; i++) {
        if (committee_count[i] > K) {
            WARN("committee %d has too many members (%d > %d)\n", i, committee_count[i], K);
            correct = 0;
        }
    }

end:
    free(committee_count);

    if (correct)
        INFO("Produced solution is correct\n");
    else
        INFO("Produced solution is incorrect\n");

    return !correct;
}

/**
 * Based on Roger Wattenhofer's Principles of Distributed Computing's
 * section 23.4.2 on k-Committee election.
 */
int main(int argc, char* argv[]) {
    int N;
    int M;
    int K;
    int* kvals;
    graph* g;

    int iterate;
    int iterations = 1;

    if ((iterate = input_through_argv(argc, argv))) {
        FILE* in = fopen(argv[2], "r");

        fscanf(in, "%d\n", &N);
        kvals = malloc(N * sizeof(int));

        fscanf(in, "%d\n", &K);

        g = new_graph(N, 0);
        
        g->M = M = read_graph(g, in);

        fscanf(in, "\n");
        for (int i = 0; i < N; i++)
            fscanf(in, "%d", &kvals[i]);

        fclose(in);

        sscanf(argv[3], "%d", &iterations);
    }
    else {
        N = 16;
        M = 64;
        K = 4;

        if (argc > 1) {
            sscanf(argv[1], "%d", &N);
            sscanf(argv[2], "%d", &M);
            sscanf(argv[3], "%d", &K);
        }

        g = generate_new_connected_graph(N, M);

        kvals = malloc(N * sizeof(int));
        for (int i = 0; i < N; i++)
            kvals[i] = i;
    }

    long long duration = 0;
    double total_energy = 0;

    int verification;

    for (int i = 0; i < iterations; i++) {
        queuelist* active_ql = new_queuelist(N, sizeof(int));
        queuelist* invite_ql = new_queuelist(N, sizeof(invitation));

        begin_timer();
        init_energy_measure();

        initialize_graph(g, kvals);

        for (int k = 0; k < K; k++) {
            DEBUG("phase k = %d\n", k);
            do_polling(g, K, active_ql);
            do_selection(g, K, invite_ql);
        }
        legalize_committees(g);

        total_energy += total_energy_used();
        duration += time_elapsed();

        verification = verify_and_print_solution(g, K);

        free_queuelist(invite_ql);
        free_queuelist(active_ql);
    }

    free(kvals);

    if (iterate)
        printf("%.2lf %.2lf\n", ((double) duration) / iterations, total_energy / iterations);

    return verification;
}
