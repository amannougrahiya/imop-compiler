#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#include "ompdist/graph.h"
#include "ompdist/graph_gen.h"
#include "ompdist/utils.h"
#include "ompdist/msr.h"
#include "config.h"

typedef struct {
    int present;
    int in_mis;
    double r;
} payload;

double randnum() {
    return ((double) rand()) / ((double) RAND_MAX);
}

/**
 * initialize_graph - Initializes the graph with basic data
 *
 * @g: a pointer to the graph object
 */
void initialize_graph(graph* g) {
    DEBUG("Initializing graph with payload\n");
    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);

        payload* data = malloc(sizeof(payload));

        data->present = 1;
        data->in_mis = 0;
        data->r = 0;

        cur->data = data;
    }
}

/**
 * generate_random_field - Generates the random field -- a double in the 
 * range [0, 1) -- for each node.
 *
 * @g: a pointer to the graph object
 */
void generate_random_field(graph* g) {
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = cur->data;

        if (!data->present)
            continue;

        data->r = randnum();
    }
}

/**
 * decide_mis_entry - Depending on each node's neighbors' random values, decide
 * if that node is entering the MIS.
 *
 * @g: a pointer to the graph object
 */
void decide_mis_entry(graph* g) {
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = cur->data;

        if (!data->present)
            continue;
        
        int enter = 1;
        for (int i = 0; i < cur->degree; i++) {
            node* neighbor = *((node**) elem_at(&cur->neighbors, i));
            payload* neighbor_data = neighbor->data;

            if (data->r > neighbor_data->r) {
                enter = 0;
                break;
            }
        }

        if (enter) {
            data->present = 0;
            data->in_mis = 1;
        }
    }
}

/**
 * remove_mis_adjacent_nodes - If a node has entered the MIS, its neighbors can never
 * be a part of the MIS. Remove them.
 *
 * @g: a pointer to the graph object
 */
void remove_mis_adjacent_nodes(graph* g) {
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = cur->data;

        if (data->in_mis) {
            for (int i = 0; i < cur->degree; i++) {
                node* neighbor = *((node**) elem_at(&cur->neighbors, i));
                payload* neighbor_data = neighbor->data;

                neighbor_data->present = 0;
            }
        }
    }
}

/**
 * do_present_nodes_exist - Checks if there are any nodes in the graph that are
 * still undecided on whether to enter the MIS or not.
 *
 * @g: a pointer to the graph object
 *
 * Returns 1 if there are such nodes, returns 0 otherwise.
 */
int do_present_nodes_exist(graph* g) {
    int keep_going = 0;

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = cur->data;

        if (data->present)
            keep_going = 1;
    }

    return keep_going;
}

/**
 * verify_and_print_solution - Verifies if the computed solution is correct and
 * prints the solution.
 *
 * @g: a pointer to the graph object
 *
 * Returns 0 if the produced solution is correct. Returns 1 otherwise.
 */
int verify_and_print_solution(graph* g) {
    /**
     * Note: there's no `#pragma omp parallel` required here - this is not a
     * part of the solution computation.
     */
    INFO("Elements in MIS: ");
    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = cur->data;

        if (data->in_mis)
            INFO("%d ", cur->label);
    }
    INFO("\n");

    /**
     * Again: no `#pragma omp parallel` required here.
     */
    int correct = 1;
    for (int i = 0; i < g->N; i++) {
        node* cur = elem_at(&g->vertices, i);
        payload* data = cur->data;

        if (!data->in_mis)
            continue;

        for (int j = 0; j < cur->degree; j++) {
            node* neighbor = *((node**) elem_at(&cur->neighbors, j));
            payload* neighbor_data = neighbor->data;

            if (neighbor_data->in_mis) {
                correct = 0;
                break;
            }
        }
    }

    if (correct)
        INFO("MIS solution verified to be correct\n");
    else
        INFO("MIS solution incorrect\n");

    return !correct;
}

/**
 * Creates a MIS from a randomly generated graph in a distributed manner.
 *
 * Links:
 *   - https://disco.ethz.ch/courses/podc_allstars/lecture/chapter7.pdf
 *     See Algorithm 7.12 (Fast MIS-2)
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

        iterate = 0;
    }

    long long duration = 0;
    double total_energy = 0;

    int verification;

    for (int i = 0; i < iterations; i++) {
        begin_timer();
        init_energy_measure();

        int keep_going = 1;

        initialize_graph(g);

        while (keep_going) {
            keep_going = 0;
            generate_random_field(g);
            decide_mis_entry(g);
            remove_mis_adjacent_nodes(g);
            keep_going = do_present_nodes_exist(g);
        }

        total_energy += total_energy_used();
        duration += time_elapsed();

        verification = verify_and_print_solution(g);
    }

    if (iterate)
        printf("%.2lf %.2lf\n", ((double) duration) / iterations, total_energy / iterations);

    return verification;
}
