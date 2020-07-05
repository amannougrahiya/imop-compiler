#include <stdlib.h>
#include <stddef.h>
#include <string.h>

#include "vector.h"
#include "utils.h"

/**
 * initialize_vector - Initializes the fields of a given vector pointer.
 *
 * @v:           a pointer to a vector
 * @object_size: Size of each object that's going into this vector.
 */
void initialize_vector(vector *v, size_t object_size) {
    v->object_size = object_size;
    v->allocated = 16;
    v->items = malloc(v->allocated * v->object_size);
    v->used = 0;
}

/**
 * new_vector - Creates a new vector
 *
 * @object_size: Size of each object that's going into this vector.
 *
 * Returns a `vector` pointer.
 */
vector* new_vector(size_t object_size) {
    vector* v = (vector*) malloc(1 * sizeof(vector));

    initialize_vector(v, object_size);

    return v;
}

/**
 * append_to_vector - Appends an object to a given vector
 *
 * @v:      A `vector` pointer.
 * @object: The object to append itself.
 */
void append_to_vector(vector* v, void* object) {
    while (v->allocated <= v->used + 1) {
        v->allocated *= 2;
        v->items = realloc(v->items, v->allocated * v->object_size);
    }

    memcpy(v->items + (v->used * v->object_size), object, v->object_size);
    v->used++;
}

/**
 * index - Get the item at a given index.
 *
 * @v:   A `vector` pointer to a vector.
 * @idx: The index of the element.
 *
 * Returns a pointer to the object.
 */
void* elem_at(vector* v, size_t idx) {
    return v->items + (idx * v->object_size);
}

/**
 * free_vector - Frees a vector and releases the memory allocated for its items.
 *
 * @v: The `vector` to free.
 */
void free_vector(vector* v) {
    free(v->items);
    free(v);
}
