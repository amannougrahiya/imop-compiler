struct __sFILEX ;
int printf(const char *restrict , ...);
extern double log(double );
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
static double x[2 * (1 << 16)];
#pragma omp threadprivate(x)
static double q[10];
int main(int argc, char **argv) {
    double Mops;
    double t1;
    double t2;
    double sx;
    double sy;
    double tm;
    double an;
    double tt;
    double gc;
    double dum[3] = {1.0, 1.0 , 1.0};
    int np;
    int i;
    int k;
    int nit;
    int k_offset;
    int j;
    int nthreads = 1;
    boolean verified;
    char size[13 + 1];
    printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version" " - EP Benchmark\n");
    int _imopVarPre149;
    double _imopVarPre150;
    int _imopVarPre153;
    int _imopVarPre154;
    unsigned int _imopVarPre155;
    _imopVarPre149 = 24 + 1;
    _imopVarPre150 = pow(2.0, _imopVarPre149);
    _imopVarPre153 = 2 > 1;
    if (_imopVarPre153) {
        _imopVarPre154 = 1;
    } else {
        _imopVarPre154 = 0;
    }
    _imopVarPre155 = __builtin_object_size(size, _imopVarPre154);
    __builtin___sprintf_chk(size, 0, _imopVarPre155, "%12.0f", _imopVarPre150);
    for (j = 13; j >= 1; j--) {
        if (size[j] == '.') {
            size[j] = ' ';
        }
    }
    printf(" Number of random numbers generated: %13s\n", size);
    verified = 0;
    np = (1 << (24 - 16));
    double *_imopVarPre159;
    double _imopVarPre160;
    double *_imopVarPre161;
    _imopVarPre159 = &(dum[2]);
    _imopVarPre160 = dum[1];
    _imopVarPre161 = &(dum[0]);
    vranlc(0, _imopVarPre161, _imopVarPre160, _imopVarPre159);
    double _imopVarPre164;
    double *_imopVarPre165;
    double _imopVarPre166;
    _imopVarPre164 = dum[2];
    _imopVarPre165 = &(dum[1]);
    _imopVarPre166 = randlc(_imopVarPre165, _imopVarPre164);
    dum[0] = _imopVarPre166;
#pragma omp parallel default(shared) private(i)
    {
#pragma omp for nowait
        for (i = 0; i < 2 * (1 << 16); i++) {
            x[i] = -1.0e99;
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    int _imopVarPre201;
    double _imopVarPre202;
    double _imopVarPre203;
    double _imopVarPre204;
    double _imopVarPre205;
    _imopVarPre201 = (1.0 > 1.0);
    if (_imopVarPre201) {
        _imopVarPre202 = 1.0;
    } else {
        _imopVarPre202 = 1.0;
    }
    _imopVarPre203 = fabs(_imopVarPre202);
    _imopVarPre204 = sqrt(_imopVarPre203);
    _imopVarPre205 = log(_imopVarPre204);
    Mops = _imopVarPre205;
    timer_clear(1);
    timer_clear(2);
    timer_clear(3);
    timer_start(1);
    double *_imopVarPre207;
    _imopVarPre207 = &t1;
    vranlc(0, _imopVarPre207, 1220703125.0, x);
    t1 = 1220703125.0;
    for (i = 1; i <= 16 + 1; i++) {
        double *_imopVarPre209;
        double _imopVarPre210;
        _imopVarPre209 = &t1;
        _imopVarPre210 = randlc(_imopVarPre209, t1);
        t2 = _imopVarPre210;
    }
    an = t1;
    tt = 271828183.0;
    gc = 0.0;
    sx = 0.0;
    sy = 0.0;
    for (i = 0; i <= 10 - 1; i++) {
        q[i] = 0.0;
    }
    k_offset = -1;
#pragma omp parallel copyin(x)
    {
        double t1;
        double t2;
        double t3;
        double t4;
        double x1;
        double x2;
        int kk;
        int i;
        int ik;
        int l;
        double qq[10];
        for (i = 0; i < 10; i++) {
            qq[i] = 0.0;
        }
#pragma omp for reduction(+:sx, sy) schedule(static) nowait
        for (k = 1; k <= np; k++) {
            kk = k_offset + k;
            t1 = 271828183.0;
            t2 = an;
            for (i = 1; i <= 100; i++) {
                ik = kk / 2;
                if (2 * ik != kk) {
                    double *_imopVarPre212;
                    double _imopVarPre213;
                    _imopVarPre212 = &t1;
                    _imopVarPre213 = randlc(_imopVarPre212, t2);
                    t3 = _imopVarPre213;
                }
                if (ik == 0) {
                    break;
                }
                double *_imopVarPre215;
                double _imopVarPre216;
                _imopVarPre215 = &t2;
                _imopVarPre216 = randlc(_imopVarPre215, t2);
                t3 = _imopVarPre216;
                kk = ik;
            }
            if (0 == 1) {
                timer_start(3);
            }
            double *_imopVarPre220;
            double *_imopVarPre221;
            int _imopVarPre222;
            _imopVarPre220 = x - 1;
            _imopVarPre221 = &t1;
            _imopVarPre222 = 2 * (1 << 16);
            vranlc(_imopVarPre222, _imopVarPre221, 1220703125.0, _imopVarPre220);
            if (0 == 1) {
                timer_stop(3);
            }
            if (0 == 1) {
                timer_start(2);
            }
            for (i = 0; i < (1 << 16); i++) {
                x1 = 2.0 * x[2 * i] - 1.0;
                x2 = 2.0 * x[2 * i + 1] - 1.0;
                t1 = (x1 * x1) + (x2 * x2);
                if (t1 <= 1.0) {
                    double _imopVarPre227;
                    double _imopVarPre228;
                    double _imopVarPre229;
                    _imopVarPre227 = log(t1);
                    _imopVarPre228 = -2.0 * _imopVarPre227 / t1;
                    _imopVarPre229 = sqrt(_imopVarPre228);
                    t2 = _imopVarPre229;
                    t3 = (x1 * t2);
                    t4 = (x2 * t2);
                    double _imopVarPre250;
                    double _imopVarPre251;
                    int _imopVarPre252;
                    double _imopVarPre253;
                    double _imopVarPre255;
                    double _imopVarPre257;
                    _imopVarPre250 = fabs(t3);
                    _imopVarPre251 = fabs(t4);
                    _imopVarPre252 = (_imopVarPre250 > _imopVarPre251);
                    if (_imopVarPre252) {
                        _imopVarPre255 = fabs(t3);
                        _imopVarPre253 = _imopVarPre255;
                    } else {
                        _imopVarPre257 = fabs(t4);
                        _imopVarPre253 = _imopVarPre257;
                    }
                    l = _imopVarPre253;
                    qq[l] += 1.0;
                    sx = sx + t3;
                    sy = sy + t4;
                }
            }
            if (0 == 1) {
                timer_stop(2);
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
// #pragma omp dummyFlush CRITICAL_START
#pragma omp critical
        {
            for (i = 0; i <= 10 - 1; i++) {
                q[i] += qq[i];
            }
        }
// #pragma omp dummyFlush CRITICAL_END
    }
    for (i = 0; i <= 10 - 1; i++) {
        gc = gc + q[i];
    }
    timer_stop(1);
    tm = timer_read(1);
    nit = 0;
    if (24 == 24) {
        double _imopVarPre269;
        double _imopVarPre270;
        int _imopVarPre271;
        double _imopVarPre274;
        double _imopVarPre275;
        _imopVarPre269 = (sx - (-3.247834652034740e3)) / sx;
        _imopVarPre270 = fabs(_imopVarPre269);
        _imopVarPre271 = (_imopVarPre270 <= 1.0e-8);
        if (_imopVarPre271) {
            _imopVarPre274 = (sy - (-6.958407078382297e3)) / sy;
            _imopVarPre275 = fabs(_imopVarPre274);
            _imopVarPre271 = (_imopVarPre275 <= 1.0e-8);
        }
        if (_imopVarPre271) {
            verified = 1;
        }
    } else {
        if (24 == 25) {
            double _imopVarPre287;
            double _imopVarPre288;
            int _imopVarPre289;
            double _imopVarPre292;
            double _imopVarPre293;
            _imopVarPre287 = (sx - (-2.863319731645753e3)) / sx;
            _imopVarPre288 = fabs(_imopVarPre287);
            _imopVarPre289 = (_imopVarPre288 <= 1.0e-8);
            if (_imopVarPre289) {
                _imopVarPre292 = (sy - (-6.320053679109499e3)) / sy;
                _imopVarPre293 = fabs(_imopVarPre292);
                _imopVarPre289 = (_imopVarPre293 <= 1.0e-8);
            }
            if (_imopVarPre289) {
                verified = 1;
            }
        } else {
            if (24 == 28) {
                double _imopVarPre305;
                double _imopVarPre306;
                int _imopVarPre307;
                double _imopVarPre310;
                double _imopVarPre311;
                _imopVarPre305 = (sx - (-4.295875165629892e3)) / sx;
                _imopVarPre306 = fabs(_imopVarPre305);
                _imopVarPre307 = (_imopVarPre306 <= 1.0e-8);
                if (_imopVarPre307) {
                    _imopVarPre310 = (sy - (-1.580732573678431e4)) / sy;
                    _imopVarPre311 = fabs(_imopVarPre310);
                    _imopVarPre307 = (_imopVarPre311 <= 1.0e-8);
                }
                if (_imopVarPre307) {
                    verified = 1;
                }
            } else {
                if (24 == 30) {
                    double _imopVarPre323;
                    double _imopVarPre324;
                    int _imopVarPre325;
                    double _imopVarPre328;
                    double _imopVarPre329;
                    _imopVarPre323 = (sx - 4.033815542441498e4) / sx;
                    _imopVarPre324 = fabs(_imopVarPre323);
                    _imopVarPre325 = (_imopVarPre324 <= 1.0e-8);
                    if (_imopVarPre325) {
                        _imopVarPre328 = (sy - (-2.660669192809235e4)) / sy;
                        _imopVarPre329 = fabs(_imopVarPre328);
                        _imopVarPre325 = (_imopVarPre329 <= 1.0e-8);
                    }
                    if (_imopVarPre325) {
                        verified = 1;
                    }
                } else {
                    if (24 == 32) {
                        double _imopVarPre341;
                        double _imopVarPre342;
                        int _imopVarPre343;
                        double _imopVarPre346;
                        double _imopVarPre347;
                        _imopVarPre341 = (sx - 4.764367927995374e4) / sx;
                        _imopVarPre342 = fabs(_imopVarPre341);
                        _imopVarPre343 = (_imopVarPre342 <= 1.0e-8);
                        if (_imopVarPre343) {
                            _imopVarPre346 = (sy - (-8.084072988043731e4)) / sy;
                            _imopVarPre347 = fabs(_imopVarPre346);
                            _imopVarPre343 = (_imopVarPre347 <= 1.0e-8);
                        }
                        if (_imopVarPre343) {
                            verified = 1;
                        }
                    }
                }
            }
        }
    }
    int _imopVarPre350;
    double _imopVarPre351;
    _imopVarPre350 = 24 + 1;
    _imopVarPre351 = pow(2.0, _imopVarPre350);
    Mops = _imopVarPre351 / tm / 1000000.0;
    printf("EP Benchmark Results: \n" "CPU Time = %10.4f\n" "N = 2^%5d\n" "No. Gaussian Pairs = %15.0f\n" "Sums = %25.15e %25.15e\n" "Counts:\n", tm, 24, gc, sx, sy);
    for (i = 0; i <= 10 - 1; i++) {
        double _imopVarPre353;
        _imopVarPre353 = q[i];
        printf("%3d %15.0f\n", i, _imopVarPre353);
    }
    int _imopVarPre355;
    _imopVarPre355 = 24 + 1;
    c_print_results("EP", 'S', _imopVarPre355, 0, 0, nit, nthreads, tm, Mops, "Random numbers generated", verified, "3.0 structured", "21 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "randdp");
    if (0 == 1) {
        double _imopVarPre357;
        _imopVarPre357 = timer_read(1);
        printf("Total time:     %f", _imopVarPre357);
        double _imopVarPre359;
        _imopVarPre359 = timer_read(2);
        printf("Gaussian pairs: %f", _imopVarPre359);
        double _imopVarPre361;
        _imopVarPre361 = timer_read(3);
        printf("Random numbers: %f", _imopVarPre361);
    }
}
