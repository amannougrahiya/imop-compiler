#ifndef _UTILS_H_
#define _UTILS_H_

#include <stdio.h>
#include <libgen.h>
#include <time.h>

#include "../config.h"

#if defined(LOG_LEVEL) && LOG_LEVEL > 0
#define WARN(fmt, ...)                                                  \
    do {                                                                \
        fprintf(stdout, "[warning] %s:%d:%s(): " fmt,                   \
                basename(__FILE__), __LINE__, __func__, ##__VA_ARGS__); \
    } while (0)
#else
#define WARN(fmt, args...)
#endif

#if defined(LOG_LEVEL) && LOG_LEVEL > 1
#define INFO(fmt, ...)                                                  \
    do {                                                                \
        fprintf(stdout, "[info] %s:%d:%s(): " fmt,                      \
                basename(__FILE__), __LINE__, __func__, ##__VA_ARGS__); \
    } while (0)
#else
#define INFO(fmt, args...)
#endif

#if defined(LOG_LEVEL) && LOG_LEVEL > 2
#define DEBUG(fmt, ...)                                                 \
    do {                                                                \
        fprintf(stdout, "[debug] %s:%d:%s(): " fmt,                     \
                basename(__FILE__), __LINE__, __func__, ##__VA_ARGS__); \
    } while (0)
#else
#define DEBUG(fmt, args...)
#endif

void swap(int*, int*);

int input_through_argv(int, char**);

struct timespec start_time;
void begin_timer();
long long time_elapsed();

#endif // _UTILS_H_
