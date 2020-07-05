#include <stdlib.h>

#include "locks.h"
#include "utils.h"

/**
 * new_locks - Creates a new set of N OpenMP locks.
 *
 * @N: the number of locks to create
 *
 * Returns a pointer to an array of locks.
 */
omp_lock_t* new_locks(int N) {
    DEBUG("allocating %d locks\n", N);
    omp_lock_t* locks = malloc(N * sizeof(omp_lock_t));

    DEBUG("initializing locks\n");
    for (int i = 0; i < N; i++)
        omp_init_lock(locks+i);

    return locks;
}

/**
 * free_locks - Frees the memory allocated for each of the N locks.
 *
 * @locks: the list of locks
 * @N:     the number of locks that have been allocated
 */
void free_locks(omp_lock_t* locks, int N) {
    DEBUG("destroying %d locks\n", N);
    for(int i = 0; i < N; i++)
        omp_destroy_lock(locks+i);

    DEBUG("freeing up allocated memory\n");
    free(locks);
}
