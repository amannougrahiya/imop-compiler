#include <stdio.h>
#include <omp.h>
#include <limits.h>
#include <stdlib.h>
#include <string.h>

#include "ompdist/tree_gen.h"
#include "ompdist/utils.h"

#include "config.h"

int ROOT;

typedef struct {
    int color;
    int recv;
    int again;
    int parent;
} payload;

/**
 * num_digits - Number of digits (in binary) required to represent each label.
 *
 * @g: the graph
 *
 * Returns the number of required digits.
 */
int num_digits(graph* g) {
    int N = g->N;

    int digits = 0;
    while (N) {
        digits++;
        N >>= 1;
    }

    return digits;
}

/**
 * initialize_graph: Initializes the graph.
 *
 * @g: the graph
 */
void initialize_graph(graph *g) {
    DEBUG("initializing graph\n");

    for(int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        u->data = malloc(sizeof(payload));
    }

    int* added = malloc(g->N * sizeof(int));
    memset(added, 0, g->N * sizeof(int));

    int* queue = malloc(g->N * sizeof(int));
    memset(queue, 0, g->N * sizeof(int));
    int index = 0;
    int max = 0;

    queue[max++] = ROOT;
    added[ROOT] = 1;

    while (index < max) {
        int i = queue[index++];
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        u_data->color = i;
        u_data->recv = i;

        u_data->again = 0;

        for (int j = 0; j < u->degree; j++) {
            node* v = *((node**) elem_at(&u->neighbors, j));
            payload* v_data = v->data;

            if (added[v->label])
                continue;

            added[v->label] = 1;
            queue[max++] = v->label;
            v_data->parent = u->label;
        }
    }

    free(queue);
    free(added);
}

/**
 * again - Checks if any of the vertices are scheduled for another check.
 *
 * @g: the graph
 *
 * Returns 1 if any of the vertices are scheduled for another check and 0
 * if none of the nodes need to colored.
 */
int again(graph* g) {
    int result = 0;

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        if (u_data->again)
            result = 1;
    }

    return result;
}

/**
 * parent_to_child - Propagates the color of each parent to all its children.
 *
 * @g: the graph
 */
void parent_to_child(graph* g) {
    DEBUG("starting\n");

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for(int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        if (u->label != ROOT) {
            node* parent = elem_at(&g->vertices, u_data->parent);
            payload* parent_data = parent->data;

            u_data->recv = parent_data->color;
        }
    }
}

/**
 * six_colors_tree - Colors the tree with six colors.
 *
 * @g:      the graph
 * @digits: the number of labels in the graph
 */
void six_color_tree(graph *g, int digits) {
    DEBUG("starting\n");

    parent_to_child(g);

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for(int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        if(u->label == ROOT)
            continue;

        u_data->again = 0;

        int xor = u_data->recv ^ u_data->color;
        for(int k = 0; k < digits; k++) {
            int mask = 1 << k;

            /* If they have this bit different, color */
            if(xor & mask) {
                u_data->color = (k << 1) + (u_data->color & mask ? 1 : 0);
                break;
            }
        }

        if(u_data->color >= 6)
            u_data->again = 1;
    }
}

/**
 * verify_and_print_solution - Verifies if the solution is correct and then
 * prints the coloring.
 *
 * @g: the graph
 *
 * Returns 0 if the solution is correct. Returns 1 otherwise.
 */
int verify_and_print_solution(graph *g) {
    int correct = 1;
    for(int i = 0; i < g->N; i++) {
        node* u = elem_at(&g->vertices, i);
        payload* u_data = u->data;

        DEBUG("%d->color = %d\n", u->label, u_data->color);

        if (u_data->color >= 6 || u_data->color < 0) {
            WARN("%d->color is not between [0, 5]\n", u->label);
            correct = 0;
        }

        for(int j = 0; j < u->degree; j++) {
            node* v = *((node**) elem_at(&u->neighbors, j));
            payload* v_data = v->data;

            if(u_data->color == v_data->color) {
                WARN("%d->color = %d->color and there's an edge\n", u->label, v->label);
                correct = 0;
            }
        }
    }

    if (correct)
        INFO("solution verified to be correct\n");
    else
        INFO("solution incorrect\n");

    return !correct;
}

/**
 * Based on Roger Wattenhofer's Principles of Distributed Computing's
 * Algorithm 1.17 to color a tree.
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

        if (argc > 1)
            sscanf(argv[1], "%d", &N);
        M = N-1;

        ROOT = 0;
        g = generate_new_tree(N);
    }

    int digits = num_digits(g);
    DEBUG("digits = %d\n", digits);

    long long duration = 0;
    double total_energy = 0;

    int verification;

    for (int i = 0; i < iterations; i++) {
        begin_timer();
        init_energy_measure();

        initialize_graph(g);
        do {
            six_color_tree(g, digits);
        } while (again(g));

        total_energy += total_energy_used();
        duration += time_elapsed();

        verification = verify_and_print_solution(g);
    }

    if (iterate)
        printf("%.2lf %.2lf\n", ((double) duration) / iterations, total_energy / iterations);

    return verification;
}
