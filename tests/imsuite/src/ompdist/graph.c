#include <stdio.h>
#include <stdlib.h>

#include "graph.h"
#include "utils.h"

/**
 * new_graph - Creates a new `graph`.
 *
 * Returns a `graph` pointer that contains the graph.
 */
graph* new_graph(int N, int M) {
    graph* g = malloc(1 * sizeof(graph));
    
    g->N = N;
    g->M = M;

    initialize_vector(&g->vertices, sizeof(node));

    for (int i = 0; i < g->N; i++) {
        node u;

        initialize_vector(&u.neighbors, sizeof(node*));
        u.degree = 0;
        u.data = NULL;
        u.label = i;
        u.weight = 0;

        append_to_vector(&g->vertices, &u);
    }

    g->adj_mat = (int**) malloc(N * sizeof(int*));
    for (int i = 0; i < g->N; i++)
        g->adj_mat[i] = (int*) calloc(N, sizeof(int));

    g->root = NULL;

    DEBUG("Created new graph %p\n", g);

    return g;
}

/**
 * read_graph - Reads a graph from the input file. This must follow the IMSuite
 * input file format.
 *
 * @g:  the graph
 * @in: a FILE* handle to the input file
 *
 * Returns the number of edges in the graph.
 */
int read_graph(graph* g, FILE* in) {
    int M = 0;

    char* line = malloc(g->N + 2);

    for (int i = 0; i < g->N; i++) {
        fscanf(in, "%s", line);
        for (int j = 0; j < g->N; j++) {
            switch (line[j]) {
                case '0':
                    break;

                case '1':
                    M++;
                    add_edge(g, i, j);
                    break;
            }
        }
    }

    free(line);

    return M/2;
}

/**
 * read_weights - Reads the weight of each edge from the input file. This must
 * follow the IMSuite input file format.
 *
 * @g:  the graph
 * @in: a FILE* handle to the input file; this must have its seek position at
 *      the start of the weight list
 */
void read_weights(graph* g, FILE* in) {
    for (int i = 0; i < g->N; i++) {
        for (int j = 0; j < g->N; j++) {
            int x;
            fscanf(in, "%d", &x);

            if (i == j)
                continue;

            if (g->adj_mat[j][i])
                g->adj_mat[i][j] = x;
        }
    }
}

/**
 * print_graph - Prints the graph to stdout.
 *
 * @g: the graph
 */
void print_graph(graph*g) {
    printf("    ");
    for (int i = 0; i < g->N; i++)
        printf("%3d ", i);
    printf("\n");

    for (int i = 0; i < g->N; i++) {
        printf("%3d ", i);
        for (int j = 0; j < g->N; j++)
            printf("%3d ", g->adj_mat[i][j]);

        printf("\n");
    }
}

/**
 * add_edge - Adds an edge between nodes of two indices. Weight will be added
 * elsewhere through the `adj_mat` table.
 *
 * @g: A `graph` pointer to the graph.
 * @i: Index/label of the first node.
 * @j: Index/label of the second node.
 */
void add_edge(graph* g, int i, int j) {
    node* node_u = elem_at(&g->vertices, i);
    node* node_v = elem_at(&g->vertices, j);

    append_to_vector(&node_u->neighbors, &node_v);
    append_to_vector(&node_v->neighbors, &node_u);

    node_u->degree++;
    node_v->degree++;

    // temporarily set the weight to 1 to signal that there's an edge
    // here and that this number must be filled in later on
    g->adj_mat[i][j] = g->adj_mat[j][i] = 1;
}

/**
 * free_graph - Frees a graph and all its internal allocations.
 *
 * @g: The graph to free.
 */
void free_graph(graph* g) {
    DEBUG("Freeing the adjacency matrix\n");
    if (g->adj_mat != NULL) {
        for (int i = 0; i < g->N; i++)
            free(g->adj_mat[i]);
        free(g->adj_mat);
    }
}

/**
 * is_connected - Checks if a given graph is connected.
 *
 * @g: The graph to check.
 *
 * Returns 1 if the given graph is connected, 0 if the number of components
 * is more than 1.
 */
int is_connected(graph* g) {
    int* visited = calloc(g->N, sizeof(int));

    node* root = elem_at(&g->vertices, 0);

    visited[root->label] = 1;

    vector* queue = new_vector(sizeof(node*));
    append_to_vector(queue, &root);

    int queue_position = 0;

    DEBUG("Traversing nodes with BFS\n");
    while (queue_position < queue->used) {
        node* cur = *((node**) elem_at(queue, queue_position));
        queue_position++;

        for (int i = 0; i < cur->degree; i++) {
            node* neighbor = *((node**) elem_at(&cur->neighbors, i));
            if (visited[neighbor->label] > 0)
                continue;

            visited[neighbor->label] = 1;
            append_to_vector(queue, &neighbor);
        }
    }

    int connected = 1;
    for (int i = 0; i < g->N; i++) {
        if (visited[i] == 0)
            connected = 0;
    }
    DEBUG("The generated graph is %sconnected\n", connected ? "" : "not ");

    free_vector(queue);
    free(visited);

    return connected;
}
