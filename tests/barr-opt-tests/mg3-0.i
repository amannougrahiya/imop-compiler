typedef long long __int64_t;
typedef long unsigned int __darwin_size_t;
typedef __int64_t __darwin_off_t;
typedef __darwin_size_t size_t;
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
void *malloc(size_t __size);
extern double fabs(double );
extern double pow(double , double );
extern double sqrt(double );
typedef int boolean;
extern double randlc(double *, double );
extern void vranlc(int , double * , double  , double *);
extern void timer_clear(int );
extern void timer_start(int );
extern void timer_stop(int );
extern double timer_read(int );
extern void c_print_results(char *name, char class , int n1 , int n2 , int n3 , int niter , int nthreads , double t , double mops , char *optype , int passed_verification , char *npbversion , char *compiletime , char *cc , char *clink , char *c_lib , char *c_inc , char *cflags , char *clinkflags , char *rand);
static int nx[11 + 1];
static int ny[11 + 1];
static int nz[11 + 1];
static char Class;
static int debug_vec[8];
static int m1[11 + 1];
static int m2[11 + 1];
static int m3[11 + 1];
static int lt;
static int lb;
static int is1;
static int is2;
static int is3;
static int ie1;
static int ie2;
static int ie3;
static void setup(int *n1, int *n2 , int *n3 , int lt);
static void mg3P(double ****u, double ***v , double ****r , double a[4] , double c[4] , int n1 , int n2 , int n3 , int k);
static void psinv(double ***r, double ***u , int n1 , int n2 , int n3 , double c[4] , int k);
static void resid(double ***u, double ***v , double ***r , int n1 , int n2 , int n3 , double a[4] , int k);
static void rprj3(double ***r, int m1k , int m2k , int m3k , double ***s , int m1j , int m2j , int m3j , int k);
static void interp(double ***z, int mm1 , int mm2 , int mm3 , double ***u , int n1 , int n2 , int n3 , int k);
static void norm2u3(double ***r, int n1 , int n2 , int n3 , double *rnm2 , double *rnmu , int nx , int ny , int nz);
static void rep_nrm(double ***u, int n1 , int n2 , int n3 , char *title , int kk);
static void comm3(double ***u, int n1 , int n2 , int n3 , int kk);
static void zran3(double ***z, int n1 , int n2 , int n3 , int nx , int ny , int k);
static void showall(double ***z, int n1 , int n2 , int n3);
static double power(double a, int n);
static void bubble(double ten[1037][2], int j1[1037][2] , int j2[1037][2] , int j3[1037][2] , int m , int ind);
static void zero3(double ***z, int n1 , int n2 , int n3);
int main(int argc, char *argv[]) {
    int k;
    int it;
    double t;
    double tinit;
    double mflops;
    int nthreads = 1;
    double ****u;
    double ***v;
    double ****r;
    double a[4];
    double c[4];
    double rnm2;
    double rnmu;
    double epsilon = 1.0e-8;
    int n1;
    int n2;
    int n3;
    int nit;
    double verify_value;
    boolean verified;
    int i;
    int j;
    int l;
    FILE *fp;
    timer_clear(1);
    timer_clear(2);
    timer_start(2);
    printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version" " - MG Benchmark\n\n");
    fp = fopen("mg.input", "r");
    if (fp != ((void *) 0)) {
        printf(" Reading from input file mg.input\n");
        int *_imopVarPre141;
        _imopVarPre141 = &lt;
        fscanf(fp, "%d", _imopVarPre141);
        int _imopVarPre143;
        _imopVarPre143 = fgetc(fp);
        while (_imopVarPre143 != '\n') {
            ;
            _imopVarPre143 = fgetc(fp);
        }
        int *_imopVarPre147;
        int *_imopVarPre148;
        int *_imopVarPre149;
        _imopVarPre147 = &nz[lt];
        _imopVarPre148 = &ny[lt];
        _imopVarPre149 = &nx[lt];
        fscanf(fp, "%d%d%d", _imopVarPre149, _imopVarPre148, _imopVarPre147);
        int _imopVarPre151;
        _imopVarPre151 = fgetc(fp);
        while (_imopVarPre151 != '\n') {
            ;
            _imopVarPre151 = fgetc(fp);
        }
        int *_imopVarPre153;
        _imopVarPre153 = &nit;
        fscanf(fp, "%d", _imopVarPre153);
        int _imopVarPre155;
        _imopVarPre155 = fgetc(fp);
        while (_imopVarPre155 != '\n') {
            ;
            _imopVarPre155 = fgetc(fp);
        }
        for (i = 0; i <= 7; i++) {
            int *_imopVarPre157;
            _imopVarPre157 = &debug_vec[i];
            fscanf(fp, "%d", _imopVarPre157);
        }
        fclose(fp);
    } else {
        printf(" No input file. Using compiled defaults\n");
        lt = 5;
        nit = 4;
        nx[lt] = 32;
        ny[lt] = 32;
        nz[lt] = 32;
        for (i = 0; i <= 7; i++) {
            debug_vec[i] = 0;
        }
    }
    int _imopVarPre158;
    _imopVarPre158 = (nx[lt] != ny[lt]);
    if (!_imopVarPre158) {
        _imopVarPre158 = (nx[lt] != nz[lt]);
    }
    if (_imopVarPre158) {
        Class = 'U';
    } else {
        int _imopVarPre160;
        _imopVarPre160 = nx[lt] == 32;
        if (_imopVarPre160) {
            _imopVarPre160 = nit == 4;
        }
        if (_imopVarPre160) {
            Class = 'S';
        } else {
            int _imopVarPre162;
            _imopVarPre162 = nx[lt] == 64;
            if (_imopVarPre162) {
                _imopVarPre162 = nit == 40;
            }
            if (_imopVarPre162) {
                Class = 'W';
            } else {
                int _imopVarPre164;
                _imopVarPre164 = nx[lt] == 256;
                if (_imopVarPre164) {
                    _imopVarPre164 = nit == 20;
                }
                if (_imopVarPre164) {
                    Class = 'B';
                } else {
                    int _imopVarPre166;
                    _imopVarPre166 = nx[lt] == 512;
                    if (_imopVarPre166) {
                        _imopVarPre166 = nit == 20;
                    }
                    if (_imopVarPre166) {
                        Class = 'C';
                    } else {
                        int _imopVarPre168;
                        _imopVarPre168 = nx[lt] == 256;
                        if (_imopVarPre168) {
                            _imopVarPre168 = nit == 4;
                        }
                        if (_imopVarPre168) {
                            Class = 'A';
                        } else {
                            Class = 'U';
                        }
                    }
                }
            }
        }
    }
    a[0] = -8.0 / 3.0;
    a[1] = 0.0;
    a[2] = 1.0 / 6.0;
    a[3] = 1.0 / 12.0;
    int _imopVarPre169;
    int _imopVarPre170;
    _imopVarPre169 = Class == 'A';
    if (!_imopVarPre169) {
        _imopVarPre170 = Class == 'S';
        if (!_imopVarPre170) {
            _imopVarPre170 = Class == 'W';
        }
        _imopVarPre169 = _imopVarPre170;
    }
    if (_imopVarPre169) {
        c[0] = -3.0 / 8.0;
        c[1] = 1.0 / 32.0;
        c[2] = -1.0 / 64.0;
        c[3] = 0.0;
    } else {
        c[0] = -3.0 / 17.0;
        c[1] = 1.0 / 33.0;
        c[2] = -1.0 / 61.0;
        c[3] = 0.0;
    }
    lb = 1;
    int *_imopVarPre174;
    int *_imopVarPre175;
    int *_imopVarPre176;
    _imopVarPre174 = &n3;
    _imopVarPre175 = &n2;
    _imopVarPre176 = &n1;
    setup(_imopVarPre176, _imopVarPre175, _imopVarPre174, lt);
    unsigned long int _imopVarPre179;
    void *_imopVarPre180;
    _imopVarPre179 = (lt + 1) * sizeof(double ***);
    _imopVarPre180 = malloc(_imopVarPre179);
    u = (double ****) _imopVarPre180;
    for (l = lt; l >= 1; l--) {
        unsigned long int _imopVarPre183;
        void *_imopVarPre184;
        _imopVarPre183 = m3[l] * sizeof(double **);
        _imopVarPre184 = malloc(_imopVarPre183);
        u[l] = (double ***) _imopVarPre184;
        for (k = 0; k < m3[l]; k++) {
            unsigned long int _imopVarPre187;
            void *_imopVarPre188;
            _imopVarPre187 = m2[l] * sizeof(double *);
            _imopVarPre188 = malloc(_imopVarPre187);
            u[l][k] = (double **) _imopVarPre188;
            for (j = 0; j < m2[l]; j++) {
                unsigned long int _imopVarPre191;
                void *_imopVarPre192;
                _imopVarPre191 = m1[l] * sizeof(double);
                _imopVarPre192 = malloc(_imopVarPre191);
                u[l][k][j] = (double *) _imopVarPre192;
            }
        }
    }
    unsigned long int _imopVarPre195;
    void *_imopVarPre196;
    _imopVarPre195 = m3[lt] * sizeof(double **);
    _imopVarPre196 = malloc(_imopVarPre195);
    v = (double ***) _imopVarPre196;
    for (k = 0; k < m3[lt]; k++) {
        unsigned long int _imopVarPre199;
        void *_imopVarPre200;
        _imopVarPre199 = m2[lt] * sizeof(double *);
        _imopVarPre200 = malloc(_imopVarPre199);
        v[k] = (double **) _imopVarPre200;
        for (j = 0; j < m2[lt]; j++) {
            unsigned long int _imopVarPre203;
            void *_imopVarPre204;
            _imopVarPre203 = m1[lt] * sizeof(double);
            _imopVarPre204 = malloc(_imopVarPre203);
            v[k][j] = (double *) _imopVarPre204;
        }
    }
    unsigned long int _imopVarPre207;
    void *_imopVarPre208;
    _imopVarPre207 = (lt + 1) * sizeof(double ***);
    _imopVarPre208 = malloc(_imopVarPre207);
    r = (double ****) _imopVarPre208;
    for (l = lt; l >= 1; l--) {
        unsigned long int _imopVarPre211;
        void *_imopVarPre212;
        _imopVarPre211 = m3[l] * sizeof(double **);
        _imopVarPre212 = malloc(_imopVarPre211);
        r[l] = (double ***) _imopVarPre212;
        for (k = 0; k < m3[l]; k++) {
            unsigned long int _imopVarPre215;
            void *_imopVarPre216;
            _imopVarPre215 = m2[l] * sizeof(double *);
            _imopVarPre216 = malloc(_imopVarPre215);
            r[l][k] = (double **) _imopVarPre216;
            for (j = 0; j < m2[l]; j++) {
                unsigned long int _imopVarPre219;
                void *_imopVarPre220;
                _imopVarPre219 = m1[l] * sizeof(double);
                _imopVarPre220 = malloc(_imopVarPre219);
                r[l][k][j] = (double *) _imopVarPre220;
            }
        }
    }
    double ***_imopVarPre222;
    _imopVarPre222 = u[lt];
    zero3(_imopVarPre222, n1, n2, n3);
    int _imopVarPre225;
    int _imopVarPre226;
    _imopVarPre225 = ny[lt];
    _imopVarPre226 = nx[lt];
    zran3(v, n1, n2, n3, _imopVarPre226, _imopVarPre225, lt);
    int _imopVarPre232;
    int _imopVarPre233;
    int _imopVarPre234;
    double *_imopVarPre235;
    double *_imopVarPre236;
    _imopVarPre232 = nz[lt];
    _imopVarPre233 = ny[lt];
    _imopVarPre234 = nx[lt];
    _imopVarPre235 = &rnmu;
    _imopVarPre236 = &rnm2;
    norm2u3(v, n1, n2, n3, _imopVarPre236, _imopVarPre235, _imopVarPre234, _imopVarPre233, _imopVarPre232);
    int _imopVarPre240;
    int _imopVarPre241;
    int _imopVarPre242;
    _imopVarPre240 = nz[lt];
    _imopVarPre241 = ny[lt];
    _imopVarPre242 = nx[lt];
    printf(" Size: %3dx%3dx%3d (class %1c)\n", _imopVarPre242, _imopVarPre241, _imopVarPre240, Class);
    printf(" Iterations: %3d\n", nit);
    double ***_imopVarPre245;
    double ***_imopVarPre246;
    _imopVarPre245 = r[lt];
    _imopVarPre246 = u[lt];
    resid(_imopVarPre246, v, _imopVarPre245, n1, n2, n3, a, lt);
    int _imopVarPre253;
    int _imopVarPre254;
    int _imopVarPre255;
    double *_imopVarPre256;
    double *_imopVarPre257;
    double ***_imopVarPre258;
    _imopVarPre253 = nz[lt];
    _imopVarPre254 = ny[lt];
    _imopVarPre255 = nx[lt];
    _imopVarPre256 = &rnmu;
    _imopVarPre257 = &rnm2;
    _imopVarPre258 = r[lt];
    norm2u3(_imopVarPre258, n1, n2, n3, _imopVarPre257, _imopVarPre256, _imopVarPre255, _imopVarPre254, _imopVarPre253);
    mg3P(u, v, r, a, c, n1, n2, n3, lt);
    double ***_imopVarPre261;
    double ***_imopVarPre262;
    _imopVarPre261 = r[lt];
    _imopVarPre262 = u[lt];
    resid(_imopVarPre262, v, _imopVarPre261, n1, n2, n3, a, lt);
    int *_imopVarPre266;
    int *_imopVarPre267;
    int *_imopVarPre268;
    _imopVarPre266 = &n3;
    _imopVarPre267 = &n2;
    _imopVarPre268 = &n1;
    setup(_imopVarPre268, _imopVarPre267, _imopVarPre266, lt);
    double ***_imopVarPre270;
    _imopVarPre270 = u[lt];
    zero3(_imopVarPre270, n1, n2, n3);
    int _imopVarPre273;
    int _imopVarPre274;
    _imopVarPre273 = ny[lt];
    _imopVarPre274 = nx[lt];
    zran3(v, n1, n2, n3, _imopVarPre274, _imopVarPre273, lt);
    timer_stop(2);
    timer_start(1);
    double ***_imopVarPre277;
    double ***_imopVarPre278;
    _imopVarPre277 = r[lt];
    _imopVarPre278 = u[lt];
    resid(_imopVarPre278, v, _imopVarPre277, n1, n2, n3, a, lt);
    int _imopVarPre285;
    int _imopVarPre286;
    int _imopVarPre287;
    double *_imopVarPre288;
    double *_imopVarPre289;
    double ***_imopVarPre290;
    _imopVarPre285 = nz[lt];
    _imopVarPre286 = ny[lt];
    _imopVarPre287 = nx[lt];
    _imopVarPre288 = &rnmu;
    _imopVarPre289 = &rnm2;
    _imopVarPre290 = r[lt];
    norm2u3(_imopVarPre290, n1, n2, n3, _imopVarPre289, _imopVarPre288, _imopVarPre287, _imopVarPre286, _imopVarPre285);
    for (it = 1; it <= nit; it++) {
        mg3P(u, v, r, a, c, n1, n2, n3, lt);
        double ***_imopVarPre293;
        double ***_imopVarPre294;
        _imopVarPre293 = r[lt];
        _imopVarPre294 = u[lt];
        resid(_imopVarPre294, v, _imopVarPre293, n1, n2, n3, a, lt);
    }
    int _imopVarPre301;
    int _imopVarPre302;
    int _imopVarPre303;
    double *_imopVarPre304;
    double *_imopVarPre305;
    double ***_imopVarPre306;
    _imopVarPre301 = nz[lt];
    _imopVarPre302 = ny[lt];
    _imopVarPre303 = nx[lt];
    _imopVarPre304 = &rnmu;
    _imopVarPre305 = &rnm2;
    _imopVarPre306 = r[lt];
    norm2u3(_imopVarPre306, n1, n2, n3, _imopVarPre305, _imopVarPre304, _imopVarPre303, _imopVarPre302, _imopVarPre301);
#pragma omp parallel
    {
    }
    timer_stop(1);
    t = timer_read(1);
    tinit = timer_read(2);
    verified = 0;
    verify_value = 0.0;
    printf(" Initialization time: %15.3f seconds\n", tinit);
    printf(" Benchmark completed\n");
    if (Class != 'U') {
        if (Class == 'S') {
            verify_value = 0.530770700573e-04;
        } else {
            if (Class == 'W') {
                verify_value = 0.250391406439e-17;
            } else {
                if (Class == 'A') {
                    verify_value = 0.2433365309e-5;
                } else {
                    if (Class == 'B') {
                        verify_value = 0.180056440132e-5;
                    } else {
                        if (Class == 'C') {
                            verify_value = 0.570674826298e-06;
                        }
                    }
                }
            }
        }
        double _imopVarPre309;
        double _imopVarPre310;
        _imopVarPre309 = rnm2 - verify_value;
        _imopVarPre310 = fabs(_imopVarPre309);
        if (_imopVarPre310 <= epsilon) {
            verified = 1;
            printf(" VERIFICATION SUCCESSFUL\n");
            printf(" L2 Norm is %20.12e\n", rnm2);
            double _imopVarPre312;
            _imopVarPre312 = rnm2 - verify_value;
            printf(" Error is   %20.12e\n", _imopVarPre312);
        } else {
            verified = 0;
            printf(" VERIFICATION FAILED\n");
            printf(" L2 Norm is             %20.12e\n", rnm2);
            printf(" The correct L2 Norm is %20.12e\n", verify_value);
        }
    } else {
        verified = 0;
        printf(" Problem size unknown\n");
        printf(" NO VERIFICATION PERFORMED\n");
    }
    if (t != 0.0) {
        int nn = nx[lt] * ny[lt] * nz[lt];
        mflops = 58. * nit * nn * 1.0e-6 / t;
    } else {
        mflops = 0.0;
    }
    int _imopVarPre316;
    int _imopVarPre317;
    int _imopVarPre318;
    _imopVarPre316 = nz[lt];
    _imopVarPre317 = ny[lt];
    _imopVarPre318 = nx[lt];
    c_print_results("MG", Class, _imopVarPre318, _imopVarPre317, _imopVarPre316, nit, nthreads, t, mflops, "          floating point", verified, "3.0 structured", "21 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "randdp");
}
static void setup(int *n1, int *n2 , int *n3 , int lt) {
    int k;
    for (k = lt - 1; k >= 1; k--) {
        nx[k] = nx[k + 1] / 2;
        ny[k] = ny[k + 1] / 2;
        nz[k] = nz[k + 1] / 2;
    }
    for (k = 1; k <= lt; k++) {
        m1[k] = nx[k] + 2;
        m2[k] = nz[k] + 2;
        m3[k] = ny[k] + 2;
    }
    is1 = 1;
    ie1 = nx[lt];
    *n1 = nx[lt] + 2;
    is2 = 1;
    ie2 = ny[lt];
    *n2 = ny[lt] + 2;
    is3 = 1;
    ie3 = nz[lt];
    *n3 = nz[lt] + 2;
    if (debug_vec[1] >= 1) {
        printf(" in setup, \n");
        printf("  lt  nx  ny  nz  n1  n2  n3 is1 is2 is3 ie1 ie2 ie3\n");
        int _imopVarPre325;
        int _imopVarPre326;
        int _imopVarPre327;
        int _imopVarPre328;
        int _imopVarPre329;
        int _imopVarPre330;
        _imopVarPre325 = *n3;
        _imopVarPre326 = *n2;
        _imopVarPre327 = *n1;
        _imopVarPre328 = nz[lt];
        _imopVarPre329 = ny[lt];
        _imopVarPre330 = nx[lt];
        printf("%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d\n", lt, _imopVarPre330, _imopVarPre329, _imopVarPre328, _imopVarPre327, _imopVarPre326, _imopVarPre325, is1, is2, is3, ie1, ie2, ie3);
    }
}
static void mg3P(double ****u, double ***v , double ****r , double a[4] , double c[4] , int n1 , int n2 , int n3 , int k) {
    int j;
    for (k = lt; k >= lb + 1; k--) {
        j = k - 1;
        int _imopVarPre339;
        int _imopVarPre340;
        int _imopVarPre341;
        double ***_imopVarPre342;
        int _imopVarPre343;
        int _imopVarPre344;
        int _imopVarPre345;
        double ***_imopVarPre346;
        _imopVarPre339 = m3[j];
        _imopVarPre340 = m2[j];
        _imopVarPre341 = m1[j];
        _imopVarPre342 = r[j];
        _imopVarPre343 = m3[k];
        _imopVarPre344 = m2[k];
        _imopVarPre345 = m1[k];
        _imopVarPre346 = r[k];
        rprj3(_imopVarPre346, _imopVarPre345, _imopVarPre344, _imopVarPre343, _imopVarPre342, _imopVarPre341, _imopVarPre340, _imopVarPre339, k);
    }
    k = lb;
    int _imopVarPre351;
    int _imopVarPre352;
    int _imopVarPre353;
    double ***_imopVarPre354;
    _imopVarPre351 = m3[k];
    _imopVarPre352 = m2[k];
    _imopVarPre353 = m1[k];
    _imopVarPre354 = u[k];
    zero3(_imopVarPre354, _imopVarPre353, _imopVarPre352, _imopVarPre351);
    int _imopVarPre360;
    int _imopVarPre361;
    int _imopVarPre362;
    double ***_imopVarPre363;
    double ***_imopVarPre364;
    _imopVarPre360 = m3[k];
    _imopVarPre361 = m2[k];
    _imopVarPre362 = m1[k];
    _imopVarPre363 = u[k];
    _imopVarPre364 = r[k];
    psinv(_imopVarPre364, _imopVarPre363, _imopVarPre362, _imopVarPre361, _imopVarPre360, c, k);
    for (k = lb + 1; k <= lt - 1; k++) {
        j = k - 1;
        int _imopVarPre369;
        int _imopVarPre370;
        int _imopVarPre371;
        double ***_imopVarPre372;
        _imopVarPre369 = m3[k];
        _imopVarPre370 = m2[k];
        _imopVarPre371 = m1[k];
        _imopVarPre372 = u[k];
        zero3(_imopVarPre372, _imopVarPre371, _imopVarPre370, _imopVarPre369);
        int _imopVarPre381;
        int _imopVarPre382;
        int _imopVarPre383;
        double ***_imopVarPre384;
        int _imopVarPre385;
        int _imopVarPre386;
        int _imopVarPre387;
        double ***_imopVarPre388;
        _imopVarPre381 = m3[k];
        _imopVarPre382 = m2[k];
        _imopVarPre383 = m1[k];
        _imopVarPre384 = u[k];
        _imopVarPre385 = m3[j];
        _imopVarPre386 = m2[j];
        _imopVarPre387 = m1[j];
        _imopVarPre388 = u[j];
        interp(_imopVarPre388, _imopVarPre387, _imopVarPre386, _imopVarPre385, _imopVarPre384, _imopVarPre383, _imopVarPre382, _imopVarPre381, k);
        int _imopVarPre395;
        int _imopVarPre396;
        int _imopVarPre397;
        double ***_imopVarPre398;
        double ***_imopVarPre399;
        double ***_imopVarPre400;
        _imopVarPre395 = m3[k];
        _imopVarPre396 = m2[k];
        _imopVarPre397 = m1[k];
        _imopVarPre398 = r[k];
        _imopVarPre399 = r[k];
        _imopVarPre400 = u[k];
        resid(_imopVarPre400, _imopVarPre399, _imopVarPre398, _imopVarPre397, _imopVarPre396, _imopVarPre395, a, k);
        int _imopVarPre406;
        int _imopVarPre407;
        int _imopVarPre408;
        double ***_imopVarPre409;
        double ***_imopVarPre410;
        _imopVarPre406 = m3[k];
        _imopVarPre407 = m2[k];
        _imopVarPre408 = m1[k];
        _imopVarPre409 = u[k];
        _imopVarPre410 = r[k];
        psinv(_imopVarPre410, _imopVarPre409, _imopVarPre408, _imopVarPre407, _imopVarPre406, c, k);
    }
    j = lt - 1;
    k = lt;
    double ***_imopVarPre416;
    int _imopVarPre417;
    int _imopVarPre418;
    int _imopVarPre419;
    double ***_imopVarPre420;
    _imopVarPre416 = u[lt];
    _imopVarPre417 = m3[j];
    _imopVarPre418 = m2[j];
    _imopVarPre419 = m1[j];
    _imopVarPre420 = u[j];
    interp(_imopVarPre420, _imopVarPre419, _imopVarPre418, _imopVarPre417, _imopVarPre416, n1, n2, n3, k);
    double ***_imopVarPre423;
    double ***_imopVarPre424;
    _imopVarPre423 = r[lt];
    _imopVarPre424 = u[lt];
    resid(_imopVarPre424, v, _imopVarPre423, n1, n2, n3, a, k);
    double ***_imopVarPre427;
    double ***_imopVarPre428;
    _imopVarPre427 = u[lt];
    _imopVarPre428 = r[lt];
    psinv(_imopVarPre428, _imopVarPre427, n1, n2, n3, c, k);
}
static void psinv(double ***r, double ***u , int n1 , int n2 , int n3 , double c[4] , int k) {
    int i3;
    int i2;
    int i1;
    double r1[1037];
    double r2[1037];
#pragma omp parallel default(shared) private(i1, i2, i3, r1, r2)
    {
#pragma omp for nowait
        for (i3 = 1; i3 < n3 - 1; i3++) {
            for (i2 = 1; i2 < n2 - 1; i2++) {
                for (i1 = 0; i1 < n1; i1++) {
                    r1[i1] = r[i3][i2 - 1][i1] + r[i3][i2 + 1][i1] + r[i3 - 1][i2][i1] + r[i3 + 1][i2][i1];
                    r2[i1] = r[i3 - 1][i2 - 1][i1] + r[i3 - 1][i2 + 1][i1] + r[i3 + 1][i2 - 1][i1] + r[i3 + 1][i2 + 1][i1];
                }
                for (i1 = 1; i1 < n1 - 1; i1++) {
                    u[i3][i2][i1] = u[i3][i2][i1] + c[0] * r[i3][i2][i1] + c[1] * (r[i3][i2][i1 - 1] + r[i3][i2][i1 + 1] + r1[i1]) + c[2] * (r2[i1] + r1[i1 - 1] + r1[i1 + 1]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    comm3(u, n1, n2, n3, k);
    if (debug_vec[0] >= 1) {
        rep_nrm(u, n1, n2, n3, "   psinv", k);
    }
    if (debug_vec[3] >= k) {
        showall(u, n1, n2, n3);
    }
}
static void resid(double ***u, double ***v , double ***r , int n1 , int n2 , int n3 , double a[4] , int k) {
    int i3;
    int i2;
    int i1;
    double u1[1037];
    double u2[1037];
#pragma omp parallel default(shared) private(i1, i2, i3, u1, u2)
    {
#pragma omp for nowait
        for (i3 = 1; i3 < n3 - 1; i3++) {
            for (i2 = 1; i2 < n2 - 1; i2++) {
                for (i1 = 0; i1 < n1; i1++) {
                    u1[i1] = u[i3][i2 - 1][i1] + u[i3][i2 + 1][i1] + u[i3 - 1][i2][i1] + u[i3 + 1][i2][i1];
                    u2[i1] = u[i3 - 1][i2 - 1][i1] + u[i3 - 1][i2 + 1][i1] + u[i3 + 1][i2 - 1][i1] + u[i3 + 1][i2 + 1][i1];
                }
                for (i1 = 1; i1 < n1 - 1; i1++) {
                    r[i3][i2][i1] = v[i3][i2][i1] - a[0] * u[i3][i2][i1] - a[2] * (u2[i1] + u1[i1 - 1] + u1[i1 + 1]) - a[3] * (u2[i1 - 1] + u2[i1 + 1]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    comm3(r, n1, n2, n3, k);
    if (debug_vec[0] >= 1) {
        rep_nrm(r, n1, n2, n3, "   resid", k);
    }
    if (debug_vec[2] >= k) {
        showall(r, n1, n2, n3);
    }
}
static void rprj3(double ***r, int m1k , int m2k , int m3k , double ***s , int m1j , int m2j , int m3j , int k) {
    int j3;
    int j2;
    int j1;
    int i3;
    int i2;
    int i1;
    int d1;
    int d2;
    int d3;
    double x1[1037];
    double y1[1037];
    double x2;
    double y2;
    if (m1k == 3) {
        d1 = 2;
    } else {
        d1 = 1;
    }
    if (m2k == 3) {
        d2 = 2;
    } else {
        d2 = 1;
    }
    if (m3k == 3) {
        d3 = 2;
    } else {
        d3 = 1;
    }
#pragma omp parallel default(shared) private(j1, j2, j3, i1, i2, i3, x1, y1, x2, y2)
    {
#pragma omp for nowait
        for (j3 = 1; j3 < m3j - 1; j3++) {
            i3 = 2 * j3 - d3;
            for (j2 = 1; j2 < m2j - 1; j2++) {
                i2 = 2 * j2 - d2;
                for (j1 = 1; j1 < m1j; j1++) {
                    i1 = 2 * j1 - d1;
                    x1[i1] = r[i3 + 1][i2][i1] + r[i3 + 1][i2 + 2][i1] + r[i3][i2 + 1][i1] + r[i3 + 2][i2 + 1][i1];
                    y1[i1] = r[i3][i2][i1] + r[i3 + 2][i2][i1] + r[i3][i2 + 2][i1] + r[i3 + 2][i2 + 2][i1];
                }
                for (j1 = 1; j1 < m1j - 1; j1++) {
                    i1 = 2 * j1 - d1;
                    y2 = r[i3][i2][i1 + 1] + r[i3 + 2][i2][i1 + 1] + r[i3][i2 + 2][i1 + 1] + r[i3 + 2][i2 + 2][i1 + 1];
                    x2 = r[i3 + 1][i2][i1 + 1] + r[i3 + 1][i2 + 2][i1 + 1] + r[i3][i2 + 1][i1 + 1] + r[i3 + 2][i2 + 1][i1 + 1];
                    s[j3][j2][j1] = 0.5 * r[i3 + 1][i2 + 1][i1 + 1] + 0.25 * (r[i3 + 1][i2 + 1][i1] + r[i3 + 1][i2 + 1][i1 + 2] + x2) + 0.125 * (x1[i1] + x1[i1 + 2] + y2) + 0.0625 * (y1[i1] + y1[i1 + 2]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    int _imopVarPre430;
    _imopVarPre430 = k - 1;
    comm3(s, m1j, m2j, m3j, _imopVarPre430);
    if (debug_vec[0] >= 1) {
        int _imopVarPre432;
        _imopVarPre432 = k - 1;
        rep_nrm(s, m1j, m2j, m3j, "   rprj3", _imopVarPre432);
    }
    if (debug_vec[4] >= k) {
        showall(s, m1j, m2j, m3j);
    }
}
static void interp(double ***z, int mm1 , int mm2 , int mm3 , double ***u , int n1 , int n2 , int n3 , int k) {
    int i3;
    int i2;
    int i1;
    int d1;
    int d2;
    int d3;
    int t1;
    int t2;
    int t3;
    double z1[1037];
    double z2[1037];
    double z3[1037];
    int _imopVarPre435;
    int _imopVarPre436;
    _imopVarPre435 = n1 != 3;
    if (_imopVarPre435) {
        _imopVarPre436 = n2 != 3;
        if (_imopVarPre436) {
            _imopVarPre436 = n3 != 3;
        }
        _imopVarPre435 = _imopVarPre436;
    }
    if (_imopVarPre435) {
#pragma omp parallel default(shared) private(i1, i2, i3, z1, z2, z3)
        {
#pragma omp for nowait
            for (i3 = 0; i3 < mm3 - 1; i3++) {
                for (i2 = 0; i2 < mm2 - 1; i2++) {
                    for (i1 = 0; i1 < mm1; i1++) {
                        z1[i1] = z[i3][i2 + 1][i1] + z[i3][i2][i1];
                        z2[i1] = z[i3 + 1][i2][i1] + z[i3][i2][i1];
                        z3[i1] = z[i3 + 1][i2 + 1][i1] + z[i3 + 1][i2][i1] + z1[i1];
                    }
                    for (i1 = 0; i1 < mm1 - 1; i1++) {
                        u[2 * i3][2 * i2][2 * i1] = u[2 * i3][2 * i2][2 * i1] + z[i3][i2][i1];
                        u[2 * i3][2 * i2][2 * i1 + 1] = u[2 * i3][2 * i2][2 * i1 + 1] + 0.5 * (z[i3][i2][i1 + 1] + z[i3][i2][i1]);
                    }
                    for (i1 = 0; i1 < mm1 - 1; i1++) {
                        u[2 * i3][2 * i2 + 1][2 * i1] = u[2 * i3][2 * i2 + 1][2 * i1] + 0.5 * z1[i1];
                        u[2 * i3][2 * i2 + 1][2 * i1 + 1] = u[2 * i3][2 * i2 + 1][2 * i1 + 1] + 0.25 * (z1[i1] + z1[i1 + 1]);
                    }
                    for (i1 = 0; i1 < mm1 - 1; i1++) {
                        u[2 * i3 + 1][2 * i2][2 * i1] = u[2 * i3 + 1][2 * i2][2 * i1] + 0.5 * z2[i1];
                        u[2 * i3 + 1][2 * i2][2 * i1 + 1] = u[2 * i3 + 1][2 * i2][2 * i1 + 1] + 0.25 * (z2[i1] + z2[i1 + 1]);
                    }
                    for (i1 = 0; i1 < mm1 - 1; i1++) {
                        u[2 * i3 + 1][2 * i2 + 1][2 * i1] = u[2 * i3 + 1][2 * i2 + 1][2 * i1] + 0.25 * z3[i1];
                        u[2 * i3 + 1][2 * i2 + 1][2 * i1 + 1] = u[2 * i3 + 1][2 * i2 + 1][2 * i1 + 1] + 0.125 * (z3[i1] + z3[i1 + 1]);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
    } else {
        if (n1 == 3) {
            d1 = 2;
            t1 = 1;
        } else {
            d1 = 1;
            t1 = 0;
        }
        if (n2 == 3) {
            d2 = 2;
            t2 = 1;
        } else {
            d2 = 1;
            t2 = 0;
        }
        if (n3 == 3) {
            d3 = 2;
            t3 = 1;
        } else {
            d3 = 1;
            t3 = 0;
        }
#pragma omp parallel default(shared) private(i1, i2, i3)
        {
#pragma omp for nowait
            for (i3 = d3; i3 <= mm3 - 1; i3++) {
                for (i2 = d2; i2 <= mm2 - 1; i2++) {
                    for (i1 = d1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - d3 - 1][2 * i2 - d2 - 1][2 * i1 - d1 - 1] = u[2 * i3 - d3 - 1][2 * i2 - d2 - 1][2 * i1 - d1 - 1] + z[i3 - 1][i2 - 1][i1 - 1];
                    }
                    for (i1 = 1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - d3 - 1][2 * i2 - d2 - 1][2 * i1 - t1 - 1] = u[2 * i3 - d3 - 1][2 * i2 - d2 - 1][2 * i1 - t1 - 1] + 0.5 * (z[i3 - 1][i2 - 1][i1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                }
                for (i2 = 1; i2 <= mm2 - 1; i2++) {
                    for (i1 = d1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - d3 - 1][2 * i2 - t2 - 1][2 * i1 - d1 - 1] = u[2 * i3 - d3 - 1][2 * i2 - t2 - 1][2 * i1 - d1 - 1] + 0.5 * (z[i3 - 1][i2][i1 - 1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                    for (i1 = 1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - d3 - 1][2 * i2 - t2 - 1][2 * i1 - t1 - 1] = u[2 * i3 - d3 - 1][2 * i2 - t2 - 1][2 * i1 - t1 - 1] + 0.25 * (z[i3 - 1][i2][i1] + z[i3 - 1][i2 - 1][i1] + z[i3 - 1][i2][i1 - 1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
            for (i3 = 1; i3 <= mm3 - 1; i3++) {
                for (i2 = d2; i2 <= mm2 - 1; i2++) {
                    for (i1 = d1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - t3 - 1][2 * i2 - d2 - 1][2 * i1 - d1 - 1] = u[2 * i3 - t3 - 1][2 * i2 - d2 - 1][2 * i1 - d1 - 1] + 0.5 * (z[i3][i2 - 1][i1 - 1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                    for (i1 = 1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - t3 - 1][2 * i2 - d2 - 1][2 * i1 - t1 - 1] = u[2 * i3 - t3 - 1][2 * i2 - d2 - 1][2 * i1 - t1 - 1] + 0.25 * (z[i3][i2 - 1][i1] + z[i3][i2 - 1][i1 - 1] + z[i3 - 1][i2 - 1][i1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                }
                for (i2 = 1; i2 <= mm2 - 1; i2++) {
                    for (i1 = d1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - t3 - 1][2 * i2 - t2 - 1][2 * i1 - d1 - 1] = u[2 * i3 - t3 - 1][2 * i2 - t2 - 1][2 * i1 - d1 - 1] + 0.25 * (z[i3][i2][i1 - 1] + z[i3][i2 - 1][i1 - 1] + z[i3 - 1][i2][i1 - 1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                    for (i1 = 1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - t3 - 1][2 * i2 - t2 - 1][2 * i1 - t1 - 1] = u[2 * i3 - t3 - 1][2 * i2 - t2 - 1][2 * i1 - t1 - 1] + 0.125 * (z[i3][i2][i1] + z[i3][i2 - 1][i1] + z[i3][i2][i1 - 1] + z[i3][i2 - 1][i1 - 1] + z[i3 - 1][i2][i1] + z[i3 - 1][i2 - 1][i1] + z[i3 - 1][i2][i1 - 1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                }
            }
        }
    }
    if (debug_vec[0] >= 1) {
        int _imopVarPre438;
        _imopVarPre438 = k - 1;
        rep_nrm(z, mm1, mm2, mm3, "z: inter", _imopVarPre438);
        rep_nrm(u, n1, n2, n3, "u: inter", k);
    }
    if (debug_vec[5] >= k) {
        showall(z, mm1, mm2, mm3);
        showall(u, n1, n2, n3);
    }
}
static void norm2u3(double ***r, int n1 , int n2 , int n3 , double *rnm2 , double *rnmu , int nx , int ny , int nz) {
    double s = 0.0;
    int i3;
    int i2;
    int i1;
    int n;
    double aman = 0.0;
    double tmp = 0.0;
    n = nx * ny * nz;
#pragma omp parallel default(shared) private(i1, i2, i3, aman)
    {
#pragma omp for reduction(+:s) reduction(max:tmp) nowait
        for (i3 = 1; i3 < n3 - 1; i3++) {
            for (i2 = 1; i2 < n2 - 1; i2++) {
                for (i1 = 1; i1 < n1 - 1; i1++) {
                    s = s + r[i3][i2][i1] * r[i3][i2][i1];
                    double _imopVarPre440;
                    double _imopVarPre441;
                    _imopVarPre440 = r[i3][i2][i1];
                    _imopVarPre441 = fabs(_imopVarPre440);
                    aman = _imopVarPre441;
                    if (aman > tmp) {
                        tmp = aman;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    *rnmu = tmp;
    double _imopVarPre443;
    double _imopVarPre444;
    _imopVarPre443 = s / (double) n;
    _imopVarPre444 = sqrt(_imopVarPre443);
    *rnm2 = _imopVarPre444;
}
static void rep_nrm(double ***u, int n1 , int n2 , int n3 , char *title , int kk) {
    double rnm2;
    double rnmu;
    int _imopVarPre450;
    int _imopVarPre451;
    int _imopVarPre452;
    double *_imopVarPre453;
    double *_imopVarPre454;
    _imopVarPre450 = nz[kk];
    _imopVarPre451 = ny[kk];
    _imopVarPre452 = nx[kk];
    _imopVarPre453 = &rnmu;
    _imopVarPre454 = &rnm2;
    norm2u3(u, n1, n2, n3, _imopVarPre454, _imopVarPre453, _imopVarPre452, _imopVarPre451, _imopVarPre450);
    printf(" Level%2d in %8s: norms =%21.14e%21.14e\n", kk, title, rnm2, rnmu);
}
static void comm3(double ***u, int n1 , int n2 , int n3 , int kk) {
    int i1;
    int i2;
    int i3;
#pragma omp parallel default(shared) private(i1, i2, i3)
    {
#pragma omp for nowait
        for (i3 = 1; i3 < n3 - 1; i3++) {
            for (i2 = 1; i2 < n2 - 1; i2++) {
                u[i3][i2][n1 - 1] = u[i3][i2][1];
                u[i3][i2][0] = u[i3][i2][n1 - 2];
            }
            for (i1 = 0; i1 < n1; i1++) {
                u[i3][n2 - 1][i1] = u[i3][1][i1];
                u[i3][0][i1] = u[i3][n2 - 2][i1];
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for nowait
        for (i2 = 0; i2 < n2; i2++) {
            for (i1 = 0; i1 < n1; i1++) {
                u[n3 - 1][i2][i1] = u[1][i2][i1];
                u[0][i2][i1] = u[n3 - 2][i2][i1];
            }
        }
    }
}
static void zran3(double ***z, int n1 , int n2 , int n3 , int nx , int ny , int k) {
    int i0;
    int m0;
    int m1;
    int i1;
    int i2;
    int i3;
    int d1;
    int e1;
    int e2;
    int e3;
    double xx;
    double x0;
    double x1;
    double a1;
    double a2;
    double ai;
    double ten[10][2];
    double best;
    int i;
    int j1[10][2];
    int j2[10][2];
    int j3[10][2];
    int jg[4][10][2];
    double rdummy;
    double _imopVarPre456;
    double _imopVarPre457;
    _imopVarPre456 = pow(5.0, 13);
    _imopVarPre457 = power(_imopVarPre456, nx);
    a1 = _imopVarPre457;
    int _imopVarPre460;
    double _imopVarPre461;
    double _imopVarPre462;
    _imopVarPre460 = nx * ny;
    _imopVarPre461 = pow(5.0, 13);
    _imopVarPre462 = power(_imopVarPre461, _imopVarPre460);
    a2 = _imopVarPre462;
    zero3(z, n1, n2, n3);
    i = is1 - 1 + nx * (is2 - 1 + ny * (is3 - 1));
    double _imopVarPre464;
    double _imopVarPre465;
    _imopVarPre464 = pow(5.0, 13);
    _imopVarPre465 = power(_imopVarPre464, i);
    ai = _imopVarPre465;
    d1 = ie1 - is1 + 1;
    e1 = ie1 - is1 + 2;
    e2 = ie2 - is2 + 2;
    e3 = ie3 - is3 + 2;
    x0 = 314159265.e0;
    double *_imopVarPre467;
    double _imopVarPre468;
    _imopVarPre467 = &x0;
    _imopVarPre468 = randlc(_imopVarPre467, ai);
    rdummy = _imopVarPre468;
    for (i3 = 1; i3 < e3; i3++) {
        x1 = x0;
        for (i2 = 1; i2 < e2; i2++) {
            xx = x1;
            double *_imopVarPre472;
            double _imopVarPre473;
            double *_imopVarPre474;
            _imopVarPre472 = &(z[i3][i2][0]);
            _imopVarPre473 = pow(5.0, 13);
            _imopVarPre474 = &xx;
            vranlc(d1, _imopVarPre474, _imopVarPre473, _imopVarPre472);
            double *_imopVarPre476;
            double _imopVarPre477;
            _imopVarPre476 = &x1;
            _imopVarPre477 = randlc(_imopVarPre476, a1);
            rdummy = _imopVarPre477;
        }
        double *_imopVarPre479;
        double _imopVarPre480;
        _imopVarPre479 = &x0;
        _imopVarPre480 = randlc(_imopVarPre479, a2);
        rdummy = _imopVarPre480;
    }
    for (i = 0; i < 10; i++) {
        ten[i][1] = 0.0;
        j1[i][1] = 0;
        j2[i][1] = 0;
        j3[i][1] = 0;
        ten[i][0] = 1.0;
        j1[i][0] = 0;
        j2[i][0] = 0;
        j3[i][0] = 0;
    }
    for (i3 = 1; i3 < n3 - 1; i3++) {
        for (i2 = 1; i2 < n2 - 1; i2++) {
            for (i1 = 1; i1 < n1 - 1; i1++) {
                if (z[i3][i2][i1] > ten[0][1]) {
                    ten[0][1] = z[i3][i2][i1];
                    j1[0][1] = i1;
                    j2[0][1] = i2;
                    j3[0][1] = i3;
                    bubble(ten, j1, j2, j3, 10, 1);
                }
                if (z[i3][i2][i1] < ten[0][0]) {
                    ten[0][0] = z[i3][i2][i1];
                    j1[0][0] = i1;
                    j2[0][0] = i2;
                    j3[0][0] = i3;
                    bubble(ten, j1, j2, j3, 10, 0);
                }
            }
        }
    }
    i1 = 10 - 1;
    i0 = 10 - 1;
    for (i = 10 - 1; i >= 0; i--) {
        best = z[j3[i1][1]][j2[i1][1]][j1[i1][1]];
        if (best == z[j3[i1][1]][j2[i1][1]][j1[i1][1]]) {
            jg[0][i][1] = 0;
            jg[1][i][1] = is1 - 1 + j1[i1][1];
            jg[2][i][1] = is2 - 1 + j2[i1][1];
            jg[3][i][1] = is3 - 1 + j3[i1][1];
            i1 = i1 - 1;
        } else {
            jg[0][i][1] = 0;
            jg[1][i][1] = 0;
            jg[2][i][1] = 0;
            jg[3][i][1] = 0;
        }
        ten[i][1] = best;
        best = z[j3[i0][0]][j2[i0][0]][j1[i0][0]];
        if (best == z[j3[i0][0]][j2[i0][0]][j1[i0][0]]) {
            jg[0][i][0] = 0;
            jg[1][i][0] = is1 - 1 + j1[i0][0];
            jg[2][i][0] = is2 - 1 + j2[i0][0];
            jg[3][i][0] = is3 - 1 + j3[i0][0];
            i0 = i0 - 1;
        } else {
            jg[0][i][0] = 0;
            jg[1][i][0] = 0;
            jg[2][i][0] = 0;
            jg[3][i][0] = 0;
        }
        ten[i][0] = best;
    }
    m1 = i1 + 1;
    m0 = i0 + 1;
#pragma omp parallel private(i2, i1)
    {
#pragma omp for nowait
        for (i3 = 0; i3 < n3; i3++) {
            for (i2 = 0; i2 < n2; i2++) {
                for (i1 = 0; i1 < n1; i1++) {
                    z[i3][i2][i1] = 0.0;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    for (i = 10 - 1; i >= m0; i--) {
        z[j3[i][0]][j2[i][0]][j1[i][0]] = -1.0;
    }
    for (i = 10 - 1; i >= m1; i--) {
        z[j3[i][1]][j2[i][1]][j1[i][1]] = 1.0;
    }
    comm3(z, n1, n2, n3, k);
}
static void showall(double ***z, int n1 , int n2 , int n3) {
    int i1;
    int i2;
    int i3;
    int m1;
    int m2;
    int m3;
    int _imopVarPre483;
    int _imopVarPre484;
    _imopVarPre483 = (n1 < 18);
    if (_imopVarPre483) {
        _imopVarPre484 = n1;
    } else {
        _imopVarPre484 = 18;
    }
    m1 = _imopVarPre484;
    int _imopVarPre487;
    int _imopVarPre488;
    _imopVarPre487 = (n2 < 14);
    if (_imopVarPre487) {
        _imopVarPre488 = n2;
    } else {
        _imopVarPre488 = 14;
    }
    m2 = _imopVarPre488;
    int _imopVarPre491;
    int _imopVarPre492;
    _imopVarPre491 = (n3 < 18);
    if (_imopVarPre491) {
        _imopVarPre492 = n3;
    } else {
        _imopVarPre492 = 18;
    }
    m3 = _imopVarPre492;
    printf("\n");
    for (i3 = 0; i3 < m3; i3++) {
        for (i1 = 0; i1 < m1; i1++) {
            for (i2 = 0; i2 < m2; i2++) {
                double _imopVarPre494;
                _imopVarPre494 = z[i3][i2][i1];
                printf("%6.3f", _imopVarPre494);
            }
            printf("\n");
        }
        printf(" - - - - - - - \n");
    }
    printf("\n");
}
static double power(double ax, int n) {
    double aj;
    int nj;
    double rdummy;
    double power;
    power = 1.0;
    nj = n;
    aj = ax;
    while (nj != 0) {
        if ((nj % 2) == 1) {
            double *_imopVarPre496;
            double _imopVarPre497;
            _imopVarPre496 = &power;
            _imopVarPre497 = randlc(_imopVarPre496, aj);
            rdummy = _imopVarPre497;
        }
        double *_imopVarPre499;
        double _imopVarPre500;
        _imopVarPre499 = &aj;
        _imopVarPre500 = randlc(_imopVarPre499, aj);
        rdummy = _imopVarPre500;
        nj = nj / 2;
    }
    return power;
}
static void bubble(double ten[1037][2], int j1[1037][2] , int j2[1037][2] , int j3[1037][2] , int m , int ind) {
    double temp;
    int i;
    int j_temp;
    if (ind == 1) {
        for (i = 0; i < m - 1; i++) {
            if (ten[i][ind] > ten[i + 1][ind]) {
                temp = ten[i + 1][ind];
                ten[i + 1][ind] = ten[i][ind];
                ten[i][ind] = temp;
                j_temp = j1[i + 1][ind];
                j1[i + 1][ind] = j1[i][ind];
                j1[i][ind] = j_temp;
                j_temp = j2[i + 1][ind];
                j2[i + 1][ind] = j2[i][ind];
                j2[i][ind] = j_temp;
                j_temp = j3[i + 1][ind];
                j3[i + 1][ind] = j3[i][ind];
                j3[i][ind] = j_temp;
            } else {
                return;
            }
        }
    } else {
        for (i = 0; i < m - 1; i++) {
            if (ten[i][ind] < ten[i + 1][ind]) {
                temp = ten[i + 1][ind];
                ten[i + 1][ind] = ten[i][ind];
                ten[i][ind] = temp;
                j_temp = j1[i + 1][ind];
                j1[i + 1][ind] = j1[i][ind];
                j1[i][ind] = j_temp;
                j_temp = j2[i + 1][ind];
                j2[i + 1][ind] = j2[i][ind];
                j2[i][ind] = j_temp;
                j_temp = j3[i + 1][ind];
                j3[i + 1][ind] = j3[i][ind];
                j3[i][ind] = j_temp;
            } else {
                return;
            }
        }
    }
}
static void zero3(double ***z, int n1 , int n2 , int n3) {
    int i1;
    int i2;
    int i3;
#pragma omp parallel private(i1, i2, i3)
    {
#pragma omp for nowait
        for (i3 = 0; i3 < n3; i3++) {
            for (i2 = 0; i2 < n2; i2++) {
                for (i1 = 0; i1 < n1; i1++) {
                    z[i3][i2][i1] = 0.0;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
}
