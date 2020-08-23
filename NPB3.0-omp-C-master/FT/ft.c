
/*[]*/
struct __sFILEX ;
/*[]*/
int printf(const char *restrict , ...);
/*[]*/
void exit(int );
/*[]*/
extern double cos(double );
/*[]*/
extern double sin(double );
/*[]*/
extern double exp(double );
/*[]*/
extern double log(double );
/*[]*/
extern double fabs(double );
/*[]*/
typedef int boolean;
/*[]*/
struct stUn_imopVarPre11 {
    double real;
    double imag;
} ;
/*[]*/
typedef struct stUn_imopVarPre11 dcomplex;
/*[]*/
extern double randlc(double *, double );
/*[]*/
extern void vranlc(int , double * , double  , double *);
/*[]*/
extern void timer_clear(int );
/*[]*/
extern void timer_start(int );
/*[]*/
extern void timer_stop(int );
/*[]*/
extern double timer_read(int );
/*[]*/
extern void c_print_results(char *name, char class , int n1 , int n2 , int n3 , int niter , int nthreads , double t , double mops , char *optype , int passed_verification , char *npbversion , char *compiletime , char *cc , char *clink , char *c_lib , char *c_inc , char *cflags , char *clinkflags , char *rand);
/*[]*/
int fftblock;
/*[]*/
int fftblockpad;
/*[]*/
static int dims[3][3];
/*[]*/
static int xstart[3];
/*[]*/
static int ystart[3];
/*[]*/
static int zstart[3];
/*[]*/
static int xend[3];
/*[]*/
static int yend[3];
/*[]*/
static int zend[3];
/*[]*/
static double ex[(6 * (64 * 64 / 4 + 64 * 64 / 4 + 64 * 64 / 4)) + 1];
/*[]*/
static dcomplex u[64];
/*[]*/
static dcomplex sums[6 + 1];
/*[]*/
static int niter;
/*[]*/
static void evolve(dcomplex u0[64][64][64], dcomplex u1[64][64][64] , int t , int indexmap[64][64][64] , int d[3]);
/*[]*/
static void compute_initial_conditions(dcomplex u0[64][64][64], int d[3]);
/*[]*/
static void ipow46(double a, int exponent , double *result);
/*[]*/
static void setup(void );
/*[]*/
static void compute_indexmap(int indexmap[64][64][64], int d[3]);
/*[]*/
static void print_timers(void );
/*[]*/
static void fft(int dir, dcomplex x1[64][64][64] , dcomplex x2[64][64][64]);
/*[]*/
static void cffts1(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]);
/*[]*/
static void cffts2(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]);
/*[]*/
static void cffts3(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]);
/*[]*/
static void fft_init(int n);
/*[]*/
static void cfftz(int is, int m , int n , dcomplex x[64][18] , dcomplex y[64][18]);
/*[]*/
static void fftz2(int is, int l , int m , int n , int ny , int ny1 , dcomplex u[64] , dcomplex x[64][18] , dcomplex y[64][18]);
/*[]*/
static int ilog2(int n);
/*[]*/
static void checksum(int i, dcomplex u1[64][64][64] , int d[3]);
/*[]*/
static void verify(int d1, int d2 , int d3 , int nt , boolean *verified , char *class);
/*[]*/
/*[]*/
/*[]*/
int main(int argc, char **argv) {
/*[]*/
    /*[]*/
    int i;
    /*[]*/
    static dcomplex u0[64][64][64];
    /*[]*/
    static dcomplex u1[64][64][64];
    /*[]*/
    static dcomplex u2[64][64][64];
    /*[]*/
    static int indexmap[64][64][64];
    /*[]*/
    int iter;
    /*[]*/
    int nthreads = 1;
    /*[]*/
    double total_time;
    /*[]*/
    double mflops;
    /*[]*/
    boolean verified;
    /*[]*/
    char class;
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    for (i = 0; i < 7; i++) {
    /*[]*/
        /*[]*/
        timer_clear(i);
        /*[]*/
    }
    /*[]*/
    setup();
    /*[]*/
    /*[]*/
    int ( *_imopVarPre145 );
    /*[]*/
    _imopVarPre145 = dims[2];
    /*[]*/
    int *d_imopVarPre86;
    /*[]*/
    d_imopVarPre86 = _imopVarPre145;
    /*[]*/
    int i_imopVarPre87;
    /*[]*/
    int j_imopVarPre88;
    /*[]*/
    int k_imopVarPre89;
    /*[]*/
    int ii_imopVarPre90;
    /*[]*/
    int ii2_imopVarPre91;
    /*[]*/
    int jj_imopVarPre92;
    /*[]*/
    int ij2_imopVarPre93;
    /*[]*/
    int kk_imopVarPre94;
    /*[]*/
    double ap_imopVarPre95;
    /*[1]*/
#pragma omp parallel default(shared) private(i_imopVarPre87, j_imopVarPre88, k_imopVarPre89, ii_imopVarPre90, ii2_imopVarPre91, jj_imopVarPre92, ij2_imopVarPre93, kk_imopVarPre94)
    {
    /*[1]*/
        /*[1]*/
#pragma omp for nowait
        /*[1]*/
        /*[1]*/
        /*[1]*/
        for (i_imopVarPre87 = 0; i_imopVarPre87 < dims[2][0]; i_imopVarPre87++) {
        /*[1]*/
            /*[1]*/
            ii_imopVarPre90 = (i_imopVarPre87 + 1 + xstart[2] - 2 + 64 / 2) % 64 - 64 / 2;
            /*[1]*/
            ii2_imopVarPre91 = ii_imopVarPre90 * ii_imopVarPre90;
            /*[1]*/
            /*[1]*/
            /*[1]*/
            /*[1]*/
            for (j_imopVarPre88 = 0; j_imopVarPre88 < dims[2][1]; j_imopVarPre88++) {
            /*[1]*/
                /*[1]*/
                jj_imopVarPre92 = (j_imopVarPre88 + 1 + ystart[2] - 2 + 64 / 2) % 64 - 64 / 2;
                /*[1]*/
                ij2_imopVarPre93 = jj_imopVarPre92 * jj_imopVarPre92 + ii2_imopVarPre91;
                /*[1]*/
                /*[1]*/
                /*[1]*/
                /*[1]*/
                for (k_imopVarPre89 = 0; k_imopVarPre89 < dims[2][2]; k_imopVarPre89++) {
                /*[1]*/
                    /*[1]*/
                    kk_imopVarPre94 = (k_imopVarPre89 + 1 + zstart[2] - 2 + 64 / 2) % 64 - 64 / 2;
                    /*[1]*/
                    indexmap[k_imopVarPre89][j_imopVarPre88][i_imopVarPre87] = kk_imopVarPre94 * kk_imopVarPre94 + ij2_imopVarPre93;
                }
            }
        }
    }
    /*[1]*/
    ap_imopVarPre95 = -4.0 * 1.0e-6 * 3.141592653589793238 * 3.141592653589793238;
    /*[1]*/
    ex[0] = 1.0;
    /*[1]*/
    double _imopVarPre217_imopVarPre96;
    /*[1]*/
    _imopVarPre217_imopVarPre96 = exp(ap_imopVarPre95);
    /*[1]*/
    /*[1]*/
    ex[1] = _imopVarPre217_imopVarPre96;
    /*[1]*/
    /*[1]*/
    /*[1]*/
    /*[1]*/
    for (i_imopVarPre87 = 2; i_imopVarPre87 <= (6 * (64 * 64 / 4 + 64 * 64 / 4 + 64 * 64 / 4)); i_imopVarPre87++) {
    /*[1]*/
        /*[1]*/
        ex[i_imopVarPre87] = ex[i_imopVarPre87 - 1] * ex[1];
    }
    /*[]*/
    int ( *_imopVarPre147 );
    /*[]*/
    _imopVarPre147 = dims[0];
    /*[]*/
    compute_initial_conditions(u1, _imopVarPre147);
    /*[]*/
    /*[]*/
    int _imopVarPre149;
    /*[]*/
    _imopVarPre149 = dims[0][0];
    /*[]*/
    fft_init(_imopVarPre149);
    /*[]*/
    /*[]*/
    int dir;
    /*[]*/
    struct stUn_imopVarPre11 ( *x1 )[64][64];
    /*[]*/
    struct stUn_imopVarPre11 ( *x2 )[64][64];
    /*[]*/
    dir = 1;
    /*[]*/
    x1 = u1;
    /*[]*/
    x2 = u0;
    /*[]*/
    dcomplex y0[64][18];
    /*[]*/
    dcomplex y1[64][18];
    /*[]*/
    /*[]*/
    if (dir == 1) {
    /*[]*/
        /*[]*/
        int ( *_imopVarPre225 );
        /*[]*/
        _imopVarPre225 = dims[0];
        /*[]*/
        int is;
        /*[]*/
        int *d;
        /*[]*/
        struct stUn_imopVarPre11 ( *x )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout )[64][64];
        /*[]*/
        is = 1;
        /*[]*/
        d = _imopVarPre225;
        /*[]*/
        x = x1;
        /*[]*/
        xout = x1;
        /*[]*/
        int logd[3];
        /*[2]*/
#pragma omp parallel default(shared) shared(is)
        {
        /*[2]*/
            /*[2]*/
            int i;
            /*[2]*/
            int j;
            /*[2]*/
            int k;
            /*[2]*/
            int jj;
            /*[2]*/
#pragma omp master
            {
            /*[2]*/
                /*[2]*/
                /*[2]*/
                /*[2]*/
                /*[2]*/
                for (i = 0; i < 3; i++) {
                /*[2]*/
                    /*[2]*/
                    int _imopVarPre243;
                    /*[2]*/
                    int _imopVarPre244;
                    /*[2]*/
                    _imopVarPre243 = d[i];
                    /*[2]*/
                    _imopVarPre244 = ilog2(_imopVarPre243);
                    /*[2]*/
                    /*[2]*/
                    logd[i] = _imopVarPre244;
                }
            }
            /*[2]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[2]*/
#pragma omp barrier
            /*[3]*/
            dcomplex y0[64][18];
            /*[3]*/
            dcomplex y1[64][18];
            /*[3]*/
#pragma omp for nowait
            /*[3]*/
            /*[3]*/
            /*[3]*/
            for (k = 0; k < d[2]; k++) {
            /*[3]*/
                /*[3]*/
                /*[3]*/
                /*[3]*/
                /*[3]*/
                for (jj = 0; jj <= d[1] - fftblock; jj += fftblock) {
                /*[3]*/
                    /*[3]*/
                    /*[3]*/
                    /*[3]*/
                    /*[3]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[3]*/
                        /*[3]*/
                        /*[3]*/
                        /*[3]*/
                        /*[3]*/
                        for (i = 0; i < d[0]; i++) {
                        /*[3]*/
                            /*[3]*/
                            y0[i][j].real = x[k][j + jj][i].real;
                            /*[3]*/
                            y0[i][j].imag = x[k][j + jj][i].imag;
                        }
                    }
                    /*[3]*/
                    int _imopVarPre247;
                    /*[3]*/
                    int _imopVarPre248;
                    /*[3]*/
                    _imopVarPre247 = d[0];
                    /*[3]*/
                    _imopVarPre248 = logd[0];
                    /*[3]*/
                    cfftz(is, _imopVarPre248, _imopVarPre247, y0, y1);
                    /*[3]*/
                    /*[3]*/
                    /*[3]*/
                    /*[3]*/
                    /*[3]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[3]*/
                        /*[3]*/
                        /*[3]*/
                        /*[3]*/
                        /*[3]*/
                        for (i = 0; i < d[0]; i++) {
                        /*[3]*/
                            /*[3]*/
                            xout[k][j + jj][i].real = y0[i][j].real;
                            /*[3]*/
                            xout[k][j + jj][i].imag = y0[i][j].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre227 );
        /*[]*/
        _imopVarPre227 = dims[1];
        /*[]*/
        int is_imopVarPre76;
        /*[]*/
        int *d_imopVarPre77;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre78 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre79 )[64][64];
        /*[]*/
        is_imopVarPre76 = 1;
        /*[]*/
        d_imopVarPre77 = _imopVarPre227;
        /*[]*/
        x_imopVarPre78 = x1;
        /*[]*/
        xout_imopVarPre79 = x1;
        /*[]*/
        int logd_imopVarPre80[3];
        /*[4]*/
#pragma omp parallel default(shared) shared(is_imopVarPre76)
        {
        /*[4]*/
            /*[4]*/
            int i;
            /*[4]*/
            int j;
            /*[4]*/
            int k;
            /*[4]*/
            int ii;
            /*[4]*/
#pragma omp master
            {
            /*[4]*/
                /*[4]*/
                /*[4]*/
                /*[4]*/
                /*[4]*/
                for (i = 0; i < 3; i++) {
                /*[4]*/
                    /*[4]*/
                    int _imopVarPre250;
                    /*[4]*/
                    int _imopVarPre251;
                    /*[4]*/
                    _imopVarPre250 = d_imopVarPre77[i];
                    /*[4]*/
                    _imopVarPre251 = ilog2(_imopVarPre250);
                    /*[4]*/
                    /*[4]*/
                    logd_imopVarPre80[i] = _imopVarPre251;
                }
            }
            /*[4]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[4]*/
#pragma omp barrier
            /*[5]*/
            dcomplex y0[64][18];
            /*[5]*/
            dcomplex y1[64][18];
            /*[5]*/
#pragma omp for nowait
            /*[5]*/
            /*[5]*/
            /*[5]*/
            for (k = 0; k < d_imopVarPre77[2]; k++) {
            /*[5]*/
                /*[5]*/
                /*[5]*/
                /*[5]*/
                /*[5]*/
                for (ii = 0; ii <= d_imopVarPre77[0] - fftblock; ii += fftblock) {
                /*[5]*/
                    /*[5]*/
                    /*[5]*/
                    /*[5]*/
                    /*[5]*/
                    for (j = 0; j < d_imopVarPre77[1]; j++) {
                    /*[5]*/
                        /*[5]*/
                        /*[5]*/
                        /*[5]*/
                        /*[5]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[5]*/
                            /*[5]*/
                            y0[j][i].real = x_imopVarPre78[k][j][i + ii].real;
                            /*[5]*/
                            y0[j][i].imag = x_imopVarPre78[k][j][i + ii].imag;
                        }
                    }
                    /*[5]*/
                    int _imopVarPre254;
                    /*[5]*/
                    int _imopVarPre255;
                    /*[5]*/
                    _imopVarPre254 = d_imopVarPre77[1];
                    /*[5]*/
                    _imopVarPre255 = logd_imopVarPre80[1];
                    /*[5]*/
                    cfftz(is_imopVarPre76, _imopVarPre255, _imopVarPre254, y0, y1);
                    /*[5]*/
                    /*[5]*/
                    /*[5]*/
                    /*[5]*/
                    /*[5]*/
                    for (j = 0; j < d_imopVarPre77[1]; j++) {
                    /*[5]*/
                        /*[5]*/
                        /*[5]*/
                        /*[5]*/
                        /*[5]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[5]*/
                            /*[5]*/
                            xout_imopVarPre79[k][j][i + ii].real = y0[j][i].real;
                            /*[5]*/
                            xout_imopVarPre79[k][j][i + ii].imag = y0[j][i].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre229 );
        /*[]*/
        _imopVarPre229 = dims[2];
        /*[]*/
        int is_imopVarPre81;
        /*[]*/
        int *d_imopVarPre82;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre83 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre84 )[64][64];
        /*[]*/
        is_imopVarPre81 = 1;
        /*[]*/
        d_imopVarPre82 = _imopVarPre229;
        /*[]*/
        x_imopVarPre83 = x1;
        /*[]*/
        xout_imopVarPre84 = x2;
        /*[]*/
        int logd_imopVarPre85[3];
        /*[6]*/
#pragma omp parallel default(shared) shared(is_imopVarPre81)
        {
        /*[6]*/
            /*[6]*/
            int i;
            /*[6]*/
            int j;
            /*[6]*/
            int k;
            /*[6]*/
            int ii;
            /*[6]*/
#pragma omp master
            {
            /*[6]*/
                /*[6]*/
                /*[6]*/
                /*[6]*/
                /*[6]*/
                for (i = 0; i < 3; i++) {
                /*[6]*/
                    /*[6]*/
                    int _imopVarPre257;
                    /*[6]*/
                    int _imopVarPre258;
                    /*[6]*/
                    _imopVarPre257 = d_imopVarPre82[i];
                    /*[6]*/
                    _imopVarPre258 = ilog2(_imopVarPre257);
                    /*[6]*/
                    /*[6]*/
                    logd_imopVarPre85[i] = _imopVarPre258;
                }
            }
            /*[6]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[6]*/
#pragma omp barrier
            /*[7]*/
            dcomplex y0[64][18];
            /*[7]*/
            dcomplex y1[64][18];
            /*[7]*/
#pragma omp for nowait
            /*[7]*/
            /*[7]*/
            /*[7]*/
            for (j = 0; j < d_imopVarPre82[1]; j++) {
            /*[7]*/
                /*[7]*/
                /*[7]*/
                /*[7]*/
                /*[7]*/
                for (ii = 0; ii <= d_imopVarPre82[0] - fftblock; ii += fftblock) {
                /*[7]*/
                    /*[7]*/
                    /*[7]*/
                    /*[7]*/
                    /*[7]*/
                    for (k = 0; k < d_imopVarPre82[2]; k++) {
                    /*[7]*/
                        /*[7]*/
                        /*[7]*/
                        /*[7]*/
                        /*[7]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[7]*/
                            /*[7]*/
                            y0[k][i].real = x_imopVarPre83[k][j][i + ii].real;
                            /*[7]*/
                            y0[k][i].imag = x_imopVarPre83[k][j][i + ii].imag;
                        }
                    }
                    /*[7]*/
                    int _imopVarPre261;
                    /*[7]*/
                    int _imopVarPre262;
                    /*[7]*/
                    _imopVarPre261 = d_imopVarPre82[2];
                    /*[7]*/
                    _imopVarPre262 = logd_imopVarPre85[2];
                    /*[7]*/
                    cfftz(is_imopVarPre81, _imopVarPre262, _imopVarPre261, y0, y1);
                    /*[7]*/
                    /*[7]*/
                    /*[7]*/
                    /*[7]*/
                    /*[7]*/
                    for (k = 0; k < d_imopVarPre82[2]; k++) {
                    /*[7]*/
                        /*[7]*/
                        /*[7]*/
                        /*[7]*/
                        /*[7]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[7]*/
                            /*[7]*/
                            xout_imopVarPre84[k][j][i + ii].real = y0[k][i].real;
                            /*[7]*/
                            xout_imopVarPre84[k][j][i + ii].imag = y0[k][i].imag;
                        }
                    }
                }
            }
        }
    } else {
    /*[]*/
        /*[]*/
        int ( *_imopVarPre232 );
        /*[]*/
        int _imopVarPre233;
        /*[]*/
        _imopVarPre232 = dims[2];
        /*[]*/
        _imopVarPre233 = -1;
        /*[]*/
        int is_imopVarPre102;
        /*[]*/
        int *d_imopVarPre103;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre104 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre105 )[64][64];
        /*[]*/
        is_imopVarPre102 = _imopVarPre233;
        /*[]*/
        d_imopVarPre103 = _imopVarPre232;
        /*[]*/
        x_imopVarPre104 = x1;
        /*[]*/
        xout_imopVarPre105 = x1;
        /*[]*/
        int logd_imopVarPre106[3];
        /*[8]*/
