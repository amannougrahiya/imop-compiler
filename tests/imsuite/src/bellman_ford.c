#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <omp.h>
#include <limits.h>

#include "ompdist/queues.h"
#include "ompdist/graph.h"
#include "ompdist/graph_gen.h"
#include "ompdist/utils.h"
#include "ompdist/msr.h"
#include "config.h"

#define INF INT_MAX

typedef struct {
    int distance;
} payload;

typedef struct {
    int from;
    int y;
} message;

int ROOT;

/**
 * initialize_graph - Initializes the graph nodes with the payload data.
 *
 * @g: the graph
 */
void initialize_graph(graph* g) {
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);

        payload* data = malloc(sizeof(payload));

        data->distance = INF;

        u->data = data;
    }

    node* root = elem_at(&g->vertices, ROOT);
    payload* root_data = root->data;
    root_data->distance = 0;
}

/**
 * root_message - Intiates the root message.
 *
 * @g:    the graph
 * @recv: the recv queuelist
 */
void root_message(graph* g, queuelist* recv) {
    DEBUG("sending root message\n");

    node* root = elem_at(&g->vertices, ROOT);

    for (int j = 0; j < root->degree; j++) {
        node* u = *((node**) elem_at(&root->neighbors, j));

        message m = {ROOT, 1};
        enqueue(recv, u->label, &m);
    }
}

/**
 * messages_in_queue - Checks if there are any messages in the queues of any
 * of the nodes.
 *
 * @g:    the graph
 * @recv: the recv queuelist
 *
 * Returns 1 if there are any messages, 0 otherwise.
 */
int messages_in_queue(int N, queuelist* recv) {
    int result = 0;

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < N; i++) {
        if (!is_ql_queue_empty(recv, i))
            result = 1;
    }

    DEBUG("messages in queue = %d\n", result);

    return result;
}


/**
 * recv_and_send - Receives messages and passes them on (after incrementing
 * the message distance) to its neighbors.
 *
 * @g:    the graph
 * @recv: the recv queuelist
 * @send: the send queuelist
 */
void recv_and_send(graph* g, queuelist* recv, queuelist* send) {
    DEBUG("receiving and sending messages\n");

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        int lowest_y = INT_MAX;
        int lowest_from = 0;
        while (!is_ql_queue_empty(recv, i)) {
            message* m = dequeue(recv, i);

            if (lowest_y > m->y) {
                lowest_y = m->y;
                lowest_from = m->from;
            }
        }

        if (lowest_y != INT_MAX && lowest_y < u_data->distance) {
            u_data->distance = lowest_y;

            for (int j = 0; j < u->degree; j++) {
                node* v = *((node**) elem_at(&u->neighbors, j));
                payload* u_data = u->data;

                if (v->label == lowest_from)
                    continue;

                message m = {u->label, lowest_y+1};
                enqueue(send, v->label, &m);
            }
        }
    }
}

/**
 * propagate_messages - Moves messages from the send queuelist to the recv
 * queuelist.
 *
 * @g:    the graph
 * @recv: the recv queuelist
 * @send: the send queuelist
 */
void propagate_messages(graph* g, queuelist* recv, queuelist* send) {
    DEBUG("propagating messages from the send queuelist to recv\n");

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);

        while (!is_ql_queue_empty(send, u->label))
            enqueue(recv, u->label, dequeue(send, u->label));
    }
}

/**
 * print_solution - prints a BFS Bellman-Ford solution
 *
 * @g: a pointer to the graph object
 */
void print_solution(graph* g) {
    int max_distance = 0;

    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = cur->data;

        if (data->distance > max_distance)
            max_distance = data->distance;

        INFO("%d->distance = %d\n", cur->label, data->distance);
    }

    INFO("max_distance = %d\n", max_distance);
}

/**
 * Based on Roger Wattenhofer's Principles of Distributed Computing's
 * Algorithm 2.13 (Bellman-Ford BFS) to solve the single-source shortest
 * path problem.
 */
int main(int argc, char* argv[]) {
    int N;
    int M;
    graph* g;

    int iterate;
    int iterations = 1;

    if ((iterate = input_through_argv(argc, argv))) {
        FILE* in = fopen(argv[2], "r");

        fscanf(in, "%d\n", &N);
        g = new_graph(N, 0);

        fscanf(in, "%d\n", &ROOT);

        g->M = M = read_graph(g, in);

        fclose(in);

        sscanf(argv[3], "%d", &iterations);
    }
    else {
        N = 16;
        M = 64;

        if (argc > 1) {
            sscanf(argv[1], "%d", &N);
            sscanf(argv[2], "%d", &M);
        }

        g = generate_new_connected_graph(N, M);

        ROOT = 0;
    }

    long long duration = 0;
    double total_energy = 0;

    for (int i = 0; i < iterations; i++) {
        queuelist* recv = new_queuelist(N, sizeof(message));
        queuelist* send = new_queuelist(N, sizeof(message));

        begin_timer();
        init_energy_measure();

        initialize_graph(g);

        root_message(g, recv);
        while (messages_in_queue(g->N, recv)) {
            recv_and_send(g, recv, send);
            propagate_messages(g, recv, send);
        }

        total_energy += total_energy_used();
        duration += time_elapsed();

        // print_solution(g);
    }

    if (iterate)
        printf("%.2lf %.2lf\n", ((double) duration) / iterations, total_energy / iterations);

    return 0;
}
