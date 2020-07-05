#ifndef _LOCKS_H_
#define _LOCKS_H_

#include <omp.h>

omp_lock_t* new_locks(int);

void free_locks(omp_lock_t*, int);

#endif  // _LOCKS_H_
