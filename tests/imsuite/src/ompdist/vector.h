#ifndef _VECTOR_H_
#define _VECTOR_H_

#include <stddef.h>

typedef struct {
    void* items;            // holds the items

    size_t object_size;     // stores the size of each object

    int allocated;          // number of objects allocated till now
    int used;               // number of objects used
} vector;

void initialize_vector(vector*, size_t);

vector* new_vector(size_t);

void append_to_vector(vector*, void*);

void* elem_at(vector*, size_t);

void free_vector(vector*);

#endif // _VECTOR_H_
