#include <stdlib.h>

#include "queues.h"
#include "vector.h"
#include "locks.h"
#include "utils.h"

/**
 * new_queuelist - Creates a new queuelist object and returns the address.
 *
 * @N:         The number of queues to create.
 * @elem_size: The size of each elem that's going into each queue.
 *
 * Returns a pointer to a queuelist object.
 */
queuelist* new_queuelist(int N, size_t elem_size) {
    queuelist* ql = malloc(sizeof(queuelist));

    ql->N = N;

    DEBUG("allocating %d vector pointers\n", N);
    vector** queues = malloc(N * sizeof(vector*));

    DEBUG("initializing %d vectors of size %zu\n", N, elem_size);
    for (int i = 0; i < N; i++)
        queues[i] = new_vector(elem_size);

    ql->queues = queues;

    ql->front = malloc(N * sizeof(int));
    for (int i = 0; i < N; i++)
        ql->front[i] = 0;

    ql->locks = new_locks(N);

    return ql;
}

/**
 * free_queuelist - Frees a queuelist object and all its contents.
 *
 * @ql: A pointer to a queuelist
 */
void free_queuelist(queuelist* ql) {
    int N = ql->N;

    DEBUG("freeing %d vectors\n", N);
    for (int i = 0; i < N; i++)
        free_vector(ql->queues[i]);

    DEBUG("freeing front array\n");
    free(ql->front);

    DEBUG("freeing ql->locks\n");
    free_locks(ql->locks, N);

    DEBUG("freeing allocated memory\n");
    free(ql);
}

/**
 * enqueue - Enqueues an object in the i-th queue of a queuelist.
 *
 * @ql:     a queuelist
 * @i:      denotes the i-th queue to enqueue into
 * @object: a pointer to the object to enqueue
 */
void enqueue(queuelist* ql, int i, void* object) {
    omp_set_lock(ql->locks + i);

    append_to_vector(ql->queues[i], object);

    omp_unset_lock(ql->locks + i);
}

/**
 * dequeue - Dequeues and returns the front-most element in a queuelist.
 *
 * @ql: a queuelist
 * @i:  denotes the i-th queue to dequeue from
 *
 * Returns a pointer to the front-most object. This pointer will be alive as
 * long as the queuelist lives.
 */
void* dequeue(queuelist* ql, int i) {
    omp_set_lock(ql->locks + i);

    void* object;

    vector* v = ql->queues[i];

    /**
     * Make sure we're not dequeueing an empty queue. Returning NULL is a safe
     * cop-out. Do NOT directly return here as the lock has to be freed.
     */
    if (ql->front[i] == v->used) {
        object = NULL;
        goto end;
    }

    object = elem_at(v, ql->front[i]);

    ql->front[i]++;

    /**
     * If this was the last element, clear the entire array.
     */
    if (ql->front[i] == v->used) {
        ql->front[i] = 0;
        v->used = 0;
    }

end:
    omp_unset_lock(ql->locks + i);

    return object;
}

/**
 * is_ql_queue_empty - Checks if a particular queue in a queuelist is empty.
 *
 * @ql: a queuelist
 * @i:  denotes the i-th queue to check
 *
 * Returns 1 if the queue is empty. Returns 0 if there is at least one element
 * in the i-th queue of the queuelist.
 */
int is_ql_queue_empty(queuelist* ql, int i) {
    omp_set_lock(ql->locks + i);

    vector* v = ql->queues[i];
    int result = ql->front[i] == v->used;

    omp_unset_lock(ql->locks + i);

    return result;
}