#pragma omp parallel default(shared) shared(is_imopVarPre102)
        {
        /*[8]*/
            /*[8]*/
            int i;
            /*[8]*/
            int j;
            /*[8]*/
            int k;
            /*[8]*/
            int ii;
            /*[8]*/
#pragma omp master
            {
            /*[8]*/
                /*[8]*/
                /*[8]*/
                /*[8]*/
                /*[8]*/
                for (i = 0; i < 3; i++) {
                /*[8]*/
                    /*[8]*/
                    int _imopVarPre257;
                    /*[8]*/
                    int _imopVarPre258;
                    /*[8]*/
                    _imopVarPre257 = d_imopVarPre103[i];
                    /*[8]*/
                    _imopVarPre258 = ilog2(_imopVarPre257);
                    /*[8]*/
                    /*[8]*/
                    logd_imopVarPre106[i] = _imopVarPre258;
                }
            }
            /*[8]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[8]*/
#pragma omp barrier
            /*[9]*/
            dcomplex y0[64][18];
            /*[9]*/
            dcomplex y1[64][18];
            /*[9]*/
#pragma omp for nowait
            /*[9]*/
            /*[9]*/
            /*[9]*/
            for (j = 0; j < d_imopVarPre103[1]; j++) {
            /*[9]*/
                /*[9]*/
                /*[9]*/
                /*[9]*/
                /*[9]*/
                for (ii = 0; ii <= d_imopVarPre103[0] - fftblock; ii += fftblock) {
                /*[9]*/
                    /*[9]*/
                    /*[9]*/
                    /*[9]*/
                    /*[9]*/
                    for (k = 0; k < d_imopVarPre103[2]; k++) {
                    /*[9]*/
                        /*[9]*/
                        /*[9]*/
                        /*[9]*/
                        /*[9]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[9]*/
                            /*[9]*/
                            y0[k][i].real = x_imopVarPre104[k][j][i + ii].real;
                            /*[9]*/
                            y0[k][i].imag = x_imopVarPre104[k][j][i + ii].imag;
                        }
                    }
                    /*[9]*/
                    int _imopVarPre261;
                    /*[9]*/
                    int _imopVarPre262;
                    /*[9]*/
                    _imopVarPre261 = d_imopVarPre103[2];
                    /*[9]*/
                    _imopVarPre262 = logd_imopVarPre106[2];
                    /*[9]*/
                    cfftz(is_imopVarPre102, _imopVarPre262, _imopVarPre261, y0, y1);
                    /*[9]*/
                    /*[9]*/
                    /*[9]*/
                    /*[9]*/
                    /*[9]*/
                    for (k = 0; k < d_imopVarPre103[2]; k++) {
                    /*[9]*/
                        /*[9]*/
                        /*[9]*/
                        /*[9]*/
                        /*[9]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[9]*/
                            /*[9]*/
                            xout_imopVarPre105[k][j][i + ii].real = y0[k][i].real;
                            /*[9]*/
                            xout_imopVarPre105[k][j][i + ii].imag = y0[k][i].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre236 );
        /*[]*/
        int _imopVarPre237;
        /*[]*/
        _imopVarPre236 = dims[1];
        /*[]*/
        _imopVarPre237 = -1;
        /*[]*/
        int is;
        /*[]*/
        int *d;
        /*[]*/
        struct stUn_imopVarPre11 ( *x )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout )[64][64];
        /*[]*/
        is = _imopVarPre237;
        /*[]*/
        d = _imopVarPre236;
        /*[]*/
        x = x1;
        /*[]*/
        xout = x1;
        /*[]*/
        int logd[3];
        /*[10]*/
#pragma omp parallel default(shared) shared(is)
        {
        /*[10]*/
            /*[10]*/
            int i;
            /*[10]*/
            int j;
            /*[10]*/
            int k;
            /*[10]*/
            int ii;
            /*[10]*/
#pragma omp master
            {
            /*[10]*/
                /*[10]*/
                /*[10]*/
                /*[10]*/
                /*[10]*/
                for (i = 0; i < 3; i++) {
                /*[10]*/
                    /*[10]*/
                    int _imopVarPre250;
                    /*[10]*/
                    int _imopVarPre251;
                    /*[10]*/
                    _imopVarPre250 = d[i];
                    /*[10]*/
                    _imopVarPre251 = ilog2(_imopVarPre250);
                    /*[10]*/
                    /*[10]*/
                    logd[i] = _imopVarPre251;
                }
            }
            /*[10]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[10]*/
#pragma omp barrier
            /*[11]*/
            dcomplex y0[64][18];
            /*[11]*/
            dcomplex y1[64][18];
            /*[11]*/
#pragma omp for nowait
            /*[11]*/
            /*[11]*/
            /*[11]*/
            for (k = 0; k < d[2]; k++) {
            /*[11]*/
                /*[11]*/
                /*[11]*/
                /*[11]*/
                /*[11]*/
                for (ii = 0; ii <= d[0] - fftblock; ii += fftblock) {
                /*[11]*/
                    /*[11]*/
                    /*[11]*/
                    /*[11]*/
                    /*[11]*/
                    for (j = 0; j < d[1]; j++) {
                    /*[11]*/
                        /*[11]*/
                        /*[11]*/
                        /*[11]*/
                        /*[11]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[11]*/
                            /*[11]*/
                            y0[j][i].real = x[k][j][i + ii].real;
                            /*[11]*/
                            y0[j][i].imag = x[k][j][i + ii].imag;
                        }
                    }
                    /*[11]*/
                    int _imopVarPre254;
                    /*[11]*/
                    int _imopVarPre255;
                    /*[11]*/
                    _imopVarPre254 = d[1];
                    /*[11]*/
                    _imopVarPre255 = logd[1];
                    /*[11]*/
                    cfftz(is, _imopVarPre255, _imopVarPre254, y0, y1);
                    /*[11]*/
                    /*[11]*/
                    /*[11]*/
                    /*[11]*/
                    /*[11]*/
                    for (j = 0; j < d[1]; j++) {
                    /*[11]*/
                        /*[11]*/
                        /*[11]*/
                        /*[11]*/
                        /*[11]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[11]*/
                            /*[11]*/
                            xout[k][j][i + ii].real = y0[j][i].real;
                            /*[11]*/
                            xout[k][j][i + ii].imag = y0[j][i].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre240 );
        /*[]*/
        int _imopVarPre241;
        /*[]*/
        _imopVarPre240 = dims[0];
        /*[]*/
        _imopVarPre241 = -1;
        /*[]*/
        int is_imopVarPre97;
        /*[]*/
        int *d_imopVarPre98;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre99 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre100 )[64][64];
        /*[]*/
        is_imopVarPre97 = _imopVarPre241;
        /*[]*/
        d_imopVarPre98 = _imopVarPre240;
        /*[]*/
        x_imopVarPre99 = x1;
        /*[]*/
        xout_imopVarPre100 = x2;
        /*[]*/
        int logd_imopVarPre101[3];
        /*[12]*/
#pragma omp parallel default(shared) shared(is_imopVarPre97)
        {
        /*[12]*/
            /*[12]*/
            int i;
            /*[12]*/
            int j;
            /*[12]*/
            int k;
            /*[12]*/
            int jj;
            /*[12]*/
#pragma omp master
            {
            /*[12]*/
                /*[12]*/
                /*[12]*/
                /*[12]*/
                /*[12]*/
                for (i = 0; i < 3; i++) {
                /*[12]*/
                    /*[12]*/
                    int _imopVarPre243;
                    /*[12]*/
                    int _imopVarPre244;
                    /*[12]*/
                    _imopVarPre243 = d_imopVarPre98[i];
                    /*[12]*/
                    _imopVarPre244 = ilog2(_imopVarPre243);
                    /*[12]*/
                    /*[12]*/
                    logd_imopVarPre101[i] = _imopVarPre244;
                }
            }
            /*[12]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[12]*/
#pragma omp barrier
            /*[13]*/
            dcomplex y0[64][18];
            /*[13]*/
            dcomplex y1[64][18];
            /*[13]*/
#pragma omp for nowait
            /*[13]*/
            /*[13]*/
            /*[13]*/
            for (k = 0; k < d_imopVarPre98[2]; k++) {
            /*[13]*/
                /*[13]*/
                /*[13]*/
                /*[13]*/
                /*[13]*/
                for (jj = 0; jj <= d_imopVarPre98[1] - fftblock; jj += fftblock) {
                /*[13]*/
                    /*[13]*/
                    /*[13]*/
                    /*[13]*/
                    /*[13]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[13]*/
                        /*[13]*/
                        /*[13]*/
                        /*[13]*/
                        /*[13]*/
                        for (i = 0; i < d_imopVarPre98[0]; i++) {
                        /*[13]*/
                            /*[13]*/
                            y0[i][j].real = x_imopVarPre99[k][j + jj][i].real;
                            /*[13]*/
                            y0[i][j].imag = x_imopVarPre99[k][j + jj][i].imag;
                        }
                    }
                    /*[13]*/
                    int _imopVarPre247;
                    /*[13]*/
                    int _imopVarPre248;
                    /*[13]*/
                    _imopVarPre247 = d_imopVarPre98[0];
                    /*[13]*/
                    _imopVarPre248 = logd_imopVarPre101[0];
                    /*[13]*/
                    cfftz(is_imopVarPre97, _imopVarPre248, _imopVarPre247, y0, y1);
                    /*[13]*/
                    /*[13]*/
                    /*[13]*/
                    /*[13]*/
                    /*[13]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[13]*/
                        /*[13]*/
                        /*[13]*/
                        /*[13]*/
                        /*[13]*/
                        for (i = 0; i < d_imopVarPre98[0]; i++) {
                        /*[13]*/
                            /*[13]*/
                            xout_imopVarPre100[k][j + jj][i].real = y0[i][j].real;
                            /*[13]*/
                            xout_imopVarPre100[k][j + jj][i].imag = y0[i][j].imag;
                        }
                    }
                }
            }
        }
    }
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    for (i = 0; i < 7; i++) {
    /*[]*/
        /*[]*/
        timer_clear(i);
        /*[]*/
    }
    /*[]*/
    timer_start(0);
    /*[]*/
    /*[]*/
    /*[]*/
    if (0 == 1) {
    /*[]*/
        /*[]*/
        timer_start(1);
        /*[]*/
    }
    /*[]*/
    int ( *_imopVarPre151 );
    /*[]*/
    _imopVarPre151 = dims[2];
    /*[]*/
    int *d;
    /*[]*/
    d = _imopVarPre151;
    /*[]*/
    int i_imopVarPre75;
    /*[]*/
    int j;
    /*[]*/
    int k;
    /*[]*/
    int ii;
    /*[]*/
    int ii2;
    /*[]*/
    int jj;
    /*[]*/
    int ij2;
    /*[]*/
    int kk;
    /*[]*/
    double ap;
    /*[14]*/
#pragma omp parallel default(shared) private(i_imopVarPre75, j, k, ii, ii2, jj, ij2, kk)
    {
    /*[14]*/
        /*[14]*/
#pragma omp for nowait
        /*[14]*/
        /*[14]*/
        /*[14]*/
        for (i_imopVarPre75 = 0; i_imopVarPre75 < dims[2][0]; i_imopVarPre75++) {
        /*[14]*/
            /*[14]*/
            ii = (i_imopVarPre75 + 1 + xstart[2] - 2 + 64 / 2) % 64 - 64 / 2;
            /*[14]*/
            ii2 = ii * ii;
            /*[14]*/
            /*[14]*/
            /*[14]*/
            /*[14]*/
            for (j = 0; j < dims[2][1]; j++) {
            /*[14]*/
                /*[14]*/
                jj = (j + 1 + ystart[2] - 2 + 64 / 2) % 64 - 64 / 2;
                /*[14]*/
                ij2 = jj * jj + ii2;
                /*[14]*/
                /*[14]*/
                /*[14]*/
                /*[14]*/
                for (k = 0; k < dims[2][2]; k++) {
                /*[14]*/
                    /*[14]*/
                    kk = (k + 1 + zstart[2] - 2 + 64 / 2) % 64 - 64 / 2;
                    /*[14]*/
                    indexmap[k][j][i_imopVarPre75] = kk * kk + ij2;
                }
            }
        }
    }
    /*[14]*/
    ap = -4.0 * 1.0e-6 * 3.141592653589793238 * 3.141592653589793238;
    /*[14]*/
    ex[0] = 1.0;
    /*[14]*/
    double _imopVarPre217;
    /*[14]*/
    _imopVarPre217 = exp(ap);
    /*[14]*/
    /*[14]*/
    ex[1] = _imopVarPre217;
    /*[14]*/
    /*[14]*/
    /*[14]*/
    /*[14]*/
    for (i_imopVarPre75 = 2; i_imopVarPre75 <= (6 * (64 * 64 / 4 + 64 * 64 / 4 + 64 * 64 / 4)); i_imopVarPre75++) {
    /*[14]*/
        /*[14]*/
        ex[i_imopVarPre75] = ex[i_imopVarPre75 - 1] * ex[1];
    }
    /*[]*/
    int ( *_imopVarPre153 );
    /*[]*/
    _imopVarPre153 = dims[0];
    /*[]*/
    compute_initial_conditions(u1, _imopVarPre153);
    /*[]*/
    /*[]*/
    int _imopVarPre155;
    /*[]*/
    _imopVarPre155 = dims[0][0];
    /*[]*/
    fft_init(_imopVarPre155);
    /*[]*/
    /*[]*/
    /*[]*/
    if (0 == 1) {
    /*[]*/
        /*[]*/
        timer_stop(1);
        /*[]*/
    }
    /*[]*/
    /*[]*/
    if (0 == 1) {
    /*[]*/
        /*[]*/
        timer_start(2);
        /*[]*/
    }
    /*[]*/
    int dir_imopVarPre112;
    /*[]*/
    struct stUn_imopVarPre11 ( *x1_imopVarPre113 )[64][64];
    /*[]*/
    struct stUn_imopVarPre11 ( *x2_imopVarPre114 )[64][64];
    /*[]*/
    dir_imopVarPre112 = 1;
    /*[]*/
    x1_imopVarPre113 = u1;
    /*[]*/
    x2_imopVarPre114 = u0;
    /*[]*/
    dcomplex y0_imopVarPre115[64][18];
    /*[]*/
    dcomplex y1_imopVarPre116[64][18];
    /*[]*/
    /*[]*/
    if (dir_imopVarPre112 == 1) {
    /*[]*/
        /*[]*/
        int ( *_imopVarPre225 );
        /*[]*/
        _imopVarPre225 = dims[0];
        /*[]*/
        int is;
        /*[]*/
        int *d;
        /*[]*/
        struct stUn_imopVarPre11 ( *x )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout )[64][64];
        /*[]*/
        is = 1;
        /*[]*/
        d = _imopVarPre225;
        /*[]*/
        x = x1_imopVarPre113;
        /*[]*/
        xout = x1_imopVarPre113;
        /*[]*/
        int logd[3];
        /*[15]*/
