#include <math.h>
#include <stdlib.h>

#include "tree_gen.h"
#include "utils.h"

/**
 * generate_new_tree - Generates a new tree and returns it.
 *
 * @N: The number of vertices in the tree that's to be generated.
 *
 * Returns a graph pointer.
 */
graph* generate_new_tree(int N) {
    int M = N;
    M *= sqrt(N);

    graph* cg = generate_new_connected_graph(N, M);

    int* visited = calloc(N, sizeof(int));

    graph* g = new_graph(N, N-1);

    vector* queue = new_vector(sizeof(node*));
    int queue_position = 0;

    node* root = elem_at(&cg->vertices, 0);
    append_to_vector(queue, &root);
    visited[root->label] = 1;

    int edges_added = 0;

    DEBUG("Walking through the connected graph to generate a tree\n");
    while (queue_position < queue->used) {
        node* cur = *((node**) elem_at(queue, queue_position));
        queue_position++;

        for (int i = 0; i < cur->degree; i++) {
            node* neighbor = *((node**) elem_at(&cur->neighbors, i));

            if (visited[neighbor->label])
                continue;

            add_edge(g, cur->label, neighbor->label);
            visited[neighbor->label] = 1;
            append_to_vector(queue, &neighbor);

            edges_added += 1;
        }
    }

    free_graph(cg);
    free_vector(queue);
    free(visited);

    DEBUG("Created a %d-edged tree (%p) of %d vertices\n", edges_added, g, N);
    return g;
}
