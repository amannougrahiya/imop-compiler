struct __sFILEX ;
int printf(const char *restrict , ...);
extern double sqrt(double );
void free(void *);
extern char *suboptarg;
struct fssearchblock ;
struct searchstate ;
extern int omp_get_max_threads(void );
void OSCR_init(int numThreads, char *description , char *usage , int numArgs , char *argNames[] , char *defaultValues[] , int numTimers , int numReportedTimers , char *reportedTimerNames[] , int argc , char *argv[]);
extern int OSCR_getarg_int(int ind);
extern double OSCR_getarg_double(int ind);
extern void OSCR_timer_start(int );
extern void OSCR_timer_stop(int );
extern double OSCR_timer_read(int );
extern void OSCR_report();
int n;
int m;
int mits;
double tol;
double relax;
double alpha;
void jacobi(int n, int m , double dx , double dy , double alpha , double omega , double *u , double *f , double tol , int maxit);
void initialize(int n, int m , double alpha , double *dx , double *dy , double *u , double *f) {
    int i;
    int j;
    int xx;
    int yy;
    *dx = 2.0 / (n - 1);
    *dy = 2.0 / (m - 1);
    for (j = 0; j < m; j++) {
        for (i = 0; i < n; i++) {
            xx = -1.0 + *dx * (i - 1);
            yy = -1.0 + *dy * (j - 1);
            u[j * n + i] = 0.0;
            f[j * n + i] = -alpha * (1.0 - xx * xx) * (1.0 - yy * yy) - 2.0 * (1.0 - xx * xx) - 2.0 * (1.0 - yy * yy);
        }
    }
}
void error_check(int n, int m , double alpha , double dx , double dy , double *u , double *f) {
    int i;
    int j;
    double xx;
    double yy;
    double temp;
    double error;
    dx = 2.0 / (n - 1);
    dy = 2.0 / (n - 2);
    error = 0.0;
    for (j = 0; j < m; j++) {
        for (i = 0; i < n; i++) {
            xx = -1.0 + dx * (i - 1);
            yy = -1.0 + dy * (j - 1);
            temp = u[j * n + i] - (1.0 - xx * xx) * (1.0 - yy * yy);
            error += temp * temp;
        }
    }
    double _imopVarPre142;
    _imopVarPre142 = sqrt(error);
    error = _imopVarPre142 / (n * m);
    printf("Solution Error : %g\n", error);
}
int main(int argc, char **argv) {
    double *u;
    double *f;
    double dx;
    double dy;
    double dt;
    double mflops;
    int NUMTHREADS;
    char *PARAM_NAMES[6] = {"Grid dimension: X dir =", "Grid dimension: Y dir =" , "Helmhotlz constant =" , "Successive over-relaxation parameter =" , "error tolerance for iterative solver =" , "Maximum iterations for solver ="};
    char *TIMERS_NAMES[1] = {"Total_time"};
    char *DEFAULT_VALUES[6] = {"5000", "5000" , "0.8" , "1.0" , "1e-7" , "1000"};
    NUMTHREADS = omp_get_max_threads();
    OSCR_init(NUMTHREADS, "Jacobi Solver v1", "Use 'jacobi01' <n> <m> <alpha> <relax> <tol> <mits>", 6, PARAM_NAMES, DEFAULT_VALUES, 1, 1, TIMERS_NAMES, argc, argv);
    n = OSCR_getarg_int(1);
    m = OSCR_getarg_int(2);
    alpha = OSCR_getarg_double(3);
    relax = OSCR_getarg_double(4);
    tol = OSCR_getarg_double(5);
    mits = OSCR_getarg_int(6);
    printf("-> %d, %d, %g, %g, %g, %d\n", n, m, alpha, relax, tol, mits);
    unsigned long int _imopVarPre145;
    int _imopVarPre146;
    _imopVarPre145 = n * m * sizeof(double);
    _imopVarPre146 = OSCR_malloc(_imopVarPre145);
    u = (double *) _imopVarPre146;
    unsigned long int _imopVarPre149;
    int _imopVarPre150;
    _imopVarPre149 = n * m * sizeof(double);
    _imopVarPre150 = OSCR_malloc(_imopVarPre149);
    f = (double *) _imopVarPre150;
    double *_imopVarPre153;
    double *_imopVarPre154;
    _imopVarPre153 = &dy;
    _imopVarPre154 = &dx;
    initialize(n, m, alpha, _imopVarPre154, _imopVarPre153, u, f);
    OSCR_timer_start(0);
    jacobi(n, m, dx, dy, alpha, relax, u, f, tol, mits);
    OSCR_timer_stop(0);
    dt = OSCR_timer_read(0);
    printf(" elapsed time : %12.6f\n", dt);
    mflops = (0.000001 * mits * (m - 2) * (n - 2) * 13) / dt;
    printf(" MFlops       : %12.6g (%d, %d, %d, %g)\n", mflops, mits, m, n, dt);
    error_check(n, m, alpha, dx, dy, u, f);
    OSCR_report(1, TIMERS_NAMES);
    return 0;
}
void jacobi(const int n, const int m , double dx , double dy , double alpha , double omega , double *u , double *f , double tol , int maxit) {
    int i;
    int j;
    int k;
    double error;
    double resid;
    double ax;
    double ay;
    double b;
    double *uold;
    unsigned long int _imopVarPre157;
    int _imopVarPre158;
    _imopVarPre157 = sizeof(double) * n * m;
    _imopVarPre158 = OSCR_malloc(_imopVarPre157);
    uold = (double *) _imopVarPre158;
    ax = 1.0 / (dx * dx);
    ay = 1.0 / (dy * dy);
    b = -2.0 / (dx * dx) - 2.0 / (dy * dy) - alpha;
    error = 10.0 * tol;
    k = 1;
    int _imopVarPre160;
    _imopVarPre160 = k <= maxit;
    if (_imopVarPre160) {
        _imopVarPre160 = error > tol;
    }
    while (_imopVarPre160) {
        error = 0.0;
#pragma omp parallel private(i)
        {
            /*A nowait clause was added to this construct to make its barrier explicit.*/
#pragma omp for nowait
            for (j = 0; j < m; j++) {
                for (i = 0; i < n; i++) {
                    uold[i + m * j] = u[i + m * j];
                }
            }
// #pragma omp dummyFlush BARRIER_START
            /*This explicit barrier was added as a replacement for some implicit barrier.*/
#pragma omp barrier
        }
#pragma omp parallel private(i, resid)
        {
            /*A nowait clause was added to this construct to make its barrier explicit.*/
#pragma omp for reduction(+:error) nowait
            for (j = 1; j < m - 1; j++) {
                for (i = 1; i < n - 1; i++) {
                    resid = (ax * (uold[i - 1 + m * j] + uold[i + 1 + m * j]) + ay * (uold[i + m * (j - 1)] + uold[i + m * (j + 1)]) + b * uold[i + m * j] - f[i + m * j]) / b;
                    u[i + m * j] = uold[i + m * j] - omega * resid;
                    error = error + resid * resid;
                }
            }
// #pragma omp dummyFlush BARRIER_START
            /*This explicit barrier was added as a replacement for some implicit barrier.*/
#pragma omp barrier
        }
        k++;
        double _imopVarPre162;
        _imopVarPre162 = sqrt(error);
        error = _imopVarPre162 / (n * m);
        _imopVarPre160 = k <= maxit;
        if (_imopVarPre160) {
            _imopVarPre160 = error > tol;
        }
    }
    printf("Total Number of Iterations %d\n", k);
    printf("Residual                   %.15f\n\n", error);
    free(uold);
}
