#include <stdlib.h>
#include <stdio.h>
#include <omp.h>
#include <limits.h>

#include "ompdist/vector.h"
#include "ompdist/queues.h"
#include "ompdist/graph.h"
#include "ompdist/graph_gen.h"
#include "ompdist/utils.h"
#include "ompdist/msr.h"
#include "config.h"

typedef struct {
    int from;
} message;

typedef struct {
    int u;
    int v;
    int w;
} edge;

typedef struct {
    int fragment_id;
    int tmp_fragment_id;
    int received_first_message;
    edge* b;
} payload;

/**
 * initialize_graph: Initializes the graph.
 *
 * @g: the graph
 */
void initialize_graph(graph* g) {
    DEBUG("initializing the graph\n");
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = malloc(sizeof(payload));
        u->data = u_data;

        u_data->fragment_id = u->label;
        u_data->tmp_fragment_id = u->label;
        u_data->received_first_message = 0;
        u_data->b = NULL;
    }
}

/**
 * multiple_fragments - Checks if there are multiple fragments in the graph.
 *
 * @g: the graph
 *
 * Returns 1 if there are mutiple fragments. Returns 0 if there's just one
 * fragment (indicating that our algorithm has comepleted).
 */
int multiple_fragments(graph* g) {
    int multiple = 0;
    int last = -1;

    DEBUG("checking if there are multiple fragments\n");
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        if (last == -1)
            last = u_data->fragment_id;
        else if (u_data->fragment_id != last) {
            multiple = 1;
            break;
        }
    }

    return multiple;
}

/**
 * change_fragment - Changes all the nodes in a fragment to another fragment.
 *
 * @g:    the graph
 * @from: the source fragment ID
 * @to:   the destination fragment ID
 */
void change_fragment(graph* g, int from, int to) {
    DEBUG("changing all nodes with fragment_id=%d to %d\n", from, to);
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        if (u_data->fragment_id == from)
            u_data->fragment_id = to;
    }
}

/**
 * find_blue_edges - Finds the blue edge of each fragment using flooding-echo.
 *
 * @g:        the graph
 * @msgs:     each node's messages
 * @tmp_msgs: temporary queuelist holder for each node's messages
 * @blues:    queuelist of potential blue edges for each node
 */
