#ifndef _QUEUES_H_
#define _QUEUES_H_

#include <ctype.h>

#include "vector.h"
#include "locks.h"

typedef struct {
    int N;
    vector** queues;
    int* front;
    omp_lock_t* locks;
} queuelist;

queuelist* new_queuelist(int, size_t);

void free_queuelist(queuelist*);

void enqueue(queuelist*, int, void*);

void* dequeue(queuelist*, int);

int is_ql_queue_empty(queuelist*, int);

#endif  // _QUEUES_H_
