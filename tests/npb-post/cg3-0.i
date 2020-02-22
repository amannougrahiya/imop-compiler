struct __sFILEX ;
int printf(const char *restrict , ...);
void exit(int );
extern double fabs(double );
extern double pow(double , double );
extern double sqrt(double );
typedef int boolean;
extern double randlc(double *, double );
extern void timer_clear(int );
extern void timer_start(int );
extern void timer_stop(int );
extern double timer_read(int );
extern void c_print_results(char *name, char class , int n1 , int n2 , int n3 , int niter , int nthreads , double t , double mops , char *optype , int passed_verification , char *npbversion , char *compiletime , char *cc , char *clink , char *c_lib , char *c_inc , char *cflags , char *clinkflags , char *rand);
static int naa;
static int nzz;
static int firstrow;
static int lastrow;
static int firstcol;
static int lastcol;
static int colidx[1400 * (7 + 1) * (7 + 1) + 1400 * (7 + 2) + 1];
static int rowstr[1400 + 1 + 1];
static int iv[2 * 1400 + 1 + 1];
static int arow[1400 * (7 + 1) * (7 + 1) + 1400 * (7 + 2) + 1];
static int acol[1400 * (7 + 1) * (7 + 1) + 1400 * (7 + 2) + 1];
static double v[1400 + 1 + 1];
static double aelt[1400 * (7 + 1) * (7 + 1) + 1400 * (7 + 2) + 1];
static double a[1400 * (7 + 1) * (7 + 1) + 1400 * (7 + 2) + 1];
static double x[1400 + 2 + 1];
static double z[1400 + 2 + 1];
static double p[1400 + 2 + 1];
static double q[1400 + 2 + 1];
static double r[1400 + 2 + 1];
static double amult;
static double tran;
static void conj_grad(int colidx[], int rowstr[] , double x[] , double z[] , double a[] , double p[] , double q[] , double r[] , double *rnorm);
static void makea(int n, int nz , double a[] , int colidx[] , int rowstr[] , int nonzer , int firstrow , int lastrow , int firstcol , int lastcol , double rcond , int arow[] , int acol[] , double aelt[] , double v[] , int iv[] , double shift);
static void sparse(double a[], int colidx[] , int rowstr[] , int n , int arow[] , int acol[] , double aelt[] , int firstrow , int lastrow , double x[] , boolean mark[] , int nzloc[] , int nnza);
static void sprnvc(int n, int nz , double v[] , int iv[] , int nzloc[] , int mark[]);
static int icnvrt(double x, int ipwr2);
static void vecset(int n, double v[] , int iv[] , int *nzv , int i , double val);
int main(int argc, char **argv) {
    int i;
    int j;
    int k;
    int it;
    int nthreads = 1;
    double zeta;
    double rnorm;
    double norm_temp11;
    double norm_temp12;
    double t;
    double mflops;
    char class;
    boolean verified;
    double zeta_verify_value;
    double epsilon;
    firstrow = 1;
    lastrow = 1400;
    firstcol = 1;
    lastcol = 1400;
    int _imopVarPre143;
    int _imopVarPre144;
    int _imopVarPre145;
    _imopVarPre143 = 1400 == 1400;
    if (_imopVarPre143) {
        _imopVarPre144 = 7 == 7;
        if (_imopVarPre144) {
            _imopVarPre145 = 15 == 15;
            if (_imopVarPre145) {
                _imopVarPre145 = 10.0 == 10.0;
            }
            _imopVarPre144 = _imopVarPre145;
        }
        _imopVarPre143 = _imopVarPre144;
    }
    if (_imopVarPre143) {
        class = 'S';
        zeta_verify_value = 8.5971775078648;
    } else {
        int _imopVarPre149;
        int _imopVarPre150;
        int _imopVarPre151;
        _imopVarPre149 = 1400 == 7000;
        if (_imopVarPre149) {
            _imopVarPre150 = 7 == 8;
            if (_imopVarPre150) {
                _imopVarPre151 = 15 == 15;
                if (_imopVarPre151) {
                    _imopVarPre151 = 10.0 == 12.0;
                }
                _imopVarPre150 = _imopVarPre151;
            }
            _imopVarPre149 = _imopVarPre150;
        }
        if (_imopVarPre149) {
            class = 'W';
            zeta_verify_value = 10.362595087124;
        } else {
            int _imopVarPre155;
            int _imopVarPre156;
            int _imopVarPre157;
            _imopVarPre155 = 1400 == 14000;
            if (_imopVarPre155) {
                _imopVarPre156 = 7 == 11;
                if (_imopVarPre156) {
                    _imopVarPre157 = 15 == 15;
                    if (_imopVarPre157) {
                        _imopVarPre157 = 10.0 == 20.0;
                    }
                    _imopVarPre156 = _imopVarPre157;
                }
                _imopVarPre155 = _imopVarPre156;
            }
            if (_imopVarPre155) {
                class = 'A';
                zeta_verify_value = 17.130235054029;
            } else {
                int _imopVarPre161;
                int _imopVarPre162;
                int _imopVarPre163;
                _imopVarPre161 = 1400 == 75000;
                if (_imopVarPre161) {
                    _imopVarPre162 = 7 == 13;
                    if (_imopVarPre162) {
                        _imopVarPre163 = 15 == 75;
                        if (_imopVarPre163) {
                            _imopVarPre163 = 10.0 == 60.0;
                        }
                        _imopVarPre162 = _imopVarPre163;
                    }
                    _imopVarPre161 = _imopVarPre162;
                }
                if (_imopVarPre161) {
                    class = 'B';
                    zeta_verify_value = 22.712745482631;
                } else {
                    int _imopVarPre167;
                    int _imopVarPre168;
                    int _imopVarPre169;
                    _imopVarPre167 = 1400 == 150000;
                    if (_imopVarPre167) {
                        _imopVarPre168 = 7 == 15;
                        if (_imopVarPre168) {
                            _imopVarPre169 = 15 == 75;
                            if (_imopVarPre169) {
                                _imopVarPre169 = 10.0 == 110.0;
                            }
                            _imopVarPre168 = _imopVarPre169;
                        }
                        _imopVarPre167 = _imopVarPre168;
                    }
                    if (_imopVarPre167) {
                        class = 'C';
                        zeta_verify_value = 28.973605592845;
                    } else {
                        class = 'U';
                    }
                }
            }
        }
    }
    printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version" " - CG Benchmark\n");
    printf(" Size: %10d\n", 1400);
    printf(" Iterations: %5d\n", 15);
    naa = 1400;
    nzz = 1400 * (7 + 1) * (7 + 1) + 1400 * (7 + 2);
    tran = 314159265.0;
    amult = 1220703125.0;
    double *_imopVarPre171;
    double _imopVarPre172;
    _imopVarPre171 = &tran;
    _imopVarPre172 = randlc(_imopVarPre171, amult);
    zeta = _imopVarPre172;
    makea(naa, nzz, a, colidx, rowstr, 7, firstrow, lastrow, firstcol, lastcol, 1.0e-1, arow, acol, aelt, v, iv, 10.0);
#pragma omp parallel default(shared) private(i, j, k)
    {
#pragma omp for nowait
        for (j = 1; j <= lastrow - firstrow + 1; j++) {
            for (k = rowstr[j]; k < rowstr[j + 1]; k++) {
                colidx[k] = colidx[k] - firstcol + 1;
            }
        }
#pragma omp for nowait
        for (i = 1; i <= 1400 + 1; i++) {
            x[i] = 1.0;
        }
#pragma omp for nowait
        for (j = 1; j <= lastcol - firstcol + 1; j++) {
            q[j] = 0.0;
            z[j] = 0.0;
            r[j] = 0.0;
            p[j] = 0.0;
        }
    }
    zeta = 0.0;
    for (it = 1; it <= 1; it++) {
        double *_imopVarPre174;
        _imopVarPre174 = &rnorm;
        conj_grad(colidx, rowstr, x, z, a, p, q, r, _imopVarPre174);
        norm_temp11 = 0.0;
        norm_temp12 = 0.0;
#pragma omp parallel default(shared) private(j)
        {
#pragma omp for reduction(+:norm_temp11, norm_temp12) nowait
            for (j = 1; j <= lastcol - firstcol + 1; j++) {
                norm_temp11 = norm_temp11 + x[j] * z[j];
                norm_temp12 = norm_temp12 + z[j] * z[j];
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
        double _imopVarPre176;
        _imopVarPre176 = sqrt(norm_temp12);
        norm_temp12 = 1.0 / _imopVarPre176;
#pragma omp parallel default(shared) private(j)
        {
#pragma omp for nowait
            for (j = 1; j <= lastcol - firstcol + 1; j++) {
                x[j] = norm_temp12 * z[j];
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
    }
#pragma omp parallel default(shared) private(i)
    {
#pragma omp for nowait
        for (i = 1; i <= 1400 + 1; i++) {
            x[i] = 1.0;
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    zeta = 0.0;
    timer_clear(1);
    timer_start(1);
    for (it = 1; it <= 15; it++) {
        double *_imopVarPre178;
        _imopVarPre178 = &rnorm;
        conj_grad(colidx, rowstr, x, z, a, p, q, r, _imopVarPre178);
        norm_temp11 = 0.0;
        norm_temp12 = 0.0;
#pragma omp parallel default(shared) private(j)
        {
#pragma omp for reduction(+:norm_temp11, norm_temp12) nowait
            for (j = 1; j <= lastcol - firstcol + 1; j++) {
                norm_temp11 = norm_temp11 + x[j] * z[j];
                norm_temp12 = norm_temp12 + z[j] * z[j];
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
        double _imopVarPre180;
        _imopVarPre180 = sqrt(norm_temp12);
        norm_temp12 = 1.0 / _imopVarPre180;
        zeta = 10.0 + 1.0 / norm_temp11;
        if (it == 1) {
            printf("   iteration           ||r||                 zeta\n");
        }
        printf("    %5d       %20.14e%20.13e\n", it, rnorm, zeta);
#pragma omp parallel default(shared) private(j)
        {
#pragma omp for nowait
            for (j = 1; j <= lastcol - firstcol + 1; j++) {
                x[j] = norm_temp12 * z[j];
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
    }
#pragma omp parallel
    {
    }
    timer_stop(1);
    t = timer_read(1);
    printf(" Benchmark completed\n");
    epsilon = 1.0e-10;
    if (class != 'U') {
        double _imopVarPre183;
        double _imopVarPre184;
        _imopVarPre183 = zeta - zeta_verify_value;
        _imopVarPre184 = fabs(_imopVarPre183);
        if (_imopVarPre184 <= epsilon) {
            verified = 1;
            printf(" VERIFICATION SUCCESSFUL\n");
            printf(" Zeta is    %20.12e\n", zeta);
            double _imopVarPre186;
            _imopVarPre186 = zeta - zeta_verify_value;
            printf(" Error is   %20.12e\n", _imopVarPre186);
        } else {
            verified = 0;
            printf(" VERIFICATION FAILED\n");
            printf(" Zeta                %20.12e\n", zeta);
            printf(" The correct zeta is %20.12e\n", zeta_verify_value);
        }
    } else {
        verified = 0;
        printf(" Problem size unknown\n");
        printf(" NO VERIFICATION PERFORMED\n");
    }
    if (t != 0.0) {
        mflops = (2.0 * 15 * 1400) * (3.0 + (7 * (7 + 1)) + 25.0 * (5.0 + (7 * (7 + 1))) + 3.0) / t / 1000000.0;
    } else {
        mflops = 0.0;
    }
    c_print_results("CG", class, 1400, 0, 0, 15, nthreads, t, mflops, "          floating point", verified, "3.0 structured", "21 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "randdp");
}
static void conj_grad(int colidx[], int rowstr[] , double x[] , double z[] , double a[] , double p[] , double q[] , double r[] , double *rnorm) {
    static int callcount = 0;
    double d;
    double sum;
    double rho;
    double rho0;
    double alpha;
    double beta;
    int j;
    int k;
    int cgit;
    int cgitmax = 25;
    rho = 0.0;
#pragma omp parallel default(shared) private(j, sum) shared(rho, naa)
    {
#pragma omp for nowait
        for (j = 1; j <= naa + 1; j++) {
            q[j] = 0.0;
            z[j] = 0.0;
            r[j] = x[j];
            p[j] = r[j];
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for reduction(+:rho) nowait
        for (j = 1; j <= lastcol - firstcol + 1; j++) {
            rho = rho + r[j] * r[j];
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    for (cgit = 1; cgit <= cgitmax; cgit++) {
        rho0 = rho;
        d = 0.0;
        rho = 0.0;
#pragma omp parallel default(shared) private(j, k, sum, alpha, beta) shared(d, rho0, rho)
        {
#pragma omp for nowait
            for (j = 1; j <= lastrow - firstrow + 1; j++) {
                sum = 0.0;
                for (k = rowstr[j]; k < rowstr[j + 1]; k++) {
                    sum = sum + a[k] * p[colidx[k]];
                }
                q[j] = sum;
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for reduction(+:d) nowait
            for (j = 1; j <= lastcol - firstcol + 1; j++) {
                d = d + p[j] * q[j];
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
            alpha = rho0 / d;
#pragma omp for reduction(+:rho) nowait
            for (j = 1; j <= lastcol - firstcol + 1; j++) {
                z[j] = z[j] + alpha * p[j];
                r[j] = r[j] - alpha * q[j];
                rho = rho + r[j] * r[j];
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
            beta = rho / rho0;
#pragma omp for nowait
            for (j = 1; j <= lastcol - firstcol + 1; j++) {
                p[j] = r[j] + beta * p[j];
            }
            callcount++;
        }
    }
    sum = 0.0;
#pragma omp parallel default(shared) private(j, d) shared(sum)
    {
#pragma omp for nowait
        for (j = 1; j <= lastrow - firstrow + 1; j++) {
            d = 0.0;
            for (k = rowstr[j]; k <= rowstr[j + 1] - 1; k++) {
                d = d + a[k] * z[colidx[k]];
            }
            r[j] = d;
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
#pragma omp for reduction(+:sum) nowait
        for (j = 1; j <= lastcol - firstcol + 1; j++) {
            d = x[j] - r[j];
            sum = sum + d * d;
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    double _imopVarPre187;
    _imopVarPre187 = sqrt(sum);
    (*rnorm) = _imopVarPre187;
}
static void makea(int n, int nz , double a[] , int colidx[] , int rowstr[] , int nonzer , int firstrow , int lastrow , int firstcol , int lastcol , double rcond , int arow[] , int acol[] , double aelt[] , double v[] , int iv[] , double shift) {
    int i;
    int nnza;
    int iouter;
    int ivelt;
    int ivelt1;
    int irow;
    int nzv;
    double size;
    double ratio;
    double scale;
    int jcol;
    size = 1.0;
    double _imopVarPre189;
    double _imopVarPre190;
    _imopVarPre189 = (1.0 / (double) n);
    _imopVarPre190 = pow(rcond, _imopVarPre189);
    ratio = _imopVarPre190;
    nnza = 0;
#pragma omp parallel default(shared) private(i)
    {
#pragma omp for nowait
        for (i = 1; i <= n; i++) {
            colidx[n + i] = 0;
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    for (iouter = 1; iouter <= n; iouter++) {
        nzv = nonzer;
        int *_imopVarPre193;
        int *_imopVarPre194;
        _imopVarPre193 = &(colidx[n]);
        _imopVarPre194 = &(colidx[0]);
        sprnvc(n, nzv, v, iv, _imopVarPre194, _imopVarPre193);
        int *_imopVarPre196;
        _imopVarPre196 = &nzv;
        vecset(n, v, iv, _imopVarPre196, iouter, 0.5);
        for (ivelt = 1; ivelt <= nzv; ivelt++) {
            jcol = iv[ivelt];
            int _imopVarPre198;
            _imopVarPre198 = jcol >= firstcol;
            if (_imopVarPre198) {
                _imopVarPre198 = jcol <= lastcol;
            }
            if (_imopVarPre198) {
                scale = size * v[ivelt];
                for (ivelt1 = 1; ivelt1 <= nzv; ivelt1++) {
                    irow = iv[ivelt1];
                    int _imopVarPre200;
                    _imopVarPre200 = irow >= firstrow;
                    if (_imopVarPre200) {
                        _imopVarPre200 = irow <= lastrow;
                    }
                    if (_imopVarPre200) {
                        nnza = nnza + 1;
                        if (nnza > nz) {
                            printf("Space for matrix elements exceeded in" " makea\n");
                            printf("nnza, nzmax = %d, %d\n", nnza, nz);
                            printf("iouter = %d\n", iouter);
                            exit(1);
                        }
                        acol[nnza] = jcol;
                        arow[nnza] = irow;
                        aelt[nnza] = v[ivelt1] * scale;
                    }
                }
            }
        }
        size = size * ratio;
    }
    for (i = firstrow; i <= lastrow; i++) {
        int _imopVarPre202;
        _imopVarPre202 = i >= firstcol;
        if (_imopVarPre202) {
            _imopVarPre202 = i <= lastcol;
        }
        if (_imopVarPre202) {
            iouter = n + i;
            nnza = nnza + 1;
            if (nnza > nz) {
                printf("Space for matrix elements exceeded in makea\n");
                printf("nnza, nzmax = %d, %d\n", nnza, nz);
                printf("iouter = %d\n", iouter);
                exit(1);
            }
            acol[nnza] = i;
            arow[nnza] = i;
            aelt[nnza] = rcond - shift;
        }
    }
    int *_imopVarPre205;
    int *_imopVarPre206;
    _imopVarPre205 = &(iv[n]);
    _imopVarPre206 = &(iv[0]);
    sparse(a, colidx, rowstr, n, arow, acol, aelt, firstrow, lastrow, v, _imopVarPre206, _imopVarPre205, nnza);
}
static void sparse(double a[], int colidx[] , int rowstr[] , int n , int arow[] , int acol[] , double aelt[] , int firstrow , int lastrow , double x[] , boolean mark[] , int nzloc[] , int nnza) {
    int nrows;
    int i;
    int j;
    int jajp1;
    int nza;
    int k;
    int nzrow;
    double xi;
    nrows = lastrow - firstrow + 1;
#pragma omp parallel default(shared) private(j)
    {
#pragma omp for nowait
        for (j = 1; j <= n; j++) {
            rowstr[j] = 0;
            mark[j] = 0;
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    rowstr[n + 1] = 0;
    for (nza = 1; nza <= nnza; nza++) {
        j = (arow[nza] - firstrow + 1) + 1;
        rowstr[j] = rowstr[j] + 1;
    }
    rowstr[1] = 1;
    for (j = 2; j <= nrows + 1; j++) {
        rowstr[j] = rowstr[j] + rowstr[j - 1];
    }
#pragma omp parallel default(shared) private(k, j)
    {
#pragma omp for nowait
        for (j = 0; j <= nrows - 1; j++) {
            for (k = rowstr[j]; k <= rowstr[j + 1] - 1; k++) {
                a[k] = 0.0;
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    for (nza = 1; nza <= nnza; nza++) {
        j = arow[nza] - firstrow + 1;
        k = rowstr[j];
        a[k] = aelt[nza];
        colidx[k] = acol[nza];
        rowstr[j] = rowstr[j] + 1;
    }
    for (j = nrows; j >= 1; j--) {
        rowstr[j + 1] = rowstr[j];
    }
    rowstr[1] = 1;
    nza = 0;
#pragma omp parallel default(shared) private(i)
    {
#pragma omp for nowait
        for (i = 1; i <= n; i++) {
            x[i] = 0.0;
            mark[i] = 0;
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    jajp1 = rowstr[1];
    for (j = 1; j <= nrows; j++) {
        nzrow = 0;
        for (k = jajp1; k < rowstr[j + 1]; k++) {
            i = colidx[k];
            x[i] = x[i] + a[k];
            int _imopVarPre208;
            _imopVarPre208 = mark[i] == 0;
            if (_imopVarPre208) {
                _imopVarPre208 = x[i] != 0.0;
            }
            if (_imopVarPre208) {
                mark[i] = 1;
                nzrow = nzrow + 1;
                nzloc[nzrow] = i;
            }
        }
        for (k = 1; k <= nzrow; k++) {
            i = nzloc[k];
            mark[i] = 0;
            xi = x[i];
            x[i] = 0.0;
            if (xi != 0.0) {
                nza = nza + 1;
                a[nza] = xi;
                colidx[nza] = i;
            }
        }
        jajp1 = rowstr[j + 1];
        rowstr[j + 1] = nza + rowstr[1];
    }
}
static void sprnvc(int n, int nz , double v[] , int iv[] , int nzloc[] , int mark[]) {
    int nn1;
    int nzrow;
    int nzv;
    int ii;
    int i;
    double vecelt;
    double vecloc;
    nzv = 0;
    nzrow = 0;
    nn1 = 1;
    do {
        nn1 = 2 * nn1;
    } while (nn1 < n);
    while (nzv < nz) {
        double *_imopVarPre210;
        double _imopVarPre211;
        _imopVarPre210 = &tran;
        _imopVarPre211 = randlc(_imopVarPre210, amult);
        vecelt = _imopVarPre211;
        double *_imopVarPre213;
        double _imopVarPre214;
        _imopVarPre213 = &tran;
        _imopVarPre214 = randlc(_imopVarPre213, amult);
        vecloc = _imopVarPre214;
        int _imopVarPre216;
        _imopVarPre216 = icnvrt(vecloc, nn1);
        i = _imopVarPre216 + 1;
        if (i > n) {
            continue;
        }
        if (mark[i] == 0) {
            mark[i] = 1;
            nzrow = nzrow + 1;
            nzloc[nzrow] = i;
            nzv = nzv + 1;
            v[nzv] = vecelt;
            iv[nzv] = i;
        }
    }
    for (ii = 1; ii <= nzrow; ii++) {
        i = nzloc[ii];
        mark[i] = 0;
    }
}
static int icnvrt(double x, int ipwr2) {
    return ((int) (ipwr2 * x));
}
static void vecset(int n, double v[] , int iv[] , int *nzv , int i , double val) {
    int k;
    boolean set;
    set = 0;
    for (k = 1; k <= *nzv; k++) {
        if (iv[k] == i) {
            v[k] = val;
            set = 1;
        }
    }
    if (set == 0) {
        *nzv = *nzv + 1;
        v[*nzv] = val;
        iv[*nzv] = i;
    }
}
