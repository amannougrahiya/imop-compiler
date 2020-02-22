#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>

int CHUNK_SIZE = 32;
const int MAX_FIB = 48; // for numbers > 47, fib(n) overflows integer range

typedef struct Node {
    long data;
    long fib_data;
    struct Node *next;
} Node;

long fib(long i) {
    long ret = 0, temp = 1;
	long j;
    for (j = 0; j < i; ++j) {
        long t = temp + ret;
        ret = temp;
        temp = t;
    }

    return ret;
}


void update_list(Node *curr) {
    int n = omp_get_num_threads();
    size_t wlsize;
    Node **worklist;
    size_t head = 0, tail = 0;

#pragma omp parallel shared(curr, worklist, head, tail, wlsize)
    {
        int tid = omp_get_thread_num();
        if (tid == 0) {
            wlsize = (omp_get_num_threads()*CHUNK_SIZE);
            worklist = (Node **) malloc(sizeof(Node *)*wlsize);
        }
#pragma omp barrier

        if (tid == 0) {
            Node *buf[CHUNK_SIZE];
            int size;
            while (curr != NULL) {
                size = 0;

                while (curr != NULL && size < CHUNK_SIZE) {
                    buf[size] = curr;
                    curr = curr->next;
                    size++;
                }

                while (tail - head == wlsize) ;

				int i;
                for (i = 0; i < size; ++i)
                    worklist[(i+tail)%wlsize] = buf[i];
                tail += size;
            }
        } else {
            Node *buf[CHUNK_SIZE];
            int size;
            while (curr != NULL || head != tail) {
                size = 0;
#pragma omp critical
                {
                    if (head != tail) {
                        while (tail == head) ;
                        while (head + size < tail && size < CHUNK_SIZE) {
                            buf[size] = worklist[(head+size)%wlsize];
                            size++;
                        }
                        head += size;
                    }
                }

				int i;
                for (i = 0; i < size; ++i)
                    buf[i]->fib_data = fib(buf[i]->data);
            }
        }
    }
    free(worklist);
}

Node *make_node(unsigned int val) {
    Node *n = (Node *) malloc(sizeof (Node));
    n->data = val;
    n->fib_data = -1;
    n->next = NULL;
    return n;
}

void print_list(Node *head) {
    while (head != NULL) {
        printf("%d %ld --> ", head->data, head->fib_data);
        head = head->next;
    }
    printf("\n");
}

int verify_list(Node *curr) {
    while (curr != NULL) {
        if (curr->fib_data != fib(curr->data))
            return -1;
        curr = curr->next;
    }
    return 0;
}

void destroy_list(Node *curr) {
    Node *temp;
    while (curr != NULL) {
        temp = curr;
        curr = curr->next;
        free(temp);
    }
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("Usage: %s NUM_NODES [CHUNK_SIZE]\n", argv[0]);
        return 0;
    }

    unsigned int randval;
    FILE *f;
    f = fopen("/dev/urandom", "r");
    
    size_t N;
    sscanf(argv[1], "%zu", &N);
    
    if (argc >= 3)
        sscanf(argv[2], "%d", &CHUNK_SIZE);

    Node *head = NULL;
    Node **temp = &head;
	size_t i;
    for (i = 0; i < N; ++i) {
        fread(&randval, sizeof(randval), 1, f);
        *temp = make_node(randval%MAX_FIB);
        temp = &(*temp)->next;
    }
    fclose(f);

    struct timespec t1, t2;
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &t1);
    update_list(head);
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &t2);
    long elapsed = (t2.tv_sec - t1.tv_sec)*1e9 + (t2.tv_nsec - t1.tv_nsec);
    printf("CPU time of all threads = %lf sec\n", (double)elapsed/1e9);

    //if (-1 == verify_list(head))
        //printf("List not processed completely\n");

    destroy_list(head);
    return 0;
}