#pragma omp parallel default(shared) shared(is)
        {
        /*[15]*/
            /*[15]*/
            int i;
            /*[15]*/
            int j;
            /*[15]*/
            int k;
            /*[15]*/
            int jj;
            /*[15]*/
#pragma omp master
            {
            /*[15]*/
                /*[15]*/
                /*[15]*/
                /*[15]*/
                /*[15]*/
                for (i = 0; i < 3; i++) {
                /*[15]*/
                    /*[15]*/
                    int _imopVarPre243;
                    /*[15]*/
                    int _imopVarPre244;
                    /*[15]*/
                    _imopVarPre243 = d[i];
                    /*[15]*/
                    _imopVarPre244 = ilog2(_imopVarPre243);
                    /*[15]*/
                    /*[15]*/
                    logd[i] = _imopVarPre244;
                }
            }
            /*[15]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[15]*/
#pragma omp barrier
            /*[16]*/
            dcomplex y0[64][18];
            /*[16]*/
            dcomplex y1[64][18];
            /*[16]*/
#pragma omp for nowait
            /*[16]*/
            /*[16]*/
            /*[16]*/
            for (k = 0; k < d[2]; k++) {
            /*[16]*/
                /*[16]*/
                /*[16]*/
                /*[16]*/
                /*[16]*/
                for (jj = 0; jj <= d[1] - fftblock; jj += fftblock) {
                /*[16]*/
                    /*[16]*/
                    /*[16]*/
                    /*[16]*/
                    /*[16]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[16]*/
                        /*[16]*/
                        /*[16]*/
                        /*[16]*/
                        /*[16]*/
                        for (i = 0; i < d[0]; i++) {
                        /*[16]*/
                            /*[16]*/
                            y0[i][j].real = x[k][j + jj][i].real;
                            /*[16]*/
                            y0[i][j].imag = x[k][j + jj][i].imag;
                        }
                    }
                    /*[16]*/
                    int _imopVarPre247;
                    /*[16]*/
                    int _imopVarPre248;
                    /*[16]*/
                    _imopVarPre247 = d[0];
                    /*[16]*/
                    _imopVarPre248 = logd[0];
                    /*[16]*/
                    cfftz(is, _imopVarPre248, _imopVarPre247, y0, y1);
                    /*[16]*/
                    /*[16]*/
                    /*[16]*/
                    /*[16]*/
                    /*[16]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[16]*/
                        /*[16]*/
                        /*[16]*/
                        /*[16]*/
                        /*[16]*/
                        for (i = 0; i < d[0]; i++) {
                        /*[16]*/
                            /*[16]*/
                            xout[k][j + jj][i].real = y0[i][j].real;
                            /*[16]*/
                            xout[k][j + jj][i].imag = y0[i][j].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre227 );
        /*[]*/
        _imopVarPre227 = dims[1];
        /*[]*/
        int is_imopVarPre76;
        /*[]*/
        int *d_imopVarPre77;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre78 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre79 )[64][64];
        /*[]*/
        is_imopVarPre76 = 1;
        /*[]*/
        d_imopVarPre77 = _imopVarPre227;
        /*[]*/
        x_imopVarPre78 = x1_imopVarPre113;
        /*[]*/
        xout_imopVarPre79 = x1_imopVarPre113;
        /*[]*/
        int logd_imopVarPre80[3];
        /*[17]*/
#pragma omp parallel default(shared) shared(is_imopVarPre76)
        {
        /*[17]*/
            /*[17]*/
            int i;
            /*[17]*/
            int j;
            /*[17]*/
            int k;
            /*[17]*/
            int ii;
            /*[17]*/
#pragma omp master
            {
            /*[17]*/
                /*[17]*/
                /*[17]*/
                /*[17]*/
                /*[17]*/
                for (i = 0; i < 3; i++) {
                /*[17]*/
                    /*[17]*/
                    int _imopVarPre250;
                    /*[17]*/
                    int _imopVarPre251;
                    /*[17]*/
                    _imopVarPre250 = d_imopVarPre77[i];
                    /*[17]*/
                    _imopVarPre251 = ilog2(_imopVarPre250);
                    /*[17]*/
                    /*[17]*/
                    logd_imopVarPre80[i] = _imopVarPre251;
                }
            }
            /*[17]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[17]*/
#pragma omp barrier
            /*[18]*/
            dcomplex y0[64][18];
            /*[18]*/
            dcomplex y1[64][18];
            /*[18]*/
#pragma omp for nowait
            /*[18]*/
            /*[18]*/
            /*[18]*/
            for (k = 0; k < d_imopVarPre77[2]; k++) {
            /*[18]*/
                /*[18]*/
                /*[18]*/
                /*[18]*/
                /*[18]*/
                for (ii = 0; ii <= d_imopVarPre77[0] - fftblock; ii += fftblock) {
                /*[18]*/
                    /*[18]*/
                    /*[18]*/
                    /*[18]*/
                    /*[18]*/
                    for (j = 0; j < d_imopVarPre77[1]; j++) {
                    /*[18]*/
                        /*[18]*/
                        /*[18]*/
                        /*[18]*/
                        /*[18]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[18]*/
                            /*[18]*/
                            y0[j][i].real = x_imopVarPre78[k][j][i + ii].real;
                            /*[18]*/
                            y0[j][i].imag = x_imopVarPre78[k][j][i + ii].imag;
                        }
                    }
                    /*[18]*/
                    int _imopVarPre254;
                    /*[18]*/
                    int _imopVarPre255;
                    /*[18]*/
                    _imopVarPre254 = d_imopVarPre77[1];
                    /*[18]*/
                    _imopVarPre255 = logd_imopVarPre80[1];
                    /*[18]*/
                    cfftz(is_imopVarPre76, _imopVarPre255, _imopVarPre254, y0, y1);
                    /*[18]*/
                    /*[18]*/
                    /*[18]*/
                    /*[18]*/
                    /*[18]*/
                    for (j = 0; j < d_imopVarPre77[1]; j++) {
                    /*[18]*/
                        /*[18]*/
                        /*[18]*/
                        /*[18]*/
                        /*[18]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[18]*/
                            /*[18]*/
                            xout_imopVarPre79[k][j][i + ii].real = y0[j][i].real;
                            /*[18]*/
                            xout_imopVarPre79[k][j][i + ii].imag = y0[j][i].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre229 );
        /*[]*/
        _imopVarPre229 = dims[2];
        /*[]*/
        int is_imopVarPre81;
        /*[]*/
        int *d_imopVarPre82;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre83 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre84 )[64][64];
        /*[]*/
        is_imopVarPre81 = 1;
        /*[]*/
        d_imopVarPre82 = _imopVarPre229;
        /*[]*/
        x_imopVarPre83 = x1_imopVarPre113;
        /*[]*/
        xout_imopVarPre84 = x2_imopVarPre114;
        /*[]*/
        int logd_imopVarPre85[3];
        /*[19]*/
#pragma omp parallel default(shared) shared(is_imopVarPre81)
        {
        /*[19]*/
            /*[19]*/
            int i;
            /*[19]*/
            int j;
            /*[19]*/
            int k;
            /*[19]*/
            int ii;
            /*[19]*/
#pragma omp master
            {
            /*[19]*/
                /*[19]*/
                /*[19]*/
                /*[19]*/
                /*[19]*/
                for (i = 0; i < 3; i++) {
                /*[19]*/
                    /*[19]*/
                    int _imopVarPre257;
                    /*[19]*/
                    int _imopVarPre258;
                    /*[19]*/
                    _imopVarPre257 = d_imopVarPre82[i];
                    /*[19]*/
                    _imopVarPre258 = ilog2(_imopVarPre257);
                    /*[19]*/
                    /*[19]*/
                    logd_imopVarPre85[i] = _imopVarPre258;
                }
            }
            /*[19]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[19]*/
#pragma omp barrier
            /*[20]*/
            dcomplex y0[64][18];
            /*[20]*/
            dcomplex y1[64][18];
            /*[20]*/
#pragma omp for nowait
            /*[20]*/
            /*[20]*/
            /*[20]*/
            for (j = 0; j < d_imopVarPre82[1]; j++) {
            /*[20]*/
                /*[20]*/
                /*[20]*/
                /*[20]*/
                /*[20]*/
                for (ii = 0; ii <= d_imopVarPre82[0] - fftblock; ii += fftblock) {
                /*[20]*/
                    /*[20]*/
                    /*[20]*/
                    /*[20]*/
                    /*[20]*/
                    for (k = 0; k < d_imopVarPre82[2]; k++) {
                    /*[20]*/
                        /*[20]*/
                        /*[20]*/
                        /*[20]*/
                        /*[20]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[20]*/
                            /*[20]*/
                            y0[k][i].real = x_imopVarPre83[k][j][i + ii].real;
                            /*[20]*/
                            y0[k][i].imag = x_imopVarPre83[k][j][i + ii].imag;
                        }
                    }
                    /*[20]*/
                    int _imopVarPre261;
                    /*[20]*/
                    int _imopVarPre262;
                    /*[20]*/
                    _imopVarPre261 = d_imopVarPre82[2];
                    /*[20]*/
                    _imopVarPre262 = logd_imopVarPre85[2];
                    /*[20]*/
                    cfftz(is_imopVarPre81, _imopVarPre262, _imopVarPre261, y0, y1);
                    /*[20]*/
                    /*[20]*/
                    /*[20]*/
                    /*[20]*/
                    /*[20]*/
                    for (k = 0; k < d_imopVarPre82[2]; k++) {
                    /*[20]*/
                        /*[20]*/
                        /*[20]*/
                        /*[20]*/
                        /*[20]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[20]*/
                            /*[20]*/
                            xout_imopVarPre84[k][j][i + ii].real = y0[k][i].real;
                            /*[20]*/
                            xout_imopVarPre84[k][j][i + ii].imag = y0[k][i].imag;
                        }
                    }
                }
            }
        }
    } else {
    /*[]*/
        /*[]*/
        int ( *_imopVarPre232 );
        /*[]*/
        int _imopVarPre233;
        /*[]*/
        _imopVarPre232 = dims[2];
        /*[]*/
        _imopVarPre233 = -1;
        /*[]*/
        int is_imopVarPre102;
        /*[]*/
        int *d_imopVarPre103;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre104 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre105 )[64][64];
        /*[]*/
        is_imopVarPre102 = _imopVarPre233;
        /*[]*/
        d_imopVarPre103 = _imopVarPre232;
        /*[]*/
        x_imopVarPre104 = x1_imopVarPre113;
        /*[]*/
        xout_imopVarPre105 = x1_imopVarPre113;
        /*[]*/
        int logd_imopVarPre106[3];
        /*[21]*/
