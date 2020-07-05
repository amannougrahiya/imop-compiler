#include <string.h>
#include <time.h>

#include "utils.h"

void swap(int* a, int* b) {
    int t = *a;
    *a = *b;
    *b = t;
}

/**
 * input_through_argv - Checks if the input is to be generated or read in from
 * an input file.
 *
 * @argc: the number of input arguments
 * @argv: the list of input arguments
 *
 * Returns 0 if the input is to be generated. Returns 1 if the input is to be
 * read in from a file and run once. Returns 2 if the input is to be executed
 * multiple times with the average time printed to screen.
 */
int input_through_argv(int argc, char* argv[]) {
    if (argc < 3)
        return 0;

    if (argc == 4 && strcmp(argv[1], "-") == 0)
        return 2;

    return 0;
}

/**
 * begin_timer - Starts a nanosecond precise timer.
 */
void begin_timer() {
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &start_time);
}

/**
 * time_elapsed - Returns the number of nanoseconds elapsed since the begin_timer.
 *
 * Returns a long long of nanoseconds elapsed.
 */
long long time_elapsed() {
    struct timespec end_time;
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &end_time);
    
    long long int s = end_time.tv_sec - start_time.tv_sec;
    long long int ns = end_time.tv_nsec - start_time.tv_nsec;
    return (s * ((long long) 1e9)) + ns;
}
