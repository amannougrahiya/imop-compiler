typedef long long __int64_t;
typedef __int64_t __darwin_off_t;
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
int fclose(FILE *);
int fgetc(FILE *);
FILE *fopen(const char *restrict __filename, const char *restrict __mode);
int fscanf(FILE *restrict , const char *restrict , ...);
int printf(const char *restrict , ...);
void exit(int );
extern double fabs(double );
extern double sqrt(double );
extern int omp_get_num_threads(void );
typedef int boolean;
extern void timer_clear(int );
extern void timer_start(int );
extern void timer_stop(int );
extern double timer_read(int );
extern void c_print_results(char *name, char class , int n1 , int n2 , int n3 , int niter , int nthreads , double t , double mops , char *optype , int passed_verification , char *npbversion , char *compiletime , char *cc , char *clink , char *c_lib , char *c_inc , char *cflags , char *clinkflags , char *rand);
static int nx;
static int ny;
static int nz;
static int nx0;
static int ny0;
static int nz0;
static int ist;
static int iend;
static int jst;
static int jend;
static int ii1;
static int ii2;
static int ji1;
static int ji2;
static int ki1;
static int ki2;
static double dxi;
static double deta;
static double dzeta;
static double tx1;
static double tx2;
static double tx3;
static double ty1;
static double ty2;
static double ty3;
static double tz1;
static double tz2;
static double tz3;
static double dx1;
static double dx2;
static double dx3;
static double dx4;
static double dx5;
static double dy1;
static double dy2;
static double dy3;
static double dy4;
static double dy5;
static double dz1;
static double dz2;
static double dz3;
static double dz4;
static double dz5;
static double dssp;
static double u[12][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5];
static double rsd[12][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5];
static double frct[12][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5];
static double flux[12][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5];
static int ipr;
static int inorm;
static int itmax;
static double dt;
static double omega;
static double tolrsd[5];
static double rsdnm[5];
static double errnm[5];
static double frc;
static double a[12][12][5][5];
static double b[12][12][5][5];
static double c[12][12][5][5];
static double d[12][12][5][5];
static double ce[5][13];
static double maxtime;
static boolean flag[12 / 2 * 2 + 1];
static void blts(int nx, int ny , int nz , int k , double omega , double v[12][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5] , double ldz[12][12][5][5] , double ldy[12][12][5][5] , double ldx[12][12][5][5] , double d[12][12][5][5] , int ist , int iend , int jst , int jend , int nx0 , int ny0);
static void buts(int nx, int ny , int nz , int k , double omega , double v[12][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5] , double tv[12][12][5] , double d[12][12][5][5] , double udx[12][12][5][5] , double udy[12][12][5][5] , double udz[12][12][5][5] , int ist , int iend , int jst , int jend , int nx0 , int ny0);
static void domain(void );
static void erhs(void );
static void error(void );
static void exact(int i, int j , int k , double u000ijk[5]);
static void jacld(int k);
static void jacu(int k);
static void l2norm(int nx0, int ny0 , int nz0 , int ist , int iend , int jst , int jend , double v[12][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5] , double sum[5]);
static void pintgr(void );
static void read_input(void );
static void rhs(void );
static void setbv(void );
static void setcoeff(void );
static void setiv(void );
static void ssor(void );
static void verify(double xcr[5], double xce[5] , double xci , char *class , boolean *verified);
int main(int argc, char **argv) {
    char class;
    boolean verified;
    double mflops;
    int nthreads = 1;
    read_input();
    domain();
    setcoeff();
    setbv();
    setiv();
    erhs();
#pragma omp parallel
    {
#pragma omp master
        {
            nthreads = omp_get_num_threads();
        }
    }
    ssor();
    error();
    pintgr();
    int *_imopVarPre144;
    char *_imopVarPre145;
    _imopVarPre144 = &verified;
    _imopVarPre145 = &class;
    verify(rsdnm, errnm, frc, _imopVarPre145, _imopVarPre144);
    mflops = (double) itmax * (1984.77 * (double) nx0 * (double) ny0 * (double) nz0 - 10923.3 * (((double) (nx0 + ny0 + nz0) / 3.0) * ((double) (nx0 + ny0 + nz0) / 3.0)) + 27770.9 * (double) (nx0 + ny0 + nz0) / 3.0 - 144010.0) / (maxtime * 1000000.0);
    c_print_results("LU", class, nx0, ny0, nz0, itmax, nthreads, maxtime, mflops, "          floating point", verified, "3.0 structured", "21 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "(none)");
}
static void blts(int nx, int ny , int nz , int k , double omega , double v[12][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5] , double ldz[12][12][5][5] , double ldy[12][12][5][5] , double ldx[12][12][5][5] , double d[12][12][5][5] , int ist , int iend , int jst , int jend , int nx0 , int ny0) {
    int i;
    int j;
    int m;
    double tmp;
    double tmp1;
    double tmat[5][5];
#pragma omp for nowait schedule(static)
    for (i = ist; i <= iend; i++) {
        for (j = jst; j <= jend; j++) {
            for (m = 0; m < 5; m++) {
                v[i][j][k][m] = v[i][j][k][m] - omega * (ldz[i][j][m][0] * v[i][j][k - 1][0] + ldz[i][j][m][1] * v[i][j][k - 1][1] + ldz[i][j][m][2] * v[i][j][k - 1][2] + ldz[i][j][m][3] * v[i][j][k - 1][3] + ldz[i][j][m][4] * v[i][j][k - 1][4]);
            }
        }
    }
#pragma omp for nowait schedule(static)
    for (i = ist; i <= iend; i++) {
        if (i != ist) {
            while (flag[i - 1] == 0) {
// #pragma omp dummyFlush FLUSH_START
#pragma omp flush(flag)
                ;
            }
        }
        if (i != iend) {
            while (flag[i] == 1) {
// #pragma omp dummyFlush FLUSH_START
#pragma omp flush(flag)
                ;
            }
        }
        for (j = jst; j <= jend; j++) {
            for (m = 0; m < 5; m++) {
                v[i][j][k][m] = v[i][j][k][m] - omega * (ldy[i][j][m][0] * v[i][j - 1][k][0] + ldx[i][j][m][0] * v[i - 1][j][k][0] + ldy[i][j][m][1] * v[i][j - 1][k][1] + ldx[i][j][m][1] * v[i - 1][j][k][1] + ldy[i][j][m][2] * v[i][j - 1][k][2] + ldx[i][j][m][2] * v[i - 1][j][k][2] + ldy[i][j][m][3] * v[i][j - 1][k][3] + ldx[i][j][m][3] * v[i - 1][j][k][3] + ldy[i][j][m][4] * v[i][j - 1][k][4] + ldx[i][j][m][4] * v[i - 1][j][k][4]);
            }
            for (m = 0; m < 5; m++) {
                tmat[m][0] = d[i][j][m][0];
                tmat[m][1] = d[i][j][m][1];
                tmat[m][2] = d[i][j][m][2];
                tmat[m][3] = d[i][j][m][3];
                tmat[m][4] = d[i][j][m][4];
            }
            tmp1 = 1.0 / tmat[0][0];
            tmp = tmp1 * tmat[1][0];
            tmat[1][1] = tmat[1][1] - tmp * tmat[0][1];
            tmat[1][2] = tmat[1][2] - tmp * tmat[0][2];
            tmat[1][3] = tmat[1][3] - tmp * tmat[0][3];
            tmat[1][4] = tmat[1][4] - tmp * tmat[0][4];
            v[i][j][k][1] = v[i][j][k][1] - v[i][j][k][0] * tmp;
            tmp = tmp1 * tmat[2][0];
            tmat[2][1] = tmat[2][1] - tmp * tmat[0][1];
            tmat[2][2] = tmat[2][2] - tmp * tmat[0][2];
            tmat[2][3] = tmat[2][3] - tmp * tmat[0][3];
            tmat[2][4] = tmat[2][4] - tmp * tmat[0][4];
            v[i][j][k][2] = v[i][j][k][2] - v[i][j][k][0] * tmp;
            tmp = tmp1 * tmat[3][0];
            tmat[3][1] = tmat[3][1] - tmp * tmat[0][1];
            tmat[3][2] = tmat[3][2] - tmp * tmat[0][2];
            tmat[3][3] = tmat[3][3] - tmp * tmat[0][3];
            tmat[3][4] = tmat[3][4] - tmp * tmat[0][4];
            v[i][j][k][3] = v[i][j][k][3] - v[i][j][k][0] * tmp;
            tmp = tmp1 * tmat[4][0];
            tmat[4][1] = tmat[4][1] - tmp * tmat[0][1];
            tmat[4][2] = tmat[4][2] - tmp * tmat[0][2];
            tmat[4][3] = tmat[4][3] - tmp * tmat[0][3];
            tmat[4][4] = tmat[4][4] - tmp * tmat[0][4];
            v[i][j][k][4] = v[i][j][k][4] - v[i][j][k][0] * tmp;
            tmp1 = 1.0 / tmat[1][1];
            tmp = tmp1 * tmat[2][1];
            tmat[2][2] = tmat[2][2] - tmp * tmat[1][2];
            tmat[2][3] = tmat[2][3] - tmp * tmat[1][3];
            tmat[2][4] = tmat[2][4] - tmp * tmat[1][4];
            v[i][j][k][2] = v[i][j][k][2] - v[i][j][k][1] * tmp;
            tmp = tmp1 * tmat[3][1];
            tmat[3][2] = tmat[3][2] - tmp * tmat[1][2];
            tmat[3][3] = tmat[3][3] - tmp * tmat[1][3];
            tmat[3][4] = tmat[3][4] - tmp * tmat[1][4];
            v[i][j][k][3] = v[i][j][k][3] - v[i][j][k][1] * tmp;
            tmp = tmp1 * tmat[4][1];
            tmat[4][2] = tmat[4][2] - tmp * tmat[1][2];
            tmat[4][3] = tmat[4][3] - tmp * tmat[1][3];
            tmat[4][4] = tmat[4][4] - tmp * tmat[1][4];
            v[i][j][k][4] = v[i][j][k][4] - v[i][j][k][1] * tmp;
            tmp1 = 1.0 / tmat[2][2];
            tmp = tmp1 * tmat[3][2];
            tmat[3][3] = tmat[3][3] - tmp * tmat[2][3];
            tmat[3][4] = tmat[3][4] - tmp * tmat[2][4];
            v[i][j][k][3] = v[i][j][k][3] - v[i][j][k][2] * tmp;
            tmp = tmp1 * tmat[4][2];
            tmat[4][3] = tmat[4][3] - tmp * tmat[2][3];
            tmat[4][4] = tmat[4][4] - tmp * tmat[2][4];
            v[i][j][k][4] = v[i][j][k][4] - v[i][j][k][2] * tmp;
            tmp1 = 1.0 / tmat[3][3];
            tmp = tmp1 * tmat[4][3];
            tmat[4][4] = tmat[4][4] - tmp * tmat[3][4];
            v[i][j][k][4] = v[i][j][k][4] - v[i][j][k][3] * tmp;
            v[i][j][k][4] = v[i][j][k][4] / tmat[4][4];
            v[i][j][k][3] = v[i][j][k][3] - tmat[3][4] * v[i][j][k][4];
            v[i][j][k][3] = v[i][j][k][3] / tmat[3][3];
            v[i][j][k][2] = v[i][j][k][2] - tmat[2][3] * v[i][j][k][3] - tmat[2][4] * v[i][j][k][4];
            v[i][j][k][2] = v[i][j][k][2] / tmat[2][2];
            v[i][j][k][1] = v[i][j][k][1] - tmat[1][2] * v[i][j][k][2] - tmat[1][3] * v[i][j][k][3] - tmat[1][4] * v[i][j][k][4];
            v[i][j][k][1] = v[i][j][k][1] / tmat[1][1];
            v[i][j][k][0] = v[i][j][k][0] - tmat[0][1] * v[i][j][k][1] - tmat[0][2] * v[i][j][k][2] - tmat[0][3] * v[i][j][k][3] - tmat[0][4] * v[i][j][k][4];
            v[i][j][k][0] = v[i][j][k][0] / tmat[0][0];
        }
        if (i != ist) {
            flag[i - 1] = 0;
        }
        if (i != iend) {
            flag[i] = 1;
        }
// #pragma omp dummyFlush FLUSH_START
#pragma omp flush(flag)
    }
}
static void buts(int nx, int ny , int nz , int k , double omega , double v[12][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5] , double tv[12][12][5] , double d[12][12][5][5] , double udx[12][12][5][5] , double udy[12][12][5][5] , double udz[12][12][5][5] , int ist , int iend , int jst , int jend , int nx0 , int ny0) {
    int i;
    int j;
    int m;
    double tmp;
    double tmp1;
    double tmat[5][5];
#pragma omp for nowait schedule(static)
    for (i = iend; i >= ist; i--) {
        for (j = jend; j >= jst; j--) {
            for (m = 0; m < 5; m++) {
                tv[i][j][m] = omega * (udz[i][j][m][0] * v[i][j][k + 1][0] + udz[i][j][m][1] * v[i][j][k + 1][1] + udz[i][j][m][2] * v[i][j][k + 1][2] + udz[i][j][m][3] * v[i][j][k + 1][3] + udz[i][j][m][4] * v[i][j][k + 1][4]);
            }
        }
    }
#pragma omp for nowait schedule(static)
    for (i = iend; i >= ist; i--) {
        if (i != iend) {
            while (flag[i + 1] == 0) {
// #pragma omp dummyFlush FLUSH_START
#pragma omp flush(flag)
                ;
            }
        }
        if (i != ist) {
            while (flag[i] == 1) {
// #pragma omp dummyFlush FLUSH_START
#pragma omp flush(flag)
                ;
            }
        }
        for (j = jend; j >= jst; j--) {
            for (m = 0; m < 5; m++) {
                tv[i][j][m] = tv[i][j][m] + omega * (udy[i][j][m][0] * v[i][j + 1][k][0] + udx[i][j][m][0] * v[i + 1][j][k][0] + udy[i][j][m][1] * v[i][j + 1][k][1] + udx[i][j][m][1] * v[i + 1][j][k][1] + udy[i][j][m][2] * v[i][j + 1][k][2] + udx[i][j][m][2] * v[i + 1][j][k][2] + udy[i][j][m][3] * v[i][j + 1][k][3] + udx[i][j][m][3] * v[i + 1][j][k][3] + udy[i][j][m][4] * v[i][j + 1][k][4] + udx[i][j][m][4] * v[i + 1][j][k][4]);
            }
            for (m = 0; m < 5; m++) {
                tmat[m][0] = d[i][j][m][0];
                tmat[m][1] = d[i][j][m][1];
                tmat[m][2] = d[i][j][m][2];
                tmat[m][3] = d[i][j][m][3];
                tmat[m][4] = d[i][j][m][4];
            }
            tmp1 = 1.0 / tmat[0][0];
            tmp = tmp1 * tmat[1][0];
            tmat[1][1] = tmat[1][1] - tmp * tmat[0][1];
            tmat[1][2] = tmat[1][2] - tmp * tmat[0][2];
            tmat[1][3] = tmat[1][3] - tmp * tmat[0][3];
            tmat[1][4] = tmat[1][4] - tmp * tmat[0][4];
            tv[i][j][1] = tv[i][j][1] - tv[i][j][0] * tmp;
            tmp = tmp1 * tmat[2][0];
            tmat[2][1] = tmat[2][1] - tmp * tmat[0][1];
            tmat[2][2] = tmat[2][2] - tmp * tmat[0][2];
            tmat[2][3] = tmat[2][3] - tmp * tmat[0][3];
            tmat[2][4] = tmat[2][4] - tmp * tmat[0][4];
            tv[i][j][2] = tv[i][j][2] - tv[i][j][0] * tmp;
            tmp = tmp1 * tmat[3][0];
            tmat[3][1] = tmat[3][1] - tmp * tmat[0][1];
            tmat[3][2] = tmat[3][2] - tmp * tmat[0][2];
            tmat[3][3] = tmat[3][3] - tmp * tmat[0][3];
            tmat[3][4] = tmat[3][4] - tmp * tmat[0][4];
            tv[i][j][3] = tv[i][j][3] - tv[i][j][0] * tmp;
            tmp = tmp1 * tmat[4][0];
            tmat[4][1] = tmat[4][1] - tmp * tmat[0][1];
            tmat[4][2] = tmat[4][2] - tmp * tmat[0][2];
            tmat[4][3] = tmat[4][3] - tmp * tmat[0][3];
            tmat[4][4] = tmat[4][4] - tmp * tmat[0][4];
            tv[i][j][4] = tv[i][j][4] - tv[i][j][0] * tmp;
            tmp1 = 1.0 / tmat[1][1];
            tmp = tmp1 * tmat[2][1];
            tmat[2][2] = tmat[2][2] - tmp * tmat[1][2];
            tmat[2][3] = tmat[2][3] - tmp * tmat[1][3];
            tmat[2][4] = tmat[2][4] - tmp * tmat[1][4];
            tv[i][j][2] = tv[i][j][2] - tv[i][j][1] * tmp;
            tmp = tmp1 * tmat[3][1];
            tmat[3][2] = tmat[3][2] - tmp * tmat[1][2];
            tmat[3][3] = tmat[3][3] - tmp * tmat[1][3];
            tmat[3][4] = tmat[3][4] - tmp * tmat[1][4];
            tv[i][j][3] = tv[i][j][3] - tv[i][j][1] * tmp;
            tmp = tmp1 * tmat[4][1];
            tmat[4][2] = tmat[4][2] - tmp * tmat[1][2];
            tmat[4][3] = tmat[4][3] - tmp * tmat[1][3];
            tmat[4][4] = tmat[4][4] - tmp * tmat[1][4];
            tv[i][j][4] = tv[i][j][4] - tv[i][j][1] * tmp;
            tmp1 = 1.0 / tmat[2][2];
            tmp = tmp1 * tmat[3][2];
            tmat[3][3] = tmat[3][3] - tmp * tmat[2][3];
            tmat[3][4] = tmat[3][4] - tmp * tmat[2][4];
            tv[i][j][3] = tv[i][j][3] - tv[i][j][2] * tmp;
            tmp = tmp1 * tmat[4][2];
            tmat[4][3] = tmat[4][3] - tmp * tmat[2][3];
            tmat[4][4] = tmat[4][4] - tmp * tmat[2][4];
            tv[i][j][4] = tv[i][j][4] - tv[i][j][2] * tmp;
            tmp1 = 1.0 / tmat[3][3];
            tmp = tmp1 * tmat[4][3];
            tmat[4][4] = tmat[4][4] - tmp * tmat[3][4];
            tv[i][j][4] = tv[i][j][4] - tv[i][j][3] * tmp;
            tv[i][j][4] = tv[i][j][4] / tmat[4][4];
            tv[i][j][3] = tv[i][j][3] - tmat[3][4] * tv[i][j][4];
            tv[i][j][3] = tv[i][j][3] / tmat[3][3];
            tv[i][j][2] = tv[i][j][2] - tmat[2][3] * tv[i][j][3] - tmat[2][4] * tv[i][j][4];
            tv[i][j][2] = tv[i][j][2] / tmat[2][2];
            tv[i][j][1] = tv[i][j][1] - tmat[1][2] * tv[i][j][2] - tmat[1][3] * tv[i][j][3] - tmat[1][4] * tv[i][j][4];
            tv[i][j][1] = tv[i][j][1] / tmat[1][1];
            tv[i][j][0] = tv[i][j][0] - tmat[0][1] * tv[i][j][1] - tmat[0][2] * tv[i][j][2] - tmat[0][3] * tv[i][j][3] - tmat[0][4] * tv[i][j][4];
            tv[i][j][0] = tv[i][j][0] / tmat[0][0];
            v[i][j][k][0] = v[i][j][k][0] - tv[i][j][0];
            v[i][j][k][1] = v[i][j][k][1] - tv[i][j][1];
            v[i][j][k][2] = v[i][j][k][2] - tv[i][j][2];
            v[i][j][k][3] = v[i][j][k][3] - tv[i][j][3];
            v[i][j][k][4] = v[i][j][k][4] - tv[i][j][4];
        }
        if (i != iend) {
            flag[i + 1] = 0;
        }
        if (i != ist) {
            flag[i] = 1;
        }
// #pragma omp dummyFlush FLUSH_START
#pragma omp flush(flag)
    }
}
static void domain(void ) {
    nx = nx0;
    ny = ny0;
    nz = nz0;
    int _imopVarPre146;
    int _imopVarPre147;
    _imopVarPre146 = nx < 4;
    if (!_imopVarPre146) {
        _imopVarPre147 = ny < 4;
        if (!_imopVarPre147) {
            _imopVarPre147 = nz < 4;
        }
        _imopVarPre146 = _imopVarPre147;
    }
    if (_imopVarPre146) {
        printf("     SUBDOMAIN SIZE IS TOO SMALL - \n" "     ADJUST PROBLEM SIZE OR NUMBER OF PROCESSORS\n" "     SO THAT NX, NY AND NZ ARE GREATER THAN OR EQUAL\n" "     TO 4 THEY ARE CURRENTLY%3d%3d%3d\n", nx, ny, nz);
        exit(1);
    }
    int _imopVarPre148;
    int _imopVarPre149;
    _imopVarPre148 = nx > 12;
    if (!_imopVarPre148) {
        _imopVarPre149 = ny > 12;
        if (!_imopVarPre149) {
            _imopVarPre149 = nz > 12;
        }
        _imopVarPre148 = _imopVarPre149;
    }
    if (_imopVarPre148) {
        printf("     SUBDOMAIN SIZE IS TOO LARGE - \n" "     ADJUST PROBLEM SIZE OR NUMBER OF PROCESSORS\n" "     SO THAT NX, NY AND NZ ARE LESS THAN OR EQUAL TO \n" "     ISIZ1, ISIZ2 AND ISIZ3 RESPECTIVELY.  THEY ARE\n" "     CURRENTLY%4d%4d%4d\n", nx, ny, nz);
        exit(1);
    }
    ist = 1;
    iend = nx - 2;
    jst = 1;
    jend = ny - 2;
}
static void erhs(void ) {
#pragma omp parallel
    {
        int i;
        int j;
        int k;
        int m;
        int iglob;
        int jglob;
        int L1;
        int L2;
        int ist1;
        int iend1;
        int jst1;
        int jend1;
        double dsspm;
        double xi;
        double eta;
        double zeta;
        double q;
        double u21;
        double u31;
        double u41;
        double tmp;
        double u21i;
        double u31i;
        double u41i;
        double u51i;
        double u21j;
        double u31j;
        double u41j;
        double u51j;
        double u21k;
        double u31k;
        double u41k;
        double u51k;
        double u21im1;
        double u31im1;
        double u41im1;
        double u51im1;
        double u21jm1;
        double u31jm1;
        double u41jm1;
        double u51jm1;
        double u21km1;
        double u31km1;
        double u41km1;
        double u51km1;
        dsspm = dssp;
#pragma omp for nowait
        for (i = 0; i < nx; i++) {
            for (j = 0; j < ny; j++) {
                for (k = 0; k < nz; k++) {
                    for (m = 0; m < 5; m++) {
                        frct[i][j][k][m] = 0.0;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (i = 0; i < nx; i++) {
            iglob = i;
            xi = ((double) iglob) / (nx0 - 1);
            for (j = 0; j < ny; j++) {
                jglob = j;
                eta = ((double) jglob) / (ny0 - 1);
                for (k = 0; k < nz; k++) {
                    zeta = ((double) k) / (nz - 1);
                    for (m = 0; m < 5; m++) {
                        rsd[i][j][k][m] = ce[m][0] + ce[m][1] * xi + ce[m][2] * eta + ce[m][3] * zeta + ce[m][4] * xi * xi + ce[m][5] * eta * eta + ce[m][6] * zeta * zeta + ce[m][7] * xi * xi * xi + ce[m][8] * eta * eta * eta + ce[m][9] * zeta * zeta * zeta + ce[m][10] * xi * xi * xi * xi + ce[m][11] * eta * eta * eta * eta + ce[m][12] * zeta * zeta * zeta * zeta;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        L1 = 0;
        L2 = nx - 1;
#pragma omp for nowait
        for (i = L1; i <= L2; i++) {
            for (j = jst; j <= jend; j++) {
                for (k = 1; k < nz - 1; k++) {
                    flux[i][j][k][0] = rsd[i][j][k][1];
                    u21 = rsd[i][j][k][1] / rsd[i][j][k][0];
                    q = 0.50 * (rsd[i][j][k][1] * rsd[i][j][k][1] + rsd[i][j][k][2] * rsd[i][j][k][2] + rsd[i][j][k][3] * rsd[i][j][k][3]) / rsd[i][j][k][0];
                    flux[i][j][k][1] = rsd[i][j][k][1] * u21 + 0.40e+00 * (rsd[i][j][k][4] - q);
                    flux[i][j][k][2] = rsd[i][j][k][2] * u21;
                    flux[i][j][k][3] = rsd[i][j][k][3] * u21;
                    flux[i][j][k][4] = (1.40e+00 * rsd[i][j][k][4] - 0.40e+00 * q) * u21;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (j = jst; j <= jend; j++) {
            for (k = 1; k <= nz - 2; k++) {
                for (i = ist; i <= iend; i++) {
                    for (m = 0; m < 5; m++) {
                        frct[i][j][k][m] = frct[i][j][k][m] - tx2 * (flux[i + 1][j][k][m] - flux[i - 1][j][k][m]);
                    }
                }
                for (i = ist; i <= L2; i++) {
                    tmp = 1.0 / rsd[i][j][k][0];
                    u21i = tmp * rsd[i][j][k][1];
                    u31i = tmp * rsd[i][j][k][2];
                    u41i = tmp * rsd[i][j][k][3];
                    u51i = tmp * rsd[i][j][k][4];
                    tmp = 1.0 / rsd[i - 1][j][k][0];
                    u21im1 = tmp * rsd[i - 1][j][k][1];
                    u31im1 = tmp * rsd[i - 1][j][k][2];
                    u41im1 = tmp * rsd[i - 1][j][k][3];
                    u51im1 = tmp * rsd[i - 1][j][k][4];
                    flux[i][j][k][1] = (4.0 / 3.0) * tx3 * (u21i - u21im1);
                    flux[i][j][k][2] = tx3 * (u31i - u31im1);
                    flux[i][j][k][3] = tx3 * (u41i - u41im1);
                    flux[i][j][k][4] = 0.50 * (1.0 - 1.40e+00 * 1.40e+00) * tx3 * ((u21i * u21i + u31i * u31i + u41i * u41i) - (u21im1 * u21im1 + u31im1 * u31im1 + u41im1 * u41im1)) + (1.0 / 6.0) * tx3 * (u21i * u21i - u21im1 * u21im1) + 1.40e+00 * 1.40e+00 * tx3 * (u51i - u51im1);
                }
                for (i = ist; i <= iend; i++) {
                    frct[i][j][k][0] = frct[i][j][k][0] + dx1 * tx1 * (rsd[i - 1][j][k][0] - 2.0 * rsd[i][j][k][0] + rsd[i + 1][j][k][0]);
                    frct[i][j][k][1] = frct[i][j][k][1] + tx3 * 1.00e-01 * 1.00e+00 * (flux[i + 1][j][k][1] - flux[i][j][k][1]) + dx2 * tx1 * (rsd[i - 1][j][k][1] - 2.0 * rsd[i][j][k][1] + rsd[i + 1][j][k][1]);
                    frct[i][j][k][2] = frct[i][j][k][2] + tx3 * 1.00e-01 * 1.00e+00 * (flux[i + 1][j][k][2] - flux[i][j][k][2]) + dx3 * tx1 * (rsd[i - 1][j][k][2] - 2.0 * rsd[i][j][k][2] + rsd[i + 1][j][k][2]);
                    frct[i][j][k][3] = frct[i][j][k][3] + tx3 * 1.00e-01 * 1.00e+00 * (flux[i + 1][j][k][3] - flux[i][j][k][3]) + dx4 * tx1 * (rsd[i - 1][j][k][3] - 2.0 * rsd[i][j][k][3] + rsd[i + 1][j][k][3]);
                    frct[i][j][k][4] = frct[i][j][k][4] + tx3 * 1.00e-01 * 1.00e+00 * (flux[i + 1][j][k][4] - flux[i][j][k][4]) + dx5 * tx1 * (rsd[i - 1][j][k][4] - 2.0 * rsd[i][j][k][4] + rsd[i + 1][j][k][4]);
                }
                for (m = 0; m < 5; m++) {
                    frct[1][j][k][m] = frct[1][j][k][m] - dsspm * (+5.0 * rsd[1][j][k][m] - 4.0 * rsd[2][j][k][m] + rsd[3][j][k][m]);
                    frct[2][j][k][m] = frct[2][j][k][m] - dsspm * (-4.0 * rsd[1][j][k][m] + 6.0 * rsd[2][j][k][m] - 4.0 * rsd[3][j][k][m] + rsd[4][j][k][m]);
                }
                ist1 = 3;
                iend1 = nx - 4;
                for (i = ist1; i <= iend1; i++) {
                    for (m = 0; m < 5; m++) {
                        frct[i][j][k][m] = frct[i][j][k][m] - dsspm * (rsd[i - 2][j][k][m] - 4.0 * rsd[i - 1][j][k][m] + 6.0 * rsd[i][j][k][m] - 4.0 * rsd[i + 1][j][k][m] + rsd[i + 2][j][k][m]);
                    }
                }
                for (m = 0; m < 5; m++) {
                    frct[nx - 3][j][k][m] = frct[nx - 3][j][k][m] - dsspm * (rsd[nx - 5][j][k][m] - 4.0 * rsd[nx - 4][j][k][m] + 6.0 * rsd[nx - 3][j][k][m] - 4.0 * rsd[nx - 2][j][k][m]);
                    frct[nx - 2][j][k][m] = frct[nx - 2][j][k][m] - dsspm * (rsd[nx - 4][j][k][m] - 4.0 * rsd[nx - 3][j][k][m] + 5.0 * rsd[nx - 2][j][k][m]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        L1 = 0;
        L2 = ny - 1;
#pragma omp for nowait
        for (i = ist; i <= iend; i++) {
            for (j = L1; j <= L2; j++) {
                for (k = 1; k <= nz - 2; k++) {
                    flux[i][j][k][0] = rsd[i][j][k][2];
                    u31 = rsd[i][j][k][2] / rsd[i][j][k][0];
                    q = 0.50 * (rsd[i][j][k][1] * rsd[i][j][k][1] + rsd[i][j][k][2] * rsd[i][j][k][2] + rsd[i][j][k][3] * rsd[i][j][k][3]) / rsd[i][j][k][0];
                    flux[i][j][k][1] = rsd[i][j][k][1] * u31;
                    flux[i][j][k][2] = rsd[i][j][k][2] * u31 + 0.40e+00 * (rsd[i][j][k][4] - q);
                    flux[i][j][k][3] = rsd[i][j][k][3] * u31;
                    flux[i][j][k][4] = (1.40e+00 * rsd[i][j][k][4] - 0.40e+00 * q) * u31;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (i = ist; i <= iend; i++) {
            for (k = 1; k <= nz - 2; k++) {
                for (j = jst; j <= jend; j++) {
                    for (m = 0; m < 5; m++) {
                        frct[i][j][k][m] = frct[i][j][k][m] - ty2 * (flux[i][j + 1][k][m] - flux[i][j - 1][k][m]);
                    }
                }
                for (j = jst; j <= L2; j++) {
                    tmp = 1.0 / rsd[i][j][k][0];
                    u21j = tmp * rsd[i][j][k][1];
                    u31j = tmp * rsd[i][j][k][2];
                    u41j = tmp * rsd[i][j][k][3];
                    u51j = tmp * rsd[i][j][k][4];
                    tmp = 1.0 / rsd[i][j - 1][k][0];
                    u21jm1 = tmp * rsd[i][j - 1][k][1];
                    u31jm1 = tmp * rsd[i][j - 1][k][2];
                    u41jm1 = tmp * rsd[i][j - 1][k][3];
                    u51jm1 = tmp * rsd[i][j - 1][k][4];
                    flux[i][j][k][1] = ty3 * (u21j - u21jm1);
                    flux[i][j][k][2] = (4.0 / 3.0) * ty3 * (u31j - u31jm1);
                    flux[i][j][k][3] = ty3 * (u41j - u41jm1);
                    flux[i][j][k][4] = 0.50 * (1.0 - 1.40e+00 * 1.40e+00) * ty3 * ((u21j * u21j + u31j * u31j + u41j * u41j) - (u21jm1 * u21jm1 + u31jm1 * u31jm1 + u41jm1 * u41jm1)) + (1.0 / 6.0) * ty3 * (u31j * u31j - u31jm1 * u31jm1) + 1.40e+00 * 1.40e+00 * ty3 * (u51j - u51jm1);
                }
                for (j = jst; j <= jend; j++) {
                    frct[i][j][k][0] = frct[i][j][k][0] + dy1 * ty1 * (rsd[i][j - 1][k][0] - 2.0 * rsd[i][j][k][0] + rsd[i][j + 1][k][0]);
                    frct[i][j][k][1] = frct[i][j][k][1] + ty3 * 1.00e-01 * 1.00e+00 * (flux[i][j + 1][k][1] - flux[i][j][k][1]) + dy2 * ty1 * (rsd[i][j - 1][k][1] - 2.0 * rsd[i][j][k][1] + rsd[i][j + 1][k][1]);
                    frct[i][j][k][2] = frct[i][j][k][2] + ty3 * 1.00e-01 * 1.00e+00 * (flux[i][j + 1][k][2] - flux[i][j][k][2]) + dy3 * ty1 * (rsd[i][j - 1][k][2] - 2.0 * rsd[i][j][k][2] + rsd[i][j + 1][k][2]);
                    frct[i][j][k][3] = frct[i][j][k][3] + ty3 * 1.00e-01 * 1.00e+00 * (flux[i][j + 1][k][3] - flux[i][j][k][3]) + dy4 * ty1 * (rsd[i][j - 1][k][3] - 2.0 * rsd[i][j][k][3] + rsd[i][j + 1][k][3]);
                    frct[i][j][k][4] = frct[i][j][k][4] + ty3 * 1.00e-01 * 1.00e+00 * (flux[i][j + 1][k][4] - flux[i][j][k][4]) + dy5 * ty1 * (rsd[i][j - 1][k][4] - 2.0 * rsd[i][j][k][4] + rsd[i][j + 1][k][4]);
                }
                for (m = 0; m < 5; m++) {
                    frct[i][1][k][m] = frct[i][1][k][m] - dsspm * (+5.0 * rsd[i][1][k][m] - 4.0 * rsd[i][2][k][m] + rsd[i][3][k][m]);
                    frct[i][2][k][m] = frct[i][2][k][m] - dsspm * (-4.0 * rsd[i][1][k][m] + 6.0 * rsd[i][2][k][m] - 4.0 * rsd[i][3][k][m] + rsd[i][4][k][m]);
                }
                jst1 = 3;
                jend1 = ny - 4;
                for (j = jst1; j <= jend1; j++) {
                    for (m = 0; m < 5; m++) {
                        frct[i][j][k][m] = frct[i][j][k][m] - dsspm * (rsd[i][j - 2][k][m] - 4.0 * rsd[i][j - 1][k][m] + 6.0 * rsd[i][j][k][m] - 4.0 * rsd[i][j + 1][k][m] + rsd[i][j + 2][k][m]);
                    }
                }
                for (m = 0; m < 5; m++) {
                    frct[i][ny - 3][k][m] = frct[i][ny - 3][k][m] - dsspm * (rsd[i][ny - 5][k][m] - 4.0 * rsd[i][ny - 4][k][m] + 6.0 * rsd[i][ny - 3][k][m] - 4.0 * rsd[i][ny - 2][k][m]);
                    frct[i][ny - 2][k][m] = frct[i][ny - 2][k][m] - dsspm * (rsd[i][ny - 4][k][m] - 4.0 * rsd[i][ny - 3][k][m] + 5.0 * rsd[i][ny - 2][k][m]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (i = ist; i <= iend; i++) {
            for (j = jst; j <= jend; j++) {
                for (k = 0; k <= nz - 1; k++) {
                    flux[i][j][k][0] = rsd[i][j][k][3];
                    u41 = rsd[i][j][k][3] / rsd[i][j][k][0];
                    q = 0.50 * (rsd[i][j][k][1] * rsd[i][j][k][1] + rsd[i][j][k][2] * rsd[i][j][k][2] + rsd[i][j][k][3] * rsd[i][j][k][3]) / rsd[i][j][k][0];
                    flux[i][j][k][1] = rsd[i][j][k][1] * u41;
                    flux[i][j][k][2] = rsd[i][j][k][2] * u41;
                    flux[i][j][k][3] = rsd[i][j][k][3] * u41 + 0.40e+00 * (rsd[i][j][k][4] - q);
                    flux[i][j][k][4] = (1.40e+00 * rsd[i][j][k][4] - 0.40e+00 * q) * u41;
                }
                for (k = 1; k <= nz - 2; k++) {
                    for (m = 0; m < 5; m++) {
                        frct[i][j][k][m] = frct[i][j][k][m] - tz2 * (flux[i][j][k + 1][m] - flux[i][j][k - 1][m]);
                    }
                }
                for (k = 1; k <= nz - 1; k++) {
                    tmp = 1.0 / rsd[i][j][k][0];
                    u21k = tmp * rsd[i][j][k][1];
                    u31k = tmp * rsd[i][j][k][2];
                    u41k = tmp * rsd[i][j][k][3];
                    u51k = tmp * rsd[i][j][k][4];
                    tmp = 1.0 / rsd[i][j][k - 1][0];
                    u21km1 = tmp * rsd[i][j][k - 1][1];
                    u31km1 = tmp * rsd[i][j][k - 1][2];
                    u41km1 = tmp * rsd[i][j][k - 1][3];
                    u51km1 = tmp * rsd[i][j][k - 1][4];
                    flux[i][j][k][1] = tz3 * (u21k - u21km1);
                    flux[i][j][k][2] = tz3 * (u31k - u31km1);
                    flux[i][j][k][3] = (4.0 / 3.0) * tz3 * (u41k - u41km1);
                    flux[i][j][k][4] = 0.50 * (1.0 - 1.40e+00 * 1.40e+00) * tz3 * ((u21k * u21k + u31k * u31k + u41k * u41k) - (u21km1 * u21km1 + u31km1 * u31km1 + u41km1 * u41km1)) + (1.0 / 6.0) * tz3 * (u41k * u41k - u41km1 * u41km1) + 1.40e+00 * 1.40e+00 * tz3 * (u51k - u51km1);
                }
                for (k = 1; k <= nz - 2; k++) {
                    frct[i][j][k][0] = frct[i][j][k][0] + dz1 * tz1 * (rsd[i][j][k + 1][0] - 2.0 * rsd[i][j][k][0] + rsd[i][j][k - 1][0]);
                    frct[i][j][k][1] = frct[i][j][k][1] + tz3 * 1.00e-01 * 1.00e+00 * (flux[i][j][k + 1][1] - flux[i][j][k][1]) + dz2 * tz1 * (rsd[i][j][k + 1][1] - 2.0 * rsd[i][j][k][1] + rsd[i][j][k - 1][1]);
                    frct[i][j][k][2] = frct[i][j][k][2] + tz3 * 1.00e-01 * 1.00e+00 * (flux[i][j][k + 1][2] - flux[i][j][k][2]) + dz3 * tz1 * (rsd[i][j][k + 1][2] - 2.0 * rsd[i][j][k][2] + rsd[i][j][k - 1][2]);
                    frct[i][j][k][3] = frct[i][j][k][3] + tz3 * 1.00e-01 * 1.00e+00 * (flux[i][j][k + 1][3] - flux[i][j][k][3]) + dz4 * tz1 * (rsd[i][j][k + 1][3] - 2.0 * rsd[i][j][k][3] + rsd[i][j][k - 1][3]);
                    frct[i][j][k][4] = frct[i][j][k][4] + tz3 * 1.00e-01 * 1.00e+00 * (flux[i][j][k + 1][4] - flux[i][j][k][4]) + dz5 * tz1 * (rsd[i][j][k + 1][4] - 2.0 * rsd[i][j][k][4] + rsd[i][j][k - 1][4]);
                }
                for (m = 0; m < 5; m++) {
                    frct[i][j][1][m] = frct[i][j][1][m] - dsspm * (+5.0 * rsd[i][j][1][m] - 4.0 * rsd[i][j][2][m] + rsd[i][j][3][m]);
                    frct[i][j][2][m] = frct[i][j][2][m] - dsspm * (-4.0 * rsd[i][j][1][m] + 6.0 * rsd[i][j][2][m] - 4.0 * rsd[i][j][3][m] + rsd[i][j][4][m]);
                }
                for (k = 3; k <= nz - 4; k++) {
                    for (m = 0; m < 5; m++) {
                        frct[i][j][k][m] = frct[i][j][k][m] - dsspm * (rsd[i][j][k - 2][m] - 4.0 * rsd[i][j][k - 1][m] + 6.0 * rsd[i][j][k][m] - 4.0 * rsd[i][j][k + 1][m] + rsd[i][j][k + 2][m]);
                    }
                }
                for (m = 0; m < 5; m++) {
                    frct[i][j][nz - 3][m] = frct[i][j][nz - 3][m] - dsspm * (rsd[i][j][nz - 5][m] - 4.0 * rsd[i][j][nz - 4][m] + 6.0 * rsd[i][j][nz - 3][m] - 4.0 * rsd[i][j][nz - 2][m]);
                    frct[i][j][nz - 2][m] = frct[i][j][nz - 2][m] - dsspm * (rsd[i][j][nz - 4][m] - 4.0 * rsd[i][j][nz - 3][m] + 5.0 * rsd[i][j][nz - 2][m]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
}
static void error(void ) {
    int i;
    int j;
    int k;
    int m;
    int iglob;
    int jglob;
    double tmp;
    double u000ijk[5];
    for (m = 0; m < 5; m++) {
        errnm[m] = 0.0;
    }
    for (i = ist; i <= iend; i++) {
        iglob = i;
        for (j = jst; j <= jend; j++) {
            jglob = j;
            for (k = 1; k <= nz - 2; k++) {
                exact(iglob, jglob, k, u000ijk);
                for (m = 0; m < 5; m++) {
                    tmp = (u000ijk[m] - u[i][j][k][m]);
                    errnm[m] = errnm[m] + tmp * tmp;
                }
            }
        }
    }
    for (m = 0; m < 5; m++) {
        double _imopVarPre151;
        double _imopVarPre152;
        _imopVarPre151 = errnm[m] / ((nx0 - 2) * (ny0 - 2) * (nz0 - 2));
        _imopVarPre152 = sqrt(_imopVarPre151);
        errnm[m] = _imopVarPre152;
    }
}
static void exact(int i, int j , int k , double u000ijk[5]) {
    int m;
    double xi;
    double eta;
    double zeta;
    xi = ((double) i) / (nx0 - 1);
    eta = ((double) j) / (ny0 - 1);
    zeta = ((double) k) / (nz - 1);
    for (m = 0; m < 5; m++) {
        u000ijk[m] = ce[m][0] + ce[m][1] * xi + ce[m][2] * eta + ce[m][3] * zeta + ce[m][4] * xi * xi + ce[m][5] * eta * eta + ce[m][6] * zeta * zeta + ce[m][7] * xi * xi * xi + ce[m][8] * eta * eta * eta + ce[m][9] * zeta * zeta * zeta + ce[m][10] * xi * xi * xi * xi + ce[m][11] * eta * eta * eta * eta + ce[m][12] * zeta * zeta * zeta * zeta;
    }
}
static void jacld(int k) {
    int i;
    int j;
    double r43;
    double c1345;
    double c34;
    double tmp1;
    double tmp2;
    double tmp3;
    r43 = (4.0 / 3.0);
    c1345 = 1.40e+00 * 1.00e-01 * 1.00e+00 * 1.40e+00;
    c34 = 1.00e-01 * 1.00e+00;
#pragma omp for nowait schedule(static)
    for (i = ist; i <= iend; i++) {
        for (j = jst; j <= jend; j++) {
            tmp1 = 1.0 / u[i][j][k][0];
            tmp2 = tmp1 * tmp1;
            tmp3 = tmp1 * tmp2;
            d[i][j][0][0] = 1.0 + dt * 2.0 * (tx1 * dx1 + ty1 * dy1 + tz1 * dz1);
            d[i][j][0][1] = 0.0;
            d[i][j][0][2] = 0.0;
            d[i][j][0][3] = 0.0;
            d[i][j][0][4] = 0.0;
            d[i][j][1][0] = dt * 2.0 * (tx1 * (-r43 * c34 * tmp2 * u[i][j][k][1]) + ty1 * (-c34 * tmp2 * u[i][j][k][1]) + tz1 * (-c34 * tmp2 * u[i][j][k][1]));
            d[i][j][1][1] = 1.0 + dt * 2.0 * (tx1 * r43 * c34 * tmp1 + ty1 * c34 * tmp1 + tz1 * c34 * tmp1) + dt * 2.0 * (tx1 * dx2 + ty1 * dy2 + tz1 * dz2);
            d[i][j][1][2] = 0.0;
            d[i][j][1][3] = 0.0;
            d[i][j][1][4] = 0.0;
            d[i][j][2][0] = dt * 2.0 * (tx1 * (-c34 * tmp2 * u[i][j][k][2]) + ty1 * (-r43 * c34 * tmp2 * u[i][j][k][2]) + tz1 * (-c34 * tmp2 * u[i][j][k][2]));
            d[i][j][2][1] = 0.0;
            d[i][j][2][2] = 1.0 + dt * 2.0 * (tx1 * c34 * tmp1 + ty1 * r43 * c34 * tmp1 + tz1 * c34 * tmp1) + dt * 2.0 * (tx1 * dx3 + ty1 * dy3 + tz1 * dz3);
            d[i][j][2][3] = 0.0;
            d[i][j][2][4] = 0.0;
            d[i][j][3][0] = dt * 2.0 * (tx1 * (-c34 * tmp2 * u[i][j][k][3]) + ty1 * (-c34 * tmp2 * u[i][j][k][3]) + tz1 * (-r43 * c34 * tmp2 * u[i][j][k][3]));
            d[i][j][3][1] = 0.0;
            d[i][j][3][2] = 0.0;
            d[i][j][3][3] = 1.0 + dt * 2.0 * (tx1 * c34 * tmp1 + ty1 * c34 * tmp1 + tz1 * r43 * c34 * tmp1) + dt * 2.0 * (tx1 * dx4 + ty1 * dy4 + tz1 * dz4);
            d[i][j][3][4] = 0.0;
            d[i][j][4][0] = dt * 2.0 * (tx1 * (-(r43 * c34 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (c34 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (c34 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4]) + ty1 * (-(c34 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (r43 * c34 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (c34 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4]) + tz1 * (-(c34 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (c34 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (r43 * c34 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4]));
            d[i][j][4][1] = dt * 2.0 * (tx1 * (r43 * c34 - c1345) * tmp2 * u[i][j][k][1] + ty1 * (c34 - c1345) * tmp2 * u[i][j][k][1] + tz1 * (c34 - c1345) * tmp2 * u[i][j][k][1]);
            d[i][j][4][2] = dt * 2.0 * (tx1 * (c34 - c1345) * tmp2 * u[i][j][k][2] + ty1 * (r43 * c34 - c1345) * tmp2 * u[i][j][k][2] + tz1 * (c34 - c1345) * tmp2 * u[i][j][k][2]);
            d[i][j][4][3] = dt * 2.0 * (tx1 * (c34 - c1345) * tmp2 * u[i][j][k][3] + ty1 * (c34 - c1345) * tmp2 * u[i][j][k][3] + tz1 * (r43 * c34 - c1345) * tmp2 * u[i][j][k][3]);
            d[i][j][4][4] = 1.0 + dt * 2.0 * (tx1 * c1345 * tmp1 + ty1 * c1345 * tmp1 + tz1 * c1345 * tmp1) + dt * 2.0 * (tx1 * dx5 + ty1 * dy5 + tz1 * dz5);
            tmp1 = 1.0 / u[i][j][k - 1][0];
            tmp2 = tmp1 * tmp1;
            tmp3 = tmp1 * tmp2;
            a[i][j][0][0] = -dt * tz1 * dz1;
            a[i][j][0][1] = 0.0;
            a[i][j][0][2] = 0.0;
            a[i][j][0][3] = -dt * tz2;
            a[i][j][0][4] = 0.0;
            a[i][j][1][0] = -dt * tz2 * (-(u[i][j][k - 1][1] * u[i][j][k - 1][3]) * tmp2) - dt * tz1 * (-c34 * tmp2 * u[i][j][k - 1][1]);
            a[i][j][1][1] = -dt * tz2 * (u[i][j][k - 1][3] * tmp1) - dt * tz1 * c34 * tmp1 - dt * tz1 * dz2;
            a[i][j][1][2] = 0.0;
            a[i][j][1][3] = -dt * tz2 * (u[i][j][k - 1][1] * tmp1);
            a[i][j][1][4] = 0.0;
            a[i][j][2][0] = -dt * tz2 * (-(u[i][j][k - 1][2] * u[i][j][k - 1][3]) * tmp2) - dt * tz1 * (-c34 * tmp2 * u[i][j][k - 1][2]);
            a[i][j][2][1] = 0.0;
            a[i][j][2][2] = -dt * tz2 * (u[i][j][k - 1][3] * tmp1) - dt * tz1 * (c34 * tmp1) - dt * tz1 * dz3;
            a[i][j][2][3] = -dt * tz2 * (u[i][j][k - 1][2] * tmp1);
            a[i][j][2][4] = 0.0;
            a[i][j][3][0] = -dt * tz2 * (-(u[i][j][k - 1][3] * tmp1) * (u[i][j][k - 1][3] * tmp1) + 0.50 * 0.40e+00 * ((u[i][j][k - 1][1] * u[i][j][k - 1][1] + u[i][j][k - 1][2] * u[i][j][k - 1][2] + u[i][j][k - 1][3] * u[i][j][k - 1][3]) * tmp2)) - dt * tz1 * (-r43 * c34 * tmp2 * u[i][j][k - 1][3]);
            a[i][j][3][1] = -dt * tz2 * (-0.40e+00 * (u[i][j][k - 1][1] * tmp1));
            a[i][j][3][2] = -dt * tz2 * (-0.40e+00 * (u[i][j][k - 1][2] * tmp1));
            a[i][j][3][3] = -dt * tz2 * (2.0 - 0.40e+00) * (u[i][j][k - 1][3] * tmp1) - dt * tz1 * (r43 * c34 * tmp1) - dt * tz1 * dz4;
            a[i][j][3][4] = -dt * tz2 * 0.40e+00;
            a[i][j][4][0] = -dt * tz2 * ((0.40e+00 * (u[i][j][k - 1][1] * u[i][j][k - 1][1] + u[i][j][k - 1][2] * u[i][j][k - 1][2] + u[i][j][k - 1][3] * u[i][j][k - 1][3]) * tmp2 - 1.40e+00 * (u[i][j][k - 1][4] * tmp1)) * (u[i][j][k - 1][3] * tmp1)) - dt * tz1 * (-(c34 - c1345) * tmp3 * (u[i][j][k - 1][1] * u[i][j][k - 1][1]) - (c34 - c1345) * tmp3 * (u[i][j][k - 1][2] * u[i][j][k - 1][2]) - (r43 * c34 - c1345) * tmp3 * (u[i][j][k - 1][3] * u[i][j][k - 1][3]) - c1345 * tmp2 * u[i][j][k - 1][4]);
            a[i][j][4][1] = -dt * tz2 * (-0.40e+00 * (u[i][j][k - 1][1] * u[i][j][k - 1][3]) * tmp2) - dt * tz1 * (c34 - c1345) * tmp2 * u[i][j][k - 1][1];
            a[i][j][4][2] = -dt * tz2 * (-0.40e+00 * (u[i][j][k - 1][2] * u[i][j][k - 1][3]) * tmp2) - dt * tz1 * (c34 - c1345) * tmp2 * u[i][j][k - 1][2];
            a[i][j][4][3] = -dt * tz2 * (1.40e+00 * (u[i][j][k - 1][4] * tmp1) - 0.50 * 0.40e+00 * ((u[i][j][k - 1][1] * u[i][j][k - 1][1] + u[i][j][k - 1][2] * u[i][j][k - 1][2] + 3.0 * u[i][j][k - 1][3] * u[i][j][k - 1][3]) * tmp2)) - dt * tz1 * (r43 * c34 - c1345) * tmp2 * u[i][j][k - 1][3];
            a[i][j][4][4] = -dt * tz2 * (1.40e+00 * (u[i][j][k - 1][3] * tmp1)) - dt * tz1 * c1345 * tmp1 - dt * tz1 * dz5;
            tmp1 = 1.0 / u[i][j - 1][k][0];
            tmp2 = tmp1 * tmp1;
            tmp3 = tmp1 * tmp2;
            b[i][j][0][0] = -dt * ty1 * dy1;
            b[i][j][0][1] = 0.0;
            b[i][j][0][2] = -dt * ty2;
            b[i][j][0][3] = 0.0;
            b[i][j][0][4] = 0.0;
            b[i][j][1][0] = -dt * ty2 * (-(u[i][j - 1][k][1] * u[i][j - 1][k][2]) * tmp2) - dt * ty1 * (-c34 * tmp2 * u[i][j - 1][k][1]);
            b[i][j][1][1] = -dt * ty2 * (u[i][j - 1][k][2] * tmp1) - dt * ty1 * (c34 * tmp1) - dt * ty1 * dy2;
            b[i][j][1][2] = -dt * ty2 * (u[i][j - 1][k][1] * tmp1);
            b[i][j][1][3] = 0.0;
            b[i][j][1][4] = 0.0;
            b[i][j][2][0] = -dt * ty2 * (-(u[i][j - 1][k][2] * tmp1) * (u[i][j - 1][k][2] * tmp1) + 0.50 * 0.40e+00 * ((u[i][j - 1][k][1] * u[i][j - 1][k][1] + u[i][j - 1][k][2] * u[i][j - 1][k][2] + u[i][j - 1][k][3] * u[i][j - 1][k][3]) * tmp2)) - dt * ty1 * (-r43 * c34 * tmp2 * u[i][j - 1][k][2]);
            b[i][j][2][1] = -dt * ty2 * (-0.40e+00 * (u[i][j - 1][k][1] * tmp1));
            b[i][j][2][2] = -dt * ty2 * ((2.0 - 0.40e+00) * (u[i][j - 1][k][2] * tmp1)) - dt * ty1 * (r43 * c34 * tmp1) - dt * ty1 * dy3;
            b[i][j][2][3] = -dt * ty2 * (-0.40e+00 * (u[i][j - 1][k][3] * tmp1));
            b[i][j][2][4] = -dt * ty2 * 0.40e+00;
            b[i][j][3][0] = -dt * ty2 * (-(u[i][j - 1][k][2] * u[i][j - 1][k][3]) * tmp2) - dt * ty1 * (-c34 * tmp2 * u[i][j - 1][k][3]);
            b[i][j][3][1] = 0.0;
            b[i][j][3][2] = -dt * ty2 * (u[i][j - 1][k][3] * tmp1);
            b[i][j][3][3] = -dt * ty2 * (u[i][j - 1][k][2] * tmp1) - dt * ty1 * (c34 * tmp1) - dt * ty1 * dy4;
            b[i][j][3][4] = 0.0;
            b[i][j][4][0] = -dt * ty2 * ((0.40e+00 * (u[i][j - 1][k][1] * u[i][j - 1][k][1] + u[i][j - 1][k][2] * u[i][j - 1][k][2] + u[i][j - 1][k][3] * u[i][j - 1][k][3]) * tmp2 - 1.40e+00 * (u[i][j - 1][k][4] * tmp1)) * (u[i][j - 1][k][2] * tmp1)) - dt * ty1 * (-(c34 - c1345) * tmp3 * (((u[i][j - 1][k][1]) * (u[i][j - 1][k][1]))) - (r43 * c34 - c1345) * tmp3 * (((u[i][j - 1][k][2]) * (u[i][j - 1][k][2]))) - (c34 - c1345) * tmp3 * (((u[i][j - 1][k][3]) * (u[i][j - 1][k][3]))) - c1345 * tmp2 * u[i][j - 1][k][4]);
            b[i][j][4][1] = -dt * ty2 * (-0.40e+00 * (u[i][j - 1][k][1] * u[i][j - 1][k][2]) * tmp2) - dt * ty1 * (c34 - c1345) * tmp2 * u[i][j - 1][k][1];
            b[i][j][4][2] = -dt * ty2 * (1.40e+00 * (u[i][j - 1][k][4] * tmp1) - 0.50 * 0.40e+00 * ((u[i][j - 1][k][1] * u[i][j - 1][k][1] + 3.0 * u[i][j - 1][k][2] * u[i][j - 1][k][2] + u[i][j - 1][k][3] * u[i][j - 1][k][3]) * tmp2)) - dt * ty1 * (r43 * c34 - c1345) * tmp2 * u[i][j - 1][k][2];
            b[i][j][4][3] = -dt * ty2 * (-0.40e+00 * (u[i][j - 1][k][2] * u[i][j - 1][k][3]) * tmp2) - dt * ty1 * (c34 - c1345) * tmp2 * u[i][j - 1][k][3];
            b[i][j][4][4] = -dt * ty2 * (1.40e+00 * (u[i][j - 1][k][2] * tmp1)) - dt * ty1 * c1345 * tmp1 - dt * ty1 * dy5;
            tmp1 = 1.0 / u[i - 1][j][k][0];
            tmp2 = tmp1 * tmp1;
            tmp3 = tmp1 * tmp2;
            c[i][j][0][0] = -dt * tx1 * dx1;
            c[i][j][0][1] = -dt * tx2;
            c[i][j][0][2] = 0.0;
            c[i][j][0][3] = 0.0;
            c[i][j][0][4] = 0.0;
            c[i][j][1][0] = -dt * tx2 * (-(u[i - 1][j][k][1] * tmp1) * (u[i - 1][j][k][1] * tmp1) + 0.40e+00 * 0.50 * (u[i - 1][j][k][1] * u[i - 1][j][k][1] + u[i - 1][j][k][2] * u[i - 1][j][k][2] + u[i - 1][j][k][3] * u[i - 1][j][k][3]) * tmp2) - dt * tx1 * (-r43 * c34 * tmp2 * u[i - 1][j][k][1]);
            c[i][j][1][1] = -dt * tx2 * ((2.0 - 0.40e+00) * (u[i - 1][j][k][1] * tmp1)) - dt * tx1 * (r43 * c34 * tmp1) - dt * tx1 * dx2;
            c[i][j][1][2] = -dt * tx2 * (-0.40e+00 * (u[i - 1][j][k][2] * tmp1));
            c[i][j][1][3] = -dt * tx2 * (-0.40e+00 * (u[i - 1][j][k][3] * tmp1));
            c[i][j][1][4] = -dt * tx2 * 0.40e+00;
            c[i][j][2][0] = -dt * tx2 * (-(u[i - 1][j][k][1] * u[i - 1][j][k][2]) * tmp2) - dt * tx1 * (-c34 * tmp2 * u[i - 1][j][k][2]);
            c[i][j][2][1] = -dt * tx2 * (u[i - 1][j][k][2] * tmp1);
            c[i][j][2][2] = -dt * tx2 * (u[i - 1][j][k][1] * tmp1) - dt * tx1 * (c34 * tmp1) - dt * tx1 * dx3;
            c[i][j][2][3] = 0.0;
            c[i][j][2][4] = 0.0;
            c[i][j][3][0] = -dt * tx2 * (-(u[i - 1][j][k][1] * u[i - 1][j][k][3]) * tmp2) - dt * tx1 * (-c34 * tmp2 * u[i - 1][j][k][3]);
            c[i][j][3][1] = -dt * tx2 * (u[i - 1][j][k][3] * tmp1);
            c[i][j][3][2] = 0.0;
            c[i][j][3][3] = -dt * tx2 * (u[i - 1][j][k][1] * tmp1) - dt * tx1 * (c34 * tmp1) - dt * tx1 * dx4;
            c[i][j][3][4] = 0.0;
            c[i][j][4][0] = -dt * tx2 * ((0.40e+00 * (u[i - 1][j][k][1] * u[i - 1][j][k][1] + u[i - 1][j][k][2] * u[i - 1][j][k][2] + u[i - 1][j][k][3] * u[i - 1][j][k][3]) * tmp2 - 1.40e+00 * (u[i - 1][j][k][4] * tmp1)) * (u[i - 1][j][k][1] * tmp1)) - dt * tx1 * (-(r43 * c34 - c1345) * tmp3 * (((u[i - 1][j][k][1]) * (u[i - 1][j][k][1]))) - (c34 - c1345) * tmp3 * (((u[i - 1][j][k][2]) * (u[i - 1][j][k][2]))) - (c34 - c1345) * tmp3 * (((u[i - 1][j][k][3]) * (u[i - 1][j][k][3]))) - c1345 * tmp2 * u[i - 1][j][k][4]);
            c[i][j][4][1] = -dt * tx2 * (1.40e+00 * (u[i - 1][j][k][4] * tmp1) - 0.50 * 0.40e+00 * ((3.0 * u[i - 1][j][k][1] * u[i - 1][j][k][1] + u[i - 1][j][k][2] * u[i - 1][j][k][2] + u[i - 1][j][k][3] * u[i - 1][j][k][3]) * tmp2)) - dt * tx1 * (r43 * c34 - c1345) * tmp2 * u[i - 1][j][k][1];
            c[i][j][4][2] = -dt * tx2 * (-0.40e+00 * (u[i - 1][j][k][2] * u[i - 1][j][k][1]) * tmp2) - dt * tx1 * (c34 - c1345) * tmp2 * u[i - 1][j][k][2];
            c[i][j][4][3] = -dt * tx2 * (-0.40e+00 * (u[i - 1][j][k][3] * u[i - 1][j][k][1]) * tmp2) - dt * tx1 * (c34 - c1345) * tmp2 * u[i - 1][j][k][3];
            c[i][j][4][4] = -dt * tx2 * (1.40e+00 * (u[i - 1][j][k][1] * tmp1)) - dt * tx1 * c1345 * tmp1 - dt * tx1 * dx5;
        }
    }
}
static void jacu(int k) {
    int i;
    int j;
    double r43;
    double c1345;
    double c34;
    double tmp1;
    double tmp2;
    double tmp3;
    r43 = (4.0 / 3.0);
    c1345 = 1.40e+00 * 1.00e-01 * 1.00e+00 * 1.40e+00;
    c34 = 1.00e-01 * 1.00e+00;
#pragma omp for nowait schedule(static)
    for (i = iend; i >= ist; i--) {
        for (j = jend; j >= jst; j--) {
            tmp1 = 1.0 / u[i][j][k][0];
            tmp2 = tmp1 * tmp1;
            tmp3 = tmp1 * tmp2;
            d[i][j][0][0] = 1.0 + dt * 2.0 * (tx1 * dx1 + ty1 * dy1 + tz1 * dz1);
            d[i][j][0][1] = 0.0;
            d[i][j][0][2] = 0.0;
            d[i][j][0][3] = 0.0;
            d[i][j][0][4] = 0.0;
            d[i][j][1][0] = dt * 2.0 * (tx1 * (-r43 * c34 * tmp2 * u[i][j][k][1]) + ty1 * (-c34 * tmp2 * u[i][j][k][1]) + tz1 * (-c34 * tmp2 * u[i][j][k][1]));
            d[i][j][1][1] = 1.0 + dt * 2.0 * (tx1 * r43 * c34 * tmp1 + ty1 * c34 * tmp1 + tz1 * c34 * tmp1) + dt * 2.0 * (tx1 * dx2 + ty1 * dy2 + tz1 * dz2);
            d[i][j][1][2] = 0.0;
            d[i][j][1][3] = 0.0;
            d[i][j][1][4] = 0.0;
            d[i][j][2][0] = dt * 2.0 * (tx1 * (-c34 * tmp2 * u[i][j][k][2]) + ty1 * (-r43 * c34 * tmp2 * u[i][j][k][2]) + tz1 * (-c34 * tmp2 * u[i][j][k][2]));
            d[i][j][2][1] = 0.0;
            d[i][j][2][2] = 1.0 + dt * 2.0 * (tx1 * c34 * tmp1 + ty1 * r43 * c34 * tmp1 + tz1 * c34 * tmp1) + dt * 2.0 * (tx1 * dx3 + ty1 * dy3 + tz1 * dz3);
            d[i][j][2][3] = 0.0;
            d[i][j][2][4] = 0.0;
            d[i][j][3][0] = dt * 2.0 * (tx1 * (-c34 * tmp2 * u[i][j][k][3]) + ty1 * (-c34 * tmp2 * u[i][j][k][3]) + tz1 * (-r43 * c34 * tmp2 * u[i][j][k][3]));
            d[i][j][3][1] = 0.0;
            d[i][j][3][2] = 0.0;
            d[i][j][3][3] = 1.0 + dt * 2.0 * (tx1 * c34 * tmp1 + ty1 * c34 * tmp1 + tz1 * r43 * c34 * tmp1) + dt * 2.0 * (tx1 * dx4 + ty1 * dy4 + tz1 * dz4);
            d[i][j][3][4] = 0.0;
            d[i][j][4][0] = dt * 2.0 * (tx1 * (-(r43 * c34 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (c34 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (c34 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4]) + ty1 * (-(c34 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (r43 * c34 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (c34 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4]) + tz1 * (-(c34 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (c34 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (r43 * c34 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4]));
            d[i][j][4][1] = dt * 2.0 * (tx1 * (r43 * c34 - c1345) * tmp2 * u[i][j][k][1] + ty1 * (c34 - c1345) * tmp2 * u[i][j][k][1] + tz1 * (c34 - c1345) * tmp2 * u[i][j][k][1]);
            d[i][j][4][2] = dt * 2.0 * (tx1 * (c34 - c1345) * tmp2 * u[i][j][k][2] + ty1 * (r43 * c34 - c1345) * tmp2 * u[i][j][k][2] + tz1 * (c34 - c1345) * tmp2 * u[i][j][k][2]);
            d[i][j][4][3] = dt * 2.0 * (tx1 * (c34 - c1345) * tmp2 * u[i][j][k][3] + ty1 * (c34 - c1345) * tmp2 * u[i][j][k][3] + tz1 * (r43 * c34 - c1345) * tmp2 * u[i][j][k][3]);
            d[i][j][4][4] = 1.0 + dt * 2.0 * (tx1 * c1345 * tmp1 + ty1 * c1345 * tmp1 + tz1 * c1345 * tmp1) + dt * 2.0 * (tx1 * dx5 + ty1 * dy5 + tz1 * dz5);
            tmp1 = 1.0 / u[i + 1][j][k][0];
            tmp2 = tmp1 * tmp1;
            tmp3 = tmp1 * tmp2;
            a[i][j][0][0] = -dt * tx1 * dx1;
            a[i][j][0][1] = dt * tx2;
            a[i][j][0][2] = 0.0;
            a[i][j][0][3] = 0.0;
            a[i][j][0][4] = 0.0;
            a[i][j][1][0] = dt * tx2 * (-(u[i + 1][j][k][1] * tmp1) * (u[i + 1][j][k][1] * tmp1) + 0.40e+00 * 0.50 * (u[i + 1][j][k][1] * u[i + 1][j][k][1] + u[i + 1][j][k][2] * u[i + 1][j][k][2] + u[i + 1][j][k][3] * u[i + 1][j][k][3]) * tmp2) - dt * tx1 * (-r43 * c34 * tmp2 * u[i + 1][j][k][1]);
            a[i][j][1][1] = dt * tx2 * ((2.0 - 0.40e+00) * (u[i + 1][j][k][1] * tmp1)) - dt * tx1 * (r43 * c34 * tmp1) - dt * tx1 * dx2;
            a[i][j][1][2] = dt * tx2 * (-0.40e+00 * (u[i + 1][j][k][2] * tmp1));
            a[i][j][1][3] = dt * tx2 * (-0.40e+00 * (u[i + 1][j][k][3] * tmp1));
            a[i][j][1][4] = dt * tx2 * 0.40e+00;
            a[i][j][2][0] = dt * tx2 * (-(u[i + 1][j][k][1] * u[i + 1][j][k][2]) * tmp2) - dt * tx1 * (-c34 * tmp2 * u[i + 1][j][k][2]);
            a[i][j][2][1] = dt * tx2 * (u[i + 1][j][k][2] * tmp1);
            a[i][j][2][2] = dt * tx2 * (u[i + 1][j][k][1] * tmp1) - dt * tx1 * (c34 * tmp1) - dt * tx1 * dx3;
            a[i][j][2][3] = 0.0;
            a[i][j][2][4] = 0.0;
            a[i][j][3][0] = dt * tx2 * (-(u[i + 1][j][k][1] * u[i + 1][j][k][3]) * tmp2) - dt * tx1 * (-c34 * tmp2 * u[i + 1][j][k][3]);
            a[i][j][3][1] = dt * tx2 * (u[i + 1][j][k][3] * tmp1);
            a[i][j][3][2] = 0.0;
            a[i][j][3][3] = dt * tx2 * (u[i + 1][j][k][1] * tmp1) - dt * tx1 * (c34 * tmp1) - dt * tx1 * dx4;
            a[i][j][3][4] = 0.0;
            a[i][j][4][0] = dt * tx2 * ((0.40e+00 * (u[i + 1][j][k][1] * u[i + 1][j][k][1] + u[i + 1][j][k][2] * u[i + 1][j][k][2] + u[i + 1][j][k][3] * u[i + 1][j][k][3]) * tmp2 - 1.40e+00 * (u[i + 1][j][k][4] * tmp1)) * (u[i + 1][j][k][1] * tmp1)) - dt * tx1 * (-(r43 * c34 - c1345) * tmp3 * (((u[i + 1][j][k][1]) * (u[i + 1][j][k][1]))) - (c34 - c1345) * tmp3 * (((u[i + 1][j][k][2]) * (u[i + 1][j][k][2]))) - (c34 - c1345) * tmp3 * (((u[i + 1][j][k][3]) * (u[i + 1][j][k][3]))) - c1345 * tmp2 * u[i + 1][j][k][4]);
            a[i][j][4][1] = dt * tx2 * (1.40e+00 * (u[i + 1][j][k][4] * tmp1) - 0.50 * 0.40e+00 * ((3.0 * u[i + 1][j][k][1] * u[i + 1][j][k][1] + u[i + 1][j][k][2] * u[i + 1][j][k][2] + u[i + 1][j][k][3] * u[i + 1][j][k][3]) * tmp2)) - dt * tx1 * (r43 * c34 - c1345) * tmp2 * u[i + 1][j][k][1];
            a[i][j][4][2] = dt * tx2 * (-0.40e+00 * (u[i + 1][j][k][2] * u[i + 1][j][k][1]) * tmp2) - dt * tx1 * (c34 - c1345) * tmp2 * u[i + 1][j][k][2];
            a[i][j][4][3] = dt * tx2 * (-0.40e+00 * (u[i + 1][j][k][3] * u[i + 1][j][k][1]) * tmp2) - dt * tx1 * (c34 - c1345) * tmp2 * u[i + 1][j][k][3];
            a[i][j][4][4] = dt * tx2 * (1.40e+00 * (u[i + 1][j][k][1] * tmp1)) - dt * tx1 * c1345 * tmp1 - dt * tx1 * dx5;
            tmp1 = 1.0 / u[i][j + 1][k][0];
            tmp2 = tmp1 * tmp1;
            tmp3 = tmp1 * tmp2;
            b[i][j][0][0] = -dt * ty1 * dy1;
            b[i][j][0][1] = 0.0;
            b[i][j][0][2] = dt * ty2;
            b[i][j][0][3] = 0.0;
            b[i][j][0][4] = 0.0;
            b[i][j][1][0] = dt * ty2 * (-(u[i][j + 1][k][1] * u[i][j + 1][k][2]) * tmp2) - dt * ty1 * (-c34 * tmp2 * u[i][j + 1][k][1]);
            b[i][j][1][1] = dt * ty2 * (u[i][j + 1][k][2] * tmp1) - dt * ty1 * (c34 * tmp1) - dt * ty1 * dy2;
            b[i][j][1][2] = dt * ty2 * (u[i][j + 1][k][1] * tmp1);
            b[i][j][1][3] = 0.0;
            b[i][j][1][4] = 0.0;
            b[i][j][2][0] = dt * ty2 * (-(u[i][j + 1][k][2] * tmp1) * (u[i][j + 1][k][2] * tmp1) + 0.50 * 0.40e+00 * ((u[i][j + 1][k][1] * u[i][j + 1][k][1] + u[i][j + 1][k][2] * u[i][j + 1][k][2] + u[i][j + 1][k][3] * u[i][j + 1][k][3]) * tmp2)) - dt * ty1 * (-r43 * c34 * tmp2 * u[i][j + 1][k][2]);
            b[i][j][2][1] = dt * ty2 * (-0.40e+00 * (u[i][j + 1][k][1] * tmp1));
            b[i][j][2][2] = dt * ty2 * ((2.0 - 0.40e+00) * (u[i][j + 1][k][2] * tmp1)) - dt * ty1 * (r43 * c34 * tmp1) - dt * ty1 * dy3;
            b[i][j][2][3] = dt * ty2 * (-0.40e+00 * (u[i][j + 1][k][3] * tmp1));
            b[i][j][2][4] = dt * ty2 * 0.40e+00;
            b[i][j][3][0] = dt * ty2 * (-(u[i][j + 1][k][2] * u[i][j + 1][k][3]) * tmp2) - dt * ty1 * (-c34 * tmp2 * u[i][j + 1][k][3]);
            b[i][j][3][1] = 0.0;
            b[i][j][3][2] = dt * ty2 * (u[i][j + 1][k][3] * tmp1);
            b[i][j][3][3] = dt * ty2 * (u[i][j + 1][k][2] * tmp1) - dt * ty1 * (c34 * tmp1) - dt * ty1 * dy4;
            b[i][j][3][4] = 0.0;
            b[i][j][4][0] = dt * ty2 * ((0.40e+00 * (u[i][j + 1][k][1] * u[i][j + 1][k][1] + u[i][j + 1][k][2] * u[i][j + 1][k][2] + u[i][j + 1][k][3] * u[i][j + 1][k][3]) * tmp2 - 1.40e+00 * (u[i][j + 1][k][4] * tmp1)) * (u[i][j + 1][k][2] * tmp1)) - dt * ty1 * (-(c34 - c1345) * tmp3 * (((u[i][j + 1][k][1]) * (u[i][j + 1][k][1]))) - (r43 * c34 - c1345) * tmp3 * (((u[i][j + 1][k][2]) * (u[i][j + 1][k][2]))) - (c34 - c1345) * tmp3 * (((u[i][j + 1][k][3]) * (u[i][j + 1][k][3]))) - c1345 * tmp2 * u[i][j + 1][k][4]);
            b[i][j][4][1] = dt * ty2 * (-0.40e+00 * (u[i][j + 1][k][1] * u[i][j + 1][k][2]) * tmp2) - dt * ty1 * (c34 - c1345) * tmp2 * u[i][j + 1][k][1];
            b[i][j][4][2] = dt * ty2 * (1.40e+00 * (u[i][j + 1][k][4] * tmp1) - 0.50 * 0.40e+00 * ((u[i][j + 1][k][1] * u[i][j + 1][k][1] + 3.0 * u[i][j + 1][k][2] * u[i][j + 1][k][2] + u[i][j + 1][k][3] * u[i][j + 1][k][3]) * tmp2)) - dt * ty1 * (r43 * c34 - c1345) * tmp2 * u[i][j + 1][k][2];
            b[i][j][4][3] = dt * ty2 * (-0.40e+00 * (u[i][j + 1][k][2] * u[i][j + 1][k][3]) * tmp2) - dt * ty1 * (c34 - c1345) * tmp2 * u[i][j + 1][k][3];
            b[i][j][4][4] = dt * ty2 * (1.40e+00 * (u[i][j + 1][k][2] * tmp1)) - dt * ty1 * c1345 * tmp1 - dt * ty1 * dy5;
            tmp1 = 1.0 / u[i][j][k + 1][0];
            tmp2 = tmp1 * tmp1;
            tmp3 = tmp1 * tmp2;
            c[i][j][0][0] = -dt * tz1 * dz1;
            c[i][j][0][1] = 0.0;
            c[i][j][0][2] = 0.0;
            c[i][j][0][3] = dt * tz2;
            c[i][j][0][4] = 0.0;
            c[i][j][1][0] = dt * tz2 * (-(u[i][j][k + 1][1] * u[i][j][k + 1][3]) * tmp2) - dt * tz1 * (-c34 * tmp2 * u[i][j][k + 1][1]);
            c[i][j][1][1] = dt * tz2 * (u[i][j][k + 1][3] * tmp1) - dt * tz1 * c34 * tmp1 - dt * tz1 * dz2;
            c[i][j][1][2] = 0.0;
            c[i][j][1][3] = dt * tz2 * (u[i][j][k + 1][1] * tmp1);
            c[i][j][1][4] = 0.0;
            c[i][j][2][0] = dt * tz2 * (-(u[i][j][k + 1][2] * u[i][j][k + 1][3]) * tmp2) - dt * tz1 * (-c34 * tmp2 * u[i][j][k + 1][2]);
            c[i][j][2][1] = 0.0;
            c[i][j][2][2] = dt * tz2 * (u[i][j][k + 1][3] * tmp1) - dt * tz1 * (c34 * tmp1) - dt * tz1 * dz3;
            c[i][j][2][3] = dt * tz2 * (u[i][j][k + 1][2] * tmp1);
            c[i][j][2][4] = 0.0;
            c[i][j][3][0] = dt * tz2 * (-(u[i][j][k + 1][3] * tmp1) * (u[i][j][k + 1][3] * tmp1) + 0.50 * 0.40e+00 * ((u[i][j][k + 1][1] * u[i][j][k + 1][1] + u[i][j][k + 1][2] * u[i][j][k + 1][2] + u[i][j][k + 1][3] * u[i][j][k + 1][3]) * tmp2)) - dt * tz1 * (-r43 * c34 * tmp2 * u[i][j][k + 1][3]);
            c[i][j][3][1] = dt * tz2 * (-0.40e+00 * (u[i][j][k + 1][1] * tmp1));
            c[i][j][3][2] = dt * tz2 * (-0.40e+00 * (u[i][j][k + 1][2] * tmp1));
            c[i][j][3][3] = dt * tz2 * (2.0 - 0.40e+00) * (u[i][j][k + 1][3] * tmp1) - dt * tz1 * (r43 * c34 * tmp1) - dt * tz1 * dz4;
            c[i][j][3][4] = dt * tz2 * 0.40e+00;
            c[i][j][4][0] = dt * tz2 * ((0.40e+00 * (u[i][j][k + 1][1] * u[i][j][k + 1][1] + u[i][j][k + 1][2] * u[i][j][k + 1][2] + u[i][j][k + 1][3] * u[i][j][k + 1][3]) * tmp2 - 1.40e+00 * (u[i][j][k + 1][4] * tmp1)) * (u[i][j][k + 1][3] * tmp1)) - dt * tz1 * (-(c34 - c1345) * tmp3 * (((u[i][j][k + 1][1]) * (u[i][j][k + 1][1]))) - (c34 - c1345) * tmp3 * (((u[i][j][k + 1][2]) * (u[i][j][k + 1][2]))) - (r43 * c34 - c1345) * tmp3 * (((u[i][j][k + 1][3]) * (u[i][j][k + 1][3]))) - c1345 * tmp2 * u[i][j][k + 1][4]);
            c[i][j][4][1] = dt * tz2 * (-0.40e+00 * (u[i][j][k + 1][1] * u[i][j][k + 1][3]) * tmp2) - dt * tz1 * (c34 - c1345) * tmp2 * u[i][j][k + 1][1];
            c[i][j][4][2] = dt * tz2 * (-0.40e+00 * (u[i][j][k + 1][2] * u[i][j][k + 1][3]) * tmp2) - dt * tz1 * (c34 - c1345) * tmp2 * u[i][j][k + 1][2];
            c[i][j][4][3] = dt * tz2 * (1.40e+00 * (u[i][j][k + 1][4] * tmp1) - 0.50 * 0.40e+00 * ((u[i][j][k + 1][1] * u[i][j][k + 1][1] + u[i][j][k + 1][2] * u[i][j][k + 1][2] + 3.0 * u[i][j][k + 1][3] * u[i][j][k + 1][3]) * tmp2)) - dt * tz1 * (r43 * c34 - c1345) * tmp2 * u[i][j][k + 1][3];
            c[i][j][4][4] = dt * tz2 * (1.40e+00 * (u[i][j][k + 1][3] * tmp1)) - dt * tz1 * c1345 * tmp1 - dt * tz1 * dz5;
        }
    }
}
static void l2norm(int nx0, int ny0 , int nz0 , int ist , int iend , int jst , int jend , double v[12][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5] , double sum[5]) {
#pragma omp parallel
    {
        int i;
        int j;
        int k;
        int m;
        double sum0 = 0.0;
        double sum1 = 0.0;
        double sum2 = 0.0;
        double sum3 = 0.0;
        double sum4 = 0.0;
#pragma omp single nowait
        {
            for (m = 0; m < 5; m++) {
                sum[m] = 0.0;
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (i = ist; i <= iend; i++) {
            for (j = jst; j <= jend; j++) {
                for (k = 1; k <= nz0 - 2; k++) {
                    sum0 = sum0 + v[i][j][k][0] * v[i][j][k][0];
                    sum1 = sum1 + v[i][j][k][1] * v[i][j][k][1];
                    sum2 = sum2 + v[i][j][k][2] * v[i][j][k][2];
                    sum3 = sum3 + v[i][j][k][3] * v[i][j][k][3];
                    sum4 = sum4 + v[i][j][k][4] * v[i][j][k][4];
                }
            }
        }
// #pragma omp dummyFlush CRITICAL_START
#pragma omp critical
        {
            sum[0] += sum0;
            sum[1] += sum1;
            sum[2] += sum2;
            sum[3] += sum3;
            sum[4] += sum4;
        }
// #pragma omp dummyFlush CRITICAL_END
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp single nowait
        {
            for (m = 0; m < 5; m++) {
                double _imopVarPre154;
                double _imopVarPre155;
                _imopVarPre154 = sum[m] / ((nx0 - 2) * (ny0 - 2) * (nz0 - 2));
                _imopVarPre155 = sqrt(_imopVarPre154);
                sum[m] = _imopVarPre155;
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
}
static void pintgr(void ) {
    int i;
    int j;
    int k;
    int ibeg;
    int ifin;
    int ifin1;
    int jbeg;
    int jfin;
    int jfin1;
    int iglob;
    int iglob1;
    int iglob2;
    int jglob;
    int jglob1;
    int jglob2;
    double phi1[12 + 2][12 + 2];
    double phi2[12 + 2][12 + 2];
    double frc1;
    double frc2;
    double frc3;
    ibeg = nx;
    ifin = 0;
    iglob1 = -1;
    iglob2 = nx - 1;
    int _imopVarPre157;
    _imopVarPre157 = iglob1 >= ii1;
    if (_imopVarPre157) {
        _imopVarPre157 = iglob2 < ii2 + nx;
    }
    if (_imopVarPre157) {
        ibeg = 0;
    }
    int _imopVarPre159;
    _imopVarPre159 = iglob1 >= ii1 - nx;
    if (_imopVarPre159) {
        _imopVarPre159 = iglob2 <= ii2;
    }
    if (_imopVarPre159) {
        ifin = nx;
    }
    int _imopVarPre161;
    _imopVarPre161 = ii1 >= iglob1;
    if (_imopVarPre161) {
        _imopVarPre161 = ii1 <= iglob2;
    }
    if (_imopVarPre161) {
        ibeg = ii1;
    }
    int _imopVarPre163;
    _imopVarPre163 = ii2 >= iglob1;
    if (_imopVarPre163) {
        _imopVarPre163 = ii2 <= iglob2;
    }
    if (_imopVarPre163) {
        ifin = ii2;
    }
    jbeg = ny;
    jfin = -1;
    jglob1 = 0;
    jglob2 = ny - 1;
    int _imopVarPre165;
    _imopVarPre165 = jglob1 >= ji1;
    if (_imopVarPre165) {
        _imopVarPre165 = jglob2 < ji2 + ny;
    }
    if (_imopVarPre165) {
        jbeg = 0;
    }
    int _imopVarPre167;
    _imopVarPre167 = jglob1 > ji1 - ny;
    if (_imopVarPre167) {
        _imopVarPre167 = jglob2 <= ji2;
    }
    if (_imopVarPre167) {
        jfin = ny;
    }
    int _imopVarPre169;
    _imopVarPre169 = ji1 >= jglob1;
    if (_imopVarPre169) {
        _imopVarPre169 = ji1 <= jglob2;
    }
    if (_imopVarPre169) {
        jbeg = ji1;
    }
    int _imopVarPre171;
    _imopVarPre171 = ji2 >= jglob1;
    if (_imopVarPre171) {
        _imopVarPre171 = ji2 <= jglob2;
    }
    if (_imopVarPre171) {
        jfin = ji2;
    }
    ifin1 = ifin;
    jfin1 = jfin;
    if (ifin1 == ii2) {
        ifin1 = ifin - 1;
    }
    if (jfin1 == ji2) {
        jfin1 = jfin - 1;
    }
    for (i = 0; i <= 12 + 1; i++) {
        for (k = 0; k <= 12 + 1; k++) {
            phi1[i][k] = 0.0;
            phi2[i][k] = 0.0;
        }
    }
    for (i = ibeg; i <= ifin; i++) {
        iglob = i;
        for (j = jbeg; j <= jfin; j++) {
            jglob = j;
            k = ki1;
            phi1[i][j] = 0.40e+00 * (u[i][j][k][4] - 0.50 * (((u[i][j][k][1]) * (u[i][j][k][1])) + ((u[i][j][k][2]) * (u[i][j][k][2])) + ((u[i][j][k][3]) * (u[i][j][k][3]))) / u[i][j][k][0]);
            k = ki2;
            phi2[i][j] = 0.40e+00 * (u[i][j][k][4] - 0.50 * (((u[i][j][k][1]) * (u[i][j][k][1])) + ((u[i][j][k][2]) * (u[i][j][k][2])) + ((u[i][j][k][3]) * (u[i][j][k][3]))) / u[i][j][k][0]);
        }
    }
    frc1 = 0.0;
    for (i = ibeg; i <= ifin1; i++) {
        for (j = jbeg; j <= jfin1; j++) {
            frc1 = frc1 + (phi1[i][j] + phi1[i + 1][j] + phi1[i][j + 1] + phi1[i + 1][j + 1] + phi2[i][j] + phi2[i + 1][j] + phi2[i][j + 1] + phi2[i + 1][j + 1]);
        }
    }
    frc1 = dxi * deta * frc1;
    for (i = 0; i <= 12 + 1; i++) {
        for (k = 0; k <= 12 + 1; k++) {
            phi1[i][k] = 0.0;
            phi2[i][k] = 0.0;
        }
    }
    jglob = jbeg;
    if (jglob == ji1) {
        for (i = ibeg; i <= ifin; i++) {
            iglob = i;
            for (k = ki1; k <= ki2; k++) {
                phi1[i][k] = 0.40e+00 * (u[i][jbeg][k][4] - 0.50 * (((u[i][jbeg][k][1]) * (u[i][jbeg][k][1])) + ((u[i][jbeg][k][2]) * (u[i][jbeg][k][2])) + ((u[i][jbeg][k][3]) * (u[i][jbeg][k][3]))) / u[i][jbeg][k][0]);
            }
        }
    }
    jglob = jfin;
    if (jglob == ji2) {
        for (i = ibeg; i <= ifin; i++) {
            iglob = i;
            for (k = ki1; k <= ki2; k++) {
                phi2[i][k] = 0.40e+00 * (u[i][jfin][k][4] - 0.50 * (((u[i][jfin][k][1]) * (u[i][jfin][k][1])) + ((u[i][jfin][k][2]) * (u[i][jfin][k][2])) + ((u[i][jfin][k][3]) * (u[i][jfin][k][3]))) / u[i][jfin][k][0]);
            }
        }
    }
    frc2 = 0.0;
    for (i = ibeg; i <= ifin1; i++) {
        for (k = ki1; k <= ki2 - 1; k++) {
            frc2 = frc2 + (phi1[i][k] + phi1[i + 1][k] + phi1[i][k + 1] + phi1[i + 1][k + 1] + phi2[i][k] + phi2[i + 1][k] + phi2[i][k + 1] + phi2[i + 1][k + 1]);
        }
    }
    frc2 = dxi * dzeta * frc2;
    for (i = 0; i <= 12 + 1; i++) {
        for (k = 0; k <= 12 + 1; k++) {
            phi1[i][k] = 0.0;
            phi2[i][k] = 0.0;
        }
    }
    iglob = ibeg;
    if (iglob == ii1) {
        for (j = jbeg; j <= jfin; j++) {
            jglob = j;
            for (k = ki1; k <= ki2; k++) {
                phi1[j][k] = 0.40e+00 * (u[ibeg][j][k][4] - 0.50 * (((u[ibeg][j][k][1]) * (u[ibeg][j][k][1])) + ((u[ibeg][j][k][2]) * (u[ibeg][j][k][2])) + ((u[ibeg][j][k][3]) * (u[ibeg][j][k][3]))) / u[ibeg][j][k][0]);
            }
        }
    }
    iglob = ifin;
    if (iglob == ii2) {
        for (j = jbeg; j <= jfin; j++) {
            jglob = j;
            for (k = ki1; k <= ki2; k++) {
                phi2[j][k] = 0.40e+00 * (u[ifin][j][k][4] - 0.50 * (((u[ifin][j][k][1]) * (u[ifin][j][k][1])) + ((u[ifin][j][k][2]) * (u[ifin][j][k][2])) + ((u[ifin][j][k][3]) * (u[ifin][j][k][3]))) / u[ifin][j][k][0]);
            }
        }
    }
    frc3 = 0.0;
    for (j = jbeg; j <= jfin1; j++) {
        for (k = ki1; k <= ki2 - 1; k++) {
            frc3 = frc3 + (phi1[j][k] + phi1[j + 1][k] + phi1[j][k + 1] + phi1[j + 1][k + 1] + phi2[j][k] + phi2[j + 1][k] + phi2[j][k + 1] + phi2[j + 1][k + 1]);
        }
    }
    frc3 = deta * dzeta * frc3;
    frc = 0.25 * (frc1 + frc2 + frc3);
}
static void read_input(void ) {
    FILE *fp;
    printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version" " - LU Benchmark\n\n");
    fp = fopen("inputlu.data", "r");
    if (fp != ((void *) 0)) {
        printf(" Reading from input file inputlu.data\n");
        int _imopVarPre173;
        _imopVarPre173 = fgetc(fp);
        while (_imopVarPre173 != '\n') {
            ;
            _imopVarPre173 = fgetc(fp);
        }
        int _imopVarPre175;
        _imopVarPre175 = fgetc(fp);
        while (_imopVarPre175 != '\n') {
            ;
            _imopVarPre175 = fgetc(fp);
        }
        int *_imopVarPre178;
        int *_imopVarPre179;
        _imopVarPre178 = &inorm;
        _imopVarPre179 = &ipr;
        fscanf(fp, "%d%d", _imopVarPre179, _imopVarPre178);
        int _imopVarPre181;
        _imopVarPre181 = fgetc(fp);
        while (_imopVarPre181 != '\n') {
            ;
            _imopVarPre181 = fgetc(fp);
        }
        int _imopVarPre183;
        _imopVarPre183 = fgetc(fp);
        while (_imopVarPre183 != '\n') {
            ;
            _imopVarPre183 = fgetc(fp);
        }
        int _imopVarPre185;
        _imopVarPre185 = fgetc(fp);
        while (_imopVarPre185 != '\n') {
            ;
            _imopVarPre185 = fgetc(fp);
        }
        int *_imopVarPre187;
        _imopVarPre187 = &itmax;
        fscanf(fp, "%d", _imopVarPre187);
        int _imopVarPre189;
        _imopVarPre189 = fgetc(fp);
        while (_imopVarPre189 != '\n') {
            ;
            _imopVarPre189 = fgetc(fp);
        }
        int _imopVarPre191;
        _imopVarPre191 = fgetc(fp);
        while (_imopVarPre191 != '\n') {
            ;
            _imopVarPre191 = fgetc(fp);
        }
        int _imopVarPre193;
        _imopVarPre193 = fgetc(fp);
        while (_imopVarPre193 != '\n') {
            ;
            _imopVarPre193 = fgetc(fp);
        }
        double *_imopVarPre195;
        _imopVarPre195 = &dt;
        fscanf(fp, "%lf", _imopVarPre195);
        int _imopVarPre197;
        _imopVarPre197 = fgetc(fp);
        while (_imopVarPre197 != '\n') {
            ;
            _imopVarPre197 = fgetc(fp);
        }
        int _imopVarPre199;
        _imopVarPre199 = fgetc(fp);
        while (_imopVarPre199 != '\n') {
            ;
            _imopVarPre199 = fgetc(fp);
        }
        int _imopVarPre201;
        _imopVarPre201 = fgetc(fp);
        while (_imopVarPre201 != '\n') {
            ;
            _imopVarPre201 = fgetc(fp);
        }
        double *_imopVarPre203;
        _imopVarPre203 = &omega;
        fscanf(fp, "%lf", _imopVarPre203);
        int _imopVarPre205;
        _imopVarPre205 = fgetc(fp);
        while (_imopVarPre205 != '\n') {
            ;
            _imopVarPre205 = fgetc(fp);
        }
        int _imopVarPre207;
        _imopVarPre207 = fgetc(fp);
        while (_imopVarPre207 != '\n') {
            ;
            _imopVarPre207 = fgetc(fp);
        }
        int _imopVarPre209;
        _imopVarPre209 = fgetc(fp);
        while (_imopVarPre209 != '\n') {
            ;
            _imopVarPre209 = fgetc(fp);
        }
        double *_imopVarPre215;
        double *_imopVarPre216;
        double *_imopVarPre217;
        double *_imopVarPre218;
        double *_imopVarPre219;
        _imopVarPre215 = &tolrsd[4];
        _imopVarPre216 = &tolrsd[3];
        _imopVarPre217 = &tolrsd[2];
        _imopVarPre218 = &tolrsd[1];
        _imopVarPre219 = &tolrsd[0];
        fscanf(fp, "%lf%lf%lf%lf%lf", _imopVarPre219, _imopVarPre218, _imopVarPre217, _imopVarPre216, _imopVarPre215);
        int _imopVarPre221;
        _imopVarPre221 = fgetc(fp);
        while (_imopVarPre221 != '\n') {
            ;
            _imopVarPre221 = fgetc(fp);
        }
        int _imopVarPre223;
        _imopVarPre223 = fgetc(fp);
        while (_imopVarPre223 != '\n') {
            ;
            _imopVarPre223 = fgetc(fp);
        }
        int _imopVarPre225;
        _imopVarPre225 = fgetc(fp);
        while (_imopVarPre225 != '\n') {
            ;
            _imopVarPre225 = fgetc(fp);
        }
        int *_imopVarPre229;
        int *_imopVarPre230;
        int *_imopVarPre231;
        _imopVarPre229 = &nz0;
        _imopVarPre230 = &ny0;
        _imopVarPre231 = &nx0;
        fscanf(fp, "%d%d%d", _imopVarPre231, _imopVarPre230, _imopVarPre229);
        int _imopVarPre233;
        _imopVarPre233 = fgetc(fp);
        while (_imopVarPre233 != '\n') {
            ;
            _imopVarPre233 = fgetc(fp);
        }
        fclose(fp);
    } else {
        ipr = 1;
        inorm = 50;
        itmax = 50;
        dt = 0.5;
        omega = 1.2;
        tolrsd[0] = 1.0e-8;
        tolrsd[1] = 1.0e-8;
        tolrsd[2] = 1.0e-8;
        tolrsd[3] = 1.0e-8;
        tolrsd[4] = 1.0e-8;
        nx0 = 12;
        ny0 = 12;
        nz0 = 12;
    }
    int _imopVarPre234;
    int _imopVarPre235;
    _imopVarPre234 = nx0 < 4;
    if (!_imopVarPre234) {
        _imopVarPre235 = ny0 < 4;
        if (!_imopVarPre235) {
            _imopVarPre235 = nz0 < 4;
        }
        _imopVarPre234 = _imopVarPre235;
    }
    if (_imopVarPre234) {
        printf("     PROBLEM SIZE IS TOO SMALL - \n" "     SET EACH OF NX, NY AND NZ AT LEAST EQUAL TO 5\n");
        exit(1);
    }
    int _imopVarPre236;
    int _imopVarPre237;
    _imopVarPre236 = nx0 > 12;
    if (!_imopVarPre236) {
        _imopVarPre237 = ny0 > 12;
        if (!_imopVarPre237) {
            _imopVarPre237 = nz0 > 12;
        }
        _imopVarPre236 = _imopVarPre237;
    }
    if (_imopVarPre236) {
        printf("     PROBLEM SIZE IS TOO LARGE - \n" "     NX, NY AND NZ SHOULD BE EQUAL TO \n" "     ISIZ1, ISIZ2 AND ISIZ3 RESPECTIVELY\n");
        exit(1);
    }
    printf(" Size: %3dx%3dx%3d\n", nx0, ny0, nz0);
    printf(" Iterations: %3d\n", itmax);
}
static void rhs(void ) {
#pragma omp parallel
    {
        int i;
        int j;
        int k;
        int m;
        int L1;
        int L2;
        int ist1;
        int iend1;
        int jst1;
        int jend1;
        double q;
        double u21;
        double u31;
        double u41;
        double tmp;
        double u21i;
        double u31i;
        double u41i;
        double u51i;
        double u21j;
        double u31j;
        double u41j;
        double u51j;
        double u21k;
        double u31k;
        double u41k;
        double u51k;
        double u21im1;
        double u31im1;
        double u41im1;
        double u51im1;
        double u21jm1;
        double u31jm1;
        double u41jm1;
        double u51jm1;
        double u21km1;
        double u31km1;
        double u41km1;
        double u51km1;
#pragma omp for nowait
        for (i = 0; i <= nx - 1; i++) {
            for (j = 0; j <= ny - 1; j++) {
                for (k = 0; k <= nz - 1; k++) {
                    for (m = 0; m < 5; m++) {
                        rsd[i][j][k][m] = -frct[i][j][k][m];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        L1 = 0;
        L2 = nx - 1;
#pragma omp for nowait
        for (i = L1; i <= L2; i++) {
            for (j = jst; j <= jend; j++) {
                for (k = 1; k <= nz - 2; k++) {
                    flux[i][j][k][0] = u[i][j][k][1];
                    u21 = u[i][j][k][1] / u[i][j][k][0];
                    q = 0.50 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) / u[i][j][k][0];
                    flux[i][j][k][1] = u[i][j][k][1] * u21 + 0.40e+00 * (u[i][j][k][4] - q);
                    flux[i][j][k][2] = u[i][j][k][2] * u21;
                    flux[i][j][k][3] = u[i][j][k][3] * u21;
                    flux[i][j][k][4] = (1.40e+00 * u[i][j][k][4] - 0.40e+00 * q) * u21;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (j = jst; j <= jend; j++) {
            for (k = 1; k <= nz - 2; k++) {
                for (i = ist; i <= iend; i++) {
                    for (m = 0; m < 5; m++) {
                        rsd[i][j][k][m] = rsd[i][j][k][m] - tx2 * (flux[i + 1][j][k][m] - flux[i - 1][j][k][m]);
                    }
                }
                L2 = nx - 1;
                for (i = ist; i <= L2; i++) {
                    tmp = 1.0 / u[i][j][k][0];
                    u21i = tmp * u[i][j][k][1];
                    u31i = tmp * u[i][j][k][2];
                    u41i = tmp * u[i][j][k][3];
                    u51i = tmp * u[i][j][k][4];
                    tmp = 1.0 / u[i - 1][j][k][0];
                    u21im1 = tmp * u[i - 1][j][k][1];
                    u31im1 = tmp * u[i - 1][j][k][2];
                    u41im1 = tmp * u[i - 1][j][k][3];
                    u51im1 = tmp * u[i - 1][j][k][4];
                    flux[i][j][k][1] = (4.0 / 3.0) * tx3 * (u21i - u21im1);
                    flux[i][j][k][2] = tx3 * (u31i - u31im1);
                    flux[i][j][k][3] = tx3 * (u41i - u41im1);
                    flux[i][j][k][4] = 0.50 * (1.0 - 1.40e+00 * 1.40e+00) * tx3 * (((u21i * u21i) + (u31i * u31i) + (u41i * u41i)) - ((u21im1 * u21im1) + (u31im1 * u31im1) + (u41im1 * u41im1))) + (1.0 / 6.0) * tx3 * ((u21i * u21i) - (u21im1 * u21im1)) + 1.40e+00 * 1.40e+00 * tx3 * (u51i - u51im1);
                }
                for (i = ist; i <= iend; i++) {
                    rsd[i][j][k][0] = rsd[i][j][k][0] + dx1 * tx1 * (u[i - 1][j][k][0] - 2.0 * u[i][j][k][0] + u[i + 1][j][k][0]);
                    rsd[i][j][k][1] = rsd[i][j][k][1] + tx3 * 1.00e-01 * 1.00e+00 * (flux[i + 1][j][k][1] - flux[i][j][k][1]) + dx2 * tx1 * (u[i - 1][j][k][1] - 2.0 * u[i][j][k][1] + u[i + 1][j][k][1]);
                    rsd[i][j][k][2] = rsd[i][j][k][2] + tx3 * 1.00e-01 * 1.00e+00 * (flux[i + 1][j][k][2] - flux[i][j][k][2]) + dx3 * tx1 * (u[i - 1][j][k][2] - 2.0 * u[i][j][k][2] + u[i + 1][j][k][2]);
                    rsd[i][j][k][3] = rsd[i][j][k][3] + tx3 * 1.00e-01 * 1.00e+00 * (flux[i + 1][j][k][3] - flux[i][j][k][3]) + dx4 * tx1 * (u[i - 1][j][k][3] - 2.0 * u[i][j][k][3] + u[i + 1][j][k][3]);
                    rsd[i][j][k][4] = rsd[i][j][k][4] + tx3 * 1.00e-01 * 1.00e+00 * (flux[i + 1][j][k][4] - flux[i][j][k][4]) + dx5 * tx1 * (u[i - 1][j][k][4] - 2.0 * u[i][j][k][4] + u[i + 1][j][k][4]);
                }
                for (m = 0; m < 5; m++) {
                    rsd[1][j][k][m] = rsd[1][j][k][m] - dssp * (+5.0 * u[1][j][k][m] - 4.0 * u[2][j][k][m] + u[3][j][k][m]);
                    rsd[2][j][k][m] = rsd[2][j][k][m] - dssp * (-4.0 * u[1][j][k][m] + 6.0 * u[2][j][k][m] - 4.0 * u[3][j][k][m] + u[4][j][k][m]);
                }
                ist1 = 3;
                iend1 = nx - 4;
                for (i = ist1; i <= iend1; i++) {
                    for (m = 0; m < 5; m++) {
                        rsd[i][j][k][m] = rsd[i][j][k][m] - dssp * (u[i - 2][j][k][m] - 4.0 * u[i - 1][j][k][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i + 1][j][k][m] + u[i + 2][j][k][m]);
                    }
                }
                for (m = 0; m < 5; m++) {
                    rsd[nx - 3][j][k][m] = rsd[nx - 3][j][k][m] - dssp * (u[nx - 5][j][k][m] - 4.0 * u[nx - 4][j][k][m] + 6.0 * u[nx - 3][j][k][m] - 4.0 * u[nx - 2][j][k][m]);
                    rsd[nx - 2][j][k][m] = rsd[nx - 2][j][k][m] - dssp * (u[nx - 4][j][k][m] - 4.0 * u[nx - 3][j][k][m] + 5.0 * u[nx - 2][j][k][m]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        L1 = 0;
        L2 = ny - 1;
#pragma omp for nowait
        for (i = ist; i <= iend; i++) {
            for (j = L1; j <= L2; j++) {
                for (k = 1; k <= nz - 2; k++) {
                    flux[i][j][k][0] = u[i][j][k][2];
                    u31 = u[i][j][k][2] / u[i][j][k][0];
                    q = 0.50 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) / u[i][j][k][0];
                    flux[i][j][k][1] = u[i][j][k][1] * u31;
                    flux[i][j][k][2] = u[i][j][k][2] * u31 + 0.40e+00 * (u[i][j][k][4] - q);
                    flux[i][j][k][3] = u[i][j][k][3] * u31;
                    flux[i][j][k][4] = (1.40e+00 * u[i][j][k][4] - 0.40e+00 * q) * u31;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (i = ist; i <= iend; i++) {
            for (k = 1; k <= nz - 2; k++) {
                for (j = jst; j <= jend; j++) {
                    for (m = 0; m < 5; m++) {
                        rsd[i][j][k][m] = rsd[i][j][k][m] - ty2 * (flux[i][j + 1][k][m] - flux[i][j - 1][k][m]);
                    }
                }
                L2 = ny - 1;
                for (j = jst; j <= L2; j++) {
                    tmp = 1.0 / u[i][j][k][0];
                    u21j = tmp * u[i][j][k][1];
                    u31j = tmp * u[i][j][k][2];
                    u41j = tmp * u[i][j][k][3];
                    u51j = tmp * u[i][j][k][4];
                    tmp = 1.0 / u[i][j - 1][k][0];
                    u21jm1 = tmp * u[i][j - 1][k][1];
                    u31jm1 = tmp * u[i][j - 1][k][2];
                    u41jm1 = tmp * u[i][j - 1][k][3];
                    u51jm1 = tmp * u[i][j - 1][k][4];
                    flux[i][j][k][1] = ty3 * (u21j - u21jm1);
                    flux[i][j][k][2] = (4.0 / 3.0) * ty3 * (u31j - u31jm1);
                    flux[i][j][k][3] = ty3 * (u41j - u41jm1);
                    flux[i][j][k][4] = 0.50 * (1.0 - 1.40e+00 * 1.40e+00) * ty3 * (((u21j * u21j) + (u31j * u31j) + (u41j * u41j)) - ((u21jm1 * u21jm1) + (u31jm1 * u31jm1) + (u41jm1 * u41jm1))) + (1.0 / 6.0) * ty3 * ((u31j * u31j) - (u31jm1 * u31jm1)) + 1.40e+00 * 1.40e+00 * ty3 * (u51j - u51jm1);
                }
                for (j = jst; j <= jend; j++) {
                    rsd[i][j][k][0] = rsd[i][j][k][0] + dy1 * ty1 * (u[i][j - 1][k][0] - 2.0 * u[i][j][k][0] + u[i][j + 1][k][0]);
                    rsd[i][j][k][1] = rsd[i][j][k][1] + ty3 * 1.00e-01 * 1.00e+00 * (flux[i][j + 1][k][1] - flux[i][j][k][1]) + dy2 * ty1 * (u[i][j - 1][k][1] - 2.0 * u[i][j][k][1] + u[i][j + 1][k][1]);
                    rsd[i][j][k][2] = rsd[i][j][k][2] + ty3 * 1.00e-01 * 1.00e+00 * (flux[i][j + 1][k][2] - flux[i][j][k][2]) + dy3 * ty1 * (u[i][j - 1][k][2] - 2.0 * u[i][j][k][2] + u[i][j + 1][k][2]);
                    rsd[i][j][k][3] = rsd[i][j][k][3] + ty3 * 1.00e-01 * 1.00e+00 * (flux[i][j + 1][k][3] - flux[i][j][k][3]) + dy4 * ty1 * (u[i][j - 1][k][3] - 2.0 * u[i][j][k][3] + u[i][j + 1][k][3]);
                    rsd[i][j][k][4] = rsd[i][j][k][4] + ty3 * 1.00e-01 * 1.00e+00 * (flux[i][j + 1][k][4] - flux[i][j][k][4]) + dy5 * ty1 * (u[i][j - 1][k][4] - 2.0 * u[i][j][k][4] + u[i][j + 1][k][4]);
                }
                for (m = 0; m < 5; m++) {
                    rsd[i][1][k][m] = rsd[i][1][k][m] - dssp * (+5.0 * u[i][1][k][m] - 4.0 * u[i][2][k][m] + u[i][3][k][m]);
                    rsd[i][2][k][m] = rsd[i][2][k][m] - dssp * (-4.0 * u[i][1][k][m] + 6.0 * u[i][2][k][m] - 4.0 * u[i][3][k][m] + u[i][4][k][m]);
                }
                jst1 = 3;
                jend1 = ny - 4;
                for (j = jst1; j <= jend1; j++) {
                    for (m = 0; m < 5; m++) {
                        rsd[i][j][k][m] = rsd[i][j][k][m] - dssp * (u[i][j - 2][k][m] - 4.0 * u[i][j - 1][k][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i][j + 1][k][m] + u[i][j + 2][k][m]);
                    }
                }
                for (m = 0; m < 5; m++) {
                    rsd[i][ny - 3][k][m] = rsd[i][ny - 3][k][m] - dssp * (u[i][ny - 5][k][m] - 4.0 * u[i][ny - 4][k][m] + 6.0 * u[i][ny - 3][k][m] - 4.0 * u[i][ny - 2][k][m]);
                    rsd[i][ny - 2][k][m] = rsd[i][ny - 2][k][m] - dssp * (u[i][ny - 4][k][m] - 4.0 * u[i][ny - 3][k][m] + 5.0 * u[i][ny - 2][k][m]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (i = ist; i <= iend; i++) {
            for (j = jst; j <= jend; j++) {
                for (k = 0; k <= nz - 1; k++) {
                    flux[i][j][k][0] = u[i][j][k][3];
                    u41 = u[i][j][k][3] / u[i][j][k][0];
                    q = 0.50 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) / u[i][j][k][0];
                    flux[i][j][k][1] = u[i][j][k][1] * u41;
                    flux[i][j][k][2] = u[i][j][k][2] * u41;
                    flux[i][j][k][3] = u[i][j][k][3] * u41 + 0.40e+00 * (u[i][j][k][4] - q);
                    flux[i][j][k][4] = (1.40e+00 * u[i][j][k][4] - 0.40e+00 * q) * u41;
                }
                for (k = 1; k <= nz - 2; k++) {
                    for (m = 0; m < 5; m++) {
                        rsd[i][j][k][m] = rsd[i][j][k][m] - tz2 * (flux[i][j][k + 1][m] - flux[i][j][k - 1][m]);
                    }
                }
                for (k = 1; k <= nz - 1; k++) {
                    tmp = 1.0 / u[i][j][k][0];
                    u21k = tmp * u[i][j][k][1];
                    u31k = tmp * u[i][j][k][2];
                    u41k = tmp * u[i][j][k][3];
                    u51k = tmp * u[i][j][k][4];
                    tmp = 1.0 / u[i][j][k - 1][0];
                    u21km1 = tmp * u[i][j][k - 1][1];
                    u31km1 = tmp * u[i][j][k - 1][2];
                    u41km1 = tmp * u[i][j][k - 1][3];
                    u51km1 = tmp * u[i][j][k - 1][4];
                    flux[i][j][k][1] = tz3 * (u21k - u21km1);
                    flux[i][j][k][2] = tz3 * (u31k - u31km1);
                    flux[i][j][k][3] = (4.0 / 3.0) * tz3 * (u41k - u41km1);
                    flux[i][j][k][4] = 0.50 * (1.0 - 1.40e+00 * 1.40e+00) * tz3 * (((u21k * u21k) + (u31k * u31k) + (u41k * u41k)) - ((u21km1 * u21km1) + (u31km1 * u31km1) + (u41km1 * u41km1))) + (1.0 / 6.0) * tz3 * ((u41k * u41k) - (u41km1 * u41km1)) + 1.40e+00 * 1.40e+00 * tz3 * (u51k - u51km1);
                }
                for (k = 1; k <= nz - 2; k++) {
                    rsd[i][j][k][0] = rsd[i][j][k][0] + dz1 * tz1 * (u[i][j][k - 1][0] - 2.0 * u[i][j][k][0] + u[i][j][k + 1][0]);
                    rsd[i][j][k][1] = rsd[i][j][k][1] + tz3 * 1.00e-01 * 1.00e+00 * (flux[i][j][k + 1][1] - flux[i][j][k][1]) + dz2 * tz1 * (u[i][j][k - 1][1] - 2.0 * u[i][j][k][1] + u[i][j][k + 1][1]);
                    rsd[i][j][k][2] = rsd[i][j][k][2] + tz3 * 1.00e-01 * 1.00e+00 * (flux[i][j][k + 1][2] - flux[i][j][k][2]) + dz3 * tz1 * (u[i][j][k - 1][2] - 2.0 * u[i][j][k][2] + u[i][j][k + 1][2]);
                    rsd[i][j][k][3] = rsd[i][j][k][3] + tz3 * 1.00e-01 * 1.00e+00 * (flux[i][j][k + 1][3] - flux[i][j][k][3]) + dz4 * tz1 * (u[i][j][k - 1][3] - 2.0 * u[i][j][k][3] + u[i][j][k + 1][3]);
                    rsd[i][j][k][4] = rsd[i][j][k][4] + tz3 * 1.00e-01 * 1.00e+00 * (flux[i][j][k + 1][4] - flux[i][j][k][4]) + dz5 * tz1 * (u[i][j][k - 1][4] - 2.0 * u[i][j][k][4] + u[i][j][k + 1][4]);
                }
                for (m = 0; m < 5; m++) {
                    rsd[i][j][1][m] = rsd[i][j][1][m] - dssp * (+5.0 * u[i][j][1][m] - 4.0 * u[i][j][2][m] + u[i][j][3][m]);
                    rsd[i][j][2][m] = rsd[i][j][2][m] - dssp * (-4.0 * u[i][j][1][m] + 6.0 * u[i][j][2][m] - 4.0 * u[i][j][3][m] + u[i][j][4][m]);
                }
                for (k = 3; k <= nz - 4; k++) {
                    for (m = 0; m < 5; m++) {
                        rsd[i][j][k][m] = rsd[i][j][k][m] - dssp * (u[i][j][k - 2][m] - 4.0 * u[i][j][k - 1][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i][j][k + 1][m] + u[i][j][k + 2][m]);
                    }
                }
                for (m = 0; m < 5; m++) {
                    rsd[i][j][nz - 3][m] = rsd[i][j][nz - 3][m] - dssp * (u[i][j][nz - 5][m] - 4.0 * u[i][j][nz - 4][m] + 6.0 * u[i][j][nz - 3][m] - 4.0 * u[i][j][nz - 2][m]);
                    rsd[i][j][nz - 2][m] = rsd[i][j][nz - 2][m] - dssp * (u[i][j][nz - 4][m] - 4.0 * u[i][j][nz - 3][m] + 5.0 * u[i][j][nz - 2][m]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
}
static void setbv(void ) {
#pragma omp parallel
    {
        int i;
        int j;
        int k;
        int iglob;
        int jglob;
#pragma omp for nowait
        for (i = 0; i < nx; i++) {
            iglob = i;
            for (j = 0; j < ny; j++) {
                jglob = j;
                double *_imopVarPre239;
                _imopVarPre239 = &u[i][j][0][0];
                exact(iglob, jglob, 0, _imopVarPre239);
                double *_imopVarPre242;
                int _imopVarPre243;
                _imopVarPre242 = &u[i][j][nz - 1][0];
                _imopVarPre243 = nz - 1;
                exact(iglob, jglob, _imopVarPre243, _imopVarPre242);
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (i = 0; i < nx; i++) {
            iglob = i;
            for (k = 0; k < nz; k++) {
                double *_imopVarPre245;
                _imopVarPre245 = &u[i][0][k][0];
                exact(iglob, 0, k, _imopVarPre245);
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (i = 0; i < nx; i++) {
            iglob = i;
            for (k = 0; k < nz; k++) {
                double *_imopVarPre248;
                int _imopVarPre249;
                _imopVarPre248 = &u[i][ny - 1][k][0];
                _imopVarPre249 = ny0 - 1;
                exact(iglob, _imopVarPre249, k, _imopVarPre248);
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (j = 0; j < ny; j++) {
            jglob = j;
            for (k = 0; k < nz; k++) {
                double *_imopVarPre251;
                _imopVarPre251 = &u[0][j][k][0];
                exact(0, jglob, k, _imopVarPre251);
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (j = 0; j < ny; j++) {
            jglob = j;
            for (k = 0; k < nz; k++) {
                double *_imopVarPre254;
                int _imopVarPre255;
                _imopVarPre254 = &u[nx - 1][j][k][0];
                _imopVarPre255 = nx0 - 1;
                exact(_imopVarPre255, jglob, k, _imopVarPre254);
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
}
static void setcoeff(void ) {
    dxi = 1.0 / (nx0 - 1);
    deta = 1.0 / (ny0 - 1);
    dzeta = 1.0 / (nz0 - 1);
    tx1 = 1.0 / (dxi * dxi);
    tx2 = 1.0 / (2.0 * dxi);
    tx3 = 1.0 / dxi;
    ty1 = 1.0 / (deta * deta);
    ty2 = 1.0 / (2.0 * deta);
    ty3 = 1.0 / deta;
    tz1 = 1.0 / (dzeta * dzeta);
    tz2 = 1.0 / (2.0 * dzeta);
    tz3 = 1.0 / dzeta;
    ii1 = 1;
    ii2 = nx0 - 2;
    ji1 = 1;
    ji2 = ny0 - 3;
    ki1 = 2;
    ki2 = nz0 - 2;
    dx1 = 0.75;
    dx2 = dx1;
    dx3 = dx1;
    dx4 = dx1;
    dx5 = dx1;
    dy1 = 0.75;
    dy2 = dy1;
    dy3 = dy1;
    dy4 = dy1;
    dy5 = dy1;
    dz1 = 1.00;
    dz2 = dz1;
    dz3 = dz1;
    dz4 = dz1;
    dz5 = dz1;
    int _imopVarPre348;
    double _imopVarPre349;
    int _imopVarPre350;
    double _imopVarPre351;
    int _imopVarPre358;
    double _imopVarPre359;
    _imopVarPre348 = (dy1 > dz1);
    if (_imopVarPre348) {
        _imopVarPre349 = dy1;
    } else {
        _imopVarPre349 = dz1;
    }
    _imopVarPre350 = (dx1 > _imopVarPre349);
    if (_imopVarPre350) {
        _imopVarPre351 = dx1;
    } else {
        _imopVarPre358 = (dy1 > dz1);
        if (_imopVarPre358) {
            _imopVarPre359 = dy1;
        } else {
            _imopVarPre359 = dz1;
        }
        _imopVarPre351 = _imopVarPre359;
    }
    dssp = _imopVarPre351 / 4.0;
    ce[0][0] = 2.0;
    ce[0][1] = 0.0;
    ce[0][2] = 0.0;
    ce[0][3] = 4.0;
    ce[0][4] = 5.0;
    ce[0][5] = 3.0;
    ce[0][6] = 5.0e-01;
    ce[0][7] = 2.0e-02;
    ce[0][8] = 1.0e-02;
    ce[0][9] = 3.0e-02;
    ce[0][10] = 5.0e-01;
    ce[0][11] = 4.0e-01;
    ce[0][12] = 3.0e-01;
    ce[1][0] = 1.0;
    ce[1][1] = 0.0;
    ce[1][2] = 0.0;
    ce[1][3] = 0.0;
    ce[1][4] = 1.0;
    ce[1][5] = 2.0;
    ce[1][6] = 3.0;
    ce[1][7] = 1.0e-02;
    ce[1][8] = 3.0e-02;
    ce[1][9] = 2.0e-02;
    ce[1][10] = 4.0e-01;
    ce[1][11] = 3.0e-01;
    ce[1][12] = 5.0e-01;
    ce[2][0] = 2.0;
    ce[2][1] = 2.0;
    ce[2][2] = 0.0;
    ce[2][3] = 0.0;
    ce[2][4] = 0.0;
    ce[2][5] = 2.0;
    ce[2][6] = 3.0;
    ce[2][7] = 4.0e-02;
    ce[2][8] = 3.0e-02;
    ce[2][9] = 5.0e-02;
    ce[2][10] = 3.0e-01;
    ce[2][11] = 5.0e-01;
    ce[2][12] = 4.0e-01;
    ce[3][0] = 2.0;
    ce[3][1] = 2.0;
    ce[3][2] = 0.0;
    ce[3][3] = 0.0;
    ce[3][4] = 0.0;
    ce[3][5] = 2.0;
    ce[3][6] = 3.0;
    ce[3][7] = 3.0e-02;
    ce[3][8] = 5.0e-02;
    ce[3][9] = 4.0e-02;
    ce[3][10] = 2.0e-01;
    ce[3][11] = 1.0e-01;
    ce[3][12] = 3.0e-01;
    ce[4][0] = 5.0;
    ce[4][1] = 4.0;
    ce[4][2] = 3.0;
    ce[4][3] = 2.0;
    ce[4][4] = 1.0e-01;
    ce[4][5] = 4.0e-01;
    ce[4][6] = 3.0e-01;
    ce[4][7] = 5.0e-02;
    ce[4][8] = 4.0e-02;
    ce[4][9] = 3.0e-02;
    ce[4][10] = 1.0e-01;
    ce[4][11] = 3.0e-01;
    ce[4][12] = 2.0e-01;
}
static void setiv(void ) {
#pragma omp parallel
    {
        int i;
        int j;
        int k;
        int m;
        int iglob;
        int jglob;
        double xi;
        double eta;
        double zeta;
        double pxi;
        double peta;
        double pzeta;
        double ue_1jk[5];
        double ue_nx0jk[5];
        double ue_i1k[5];
        double ue_iny0k[5];
        double ue_ij1[5];
        double ue_ijnz[5];
#pragma omp for nowait
        for (j = 0; j < ny; j++) {
            jglob = j;
            for (k = 1; k < nz - 1; k++) {
                zeta = ((double) k) / (nz - 1);
                int _imopVarPre361;
                _imopVarPre361 = jglob != 0;
                if (_imopVarPre361) {
                    _imopVarPre361 = jglob != ny0 - 1;
                }
                if (_imopVarPre361) {
                    eta = ((double) jglob) / (ny0 - 1);
                    for (i = 0; i < nx; i++) {
                        iglob = i;
                        int _imopVarPre363;
                        _imopVarPre363 = iglob != 0;
                        if (_imopVarPre363) {
                            _imopVarPre363 = iglob != nx0 - 1;
                        }
                        if (_imopVarPre363) {
                            xi = ((double) iglob) / (nx0 - 1);
                            exact(0, jglob, k, ue_1jk);
                            int _imopVarPre365;
                            _imopVarPre365 = nx0 - 1;
                            exact(_imopVarPre365, jglob, k, ue_nx0jk);
                            exact(iglob, 0, k, ue_i1k);
                            int _imopVarPre367;
                            _imopVarPre367 = ny0 - 1;
                            exact(iglob, _imopVarPre367, k, ue_iny0k);
                            exact(iglob, jglob, 0, ue_ij1);
                            int _imopVarPre369;
                            _imopVarPre369 = nz - 1;
                            exact(iglob, jglob, _imopVarPre369, ue_ijnz);
                            for (m = 0; m < 5; m++) {
                                pxi = (1.0 - xi) * ue_1jk[m] + xi * ue_nx0jk[m];
                                peta = (1.0 - eta) * ue_i1k[m] + eta * ue_iny0k[m];
                                pzeta = (1.0 - zeta) * ue_ij1[m] + zeta * ue_ijnz[m];
                                u[i][j][k][m] = pxi + peta + pzeta - pxi * peta - peta * pzeta - pzeta * pxi + pxi * peta * pzeta;
                            }
                        }
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
}
static void ssor(void ) {
    int i;
    int j;
    int k;
    int m;
    int istep;
    double tmp;
    double delunm[5];
    double tv[12][12][5];
    tmp = 1.0 / (omega * (2.0 - omega));
#pragma omp parallel private(i, j, k, m)
    {
#pragma omp for nowait
        for (i = 0; i < 12; i++) {
            for (j = 0; j < 12; j++) {
                for (k = 0; k < 5; k++) {
                    for (m = 0; m < 5; m++) {
                        a[i][j][k][m] = 0.0;
                        b[i][j][k][m] = 0.0;
                        c[i][j][k][m] = 0.0;
                        d[i][j][k][m] = 0.0;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    rhs();
    l2norm(nx0, ny0, nz0, ist, iend, jst, jend, rsd, rsdnm);
    timer_clear(1);
    timer_start(1);
    for (istep = 1; istep <= itmax; istep++) {
        int _imopVarPre370;
        int _imopVarPre371;
        _imopVarPre370 = istep % 20 == 0;
        if (!_imopVarPre370) {
            _imopVarPre371 = istep == itmax;
            if (!_imopVarPre371) {
                _imopVarPre371 = istep == 1;
            }
            _imopVarPre370 = _imopVarPre371;
        }
        if (_imopVarPre370) {
#pragma omp master
            {
                printf(" Time step %4d\n", istep);
            }
        }
#pragma omp parallel private(istep, i, j, k, m)
        {
#pragma omp for nowait
            for (i = ist; i <= iend; i++) {
                for (j = jst; j <= jend; j++) {
                    for (k = 1; k <= nz - 2; k++) {
                        for (m = 0; m < 5; m++) {
                            rsd[i][j][k][m] = dt * rsd[i][j][k][m];
                        }
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
            for (k = 1; k <= nz - 2; k++) {
                jacld(k);
                blts(nx, ny, nz, k, omega, rsd, a, b, c, d, ist, iend, jst, jend, nx0, ny0);
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
            for (k = nz - 2; k >= 1; k--) {
                jacu(k);
                buts(nx, ny, nz, k, omega, rsd, tv, d, a, b, c, ist, iend, jst, jend, nx0, ny0);
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
            for (i = ist; i <= iend; i++) {
                for (j = jst; j <= jend; j++) {
                    for (k = 1; k <= nz - 2; k++) {
                        for (m = 0; m < 5; m++) {
                            u[i][j][k][m] = u[i][j][k][m] + tmp * rsd[i][j][k][m];
                        }
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
        if (istep % inorm == 0) {
            l2norm(nx0, ny0, nz0, ist, iend, jst, jend, rsd, delunm);
        }
        rhs();
        int _imopVarPre372;
        _imopVarPre372 = (istep % inorm == 0);
        if (!_imopVarPre372) {
            _imopVarPre372 = (istep == itmax);
        }
        if (_imopVarPre372) {
            l2norm(nx0, ny0, nz0, ist, iend, jst, jend, rsd, rsdnm);
        }
        int _imopVarPre377;
        int _imopVarPre378;
        int _imopVarPre379;
        int _imopVarPre380;
        _imopVarPre377 = (rsdnm[0] < tolrsd[0]);
        if (_imopVarPre377) {
            _imopVarPre378 = (rsdnm[1] < tolrsd[1]);
            if (_imopVarPre378) {
                _imopVarPre379 = (rsdnm[2] < tolrsd[2]);
                if (_imopVarPre379) {
                    _imopVarPre380 = (rsdnm[3] < tolrsd[3]);
                    if (_imopVarPre380) {
                        _imopVarPre380 = (rsdnm[4] < tolrsd[4]);
                    }
                    _imopVarPre379 = _imopVarPre380;
                }
                _imopVarPre378 = _imopVarPre379;
            }
            _imopVarPre377 = _imopVarPre378;
        }
        if (_imopVarPre377) {
            exit(1);
        }
    }
    timer_stop(1);
    maxtime = timer_read(1);
}
static void verify(double xcr[5], double xce[5] , double xci , char *class , boolean *verified) {
    double xcrref[5];
    double xceref[5];
    double xciref;
    double xcrdif[5];
    double xcedif[5];
    double xcidif;
    double epsilon;
    double dtref;
    int m;
    epsilon = 1.0e-08;
    *class = 'U';
    *verified = 1;
    for (m = 0; m < 5; m++) {
        xcrref[m] = 1.0;
        xceref[m] = 1.0;
    }
    xciref = 1.0;
    int _imopVarPre384;
    int _imopVarPre385;
    int _imopVarPre386;
    _imopVarPre384 = nx0 == 12;
    if (_imopVarPre384) {
        _imopVarPre385 = ny0 == 12;
        if (_imopVarPre385) {
            _imopVarPre386 = nz0 == 12;
            if (_imopVarPre386) {
                _imopVarPre386 = itmax == 50;
            }
            _imopVarPre385 = _imopVarPre386;
        }
        _imopVarPre384 = _imopVarPre385;
    }
    if (_imopVarPre384) {
        *class = 'S';
        dtref = 5.0e-1;
        xcrref[0] = 1.6196343210976702e-02;
        xcrref[1] = 2.1976745164821318e-03;
        xcrref[2] = 1.5179927653399185e-03;
        xcrref[3] = 1.5029584435994323e-03;
        xcrref[4] = 3.4264073155896461e-02;
        xceref[0] = 6.4223319957960924e-04;
        xceref[1] = 8.4144342047347926e-05;
        xceref[2] = 5.8588269616485186e-05;
        xceref[3] = 5.8474222595157350e-05;
        xceref[4] = 1.3103347914111294e-03;
        xciref = 7.8418928865937083;
    } else {
        int _imopVarPre390;
        int _imopVarPre391;
        int _imopVarPre392;
        _imopVarPre390 = nx0 == 33;
        if (_imopVarPre390) {
            _imopVarPre391 = ny0 == 33;
            if (_imopVarPre391) {
                _imopVarPre392 = nz0 == 33;
                if (_imopVarPre392) {
                    _imopVarPre392 = itmax == 300;
                }
                _imopVarPre391 = _imopVarPre392;
            }
            _imopVarPre390 = _imopVarPre391;
        }
        if (_imopVarPre390) {
            *class = 'W';
            dtref = 1.5e-3;
            xcrref[0] = 0.1236511638192e+02;
            xcrref[1] = 0.1317228477799e+01;
            xcrref[2] = 0.2550120713095e+01;
            xcrref[3] = 0.2326187750252e+01;
            xcrref[4] = 0.2826799444189e+02;
            xceref[0] = 0.4867877144216;
            xceref[1] = 0.5064652880982e-01;
            xceref[2] = 0.9281818101960e-01;
            xceref[3] = 0.8570126542733e-01;
            xceref[4] = 0.1084277417792e+01;
            xciref = 0.1161399311023e+02;
        } else {
            int _imopVarPre396;
            int _imopVarPre397;
            int _imopVarPre398;
            _imopVarPre396 = nx0 == 64;
            if (_imopVarPre396) {
                _imopVarPre397 = ny0 == 64;
                if (_imopVarPre397) {
                    _imopVarPre398 = nz0 == 64;
                    if (_imopVarPre398) {
                        _imopVarPre398 = itmax == 250;
                    }
                    _imopVarPre397 = _imopVarPre398;
                }
                _imopVarPre396 = _imopVarPre397;
            }
            if (_imopVarPre396) {
                *class = 'A';
                dtref = 2.0e+0;
                xcrref[0] = 7.7902107606689367e+02;
                xcrref[1] = 6.3402765259692870e+01;
                xcrref[2] = 1.9499249727292479e+02;
                xcrref[3] = 1.7845301160418537e+02;
                xcrref[4] = 1.8384760349464247e+03;
                xceref[0] = 2.9964085685471943e+01;
                xceref[1] = 2.8194576365003349;
                xceref[2] = 7.3473412698774742;
                xceref[3] = 6.7139225687777051;
                xceref[4] = 7.0715315688392578e+01;
                xciref = 2.6030925604886277e+01;
            } else {
                int _imopVarPre402;
                int _imopVarPre403;
                int _imopVarPre404;
                _imopVarPre402 = nx0 == 102;
                if (_imopVarPre402) {
                    _imopVarPre403 = ny0 == 102;
                    if (_imopVarPre403) {
                        _imopVarPre404 = nz0 == 102;
                        if (_imopVarPre404) {
                            _imopVarPre404 = itmax == 250;
                        }
                        _imopVarPre403 = _imopVarPre404;
                    }
                    _imopVarPre402 = _imopVarPre403;
                }
                if (_imopVarPre402) {
                    *class = 'B';
                    dtref = 2.0e+0;
                    xcrref[0] = 3.5532672969982736e+03;
                    xcrref[1] = 2.6214750795310692e+02;
                    xcrref[2] = 8.8333721850952190e+02;
                    xcrref[3] = 7.7812774739425265e+02;
                    xcrref[4] = 7.3087969592545314e+03;
                    xceref[0] = 1.1401176380212709e+02;
                    xceref[1] = 8.1098963655421574;
                    xceref[2] = 2.8480597317698308e+01;
                    xceref[3] = 2.5905394567832939e+01;
                    xceref[4] = 2.6054907504857413e+02;
                    xciref = 4.7887162703308227e+01;
                } else {
                    int _imopVarPre408;
                    int _imopVarPre409;
                    int _imopVarPre410;
                    _imopVarPre408 = nx0 == 162;
                    if (_imopVarPre408) {
                        _imopVarPre409 = ny0 == 162;
                        if (_imopVarPre409) {
                            _imopVarPre410 = nz0 == 162;
                            if (_imopVarPre410) {
                                _imopVarPre410 = itmax == 250;
                            }
                            _imopVarPre409 = _imopVarPre410;
                        }
                        _imopVarPre408 = _imopVarPre409;
                    }
                    if (_imopVarPre408) {
                        *class = 'C';
                        dtref = 2.0e+0;
                        xcrref[0] = 1.03766980323537846e+04;
                        xcrref[1] = 8.92212458801008552e+02;
                        xcrref[2] = 2.56238814582660871e+03;
                        xcrref[3] = 2.19194343857831427e+03;
                        xcrref[4] = 1.78078057261061185e+04;
                        xceref[0] = 2.15986399716949279e+02;
                        xceref[1] = 1.55789559239863600e+01;
                        xceref[2] = 5.41318863077207766e+01;
                        xceref[3] = 4.82262643154045421e+01;
                        xceref[4] = 4.55902910043250358e+02;
                        xciref = 6.66404553572181300e+01;
                    } else {
                        *verified = 0;
                    }
                }
            }
        }
    }
    for (m = 0; m < 5; m++) {
        double _imopVarPre412;
        double _imopVarPre413;
        _imopVarPre412 = (xcr[m] - xcrref[m]) / xcrref[m];
        _imopVarPre413 = fabs(_imopVarPre412);
        xcrdif[m] = _imopVarPre413;
        double _imopVarPre415;
        double _imopVarPre416;
        _imopVarPre415 = (xce[m] - xceref[m]) / xceref[m];
        _imopVarPre416 = fabs(_imopVarPre415);
        xcedif[m] = _imopVarPre416;
    }
    double _imopVarPre418;
    double _imopVarPre419;
    _imopVarPre418 = (xci - xciref) / xciref;
    _imopVarPre419 = fabs(_imopVarPre418);
    xcidif = _imopVarPre419;
    if (*class != 'U') {
        char _imopVarPre421;
        _imopVarPre421 = *class;
        printf("\n Verification being performed for class %1c\n", _imopVarPre421);
        printf(" Accuracy setting for epsilon = %20.13e\n", epsilon);
        double _imopVarPre424;
        double _imopVarPre425;
        _imopVarPre424 = dt - dtref;
        _imopVarPre425 = fabs(_imopVarPre424);
        if (_imopVarPre425 > epsilon) {
            *verified = 0;
            *class = 'U';
            printf(" DT does not match the reference value of %15.8e\n", dtref);
        }
    } else {
        printf(" Unknown class\n");
    }
    if (*class != 'U') {
        printf(" Comparison of RMS-norms of residual\n");
    } else {
        printf(" RMS-norms of residual\n");
    }
    for (m = 0; m < 5; m++) {
        if (*class == 'U') {
            double _imopVarPre427;
            _imopVarPre427 = xcr[m];
            printf("          %2d  %20.13e\n", m, _imopVarPre427);
        } else {
            if (xcrdif[m] > epsilon) {
                *verified = 0;
                double _imopVarPre431;
                double _imopVarPre432;
                double _imopVarPre433;
                _imopVarPre431 = xcrdif[m];
                _imopVarPre432 = xcrref[m];
                _imopVarPre433 = xcr[m];
                printf(" FAILURE: %2d  %20.13e%20.13e%20.13e\n", m, _imopVarPre433, _imopVarPre432, _imopVarPre431);
            } else {
                double _imopVarPre437;
                double _imopVarPre438;
                double _imopVarPre439;
                _imopVarPre437 = xcrdif[m];
                _imopVarPre438 = xcrref[m];
                _imopVarPre439 = xcr[m];
                printf("          %2d  %20.13e%20.13e%20.13e\n", m, _imopVarPre439, _imopVarPre438, _imopVarPre437);
            }
        }
    }
    if (*class != 'U') {
        printf(" Comparison of RMS-norms of solution error\n");
    } else {
        printf(" RMS-norms of solution error\n");
    }
    for (m = 0; m < 5; m++) {
        if (*class == 'U') {
            double _imopVarPre441;
            _imopVarPre441 = xce[m];
            printf("          %2d  %20.13e\n", m, _imopVarPre441);
        } else {
            if (xcedif[m] > epsilon) {
                *verified = 0;
                double _imopVarPre445;
                double _imopVarPre446;
                double _imopVarPre447;
                _imopVarPre445 = xcedif[m];
                _imopVarPre446 = xceref[m];
                _imopVarPre447 = xce[m];
                printf(" FAILURE: %2d  %20.13e%20.13e%20.13e\n", m, _imopVarPre447, _imopVarPre446, _imopVarPre445);
            } else {
                double _imopVarPre451;
                double _imopVarPre452;
                double _imopVarPre453;
                _imopVarPre451 = xcedif[m];
                _imopVarPre452 = xceref[m];
                _imopVarPre453 = xce[m];
                printf("          %2d  %20.13e%20.13e%20.13e\n", m, _imopVarPre453, _imopVarPre452, _imopVarPre451);
            }
        }
    }
    if (*class != 'U') {
        printf(" Comparison of surface integral\n");
    } else {
        printf(" Surface integral\n");
    }
    if (*class == 'U') {
        printf("              %20.13e\n", xci);
    } else {
        if (xcidif > epsilon) {
            *verified = 0;
            printf(" FAILURE:     %20.13e%20.13e%20.13e\n", xci, xciref, xcidif);
        } else {
            printf("              %20.13e%20.13e%20.13e\n", xci, xciref, xcidif);
        }
    }
    if (*class == 'U') {
        printf(" No reference values provided\n");
        printf(" No verification performed\n");
    } else {
        if (*verified) {
            printf(" Verification Successful\n");
        } else {
            printf(" Verification failed\n");
        }
    }
}