#pragma omp parallel default(shared) shared(is_imopVarPre102)
        {
        /*[21]*/
            /*[21]*/
            int i;
            /*[21]*/
            int j;
            /*[21]*/
            int k;
            /*[21]*/
            int ii;
            /*[21]*/
#pragma omp master
            {
            /*[21]*/
                /*[21]*/
                /*[21]*/
                /*[21]*/
                /*[21]*/
                for (i = 0; i < 3; i++) {
                /*[21]*/
                    /*[21]*/
                    int _imopVarPre257;
                    /*[21]*/
                    int _imopVarPre258;
                    /*[21]*/
                    _imopVarPre257 = d_imopVarPre103[i];
                    /*[21]*/
                    _imopVarPre258 = ilog2(_imopVarPre257);
                    /*[21]*/
                    /*[21]*/
                    logd_imopVarPre106[i] = _imopVarPre258;
                }
            }
            /*[21]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[21]*/
#pragma omp barrier
            /*[22]*/
            dcomplex y0[64][18];
            /*[22]*/
            dcomplex y1[64][18];
            /*[22]*/
#pragma omp for nowait
            /*[22]*/
            /*[22]*/
            /*[22]*/
            for (j = 0; j < d_imopVarPre103[1]; j++) {
            /*[22]*/
                /*[22]*/
                /*[22]*/
                /*[22]*/
                /*[22]*/
                for (ii = 0; ii <= d_imopVarPre103[0] - fftblock; ii += fftblock) {
                /*[22]*/
                    /*[22]*/
                    /*[22]*/
                    /*[22]*/
                    /*[22]*/
                    for (k = 0; k < d_imopVarPre103[2]; k++) {
                    /*[22]*/
                        /*[22]*/
                        /*[22]*/
                        /*[22]*/
                        /*[22]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[22]*/
                            /*[22]*/
                            y0[k][i].real = x_imopVarPre104[k][j][i + ii].real;
                            /*[22]*/
                            y0[k][i].imag = x_imopVarPre104[k][j][i + ii].imag;
                        }
                    }
                    /*[22]*/
                    int _imopVarPre261;
                    /*[22]*/
                    int _imopVarPre262;
                    /*[22]*/
                    _imopVarPre261 = d_imopVarPre103[2];
                    /*[22]*/
                    _imopVarPre262 = logd_imopVarPre106[2];
                    /*[22]*/
                    cfftz(is_imopVarPre102, _imopVarPre262, _imopVarPre261, y0, y1);
                    /*[22]*/
                    /*[22]*/
                    /*[22]*/
                    /*[22]*/
                    /*[22]*/
                    for (k = 0; k < d_imopVarPre103[2]; k++) {
                    /*[22]*/
                        /*[22]*/
                        /*[22]*/
                        /*[22]*/
                        /*[22]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[22]*/
                            /*[22]*/
                            xout_imopVarPre105[k][j][i + ii].real = y0[k][i].real;
                            /*[22]*/
                            xout_imopVarPre105[k][j][i + ii].imag = y0[k][i].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre236 );
        /*[]*/
        int _imopVarPre237;
        /*[]*/
        _imopVarPre236 = dims[1];
        /*[]*/
        _imopVarPre237 = -1;
        /*[]*/
        int is;
        /*[]*/
        int *d;
        /*[]*/
        struct stUn_imopVarPre11 ( *x )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout )[64][64];
        /*[]*/
        is = _imopVarPre237;
        /*[]*/
        d = _imopVarPre236;
        /*[]*/
        x = x1_imopVarPre113;
        /*[]*/
        xout = x1_imopVarPre113;
        /*[]*/
        int logd[3];
        /*[23]*/
#pragma omp parallel default(shared) shared(is)
        {
        /*[23]*/
            /*[23]*/
            int i;
            /*[23]*/
            int j;
            /*[23]*/
            int k;
            /*[23]*/
            int ii;
            /*[23]*/
#pragma omp master
            {
            /*[23]*/
                /*[23]*/
                /*[23]*/
                /*[23]*/
                /*[23]*/
                for (i = 0; i < 3; i++) {
                /*[23]*/
                    /*[23]*/
                    int _imopVarPre250;
                    /*[23]*/
                    int _imopVarPre251;
                    /*[23]*/
                    _imopVarPre250 = d[i];
                    /*[23]*/
                    _imopVarPre251 = ilog2(_imopVarPre250);
                    /*[23]*/
                    /*[23]*/
                    logd[i] = _imopVarPre251;
                }
            }
            /*[23]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[23]*/
#pragma omp barrier
            /*[24]*/
            dcomplex y0[64][18];
            /*[24]*/
            dcomplex y1[64][18];
            /*[24]*/
#pragma omp for nowait
            /*[24]*/
            /*[24]*/
            /*[24]*/
            for (k = 0; k < d[2]; k++) {
            /*[24]*/
                /*[24]*/
                /*[24]*/
                /*[24]*/
                /*[24]*/
                for (ii = 0; ii <= d[0] - fftblock; ii += fftblock) {
                /*[24]*/
                    /*[24]*/
                    /*[24]*/
                    /*[24]*/
                    /*[24]*/
                    for (j = 0; j < d[1]; j++) {
                    /*[24]*/
                        /*[24]*/
                        /*[24]*/
                        /*[24]*/
                        /*[24]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[24]*/
                            /*[24]*/
                            y0[j][i].real = x[k][j][i + ii].real;
                            /*[24]*/
                            y0[j][i].imag = x[k][j][i + ii].imag;
                        }
                    }
                    /*[24]*/
                    int _imopVarPre254;
                    /*[24]*/
                    int _imopVarPre255;
                    /*[24]*/
                    _imopVarPre254 = d[1];
                    /*[24]*/
                    _imopVarPre255 = logd[1];
                    /*[24]*/
                    cfftz(is, _imopVarPre255, _imopVarPre254, y0, y1);
                    /*[24]*/
                    /*[24]*/
                    /*[24]*/
                    /*[24]*/
                    /*[24]*/
                    for (j = 0; j < d[1]; j++) {
                    /*[24]*/
                        /*[24]*/
                        /*[24]*/
                        /*[24]*/
                        /*[24]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[24]*/
                            /*[24]*/
                            xout[k][j][i + ii].real = y0[j][i].real;
                            /*[24]*/
                            xout[k][j][i + ii].imag = y0[j][i].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre240 );
        /*[]*/
        int _imopVarPre241;
        /*[]*/
        _imopVarPre240 = dims[0];
        /*[]*/
        _imopVarPre241 = -1;
        /*[]*/
        int is_imopVarPre97;
        /*[]*/
        int *d_imopVarPre98;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre99 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre100 )[64][64];
        /*[]*/
        is_imopVarPre97 = _imopVarPre241;
        /*[]*/
        d_imopVarPre98 = _imopVarPre240;
        /*[]*/
        x_imopVarPre99 = x1_imopVarPre113;
        /*[]*/
        xout_imopVarPre100 = x2_imopVarPre114;
        /*[]*/
        int logd_imopVarPre101[3];
        /*[25]*/
#pragma omp parallel default(shared) shared(is_imopVarPre97)
        {
        /*[25]*/
            /*[25]*/
            int i;
            /*[25]*/
            int j;
            /*[25]*/
            int k;
            /*[25]*/
            int jj;
            /*[25]*/
#pragma omp master
            {
            /*[25]*/
                /*[25]*/
                /*[25]*/
                /*[25]*/
                /*[25]*/
                for (i = 0; i < 3; i++) {
                /*[25]*/
                    /*[25]*/
                    int _imopVarPre243;
                    /*[25]*/
                    int _imopVarPre244;
                    /*[25]*/
                    _imopVarPre243 = d_imopVarPre98[i];
                    /*[25]*/
                    _imopVarPre244 = ilog2(_imopVarPre243);
                    /*[25]*/
                    /*[25]*/
                    logd_imopVarPre101[i] = _imopVarPre244;
                }
            }
            /*[25]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[25]*/
#pragma omp barrier
            /*[26]*/
            dcomplex y0[64][18];
            /*[26]*/
            dcomplex y1[64][18];
            /*[26]*/
#pragma omp for nowait
            /*[26]*/
            /*[26]*/
            /*[26]*/
            for (k = 0; k < d_imopVarPre98[2]; k++) {
            /*[26]*/
                /*[26]*/
                /*[26]*/
                /*[26]*/
                /*[26]*/
                for (jj = 0; jj <= d_imopVarPre98[1] - fftblock; jj += fftblock) {
                /*[26]*/
                    /*[26]*/
                    /*[26]*/
                    /*[26]*/
                    /*[26]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[26]*/
                        /*[26]*/
                        /*[26]*/
                        /*[26]*/
                        /*[26]*/
                        for (i = 0; i < d_imopVarPre98[0]; i++) {
                        /*[26]*/
                            /*[26]*/
                            y0[i][j].real = x_imopVarPre99[k][j + jj][i].real;
                            /*[26]*/
                            y0[i][j].imag = x_imopVarPre99[k][j + jj][i].imag;
                        }
                    }
                    /*[26]*/
                    int _imopVarPre247;
                    /*[26]*/
                    int _imopVarPre248;
                    /*[26]*/
                    _imopVarPre247 = d_imopVarPre98[0];
                    /*[26]*/
                    _imopVarPre248 = logd_imopVarPre101[0];
                    /*[26]*/
                    cfftz(is_imopVarPre97, _imopVarPre248, _imopVarPre247, y0, y1);
                    /*[26]*/
                    /*[26]*/
                    /*[26]*/
                    /*[26]*/
                    /*[26]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[26]*/
                        /*[26]*/
                        /*[26]*/
                        /*[26]*/
                        /*[26]*/
                        for (i = 0; i < d_imopVarPre98[0]; i++) {
                        /*[26]*/
                            /*[26]*/
                            xout_imopVarPre100[k][j + jj][i].real = y0[i][j].real;
                            /*[26]*/
                            xout_imopVarPre100[k][j + jj][i].imag = y0[i][j].imag;
                        }
                    }
                }
            }
        }
    }
    /*[]*/
    /*[]*/
    if (0 == 1) {
    /*[]*/
        /*[]*/
        timer_stop(2);
        /*[]*/
    }
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    for (iter = 1; iter <= niter; iter++) {
    /*[]*/
        /*[]*/
        int _imopVarPre159;
        /*[]*/
        int ( *_imopVarPre161 );
        /*[]*/
        int ( *_imopVarPre157 );
        /*[27]*/
#pragma omp parallel
        {
        /*[27]*/
            /*[27]*/
#pragma omp master
            {
            /*[27]*/
                /*[27]*/
                /*[27]*/
                if (0 == 1) {
                /*[27]*/
                    /*[27]*/
                    timer_start(3);
                    /*[27]*/
                }
                /*[27]*/
                _imopVarPre157 = dims[0];
            }
            /*[27]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[27]*/
#pragma omp barrier
            /*[28]*/
            evolve(u0, u1, iter, indexmap, _imopVarPre157);
            /*[28]*/
            /*[28]*/
#pragma omp master
            {
            /*[28]*/
                /*[28]*/
                /*[28]*/
                if (0 == 1) {
                /*[28]*/
                    /*[28]*/
                    timer_stop(3);
                    /*[28]*/
                }
                /*[28]*/
                /*[28]*/
                if (0 == 1) {
                /*[28]*/
                    /*[28]*/
                    timer_start(2);
                    /*[28]*/
                }
                /*[28]*/
                _imopVarPre159 = -1;
            }
        }
        /*[28]*/
        int dir_imopVarPre107;
        /*[28]*/
        struct stUn_imopVarPre11 ( *x1_imopVarPre108 )[64][64];
        /*[28]*/
        struct stUn_imopVarPre11 ( *x2_imopVarPre109 )[64][64];
        /*[28]*/
        dir_imopVarPre107 = _imopVarPre159;
        /*[28]*/
        x1_imopVarPre108 = u1;
        /*[28]*/
        x2_imopVarPre109 = u2;
        /*[28]*/
        dcomplex y0_imopVarPre110[64][18];
        /*[28]*/
        dcomplex y1_imopVarPre111[64][18];
        /*[28]*/
        /*[28]*/
        if (dir_imopVarPre107 == 1) {
        /*[28]*/
            /*[28]*/
            int ( *_imopVarPre225 );
            /*[28]*/
            _imopVarPre225 = dims[0];
            /*[28]*/
            int is;
            /*[28]*/
            int *d;
            /*[28]*/
            struct stUn_imopVarPre11 ( *x )[64][64];
            /*[28]*/
            struct stUn_imopVarPre11 ( *xout )[64][64];
            /*[28]*/
            is = 1;
            /*[28]*/
            d = _imopVarPre225;
            /*[28]*/
            x = x1_imopVarPre108;
            /*[28]*/
            xout = x1_imopVarPre108;
            /*[28]*/
            int logd[3];
            /*[28, 29]*/
#pragma omp parallel default(shared) shared(is)
            {
            /*[28, 29]*/
                /*[28, 29]*/
                int i;
                /*[28, 29]*/
                int j;
                /*[28, 29]*/
                int k;
                /*[28, 29]*/
                int jj;
                /*[28, 29]*/
#pragma omp master
                {
                /*[28, 29]*/
                    /*[28, 29]*/
                    /*[28, 29]*/
                    /*[28, 29]*/
                    /*[28, 29]*/
                    for (i = 0; i < 3; i++) {
                    /*[28, 29]*/
                        /*[28, 29]*/
                        int _imopVarPre243;
                        /*[28, 29]*/
                        int _imopVarPre244;
                        /*[28, 29]*/
                        _imopVarPre243 = d[i];
                        /*[28, 29]*/
                        _imopVarPre244 = ilog2(_imopVarPre243);
                        /*[28, 29]*/
                        /*[28, 29]*/
                        logd[i] = _imopVarPre244;
                    }
                }
                /*[28, 29]*/
                // #pragma omp dummyFlush BARRIER_START
                /*[28, 29]*/
#pragma omp barrier
                /*[28, 30]*/
                dcomplex y0[64][18];
                /*[28, 30]*/
                dcomplex y1[64][18];
                /*[28, 30]*/
#pragma omp for nowait
                /*[28, 30]*/
                /*[28, 30]*/
                /*[28, 30]*/
                for (k = 0; k < d[2]; k++) {
                /*[28, 30]*/
                    /*[28, 30]*/
                    /*[28, 30]*/
                    /*[28, 30]*/
                    /*[28, 30]*/
                    for (jj = 0; jj <= d[1] - fftblock; jj += fftblock) {
                    /*[28, 30]*/
                        /*[28, 30]*/
                        /*[28, 30]*/
                        /*[28, 30]*/
                        /*[28, 30]*/
                        for (j = 0; j < fftblock; j++) {
                        /*[28, 30]*/
                            /*[28, 30]*/
                            /*[28, 30]*/
                            /*[28, 30]*/
                            /*[28, 30]*/
                            for (i = 0; i < d[0]; i++) {
                            /*[28, 30]*/
                                /*[28, 30]*/
                                y0[i][j].real = x[k][j + jj][i].real;
                                /*[28, 30]*/
                                y0[i][j].imag = x[k][j + jj][i].imag;
                            }
                        }
                        /*[28, 30]*/
                        int _imopVarPre247;
                        /*[28, 30]*/
                        int _imopVarPre248;
                        /*[28, 30]*/
                        _imopVarPre247 = d[0];
                        /*[28, 30]*/
                        _imopVarPre248 = logd[0];
                        /*[28, 30]*/
                        cfftz(is, _imopVarPre248, _imopVarPre247, y0, y1);
                        /*[28, 30]*/
                        /*[28, 30]*/
                        /*[28, 30]*/
                        /*[28, 30]*/
                        /*[28, 30]*/
                        for (j = 0; j < fftblock; j++) {
                        /*[28, 30]*/
                            /*[28, 30]*/
                            /*[28, 30]*/
                            /*[28, 30]*/
                            /*[28, 30]*/
                            for (i = 0; i < d[0]; i++) {
                            /*[28, 30]*/
                                /*[28, 30]*/
                                xout[k][j + jj][i].real = y0[i][j].real;
                                /*[28, 30]*/
                                xout[k][j + jj][i].imag = y0[i][j].imag;
                            }
                        }
                    }
                }
            }
            /*[28]*/
            int ( *_imopVarPre227 );
            /*[28]*/
            _imopVarPre227 = dims[1];
            /*[28]*/
            int is_imopVarPre76;
            /*[28]*/
            int *d_imopVarPre77;
            /*[28]*/
            struct stUn_imopVarPre11 ( *x_imopVarPre78 )[64][64];
            /*[28]*/
            struct stUn_imopVarPre11 ( *xout_imopVarPre79 )[64][64];
            /*[28]*/
            is_imopVarPre76 = 1;
            /*[28]*/
            d_imopVarPre77 = _imopVarPre227;
            /*[28]*/
            x_imopVarPre78 = x1_imopVarPre108;
            /*[28]*/
            xout_imopVarPre79 = x1_imopVarPre108;
            /*[28]*/
            int logd_imopVarPre80[3];
            /*[28, 31]*/
#pragma omp parallel default(shared) shared(is_imopVarPre76)
            {
            /*[28, 31]*/
                /*[28, 31]*/
                int i;
                /*[28, 31]*/
                int j;
                /*[28, 31]*/
                int k;
                /*[28, 31]*/
                int ii;
                /*[28, 31]*/
#pragma omp master
                {
                /*[28, 31]*/
                    /*[28, 31]*/
                    /*[28, 31]*/
                    /*[28, 31]*/
                    /*[28, 31]*/
                    for (i = 0; i < 3; i++) {
                    /*[28, 31]*/
                        /*[28, 31]*/
                        int _imopVarPre250;
                        /*[28, 31]*/
                        int _imopVarPre251;
                        /*[28, 31]*/
                        _imopVarPre250 = d_imopVarPre77[i];
                        /*[28, 31]*/
                        _imopVarPre251 = ilog2(_imopVarPre250);
                        /*[28, 31]*/
                        /*[28, 31]*/
                        logd_imopVarPre80[i] = _imopVarPre251;
                    }
                }
                /*[28, 31]*/
                // #pragma omp dummyFlush BARRIER_START
                /*[28, 31]*/
#pragma omp barrier
                /*[28, 32]*/
                dcomplex y0[64][18];
                /*[28, 32]*/
                dcomplex y1[64][18];
                /*[28, 32]*/
#pragma omp for nowait
                /*[28, 32]*/
                /*[28, 32]*/
                /*[28, 32]*/
                for (k = 0; k < d_imopVarPre77[2]; k++) {
                /*[28, 32]*/
                    /*[28, 32]*/
                    /*[28, 32]*/
                    /*[28, 32]*/
                    /*[28, 32]*/
                    for (ii = 0; ii <= d_imopVarPre77[0] - fftblock; ii += fftblock) {
                    /*[28, 32]*/
                        /*[28, 32]*/
                        /*[28, 32]*/
                        /*[28, 32]*/
                        /*[28, 32]*/
                        for (j = 0; j < d_imopVarPre77[1]; j++) {
                        /*[28, 32]*/
                            /*[28, 32]*/
                            /*[28, 32]*/
                            /*[28, 32]*/
                            /*[28, 32]*/
                            for (i = 0; i < fftblock; i++) {
                            /*[28, 32]*/
                                /*[28, 32]*/
                                y0[j][i].real = x_imopVarPre78[k][j][i + ii].real;
                                /*[28, 32]*/
                                y0[j][i].imag = x_imopVarPre78[k][j][i + ii].imag;
                            }
                        }
                        /*[28, 32]*/
                        int _imopVarPre254;
                        /*[28, 32]*/
                        int _imopVarPre255;
                        /*[28, 32]*/
                        _imopVarPre254 = d_imopVarPre77[1];
                        /*[28, 32]*/
                        _imopVarPre255 = logd_imopVarPre80[1];
                        /*[28, 32]*/
                        cfftz(is_imopVarPre76, _imopVarPre255, _imopVarPre254, y0, y1);
                        /*[28, 32]*/
                        /*[28, 32]*/
                        /*[28, 32]*/
                        /*[28, 32]*/
                        /*[28, 32]*/
                        for (j = 0; j < d_imopVarPre77[1]; j++) {
                        /*[28, 32]*/
                            /*[28, 32]*/
                            /*[28, 32]*/
                            /*[28, 32]*/
                            /*[28, 32]*/
                            for (i = 0; i < fftblock; i++) {
                            /*[28, 32]*/
                                /*[28, 32]*/
                                xout_imopVarPre79[k][j][i + ii].real = y0[j][i].real;
                                /*[28, 32]*/
                                xout_imopVarPre79[k][j][i + ii].imag = y0[j][i].imag;
                            }
                        }
                    }
                }
            }
            /*[28]*/
            int ( *_imopVarPre229 );
            /*[28]*/
            _imopVarPre229 = dims[2];
            /*[28]*/
            int is_imopVarPre81;
            /*[28]*/
            int *d_imopVarPre82;
            /*[28]*/
            struct stUn_imopVarPre11 ( *x_imopVarPre83 )[64][64];
            /*[28]*/
            struct stUn_imopVarPre11 ( *xout_imopVarPre84 )[64][64];
            /*[28]*/
            is_imopVarPre81 = 1;
            /*[28]*/
            d_imopVarPre82 = _imopVarPre229;
            /*[28]*/
            x_imopVarPre83 = x1_imopVarPre108;
            /*[28]*/
            xout_imopVarPre84 = x2_imopVarPre109;
            /*[28]*/
            int logd_imopVarPre85[3];
            /*[28, 33]*/
#pragma omp parallel default(shared) shared(is_imopVarPre81)
            {
            /*[28, 33]*/
                /*[28, 33]*/
                int i;
                /*[28, 33]*/
                int j;
                /*[28, 33]*/
                int k;
                /*[28, 33]*/
                int ii;
                /*[28, 33]*/
#pragma omp master
                {
                /*[28, 33]*/
                    /*[28, 33]*/
                    /*[28, 33]*/
                    /*[28, 33]*/
                    /*[28, 33]*/
                    for (i = 0; i < 3; i++) {
                    /*[28, 33]*/
                        /*[28, 33]*/
                        int _imopVarPre257;
                        /*[28, 33]*/
                        int _imopVarPre258;
                        /*[28, 33]*/
                        _imopVarPre257 = d_imopVarPre82[i];
                        /*[28, 33]*/
                        _imopVarPre258 = ilog2(_imopVarPre257);
                        /*[28, 33]*/
                        /*[28, 33]*/
                        logd_imopVarPre85[i] = _imopVarPre258;
                    }
                }
                /*[28, 33]*/
                // #pragma omp dummyFlush BARRIER_START
                /*[28, 33]*/
#pragma omp barrier
                /*[28, 34]*/
                dcomplex y0[64][18];
                /*[28, 34]*/
                dcomplex y1[64][18];
                /*[28, 34]*/
#pragma omp for nowait
                /*[28, 34]*/
                /*[28, 34]*/
                /*[28, 34]*/
                for (j = 0; j < d_imopVarPre82[1]; j++) {
                /*[28, 34]*/
                    /*[28, 34]*/
                    /*[28, 34]*/
                    /*[28, 34]*/
                    /*[28, 34]*/
                    for (ii = 0; ii <= d_imopVarPre82[0] - fftblock; ii += fftblock) {
                    /*[28, 34]*/
                        /*[28, 34]*/
                        /*[28, 34]*/
                        /*[28, 34]*/
                        /*[28, 34]*/
                        for (k = 0; k < d_imopVarPre82[2]; k++) {
                        /*[28, 34]*/
                            /*[28, 34]*/
                            /*[28, 34]*/
                            /*[28, 34]*/
                            /*[28, 34]*/
                            for (i = 0; i < fftblock; i++) {
                            /*[28, 34]*/
                                /*[28, 34]*/
                                y0[k][i].real = x_imopVarPre83[k][j][i + ii].real;
                                /*[28, 34]*/
                                y0[k][i].imag = x_imopVarPre83[k][j][i + ii].imag;
                            }
                        }
                        /*[28, 34]*/
                        int _imopVarPre261;
                        /*[28, 34]*/
                        int _imopVarPre262;
                        /*[28, 34]*/
                        _imopVarPre261 = d_imopVarPre82[2];
                        /*[28, 34]*/
                        _imopVarPre262 = logd_imopVarPre85[2];
                        /*[28, 34]*/
                        cfftz(is_imopVarPre81, _imopVarPre262, _imopVarPre261, y0, y1);
                        /*[28, 34]*/
                        /*[28, 34]*/
                        /*[28, 34]*/
                        /*[28, 34]*/
                        /*[28, 34]*/
                        for (k = 0; k < d_imopVarPre82[2]; k++) {
                        /*[28, 34]*/
                            /*[28, 34]*/
                            /*[28, 34]*/
                            /*[28, 34]*/
                            /*[28, 34]*/
                            for (i = 0; i < fftblock; i++) {
                            /*[28, 34]*/
                                /*[28, 34]*/
                                xout_imopVarPre84[k][j][i + ii].real = y0[k][i].real;
                                /*[28, 34]*/
                                xout_imopVarPre84[k][j][i + ii].imag = y0[k][i].imag;
                            }
                        }
                    }
                }
            }
        } else {
        /*[28]*/
            /*[28]*/
            int ( *_imopVarPre232 );
            /*[28]*/
            int _imopVarPre233;
            /*[28]*/
            _imopVarPre232 = dims[2];
            /*[28]*/
            _imopVarPre233 = -1;
            /*[28]*/
            int is_imopVarPre102;
            /*[28]*/
            int *d_imopVarPre103;
            /*[28]*/
            struct stUn_imopVarPre11 ( *x_imopVarPre104 )[64][64];
            /*[28]*/
            struct stUn_imopVarPre11 ( *xout_imopVarPre105 )[64][64];
            /*[28]*/
            is_imopVarPre102 = _imopVarPre233;
            /*[28]*/
            d_imopVarPre103 = _imopVarPre232;
            /*[28]*/
            x_imopVarPre104 = x1_imopVarPre108;
            /*[28]*/
            xout_imopVarPre105 = x1_imopVarPre108;
            /*[28]*/
            int logd_imopVarPre106[3];
            /*[28, 35]*/
#pragma omp parallel default(shared) shared(is_imopVarPre102)
            {
            /*[28, 35]*/
                /*[28, 35]*/
                int i;
                /*[28, 35]*/
                int j;
                /*[28, 35]*/
                int k;
                /*[28, 35]*/
                int ii;
                /*[28, 35]*/
#pragma omp master
                {
                /*[28, 35]*/
                    /*[28, 35]*/
                    /*[28, 35]*/
                    /*[28, 35]*/
                    /*[28, 35]*/
                    for (i = 0; i < 3; i++) {
                    /*[28, 35]*/
                        /*[28, 35]*/
                        int _imopVarPre257;
                        /*[28, 35]*/
                        int _imopVarPre258;
                        /*[28, 35]*/
                        _imopVarPre257 = d_imopVarPre103[i];
                        /*[28, 35]*/
                        _imopVarPre258 = ilog2(_imopVarPre257);
                        /*[28, 35]*/
                        /*[28, 35]*/
                        logd_imopVarPre106[i] = _imopVarPre258;
                    }
                }
                /*[28, 35]*/
                // #pragma omp dummyFlush BARRIER_START
                /*[28, 35]*/
#pragma omp barrier
                /*[28, 36]*/
                dcomplex y0[64][18];
                /*[28, 36]*/
                dcomplex y1[64][18];
                /*[28, 36]*/
#pragma omp for nowait
                /*[28, 36]*/
                /*[28, 36]*/
                /*[28, 36]*/
                for (j = 0; j < d_imopVarPre103[1]; j++) {
                /*[28, 36]*/
                    /*[28, 36]*/
                    /*[28, 36]*/
                    /*[28, 36]*/
                    /*[28, 36]*/
                    for (ii = 0; ii <= d_imopVarPre103[0] - fftblock; ii += fftblock) {
                    /*[28, 36]*/
                        /*[28, 36]*/
                        /*[28, 36]*/
                        /*[28, 36]*/
                        /*[28, 36]*/
                        for (k = 0; k < d_imopVarPre103[2]; k++) {
                        /*[28, 36]*/
                            /*[28, 36]*/
                            /*[28, 36]*/
                            /*[28, 36]*/
                            /*[28, 36]*/
                            for (i = 0; i < fftblock; i++) {
                            /*[28, 36]*/
                                /*[28, 36]*/
                                y0[k][i].real = x_imopVarPre104[k][j][i + ii].real;
                                /*[28, 36]*/
                                y0[k][i].imag = x_imopVarPre104[k][j][i + ii].imag;
                            }
                        }
                        /*[28, 36]*/
                        int _imopVarPre261;
                        /*[28, 36]*/
                        int _imopVarPre262;
                        /*[28, 36]*/
                        _imopVarPre261 = d_imopVarPre103[2];
                        /*[28, 36]*/
                        _imopVarPre262 = logd_imopVarPre106[2];
                        /*[28, 36]*/
                        cfftz(is_imopVarPre102, _imopVarPre262, _imopVarPre261, y0, y1);
                        /*[28, 36]*/
                        /*[28, 36]*/
                        /*[28, 36]*/
                        /*[28, 36]*/
                        /*[28, 36]*/
                        for (k = 0; k < d_imopVarPre103[2]; k++) {
                        /*[28, 36]*/
                            /*[28, 36]*/
                            /*[28, 36]*/
                            /*[28, 36]*/
                            /*[28, 36]*/
                            for (i = 0; i < fftblock; i++) {
                            /*[28, 36]*/
                                /*[28, 36]*/
                                xout_imopVarPre105[k][j][i + ii].real = y0[k][i].real;
                                /*[28, 36]*/
                                xout_imopVarPre105[k][j][i + ii].imag = y0[k][i].imag;
                            }
                        }
                    }
                }
            }
            /*[28]*/
            int ( *_imopVarPre236 );
            /*[28]*/
            int _imopVarPre237;
            /*[28]*/
            _imopVarPre236 = dims[1];
            /*[28]*/
            _imopVarPre237 = -1;
            /*[28]*/
            int is;
            /*[28]*/
            int *d;
            /*[28]*/
            struct stUn_imopVarPre11 ( *x )[64][64];
            /*[28]*/
            struct stUn_imopVarPre11 ( *xout )[64][64];
            /*[28]*/
            is = _imopVarPre237;
            /*[28]*/
            d = _imopVarPre236;
            /*[28]*/
            x = x1_imopVarPre108;
            /*[28]*/
            xout = x1_imopVarPre108;
            /*[28]*/
            int logd[3];
            /*[28, 37]*/
#pragma omp parallel default(shared) shared(is)
            {
            /*[28, 37]*/
                /*[28, 37]*/
                int i;
                /*[28, 37]*/
                int j;
                /*[28, 37]*/
                int k;
                /*[28, 37]*/
                int ii;
                /*[28, 37]*/
#pragma omp master
                {
                /*[28, 37]*/
                    /*[28, 37]*/
                    /*[28, 37]*/
                    /*[28, 37]*/
                    /*[28, 37]*/
                    for (i = 0; i < 3; i++) {
                    /*[28, 37]*/
                        /*[28, 37]*/
                        int _imopVarPre250;
                        /*[28, 37]*/
                        int _imopVarPre251;
                        /*[28, 37]*/
                        _imopVarPre250 = d[i];
                        /*[28, 37]*/
                        _imopVarPre251 = ilog2(_imopVarPre250);
                        /*[28, 37]*/
                        /*[28, 37]*/
                        logd[i] = _imopVarPre251;
                    }
                }
                /*[28, 37]*/
                // #pragma omp dummyFlush BARRIER_START
                /*[28, 37]*/
#pragma omp barrier
                /*[28, 38]*/
                dcomplex y0[64][18];
                /*[28, 38]*/
                dcomplex y1[64][18];
                /*[28, 38]*/
#pragma omp for nowait
                /*[28, 38]*/
                /*[28, 38]*/
                /*[28, 38]*/
                for (k = 0; k < d[2]; k++) {
                /*[28, 38]*/
                    /*[28, 38]*/
                    /*[28, 38]*/
                    /*[28, 38]*/
                    /*[28, 38]*/
                    for (ii = 0; ii <= d[0] - fftblock; ii += fftblock) {
                    /*[28, 38]*/
                        /*[28, 38]*/
                        /*[28, 38]*/
                        /*[28, 38]*/
                        /*[28, 38]*/
                        for (j = 0; j < d[1]; j++) {
                        /*[28, 38]*/
                            /*[28, 38]*/
                            /*[28, 38]*/
                            /*[28, 38]*/
                            /*[28, 38]*/
                            for (i = 0; i < fftblock; i++) {
                            /*[28, 38]*/
                                /*[28, 38]*/
                                y0[j][i].real = x[k][j][i + ii].real;
                                /*[28, 38]*/
                                y0[j][i].imag = x[k][j][i + ii].imag;
                            }
                        }
                        /*[28, 38]*/
                        int _imopVarPre254;
                        /*[28, 38]*/
                        int _imopVarPre255;
                        /*[28, 38]*/
                        _imopVarPre254 = d[1];
                        /*[28, 38]*/
                        _imopVarPre255 = logd[1];
                        /*[28, 38]*/
                        cfftz(is, _imopVarPre255, _imopVarPre254, y0, y1);
                        /*[28, 38]*/
                        /*[28, 38]*/
                        /*[28, 38]*/
                        /*[28, 38]*/
                        /*[28, 38]*/
                        for (j = 0; j < d[1]; j++) {
                        /*[28, 38]*/
                            /*[28, 38]*/
                            /*[28, 38]*/
                            /*[28, 38]*/
                            /*[28, 38]*/
                            for (i = 0; i < fftblock; i++) {
                            /*[28, 38]*/
                                /*[28, 38]*/
                                xout[k][j][i + ii].real = y0[j][i].real;
                                /*[28, 38]*/
                                xout[k][j][i + ii].imag = y0[j][i].imag;
                            }
                        }
                    }
                }
            }
            /*[28]*/
            int ( *_imopVarPre240 );
            /*[28]*/
            int _imopVarPre241;
            /*[28]*/
            _imopVarPre240 = dims[0];
            /*[28]*/
            _imopVarPre241 = -1;
            /*[28]*/
            int is_imopVarPre97;
            /*[28]*/
            int *d_imopVarPre98;
            /*[28]*/
            struct stUn_imopVarPre11 ( *x_imopVarPre99 )[64][64];
            /*[28]*/
            struct stUn_imopVarPre11 ( *xout_imopVarPre100 )[64][64];
            /*[28]*/
            is_imopVarPre97 = _imopVarPre241;
            /*[28]*/
            d_imopVarPre98 = _imopVarPre240;
            /*[28]*/
            x_imopVarPre99 = x1_imopVarPre108;
            /*[28]*/
            xout_imopVarPre100 = x2_imopVarPre109;
            /*[28]*/
            int logd_imopVarPre101[3];
            /*[28, 39]*/
#pragma omp parallel default(shared) shared(is_imopVarPre97)
            {
            /*[28, 39]*/
                /*[28, 39]*/
                int i;
                /*[28, 39]*/
                int j;
                /*[28, 39]*/
                int k;
                /*[28, 39]*/
                int jj;
                /*[28, 39]*/
#pragma omp master
                {
                /*[28, 39]*/
                    /*[28, 39]*/
                    /*[28, 39]*/
                    /*[28, 39]*/
                    /*[28, 39]*/
                    for (i = 0; i < 3; i++) {
                    /*[28, 39]*/
                        /*[28, 39]*/
                        int _imopVarPre243;
                        /*[28, 39]*/
                        int _imopVarPre244;
                        /*[28, 39]*/
                        _imopVarPre243 = d_imopVarPre98[i];
                        /*[28, 39]*/
                        _imopVarPre244 = ilog2(_imopVarPre243);
                        /*[28, 39]*/
                        /*[28, 39]*/
                        logd_imopVarPre101[i] = _imopVarPre244;
                    }
                }
                /*[28, 39]*/
                // #pragma omp dummyFlush BARRIER_START
                /*[28, 39]*/
#pragma omp barrier
                /*[28, 40]*/
                dcomplex y0[64][18];
                /*[28, 40]*/
                dcomplex y1[64][18];
                /*[28, 40]*/
#pragma omp for nowait
                /*[28, 40]*/
                /*[28, 40]*/
                /*[28, 40]*/
                for (k = 0; k < d_imopVarPre98[2]; k++) {
                /*[28, 40]*/
                    /*[28, 40]*/
                    /*[28, 40]*/
                    /*[28, 40]*/
                    /*[28, 40]*/
                    for (jj = 0; jj <= d_imopVarPre98[1] - fftblock; jj += fftblock) {
                    /*[28, 40]*/
                        /*[28, 40]*/
                        /*[28, 40]*/
                        /*[28, 40]*/
                        /*[28, 40]*/
                        for (j = 0; j < fftblock; j++) {
                        /*[28, 40]*/
                            /*[28, 40]*/
                            /*[28, 40]*/
                            /*[28, 40]*/
                            /*[28, 40]*/
                            for (i = 0; i < d_imopVarPre98[0]; i++) {
                            /*[28, 40]*/
                                /*[28, 40]*/
                                y0[i][j].real = x_imopVarPre99[k][j + jj][i].real;
                                /*[28, 40]*/
                                y0[i][j].imag = x_imopVarPre99[k][j + jj][i].imag;
                            }
                        }
                        /*[28, 40]*/
                        int _imopVarPre247;
                        /*[28, 40]*/
                        int _imopVarPre248;
                        /*[28, 40]*/
                        _imopVarPre247 = d_imopVarPre98[0];
                        /*[28, 40]*/
                        _imopVarPre248 = logd_imopVarPre101[0];
                        /*[28, 40]*/
                        cfftz(is_imopVarPre97, _imopVarPre248, _imopVarPre247, y0, y1);
                        /*[28, 40]*/
                        /*[28, 40]*/
                        /*[28, 40]*/
                        /*[28, 40]*/
                        /*[28, 40]*/
                        for (j = 0; j < fftblock; j++) {
                        /*[28, 40]*/
                            /*[28, 40]*/
                            /*[28, 40]*/
                            /*[28, 40]*/
                            /*[28, 40]*/
                            for (i = 0; i < d_imopVarPre98[0]; i++) {
                            /*[28, 40]*/
                                /*[28, 40]*/
                                xout_imopVarPre100[k][j + jj][i].real = y0[i][j].real;
                                /*[28, 40]*/
                                xout_imopVarPre100[k][j + jj][i].imag = y0[i][j].imag;
                            }
                        }
                    }
                }
            }
        }
        /*[41]*/
#pragma omp parallel
        {
        /*[41]*/
            /*[41]*/
#pragma omp master
            {
            /*[41]*/
                /*[41]*/
                /*[41]*/
                if (0 == 1) {
                /*[41]*/
                    /*[41]*/
                    timer_stop(2);
                    /*[41]*/
                }
                /*[41]*/
                /*[41]*/
                if (0 == 1) {
                /*[41]*/
                    /*[41]*/
                    timer_start(4);
                    /*[41]*/
                }
                /*[41]*/
                _imopVarPre161 = dims[0];
            }
            /*[41]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[41]*/
#pragma omp barrier
            /*[42]*/
            int i_imopVarPre117;
            /*[42]*/
            struct stUn_imopVarPre11 ( *u1_imopVarPre118 )[64][64];
            /*[42]*/
            int *d_imopVarPre119;
            /*[42]*/
            i_imopVarPre117 = iter;
            /*[42]*/
            u1_imopVarPre118 = u2;
            /*[42]*/
            d_imopVarPre119 = _imopVarPre161;
            /*[42]*/
            int j_imopVarPre120;
            /*[42]*/
            int q;
            /*[42]*/
            int r;
            /*[42]*/
            int s;
            /*[42]*/
            dcomplex chk;
            /*[42]*/
            chk.real = 0.0;
            /*[42]*/
            chk.imag = 0.0;
            /*[42]*/
#pragma omp for nowait
            /*[42]*/
            /*[42]*/
            /*[42]*/
            for (j_imopVarPre120 = 1; j_imopVarPre120 <= 1024; j_imopVarPre120++) {
            /*[42]*/
                /*[42]*/
                q = j_imopVarPre120 % 64 + 1;
                /*[42]*/
                int _imopVarPre272;
                /*[42]*/
                _imopVarPre272 = q >= xstart[0];
                /*[42]*/
                /*[42]*/
                if (_imopVarPre272) {
                /*[42]*/
                    /*[42]*/
                    _imopVarPre272 = q <= xend[0];
                }
                /*[42]*/
                /*[42]*/
                if (_imopVarPre272) {
                /*[42]*/
                    /*[42]*/
                    r = (3 * j_imopVarPre120) % 64 + 1;
                    /*[42]*/
                    int _imopVarPre274;
                    /*[42]*/
                    _imopVarPre274 = r >= ystart[0];
                    /*[42]*/
                    /*[42]*/
                    if (_imopVarPre274) {
                    /*[42]*/
                        /*[42]*/
                        _imopVarPre274 = r <= yend[0];
                    }
                    /*[42]*/
                    /*[42]*/
                    if (_imopVarPre274) {
                    /*[42]*/
                        /*[42]*/
                        s = (5 * j_imopVarPre120) % 64 + 1;
                        /*[42]*/
                        int _imopVarPre276;
                        /*[42]*/
                        _imopVarPre276 = s >= zstart[0];
                        /*[42]*/
                        /*[42]*/
                        if (_imopVarPre276) {
                        /*[42]*/
                            /*[42]*/
                            _imopVarPre276 = s <= zend[0];
                        }
                        /*[42]*/
                        /*[42]*/
                        if (_imopVarPre276) {
                        /*[42]*/
                            /*[42]*/
                            chk.real = chk.real + u1_imopVarPre118[s - zstart[0]][r - ystart[0]][q - xstart[0]].real;
                            /*[42]*/
                            (chk.imag = chk.imag + u1_imopVarPre118[s - zstart[0]][r - ystart[0]][q - xstart[0]].imag);
                        }
                    }
                }
            }
            /*[42]*/
            // #pragma omp dummyFlush CRITICAL_START
            /*[42]*/
#pragma omp critical
            {
            /*[42]*/
                /*[42]*/
                sums[i_imopVarPre117].real += chk.real;
                /*[42]*/
                sums[i_imopVarPre117].imag += chk.imag;
            }
            /*[42]*/
            // #pragma omp dummyFlush CRITICAL_END
            /*[42]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[42]*/
#pragma omp barrier
            /*[43]*/
#pragma omp single nowait
            {
            /*[43]*/
                /*[43]*/
                sums[i_imopVarPre117].real = sums[i_imopVarPre117].real / (double) 262144;
                /*[43]*/
                sums[i_imopVarPre117].imag = sums[i_imopVarPre117].imag / (double) 262144;
                /*[43]*/
                double _imopVarPre279;
                /*[43]*/
                double _imopVarPre280;
                /*[43]*/
                _imopVarPre279 = sums[i_imopVarPre117].imag;
                /*[43]*/
                _imopVarPre280 = sums[i_imopVarPre117].real;
                /*[43]*/
                printf("T = %5d     Checksum = %22.12e %22.12e\n", i_imopVarPre117, _imopVarPre280, _imopVarPre279);
                /*[43]*/
            }
            /*[43]*/
#pragma omp master
            {
            /*[43]*/
                /*[43]*/
                /*[43]*/
                if (0 == 1) {
                /*[43]*/
                    /*[43]*/
                    timer_stop(4);
                    /*[43]*/
                }
            }
        }
    }
    /*[]*/
    char *_imopVarPre164;
    /*[]*/
    int *_imopVarPre165;
    /*[]*/
    _imopVarPre164 = &class;
    /*[]*/
    _imopVarPre165 = &verified;
    /*[]*/
    verify(64, 64, 64, niter, _imopVarPre165, _imopVarPre164);
    /*[]*/
    /*[44]*/
