typedef long long __int64_t;
typedef long unsigned int __darwin_size_t;
typedef long __darwin_ssize_t;
typedef __int64_t __darwin_off_t;
typedef __darwin_size_t size_t;
typedef __darwin_ssize_t ssize_t;
ssize_t read(int , void * , size_t );
struct fssearchblock ;
struct searchstate ;
int strcmp(const char *__s1, const char *__s2);
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
extern FILE *__stdoutp;
extern FILE *__stderrp;
FILE *fopen(const char *restrict __filename, const char *restrict __mode);
int fprintf(FILE *restrict , const char *restrict , ...);
int fscanf(FILE *restrict , const char *restrict , ...);
int printf(const char *restrict , ...);
int abs(int );
int atoi(const char *);
void exit(int );
void free(void *);
void *malloc(size_t __size);
int rand(void );
void srand(unsigned );
extern char *suboptarg;
extern double fabs(double );
extern double sqrt(double );
#pragma pack(4)
#pragma pack()
struct _filesec ;
int open(const char *, int , ...);
struct lnkNode {
    int F2Neuron;
    double Vigilance;
    int x, y;
    struct lnkNode *next;
} ;
struct lnkNode *head;
int numTraining;
void alloc_td_bu();
int multiplier;
unsigned char **cimage;
double **tds;
double **bus;
double **busp;
int numthreads;
int lwidth;
int lheight;
int width;
int height;
int numinputs;
long i;
int *pass_flag;
int **highx;
int **highy;
double **highest_confidence;
int **set_high;
int **winner;
int **cp;
int o;
int numf1s;
int numf2s;
int numpatterns;
int resonant;
double a;
double b;
double cc;
double d;
double theta;
double delta_t;
double rho;
struct stUn_imopVarPre12 {
    double *I;
    double W;
    double X;
    double V;
    double U;
    double P;
    double Q;
    double R;
} ;
typedef struct stUn_imopVarPre12 f1_neuron;
f1_neuron **f1_layer;
struct stUn_imopVarPre13 {
    double y;
    int reset;
} ;
typedef struct stUn_imopVarPre13 xyz;
xyz **Y;
void add_list_item(int neuron, double vigi , int x , int y) {
    struct lnkNode *temp;
    unsigned long int _imopVarPre144;
    void *_imopVarPre145;
    _imopVarPre144 = sizeof(struct lnkNode);
    _imopVarPre145 = malloc(_imopVarPre144);
    temp = (struct lnkNode *) _imopVarPre145;
    if (temp == ((void *) 0)) {
        printf("malloc problems\n");
        exit(1);
    }
    temp->x = x;
    temp->y = y;
    temp->F2Neuron = neuron;
    temp->Vigilance = vigi;
#pragma omp critical
    {
        temp->next = head;
        head = temp;
    }
}
void sort_list_items() {
    struct lnkNode *nHead;
    struct lnkNode *nCurrent;
    struct lnkNode *previousgreat;
    struct lnkNode *previous;
    struct lnkNode *greatest;
    struct lnkNode *current;
    nHead = ((void *) 0);
    nCurrent = ((void *) 0);
    previousgreat = ((void *) 0);
    previous = ((void *) 0);
    greatest = head;
    current = head;
    while (head != ((void *) 0)) {
        previous = ((void *) 0);
        previousgreat = ((void *) 0);
        current = head;
        greatest = head;
        while (current != ((void *) 0)) {
            if (current->x > greatest->x) {
                greatest = current;
                previousgreat = previous;
            }
            previous = current;
            current = current->next;
        }
        if (previousgreat != ((void *) 0)) {
            previousgreat->next = greatest->next;
        } else {
            head = greatest->next;
        }
        greatest->next = ((void *) 0);
        if (nHead == ((void *) 0)) {
            nHead = greatest;
        } else {
            nCurrent->next = greatest;
        }
        nCurrent = greatest;
    }
    head = nHead;
}
double g(int i) {
    double result;
    if (i != winner[o][0]) {
        result = 0;
    } else {
        if (Y[o][i].y > 0) {
            result = d;
        } else {
            result = 0;
        }
    }
    return result;
}
void find_match(int o) {
    int i;
    winner[o][0] = 0;
    for (i = 0; i < numf2s; i++) {
        if (Y[o][i].y > Y[o][winner[o][0]].y) {
            winner[o][0] = i;
        }
    }
}
double simtest() {
    int j;
    int varNumF1;
    double sum;
    double norm;
    double temp_sum;
    varNumF1 = numf1s;
    sum = norm = 0;
    for (j = 0; j < varNumF1; j++) {
        norm += f1_layer[o][j].P * f1_layer[o][j].P;
    }
    double _imopVarPre147;
    double _imopVarPre148;
    _imopVarPre147 = (double) norm;
    _imopVarPre148 = sqrt(_imopVarPre147);
    norm = _imopVarPre148;
    norm *= cc;
    sum += norm;
    norm = 0;
    for (j = 0; j < varNumF1; j++) {
        temp_sum = f1_layer[o][j].U * f1_layer[o][j].U;
        norm += temp_sum;
    }
    double _imopVarPre150;
    double _imopVarPre151;
    _imopVarPre150 = (double) norm;
    _imopVarPre151 = sqrt(_imopVarPre150);
    norm = _imopVarPre151;
    sum += norm;
    norm = 0;
    for (j = 0; j < varNumF1; j++) {
        f1_layer[o][j].R = (f1_layer[o][j].U + cc * f1_layer[o][j].P) / sum;
        norm += f1_layer[o][j].R * f1_layer[o][j].R;
    }
    double _imopVarPre153;
    double _imopVarPre154;
    _imopVarPre153 = (double) norm;
    _imopVarPre154 = sqrt(_imopVarPre153);
    norm = _imopVarPre154;
    return norm;
}
double simtest2(int o) {
    int j;
    double Su;
    double Sp;
    double numerator;
    double denom;
    double su;
    double sp;
    double su2;
    double sp2;
    double sup;
    double r;
    double e = 0.0000000001;
    su = sp = sup = su2 = sp2 = numerator = 0.0;
    for (j = 0; j < numf1s; j++) {
        su += f1_layer[o][j].U;
        sp += f1_layer[o][j].P;
        su2 += f1_layer[o][j].U * f1_layer[o][j].U;
        sp2 += f1_layer[o][j].P * f1_layer[o][j].P;
        sup += f1_layer[o][j].U * f1_layer[o][j].P;
    }
    Su = ((double) numf1s * su2 - su * su) / ((double) numf1s * ((double) numf1s - 1.0));
    Su = sqrt(Su);
    Sp = ((double) numf1s * sp2 - sp * sp) / ((double) numf1s * ((double) numf1s - 1.0));
    Sp = sqrt(Sp);
    numerator = (double) numf1s * sup - su * sp;
    double _imopVarPre159;
    double _imopVarPre160;
    double _imopVarPre161;
    double _imopVarPre162;
    _imopVarPre159 = (double) numf1s * su2 - su * su;
    _imopVarPre160 = sqrt(_imopVarPre159);
    _imopVarPre161 = (double) numf1s * sp2 - sp * sp;
    _imopVarPre162 = sqrt(_imopVarPre161);
    denom = _imopVarPre160 * _imopVarPre162;
    r = (numerator + e) / (denom + e);
    int _imopVarPre163;
    _imopVarPre163 = (numerator == 0);
    if (!_imopVarPre163) {
        _imopVarPre163 = (denom == 0);
    }
    if (_imopVarPre163) {
        fprintf(__stderrp, "potential div by zero");
        r = 1;
    }
    int _imopVarPre165;
    _imopVarPre165 = (numerator != 0);
    if (_imopVarPre165) {
        _imopVarPre165 = (denom == 0);
    }
    if (_imopVarPre165) {
        fprintf(__stderrp, "div by zero");
        r = 1;
    }
    r *= r;
    return r;
}
void weightadj() {
    int i;
    int j;
    int k;
    double temp;
    double er = 0.000000001;
    i = winner[o][0];
    for (k = 0; k < 1; k++) {
        resonant = 0;
        for (j = 0; j < numf1s; j++) {
            temp = tds[j][i];
            double _imopVarPre167;
            _imopVarPre167 = g(i);
            tds[j][i] += _imopVarPre167 * (f1_layer[o][j].P - tds[j][i]) * delta_t;
            double _imopVarPre170;
            double _imopVarPre171;
            _imopVarPre170 = temp - tds[j][i];
            _imopVarPre171 = fabs(_imopVarPre170);
            if (_imopVarPre171 <= er) {
                resonant = 1;
            }
        }
        for (j = 0; j < numf1s; j++) {
            temp = bus[j][i];
            double _imopVarPre173;
            _imopVarPre173 = g(i);
            bus[j][i] += _imopVarPre173 * (f1_layer[o][j].P - bus[j][i]) * delta_t;
            double _imopVarPre181;
            double _imopVarPre182;
            int _imopVarPre183;
            _imopVarPre181 = temp - bus[j][i];
            _imopVarPre182 = fabs(_imopVarPre181);
            _imopVarPre183 = (_imopVarPre182 <= er);
            if (_imopVarPre183) {
                _imopVarPre183 = resonant;
            }
            if (_imopVarPre183) {
                resonant = 1;
            } else {
                resonant = 0;
            }
        }
    }
}
void init_globs(int mode) {
    int i;
    head = ((void *) 0);
    unsigned long int _imopVarPre186;
    void *_imopVarPre187;
    _imopVarPre186 = sizeof(int) * numthreads;
    _imopVarPre187 = malloc(_imopVarPre186);
    pass_flag = (int *) _imopVarPre187;
    if (pass_flag == ((void *) 0)) {
        printf("malloc error\n");
        exit(1);
    }
    unsigned long int _imopVarPre190;
    void *_imopVarPre191;
    _imopVarPre190 = sizeof(int *) * numthreads;
    _imopVarPre191 = malloc(_imopVarPre190);
    winner = (int **) _imopVarPre191;
    if (winner == ((void *) 0)) {
        printf("malloc error\n");
        exit(1);
    }
    unsigned long int _imopVarPre194;
    void *_imopVarPre195;
    _imopVarPre194 = sizeof(int *) * numthreads;
    _imopVarPre195 = malloc(_imopVarPre194);
    cp = (int **) _imopVarPre195;
    if (cp == ((void *) 0)) {
        printf("malloc error\n");
        exit(1);
    }
    unsigned long int _imopVarPre198;
    void *_imopVarPre199;
    _imopVarPre198 = sizeof(int *) * numthreads;
    _imopVarPre199 = malloc(_imopVarPre198);
    highx = (int **) _imopVarPre199;
    if (highx == ((void *) 0)) {
        printf("malloc error\n");
        exit(1);
    }
    unsigned long int _imopVarPre202;
    void *_imopVarPre203;
    _imopVarPre202 = sizeof(int *) * numthreads;
    _imopVarPre203 = malloc(_imopVarPre202);
    highy = (int **) _imopVarPre203;
    if (highy == ((void *) 0)) {
        printf("malloc error\n");
        exit(1);
    }
    unsigned long int _imopVarPre206;
    void *_imopVarPre207;
    _imopVarPre206 = sizeof(double *) * numthreads;
    _imopVarPre207 = malloc(_imopVarPre206);
    highest_confidence = (double **) _imopVarPre207;
    if (highest_confidence == ((void *) 0)) {
        printf("malloc error\n");
        exit(1);
    }
    unsigned long int _imopVarPre210;
    void *_imopVarPre211;
    _imopVarPre210 = sizeof(int *) * numthreads;
    _imopVarPre211 = malloc(_imopVarPre210);
    set_high = (int **) _imopVarPre211;
    if (set_high == ((void *) 0)) {
        printf("malloc error\n");
        exit(1);
    }
#pragma omp for private(i) nowait
    for (i = 0; i < numthreads; i++) {
        unsigned long int _imopVarPre214;
        void *_imopVarPre215;
        _imopVarPre214 = 4 * sizeof(int);
        _imopVarPre215 = malloc(_imopVarPre214);
        winner[i] = (int *) _imopVarPre215;
        if (winner[i] == ((void *) 0)) {
            printf("malloc error\n");
            exit(1);
        }
        winner[i][0] = 0;
        unsigned long int _imopVarPre218;
        void *_imopVarPre219;
        _imopVarPre218 = 4 * sizeof(int);
        _imopVarPre219 = malloc(_imopVarPre218);
        cp[i] = (int *) _imopVarPre219;
        if (cp[i] == ((void *) 0)) {
            printf("malloc error\n");
            exit(1);
        }
        cp[i][0] = 0;
        unsigned long int _imopVarPre222;
        void *_imopVarPre223;
        _imopVarPre222 = 4 * sizeof(int);
        _imopVarPre223 = malloc(_imopVarPre222);
        highx[i] = (int *) _imopVarPre223;
        if (highx[i] == ((void *) 0)) {
            printf("malloc error\n");
            exit(1);
        }
        highx[i][0] = 0;
        unsigned long int _imopVarPre226;
        void *_imopVarPre227;
        _imopVarPre226 = 4 * sizeof(int);
        _imopVarPre227 = malloc(_imopVarPre226);
        highy[i] = (int *) _imopVarPre227;
        if (highy[i] == ((void *) 0)) {
            printf("malloc error\n");
            exit(1);
        }
        highy[i][0] = 0;
        unsigned long int _imopVarPre230;
        void *_imopVarPre231;
        _imopVarPre230 = 4 * sizeof(double);
        _imopVarPre231 = malloc(_imopVarPre230);
        highest_confidence[i] = (double *) _imopVarPre231;
        if (highest_confidence == ((void *) 0)) {
            printf("malloc error\n");
            exit(1);
        }
        highest_confidence[i][0] = 0.0;
        unsigned long int _imopVarPre234;
        void *_imopVarPre235;
        _imopVarPre234 = 4 * sizeof(int);
        _imopVarPre235 = malloc(_imopVarPre234);
        set_high[i] = (int *) _imopVarPre235;
        if (set_high == ((void *) 0)) {
            printf("malloc error\n");
            exit(1);
        }
        set_high[i][0] = 0;
    }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    if (mode == 0) {
        a = 255;
        b = 0.0;
        cc = 0.11;
        d = 0.9;
        double _imopVarPre238;
        double _imopVarPre239;
        _imopVarPre238 = (double) numf1s;
        _imopVarPre239 = sqrt(_imopVarPre238);
        theta = 1 / _imopVarPre239;
        delta_t = 0.1;
        rho = 0.70;
    } else {
        a = 255;
        b = 10.0;
        cc = 0.11;
        d = 0.9;
        double _imopVarPre242;
        double _imopVarPre243;
        _imopVarPre242 = (double) numf1s;
        _imopVarPre243 = sqrt(_imopVarPre242);
        theta = 1 / _imopVarPre243;
        delta_t = 0.7;
        rho = 0.95;
    }
}
void init_net() {
    int i;
    int j;
    unsigned long int _imopVarPre246;
    void *_imopVarPre247;
    _imopVarPre246 = numthreads * sizeof(f1_neuron *);
    _imopVarPre247 = malloc(_imopVarPre246);
    f1_layer = (f1_neuron **) _imopVarPre247;
    if (f1_layer == ((void *) 0)) {
        fprintf(__stderrp, "malloc error in init_net\n");
        exit(1);
    }
#pragma omp parallel private(i)
    {
#pragma omp for nowait
        for (i = 0; i < numthreads; i++) {
            unsigned long int _imopVarPre250;
            void *_imopVarPre251;
            _imopVarPre250 = numf1s * sizeof(f1_neuron);
            _imopVarPre251 = malloc(_imopVarPre250);
            f1_layer[i] = (f1_neuron *) _imopVarPre251;
            if (f1_layer[i] == ((void *) 0)) {
                fprintf(__stderrp, "malloc error in init_net\n");
                exit(1);
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
#pragma omp parallel private(i, j)
    {
#pragma omp for nowait
        for (j = 0; j < numthreads; j++) {
            for (i = 0; i < numf1s; i++) {
                unsigned long int _imopVarPre254;
                void *_imopVarPre255;
                _imopVarPre254 = 2 * sizeof(double);
                _imopVarPre255 = malloc(_imopVarPre254);
                f1_layer[j][i].I = (double *) _imopVarPre255;
                if (f1_layer[j][i].I == ((void *) 0)) {
                    fprintf(__stderrp, "malloc error in init_net\n");
                    exit(1);
                }
                f1_layer[j][i].W = 0.0;
                f1_layer[j][i].X = 0.0;
                f1_layer[j][i].V = 0.0;
                f1_layer[j][i].U = 0.0;
                f1_layer[j][i].P = 0.0;
                f1_layer[j][i].Q = 0.0;
                f1_layer[j][i].R = 0.0;
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    unsigned long int _imopVarPre258;
    void *_imopVarPre259;
    _imopVarPre258 = numthreads * sizeof(xyz *);
    _imopVarPre259 = malloc(_imopVarPre258);
    Y = (xyz **) _imopVarPre259;
    if (Y == ((void *) 0)) {
        fprintf(__stdoutp, "Malloc error for Y\n");
        exit(1);
    }
#pragma omp parallel private(i)
    {
#pragma omp for nowait
        for (i = 0; i < numthreads; i++) {
            unsigned long int _imopVarPre262;
            void *_imopVarPre263;
            _imopVarPre262 = numf2s * sizeof(xyz);
            _imopVarPre263 = malloc(_imopVarPre262);
            Y[i] = (xyz *) _imopVarPre263;
            if (Y[i] == ((void *) 0)) {
                fprintf(__stderrp, "malloc error in init_net\n");
            }
            Y[i][0].y = 0.0;
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
}
void reset_nodes() {
    int i;
    int varNumF1;
    int o;
    o = 0;
    varNumF1 = numf1s;
    for (i = 0; i < varNumF1; i++) {
        f1_layer[o][i].W = 0.0;
        f1_layer[o][i].X = 0.0;
        f1_layer[o][i].V = 0.0;
        f1_layer[o][i].U = 0.0;
        f1_layer[o][i].P = 0.0;
        f1_layer[o][i].Q = 0.0;
        f1_layer[o][i].R = 0.0;
    }
    for (i = 0; i < numf2s; i++) {
        Y[o][i].y = 0.0;
        Y[o][i].reset = 0;
    }
    winner[o][0] = 0;
    resonant = 0;
}
void reset_nodes2() {
    int i;
    int varNumF1;
    int o;
    o = 0;
    varNumF1 = numf1s;
    for (i = 0; i < varNumF1; i++) {
        f1_layer[o][i].W = 0.0;
        f1_layer[o][i].X = 0.0;
        f1_layer[o][i].V = 0.0;
        f1_layer[o][i].U = 0.0;
        f1_layer[o][i].P = 0.0;
        f1_layer[o][i].Q = 0.0;
        f1_layer[o][i].R = 0.0;
    }
    for (i = 0; i < numf2s; i++) {
        Y[o][i].y = 0.0;
    }
    winner[o][0] = 0;
    resonant = 0;
}
void reset_unnecessary_f2s() {
    int i;
    int o;
    o = 0;
    for (i = numpatterns; i < numf2s; i++) {
        Y[o][i].reset = 1;
    }
}
void compute_train_match(int o, int *f1res , int *spot) {
    int varNumF1;
    int varNumF2;
    int varNumCp;
    double varNumA;
    double varNumB;
    double varNumD;
    double varNumTheta;
    double oldTnorm;
    int ti;
    int tj;
    int tresult;
    double tnorm;
    double xr;
    double qr;
    double tsum;
    double ttemp;
    varNumF1 = numf1s;
    varNumF2 = numf2s;
    varNumCp = cp[o][0];
    varNumA = a;
    varNumB = b;
    varNumD = d;
    varNumTheta = theta;
    tnorm = 0;
    for (ti = 0; ti < varNumF1; ti++) {
        f1_layer[o][ti].W = f1_layer[o][ti].I[varNumCp] + varNumA * (f1_layer[o][ti].U);
        tnorm += f1_layer[o][ti].W * f1_layer[o][ti].W;
    }
    double _imopVarPre277;
    double _imopVarPre278;
    _imopVarPre277 = (double) tnorm;
    _imopVarPre278 = sqrt(_imopVarPre277);
    tnorm = _imopVarPre278;
    oldTnorm = tnorm;
    tnorm = 0;
    for (ti = 0; ti < varNumF1; ti++) {
        f1_layer[o][ti].X = f1_layer[o][ti].W / oldTnorm;
        if (f1_layer[o][ti].X < varNumTheta) {
            xr = 0;
        } else {
            xr = f1_layer[o][ti].X;
        }
        if (f1_layer[o][ti].Q < varNumTheta) {
            qr = 0;
        } else {
            qr = f1_layer[o][ti].Q;
        }
        f1_layer[o][ti].V = xr + varNumB * qr;
        tnorm += f1_layer[o][ti].V * f1_layer[o][ti].V;
    }
    double _imopVarPre280;
    double _imopVarPre281;
    _imopVarPre280 = (double) tnorm;
    _imopVarPre281 = sqrt(_imopVarPre280);
    tnorm = _imopVarPre281;
    oldTnorm = tnorm;
    tnorm = 0;
    tsum = 0;
    tresult = 1;
    for (ti = 0; ti < varNumF1; ti++) {
        f1_layer[o][ti].U = f1_layer[o][ti].V / oldTnorm;
        tsum = 0;
        ttemp = f1_layer[o][ti].P;
        for (tj = *spot; tj < numpatterns; tj++) {
            int _imopVarPre283;
            _imopVarPre283 = (tj == winner[o][0]);
            if (_imopVarPre283) {
                _imopVarPre283 = (Y[o][tj].y > 0);
            }
            if (_imopVarPre283) {
                tsum += tds[ti][tj] * varNumD;
            }
        }
        f1_layer[o][ti].P = f1_layer[o][ti].U + tsum;
        tnorm += f1_layer[o][ti].P * f1_layer[o][ti].P;
        double _imopVarPre286;
        double _imopVarPre287;
        _imopVarPre286 = ttemp - f1_layer[o][ti].P;
        _imopVarPre287 = fabs(_imopVarPre286);
        if (_imopVarPre287 < 0.000001) {
            tresult = 0;
        }
    }
    *f1res = tresult;
    double _imopVarPre289;
    double _imopVarPre290;
    _imopVarPre289 = (double) tnorm;
    _imopVarPre290 = sqrt(_imopVarPre289);
    tnorm = _imopVarPre290;
    for (tj = 0; tj < varNumF1; tj++) {
        f1_layer[o][tj].Q = f1_layer[o][tj].P;
    }
    for (tj = *spot; tj < numpatterns; tj++) {
        Y[o][tj].y = 0;
        if (!Y[o][tj].reset) {
            for (ti = 0; ti < varNumF1; ti++) {
                Y[o][tj].y += f1_layer[o][ti].P * bus[ti][tj];
            }
        }
    }
    winner[o][0] = 0;
    for (ti = *spot; ti < numpatterns; ti++) {
        if (Y[o][ti].y > Y[o][winner[o][0]].y) {
            winner[o][0] = ti;
        }
    }
}
void compute_values_match(int o, int *f1res , int *spot , double **busp) {
    int varNumF1;
    int varNumF2;
    int varNumCp;
    double varNumA;
    double varNumB;
    double varNumD;
    double varNumTheta;
    double oldTnorm;
    int ti;
    int tj;
    int tresult;
    double tnorm;
    double xr;
    double qr;
    double tsum;
    double ttemp;
    varNumF1 = numf1s;
    varNumF2 = numf2s;
    varNumCp = cp[o][0];
    varNumA = a;
    varNumB = b;
    varNumD = d;
    varNumTheta = theta;
    tnorm = 0;
    for (ti = 0; ti < varNumF1; ti++) {
        f1_layer[o][ti].W = f1_layer[o][ti].I[varNumCp] + varNumA * (f1_layer[o][ti].U);
        tnorm += f1_layer[o][ti].W * f1_layer[o][ti].W;
    }
    double _imopVarPre292;
    double _imopVarPre293;
    _imopVarPre292 = (double) tnorm;
    _imopVarPre293 = sqrt(_imopVarPre292);
    tnorm = _imopVarPre293;
    oldTnorm = tnorm;
    tnorm = 0;
    for (ti = 0; ti < varNumF1; ti++) {
        f1_layer[o][ti].X = f1_layer[o][ti].W / oldTnorm;
        if (f1_layer[o][ti].X < varNumTheta) {
            xr = 0;
        } else {
            xr = f1_layer[o][ti].X;
        }
        if (f1_layer[o][ti].Q < varNumTheta) {
            qr = 0;
        } else {
            qr = f1_layer[o][ti].Q;
        }
        f1_layer[o][ti].V = xr + varNumB * qr;
        tnorm += f1_layer[o][ti].V * f1_layer[o][ti].V;
    }
    double _imopVarPre295;
    double _imopVarPre296;
    _imopVarPre295 = (double) tnorm;
    _imopVarPre296 = sqrt(_imopVarPre295);
    tnorm = _imopVarPre296;
    oldTnorm = tnorm;
    tnorm = 0;
    tsum = 0;
    tresult = 1;
    for (ti = 0; ti < varNumF1; ti++) {
        f1_layer[o][ti].U = f1_layer[o][ti].V / oldTnorm;
        tsum = 0;
        ttemp = f1_layer[o][ti].P;
        for (tj = *spot; tj < varNumF2; tj++) {
            int _imopVarPre298;
            _imopVarPre298 = (tj == winner[o][0]);
            if (_imopVarPre298) {
                _imopVarPre298 = (Y[o][tj].y > 0);
            }
            if (_imopVarPre298) {
                tsum += tds[ti][tj] * varNumD;
            }
        }
        f1_layer[o][ti].P = f1_layer[o][ti].U + tsum;
        tnorm += f1_layer[o][ti].P * f1_layer[o][ti].P;
        double _imopVarPre301;
        double _imopVarPre302;
        _imopVarPre301 = ttemp - f1_layer[o][ti].P;
        _imopVarPre302 = fabs(_imopVarPre301);
        if (_imopVarPre302 < 0.000001) {
            tresult = 0;
        }
    }
    *f1res = tresult;
    double _imopVarPre304;
    double _imopVarPre305;
    _imopVarPre304 = (double) tnorm;
    _imopVarPre305 = sqrt(_imopVarPre304);
    tnorm = _imopVarPre305;
    for (tj = 0; tj < varNumF1; tj++) {
        f1_layer[o][tj].Q = f1_layer[o][tj].P;
    }
    for (tj = *spot; tj < varNumF2; tj++) {
        Y[o][tj].y = 0;
    }
    for (ti = 0; ti < varNumF1; ti++) {
        for (tj = *spot; tj < varNumF2; tj++) {
            if (!Y[o][tj].reset) {
                Y[o][tj].y += f1_layer[o][ti].P * busp[ti][tj];
            }
        }
    }
    winner[o][0] = 0;
    for (ti = *spot; ti < varNumF2; ti++) {
        if (Y[o][ti].y > Y[o][winner[o][0]].y) {
            winner[o][0] = ti;
        }
    }
}
void train_match(int spot) {
    int j;
    int matched;
    int f1res;
    int mt;
    char matchtest;
    double match_confidence;
    int o;
    o = 0;
    numTraining = numTraining + 1;
    f1res = 0;
    reset_nodes();
    cp[o][0] = spot;
    matched = 0;
    reset_unnecessary_f2s();
    while (!matched) {
        f1res = 0;
        int _imopVarPre307;
        j = 0;
        _imopVarPre307 = j < 9;
        if (_imopVarPre307) {
            _imopVarPre307 = !f1res;
        }
        for (; _imopVarPre307; ) {
            int *_imopVarPre310;
            int *_imopVarPre311;
            _imopVarPre310 = &spot;
            _imopVarPre311 = &f1res;
            compute_train_match(o, _imopVarPre311, _imopVarPre310);
            j++;
            _imopVarPre307 = j < 9;
            if (_imopVarPre307) {
                _imopVarPre307 = !f1res;
            }
        }
        match_confidence = simtest();
        if (match_confidence > rho) {
            weightadj();
            matched = 1;
        } else {
            Y[o][winner[o][0]].y = 0;
            Y[o][winner[o][0]].reset = 1;
            matchtest = 0;
            for (mt = spot; mt < numf2s; mt++) {
                if (Y[o][mt].reset == 0) {
                    matchtest = 1;
                }
            }
            if (matchtest) {
                find_match(o);
            } else {
                matched = 1;
            }
        }
    }
}
int match(int o, int xcoor , int ycoor , double *mcp , double **busp) {
    int j;
    int matched;
    int f1res;
    int mt;
    long c;
    char matchtest;
    double match_confidence;
    int spot;
    int ret;
    int mprint;
    c = 0;
    ret = 0;
    f1res = 0;
    spot = 0;
    cp[o][0] = 0;
    reset_nodes();
    matched = 0;
    mprint = 1;
    while (!matched) {
        if (c++ > 3) {
            break;
        }
        reset_nodes2();
        f1res = 0;
        int _imopVarPre313;
        j = 0;
        _imopVarPre313 = j < 9;
        if (_imopVarPre313) {
            _imopVarPre313 = !f1res;
        }
        for (; _imopVarPre313; ) {
            int *_imopVarPre316;
            int *_imopVarPre317;
            _imopVarPre316 = &spot;
            _imopVarPre317 = &f1res;
            compute_values_match(o, _imopVarPre317, _imopVarPre316, busp);
            j++;
            _imopVarPre313 = j < 9;
            if (_imopVarPre313) {
                _imopVarPre313 = !f1res;
            }
        }
        match_confidence = simtest2(o);
        if (mprint == 1) {
            *mcp = match_confidence;
            mprint = 0;
        }
        if (match_confidence > rho) {
            if (winner[o][0] != numf2s - 1) {
                pass_flag[o] = 1;
                ret = 1;
                if (match_confidence >= 0.99) {
                    int _imopVarPre319;
                    _imopVarPre319 = winner[o][0];
                    add_list_item(_imopVarPre319, match_confidence, xcoor, ycoor);
                }
                if (match_confidence > highest_confidence[o][winner[o][0]]) {
                    highest_confidence[o][winner[o][0]] = match_confidence;
                    set_high[o][winner[o][0]] = 1;
                }
            }
            matched = 1;
        } else {
            Y[o][winner[o][0]].y = 0;
            Y[o][winner[o][0]].reset = 1;
            matchtest = 0;
            for (mt = 0; mt < numf2s; mt++) {
                if (Y[o][mt].reset == 0) {
                    matchtest = 1;
                }
            }
            if (matchtest) {
                find_match(o);
            } else {
                matched = 1;
            }
        }
    }
    return ret;
}
void loadimage(char *input_file) {
    int i;
    int j;
    int r;
    int c;
    int fd;
    int cimgwidth;
    int cimgheight;
    char buffer[64];
    char *superbuffer;
    int _imopVarPre321;
    _imopVarPre321 = open(input_file, 0x0000);
    if ((fd = _imopVarPre321) == -1) {
        fprintf(__stderrp, "Error opening %s\n", input_file);
        exit(1);
    }
    read(fd, buffer, 8);
    read(fd, buffer, 4);
    for (i = 0; i < 4; i++) {
        if (buffer[i] != ' ') {
            width = width * 10 + buffer[i] - '0';
        }
    }
    read(fd, buffer, 4);
    for (i = 0; i < 4; i++) {
        if (buffer[i] != ' ') {
            height = height * 10 + buffer[i] - '0';
        }
    }
    cimgwidth = width * multiplier;
    cimgheight = height * multiplier;
    unsigned long int _imopVarPre324;
    void *_imopVarPre325;
    _imopVarPre324 = width * height * sizeof(char);
    _imopVarPre325 = malloc(_imopVarPre324);
    superbuffer = (char *) _imopVarPre325;
    if (superbuffer == ((void *) 0)) {
        fprintf(__stderrp, "Problems with malloc in loadimage()\n");
        exit(1);
    }
    unsigned long int _imopVarPre328;
    void *_imopVarPre329;
    _imopVarPre328 = sizeof(unsigned char *) * cimgheight;
    _imopVarPre329 = malloc(_imopVarPre328);
    cimage = (unsigned char **) _imopVarPre329;
    if (cimage == ((void *) 0)) {
        fprintf(__stderrp, "Problems with malloc in loadimage()\n");
        exit(1);
    }
    for (i = 0; i < cimgheight; i++) {
        unsigned long int _imopVarPre332;
        void *_imopVarPre333;
        _imopVarPre332 = cimgwidth * sizeof(unsigned char);
        _imopVarPre333 = malloc(_imopVarPre332);
        cimage[i] = (unsigned char *) _imopVarPre333;
        if (cimage[i] == ((void *) 0)) {
            fprintf(__stderrp, "Problems with malloc in loadimage()\n");
            exit(1);
        }
    }
    int _imopVarPre335;
    _imopVarPre335 = width * height;
    read(fd, superbuffer, _imopVarPre335);
    for (i = 0; i < cimgheight; i += height) {
        for (j = 0; j < cimgwidth; j += width) {
            for (r = 0; r < height; r++) {
                for (c = 0; c < width; c++) {
                    cimage[i + r][j + c] = superbuffer[r * width + c];
                }
            }
        }
    }
    width = cimgwidth;
    height = cimgheight;
}
void load_weights(char *weightfile) {
    double a;
    long i;
    long j;
    FILE *inp;
    struct __sFILE *_imopVarPre337;
    _imopVarPre337 = fopen(weightfile, "r");
    if ((inp = _imopVarPre337) == ((void *) 0)) {
        fprintf(__stderrp, "Unable to open %s\n", weightfile);
        exit(1);
    }
    printf("made it to load_weights\n");
    int *_imopVarPre340;
    int *_imopVarPre341;
    _imopVarPre340 = &lheight;
    _imopVarPre341 = &lwidth;
    fscanf(inp, "%d %d", _imopVarPre341, _imopVarPre340);
    numf1s = numinputs = lwidth * lheight;
    numf2s = numpatterns + 1;
    alloc_td_bu();
    j = 0;
    for (i = 0; i < numf1s; i++) {
        double *_imopVarPre343;
        _imopVarPre343 = &a;
        fscanf(inp, "%le", _imopVarPre343);
        bus[i][j] = tds[i][j] = a;
    }
}
void alloc_td_bu() {
    unsigned long int _imopVarPre346;
    void *_imopVarPre347;
    _imopVarPre346 = numf1s * sizeof(double *);
    _imopVarPre347 = malloc(_imopVarPre346);
    bus = (double **) _imopVarPre347;
    unsigned long int _imopVarPre350;
    void *_imopVarPre351;
    _imopVarPre350 = numf1s * sizeof(double *);
    _imopVarPre351 = malloc(_imopVarPre350);
    tds = (double **) _imopVarPre351;
    int _imopVarPre352;
    _imopVarPre352 = (bus == ((void *) 0));
    if (!_imopVarPre352) {
        _imopVarPre352 = (tds == ((void *) 0));
    }
    if (_imopVarPre352) {
        fprintf(__stderrp, "Malloc problem in load_weights\n");
        exit(1);
    }
    for (i = 0; i < numf1s; i++) {
        unsigned long int _imopVarPre355;
        void *_imopVarPre356;
        _imopVarPre355 = numf2s * sizeof(double);
        _imopVarPre356 = malloc(_imopVarPre355);
        bus[i] = (double *) _imopVarPre356;
        unsigned long int _imopVarPre359;
        void *_imopVarPre360;
        _imopVarPre359 = numf2s * sizeof(double);
        _imopVarPre360 = malloc(_imopVarPre359);
        tds[i] = (double *) _imopVarPre360;
        int _imopVarPre361;
        _imopVarPre361 = (bus[i] == ((void *) 0));
        if (!_imopVarPre361) {
            _imopVarPre361 = (tds[i] == ((void *) 0));
        }
        if (_imopVarPre361) {
            fprintf(__stderrp, "Malloc problem in load_weights, i=%d\n", i);
            exit(1);
        }
    }
}
void init_td(int start) {
    int i;
    int j;
    for (i = 0; i < numf1s; i++) {
        for (j = start; j < numf2s; j++) {
            tds[i][j] = 0.0;
        }
    }
}
void init_bu(int start) {
    int i;
    int j;
    for (i = 0; i < numf1s; i++) {
        for (j = start; j < numf2s; j++) {
            double _imopVarPre364;
            double _imopVarPre365;
            _imopVarPre364 = (double) numf1s;
            _imopVarPre365 = sqrt(_imopVarPre364);
            bus[i][j] = 1 / (1.0 - d) / _imopVarPre365;
        }
    }
}
void load_train(char *trainfile, int mode , int objects) {
    int i;
    int fd;
    char buffer[64];
    char *superbuffer;
    unsigned char t;
    int spot;
    if (mode == 1) {
        spot = 0;
    } else {
        spot = 1;
    }
    int _imopVarPre367;
    _imopVarPre367 = open(trainfile, 0x0000);
    if ((fd = _imopVarPre367) == -1) {
        fprintf(__stderrp, "Error opening %s\n", trainfile);
        exit(1);
    }
    lwidth = 0;
    lheight = 0;
    read(fd, buffer, 8);
    read(fd, buffer, 4);
    for (i = 0; i < 4; i++) {
        if (buffer[i] != ' ') {
            lwidth = lwidth * 10 + buffer[i] - '0';
        }
    }
    read(fd, buffer, 4);
    for (i = 0; i < 4; i++) {
        if (buffer[i] != ' ') {
            lheight = lheight * 10 + buffer[i] - '0';
        }
    }
    if (mode == 1) {
        numf1s = numinputs = lwidth * lheight;
        numf2s = objects + 1;
        init_globs(1);
        init_net();
    } else {
        if ((lwidth * lheight) != numf1s) {
            fprintf(__stderrp, "Dimensions of first image do not match");
            fprintf(__stderrp, " dimensions of second.\n");
            exit(1);
        }
    }
    unsigned long int _imopVarPre370;
    void *_imopVarPre371;
    _imopVarPre370 = lwidth * lheight * sizeof(char);
    _imopVarPre371 = malloc(_imopVarPre370);
    superbuffer = (char *) _imopVarPre371;
    if (superbuffer == ((void *) 0)) {
        fprintf(__stderrp, "Problems with malloc in loadimage()\n");
        exit(1);
    }
    int _imopVarPre373;
    _imopVarPre373 = lwidth * lheight;
    read(fd, superbuffer, _imopVarPre373);
    for (i = 0; i < lheight * lwidth; i++) {
        t = superbuffer[i];
        f1_layer[o][i].I[spot] = (double) t;
    }
    free(superbuffer);
}
void sim_other_objects(int low, int high , int stop) {
    int i;
    int j;
    int varNumF1;
    int varHigh;
    int noise1;
    double noise2;
    if (high <= low) {
        return;
    }
    srand(10);
    varNumF1 = numf1s;
    varHigh = high;
    for (i = low; i < varHigh; i++) {
        for (j = 0; j < varNumF1; j++) {
            if (i % low) {
                tds[j][i] = tds[j][0];
                tds[j][i] = bus[j][0];
            } else {
                tds[j][i] = tds[j][1];
                tds[j][i] = bus[j][1];
            }
        }
    }
    for (i = low; i < varHigh; i++) {
        for (j = 0; j < varNumF1; j++) {
            int _imopVarPre375;
            _imopVarPre375 = rand();
            noise1 = _imopVarPre375 & 0xffff;
            noise2 = (double) noise1 / (double) 0xffff;
            tds[j][i] -= noise2;
            bus[j][i] -= noise2;
        }
    }
}
void setup_base_pattern(int spot) {
    int i;
    int j;
    for (i = 0; i < numf1s; i++) {
        for (j = spot; j < numf2s; j++) {
            double _imopVarPre378;
            double _imopVarPre379;
            _imopVarPre378 = (double) numf1s;
            _imopVarPre379 = sqrt(_imopVarPre378);
            tds[i][j] = bus[i][j] = 1.0 / _imopVarPre379 / (1 - d);
        }
    }
}
void scan_recognize(int startx, int starty , int endx , int endy , int stride) {
    int gStartY;
    int gEndY;
    int gStartX;
    int gEndX;
    int gLheight;
    int gLwidth;
    int gStride;
    int gPassFlag;
    int avgy;
    int i;
    int j;
    int m;
    int n;
    long k;
    int ij;
    int ijmx;
    int jnum;
    int inum;
    int modval;
    int addsubval_out;
    int addsubval_in;
    double *mat_con;
    gStartY = starty;
    gStartX = startx;
    gEndY = endy;
    gEndX = endx;
    gLheight = lheight;
    gLwidth = lwidth;
    gStride = stride;
    int _imopVarPre380;
    _imopVarPre380 = (starty > (height - lheight + 1));
    if (!_imopVarPre380) {
        _imopVarPre380 = (startx > (width - lwidth + 1));
    }
    if (_imopVarPre380) {
        fprintf(__stderrp, "Startx %d or Starty %d is out of range\n", startx, starty);
        exit(1);
    }
    int _imopVarPre381;
    _imopVarPre381 = (endy > (height - lheight + 1));
    if (!_imopVarPre381) {
        _imopVarPre381 = (endx > (width - lwidth + 1));
    }
    if (_imopVarPre381) {
        fprintf(__stderrp, "endx %d or endy %d is out of range\n", endx, endy);
        exit(1);
    }
    int _imopVarPre383;
    int _imopVarPre384;
    _imopVarPre383 = (gEndY - gStartY) / numthreads;
    _imopVarPre384 = abs(_imopVarPre383);
    avgy = _imopVarPre384;
    modval = avgy % 2;
    if (modval != 0) {
        addsubval_out = avgy + 1;
        addsubval_in = avgy - 1;
    } else {
        addsubval_out = avgy;
        addsubval_in = avgy;
    }
    inum = (gEndX - gStartX) / gStride;
    jnum = (gEndY - gStartY) / gStride;
    ijmx = (gEndX - gStartX) * (gEndY - gStartY) / (gStride * gStride);
    unsigned long int _imopVarPre387;
    void *_imopVarPre388;
    _imopVarPre387 = ijmx * sizeof(double);
    _imopVarPre388 = malloc(_imopVarPre387);
    mat_con = (double *) _imopVarPre388;
    if ((mat_con == ((void *) 0))) {
        fprintf(__stderrp, "Malloc problem with mat_con\n");
        exit(1);
    }
#pragma omp parallel private(busp, o, i, j)
    {
        unsigned long int _imopVarPre391;
        void *_imopVarPre392;
        _imopVarPre391 = numf1s * sizeof(double *);
        _imopVarPre392 = malloc(_imopVarPre391);
        busp = (double **) _imopVarPre392;
        if ((busp == ((void *) 0))) {
            fprintf(__stderrp, "Malloc problem in private load_weights\n");
            exit(1);
        }
        unsigned long int _imopVarPre395;
        void *_imopVarPre396;
        _imopVarPre395 = numf1s * numf2s * sizeof(double);
        _imopVarPre396 = malloc(_imopVarPre395);
        busp[0] = (double *) _imopVarPre396;
        if ((busp[0] == ((void *) 0))) {
            fprintf(__stderrp, "Malloc problem in private load_weights 0\n");
            exit(1);
        }
        for (i = 1; i < numf1s; i++) {
            busp[i] = busp[i - 1] + numf2s;
        }
        for (i = 0; i < numf1s; i++) {
            for (j = 0; j < numf2s; j++) {
                busp[i][j] = bus[i][j];
            }
        }
        o = 0;
#pragma omp for private(k, m, n, gPassFlag) schedule(dynamic) nowait
        for (ij = 0; ij < ijmx; ij++) {
            j = ((ij / inum) * gStride) + gStartY;
            i = ((ij % inum) * gStride) + gStartX;
            k = 0;
            for (m = j; m < (gLheight + j); m++) {
                for (n = i; n < (gLwidth + i); n++) {
                    f1_layer[o][k++].I[0] = cimage[m][n];
                }
            }
            gPassFlag = 0;
            double *_imopVarPre398;
            int _imopVarPre399;
            _imopVarPre398 = &mat_con[ij];
            _imopVarPre399 = match(o, i, j, _imopVarPre398, busp);
            gPassFlag = _imopVarPre399;
            if (gPassFlag == 1) {
                if (set_high[o][0] == 1) {
                    highx[o][0] = i;
                    highy[o][0] = j;
                    set_high[o][0] = 0;
                }
                if (set_high[o][1] == 1) {
                    highx[o][1] = i;
                    highy[o][1] = j;
                    set_high[o][1] = 0;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    for (ij = 0; ij < ijmx; ij++) {
        j = ((ij / inum) * gStride) + gStartY;
        i = ((ij % inum) * gStride) + gStartX;
        double _imopVarPre401;
        _imopVarPre401 = mat_con[ij];
        printf("x = %i, y = %i, match confidence = %9.7f \n", i, j, _imopVarPre401);
    }
}
int main(int argc, char *argv[]) {
    int k;
    int startx;
    int starty;
    int endx;
    int endy;
    int stride;
    int objects;
    int arg_index;
    char *scanfile = ((void *) 0);
    char *weightfile = ((void *) 0);
    char *trainfile1 = ((void *) 0);
    char *trainfile2 = ((void *) 0);
    struct lnkNode *ptr;
    numthreads = 1;
    printf("\n");
    numTraining = 0;
    if (argc < 2) {
        goto Usage;
    }
    if (argc == 2) {
        char *_imopVarPre404;
        int _imopVarPre405;
        _imopVarPre404 = argv[1];
        _imopVarPre405 = strcmp(_imopVarPre404, "-v");
        if (_imopVarPre405 == 0) {
            goto Version;
        } else {
            char *_imopVarPre408;
            int _imopVarPre409;
            _imopVarPre408 = argv[1];
            _imopVarPre409 = strcmp(_imopVarPre408, "-h");
            if (_imopVarPre409 == 0) {
                goto Usage;
            }
        }
    }
    stride = 0;
    startx = 0;
    starty = 0;
    endy = 0;
    endx = 0;
    objects = 0;
    multiplier = 1;
    arg_index = 1;
    while (arg_index < argc - 1) {
        char *_imopVarPre412;
        int _imopVarPre413;
        _imopVarPre412 = argv[arg_index];
        _imopVarPre413 = strcmp(_imopVarPre412, "-scanfile");
        if (_imopVarPre413 == 0) {
            scanfile = argv[arg_index + 1];
        } else {
            char *_imopVarPre416;
            int _imopVarPre417;
            _imopVarPre416 = argv[arg_index];
            _imopVarPre417 = strcmp(_imopVarPre416, "-weightfile");
            if (_imopVarPre417 == 0) {
                weightfile = argv[arg_index + 1];
            } else {
                char *_imopVarPre420;
                int _imopVarPre421;
                _imopVarPre420 = argv[arg_index];
                _imopVarPre421 = strcmp(_imopVarPre420, "-trainfile1");
                if (_imopVarPre421 == 0) {
                    trainfile1 = argv[arg_index + 1];
                } else {
                    char *_imopVarPre424;
                    int _imopVarPre425;
                    _imopVarPre424 = argv[arg_index];
                    _imopVarPre425 = strcmp(_imopVarPre424, "-trainfile2");
                    if (_imopVarPre425 == 0) {
                        trainfile2 = argv[arg_index + 1];
                    } else {
                        char *_imopVarPre428;
                        int _imopVarPre429;
                        _imopVarPre428 = argv[arg_index];
                        _imopVarPre429 = strcmp(_imopVarPre428, "-startx");
                        if (_imopVarPre429 == 0) {
                            char *_imopVarPre431;
                            int _imopVarPre432;
                            _imopVarPre431 = argv[arg_index + 1];
                            _imopVarPre432 = atoi(_imopVarPre431);
                            startx = _imopVarPre432;
                        } else {
                            char *_imopVarPre435;
                            int _imopVarPre436;
                            _imopVarPre435 = argv[arg_index];
                            _imopVarPre436 = strcmp(_imopVarPre435, "-starty");
                            if (_imopVarPre436 == 0) {
                                char *_imopVarPre438;
                                int _imopVarPre439;
                                _imopVarPre438 = argv[arg_index + 1];
                                _imopVarPre439 = atoi(_imopVarPre438);
                                starty = _imopVarPre439;
                            } else {
                                char *_imopVarPre442;
                                int _imopVarPre443;
                                _imopVarPre442 = argv[arg_index];
                                _imopVarPre443 = strcmp(_imopVarPre442, "-endx");
                                if (_imopVarPre443 == 0) {
                                    char *_imopVarPre445;
                                    int _imopVarPre446;
                                    _imopVarPre445 = argv[arg_index + 1];
                                    _imopVarPre446 = atoi(_imopVarPre445);
                                    endx = _imopVarPre446;
                                } else {
                                    char *_imopVarPre449;
                                    int _imopVarPre450;
                                    _imopVarPre449 = argv[arg_index];
                                    _imopVarPre450 = strcmp(_imopVarPre449, "-endy");
                                    if (_imopVarPre450 == 0) {
                                        char *_imopVarPre452;
                                        int _imopVarPre453;
                                        _imopVarPre452 = argv[arg_index + 1];
                                        _imopVarPre453 = atoi(_imopVarPre452);
                                        endy = _imopVarPre453;
                                    } else {
                                        char *_imopVarPre456;
                                        int _imopVarPre457;
                                        _imopVarPre456 = argv[arg_index];
                                        _imopVarPre457 = strcmp(_imopVarPre456, "-stride");
                                        if (_imopVarPre457 == 0) {
                                            char *_imopVarPre459;
                                            int _imopVarPre460;
                                            _imopVarPre459 = argv[arg_index + 1];
                                            _imopVarPre460 = atoi(_imopVarPre459);
                                            stride = _imopVarPre460;
                                        } else {
                                            char *_imopVarPre463;
                                            int _imopVarPre464;
                                            _imopVarPre463 = argv[arg_index];
                                            _imopVarPre464 = strcmp(_imopVarPre463, "-objects");
                                            if (_imopVarPre464 == 0) {
                                                char *_imopVarPre466;
                                                int _imopVarPre467;
                                                _imopVarPre466 = argv[arg_index + 1];
                                                _imopVarPre467 = atoi(_imopVarPre466);
                                                objects = _imopVarPre467;
                                            } else {
                                                char *_imopVarPre470;
                                                int _imopVarPre471;
                                                _imopVarPre470 = argv[arg_index];
                                                _imopVarPre471 = strcmp(_imopVarPre470, "-tile");
                                                if (_imopVarPre471 == 0) {
                                                    char *_imopVarPre473;
                                                    int _imopVarPre474;
                                                    _imopVarPre473 = argv[arg_index + 1];
                                                    _imopVarPre474 = atoi(_imopVarPre473);
                                                    multiplier = _imopVarPre474;
                                                } else {
                                                    char *_imopVarPre476;
                                                    _imopVarPre476 = argv[arg_index];
                                                    fprintf(__stderrp, "ERROR: Unknown option -> %s\n", _imopVarPre476);
                                                    goto Usage;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        arg_index += 2;
    }
    if (scanfile == ((void *) 0)) {
        fprintf(__stderrp, "ERROR: Must specify input files\n");
        goto Usage;
    }
    int _imopVarPre478;
    _imopVarPre478 = (weightfile == ((void *) 0));
    if (_imopVarPre478) {
        _imopVarPre478 = (trainfile1 == ((void *) 0));
    }
    if (_imopVarPre478) {
        fprintf(__stderrp, "ERROR: Must specify weightfile or trainfile1\n");
        goto Usage;
    }
    int _imopVarPre480;
    _imopVarPre480 = (weightfile != ((void *) 0));
    if (_imopVarPre480) {
        _imopVarPre480 = (trainfile1 != ((void *) 0));
    }
    if (_imopVarPre480) {
        fprintf(__stderrp, "ERROR: Cannot specify weightfile and trainfile1\n");
        goto Usage;
    }
    printf("Running ART-II Object Recognition...\n");
    loadimage(scanfile);
    if (weightfile != ((void *) 0)) {
        numpatterns = 1;
        if (objects == 0) {
            objects = numpatterns;
        }
        load_weights(weightfile);
        init_globs(0);
        init_net();
    } else {
        if (trainfile2 != ((void *) 0)) {
            numpatterns = 2;
            if (objects < numpatterns) {
                objects = numpatterns;
            }
            load_train(trainfile1, 1, objects);
            alloc_td_bu();
            init_td(0);
            init_bu(0);
            resonant = k = 0;
            while (!resonant) {
                train_match(0);
                k++;
            }
            load_train(trainfile2, 2, objects);
            init_globs(2);
            init_td(1);
            init_bu(1);
            resonant = k = 0;
            while (!resonant) {
                train_match(1);
                k++;
            }
            init_globs(0);
            init_td(objects);
            init_bu(objects);
            sim_other_objects(numpatterns, objects, numf2s);
            setup_base_pattern(objects);
        } else {
            numpatterns = 1;
            if (objects < numpatterns) {
                objects = numpatterns;
            }
            load_train(trainfile1, 1, objects);
            alloc_td_bu();
            init_td(0);
            init_bu(0);
            resonant = k = 0;
            while (!resonant) {
                train_match(0);
                k++;
            }
            init_globs(0);
            init_td(1);
            init_bu(1);
            setup_base_pattern(1);
        }
    }
    if (endy == 0) {
        endy = height - lheight;
    }
    if (endx == 0) {
        endx = width - lwidth;
    }
    for (i = 0; i < numthreads; i++) {
        highest_confidence[i][0] = 0.0;
        highest_confidence[i][1] = 0.0;
        highx[i][0] = 0;
        highx[i][1] = 0;
        highy[i][0] = 0;
        highy[i][1] = 0;
        set_high[i][0] = 0;
        set_high[i][1] = 0;
    }
    printf("\nWorking with %d F2 objects...\n", numf2s);
    scan_recognize(startx, starty, endx, endy, stride);
    sort_list_items();
    ptr = head;
    while (ptr != ((void *) 0)) {
        int _imopVarPre485;
        int _imopVarPre486;
        double _imopVarPre487;
        int _imopVarPre488;
        _imopVarPre485 = ptr->y;
        _imopVarPre486 = ptr->x;
        _imopVarPre487 = ptr->Vigilance;
        _imopVarPre488 = ptr->F2Neuron;
        printf("\nResult: Matched image %d with confidence of %f at coordinates x = %d, y = %d", _imopVarPre488, _imopVarPre487, _imopVarPre486, _imopVarPre485);
        ptr = ptr->next;
    }
    printf("\nDone\n");
    return 0;
    Usage: fprintf(__stderrp, "Usage: scanner [-startx <num>] [-starty <num>] [-endx <num>] [-endy <num>] [-stride <num>] -scanfile <filename> -trainfile1 <filename> [-trainfile2 <filename>]\n");
    exit(1);
    Version: fprintf(__stderrp, "Version 1.00 \n");
    exit(1);
}
