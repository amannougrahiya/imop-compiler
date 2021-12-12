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
extern FILE *__stdinp;
extern FILE *__stdoutp;
extern FILE *__stderrp;
int fflush(FILE *);
int fprintf(FILE *restrict , const char *restrict , ...);
int fscanf(FILE *restrict , const char *restrict , ...);
int printf(const char *restrict , ...);
void exit(int );
void free(void *);
void *malloc(size_t __size);
extern double cos(double );
extern double sin(double );
extern double sqrt(double );
struct options {
    int quiet;
    int help;
} ;
struct excitation {
    double dt;
    double duration;
    double t0;
} ;
struct damping {
    double zeta, consta , constb , freq;
} ;
struct properties {
    double cp;
    double cs;
    double den;
} ;
struct source {
    double dip, strike , rake , fault;
    double xyz[3];
    double epixyz[3];
    int sourcenode;
    int epicenternode;
} ;
struct smallarray_s {
    double first;
    double second;
    double third;
    double pad;
} ;
typedef struct smallarray_s smallarray_t;
struct options options;
FILE *packfile;
int ARCHnodes;
int ARCHpriv;
int ARCHmine;
int ARCHelems;
int ARCHglobalnodes;
int ARCHmesh_dim;
int ARCHglobalelems;
int ARCHcorners;
int ARCHsubdomains;
double ARCHduration;
int ARCHmatrixlen;
int ARCHcholeskylen;
int *ARCHglobalnode;
int *ARCHglobalelem;
double **ARCHcoord;
int **ARCHvertex;
int *ARCHmatrixcol;
int *ARCHmatrixindex;
void arch_init(int argc, char **argv , struct options *op);
void mem_init(void );
void arch_readnodevector(double *v, int n);
void slip(double *u, double *v , double *w);
double distance(double p1[], double p2[]);
void centroid(double x[][3], double xc[]);
double point2fault(double x[]);
void abe_matrix(double vertices[][3], int bv[] , struct properties *prop , double Ce[]);
void element_matrices(double vertices[][3], struct properties *prop , double Ke[][12] , double Me[]);
void vv12x12(double v1[], double v2[] , double u[]);
void mv12x12(double m[][12], double v[]);
void smvp(int nodes, double ( *A )[3][3] , int *Acol , int *Aindex , double ( *v )[3] , double ( *w )[3]);
double phi0(double t);
double phi1(double t);
double phi2(double t);
int *nodekind;
double *nodekindf;
int *source_elms;
double ( *M )[3];
double ( *C )[3];
double ( *M23 )[3];
double ( *C23 )[3];
double ( *V23 )[3];
double ( *vel )[3];
double ( **disp )[3];
double ( *K )[3][3];
smallarray_t **w1;
int **w2;
int my_cpu_id;
int numthreads;
struct source Src;
struct excitation Exc;
struct damping Damp;
int main(int argc, char **argv) {
    int i;
    int j;
    int k;
    int ii;
    int jj;
    int kk;
    int iter;
    int timesteps;
    int disptplus;
    int dispt;
    int disptminus;
    int verticesonbnd;
    int cor[4];
    int bv[4];
    int Step_stride;
    int *temp1;
    int *temp2;
    double time;
    double Ke[12][12];
    double Me[12];
    double Ce[12];
    double Mexv[12];
    double Cexv[12];
    double v[12];
    double alpha;
    double c0[3];
    double d1;
    double d2;
    double *bigdist1;
    double *bigdist2;
    double xc[3];
    double uf[3];
    double vertices[4][3];
    struct properties prop;
    numthreads = 1;
    struct options *_imopVarPre142;
    _imopVarPre142 = &options;
    arch_init(argc, argv, _imopVarPre142);
    mem_init();
    arch_readnodevector(nodekindf, ARCHnodes);
    if (!options.quiet) {
        char *_imopVarPre144;
        _imopVarPre144 = argv[0];
        fprintf(__stderrp, "%s: Beginning simulation.\n", _imopVarPre144);
    }
    Exc.dt = 0.0024;
    Exc.duration = ARCHduration;
    Exc.t0 = 0.6;
    timesteps = Exc.duration / Exc.dt + 1;
    Damp.zeta = 30.0;
    Damp.consta = 0.00533333;
    Damp.constb = 0.06666667;
    Damp.freq = 0.5;
    Src.strike = 111.0 * 3.141592653589793238 / 180.0;
    Src.dip = 44.0 * 3.141592653589793238 / 180.0;
    Src.rake = 70.0 * 3.141592653589793238 / 180.0;
    Src.fault = 29.640788;
    Src.xyz[0] = 32.264153;
    Src.xyz[1] = 23.814432;
    Src.xyz[2] = -11.25;
    Src.epixyz[0] = Src.xyz[0];
    Src.epixyz[1] = Src.xyz[1];
    Src.epixyz[2] = 0.0;
    Src.sourcenode = -1;
    Src.epicenternode = -1;
    uf[0] = uf[1] = uf[2] = 0.0;
    double *_imopVarPre148;
    double *_imopVarPre149;
    double *_imopVarPre150;
    _imopVarPre148 = &uf[2];
    _imopVarPre149 = &uf[1];
    _imopVarPre150 = &uf[0];
    slip(_imopVarPre150, _imopVarPre149, _imopVarPre148);
    uf[0] *= Src.fault;
    uf[1] *= Src.fault;
    uf[2] *= Src.fault;
    prop.cp = 6.0;
    prop.cs = 3.2;
    prop.den = 2.0;
    Step_stride = 30;
    disptplus = 0;
    dispt = 1;
    disptminus = 2;
    fprintf(__stderrp, "\n");
    fprintf(__stderrp, "CASE SUMMARY\n");
    fprintf(__stderrp, "Fault information\n");
    double _imopVarPre152;
    _imopVarPre152 = Src.strike;
    fprintf(__stderrp, "  Orientation:  strike: %f\n", _imopVarPre152);
    double _imopVarPre154;
    _imopVarPre154 = Src.dip;
    fprintf(__stderrp, "                   dip: %f\n", _imopVarPre154);
    double _imopVarPre156;
    _imopVarPre156 = Src.rake;
    fprintf(__stderrp, "                  rake: %f\n", _imopVarPre156);
    double _imopVarPre158;
    _imopVarPre158 = Src.fault;
    fprintf(__stderrp, "           dislocation: %f cm\n", _imopVarPre158);
    double _imopVarPre162;
    double _imopVarPre163;
    double _imopVarPre164;
    _imopVarPre162 = Src.xyz[2];
    _imopVarPre163 = Src.xyz[1];
    _imopVarPre164 = Src.xyz[0];
    fprintf(__stderrp, "Hypocenter: (%f, %f, %f) Km\n", _imopVarPre164, _imopVarPre163, _imopVarPre162);
    fprintf(__stderrp, "Excitation characteristics\n");
    double _imopVarPre166;
    _imopVarPre166 = Exc.dt;
    fprintf(__stderrp, "     Time step: %f sec\n", _imopVarPre166);
    double _imopVarPre168;
    _imopVarPre168 = Exc.duration;
    fprintf(__stderrp, "      Duration: %f sec\n", _imopVarPre168);
    double _imopVarPre170;
    _imopVarPre170 = Exc.t0;
    fprintf(__stderrp, "     Rise time: %f sec\n", _imopVarPre170);
    fprintf(__stderrp, "\n");
    fflush(__stderrp);
    for (i = 0; i < ARCHnodes; i++) {
        nodekind[i] = (int) nodekindf[i];
        if (nodekind[i] == 3) {
            nodekind[i] = 1;
        }
    }
    unsigned long int _imopVarPre173;
    void *_imopVarPre174;
    _imopVarPre173 = sizeof(int) * numthreads;
    _imopVarPre174 = malloc(_imopVarPre173);
    temp1 = (int *) _imopVarPre174;
    if (temp1 == (int *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for temp1\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre177;
    void *_imopVarPre178;
    _imopVarPre177 = sizeof(int) * numthreads;
    _imopVarPre178 = malloc(_imopVarPre177);
    temp2 = (int *) _imopVarPre178;
    if (temp2 == (int *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for temp2\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre181;
    void *_imopVarPre182;
    _imopVarPre181 = sizeof(double) * numthreads;
    _imopVarPre182 = malloc(_imopVarPre181);
    bigdist1 = (double *) _imopVarPre182;
    if (bigdist1 == (double *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for bigdist1\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre185;
    void *_imopVarPre186;
    _imopVarPre185 = sizeof(double) * numthreads;
    _imopVarPre186 = malloc(_imopVarPre185);
    bigdist2 = (double *) _imopVarPre186;
    if (bigdist2 == (double *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for bigdist2\n");
        fflush(__stderrp);
        exit(0);
    }
#pragma omp parallel private(my_cpu_id, d1, d2, c0)
    {
        my_cpu_id = 0;
        temp1[my_cpu_id] = -1;
        temp2[my_cpu_id] = -1;
        bigdist1[my_cpu_id] = 1000000.0;
        bigdist2[my_cpu_id] = 1000000.0;
#pragma omp for nowait
        for (i = 0; i < ARCHnodes; i++) {
            c0[0] = ARCHcoord[i][0];
            c0[1] = ARCHcoord[i][1];
            c0[2] = ARCHcoord[i][2];
            double *_imopVarPre188;
            double _imopVarPre189;
            _imopVarPre188 = Src.xyz;
            _imopVarPre189 = distance(c0, _imopVarPre188);
            d1 = _imopVarPre189;
            double *_imopVarPre191;
            double _imopVarPre192;
            _imopVarPre191 = Src.epixyz;
            _imopVarPre192 = distance(c0, _imopVarPre191);
            d2 = _imopVarPre192;
            if (d1 < bigdist1[my_cpu_id]) {
                bigdist1[my_cpu_id] = d1;
                temp1[my_cpu_id] = i;
            }
            if (d2 < bigdist2[my_cpu_id]) {
                bigdist2[my_cpu_id] = d2;
                temp2[my_cpu_id] = i;
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    d1 = bigdist1[0];
    d2 = bigdist2[0];
    Src.sourcenode = temp1[0];
    Src.epicenternode = temp2[0];
    for (i = 0; i < numthreads; i++) {
        if (bigdist1[i] < d1) {
            d1 = bigdist1[i];
            Src.sourcenode = temp1[i];
        }
        if (bigdist2[i] < d2) {
            d2 = bigdist2[i];
            Src.epicenternode = temp2[i];
        }
    }
    free(bigdist1);
    free(bigdist2);
    free(temp1);
    free(temp2);
    int _imopVarPre194;
    _imopVarPre194 = Src.sourcenode != 0;
    if (_imopVarPre194) {
        _imopVarPre194 = Src.sourcenode <= ARCHmine;
    }
    if (_imopVarPre194) {
        double _imopVarPre199;
        double _imopVarPre200;
        double _imopVarPre201;
        int _imopVarPre202;
        _imopVarPre199 = ARCHcoord[Src.sourcenode][2];
        _imopVarPre200 = ARCHcoord[Src.sourcenode][1];
        _imopVarPre201 = ARCHcoord[Src.sourcenode][0];
        _imopVarPre202 = ARCHglobalnode[Src.sourcenode];
        fprintf(__stderrp, "The source is node %d at (%f  %f  %f)\n", _imopVarPre202, _imopVarPre201, _imopVarPre200, _imopVarPre199);
        fflush(__stderrp);
    }
    int _imopVarPre204;
    _imopVarPre204 = Src.epicenternode != 0;
    if (_imopVarPre204) {
        _imopVarPre204 = Src.epicenternode <= ARCHmine;
    }
    if (_imopVarPre204) {
        double _imopVarPre209;
        double _imopVarPre210;
        double _imopVarPre211;
        int _imopVarPre212;
        _imopVarPre209 = ARCHcoord[Src.epicenternode][2];
        _imopVarPre210 = ARCHcoord[Src.epicenternode][1];
        _imopVarPre211 = ARCHcoord[Src.epicenternode][0];
        _imopVarPre212 = ARCHglobalnode[Src.epicenternode];
        fprintf(__stderrp, "The epicenter is node %d at (%f  %f  %f)\n", _imopVarPre212, _imopVarPre211, _imopVarPre210, _imopVarPre209);
        fflush(__stderrp);
    }
    if (Src.sourcenode != 0) {
#pragma omp parallel private(cor, j, k, xc, vertices)
        {
#pragma omp for nowait
            for (i = 0; i < ARCHelems; i++) {
                for (j = 0; j < 4; j++) {
                    cor[j] = ARCHvertex[i][j];
                }
                int _imopVarPre213;
                int _imopVarPre214;
                int _imopVarPre215;
                _imopVarPre213 = cor[0] == Src.sourcenode;
                if (!_imopVarPre213) {
                    _imopVarPre214 = cor[1] == Src.sourcenode;
                    if (!_imopVarPre214) {
                        _imopVarPre215 = cor[2] == Src.sourcenode;
                        if (!_imopVarPre215) {
                            _imopVarPre215 = cor[3] == Src.sourcenode;
                        }
                        _imopVarPre214 = _imopVarPre215;
                    }
                    _imopVarPre213 = _imopVarPre214;
                }
                if (_imopVarPre213) {
                    for (j = 0; j < 4; j++) {
                        for (k = 0; k < 3; k++) {
                            vertices[j][k] = ARCHcoord[cor[j]][k];
                        }
                    }
                    centroid(vertices, xc);
                    source_elms[i] = 2;
                    double _imopVarPre217;
                    _imopVarPre217 = point2fault(xc);
                    if (_imopVarPre217 >= 0) {
                        source_elms[i] = 3;
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
    }
    for (i = 0; i < ARCHelems; i++) {
        for (j = 0; j < 12; j++) {
            Me[j] = 0.0;
            Ce[j] = 0.0;
            v[j] = 0.0;
            for (k = 0; k < 12; k++) {
                Ke[j][k] = 0.0;
            }
        }
        for (j = 0; j < 4; j++) {
            cor[j] = ARCHvertex[i][j];
        }
        verticesonbnd = 0;
        for (j = 0; j < 4; j++) {
            if (nodekind[cor[j]] != 1) {
                bv[verticesonbnd++] = j;
            }
        }
        if (verticesonbnd == 3) {
            for (j = 0; j < 3; j++) {
                for (k = 0; k < 3; k++) {
                    vertices[j][k] = ARCHcoord[cor[bv[j]]][k];
                }
            }
            struct properties *_imopVarPre219;
            _imopVarPre219 = &prop;
            abe_matrix(vertices, bv, _imopVarPre219, Ce);
        }
        for (j = 0; j < 4; j++) {
            for (k = 0; k < 3; k++) {
                vertices[j][k] = ARCHcoord[cor[j]][k];
            }
        }
        struct properties *_imopVarPre221;
        _imopVarPre221 = &prop;
        element_matrices(vertices, _imopVarPre221, Ke, Me);
        centroid(vertices, xc);
        if (xc[2] < -11.5) {
            alpha = 2.0 * Damp.zeta / 100.0 * (2.0 * 3.141592653589793238 * Damp.freq);
        } else {
            alpha = 4.0 * 3.141592653589793238 * Damp.consta * 0.95 / (prop.cs + Damp.constb);
        }
        for (j = 0; j < 12; j++) {
            Ce[j] = Ce[j] + alpha * Me[j];
        }
        int _imopVarPre222;
        _imopVarPre222 = source_elms[i] == 2;
        if (!_imopVarPre222) {
            _imopVarPre222 = source_elms[i] == 3;
        }
        if (_imopVarPre222) {
            for (j = 0; j < 4; j++) {
                if (cor[j] == Src.sourcenode) {
                    v[3 * j] = uf[0];
                    v[3 * j + 1] = uf[1];
                    v[3 * j + 2] = uf[2];
                } else {
                    v[3 * j] = 0;
                    v[3 * j + 1] = 0;
                    v[3 * j + 2] = 0;
                }
            }
            vv12x12(Me, v, Mexv);
            vv12x12(Ce, v, Cexv);
            mv12x12(Ke, v);
            if (source_elms[i] == 3) {
                for (j = 0; j < 12; j++) {
                    v[j] = -v[j];
                    Mexv[j] = -Mexv[j];
                    Cexv[j] = -Cexv[j];
                }
            }
            for (j = 0; j < 4; j++) {
                V23[ARCHvertex[i][j]][0] += v[j * 3];
                V23[ARCHvertex[i][j]][1] += v[j * 3 + 1];
                V23[ARCHvertex[i][j]][2] += v[j * 3 + 2];
                M23[ARCHvertex[i][j]][0] += Mexv[j * 3];
                M23[ARCHvertex[i][j]][1] += Mexv[j * 3 + 1];
                M23[ARCHvertex[i][j]][2] += Mexv[j * 3 + 2];
                C23[ARCHvertex[i][j]][0] += Cexv[j * 3];
                C23[ARCHvertex[i][j]][1] += Cexv[j * 3 + 1];
                C23[ARCHvertex[i][j]][2] += Cexv[j * 3 + 2];
            }
        }
        for (j = 0; j < 4; j++) {
            M[ARCHvertex[i][j]][0] += Me[j * 3];
            M[ARCHvertex[i][j]][1] += Me[j * 3 + 1];
            M[ARCHvertex[i][j]][2] += Me[j * 3 + 2];
            C[ARCHvertex[i][j]][0] += Ce[j * 3];
            C[ARCHvertex[i][j]][1] += Ce[j * 3 + 1];
            C[ARCHvertex[i][j]][2] += Ce[j * 3 + 2];
            for (k = 0; k < 4; k++) {
                if (ARCHvertex[i][j] <= ARCHvertex[i][k]) {
                    kk = ARCHmatrixindex[ARCHvertex[i][j]];
                    while (ARCHmatrixcol[kk] != ARCHvertex[i][k]) {
                        kk++;
                        if (kk >= ARCHmatrixindex[ARCHvertex[i][k] + 1]) {
                            int _imopVarPre225;
                            int _imopVarPre226;
                            _imopVarPre225 = ARCHvertex[i][k];
                            _imopVarPre226 = ARCHvertex[i][j];
                            printf("K indexing error!!! %d %d\n", _imopVarPre226, _imopVarPre225);
                            exit(1);
                        }
                    }
                    for (ii = 0; ii < 3; ii++) {
                        for (jj = 0; jj < 3; jj++) {
                            K[kk][ii][jj] += Ke[j * 3 + ii][k * 3 + jj];
                        }
                    }
                }
            }
        }
    }
    fprintf(__stderrp, "\n");
    for (iter = 1; iter <= timesteps; iter++) {
#pragma omp parallel private(j)
        {
#pragma omp for nowait
            for (i = 0; i < ARCHnodes; i++) {
                for (j = 0; j < 3; j++) {
                    disp[disptplus][i][j] = 0.0;
                }
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
        double ( *_imopVarPre229 )[3];
        double ( *_imopVarPre230 )[3];
        _imopVarPre229 = disp[disptplus];
        _imopVarPre230 = disp[dispt];
        smvp(ARCHnodes, K, ARCHmatrixcol, ARCHmatrixindex, _imopVarPre230, _imopVarPre229);
        time = iter * Exc.dt;
#pragma omp parallel private(j)
        {
#pragma omp for nowait
            for (i = 0; i < ARCHnodes; i++) {
                for (j = 0; j < 3; j++) {
                    disp[disptplus][i][j] *= -Exc.dt * Exc.dt;
                    double _imopVarPre240;
                    double _imopVarPre241;
                    double _imopVarPre242;
                    _imopVarPre240 = phi2(time);
                    _imopVarPre241 = phi1(time);
                    _imopVarPre242 = phi0(time);
                    disp[disptplus][i][j] += 2.0 * M[i][j] * disp[dispt][i][j] - (M[i][j] - Exc.dt / 2.0 * C[i][j]) * disp[disptminus][i][j] - Exc.dt * Exc.dt * (M23[i][j] * _imopVarPre240 / 2.0 + C23[i][j] * _imopVarPre241 / 2.0 + V23[i][j] * _imopVarPre242 / 2.0);
                    disp[disptplus][i][j] = disp[disptplus][i][j] / (M[i][j] + Exc.dt / 2.0 * C[i][j]);
                    vel[i][j] = 0.5 / Exc.dt * (disp[disptplus][i][j] - disp[disptminus][i][j]);
                }
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
        if (iter % Step_stride == 0) {
            fprintf(__stderrp, "Time step %d\n", iter);
            if (Src.sourcenode <= ARCHmine) {
                double _imopVarPre247;
                double _imopVarPre248;
                double _imopVarPre249;
                int _imopVarPre250;
                _imopVarPre247 = disp[disptplus][Src.sourcenode][2];
                _imopVarPre248 = disp[disptplus][Src.sourcenode][1];
                _imopVarPre249 = disp[disptplus][Src.sourcenode][0];
                _imopVarPre250 = ARCHglobalnode[Src.sourcenode];
                printf("%d: %.2e %.2e %.2e\n", _imopVarPre250, _imopVarPre249, _imopVarPre248, _imopVarPre247);
            }
            if (Src.epicenternode <= ARCHmine) {
                double _imopVarPre255;
                double _imopVarPre256;
                double _imopVarPre257;
                int _imopVarPre258;
                _imopVarPre255 = disp[disptplus][Src.epicenternode][2];
                _imopVarPre256 = disp[disptplus][Src.epicenternode][1];
                _imopVarPre257 = disp[disptplus][Src.epicenternode][0];
                _imopVarPre258 = ARCHglobalnode[Src.epicenternode];
                printf("%d: %.2e %.2e %.2e\n", _imopVarPre258, _imopVarPre257, _imopVarPre256, _imopVarPre255);
            }
            fflush(__stdoutp);
        }
        i = disptminus;
        disptminus = dispt;
        dispt = disptplus;
        disptplus = i;
    }
    for (i = 0; i < numthreads; i++) {
        struct smallarray_s *_imopVarPre260;
        _imopVarPre260 = w1[i];
        free(_imopVarPre260);
    }
    free(w1);
    fprintf(__stderrp, "%d nodes %d elems %d timesteps\n", ARCHglobalnodes, ARCHglobalelems, timesteps);
    fprintf(__stderrp, "\n");
    fflush(__stderrp);
    if (!options.quiet) {
        fprintf(__stderrp, "Done. Terminating the simulation.\n");
    }
    return 0;
}
void shape_ders(double ds[][4]) {
    ds[0][0] = -1;
    ds[1][0] = -1;
    ds[2][0] = -1;
    ds[0][1] = 1;
    ds[1][1] = 0;
    ds[2][1] = 0;
    ds[0][2] = 0;
    ds[1][2] = 1;
    ds[2][2] = 0;
    ds[0][3] = 0;
    ds[1][3] = 0;
    ds[2][3] = 1;
}
void get_Enu(struct properties *prop, double *E , double *nu) {
    double ratio;
    ratio = prop->cp / prop->cs;
    ratio = ratio * ratio;
    *nu = 0.5 * (ratio - 2.0) / (ratio - 1.0);
    *E = 2.0 * prop->den * prop->cs * prop->cs * (1.0 + *nu);
}
void inv_J(double a[][3], double *det) {
    double d1;
    double c[3][3];
    int i;
    int j;
    c[0][0] = a[1][1] * a[2][2] - a[2][1] * a[1][2];
    c[0][1] = a[0][2] * a[2][1] - a[0][1] * a[2][2];
    c[0][2] = a[0][1] * a[1][2] - a[0][2] * a[1][1];
    c[1][0] = a[1][2] * a[2][0] - a[1][0] * a[2][2];
    c[1][1] = a[0][0] * a[2][2] - a[0][2] * a[2][0];
    c[1][2] = a[0][2] * a[1][0] - a[0][0] * a[1][2];
    c[2][0] = a[1][0] * a[2][1] - a[1][1] * a[2][0];
    c[2][1] = a[0][1] * a[2][0] - a[0][0] * a[2][1];
    c[2][2] = a[0][0] * a[1][1] - a[0][1] * a[1][0];
    *det = a[0][0] * c[0][0] + a[0][1] * c[1][0] + a[0][2] * c[2][0];
    d1 = 1.0 / *det;
    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            a[i][j] = c[i][j] * d1;
        }
    }
}
void element_matrices(double vertices[][3], struct properties *prop , double Ke[][12] , double Me[]) {
    double ds[3][4];
    double sum[3];
    double jacobian[3][3];
    double det;
    double volume;
    double E;
    double nu;
    double c1;
    double c2;
    double c3;
    double tt;
    double ts;
    int i;
    int j;
    int m;
    int n;
    int row;
    int column;
    shape_ders(ds);
    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            sum[0] = 0.0;
            for (m = 0; m < 4; m++) {
                sum[0] = sum[0] + ds[i][m] * vertices[m][j];
            }
            jacobian[j][i] = sum[0];
        }
    }
    double *_imopVarPre262;
    _imopVarPre262 = &det;
    inv_J(jacobian, _imopVarPre262);
    for (m = 0; m < 4; m++) {
        for (i = 0; i < 3; i++) {
            sum[i] = 0.0;
            for (j = 0; j < 3; j++) {
                sum[i] = sum[i] + jacobian[j][i] * ds[j][m];
            }
        }
        for (i = 0; i < 3; i++) {
            ds[i][m] = sum[i];
        }
    }
    volume = det / 6.0;
    if (volume <= 0) {
        fprintf(__stderrp, "Warning: Element volume = %f !\n", volume);
    }
    double *_imopVarPre265;
    double *_imopVarPre266;
    _imopVarPre265 = &nu;
    _imopVarPre266 = &E;
    get_Enu(prop, _imopVarPre266, _imopVarPre265);
    c1 = E / (2.0 * (nu + 1.0) * (1.0 - nu * 2.0)) * volume;
    c2 = E * nu / ((nu + 1.0) * (1.0 - nu * 2.0)) * volume;
    c3 = E / ((nu + 1.0) * 2.0) * volume;
    row = -1;
    for (m = 0; m < 4; m++) {
        for (i = 0; i < 3; ++i) {
            ++row;
            column = -1;
            for (n = 0; n <= m; n++) {
                for (j = 0; j < 3; j++) {
                    ++column;
                    ts = ds[i][m] * ds[j][n];
                    if (i == j) {
                        ts = ts * c1;
                        tt = (ds[0][m] * ds[0][n] + ds[1][m] * ds[1][n] + ds[2][m] * ds[2][n]) * c3;
                    } else {
                        if (m == n) {
                            ts = ts * c1;
                            tt = 0;
                        } else {
                            ts = ts * c2;
                            tt = ds[j][m] * ds[i][n] * c3;
                        }
                    }
                    Ke[row][column] = Ke[row][column] + ts + tt;
                }
            }
        }
    }
    tt = prop->den * volume / 4.0;
    for (i = 0; i < 12; i++) {
        Me[i] = tt;
    }
    for (i = 0; i < 12; i++) {
        for (j = 0; j <= i; j++) {
            Ke[j][i] = Ke[i][j];
        }
    }
}
double area_triangle(double vertices[][3]) {
    double a;
    double b;
    double c;
    double x2;
    double y2;
    double z2;
    double p;
    double area;
    x2 = (vertices[0][0] - vertices[1][0]) * (vertices[0][0] - vertices[1][0]);
    y2 = (vertices[0][1] - vertices[1][1]) * (vertices[0][1] - vertices[1][1]);
    z2 = (vertices[0][2] - vertices[1][2]) * (vertices[0][2] - vertices[1][2]);
    double _imopVarPre268;
    double _imopVarPre269;
    _imopVarPre268 = x2 + y2 + z2;
    _imopVarPre269 = sqrt(_imopVarPre268);
    a = _imopVarPre269;
    x2 = (vertices[2][0] - vertices[1][0]) * (vertices[2][0] - vertices[1][0]);
    y2 = (vertices[2][1] - vertices[1][1]) * (vertices[2][1] - vertices[1][1]);
    z2 = (vertices[2][2] - vertices[1][2]) * (vertices[2][2] - vertices[1][2]);
    double _imopVarPre271;
    double _imopVarPre272;
    _imopVarPre271 = x2 + y2 + z2;
    _imopVarPre272 = sqrt(_imopVarPre271);
    b = _imopVarPre272;
    x2 = (vertices[0][0] - vertices[2][0]) * (vertices[0][0] - vertices[2][0]);
    y2 = (vertices[0][1] - vertices[2][1]) * (vertices[0][1] - vertices[2][1]);
    z2 = (vertices[0][2] - vertices[2][2]) * (vertices[0][2] - vertices[2][2]);
    double _imopVarPre274;
    double _imopVarPre275;
    _imopVarPre274 = x2 + y2 + z2;
    _imopVarPre275 = sqrt(_imopVarPre274);
    c = _imopVarPre275;
    p = (a + b + c) / 2.0;
    double _imopVarPre277;
    double _imopVarPre278;
    _imopVarPre277 = p * (p - a) * (p - b) * (p - c);
    _imopVarPre278 = sqrt(_imopVarPre277);
    area = _imopVarPre278;
    return area;
}
void abe_matrix(double vertices[][3], int bv[] , struct properties *prop , double Ce[]) {
    int i;
    int j;
    double area;
    area = area_triangle(vertices);
    for (i = 0; i < 3; i++) {
        j = 3 * bv[i];
        Ce[j] = Ce[j] + prop->cs * prop->den * area / 3.0;
        Ce[j + 1] = Ce[j + 1] + prop->cs * prop->den * area / 3.0;
        Ce[j + 2] = Ce[j + 2] + prop->cp * prop->den * area / 3.0;
    }
}
double phi0(double t) {
    double value;
    if (t <= Exc.t0) {
        double _imopVarPre285;
        double _imopVarPre286;
        _imopVarPre285 = 2.0 * 3.141592653589793238 * t / Exc.t0;
        _imopVarPre286 = sin(_imopVarPre285);
        value = 0.5 / 3.141592653589793238 * (2.0 * 3.141592653589793238 * t / Exc.t0 - _imopVarPre286);
        return value;
    } else {
        return 1.0;
    }
}
double phi1(double t) {
    double value;
    if (t <= Exc.t0) {
        double _imopVarPre293;
        double _imopVarPre294;
        _imopVarPre293 = 2.0 * 3.141592653589793238 * t / Exc.t0;
        _imopVarPre294 = cos(_imopVarPre293);
        value = (1.0 - _imopVarPre294) / Exc.t0;
        return value;
    } else {
        return 0.0;
    }
}
double phi2(double t) {
    double value;
    if (t <= Exc.t0) {
        double _imopVarPre297;
        double _imopVarPre298;
        _imopVarPre297 = 2.0 * 3.141592653589793238 * t / Exc.t0;
        _imopVarPre298 = sin(_imopVarPre297);
        value = 2.0 * 3.141592653589793238 / Exc.t0 / Exc.t0 * _imopVarPre298;
        return value;
    } else {
        return 0.0;
    }
}
void slip(double *u, double *v , double *w) {
    *u = *v = *w = 0.0;
    double _imopVarPre329;
    double _imopVarPre330;
    double _imopVarPre331;
    double _imopVarPre332;
    double _imopVarPre333;
    double _imopVarPre334;
    double _imopVarPre335;
    double _imopVarPre336;
    double _imopVarPre337;
    double _imopVarPre338;
    _imopVarPre329 = Src.rake;
    _imopVarPre330 = cos(_imopVarPre329);
    _imopVarPre331 = Src.strike;
    _imopVarPre332 = sin(_imopVarPre331);
    _imopVarPre333 = Src.rake;
    _imopVarPre334 = sin(_imopVarPre333);
    _imopVarPre335 = Src.strike;
    _imopVarPre336 = cos(_imopVarPre335);
    _imopVarPre337 = Src.dip;
    _imopVarPre338 = cos(_imopVarPre337);
    *u = (_imopVarPre330 * _imopVarPre332 - _imopVarPre334 * _imopVarPre336 * _imopVarPre338);
    double _imopVarPre369;
    double _imopVarPre370;
    double _imopVarPre371;
    double _imopVarPre372;
    double _imopVarPre373;
    double _imopVarPre374;
    double _imopVarPre375;
    double _imopVarPre376;
    double _imopVarPre377;
    double _imopVarPre378;
    _imopVarPre369 = Src.rake;
    _imopVarPre370 = cos(_imopVarPre369);
    _imopVarPre371 = Src.strike;
    _imopVarPre372 = cos(_imopVarPre371);
    _imopVarPre373 = Src.rake;
    _imopVarPre374 = sin(_imopVarPre373);
    _imopVarPre375 = Src.strike;
    _imopVarPre376 = sin(_imopVarPre375);
    _imopVarPre377 = Src.dip;
    _imopVarPre378 = cos(_imopVarPre377);
    *v = (_imopVarPre370 * _imopVarPre372 + _imopVarPre374 * _imopVarPre376 * _imopVarPre378);
    double _imopVarPre383;
    double _imopVarPre384;
    double _imopVarPre385;
    double _imopVarPre386;
    _imopVarPre383 = Src.rake;
    _imopVarPre384 = sin(_imopVarPre383);
    _imopVarPre385 = Src.dip;
    _imopVarPre386 = sin(_imopVarPre385);
    *w = _imopVarPre384 * _imopVarPre386;
}
double distance(double p1[], double p2[]) {
    return ((p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1]) + (p1[2] - p2[2]) * (p1[2] - p2[2]));
}
void centroid(double x[][3], double xc[]) {
    int i;
    for (i = 0; i < 3; i++) {
        xc[i] = (x[0][i] + x[1][i] + x[2][i] + x[3][i]) / 4.0;
    }
}
double point2fault(double x[]) {
    double nx;
    double ny;
    double nz;
    double d0;
    double _imopVarPre391;
    double _imopVarPre392;
    double _imopVarPre393;
    double _imopVarPre394;
    _imopVarPre391 = Src.strike;
    _imopVarPre392 = cos(_imopVarPre391);
    _imopVarPre393 = Src.dip;
    _imopVarPre394 = sin(_imopVarPre393);
    nx = _imopVarPre392 * _imopVarPre394;
    double _imopVarPre399;
    double _imopVarPre400;
    double _imopVarPre401;
    double _imopVarPre402;
    _imopVarPre399 = Src.strike;
    _imopVarPre400 = sin(_imopVarPre399);
    _imopVarPre401 = Src.dip;
    _imopVarPre402 = sin(_imopVarPre401);
    ny = -_imopVarPre400 * _imopVarPre402;
    double _imopVarPre404;
    double _imopVarPre405;
    _imopVarPre404 = Src.dip;
    _imopVarPre405 = cos(_imopVarPre404);
    nz = _imopVarPre405;
    d0 = -(nx * Src.xyz[0] + ny * Src.xyz[1] + nz * Src.xyz[2]);
    return (double) nx * x[0] + ny * x[1] + nz * x[2] + d0;
}
void mv12x12(double m[][12], double v[]) {
    int i;
    int j;
    double u[12];
    for (i = 0; i < 12; i++) {
        u[i] = 0;
        for (j = 0; j < 12; j++) {
            u[i] += m[i][j] * v[j];
        }
    }
    for (i = 0; i < 12; i++) {
        v[i] = u[i];
    }
}
void vv12x12(double v1[], double v2[] , double u[]) {
    int i;
    for (i = 0; i < 12; i++) {
        u[i] = v1[i] * v2[i];
    }
}
void arch_bail() {
    exit(0);
}
void arch_info() {
    printf("\n");
    printf("You are running an Archimedes finite element simulation.\n\n");
    printf("The command syntax is:\n\n");
    printf("quake [-Qh] < packfile\n\n");
    printf("Command line options:\n\n");
    printf("    -Q  Quietly suppress all explanation of what this program is doing\n");
    printf("        unless an error occurs.\n");
    printf("    -h  Print this message and exit.\n");
}
void arch_parsecommandline(int argc, char **argv , struct options *op) {
    int i;
    int j;
    op->quiet = 0;
    op->help = 0;
    for (i = 1; i < argc; i++) {
        if (argv[i][0] == '-') {
            for (j = 1; argv[i][j] != '\0'; j++) {
                if (argv[i][j] == 'Q') {
                    op->quiet = 1;
                }
                int _imopVarPre407;
                _imopVarPre407 = argv[i][j] == 'h';
                if (!_imopVarPre407) {
                    _imopVarPre407 = argv[i][j] == 'H';
                }
                if (_imopVarPre407) {
                    op->help = 1;
                }
            }
        }
    }
    if (op->help) {
        arch_info();
        exit(0);
    }
}
void arch_readnodevector(double *v, int n) {
    int i;
    int type;
    int attributes;
    int *_imopVarPre410;
    int *_imopVarPre411;
    _imopVarPre410 = &attributes;
    _imopVarPre411 = &type;
    fscanf(packfile, "%d %d\n", _imopVarPre411, _imopVarPre410);
    if (type != 2) {
        fprintf(__stderrp, "READNODEVECTOR: unexpected data type\n");
        arch_bail();
    }
    if (attributes != 1) {
        fprintf(__stderrp, "READNODEVECTOR: unexpected number of attributes\n");
        arch_bail();
    }
    for (i = 0; i < n; i++) {
        double *_imopVarPre413;
        _imopVarPre413 = &v[i];
        fscanf(packfile, "%lf", _imopVarPre413);
    }
}
void readpackfile(FILE *packfile, struct options *op) {
    int oldrow;
    int newrow;
    int i;
    int j;
    int temp1;
    int temp2;
    int *_imopVarPre427;
    _imopVarPre427 = &ARCHglobalnodes;
    fscanf(packfile, "%d", _imopVarPre427);
    int *_imopVarPre429;
    _imopVarPre429 = &ARCHmesh_dim;
    fscanf(packfile, "%d", _imopVarPre429);
    int *_imopVarPre431;
    _imopVarPre431 = &ARCHglobalelems;
    fscanf(packfile, "%d", _imopVarPre431);
    int *_imopVarPre433;
    _imopVarPre433 = &ARCHcorners;
    fscanf(packfile, "%d", _imopVarPre433);
    int *_imopVarPre435;
    _imopVarPre435 = &ARCHsubdomains;
    fscanf(packfile, "%d", _imopVarPre435);
    double *_imopVarPre437;
    _imopVarPre437 = &ARCHduration;
    fscanf(packfile, "%lf", _imopVarPre437);
    if (ARCHsubdomains != 1) {
        fprintf(__stderrp, "quake: too many subdomains(%d), rerun slice using -s1\n", ARCHsubdomains);
        arch_bail();
    }
    if (!op->quiet) {
        fprintf(__stderrp, "quake: Reading nodes.\n");
    }
    int *_imopVarPre441;
    int *_imopVarPre442;
    int *_imopVarPre443;
    _imopVarPre441 = &ARCHpriv;
    _imopVarPre442 = &ARCHmine;
    _imopVarPre443 = &ARCHnodes;
    fscanf(packfile, "%d %d %d", _imopVarPre443, _imopVarPre442, _imopVarPre441);
    unsigned long int _imopVarPre446;
    void *_imopVarPre447;
    _imopVarPre446 = ARCHnodes * sizeof(int);
    _imopVarPre447 = malloc(_imopVarPre446);
    ARCHglobalnode = (int *) _imopVarPre447;
    if (ARCHglobalnode == (int *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for ARCHglobalnode\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre450;
    void *_imopVarPre451;
    _imopVarPre450 = ARCHnodes * sizeof(double *);
    _imopVarPre451 = malloc(_imopVarPre450);
    ARCHcoord = (double **) _imopVarPre451;
    for (i = 0; i < ARCHnodes; i++) {
        unsigned long int _imopVarPre454;
        void *_imopVarPre455;
        _imopVarPre454 = 3 * sizeof(double);
        _imopVarPre455 = malloc(_imopVarPre454);
        ARCHcoord[i] = (double *) _imopVarPre455;
    }
    for (i = 0; i < ARCHnodes; i++) {
        int *_imopVarPre457;
        _imopVarPre457 = &ARCHglobalnode[i];
        fscanf(packfile, "%d", _imopVarPre457);
        for (j = 0; j < ARCHmesh_dim; j++) {
            double *_imopVarPre459;
            _imopVarPre459 = &ARCHcoord[i][j];
            fscanf(packfile, "%lf", _imopVarPre459);
        }
    }
    if (!op->quiet) {
        fprintf(__stderrp, "quake: Reading elements.\n");
    }
    int *_imopVarPre461;
    _imopVarPre461 = &ARCHelems;
    fscanf(packfile, "%d", _imopVarPre461);
    unsigned long int _imopVarPre464;
    void *_imopVarPre465;
    _imopVarPre464 = ARCHelems * sizeof(int);
    _imopVarPre465 = malloc(_imopVarPre464);
    ARCHglobalelem = (int *) _imopVarPre465;
    if (ARCHglobalelem == (int *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for ARCHglobalelem\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre468;
    void *_imopVarPre469;
    _imopVarPre468 = ARCHelems * sizeof(int *);
    _imopVarPre469 = malloc(_imopVarPre468);
    ARCHvertex = (int **) _imopVarPre469;
    for (i = 0; i < ARCHelems; i++) {
        unsigned long int _imopVarPre472;
        void *_imopVarPre473;
        _imopVarPre472 = 4 * sizeof(int);
        _imopVarPre473 = malloc(_imopVarPre472);
        ARCHvertex[i] = (int *) _imopVarPre473;
    }
    for (i = 0; i < ARCHelems; i++) {
        int *_imopVarPre475;
        _imopVarPre475 = &ARCHglobalelem[i];
        fscanf(packfile, "%d", _imopVarPre475);
        for (j = 0; j < ARCHcorners; j++) {
            int *_imopVarPre477;
            _imopVarPre477 = &ARCHvertex[i][j];
            fscanf(packfile, "%d", _imopVarPre477);
        }
    }
    if (!op->quiet) {
        fprintf(__stderrp, "quake: Reading sparse matrix structure.\n");
    }
    int *_imopVarPre480;
    int *_imopVarPre481;
    _imopVarPre480 = &ARCHcholeskylen;
    _imopVarPre481 = &ARCHmatrixlen;
    fscanf(packfile, "%d %d", _imopVarPre481, _imopVarPre480);
    unsigned long int _imopVarPre484;
    void *_imopVarPre485;
    _imopVarPre484 = (ARCHmatrixlen + 1) * sizeof(int);
    _imopVarPre485 = malloc(_imopVarPre484);
    ARCHmatrixcol = (int *) _imopVarPre485;
    if (ARCHmatrixcol == (int *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for ARCHmatrixcol\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre488;
    void *_imopVarPre489;
    _imopVarPre488 = (ARCHnodes + 1) * sizeof(int);
    _imopVarPre489 = malloc(_imopVarPre488);
    ARCHmatrixindex = (int *) _imopVarPre489;
    if (ARCHmatrixindex == (int *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for ARCHmatrixindex\n");
        fflush(__stderrp);
        exit(0);
    }
    oldrow = -1;
    for (i = 0; i < ARCHmatrixlen; i++) {
        int *_imopVarPre491;
        _imopVarPre491 = &newrow;
        fscanf(packfile, "%d", _imopVarPre491);
        int *_imopVarPre493;
        _imopVarPre493 = &ARCHmatrixcol[i];
        fscanf(packfile, "%d", _imopVarPre493);
        while (oldrow < newrow) {
            if (oldrow + 1 >= ARCHnodes + 1) {
                int _imopVarPre496;
                int _imopVarPre497;
                _imopVarPre496 = ARCHnodes + 1;
                _imopVarPre497 = oldrow + 1;
                printf("quake: error: (1)idx buffer too small (%d >= %d)\n", _imopVarPre497, _imopVarPre496);
                arch_bail();
            }
            ARCHmatrixindex[++oldrow] = i;
        }
    }
    while (oldrow < ARCHnodes) {
        ARCHmatrixindex[++oldrow] = ARCHmatrixlen;
    }
    int *_imopVarPre500;
    int *_imopVarPre501;
    _imopVarPre500 = &temp2;
    _imopVarPre501 = &temp1;
    fscanf(packfile, "%d %d", _imopVarPre501, _imopVarPre500);
}
void arch_init(int argc, char **argv , struct options *op) {
    arch_parsecommandline(argc, argv, op);
    packfile = __stdinp;
    readpackfile(packfile, op);
}
void smvp(int nodes, double ( *A )[3][3] , int *Acol , int *Aindex , double ( *v )[3] , double ( *w )[3]) {
    int i;
    int j;
    int Anext;
    int Alast;
    int col;
    double sum0;
    double sum1;
    double sum2;
    for (j = 0; j < numthreads; j++) {
#pragma omp parallel private(i)
        {
#pragma omp for nowait
            for (i = 0; i < nodes; i++) {
                w2[j][i] = 0;
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
    }
#pragma omp parallel private(my_cpu_id, i, Anext, Alast, col, sum0, sum1, sum2)
    {
        my_cpu_id = 0;
#pragma omp for nowait
        for (i = 0; i < nodes; i++) {
            Anext = Aindex[i];
            Alast = Aindex[i + 1];
            sum0 = A[Anext][0][0] * v[i][0] + A[Anext][0][1] * v[i][1] + A[Anext][0][2] * v[i][2];
            sum1 = A[Anext][1][0] * v[i][0] + A[Anext][1][1] * v[i][1] + A[Anext][1][2] * v[i][2];
            sum2 = A[Anext][2][0] * v[i][0] + A[Anext][2][1] * v[i][1] + A[Anext][2][2] * v[i][2];
            Anext++;
            while (Anext < Alast) {
                col = Acol[Anext];
                sum0 += A[Anext][0][0] * v[col][0] + A[Anext][0][1] * v[col][1] + A[Anext][0][2] * v[col][2];
                sum1 += A[Anext][1][0] * v[col][0] + A[Anext][1][1] * v[col][1] + A[Anext][1][2] * v[col][2];
                sum2 += A[Anext][2][0] * v[col][0] + A[Anext][2][1] * v[col][1] + A[Anext][2][2] * v[col][2];
                if (w2[my_cpu_id][col] == 0) {
                    w2[my_cpu_id][col] = 1;
                    w1[my_cpu_id][col].first = 0.0;
                    w1[my_cpu_id][col].second = 0.0;
                    w1[my_cpu_id][col].third = 0.0;
                }
                w1[my_cpu_id][col].first += A[Anext][0][0] * v[i][0] + A[Anext][1][0] * v[i][1] + A[Anext][2][0] * v[i][2];
                w1[my_cpu_id][col].second += A[Anext][0][1] * v[i][0] + A[Anext][1][1] * v[i][1] + A[Anext][2][1] * v[i][2];
                w1[my_cpu_id][col].third += A[Anext][0][2] * v[i][0] + A[Anext][1][2] * v[i][1] + A[Anext][2][2] * v[i][2];
                Anext++;
            }
            if (w2[my_cpu_id][i] == 0) {
                w2[my_cpu_id][i] = 1;
                w1[my_cpu_id][i].first = 0.0;
                w1[my_cpu_id][i].second = 0.0;
                w1[my_cpu_id][i].third = 0.0;
            }
            w1[my_cpu_id][i].first += sum0;
            w1[my_cpu_id][i].second += sum1;
            w1[my_cpu_id][i].third += sum2;
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
#pragma omp parallel private(j)
    {
#pragma omp for nowait
        for (i = 0; i < nodes; i++) {
            for (j = 0; j < numthreads; j++) {
                if (w2[j][i]) {
                    w[i][0] += w1[j][i].first;
                    w[i][1] += w1[j][i].second;
                    w[i][2] += w1[j][i].third;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
}
void mem_init() {
    int i;
    int j;
    int k;
    unsigned long int _imopVarPre504;
    void *_imopVarPre505;
    _imopVarPre504 = ARCHnodes * sizeof(double);
    _imopVarPre505 = malloc(_imopVarPre504);
    nodekindf = (double *) _imopVarPre505;
    if (nodekindf == (double *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for nodekindf\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre508;
    void *_imopVarPre509;
    _imopVarPre508 = ARCHnodes * sizeof(int);
    _imopVarPre509 = malloc(_imopVarPre508);
    nodekind = (int *) _imopVarPre509;
    if (nodekind == (int *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for nodekind\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre512;
    void *_imopVarPre513;
    _imopVarPre512 = ARCHelems * sizeof(int);
    _imopVarPre513 = malloc(_imopVarPre512);
    source_elms = (int *) _imopVarPre513;
    if (source_elms == (int *) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for source_elms\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre516;
    void *_imopVarPre517;
    _imopVarPre516 = ARCHnodes * sizeof(double [3]);
    _imopVarPre517 = malloc(_imopVarPre516);
    vel = (double (*)[3]) _imopVarPre517;
    if (vel == (double (*)[3]) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for vel\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre520;
    void *_imopVarPre521;
    _imopVarPre520 = ARCHnodes * sizeof(double [3]);
    _imopVarPre521 = malloc(_imopVarPre520);
    M = (double (*)[3]) _imopVarPre521;
    if (M == (double (*)[3]) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for M\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre524;
    void *_imopVarPre525;
    _imopVarPre524 = ARCHnodes * sizeof(double [3]);
    _imopVarPre525 = malloc(_imopVarPre524);
    C = (double (*)[3]) _imopVarPre525;
    if (C == (double (*)[3]) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for C\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre528;
    void *_imopVarPre529;
    _imopVarPre528 = ARCHnodes * sizeof(double [3]);
    _imopVarPre529 = malloc(_imopVarPre528);
    M23 = (double (*)[3]) _imopVarPre529;
    if (M23 == (double (*)[3]) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for M23\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre532;
    void *_imopVarPre533;
    _imopVarPre532 = ARCHnodes * sizeof(double [3]);
    _imopVarPre533 = malloc(_imopVarPre532);
    C23 = (double (*)[3]) _imopVarPre533;
    if (C23 == (double (*)[3]) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for C23\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre536;
    void *_imopVarPre537;
    _imopVarPre536 = ARCHnodes * sizeof(double [3]);
    _imopVarPre537 = malloc(_imopVarPre536);
    V23 = (double (*)[3]) _imopVarPre537;
    if (V23 == (double (*)[3]) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for V23\n");
        fflush(__stderrp);
        exit(0);
    }
    unsigned long int _imopVarPre540;
    void *_imopVarPre541;
    _imopVarPre540 = numthreads * sizeof(struct smallarray_s *);
    _imopVarPre541 = malloc(_imopVarPre540);
    w1 = (smallarray_t **) _imopVarPre541;
    if (w1 == (smallarray_t **) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for w1\n");
        fflush(__stderrp);
        exit(0);
    }
    for (i = 0; i < numthreads; i++) {
        unsigned long int _imopVarPre544;
        void *_imopVarPre545;
        _imopVarPre544 = ARCHnodes * sizeof(smallarray_t);
        _imopVarPre545 = malloc(_imopVarPre544);
        w1[i] = (smallarray_t *) _imopVarPre545;
        if (w1[i] == (smallarray_t *) ((void *) 0)) {
            fprintf(__stderrp, "malloc failed for w1[%d]\n", i);
            fflush(__stderrp);
            exit(0);
        }
    }
    for (j = 0; j < numthreads; j++) {
#pragma omp parallel private(i)
        {
#pragma omp for nowait
            for (i = 0; i < ARCHnodes; i++) {
                w1[j][i].first = 0.0;
                w1[j][i].second = 0.0;
                w1[j][i].third = 0.0;
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
    }
    unsigned long int _imopVarPre548;
    void *_imopVarPre549;
    _imopVarPre548 = numthreads * sizeof(int *);
    _imopVarPre549 = malloc(_imopVarPre548);
    w2 = (int **) _imopVarPre549;
    if (w2 == (int **) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for w2\n");
        fflush(__stderrp);
        exit(0);
    }
    for (i = 0; i < numthreads; i++) {
        unsigned long int _imopVarPre552;
        void *_imopVarPre553;
        _imopVarPre552 = ARCHnodes * sizeof(int);
        _imopVarPre553 = malloc(_imopVarPre552);
        w2[i] = (int *) _imopVarPre553;
        if (w2[i] == (int *) ((void *) 0)) {
            fprintf(__stderrp, "malloc failed for w2[%d]\n", i);
            fflush(__stderrp);
            exit(0);
        }
    }
    for (j = 0; j < numthreads; j++) {
#pragma omp parallel private(i)
        {
#pragma omp for nowait
            for (i = 0; i < ARCHnodes; i++) {
                w2[j][i] = 0;
            }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
        }
    }
    unsigned long int _imopVarPre556;
    void *_imopVarPre557;
    _imopVarPre556 = 3 * sizeof(double (*)[3]);
    _imopVarPre557 = malloc(_imopVarPre556);
    disp = (double (**)[3]) _imopVarPre557;
    if (disp == (double (**)[3]) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for disp\n");
        fflush(__stderrp);
        exit(0);
    }
    for (i = 0; i < 3; i++) {
        unsigned long int _imopVarPre560;
        void *_imopVarPre561;
        _imopVarPre560 = ARCHnodes * sizeof(double [3]);
        _imopVarPre561 = malloc(_imopVarPre560);
        disp[i] = (double (*)[3]) _imopVarPre561;
        if (disp[i] == (double (*)[3]) ((void *) 0)) {
            fprintf(__stderrp, "malloc failed for disp[%d]\n", i);
            fflush(__stderrp);
            exit(0);
        }
    }
    unsigned long int _imopVarPre564;
    void *_imopVarPre565;
    _imopVarPre564 = ARCHmatrixlen * sizeof(double [3][3]);
    _imopVarPre565 = malloc(_imopVarPre564);
    K = (double (*)[3][3]) _imopVarPre565;
    if (K == (double (*)[3][3]) ((void *) 0)) {
        fprintf(__stderrp, "malloc failed for K\n");
        fflush(__stderrp);
        exit(0);
    }
#pragma omp parallel private(j)
    {
#pragma omp for nowait
        for (i = 0; i < ARCHnodes; i++) {
            nodekind[i] = 0;
            for (j = 0; j < 3; j++) {
                M[i][j] = 0.0;
                C[i][j] = 0.0;
                M23[i][j] = 0.0;
                C23[i][j] = 0.0;
                V23[i][j] = 0.0;
                disp[0][i][j] = 0.0;
                disp[1][i][j] = 0.0;
                disp[2][i][j] = 0.0;
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    for (i = 0; i < ARCHelems; i++) {
        source_elms[i] = 1;
    }
#pragma omp parallel private(j, k)
    {
#pragma omp for nowait
        for (i = 0; i < ARCHmatrixlen; i++) {
            for (j = 0; j < 3; j++) {
                for (k = 0; k < 3; k++) {
                    K[i][j][k] = 0.0;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
}