#pragma omp parallel
    {
    /*[44]*/
    }
    /*[]*/
    timer_stop(0);
    /*[]*/
    /*[]*/
    total_time = timer_read(0);
    /*[]*/
    /*[]*/
    /*[]*/
    if (total_time != 0.0) {
    /*[]*/
        /*[]*/
        double _imopVarPre184;
        /*[]*/
        double _imopVarPre185;
        /*[]*/
        double _imopVarPre188;
        /*[]*/
        double _imopVarPre189;
        /*[]*/
        _imopVarPre184 = (double) 262144;
        /*[]*/
        _imopVarPre185 = log(_imopVarPre184);
        /*[]*/
        /*[]*/
        _imopVarPre188 = (double) 262144;
        /*[]*/
        _imopVarPre189 = log(_imopVarPre188);
        /*[]*/
        /*[]*/
        mflops = 1.0e-6 * (double) 262144 * (14.8157 + 7.19641 * _imopVarPre185 + (5.23518 + 7.21113 * _imopVarPre189) * niter) / total_time;
    } else {
    /*[]*/
        /*[]*/
        mflops = 0.0;
    }
    /*[]*/
    c_print_results("FT", class, 64, 64, 64, niter, nthreads, total_time, mflops, "          floating point", verified, "3.0 structured", "21 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "randdp");
    /*[]*/
    /*[]*/
    /*[]*/
    if (0 == 1) {
    /*[]*/
        /*[]*/
        print_timers();
        /*[]*/
    }
}
/*[28]*/
/*[28]*/
/*[28]*/
/*[28]*/
/*[28]*/
/*[28]*/
static void evolve(dcomplex u0[64][64][64], dcomplex u1[64][64][64] , int t , int indexmap[64][64][64] , int d[3]) {
/*[28]*/
    /*[28]*/
    int i;
    /*[28]*/
    int j;
    /*[28]*/
    int k;
    /*[28]*/
#pragma omp for nowait
    /*[28]*/
    /*[28]*/
    /*[28]*/
    for (k = 0; k < d[2]; k++) {
    /*[28]*/
        /*[28]*/
        /*[28]*/
        /*[28]*/
        /*[28]*/
        for (j = 0; j < d[1]; j++) {
        /*[28]*/
            /*[28]*/
            /*[28]*/
            /*[28]*/
            /*[28]*/
            for (i = 0; i < d[0]; i++) {
            /*[28]*/
                /*[28]*/
                u1[k][j][i].real = u0[k][j][i].real * ex[t * indexmap[k][j][i]];
                /*[28]*/
                (u1[k][j][i].imag = u0[k][j][i].imag * ex[t * indexmap[k][j][i]]);
            }
        }
    }
}
/*[]*/
/*[]*/
/*[]*/
static void compute_initial_conditions(dcomplex u0[64][64][64], int d[3]) {
/*[]*/
    /*[]*/
    int k;
    /*[]*/
    double x0;
    /*[]*/
    double start;
    /*[]*/
    double an;
    /*[]*/
    double dummy;
    /*[]*/
    static double tmp[64 * 2 * 64 + 1];
    /*[]*/
    int i;
    /*[]*/
    int j;
    /*[]*/
    int t;
    /*[]*/
    start = 314159265.0;
    /*[]*/
    double *_imopVarPre192;
    /*[]*/
    int _imopVarPre193;
    /*[]*/
    _imopVarPre192 = &an;
    /*[]*/
    _imopVarPre193 = (zstart[0] - 1) * 2 * 64 * 64 + (ystart[0] - 1) * 2 * 64;
    /*[]*/
    ipow46(1220703125.0, _imopVarPre193, _imopVarPre192);
    /*[]*/
    /*[]*/
    double *_imopVarPre195;
    /*[]*/
    double _imopVarPre196;
    /*[]*/
    _imopVarPre195 = &start;
    /*[]*/
    _imopVarPre196 = randlc(_imopVarPre195, an);
    /*[]*/
    /*[]*/
    dummy = _imopVarPre196;
    /*[]*/
    double *_imopVarPre199;
    /*[]*/
    int _imopVarPre200;
    /*[]*/
    _imopVarPre199 = &an;
    /*[]*/
    _imopVarPre200 = 2 * 64 * 64;
    /*[]*/
    ipow46(1220703125.0, _imopVarPre200, _imopVarPre199);
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    for (k = 0; k < dims[0][2]; k++) {
    /*[]*/
        /*[]*/
        x0 = start;
        /*[]*/
        double *_imopVarPre203;
        /*[]*/
        int _imopVarPre204;
        /*[]*/
        _imopVarPre203 = &x0;
        /*[]*/
        _imopVarPre204 = 2 * 64 * dims[0][1];
        /*[]*/
        vranlc(_imopVarPre204, _imopVarPre203, 1220703125.0, tmp);
        /*[]*/
        /*[]*/
        t = 1;
        /*[]*/
        /*[]*/
        /*[]*/
        /*[]*/
        for (j = 0; j < dims[0][1]; j++) {
        /*[]*/
            /*[]*/
            /*[]*/
            /*[]*/
            /*[]*/
            for (i = 0; i < 64; i++) {
            /*[]*/
                /*[]*/
                u0[k][j][i].real = tmp[t++];
                /*[]*/
                u0[k][j][i].imag = tmp[t++];
            }
        }
        /*[]*/
        /*[]*/
        if (k != dims[0][2]) {
        /*[]*/
            /*[]*/
            double *_imopVarPre206;
            /*[]*/
            double _imopVarPre207;
            /*[]*/
            _imopVarPre206 = &start;
            /*[]*/
            _imopVarPre207 = randlc(_imopVarPre206, an);
            /*[]*/
            /*[]*/
            dummy = _imopVarPre207;
        }
    }
}
/*[]*/
/*[]*/
/*[]*/
/*[]*/
static void ipow46(double a, int exponent , double *result) {
/*[]*/
    /*[]*/
    double dummy;
    /*[]*/
    double q;
    /*[]*/
    double r;
    /*[]*/
    int n;
    /*[]*/
    int n2;
    /*[]*/
    *result = 1;
    /*[]*/
    /*[]*/
    if (exponent == 0) {
    /*[]*/
        /*[]*/
        return;
    }
    /*[]*/
    q = a;
    /*[]*/
    r = 1;
    /*[]*/
    n = exponent;
    /*[]*/
    /*[]*/
    while (n > 1) {
    /*[]*/
        /*[]*/
        n2 = n / 2;
        /*[]*/
        /*[]*/
        if (n2 * 2 == n) {
        /*[]*/
            /*[]*/
            double *_imopVarPre209;
            /*[]*/
            double _imopVarPre210;
            /*[]*/
            _imopVarPre209 = &q;
            /*[]*/
            _imopVarPre210 = randlc(_imopVarPre209, q);
            /*[]*/
            /*[]*/
            dummy = _imopVarPre210;
            /*[]*/
            n = n2;
        } else {
        /*[]*/
            /*[]*/
            double *_imopVarPre212;
            /*[]*/
            double _imopVarPre213;
            /*[]*/
            _imopVarPre212 = &r;
            /*[]*/
            _imopVarPre213 = randlc(_imopVarPre212, q);
            /*[]*/
            /*[]*/
            dummy = _imopVarPre213;
            /*[]*/
            n = n - 1;
        }
    }
    /*[]*/
    double *_imopVarPre215;
    /*[]*/
    double _imopVarPre216;
    /*[]*/
    _imopVarPre215 = &r;
    /*[]*/
    _imopVarPre216 = randlc(_imopVarPre215, q);
    /*[]*/
    /*[]*/
    dummy = _imopVarPre216;
    /*[]*/
    *result = r;
}
/*[]*/
static void setup() {
/*[]*/
    /*[]*/
    int i;
    /*[]*/
    printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version" " - FT Benchmark\n\n");
    /*[]*/
    /*[]*/
    niter = 6;
    /*[]*/
    printf(" Size                : %3dx%3dx%3d\n", 64, 64, 64);
    /*[]*/
    /*[]*/
    printf(" Iterations          :     %7d\n", niter);
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    for (i = 0; i < 3; i++) {
    /*[]*/
        /*[]*/
        dims[i][0] = 64;
        /*[]*/
        dims[i][1] = 64;
        /*[]*/
        dims[i][2] = 64;
    }
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    for (i = 0; i < 3; i++) {
    /*[]*/
        /*[]*/
        xstart[i] = 1;
        /*[]*/
        xend[i] = 64;
        /*[]*/
        ystart[i] = 1;
        /*[]*/
        yend[i] = 64;
        /*[]*/
        zstart[i] = 1;
        /*[]*/
        zend[i] = 64;
    }
    /*[]*/
    fftblock = 16;
    /*[]*/
    fftblockpad = 18;
    /*[]*/
    /*[]*/
    if (fftblock != 16) {
    /*[]*/
        /*[]*/
        fftblockpad = fftblock + 3;
    }
}
/*[]*/
/*[]*/
/*[]*/
static void compute_indexmap(int indexmap[64][64][64], int d[3]) {
/*[]*/
    /*[]*/
    int i;
    /*[]*/
    int j;
    /*[]*/
    int k;
    /*[]*/
    int ii;
    /*[]*/
    int ii2;
    /*[]*/
    int jj;
    /*[]*/
    int ij2;
    /*[]*/
    int kk;
    /*[]*/
    double ap;
    /*[45]*/
#pragma omp parallel default(shared) private(i, j, k, ii, ii2, jj, ij2, kk)
    {
    /*[45]*/
        /*[45]*/
#pragma omp for nowait
        /*[45]*/
        /*[45]*/
        /*[45]*/
        for (i = 0; i < dims[2][0]; i++) {
        /*[45]*/
            /*[45]*/
            ii = (i + 1 + xstart[2] - 2 + 64 / 2) % 64 - 64 / 2;
            /*[45]*/
            ii2 = ii * ii;
            /*[45]*/
            /*[45]*/
            /*[45]*/
            /*[45]*/
            for (j = 0; j < dims[2][1]; j++) {
            /*[45]*/
                /*[45]*/
                jj = (j + 1 + ystart[2] - 2 + 64 / 2) % 64 - 64 / 2;
                /*[45]*/
                ij2 = jj * jj + ii2;
                /*[45]*/
                /*[45]*/
                /*[45]*/
                /*[45]*/
                for (k = 0; k < dims[2][2]; k++) {
                /*[45]*/
                    /*[45]*/
                    kk = (k + 1 + zstart[2] - 2 + 64 / 2) % 64 - 64 / 2;
                    /*[45]*/
                    indexmap[k][j][i] = kk * kk + ij2;
                }
            }
        }
    }
    /*[]*/
    ap = -4.0 * 1.0e-6 * 3.141592653589793238 * 3.141592653589793238;
    /*[]*/
    ex[0] = 1.0;
    /*[]*/
    double _imopVarPre217;
    /*[]*/
    _imopVarPre217 = exp(ap);
    /*[]*/
    /*[]*/
    ex[1] = _imopVarPre217;
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    for (i = 2; i <= (6 * (64 * 64 / 4 + 64 * 64 / 4 + 64 * 64 / 4)); i++) {
    /*[]*/
        /*[]*/
        ex[i] = ex[i - 1] * ex[1];
    }
}
/*[]*/
static void print_timers() {
/*[]*/
    /*[]*/
    int i;
    /*[]*/
    char *tstrings[] = {"          total ", "          setup " , "            fft " , "         evolve " , "       checksum " , "         fftlow " , "        fftcopy "};
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    for (i = 0; i < 7; i++) {
    /*[]*/
        /*[]*/
        double _imopVarPre219;
        /*[]*/
        _imopVarPre219 = timer_read(i);
        /*[]*/
        /*[]*/
        /*[]*/
        if (_imopVarPre219 != 0.0) {
        /*[]*/
            /*[]*/
            double _imopVarPre222;
            /*[]*/
            char *_imopVarPre223;
            /*[]*/
            _imopVarPre222 = timer_read(i);
            /*[]*/
            /*[]*/
            _imopVarPre223 = tstrings[i];
            /*[]*/
            printf("timer %2d(%16s( :%10.6f\n", i, _imopVarPre223, _imopVarPre222);
            /*[]*/
        }
    }
}
/*[]*/
/*[]*/
/*[]*/
/*[]*/
static void fft(int dir, dcomplex x1[64][64][64] , dcomplex x2[64][64][64]) {
/*[]*/
    /*[]*/
    dcomplex y0[64][18];
    /*[]*/
    dcomplex y1[64][18];
    /*[]*/
    /*[]*/
    if (dir == 1) {
    /*[]*/
        /*[]*/
        int ( *_imopVarPre225 );
        /*[]*/
        _imopVarPre225 = dims[0];
        /*[]*/
        int is;
        /*[]*/
        int *d;
        /*[]*/
        struct stUn_imopVarPre11 ( *x )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout )[64][64];
        /*[]*/
        is = 1;
        /*[]*/
        d = _imopVarPre225;
        /*[]*/
        x = x1;
        /*[]*/
        xout = x1;
        /*[]*/
        int logd[3];
        /*[46]*/
#pragma omp parallel default(shared) shared(is)
        {
        /*[46]*/
            /*[46]*/
            int i;
            /*[46]*/
            int j;
            /*[46]*/
            int k;
            /*[46]*/
            int jj;
            /*[46]*/
#pragma omp master
            {
            /*[46]*/
                /*[46]*/
                /*[46]*/
                /*[46]*/
                /*[46]*/
                for (i = 0; i < 3; i++) {
                /*[46]*/
                    /*[46]*/
                    int _imopVarPre243;
                    /*[46]*/
                    int _imopVarPre244;
                    /*[46]*/
                    _imopVarPre243 = d[i];
                    /*[46]*/
                    _imopVarPre244 = ilog2(_imopVarPre243);
                    /*[46]*/
                    /*[46]*/
                    logd[i] = _imopVarPre244;
                }
            }
            /*[46]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[46]*/
#pragma omp barrier
            /*[47]*/
            dcomplex y0[64][18];
            /*[47]*/
            dcomplex y1[64][18];
            /*[47]*/
#pragma omp for nowait
            /*[47]*/
            /*[47]*/
            /*[47]*/
            for (k = 0; k < d[2]; k++) {
            /*[47]*/
                /*[47]*/
                /*[47]*/
                /*[47]*/
                /*[47]*/
                for (jj = 0; jj <= d[1] - fftblock; jj += fftblock) {
                /*[47]*/
                    /*[47]*/
                    /*[47]*/
                    /*[47]*/
                    /*[47]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[47]*/
                        /*[47]*/
                        /*[47]*/
                        /*[47]*/
                        /*[47]*/
                        for (i = 0; i < d[0]; i++) {
                        /*[47]*/
                            /*[47]*/
                            y0[i][j].real = x[k][j + jj][i].real;
                            /*[47]*/
                            y0[i][j].imag = x[k][j + jj][i].imag;
                        }
                    }
                    /*[47]*/
                    int _imopVarPre247;
                    /*[47]*/
                    int _imopVarPre248;
                    /*[47]*/
                    _imopVarPre247 = d[0];
                    /*[47]*/
                    _imopVarPre248 = logd[0];
                    /*[47]*/
                    cfftz(is, _imopVarPre248, _imopVarPre247, y0, y1);
                    /*[47]*/
                    /*[47]*/
                    /*[47]*/
                    /*[47]*/
                    /*[47]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[47]*/
                        /*[47]*/
                        /*[47]*/
                        /*[47]*/
                        /*[47]*/
                        for (i = 0; i < d[0]; i++) {
                        /*[47]*/
                            /*[47]*/
                            xout[k][j + jj][i].real = y0[i][j].real;
                            /*[47]*/
                            xout[k][j + jj][i].imag = y0[i][j].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre227 );
        /*[]*/
        _imopVarPre227 = dims[1];
        /*[]*/
        int is_imopVarPre76;
        /*[]*/
        int *d_imopVarPre77;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre78 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre79 )[64][64];
        /*[]*/
        is_imopVarPre76 = 1;
        /*[]*/
        d_imopVarPre77 = _imopVarPre227;
        /*[]*/
        x_imopVarPre78 = x1;
        /*[]*/
        xout_imopVarPre79 = x1;
        /*[]*/
        int logd_imopVarPre80[3];
        /*[48]*/
#pragma omp parallel default(shared) shared(is_imopVarPre76)
        {
        /*[48]*/
            /*[48]*/
            int i;
            /*[48]*/
            int j;
            /*[48]*/
            int k;
            /*[48]*/
            int ii;
            /*[48]*/
#pragma omp master
            {
            /*[48]*/
                /*[48]*/
                /*[48]*/
                /*[48]*/
                /*[48]*/
                for (i = 0; i < 3; i++) {
                /*[48]*/
                    /*[48]*/
                    int _imopVarPre250;
                    /*[48]*/
                    int _imopVarPre251;
                    /*[48]*/
                    _imopVarPre250 = d_imopVarPre77[i];
                    /*[48]*/
                    _imopVarPre251 = ilog2(_imopVarPre250);
                    /*[48]*/
                    /*[48]*/
                    logd_imopVarPre80[i] = _imopVarPre251;
                }
            }
            /*[48]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[48]*/
#pragma omp barrier
            /*[49]*/
            dcomplex y0[64][18];
            /*[49]*/
            dcomplex y1[64][18];
            /*[49]*/
#pragma omp for nowait
            /*[49]*/
            /*[49]*/
            /*[49]*/
            for (k = 0; k < d_imopVarPre77[2]; k++) {
            /*[49]*/
                /*[49]*/
                /*[49]*/
                /*[49]*/
                /*[49]*/
                for (ii = 0; ii <= d_imopVarPre77[0] - fftblock; ii += fftblock) {
                /*[49]*/
                    /*[49]*/
                    /*[49]*/
                    /*[49]*/
                    /*[49]*/
                    for (j = 0; j < d_imopVarPre77[1]; j++) {
                    /*[49]*/
                        /*[49]*/
                        /*[49]*/
                        /*[49]*/
                        /*[49]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[49]*/
                            /*[49]*/
                            y0[j][i].real = x_imopVarPre78[k][j][i + ii].real;
                            /*[49]*/
                            y0[j][i].imag = x_imopVarPre78[k][j][i + ii].imag;
                        }
                    }
                    /*[49]*/
                    int _imopVarPre254;
                    /*[49]*/
                    int _imopVarPre255;
                    /*[49]*/
                    _imopVarPre254 = d_imopVarPre77[1];
                    /*[49]*/
                    _imopVarPre255 = logd_imopVarPre80[1];
                    /*[49]*/
                    cfftz(is_imopVarPre76, _imopVarPre255, _imopVarPre254, y0, y1);
                    /*[49]*/
                    /*[49]*/
                    /*[49]*/
                    /*[49]*/
                    /*[49]*/
                    for (j = 0; j < d_imopVarPre77[1]; j++) {
                    /*[49]*/
                        /*[49]*/
                        /*[49]*/
                        /*[49]*/
                        /*[49]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[49]*/
                            /*[49]*/
                            xout_imopVarPre79[k][j][i + ii].real = y0[j][i].real;
                            /*[49]*/
                            xout_imopVarPre79[k][j][i + ii].imag = y0[j][i].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre229 );
        /*[]*/
        _imopVarPre229 = dims[2];
        /*[]*/
        int is_imopVarPre81;
        /*[]*/
        int *d_imopVarPre82;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre83 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre84 )[64][64];
        /*[]*/
        is_imopVarPre81 = 1;
        /*[]*/
        d_imopVarPre82 = _imopVarPre229;
        /*[]*/
        x_imopVarPre83 = x1;
        /*[]*/
        xout_imopVarPre84 = x2;
        /*[]*/
        int logd_imopVarPre85[3];
        /*[50]*/
#pragma omp parallel default(shared) shared(is_imopVarPre81)
        {
        /*[50]*/
            /*[50]*/
            int i;
            /*[50]*/
            int j;
            /*[50]*/
            int k;
            /*[50]*/
            int ii;
            /*[50]*/
#pragma omp master
            {
            /*[50]*/
                /*[50]*/
                /*[50]*/
                /*[50]*/
                /*[50]*/
                for (i = 0; i < 3; i++) {
                /*[50]*/
                    /*[50]*/
                    int _imopVarPre257;
                    /*[50]*/
                    int _imopVarPre258;
                    /*[50]*/
                    _imopVarPre257 = d_imopVarPre82[i];
                    /*[50]*/
                    _imopVarPre258 = ilog2(_imopVarPre257);
                    /*[50]*/
                    /*[50]*/
                    logd_imopVarPre85[i] = _imopVarPre258;
                }
            }
            /*[50]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[50]*/
#pragma omp barrier
            /*[51]*/
            dcomplex y0[64][18];
            /*[51]*/
            dcomplex y1[64][18];
            /*[51]*/
#pragma omp for nowait
            /*[51]*/
            /*[51]*/
            /*[51]*/
            for (j = 0; j < d_imopVarPre82[1]; j++) {
            /*[51]*/
                /*[51]*/
                /*[51]*/
                /*[51]*/
                /*[51]*/
                for (ii = 0; ii <= d_imopVarPre82[0] - fftblock; ii += fftblock) {
                /*[51]*/
                    /*[51]*/
                    /*[51]*/
                    /*[51]*/
                    /*[51]*/
                    for (k = 0; k < d_imopVarPre82[2]; k++) {
                    /*[51]*/
                        /*[51]*/
                        /*[51]*/
                        /*[51]*/
                        /*[51]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[51]*/
                            /*[51]*/
                            y0[k][i].real = x_imopVarPre83[k][j][i + ii].real;
                            /*[51]*/
                            y0[k][i].imag = x_imopVarPre83[k][j][i + ii].imag;
                        }
                    }
                    /*[51]*/
                    int _imopVarPre261;
                    /*[51]*/
                    int _imopVarPre262;
                    /*[51]*/
                    _imopVarPre261 = d_imopVarPre82[2];
                    /*[51]*/
                    _imopVarPre262 = logd_imopVarPre85[2];
                    /*[51]*/
                    cfftz(is_imopVarPre81, _imopVarPre262, _imopVarPre261, y0, y1);
                    /*[51]*/
                    /*[51]*/
                    /*[51]*/
                    /*[51]*/
                    /*[51]*/
                    for (k = 0; k < d_imopVarPre82[2]; k++) {
                    /*[51]*/
                        /*[51]*/
                        /*[51]*/
                        /*[51]*/
                        /*[51]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[51]*/
                            /*[51]*/
                            xout_imopVarPre84[k][j][i + ii].real = y0[k][i].real;
                            /*[51]*/
                            xout_imopVarPre84[k][j][i + ii].imag = y0[k][i].imag;
                        }
                    }
                }
            }
        }
    } else {
    /*[]*/
        /*[]*/
        int ( *_imopVarPre232 );
        /*[]*/
        int _imopVarPre233;
        /*[]*/
        _imopVarPre232 = dims[2];
        /*[]*/
        _imopVarPre233 = -1;
        /*[]*/
        int is_imopVarPre102;
        /*[]*/
        int *d_imopVarPre103;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre104 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre105 )[64][64];
        /*[]*/
        is_imopVarPre102 = _imopVarPre233;
        /*[]*/
        d_imopVarPre103 = _imopVarPre232;
        /*[]*/
        x_imopVarPre104 = x1;
        /*[]*/
        xout_imopVarPre105 = x1;
        /*[]*/
        int logd_imopVarPre106[3];
        /*[52]*/
