#ifndef _GRAPH_H_
#define _GRAPH_H_

#include <stdio.h>

#include "vector.h"

typedef struct {
    vector neighbors;       // a list of pointers to neighbors

    int degree;             // the out-degree (= in-degree) of the graph

    void* data;             // some arbitrary problem-specific data
                            // DO NOT free this address - this will be automatically
                            // freed when `free_graph` is called if it's not NULL.

    int label;

    int weight;
} node;

typedef struct {
    vector vertices;        // the list of vertices

    int N;                  // number of vertices
    int M;                  // number of edges

    int** adj_mat;          // edge relationships stored as an adjacency graph

    node* root;
} graph;

graph* new_graph();

int read_graph(graph*, FILE*);

void read_weights(graph*, FILE*);

void print_graph(graph*);

void add_edge(graph*, int, int);

void free_graph(graph*);

int is_connected(graph*);

#endif // _GRAPH_H_