void find_blue_edges(graph* g, queuelist* msgs, queuelist* tmp_msgs, queuelist* blues) {
    DEBUG("planting root messages\n");
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        u_data->received_first_message = 0;

        /* Only roots find the blue edge */
        if (u_data->fragment_id != u->label)
            continue;

        message m = {-1};
        enqueue(msgs, u->label, &m);
    }

    int nodes_yet_to_recv = 1;

    DEBUG("accumulating blue edges\n");
    while (nodes_yet_to_recv) {
        DEBUG("propagating the messages across the graph\n");
        #pragma omp parallel for schedule(SCHEDULING_METHOD)
        for (int i = 0; i < g->N; i++) {
            node* u = elem_at(&g->vertices, i);
            payload* u_data = u->data;

            if (u_data->received_first_message)
                continue;

            while (!is_ql_queue_empty(msgs, u->label)) {
                u_data->received_first_message = 1;
                message* m = dequeue(msgs, u->label);

                for (int j = 0; j < u->degree; j++) {
                    node* v = *((node**) elem_at(&u->neighbors, j));
                    payload* v_data = v->data;

                    /* Don't send the message back to the source */
                    if (v->label == m->from)
                        continue;

                    /**
                     * If the neighbor is outside the fragment it's a potential
                     * blue edge. Otherwise it's just a carrier for this message.
                     */
                    if (v_data->fragment_id != u_data->fragment_id) {
                        edge b = {u->label, v->label, g->adj_mat[u->label][v->label]};
                        enqueue(blues, u_data->fragment_id, &b);
                    }
                    else {
                        message mx = {u->label};
                        enqueue(tmp_msgs, v->label, &mx);
                    }
                }
            }
        }

        DEBUG("moving messages from tmp_msgs to msgs\n");
        #pragma omp parallel for schedule(SCHEDULING_METHOD)
        for (int i = 0; i < g->N; i++) {
            node* u = elem_at(&g->vertices, i);
            payload* u_data = u->data;

            while (!is_ql_queue_empty(tmp_msgs, u->label)) {
                message* m = dequeue(tmp_msgs, u->label);
                if (!u_data->received_first_message)
                    enqueue(msgs, u->label, m);
            }
        }

        nodes_yet_to_recv = 0;

        DEBUG("checking if there are any more nodes left to process\n");
        #pragma omp parallel for schedule(SCHEDULING_METHOD)
        for (int i = 0; i < g->N; i++) {
            node* u = elem_at(&g->vertices, i);
            payload* u_data = u->data;

            if (!u_data->received_first_message)
                nodes_yet_to_recv = 1;
        }

        DEBUG("nodes_yet_to_recv = %d\n", nodes_yet_to_recv);
    }

    /**
     * Find the minimum blue edge of all potential blue edge candidates for
     * each fragment. The actual data still lives on in the `blues` queue and
     * it won't be lost till the next phase, at which point we don't need it
     * anyway since all bridges would have happened.
     */
    DEBUG("finding the minimum of the accumulated blue edges\n");
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        /* only roots find the blue edge */
        if (u_data->fragment_id != u->label)
            continue;

        edge* min_edge = NULL;
        while (!is_ql_queue_empty(blues, u->label)) {
            edge* b = dequeue(blues, u->label);
            if (min_edge == NULL) {
                min_edge = b;
                continue;
            }

            /**
             * So this might look like some kind of weird logic, but there's a
             * reason why I'm doing this: say there are two different fragments
             * with blue edges into each other. If the two blue edges are the
             * same, it's perfectly fine -- it'll be resolved in the conflicting
             * blue edges scennario. However, if they're different, they'll
             * both be added to the MST when it should only be one of them. To
             * prevent this, we'll simply use the edge that has a smaller value
             * of (u*N + v). Note that both will have the exact same weight,
             * so it's fine whichever one we choose.
             */
            int b_score = b->u*g->N + b->v;
            if (b->u > b->v)
                b_score = b->v*g->N + b->u;

            int min_score = min_edge->u*g->N + min_edge->v;
            if (min_edge->u > min_edge->v)
                min_score = min_edge->v*g->N + min_edge->u;

            if ((b->w < min_edge->w) || (b->w == min_edge->w && b_score < min_score))
                min_edge = b;
        }

        node* future_leader = elem_at(&g->vertices, min_edge->u);
        payload* future_leader_data = future_leader->data;

        u_data->b = min_edge;
        future_leader_data->b = min_edge;
    }

}

/**
 * assign_tmp_fragments - Temporarily sets the fragment ID of each element in
 * a fragment to that of the element in the blue edge.
 *
 * @g: the graph
 */
void assign_tmp_fragments(graph* g) {
    DEBUG("setting tmp_fragment_id\n");
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        node* leader = elem_at(&g->vertices, u_data->fragment_id);
        payload* leader_data = leader->data;

        u_data->tmp_fragment_id = leader_data->b->u;
    }

    DEBUG("setting temporary fragment_id\n");
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        u_data->fragment_id = u_data->tmp_fragment_id;
    }
}

/**
 * merge_fragments - Merges the fragments by merging the non-conflicting (merge
 * requests (u, v, w) such that there's no merge request (v, u, w)) followed
 * by conflicting ones.
 *
 * @g:   the graph
 * @mst: a queuelist to store a list of edges (in the MST) in
 */
void merge_fragments(graph* g, queuelist* mst) {
    /**
     * `ok` denotes whether conflicting merges are okay to merge. This is done
     * so that non-conflicting merge requests are merged first and the conflicting
     * ones are done so later.
     */
    for (int ok = 0; ok < 2; ok++) {
        DEBUG("conflicts phase: %d\n", ok);
        #pragma omp parallel for schedule(SCHEDULING_METHOD)
        for (int i = 0; i < g->N; i++) {
            node* u = elem_at(&g->vertices, i);
            payload* u_data = u->data;

            if (u_data->fragment_id != u->label)
                continue;

            #pragma omp critical
            {
                node* v = elem_at(&g->vertices, u_data->b->v);
                payload* v_data = v->data;

                node* v_leader = elem_at(&g->vertices, v_data->fragment_id);
                payload* v_leader_data = v_leader->data;

                int conflicting_merges = (u->label == v_leader_data->b->v &&
                                          v_leader_data->b->u == v->label &&
                                          u_data->b->v == v->label);

                if (conflicting_merges == ok) {
                    change_fragment(g, u->label, v_leader->label);
                    edge m = {u->label, v->label, g->adj_mat[u->label][v->label]};
                    enqueue(mst, 0, &m);
                }
            }
        }
    }
}