#pragma omp parallel default(shared) shared(is_imopVarPre102)
        {
        /*[52]*/
            /*[52]*/
            int i;
            /*[52]*/
            int j;
            /*[52]*/
            int k;
            /*[52]*/
            int ii;
            /*[52]*/
#pragma omp master
            {
            /*[52]*/
                /*[52]*/
                /*[52]*/
                /*[52]*/
                /*[52]*/
                for (i = 0; i < 3; i++) {
                /*[52]*/
                    /*[52]*/
                    int _imopVarPre257;
                    /*[52]*/
                    int _imopVarPre258;
                    /*[52]*/
                    _imopVarPre257 = d_imopVarPre103[i];
                    /*[52]*/
                    _imopVarPre258 = ilog2(_imopVarPre257);
                    /*[52]*/
                    /*[52]*/
                    logd_imopVarPre106[i] = _imopVarPre258;
                }
            }
            /*[52]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[52]*/
#pragma omp barrier
            /*[53]*/
            dcomplex y0[64][18];
            /*[53]*/
            dcomplex y1[64][18];
            /*[53]*/
#pragma omp for nowait
            /*[53]*/
            /*[53]*/
            /*[53]*/
            for (j = 0; j < d_imopVarPre103[1]; j++) {
            /*[53]*/
                /*[53]*/
                /*[53]*/
                /*[53]*/
                /*[53]*/
                for (ii = 0; ii <= d_imopVarPre103[0] - fftblock; ii += fftblock) {
                /*[53]*/
                    /*[53]*/
                    /*[53]*/
                    /*[53]*/
                    /*[53]*/
                    for (k = 0; k < d_imopVarPre103[2]; k++) {
                    /*[53]*/
                        /*[53]*/
                        /*[53]*/
                        /*[53]*/
                        /*[53]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[53]*/
                            /*[53]*/
                            y0[k][i].real = x_imopVarPre104[k][j][i + ii].real;
                            /*[53]*/
                            y0[k][i].imag = x_imopVarPre104[k][j][i + ii].imag;
                        }
                    }
                    /*[53]*/
                    int _imopVarPre261;
                    /*[53]*/
                    int _imopVarPre262;
                    /*[53]*/
                    _imopVarPre261 = d_imopVarPre103[2];
                    /*[53]*/
                    _imopVarPre262 = logd_imopVarPre106[2];
                    /*[53]*/
                    cfftz(is_imopVarPre102, _imopVarPre262, _imopVarPre261, y0, y1);
                    /*[53]*/
                    /*[53]*/
                    /*[53]*/
                    /*[53]*/
                    /*[53]*/
                    for (k = 0; k < d_imopVarPre103[2]; k++) {
                    /*[53]*/
                        /*[53]*/
                        /*[53]*/
                        /*[53]*/
                        /*[53]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[53]*/
                            /*[53]*/
                            xout_imopVarPre105[k][j][i + ii].real = y0[k][i].real;
                            /*[53]*/
                            xout_imopVarPre105[k][j][i + ii].imag = y0[k][i].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre236 );
        /*[]*/
        int _imopVarPre237;
        /*[]*/
        _imopVarPre236 = dims[1];
        /*[]*/
        _imopVarPre237 = -1;
        /*[]*/
        int is;
        /*[]*/
        int *d;
        /*[]*/
        struct stUn_imopVarPre11 ( *x )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout )[64][64];
        /*[]*/
        is = _imopVarPre237;
        /*[]*/
        d = _imopVarPre236;
        /*[]*/
        x = x1;
        /*[]*/
        xout = x1;
        /*[]*/
        int logd[3];
        /*[54]*/
#pragma omp parallel default(shared) shared(is)
        {
        /*[54]*/
            /*[54]*/
            int i;
            /*[54]*/
            int j;
            /*[54]*/
            int k;
            /*[54]*/
            int ii;
            /*[54]*/
#pragma omp master
            {
            /*[54]*/
                /*[54]*/
                /*[54]*/
                /*[54]*/
                /*[54]*/
                for (i = 0; i < 3; i++) {
                /*[54]*/
                    /*[54]*/
                    int _imopVarPre250;
                    /*[54]*/
                    int _imopVarPre251;
                    /*[54]*/
                    _imopVarPre250 = d[i];
                    /*[54]*/
                    _imopVarPre251 = ilog2(_imopVarPre250);
                    /*[54]*/
                    /*[54]*/
                    logd[i] = _imopVarPre251;
                }
            }
            /*[54]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[54]*/
#pragma omp barrier
            /*[55]*/
            dcomplex y0[64][18];
            /*[55]*/
            dcomplex y1[64][18];
            /*[55]*/
#pragma omp for nowait
            /*[55]*/
            /*[55]*/
            /*[55]*/
            for (k = 0; k < d[2]; k++) {
            /*[55]*/
                /*[55]*/
                /*[55]*/
                /*[55]*/
                /*[55]*/
                for (ii = 0; ii <= d[0] - fftblock; ii += fftblock) {
                /*[55]*/
                    /*[55]*/
                    /*[55]*/
                    /*[55]*/
                    /*[55]*/
                    for (j = 0; j < d[1]; j++) {
                    /*[55]*/
                        /*[55]*/
                        /*[55]*/
                        /*[55]*/
                        /*[55]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[55]*/
                            /*[55]*/
                            y0[j][i].real = x[k][j][i + ii].real;
                            /*[55]*/
                            y0[j][i].imag = x[k][j][i + ii].imag;
                        }
                    }
                    /*[55]*/
                    int _imopVarPre254;
                    /*[55]*/
                    int _imopVarPre255;
                    /*[55]*/
                    _imopVarPre254 = d[1];
                    /*[55]*/
                    _imopVarPre255 = logd[1];
                    /*[55]*/
                    cfftz(is, _imopVarPre255, _imopVarPre254, y0, y1);
                    /*[55]*/
                    /*[55]*/
                    /*[55]*/
                    /*[55]*/
                    /*[55]*/
                    for (j = 0; j < d[1]; j++) {
                    /*[55]*/
                        /*[55]*/
                        /*[55]*/
                        /*[55]*/
                        /*[55]*/
                        for (i = 0; i < fftblock; i++) {
                        /*[55]*/
                            /*[55]*/
                            xout[k][j][i + ii].real = y0[j][i].real;
                            /*[55]*/
                            xout[k][j][i + ii].imag = y0[j][i].imag;
                        }
                    }
                }
            }
        }
        /*[]*/
        int ( *_imopVarPre240 );
        /*[]*/
        int _imopVarPre241;
        /*[]*/
        _imopVarPre240 = dims[0];
        /*[]*/
        _imopVarPre241 = -1;
        /*[]*/
        int is_imopVarPre97;
        /*[]*/
        int *d_imopVarPre98;
        /*[]*/
        struct stUn_imopVarPre11 ( *x_imopVarPre99 )[64][64];
        /*[]*/
        struct stUn_imopVarPre11 ( *xout_imopVarPre100 )[64][64];
        /*[]*/
        is_imopVarPre97 = _imopVarPre241;
        /*[]*/
        d_imopVarPre98 = _imopVarPre240;
        /*[]*/
        x_imopVarPre99 = x1;
        /*[]*/
        xout_imopVarPre100 = x2;
        /*[]*/
        int logd_imopVarPre101[3];
        /*[56]*/
