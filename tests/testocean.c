extern double fabs(double );
typedef int __int32_t;
typedef long long __int64_t;
typedef long unsigned int __darwin_size_t;
typedef long __darwin_time_t;
typedef __int64_t __darwin_off_t;
typedef __int32_t __darwin_suseconds_t;
typedef __darwin_size_t size_t;
struct timeval {
    __darwin_time_t tv_sec;
    __darwin_suseconds_t tv_usec;
} ;
void exit(int );
void *malloc(size_t __size);
int strcmp(const char *__s1, const char *__s2);
extern int omp_get_thread_num(void );
typedef __darwin_off_t fpos_t;
struct __sbuf {
    unsigned char *_base;
    int _size;
} ;
struct __sFILEX ;
struct __sFILE {
    unsigned char *_p;
    int _r;
    int _w;
    short _flags;
    short _file;
    struct __sbuf _bf;
    int _lbfsize;
    void *_cookie;
    int ( *_close )(void *);
    int ( *_read )(void *, char * , int );
    fpos_t ( *_seek )(void *, fpos_t  , int );
    int ( *_write )(void *, const char * , int );
    struct __sbuf _ub;
    struct __sFILEX *_extra;
    int _ur;
    unsigned char _ubuf[3];
    unsigned char _nbuf[1];
    struct __sbuf _lb;
    int _blksize;
    fpos_t _offset;
} ;
typedef struct __sFILE FILE;
extern FILE *__stderrp;
int fprintf(FILE *restrict , const char *restrict , ...);
int printf(const char *restrict , ...);
int scanf(const char *restrict , ...);
int sscanf(const char *restrict , const char *restrict , ...);
int gettimeofday(struct timeval *restrict , void *restrict );
int min(int a, int b) {
    int _imopVarPre142;
    int _imopVarPre143;
    _imopVarPre142 = a <= b;
    if (_imopVarPre142) {
        _imopVarPre143 = a;
    } else {
        _imopVarPre143 = b;
    }
    return _imopVarPre143;
}
int simulate_ocean_currents(double **A, int n , double tol) {
    int done = 0;
    double diff;
    double old;
    int iter = 0;
    double **B;
    double **C;
    unsigned long int _imopVarPre146;
    void *_imopVarPre147;
    _imopVarPre146 = n * sizeof(double *);
    _imopVarPre147 = malloc(_imopVarPre146);
    B = (double **) _imopVarPre147;
    int k;
    for (k = 0; k < n; k++) {
        unsigned long int _imopVarPre150;
        void *_imopVarPre151;
        _imopVarPre150 = n * sizeof(double);
        _imopVarPre151 = malloc(_imopVarPre150);
        B[k] = (double *) _imopVarPre151;
        double *_imopVarPre159;
        unsigned int _imopVarPre160;
        unsigned long int _imopVarPre161;
        double *_imopVarPre162;
        double *_imopVarPre163;
        _imopVarPre159 = B[k];
        _imopVarPre160 = __builtin_object_size(_imopVarPre159, 0);
        _imopVarPre161 = n * sizeof(double);
        _imopVarPre162 = A[k];
        _imopVarPre163 = B[k];
        __builtin___memcpy_chk(_imopVarPre163, _imopVarPre162, _imopVarPre161, _imopVarPre160);
    }
    while (!done) {
        iter++;
        diff = 0;
        int i;
        int j;
        for (i = 1; i < n - 1; ++i) {
            for (j = 1; j < n - 1; ++j) {
                old = A[i][j];
                B[i][j] = (A[i][j] + A[i][j - 1] + A[i - 1][j] + A[i][j + 1] + A[i + 1][j]) / 5.0;
                double _imopVarPre165;
                double _imopVarPre166;
                _imopVarPre165 = B[i][j] - old;
                _imopVarPre166 = fabs(_imopVarPre165);
                diff += _imopVarPre166;
            }
        }
        C = A;
        A = B;
        B = C;
        if (diff / (n * n) < tol) {
            done = 1;
        }
    }
    return iter;
}
int simulate_ocean_currents_parallel(double **A, int dim , double tol , int procs) {
    double **B;
    double **C;
    int chunk = 1 + (dim - 3) / procs;
    int done = 0;
    int iter = 0;
    double diff = 0;
#pragma omp parallel num_threads(procs) shared(A, B, dim)
    {
        void *_imopVarPre170;
        unsigned long int _imopVarPre169;
#pragma omp master
        {
            _imopVarPre169 = dim * sizeof(double *);
            _imopVarPre170 = malloc(_imopVarPre169);
            B = (double **) _imopVarPre170;
        }
// #pragma omp dummyFlush BARRIER_START written([globalCell]) read([globalCell])
#pragma omp barrier
        int _imopVarPre171;
        _imopVarPre171 = omp_get_thread_num();
        int tid = _imopVarPre171;
        int _imopVarPre173;
        int _imopVarPre174;
        _imopVarPre173 = tid * dim / procs;
        _imopVarPre174 = min(dim, _imopVarPre173);
        int start = _imopVarPre174;
        int _imopVarPre176;
        int _imopVarPre177;
        _imopVarPre176 = (tid + 1) * dim / procs;
        _imopVarPre177 = min(dim, _imopVarPre176);
        int end = _imopVarPre177;
        int i;
        for (i = start; i < end; ++i) {
            unsigned long int _imopVarPre180;
            void *_imopVarPre181;
            _imopVarPre180 = dim * sizeof(double);
            _imopVarPre181 = malloc(_imopVarPre180);
            B[i] = (double *) _imopVarPre181;
            double *_imopVarPre189;
            unsigned int _imopVarPre190;
            unsigned long int _imopVarPre191;
            double *_imopVarPre192;
            double *_imopVarPre193;
            _imopVarPre189 = B[i];
            _imopVarPre190 = __builtin_object_size(_imopVarPre189, 0);
            _imopVarPre191 = dim * sizeof(double);
            _imopVarPre192 = A[i];
            _imopVarPre193 = B[i];
            __builtin___memcpy_chk(_imopVarPre193, _imopVarPre192, _imopVarPre191, _imopVarPre190);
        }
    }
#pragma omp parallel num_threads(procs) firstprivate(done)
    {
        int _imopVarPre194;
        _imopVarPre194 = omp_get_thread_num();
        int tid = _imopVarPre194;
        int _imopVarPre198;
        int _imopVarPre199;
        int _imopVarPre200;
        _imopVarPre198 = tid * chunk;
        _imopVarPre199 = dim - 2;
        _imopVarPre200 = min(_imopVarPre199, _imopVarPre198);
        int start = 1 + _imopVarPre200;
        int _imopVarPre204;
        int _imopVarPre205;
        int _imopVarPre206;
        _imopVarPre204 = (tid + 1) * chunk;
        _imopVarPre205 = dim - 2;
        _imopVarPre206 = min(_imopVarPre205, _imopVarPre204);
        int end = 1 + _imopVarPre206;
        double old;
        double mydiff;
        int i;
        int j;
        while (!done) {
            /*A nowait clause was added to this construct to make its barrier explicit.*/
#pragma omp single nowait
            {
                iter++;
            }
            diff = 0;
// #pragma omp dummyFlush BARRIER_START written([globalCell]) read([globalCell])
#pragma omp barrier
            mydiff = 0;
            for (i = start; i < end; ++i) {
                for (j = 1; j < dim - 1; ++j) {
                    old = A[i][j];
                    B[i][j] = (A[i][j] + A[i][j - 1] + A[i - 1][j] + A[i][j + 1] + A[i + 1][j]) / 5.0;
                    double _imopVarPre208;
                    double _imopVarPre209;
                    _imopVarPre208 = B[i][j] - old;
                    _imopVarPre209 = fabs(_imopVarPre208);
                    mydiff += _imopVarPre209;
                }
            }
// #pragma omp dummyFlush ATOMIC_START written([globalCell]) read([diff])
#pragma omp atomic
            diff += mydiff;
// #pragma omp dummyFlush ATOMIC_END written([diff]) read([])
// #pragma omp dummyFlush BARRIER_START written([]) read([C, B, A, dim, diff, tol])
#pragma omp barrier
            done = diff / (dim * dim) < tol;
            /*A nowait clause was added to this construct to make its barrier explicit.*/
#pragma omp single nowait
            {
                C = A;
                A = B;
                B = C;
            }
// #pragma omp dummyFlush BARRIER_START written([C, B, A]) read([globalCell])
            /*This explicit barrier was added as a replacement for some implicit barier.*/
#pragma omp barrier
        }
    }
    return iter;
}
double **read_input(int n) {
    double **X;
    unsigned long int _imopVarPre212;
    void *_imopVarPre213;
    _imopVarPre212 = n * sizeof(double *);
    _imopVarPre213 = malloc(_imopVarPre212);
    X = (double **) _imopVarPre213;
    int i;
    int j;
    for (i = 0; i < n; ++i) {
        unsigned long int _imopVarPre216;
        void *_imopVarPre217;
        _imopVarPre216 = n * sizeof(double);
        _imopVarPre217 = malloc(_imopVarPre216);
        X[i] = (double *) _imopVarPre217;
        for (j = 0; j < n; ++j) {
            double *_imopVarPre219;
            _imopVarPre219 = &X[i][j];
            scanf("%lf", _imopVarPre219);
        }
    }
    return X;
}
void print_output(double **A, int n , int niter) {
    printf("Number of iterations = %d\n", niter);
    int i;
    int j;
    for (i = 0; i < n; ++i) {
        for (j = 0; j < n; ++j) {
            double _imopVarPre221;
            _imopVarPre221 = A[i][j];
            printf("%lf ", _imopVarPre221);
        }
        printf("\n");
    }
    printf("\n");
}
void print_statistics(struct timeval start_time, struct timeval end_time) {
    double _imopVarPre223;
    _imopVarPre223 = start_time.tv_sec + (start_time.tv_usec / 1000000.0);
    printf("Start time:\t%lf \n", _imopVarPre223);
    double _imopVarPre225;
    _imopVarPre225 = end_time.tv_sec + (end_time.tv_usec / 1000000.0);
    printf("End time:\t%lf\n", _imopVarPre225);
    double _imopVarPre227;
    _imopVarPre227 = end_time.tv_sec - start_time.tv_sec + ((end_time.tv_usec - start_time.tv_usec) / 1000000.0);
    printf("Total time: \t%lf (s)\n", _imopVarPre227);
}
void print_usage_and_exit(char *prog) {
    fprintf(__stderrp, "Usage: %s <nprocs> <tol> <-serial|-parallel>\n", prog);
    exit(1);
}
int main(int argc, char **argv) {
    struct timeval start_time;
    struct timeval end_time;
    int num_iter = 0;
    double tol;
    double **A;
    int procs;
    int dim;
    if (argc != 4) {
        char *_imopVarPre229;
        _imopVarPre229 = argv[0];
        print_usage_and_exit(_imopVarPre229);
    }
    int *_imopVarPre232;
    char *_imopVarPre233;
    _imopVarPre232 = &procs;
    _imopVarPre233 = argv[1];
    sscanf(_imopVarPre233, "%d", _imopVarPre232);
    double *_imopVarPre236;
    char *_imopVarPre237;
    _imopVarPre236 = &tol;
    _imopVarPre237 = argv[2];
    sscanf(_imopVarPre237, "%lf", _imopVarPre236);
    char *option = argv[3];
    int _imopVarPre238;
    int _imopVarPre248;
    int _imopVarPre249;
    int _imopVarPre250;
    _imopVarPre238 = option == ((void *) 0);
    if (!_imopVarPre238) {
        _imopVarPre248 = strcmp(option, "-serial");
        _imopVarPre249 = _imopVarPre248 != 0;
        if (_imopVarPre249) {
            _imopVarPre250 = strcmp(option, "-parallel");
            _imopVarPre249 = _imopVarPre250 != 0;
        }
        _imopVarPre238 = _imopVarPre249;
    }
    if (_imopVarPre238) {
        char *_imopVarPre252;
        _imopVarPre252 = argv[0];
        print_usage_and_exit(_imopVarPre252);
    }
    printf("Options: Procs = %d, Tol = %lf, Execution%s\n\n", procs, tol, option);
    int *_imopVarPre254;
    _imopVarPre254 = &dim;
    scanf("%d", _imopVarPre254);
    A = read_input(dim);
    void *_imopVarPre257;
    struct timeval *_imopVarPre258;
    _imopVarPre257 = ((void *) 0);
    _imopVarPre258 = &start_time;
    gettimeofday(_imopVarPre258, _imopVarPre257);
    int _imopVarPre260;
    _imopVarPre260 = strcmp(option, "-serial");
    if (_imopVarPre260 == 0) {
        num_iter = simulate_ocean_currents(A, dim, tol);
    } else {
        num_iter = simulate_ocean_currents_parallel(A, dim, tol, procs);
    }
    void *_imopVarPre263;
    struct timeval *_imopVarPre264;
    _imopVarPre263 = ((void *) 0);
    _imopVarPre264 = &end_time;
    gettimeofday(_imopVarPre264, _imopVarPre263);
    print_output(A, dim, num_iter);
    print_statistics(start_time, end_time);
}