/**
 * verify_and_print_solution - Verifies if the produced solution is correct
 * using Prim's algorithm to compute the minimum spanning tree.
 *
 * @g:   the graph
 * @mst: a queuelist to store a list of edges (in the MST) in
 *
 * Returns 0 if everything is correct. Returns 1 otherwise.
 */
int verify_and_print_solution(graph* g, queuelist* mst) {
    long long int computed_weight = 0;
    while (!is_ql_queue_empty(mst, 0)) {
        edge* e = dequeue(mst, 0);
        computed_weight += e->w;
        INFO("(%d, %d, %d)\n", e->u, e->v, e->w);
    }

    long long int actual_weight = 0;

    int done = 0;

    int* in_mst = malloc(g->N * sizeof(int));
    int* parent = malloc(g->N * sizeof(int));
    int* d = malloc(g->N * sizeof(int));

    for (int i = 0; i < g->N; i++) {
        in_mst[i] = 0;
        parent[i] = -1;
        d[i] = INT_MAX;
    }

    d[0] = 0;

    while (done < g->N) {
        done++;

        int min = INT_MAX;
        int min_idx = 0;
        for (int i = 0; i < g->N; i++) {
            if (!in_mst[i] && min >= d[i]) {
                min = d[i];
                min_idx = i;
            }
        }

        node* u = elem_at(&g->vertices, min_idx);
        in_mst[u->label] = 1;

        for (int i = 0; i < u->degree; i++) {
            node* v = *((node**) elem_at(&u->neighbors, i));
            int w = g->adj_mat[u->label][v->label];

            if (in_mst[v->label] == 0 && w < d[v->label]) {
                d[v->label] = w;
                parent[v->label] = u->label;
            }
        }
    }

    for (int i = 1; i < g->N; i++)
        actual_weight += g->adj_mat[i][parent[i]];

    if (actual_weight == computed_weight)
        INFO("correct! computed tree is the MST\n");
    else
        INFO("incorrect: actual_weight=%lld, computed_weight=%lld\n", actual_weight, computed_weight);

    return computed_weight != actual_weight;
}

/**
 * Based on Roger Wattenhofer's Principles of Distributed Computing's
 * Algorithm 2.18 (Gallager-Humblet-Spira) to find the minimum spanning
 * tree of a weighted graph in a distributed manner.
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
        
        g->M = M = read_graph(g, in);
        read_weights(g, in);

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
    }

    long long duration = 0;
    double total_energy = 0;

    int verification;

    for (int i = 0; i < iterations; i++) {
        queuelist* msgs = new_queuelist(g->N, sizeof(message));
        queuelist* tmp_msgs = new_queuelist(g->N, sizeof(message));
        queuelist* blues = new_queuelist(g->N, sizeof(edge));

        queuelist* mst = new_queuelist(1, sizeof(edge));

        begin_timer();
        init_energy_measure();

        initialize_graph(g);

        while (multiple_fragments(g)) {
            find_blue_edges(g, msgs, tmp_msgs, blues);
            assign_tmp_fragments(g);
            merge_fragments(g, mst);
        }

        total_energy += total_energy_used();
        duration += time_elapsed();

        free_queuelist(msgs);
        free_queuelist(tmp_msgs);
        free_queuelist(blues);

        verification = verify_and_print_solution(g, mst);
        free_queuelist(mst);
    }

    if (iterate)
        printf("%.2lf %.2lf\n", ((double) duration) / iterations, total_energy / iterations);

    return verification;
}