#pragma omp parallel default(shared) shared(is_imopVarPre97)
        {
        /*[56]*/
            /*[56]*/
            int i;
            /*[56]*/
            int j;
            /*[56]*/
            int k;
            /*[56]*/
            int jj;
            /*[56]*/
#pragma omp master
            {
            /*[56]*/
                /*[56]*/
                /*[56]*/
                /*[56]*/
                /*[56]*/
                for (i = 0; i < 3; i++) {
                /*[56]*/
                    /*[56]*/
                    int _imopVarPre243;
                    /*[56]*/
                    int _imopVarPre244;
                    /*[56]*/
                    _imopVarPre243 = d_imopVarPre98[i];
                    /*[56]*/
                    _imopVarPre244 = ilog2(_imopVarPre243);
                    /*[56]*/
                    /*[56]*/
                    logd_imopVarPre101[i] = _imopVarPre244;
                }
            }
            /*[56]*/
            // #pragma omp dummyFlush BARRIER_START
            /*[56]*/
#pragma omp barrier
            /*[57]*/
            dcomplex y0[64][18];
            /*[57]*/
            dcomplex y1[64][18];
            /*[57]*/
#pragma omp for nowait
            /*[57]*/
            /*[57]*/
            /*[57]*/
            for (k = 0; k < d_imopVarPre98[2]; k++) {
            /*[57]*/
                /*[57]*/
                /*[57]*/
                /*[57]*/
                /*[57]*/
                for (jj = 0; jj <= d_imopVarPre98[1] - fftblock; jj += fftblock) {
                /*[57]*/
                    /*[57]*/
                    /*[57]*/
                    /*[57]*/
                    /*[57]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[57]*/
                        /*[57]*/
                        /*[57]*/
                        /*[57]*/
                        /*[57]*/
                        for (i = 0; i < d_imopVarPre98[0]; i++) {
                        /*[57]*/
                            /*[57]*/
                            y0[i][j].real = x_imopVarPre99[k][j + jj][i].real;
                            /*[57]*/
                            y0[i][j].imag = x_imopVarPre99[k][j + jj][i].imag;
                        }
                    }
                    /*[57]*/
                    int _imopVarPre247;
                    /*[57]*/
                    int _imopVarPre248;
                    /*[57]*/
                    _imopVarPre247 = d_imopVarPre98[0];
                    /*[57]*/
                    _imopVarPre248 = logd_imopVarPre101[0];
                    /*[57]*/
                    cfftz(is_imopVarPre97, _imopVarPre248, _imopVarPre247, y0, y1);
                    /*[57]*/
                    /*[57]*/
                    /*[57]*/
                    /*[57]*/
                    /*[57]*/
                    for (j = 0; j < fftblock; j++) {
                    /*[57]*/
                        /*[57]*/
                        /*[57]*/
                        /*[57]*/
                        /*[57]*/
                        for (i = 0; i < d_imopVarPre98[0]; i++) {
                        /*[57]*/
                            /*[57]*/
                            xout_imopVarPre100[k][j + jj][i].real = y0[i][j].real;
                            /*[57]*/
                            xout_imopVarPre100[k][j + jj][i].imag = y0[i][j].imag;
                        }
                    }
                }
            }
        }
    }
}
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
static void cffts1(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]) {
/*[]*/
    /*[]*/
    int logd[3];
    /*[58]*/
#pragma omp parallel default(shared) shared(is)
    {
    /*[58]*/
        /*[58]*/
        int i;
        /*[58]*/
        int j;
        /*[58]*/
        int k;
        /*[58]*/
        int jj;
        /*[58]*/
#pragma omp master
        {
        /*[58]*/
            /*[58]*/
            /*[58]*/
            /*[58]*/
            /*[58]*/
            for (i = 0; i < 3; i++) {
            /*[58]*/
                /*[58]*/
                int _imopVarPre243;
                /*[58]*/
                int _imopVarPre244;
                /*[58]*/
                _imopVarPre243 = d[i];
                /*[58]*/
                _imopVarPre244 = ilog2(_imopVarPre243);
                /*[58]*/
                /*[58]*/
                logd[i] = _imopVarPre244;
            }
        }
        /*[58]*/
        // #pragma omp dummyFlush BARRIER_START
        /*[58]*/
#pragma omp barrier
        /*[59]*/
        dcomplex y0[64][18];
        /*[59]*/
        dcomplex y1[64][18];
        /*[59]*/
#pragma omp for nowait
        /*[59]*/
        /*[59]*/
        /*[59]*/
        for (k = 0; k < d[2]; k++) {
        /*[59]*/
            /*[59]*/
            /*[59]*/
            /*[59]*/
            /*[59]*/
            for (jj = 0; jj <= d[1] - fftblock; jj += fftblock) {
            /*[59]*/
                /*[59]*/
                /*[59]*/
                /*[59]*/
                /*[59]*/
                for (j = 0; j < fftblock; j++) {
                /*[59]*/
                    /*[59]*/
                    /*[59]*/
                    /*[59]*/
                    /*[59]*/
                    for (i = 0; i < d[0]; i++) {
                    /*[59]*/
                        /*[59]*/
                        y0[i][j].real = x[k][j + jj][i].real;
                        /*[59]*/
                        y0[i][j].imag = x[k][j + jj][i].imag;
                    }
                }
                /*[59]*/
                int _imopVarPre247;
                /*[59]*/
                int _imopVarPre248;
                /*[59]*/
                _imopVarPre247 = d[0];
                /*[59]*/
                _imopVarPre248 = logd[0];
                /*[59]*/
                cfftz(is, _imopVarPre248, _imopVarPre247, y0, y1);
                /*[59]*/
                /*[59]*/
                /*[59]*/
                /*[59]*/
                /*[59]*/
                for (j = 0; j < fftblock; j++) {
                /*[59]*/
                    /*[59]*/
                    /*[59]*/
                    /*[59]*/
                    /*[59]*/
                    for (i = 0; i < d[0]; i++) {
                    /*[59]*/
                        /*[59]*/
                        xout[k][j + jj][i].real = y0[i][j].real;
                        /*[59]*/
                        xout[k][j + jj][i].imag = y0[i][j].imag;
                    }
                }
            }
        }
    }
}
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
static void cffts2(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]) {
/*[]*/
    /*[]*/
    int logd[3];
    /*[60]*/
#pragma omp parallel default(shared) shared(is)
    {
    /*[60]*/
        /*[60]*/
        int i;
        /*[60]*/
        int j;
        /*[60]*/
        int k;
        /*[60]*/
        int ii;
        /*[60]*/
#pragma omp master
        {
        /*[60]*/
            /*[60]*/
            /*[60]*/
            /*[60]*/
            /*[60]*/
            for (i = 0; i < 3; i++) {
            /*[60]*/
                /*[60]*/
                int _imopVarPre250;
                /*[60]*/
                int _imopVarPre251;
                /*[60]*/
                _imopVarPre250 = d[i];
                /*[60]*/
                _imopVarPre251 = ilog2(_imopVarPre250);
                /*[60]*/
                /*[60]*/
                logd[i] = _imopVarPre251;
            }
        }
        /*[60]*/
        // #pragma omp dummyFlush BARRIER_START
        /*[60]*/
#pragma omp barrier
        /*[61]*/
        dcomplex y0[64][18];
        /*[61]*/
        dcomplex y1[64][18];
        /*[61]*/
#pragma omp for nowait
        /*[61]*/
        /*[61]*/
        /*[61]*/
        for (k = 0; k < d[2]; k++) {
        /*[61]*/
            /*[61]*/
            /*[61]*/
            /*[61]*/
            /*[61]*/
            for (ii = 0; ii <= d[0] - fftblock; ii += fftblock) {
            /*[61]*/
                /*[61]*/
                /*[61]*/
                /*[61]*/
                /*[61]*/
                for (j = 0; j < d[1]; j++) {
                /*[61]*/
                    /*[61]*/
                    /*[61]*/
                    /*[61]*/
                    /*[61]*/
                    for (i = 0; i < fftblock; i++) {
                    /*[61]*/
                        /*[61]*/
                        y0[j][i].real = x[k][j][i + ii].real;
                        /*[61]*/
                        y0[j][i].imag = x[k][j][i + ii].imag;
                    }
                }
                /*[61]*/
                int _imopVarPre254;
                /*[61]*/
                int _imopVarPre255;
                /*[61]*/
                _imopVarPre254 = d[1];
                /*[61]*/
                _imopVarPre255 = logd[1];
                /*[61]*/
                cfftz(is, _imopVarPre255, _imopVarPre254, y0, y1);
                /*[61]*/
                /*[61]*/
                /*[61]*/
                /*[61]*/
                /*[61]*/
                for (j = 0; j < d[1]; j++) {
                /*[61]*/
                    /*[61]*/
                    /*[61]*/
                    /*[61]*/
                    /*[61]*/
                    for (i = 0; i < fftblock; i++) {
                    /*[61]*/
                        /*[61]*/
                        xout[k][j][i + ii].real = y0[j][i].real;
                        /*[61]*/
                        xout[k][j][i + ii].imag = y0[j][i].imag;
                    }
                }
            }
        }
    }
}
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
static void cffts3(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]) {
/*[]*/
    /*[]*/
    int logd[3];
    /*[62]*/
#pragma omp parallel default(shared) shared(is)
    {
    /*[62]*/
        /*[62]*/
        int i;
        /*[62]*/
        int j;
        /*[62]*/
        int k;
        /*[62]*/
        int ii;
        /*[62]*/
#pragma omp master
        {
        /*[62]*/
            /*[62]*/
            /*[62]*/
            /*[62]*/
            /*[62]*/
            for (i = 0; i < 3; i++) {
            /*[62]*/
                /*[62]*/
                int _imopVarPre257;
                /*[62]*/
                int _imopVarPre258;
                /*[62]*/
                _imopVarPre257 = d[i];
                /*[62]*/
                _imopVarPre258 = ilog2(_imopVarPre257);
                /*[62]*/
                /*[62]*/
                logd[i] = _imopVarPre258;
            }
        }
        /*[62]*/
        // #pragma omp dummyFlush BARRIER_START
        /*[62]*/
#pragma omp barrier
        /*[63]*/
        dcomplex y0[64][18];
        /*[63]*/
        dcomplex y1[64][18];
        /*[63]*/
#pragma omp for nowait
        /*[63]*/
        /*[63]*/
        /*[63]*/
        for (j = 0; j < d[1]; j++) {
        /*[63]*/
            /*[63]*/
            /*[63]*/
            /*[63]*/
            /*[63]*/
            for (ii = 0; ii <= d[0] - fftblock; ii += fftblock) {
            /*[63]*/
                /*[63]*/
                /*[63]*/
                /*[63]*/
                /*[63]*/
                for (k = 0; k < d[2]; k++) {
                /*[63]*/
                    /*[63]*/
                    /*[63]*/
                    /*[63]*/
                    /*[63]*/
                    for (i = 0; i < fftblock; i++) {
                    /*[63]*/
                        /*[63]*/
                        y0[k][i].real = x[k][j][i + ii].real;
                        /*[63]*/
                        y0[k][i].imag = x[k][j][i + ii].imag;
                    }
                }
                /*[63]*/
                int _imopVarPre261;
                /*[63]*/
                int _imopVarPre262;
                /*[63]*/
                _imopVarPre261 = d[2];
                /*[63]*/
                _imopVarPre262 = logd[2];
                /*[63]*/
                cfftz(is, _imopVarPre262, _imopVarPre261, y0, y1);
                /*[63]*/
                /*[63]*/
                /*[63]*/
                /*[63]*/
                /*[63]*/
                for (k = 0; k < d[2]; k++) {
                /*[63]*/
                    /*[63]*/
                    /*[63]*/
                    /*[63]*/
                    /*[63]*/
                    for (i = 0; i < fftblock; i++) {
                    /*[63]*/
                        /*[63]*/
                        xout[k][j][i + ii].real = y0[k][i].real;
                        /*[63]*/
                        xout[k][j][i + ii].imag = y0[k][i].imag;
                    }
                }
            }
        }
    }
}
/*[]*/
/*[]*/
static void fft_init(int n) {
/*[]*/
    /*[]*/
    int m;
    /*[]*/
    int nu;
    /*[]*/
    int ku;
    /*[]*/
    int i;
    /*[]*/
    int j;
    /*[]*/
    int ln;
    /*[]*/
    double t;
    /*[]*/
    double ti;
    /*[]*/
    nu = n;
    /*[]*/
    m = ilog2(n);
    /*[]*/
    /*[]*/
    u[0].real = (double) m;
    /*[]*/
    u[0].imag = 0.0;
    /*[]*/
    ku = 1;
    /*[]*/
    ln = 1;
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    for (j = 1; j <= m; j++) {
    /*[]*/
        /*[]*/
        t = 3.141592653589793238 / ln;
        /*[]*/
        /*[]*/
        /*[]*/
        /*[]*/
        for (i = 0; i <= ln - 1; i++) {
        /*[]*/
            /*[]*/
            ti = i * t;
            /*[]*/
            double _imopVarPre263;
            /*[]*/
            _imopVarPre263 = cos(ti);
            /*[]*/
            /*[]*/
            u[i + ku].real = _imopVarPre263;
            /*[]*/
            double _imopVarPre264;
            /*[]*/
            _imopVarPre264 = sin(ti);
            /*[]*/
            /*[]*/
            u[i + ku].imag = _imopVarPre264;
        }
        /*[]*/
        ku = ku + ln;
        /*[]*/
        ln = 2 * ln;
    }
}
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
static void cfftz(int is, int m , int n , dcomplex x[64][18] , dcomplex y[64][18]) {
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int i;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int j;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int l;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int mx;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    mx = (int) (u[0].real);
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int _imopVarPre266;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int _imopVarPre267;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int _imopVarPre268;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    _imopVarPre266 = is != 1;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    if (_imopVarPre266) {
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        _imopVarPre266 = is != -1;
    }
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    _imopVarPre267 = _imopVarPre266;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    if (!_imopVarPre267) {
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        _imopVarPre268 = m < 1;
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        if (!_imopVarPre268) {
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            _imopVarPre268 = m > mx;
        }
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        _imopVarPre267 = _imopVarPre268;
    }
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    if (_imopVarPre267) {
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        printf("CFFTZ: Either U has not been initialized, or else\n" "one of the input parameters is invalid%5d%5d%5d\n", is, m, mx);
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        exit(1);
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    }
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    for (l = 1; l <= m; l += 2) {
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        fftz2(is, l, m, n, fftblock, fftblockpad, u, x, y);
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        if (l == m) {
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            break;
        }
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        int _imopVarPre270;
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        _imopVarPre270 = l + 1;
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        fftz2(is, _imopVarPre270, m, n, fftblock, fftblockpad, u, y, x);
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    }
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    if (m % 2 == 1) {
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        for (j = 0; j < n; j++) {
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            for (i = 0; i < fftblock; i++) {
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                x[j][i].real = y[j][i].real;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                x[j][i].imag = y[j][i].imag;
            }
        }
    }
}
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
static void fftz2(int is, int l , int m , int n , int ny , int ny1 , dcomplex u[64] , dcomplex x[64][18] , dcomplex y[64][18]) {
/*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int k;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int n1;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int li;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int lj;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int lk;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int ku;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int i;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int j;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int i11;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int i12;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int i21;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    int i22;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    dcomplex u1;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    n1 = n / 2;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    if (l - 1 == 0) {
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        lk = 1;
    } else {
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        lk = 2 << ((l - 1) - 1);
    }
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    if (m - l == 0) {
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        li = 1;
    } else {
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        li = 2 << ((m - l) - 1);
    }
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    lj = 2 * lk;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    ku = li;
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
    for (i = 0; i < li; i++) {
    /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        i11 = i * lk;
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        i12 = i11 + n1;
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        i21 = i * lj;
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        i22 = i21 + lk;
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        if (is >= 1) {
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            u1.real = u[ku + i].real;
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            u1.imag = u[ku + i].imag;
        } else {
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            u1.real = u[ku + i].real;
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            u1.imag = -u[ku + i].imag;
        }
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
        for (k = 0; k < lk; k++) {
        /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
            for (j = 0; j < ny; j++) {
            /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                double x11real;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                double x11imag;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                double x21real;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                double x21imag;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                x11real = x[i11 + k][j].real;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                x11imag = x[i11 + k][j].imag;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                x21real = x[i12 + k][j].real;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                x21imag = x[i12 + k][j].imag;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                y[i21 + k][j].real = x11real + x21real;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                y[i21 + k][j].imag = x11imag + x21imag;
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                y[i22 + k][j].real = u1.real * (x11real - x21real) - u1.imag * (x11imag - x21imag);
                /*[3, 5, 7, 9, 11, 13, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 47, 49, 51, 53, 55, 57, 59, 61, 63]*/
                y[i22 + k][j].imag = u1.real * (x11imag - x21imag) + u1.imag * (x11real - x21real);
            }
        }
    }
}
/*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
/*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
static int ilog2(int n) {
/*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
    int nn;
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
    int lg;
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
    if (n == 1) {
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
        /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
        return 0;
    }
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
    lg = 1;
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
    nn = 2;
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
    while (nn < n) {
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
        /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
        nn = nn << 1;
        /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
        lg++;
    }
    /*[2, 4, 6, 8, 10, 12, 15, 17, 19, 21, 23, 25, 28, 29, 31, 33, 35, 37, 39, 46, 48, 50, 52, 54, 56, 58, 60, 62]*/
    return lg;
}
/*[]*/
/*[]*/
/*[]*/
/*[]*/
static void checksum(int i, dcomplex u1[64][64][64] , int d[3]) {
/*[]*/
    /*[]*/
    int j;
    /*[]*/
    int q;
    /*[]*/
    int r;
    /*[]*/
    int s;
    /*[]*/
    dcomplex chk;
    /*[]*/
    chk.real = 0.0;
    /*[]*/
    chk.imag = 0.0;
    /*[]*/
#pragma omp for nowait
    /*[]*/
    /*[]*/
    /*[]*/
    for (j = 1; j <= 1024; j++) {
    /*[]*/
        /*[]*/
        q = j % 64 + 1;
        /*[]*/
        int _imopVarPre272;
        /*[]*/
        _imopVarPre272 = q >= xstart[0];
        /*[]*/
        /*[]*/
        if (_imopVarPre272) {
        /*[]*/
            /*[]*/
            _imopVarPre272 = q <= xend[0];
        }
        /*[]*/
        /*[]*/
        if (_imopVarPre272) {
        /*[]*/
            /*[]*/
            r = (3 * j) % 64 + 1;
            /*[]*/
            int _imopVarPre274;
            /*[]*/
            _imopVarPre274 = r >= ystart[0];
            /*[]*/
            /*[]*/
            if (_imopVarPre274) {
            /*[]*/
                /*[]*/
                _imopVarPre274 = r <= yend[0];
            }
            /*[]*/
            /*[]*/
            if (_imopVarPre274) {
            /*[]*/
                /*[]*/
                s = (5 * j) % 64 + 1;
                /*[]*/
                int _imopVarPre276;
                /*[]*/
                _imopVarPre276 = s >= zstart[0];
                /*[]*/
                /*[]*/
                if (_imopVarPre276) {
                /*[]*/
                    /*[]*/
                    _imopVarPre276 = s <= zend[0];
                }
                /*[]*/
                /*[]*/
                if (_imopVarPre276) {
                /*[]*/
                    /*[]*/
                    chk.real = chk.real + u1[s - zstart[0]][r - ystart[0]][q - xstart[0]].real;
                    /*[]*/
                    (chk.imag = chk.imag + u1[s - zstart[0]][r - ystart[0]][q - xstart[0]].imag);
                }
            }
        }
    }
    /*[]*/
    // #pragma omp dummyFlush CRITICAL_START
    /*[]*/
#pragma omp critical
    {
    /*[]*/
        /*[]*/
        sums[i].real += chk.real;
        /*[]*/
        sums[i].imag += chk.imag;
    }
    /*[]*/
    // #pragma omp dummyFlush CRITICAL_END
    /*[]*/
    // #pragma omp dummyFlush BARRIER_START
    /*[]*/
#pragma omp barrier
    /*[]*/
#pragma omp single nowait
    {
    /*[]*/
        /*[]*/
        sums[i].real = sums[i].real / (double) 262144;
        /*[]*/
        sums[i].imag = sums[i].imag / (double) 262144;
        /*[]*/
        double _imopVarPre279;
        /*[]*/
        double _imopVarPre280;
        /*[]*/
        _imopVarPre279 = sums[i].imag;
        /*[]*/
        _imopVarPre280 = sums[i].real;
        /*[]*/
        printf("T = %5d     Checksum = %22.12e %22.12e\n", i, _imopVarPre280, _imopVarPre279);
        /*[]*/
    }
}
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
/*[]*/
static void verify(int d1, int d2 , int d3 , int nt , boolean *verified , char *class) {
/*[]*/
    /*[]*/
    int i;
    /*[]*/
    double err;
    /*[]*/
    double epsilon;
    /*[]*/
    double vdata_real_s[6 + 1] = {0.0, 5.546087004964e+02 , 5.546385409189e+02 , 5.546148406171e+02 , 5.545423607415e+02 , 5.544255039624e+02 , 5.542683411902e+02};
    /*[]*/
    double vdata_imag_s[6 + 1] = {0.0, 4.845363331978e+02 , 4.865304269511e+02 , 4.883910722336e+02 , 4.901273169046e+02 , 4.917475857993e+02 , 4.932597244941e+02};
    /*[]*/
    double vdata_real_w[6 + 1] = {0.0, 5.673612178944e+02 , 5.631436885271e+02 , 5.594024089970e+02 , 5.560698047020e+02 , 5.530898991250e+02 , 5.504159734538e+02};
    /*[]*/
    double vdata_imag_w[6 + 1] = {0.0, 5.293246849175e+02 , 5.282149986629e+02 , 5.270996558037e+02 , 5.260027904925e+02 , 5.249400845633e+02 , 5.239212247086e+02};
    /*[]*/
    double vdata_real_a[6 + 1] = {0.0, 5.046735008193e+02 , 5.059412319734e+02 , 5.069376896287e+02 , 5.077892868474e+02 , 5.085233095391e+02 , 5.091487099959e+02};
    /*[]*/
    double vdata_imag_a[6 + 1] = {0.0, 5.114047905510e+02 , 5.098809666433e+02 , 5.098144042213e+02 , 5.101336130759e+02 , 5.104914655194e+02 , 5.107917842803e+02};
    /*[]*/
    double vdata_real_b[20 + 1] = {0.0, 5.177643571579e+02 , 5.154521291263e+02 , 5.146409228649e+02 , 5.142378756213e+02 , 5.139626667737e+02 , 5.137423460082e+02 , 5.135547056878e+02 , 5.133910925466e+02 , 5.132470705390e+02 , 5.131197729984e+02 , 5.130070319283e+02 , 5.129070537032e+02 , 5.128182883502e+02 , 5.127393733383e+02 , 5.126691062020e+02 , 5.126064276004e+02 , 5.125504076570e+02 , 5.125002331720e+02 , 5.124551951846e+02 , 5.124146770029e+02};
    /*[]*/
    double vdata_imag_b[20 + 1] = {0.0, 5.077803458597e+02 , 5.088249431599e+02 , 5.096208912659e+02 , 5.101023387619e+02 , 5.103976610617e+02 , 5.105948019802e+02 , 5.107404165783e+02 , 5.108576573661e+02 , 5.109577278523e+02 , 5.110460304483e+02 , 5.111252433800e+02 , 5.111968077718e+02 , 5.112616233064e+02 , 5.113203605551e+02 , 5.113735928093e+02 , 5.114218460548e+02 , 5.114656139760e+02 , 5.115053595966e+02 , 5.115415130407e+02 , 5.115744692211e+02};
    /*[]*/
    double vdata_real_c[20 + 1] = {0.0, 5.195078707457e+02 , 5.155422171134e+02 , 5.144678022222e+02 , 5.140150594328e+02 , 5.137550426810e+02 , 5.135811056728e+02 , 5.134569343165e+02 , 5.133651975661e+02 , 5.132955192805e+02 , 5.132410471738e+02 , 5.131971141679e+02 , 5.131605205716e+02 , 5.131290734194e+02 , 5.131012720314e+02 , 5.130760908195e+02 , 5.130528295923e+02 , 5.130310107773e+02 , 5.130103090133e+02 , 5.129905029333e+02 , 5.129714421109e+02};
    /*[]*/
    double vdata_imag_c[20 + 1] = {0.0, 5.149019699238e+02 , 5.127578201997e+02 , 5.122251847514e+02 , 5.121090289018e+02 , 5.121143685824e+02 , 5.121496764568e+02 , 5.121870921893e+02 , 5.122193250322e+02 , 5.122454735794e+02 , 5.122663649603e+02 , 5.122830879827e+02 , 5.122965869718e+02 , 5.123075927445e+02 , 5.123166486553e+02 , 5.123241541685e+02 , 5.123304037599e+02 , 5.123356167976e+02 , 5.123399592211e+02 , 5.123435588985e+02 , 5.123465164008e+02};
    /*[]*/
    epsilon = 1.0e-12;
    /*[]*/
    *verified = 1;
    /*[]*/
    *class = 'U';
    /*[]*/
    int _imopVarPre284;
    /*[]*/
    int _imopVarPre285;
    /*[]*/
    int _imopVarPre286;
    /*[]*/
    _imopVarPre284 = d1 == 64;
    /*[]*/
    /*[]*/
    if (_imopVarPre284) {
    /*[]*/
        /*[]*/
        _imopVarPre285 = d2 == 64;
        /*[]*/
        /*[]*/
        if (_imopVarPre285) {
        /*[]*/
            /*[]*/
            _imopVarPre286 = d3 == 64;
            /*[]*/
            /*[]*/
            if (_imopVarPre286) {
            /*[]*/
                /*[]*/
                _imopVarPre286 = nt == 6;
            }
            /*[]*/
            _imopVarPre285 = _imopVarPre286;
        }
        /*[]*/
        _imopVarPre284 = _imopVarPre285;
    }
    /*[]*/
    /*[]*/
    if (_imopVarPre284) {
    /*[]*/
        /*[]*/
        *class = 'S';
        /*[]*/
        /*[]*/
        /*[]*/
        /*[]*/
        for (i = 1; i <= nt; i++) {
        /*[]*/
            /*[]*/
            err = (sums[i].real - vdata_real_s[i]) / vdata_real_s[i];
            /*[]*/
            double _imopVarPre288;
            /*[]*/
            _imopVarPre288 = fabs(err);
            /*[]*/
            /*[]*/
            /*[]*/
            if (_imopVarPre288 > epsilon) {
            /*[]*/
                /*[]*/
                *verified = 0;
                /*[]*/
                break;
            }
            /*[]*/
            err = (sums[i].imag - vdata_imag_s[i]) / vdata_imag_s[i];
            /*[]*/
            double _imopVarPre290;
            /*[]*/
            _imopVarPre290 = fabs(err);
            /*[]*/
            /*[]*/
            /*[]*/
            if (_imopVarPre290 > epsilon) {
            /*[]*/
                /*[]*/
                *verified = 0;
                /*[]*/
                break;
            }
        }
    } else {
    /*[]*/
        /*[]*/
        int _imopVarPre294;
        /*[]*/
        int _imopVarPre295;
        /*[]*/
        int _imopVarPre296;
        /*[]*/
        _imopVarPre294 = d1 == 128;
        /*[]*/
        /*[]*/
        if (_imopVarPre294) {
        /*[]*/
            /*[]*/
            _imopVarPre295 = d2 == 128;
            /*[]*/
            /*[]*/
            if (_imopVarPre295) {
            /*[]*/
                /*[]*/
                _imopVarPre296 = d3 == 32;
                /*[]*/
                /*[]*/
                if (_imopVarPre296) {
                /*[]*/
                    /*[]*/
                    _imopVarPre296 = nt == 6;
                }
                /*[]*/
                _imopVarPre295 = _imopVarPre296;
            }
            /*[]*/
            _imopVarPre294 = _imopVarPre295;
        }
        /*[]*/
        /*[]*/
        if (_imopVarPre294) {
        /*[]*/
            /*[]*/
            *class = 'W';
            /*[]*/
            /*[]*/
            /*[]*/
            /*[]*/
            for (i = 1; i <= nt; i++) {
            /*[]*/
                /*[]*/
                err = (sums[i].real - vdata_real_w[i]) / vdata_real_w[i];
                /*[]*/
                double _imopVarPre298;
                /*[]*/
                _imopVarPre298 = fabs(err);
                /*[]*/
                /*[]*/
                /*[]*/
                if (_imopVarPre298 > epsilon) {
                /*[]*/
                    /*[]*/
                    *verified = 0;
                    /*[]*/
                    break;
                }
                /*[]*/
                err = (sums[i].imag - vdata_imag_w[i]) / vdata_imag_w[i];
                /*[]*/
                double _imopVarPre300;
                /*[]*/
                _imopVarPre300 = fabs(err);
                /*[]*/
                /*[]*/
                /*[]*/
                if (_imopVarPre300 > epsilon) {
                /*[]*/
                    /*[]*/
                    *verified = 0;
                    /*[]*/
                    break;
                }
            }
        } else {
        /*[]*/
            /*[]*/
            int _imopVarPre304;
            /*[]*/
            int _imopVarPre305;
            /*[]*/
            int _imopVarPre306;
            /*[]*/
            _imopVarPre304 = d1 == 256;
            /*[]*/
            /*[]*/
            if (_imopVarPre304) {
            /*[]*/
                /*[]*/
                _imopVarPre305 = d2 == 256;
                /*[]*/
                /*[]*/
                if (_imopVarPre305) {
                /*[]*/
                    /*[]*/
                    _imopVarPre306 = d3 == 128;
                    /*[]*/
                    /*[]*/
                    if (_imopVarPre306) {
                    /*[]*/
                        /*[]*/
                        _imopVarPre306 = nt == 6;
                    }
                    /*[]*/
                    _imopVarPre305 = _imopVarPre306;
                }
                /*[]*/
                _imopVarPre304 = _imopVarPre305;
            }
            /*[]*/
            /*[]*/
            if (_imopVarPre304) {
            /*[]*/
                /*[]*/
                *class = 'A';
                /*[]*/
                /*[]*/
                /*[]*/
                /*[]*/
                for (i = 1; i <= nt; i++) {
                /*[]*/
                    /*[]*/
                    err = (sums[i].real - vdata_real_a[i]) / vdata_real_a[i];
                    /*[]*/
                    double _imopVarPre308;
                    /*[]*/
                    _imopVarPre308 = fabs(err);
                    /*[]*/
                    /*[]*/
                    /*[]*/
                    if (_imopVarPre308 > epsilon) {
                    /*[]*/
                        /*[]*/
                        *verified = 0;
                        /*[]*/
                        break;
                    }
                    /*[]*/
                    err = (sums[i].imag - vdata_imag_a[i]) / vdata_imag_a[i];
                    /*[]*/
                    double _imopVarPre310;
                    /*[]*/
                    _imopVarPre310 = fabs(err);
                    /*[]*/
                    /*[]*/
                    /*[]*/
                    if (_imopVarPre310 > epsilon) {
                    /*[]*/
                        /*[]*/
                        *verified = 0;
                        /*[]*/
                        break;
                    }
                }
            } else {
            /*[]*/
                /*[]*/
                int _imopVarPre314;
                /*[]*/
                int _imopVarPre315;
                /*[]*/
                int _imopVarPre316;
                /*[]*/
                _imopVarPre314 = d1 == 512;
                /*[]*/
                /*[]*/
                if (_imopVarPre314) {
                /*[]*/
                    /*[]*/
                    _imopVarPre315 = d2 == 256;
                    /*[]*/
                    /*[]*/
                    if (_imopVarPre315) {
                    /*[]*/
                        /*[]*/
                        _imopVarPre316 = d3 == 256;
                        /*[]*/
                        /*[]*/
                        if (_imopVarPre316) {
                        /*[]*/
                            /*[]*/
                            _imopVarPre316 = nt == 20;
                        }
                        /*[]*/
                        _imopVarPre315 = _imopVarPre316;
                    }
                    /*[]*/
                    _imopVarPre314 = _imopVarPre315;
                }
                /*[]*/
                /*[]*/
                if (_imopVarPre314) {
                /*[]*/
                    /*[]*/
                    *class = 'B';
                    /*[]*/
                    /*[]*/
                    /*[]*/
                    /*[]*/
                    for (i = 1; i <= nt; i++) {
                    /*[]*/
                        /*[]*/
                        err = (sums[i].real - vdata_real_b[i]) / vdata_real_b[i];
                        /*[]*/
                        double _imopVarPre318;
                        /*[]*/
                        _imopVarPre318 = fabs(err);
                        /*[]*/
                        /*[]*/
                        /*[]*/
                        if (_imopVarPre318 > epsilon) {
                        /*[]*/
                            /*[]*/
                            *verified = 0;
                            /*[]*/
                            break;
                        }
                        /*[]*/
                        err = (sums[i].imag - vdata_imag_b[i]) / vdata_imag_b[i];
                        /*[]*/
                        double _imopVarPre320;
                        /*[]*/
                        _imopVarPre320 = fabs(err);
                        /*[]*/
                        /*[]*/
                        /*[]*/
                        if (_imopVarPre320 > epsilon) {
                        /*[]*/
                            /*[]*/
                            *verified = 0;
                            /*[]*/
                            break;
                        }
                    }
                } else {
                /*[]*/
                    /*[]*/
                    int _imopVarPre324;
                    /*[]*/
                    int _imopVarPre325;
                    /*[]*/
                    int _imopVarPre326;
                    /*[]*/
                    _imopVarPre324 = d1 == 512;
                    /*[]*/
                    /*[]*/
                    if (_imopVarPre324) {
                    /*[]*/
                        /*[]*/
                        _imopVarPre325 = d2 == 512;
                        /*[]*/
                        /*[]*/
                        if (_imopVarPre325) {
                        /*[]*/
                            /*[]*/
                            _imopVarPre326 = d3 == 512;
                            /*[]*/
                            /*[]*/
                            if (_imopVarPre326) {
                            /*[]*/
                                /*[]*/
                                _imopVarPre326 = nt == 20;
                            }
                            /*[]*/
                            _imopVarPre325 = _imopVarPre326;
                        }
                        /*[]*/
                        _imopVarPre324 = _imopVarPre325;
                    }
                    /*[]*/
                    /*[]*/
                    if (_imopVarPre324) {
                    /*[]*/
                        /*[]*/
                        *class = 'C';
                        /*[]*/
                        /*[]*/
                        /*[]*/
                        /*[]*/
                        for (i = 1; i <= nt; i++) {
                        /*[]*/
                            /*[]*/
                            err = (sums[i].real - vdata_real_c[i]) / vdata_real_c[i];
                            /*[]*/
                            double _imopVarPre328;
                            /*[]*/
                            _imopVarPre328 = fabs(err);
                            /*[]*/
                            /*[]*/
                            /*[]*/
                            if (_imopVarPre328 > epsilon) {
                            /*[]*/
                                /*[]*/
                                *verified = 0;
                                /*[]*/
                                break;
                            }
                            /*[]*/
                            err = (sums[i].imag - vdata_imag_c[i]) / vdata_imag_c[i];
                            /*[]*/
                            double _imopVarPre330;
                            /*[]*/
                            _imopVarPre330 = fabs(err);
                            /*[]*/
                            /*[]*/
                            /*[]*/
                            if (_imopVarPre330 > epsilon) {
                            /*[]*/
                                /*[]*/
                                *verified = 0;
                                /*[]*/
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    /*[]*/
    /*[]*/
    if (*class != 'U') {
    /*[]*/
        /*[]*/
        printf("Result verification successful\n");
        /*[]*/
    } else {
    /*[]*/
        /*[]*/
        printf("Result verification failed\n");
        /*[]*/
    }
    /*[]*/
    char _imopVarPre332;
    /*[]*/
    _imopVarPre332 = *class;
    /*[]*/
    printf("class = %1c\n", _imopVarPre332);
    /*[]*/
}
