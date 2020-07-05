#include <stdlib.h>

#include "graph_gen.h"
#include "utils.h"

/**
 * generate_new_graph - Generates a new graph and returns it.
 *
 * @N: The number of vertices in the graph that's to be generated.
 *
 * Returns a graph pointer.
 */
graph* generate_new_graph(int N, int M) {
    graph* g = new_graph(N, M);

    DEBUG("Generating edges for graph %p\n", g);

    int edges_created = 0;
    while (edges_created < M) {
        int u = rand() % N;
        int v = rand() % N;

        // no self-edges allowed
        if (u == v)
            continue;

        // for consistency, u is always less than v
        if (u > v)
            swap(&u, &v);

        // do not recreate existing edges
        if (g->adj_mat[u][v] != 0)
            continue;

        g->adj_mat[u][v] = g->adj_mat[v][u] = 1 + rand()%100;

        add_edge(g, u, v);

        edges_created += 1;
    }

    DEBUG("%d edges created\n", edges_created);

    return g;
}

/**
 * generate_new_connected_graph - Generates a new connected graph and returns it.
 *
 * @N: The number of vertices in the graph that's to be generated.
 *
 * Returns a graph pointer.
 */
graph* generate_new_connected_graph(int N, int M) {
    graph* g = NULL;

    int attempts = 0;

    do {
        attempts++;

        if (g != NULL)
            free_graph(g);

        DEBUG("Creating a new connected graph (attempt %d)\n", attempts);
        g = generate_new_graph(N, M);
    } while (!is_connected(g));

    DEBUG("Created a connected graph at %p after %d attempts\n", g, attempts);
    return g;
}
