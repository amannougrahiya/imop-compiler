typedef long long __int64_t;
typedef long unsigned int __darwin_size_t;
typedef long __darwin_ssize_t;
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
extern FILE *__stderrp;
int fclose(FILE *);
char *fgets(char *restrict , int  , FILE *);
FILE *fopen(const char *restrict __filename, const char *restrict __mode);
int fprintf(FILE *restrict , const char *restrict , ...);
int printf(const char *restrict , ...);
void rewind(FILE *);
typedef __darwin_ssize_t ssize_t;
double atof(const char *);
int atoi(const char *);
void *calloc(size_t __count, size_t __size);
void exit(int );
void free(void *);
void *malloc(size_t __size);
long random(void );
void srandom(unsigned );
char *strerror(int __errnum);
char *strtok(char *__str, const char *__sep);
extern int *__error(void );
extern double pow(double , double );
extern double sqrt(double );
int close(int );
ssize_t read(int , void * , size_t );
int getopt(int , char *const [] , const char *);
extern char *suboptarg;
struct fssearchblock ;
struct searchstate ;
#pragma pack(4)
#pragma pack()
struct _filesec ;
int open(const char *, int , ...);
extern void omp_set_num_threads(int );
extern int omp_get_max_threads(void );
extern int omp_get_thread_num(void );
extern double omp_get_wtime(void );
float multid_feuclid_dist(float *, float * , int );
float euclid_dist_2(float *, float * , int );
int find_nearest_point(float *, int  , float ** , int );
void sum_fuzzy_members(float **, int  , float ** , int  , int  , float  , float *);
float **fuzzy_kmeans_cluster(int , float ** , int  , int  , int  , float  , float );
float fuzzy_validity(float **, int  , int  , float ** , int  , float );
float *extract_moments(float *, int  , int );
void zscore_transform(float **, int  , int );
int cluster(int , int  , int  , int  , int  , int  , float ** , int  , int  , int  , float  , float  , int * , float *** , int * , float * , double * , double *);
float **kmeans_clustering(int , float ** , int  , int  , int  , float  , int *);
int _debug;
__inline float multid_feuclid_dist(float *pt1, float *pt2 , int numdims) {
    float ans = 0.0;
    int i;
    for (i = 0; i < numdims; i++) {
        ans += ((pt1[i] - pt2[i]) * (pt1[i] - pt2[i]));
    }
    double _imopVarPre143;
    double _imopVarPre144;
    _imopVarPre143 = (double) ans;
    _imopVarPre144 = sqrt(_imopVarPre143);
    ans = _imopVarPre144;
    return ans;
}
__inline int find_nearest_point(float *pt, int nfeatures , float **pts , int npts) {
    int index;
    int i;
    float max_dist = 3.40282346638528859812e+38F;
    for (i = 0; i < npts; i++) {
        float dist;
        float *_imopVarPre146;
        float _imopVarPre147;
        _imopVarPre146 = pts[i];
        _imopVarPre147 = euclid_dist_2(pt, _imopVarPre146, nfeatures);
        dist = _imopVarPre147;
        if (dist < max_dist) {
            max_dist = dist;
            index = i;
        }
    }
    return index;
}
__inline void sum_fuzzy_members(float **feature, int npoints , float **cluster_centres , int nclusters , int nfeatures , float fuzzyq , float *sum_points) {
    int i;
    int j;
    float distance;
#pragma omp parallel shared(sum_points, feature, cluster_centres) firstprivate(npoints, nclusters, nfeatures, fuzzyq) private(i, j, distance)
    {
#pragma omp for schedule(static) nowait
        for (i = 0; i < npoints; i++) {
            sum_points[i] = 0.0;
            for (j = 0; j < nclusters; j++) {
                float *_imopVarPre150;
                float *_imopVarPre151;
                float _imopVarPre152;
                _imopVarPre150 = cluster_centres[j];
                _imopVarPre151 = feature[i];
                _imopVarPre152 = euclid_dist_2(_imopVarPre151, _imopVarPre150, nfeatures);
                distance = _imopVarPre152;
                double _imopVarPre155;
                double _imopVarPre156;
                double _imopVarPre157;
                _imopVarPre155 = 1.0 / (fuzzyq - 1);
                _imopVarPre156 = 1.0 / (distance + 0.00001);
                _imopVarPre157 = pow(_imopVarPre156, _imopVarPre155);
                sum_points[i] += _imopVarPre157;
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
}
float **fuzzy_kmeans_cluster(int is_perform_atomic, float **feature , int nfeatures , int npoints , int nclusters , float fuzzyq , float delta_centres_threshold) {
    float **cluster_centres;
    float max_delta_centres = 0.0;
    float *sum_points;
    int i;
    int j;
    int k;
    int loop = 0;
    double timing;
    float sqd_dist;
    float membership;
    int nthreads;
    float **new_centre;
    float *sum;
    float **partial_sum;
    float ***partial_new_centre;
    nthreads = omp_get_max_threads();
    unsigned long int _imopVarPre160;
    void *_imopVarPre161;
    _imopVarPre160 = sizeof(float);
    _imopVarPre161 = calloc(nclusters, _imopVarPre160);
    sum = (float *) _imopVarPre161;
    unsigned long int _imopVarPre164;
    void *_imopVarPre165;
    _imopVarPre164 = nclusters * sizeof(float *);
    _imopVarPre165 = malloc(_imopVarPre164);
    new_centre = (float **) _imopVarPre165;
    unsigned long int _imopVarPre169;
    int _imopVarPre170;
    void *_imopVarPre171;
    _imopVarPre169 = sizeof(float);
    _imopVarPre170 = nclusters * nfeatures;
    _imopVarPre171 = calloc(_imopVarPre170, _imopVarPre169);
    new_centre[0] = (float *) _imopVarPre171;
    for (i = 1; i < nclusters; i++) {
        new_centre[i] = new_centre[i - 1] + nfeatures;
    }
    if (!is_perform_atomic) {
        unsigned long int _imopVarPre174;
        void *_imopVarPre175;
        _imopVarPre174 = nthreads * sizeof(float *);
        _imopVarPre175 = malloc(_imopVarPre174);
        partial_sum = (float **) _imopVarPre175;
        unsigned long int _imopVarPre179;
        int _imopVarPre180;
        void *_imopVarPre181;
        _imopVarPre179 = sizeof(float);
        _imopVarPre180 = nthreads * nclusters;
        _imopVarPre181 = calloc(_imopVarPre180, _imopVarPre179);
        partial_sum[0] = (float *) _imopVarPre181;
        for (i = 1; i < nthreads; i++) {
            partial_sum[i] = partial_sum[i - 1] + nclusters;
        }
        unsigned long int _imopVarPre184;
        void *_imopVarPre185;
        _imopVarPre184 = nthreads * sizeof(float **);
        _imopVarPre185 = malloc(_imopVarPre184);
        partial_new_centre = (float ***) _imopVarPre185;
        unsigned long int _imopVarPre188;
        void *_imopVarPre189;
        _imopVarPre188 = nthreads * nclusters * sizeof(float *);
        _imopVarPre189 = malloc(_imopVarPre188);
        partial_new_centre[0] = (float **) _imopVarPre189;
        for (i = 1; i < nthreads; i++) {
            partial_new_centre[i] = partial_new_centre[i - 1] + nclusters;
        }
        for (i = 0; i < nthreads; i++) {
            for (j = 0; j < nclusters; j++) {
                unsigned long int _imopVarPre192;
                void *_imopVarPre193;
                _imopVarPre192 = sizeof(float);
                _imopVarPre193 = calloc(nfeatures, _imopVarPre192);
                partial_new_centre[i][j] = (float *) _imopVarPre193;
            }
        }
    }
    unsigned long int _imopVarPre196;
    void *_imopVarPre197;
    _imopVarPre196 = nclusters * sizeof(float *);
    _imopVarPre197 = malloc(_imopVarPre196);
    cluster_centres = (float **) _imopVarPre197;
    unsigned long int _imopVarPre200;
    void *_imopVarPre201;
    _imopVarPre200 = nclusters * nfeatures * sizeof(float);
    _imopVarPre201 = malloc(_imopVarPre200);
    cluster_centres[0] = (float *) _imopVarPre201;
    for (i = 1; i < nclusters; i++) {
        cluster_centres[i] = cluster_centres[i - 1] + nfeatures;
    }
    for (i = 0; i < nclusters; i++) {
        signed long int _imopVarPre203;
        _imopVarPre203 = random();
        int n = (int) _imopVarPre203 % npoints;
        for (j = 0; j < nfeatures; j++) {
            cluster_centres[i][j] = feature[n][j];
        }
    }
    unsigned long int _imopVarPre206;
    void *_imopVarPre207;
    _imopVarPre206 = npoints * sizeof(float);
    _imopVarPre207 = malloc(_imopVarPre206);
    sum_points = (float *) _imopVarPre207;
    if (_debug) {
        timing = omp_get_wtime();
    }
    int _imopVarPre237;
    do {
        max_delta_centres = 0.0;
        sum_fuzzy_members(feature, npoints, cluster_centres, nclusters, nfeatures, fuzzyq, sum_points);
        if (is_perform_atomic) {
#pragma omp parallel firstprivate(npoints, nclusters, nfeatures, fuzzyq) shared(feature, cluster_centres, sum, new_centre) private(i, j, k, sqd_dist, membership)
            {
#pragma omp for schedule(static) nowait
                for (j = 0; j < npoints; j++) {
                    for (i = 0; i < nclusters; i++) {
                        float *_imopVarPre210;
                        float *_imopVarPre211;
                        float _imopVarPre212;
                        _imopVarPre210 = cluster_centres[i];
                        _imopVarPre211 = feature[j];
                        _imopVarPre212 = euclid_dist_2(_imopVarPre211, _imopVarPre210, nfeatures);
                        sqd_dist = _imopVarPre212;
                        double _imopVarPre216;
                        double _imopVarPre217;
                        double _imopVarPre218;
                        _imopVarPre216 = 1.0 / (fuzzyq - 1);
                        _imopVarPre217 = 1.0 / (sqd_dist + 0.00001);
                        _imopVarPre218 = pow(_imopVarPre217, _imopVarPre216);
                        membership = _imopVarPre218 / (sum_points[j] + 0.00001);
                        membership = pow(membership, fuzzyq);
#pragma omp atomic
                        sum[i] += membership;
                        for (k = 0; k < nfeatures; k++) {
#pragma omp atomic
                            new_centre[i][k] += membership * feature[j][k];
                        }
                    }
                }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
            }
        } else {
#pragma omp parallel shared(feature, cluster_centres, partial_sum, new_centre)
            {
                int _imopVarPre219;
                _imopVarPre219 = omp_get_thread_num();
                int tid = _imopVarPre219;
#pragma omp for firstprivate(npoints, nclusters, nfeatures, fuzzyq) private(i, j, k, sqd_dist, membership) schedule(static) nowait
                for (j = 0; j < npoints; j++) {
                    for (i = 0; i < nclusters; i++) {
                        float *_imopVarPre222;
                        float *_imopVarPre223;
                        float _imopVarPre224;
                        _imopVarPre222 = cluster_centres[i];
                        _imopVarPre223 = feature[j];
                        _imopVarPre224 = euclid_dist_2(_imopVarPre223, _imopVarPre222, nfeatures);
                        sqd_dist = _imopVarPre224;
                        double _imopVarPre228;
                        double _imopVarPre229;
                        double _imopVarPre230;
                        _imopVarPre228 = 1.0 / (fuzzyq - 1);
                        _imopVarPre229 = 1.0 / (sqd_dist + 0.00001);
                        _imopVarPre230 = pow(_imopVarPre229, _imopVarPre228);
                        membership = _imopVarPre230 / (sum_points[j] + 0.00001);
                        membership = pow(membership, fuzzyq);
                        partial_sum[tid][i] += membership;
                        for (k = 0; k < nfeatures; k++) {
                            partial_new_centre[tid][i][k] += membership * feature[j][k];
                        }
                    }
                }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
            }
            for (i = 0; i < nclusters; i++) {
                sum[i] = 0.0;
                for (k = 0; k < nfeatures; k++) {
                    new_centre[i][k] = 0.0;
                }
                for (j = 0; j < nthreads; j++) {
                    sum[i] += partial_sum[j][i];
                    partial_sum[j][i] = 0.0;
                    for (k = 0; k < nfeatures; k++) {
                        new_centre[i][k] += partial_new_centre[j][i][k];
                        partial_new_centre[j][i][k] = 0.0;
                    }
                }
            }
        }
        for (i = 0; i < nclusters; i++) {
            float delta_centre;
            for (k = 0; k < nfeatures; k++) {
                new_centre[i][k] /= sum[i];
            }
            sum[i] = 0.0;
            float *_imopVarPre233;
            float *_imopVarPre234;
            float _imopVarPre235;
            _imopVarPre233 = new_centre[i];
            _imopVarPre234 = cluster_centres[i];
            _imopVarPre235 = multid_feuclid_dist(_imopVarPre234, _imopVarPre233, nfeatures);
            delta_centre = _imopVarPre235;
            for (j = 0; j < nfeatures; j++) {
                cluster_centres[i][j] = new_centre[i][j];
                new_centre[i][j] = 0.0;
            }
            if (delta_centre > max_delta_centres) {
                max_delta_centres = delta_centre;
            }
        }
        _imopVarPre237 = max_delta_centres > delta_centres_threshold;
        if (_imopVarPre237) {
            _imopVarPre237 = loop++ < 500;
        }
    } while (_imopVarPre237);
    if (_debug) {
        double _imopVarPre239;
        _imopVarPre239 = omp_get_wtime();
        timing = _imopVarPre239 - timing;
        printf("nloops = %2d (T = %7.4f)", loop, timing);
    }
    free(sum);
    float *_imopVarPre241;
    _imopVarPre241 = new_centre[0];
    free(_imopVarPre241);
    free(new_centre);
    free(sum_points);
    return cluster_centres;
}
float fuzzy_validity(float **feature, int nfeatures , int npoints , float **cluster_centres , int nclusters , float fuzzyq) {
    int i;
    int j;
    int nearest_cluster;
    int *numpoints_in_clusters;
    float *sum_points;
    float valid_sum = 0.0;
    float min_dist = 3.40282346638528859812e+38F;
    float ret;
    float penalty;
    float sqd_dist;
    float memb_fuzzyq;
    float membership;
    if (nclusters < 2) {
        return 3.40282346638528859812e+38F;
    }
    unsigned long int _imopVarPre244;
    void *_imopVarPre245;
    _imopVarPre244 = sizeof(int);
    _imopVarPre245 = calloc(nclusters, _imopVarPre244);
    numpoints_in_clusters = (int *) _imopVarPre245;
#pragma omp parallel shared(feature, cluster_centres, numpoints_in_clusters) firstprivate(npoints, nfeatures, nclusters) private(i, nearest_cluster)
    {
#pragma omp for schedule(static) nowait
        for (i = 0; i < npoints; i++) {
            float *_imopVarPre247;
            int _imopVarPre248;
            _imopVarPre247 = feature[i];
            _imopVarPre248 = find_nearest_point(_imopVarPre247, nfeatures, cluster_centres, nclusters);
            nearest_cluster = _imopVarPre248;
#pragma omp atomic
            numpoints_in_clusters[nearest_cluster]++;
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    penalty = 0.0;
#pragma omp parallel shared(numpoints_in_clusters) firstprivate(nclusters) private(i)
    {
#pragma omp for schedule(static) reduction(+:penalty) nowait
        for (i = 0; i < nclusters; i++) {
            penalty += 1.0 / (1.0 + ((float) numpoints_in_clusters[i]));
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    penalty /= (float) nclusters;
    free(numpoints_in_clusters);
    unsigned long int _imopVarPre251;
    void *_imopVarPre252;
    _imopVarPre251 = npoints * sizeof(float);
    _imopVarPre252 = malloc(_imopVarPre251);
    sum_points = (float *) _imopVarPre252;
    sum_fuzzy_members(feature, npoints, cluster_centres, nclusters, nfeatures, fuzzyq, sum_points);
#pragma omp parallel shared(feature, cluster_centres) firstprivate(npoints, nclusters, nfeatures, fuzzyq) private(i, j, sqd_dist, memb_fuzzyq, membership)
    {
#pragma omp for schedule(static) reduction(+:valid_sum) nowait
        for (j = 0; j < npoints; j++) {
            for (i = 0; i < nclusters; i++) {
                float *_imopVarPre255;
                float *_imopVarPre256;
                float _imopVarPre257;
                _imopVarPre255 = cluster_centres[i];
                _imopVarPre256 = feature[j];
                _imopVarPre257 = euclid_dist_2(_imopVarPre256, _imopVarPre255, nfeatures);
                sqd_dist = _imopVarPre257;
                double _imopVarPre261;
                double _imopVarPre262;
                double _imopVarPre263;
                _imopVarPre261 = 1.0 / (fuzzyq - 1);
                _imopVarPre262 = 1.0 / (sqd_dist + 0.00001);
                _imopVarPre263 = pow(_imopVarPre262, _imopVarPre261);
                membership = _imopVarPre263 / (sum_points[j] + 0.00001);
                memb_fuzzyq = pow(membership, fuzzyq);
                valid_sum += sqd_dist * memb_fuzzyq;
            }
        }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
    }
    free(sum_points);
    min_dist = 0.0;
    for (i = 0; i < nclusters - 1; i++) {
        float this_cluster_min_dist = 3.40282346638528859812e+38F;
        for (j = i + 1; j < nclusters; j++) {
            float tmp_dist;
            float *_imopVarPre266;
            float *_imopVarPre267;
            float _imopVarPre268;
            _imopVarPre266 = cluster_centres[j];
            _imopVarPre267 = cluster_centres[i];
            _imopVarPre268 = multid_feuclid_dist(_imopVarPre267, _imopVarPre266, nfeatures);
            tmp_dist = _imopVarPre268;
            if (tmp_dist < this_cluster_min_dist) {
                this_cluster_min_dist = tmp_dist;
            }
        }
        min_dist += this_cluster_min_dist;
    }
    min_dist = (((min_dist / (float) (nclusters - 1))) * ((min_dist / (float) (nclusters - 1))));
    ret = (float) (valid_sum / (npoints * min_dist) + penalty);
    return ret;
}
__inline float euclid_dist_2(float *pt1, float *pt2 , int numdims) {
    int i;
    float ans = 0.0;
    for (i = 0; i < numdims; i++) {
        ans += (pt1[i] - pt2[i]) * (pt1[i] - pt2[i]);
    }
    return ans;
}
float **kmeans_clustering(int is_perform_atomic, float **feature , int nfeatures , int npoints , int nclusters , float threshold , int *membership) {
    int i;
    int j;
    int k;
    int index;
    int loop = 0;
    int *new_centers_len;
    float delta;
    float **clusters;
    float **new_centers;
    double timing;
    int nthreads;
    int **partial_new_centers_len;
    float ***partial_new_centers;
    nthreads = omp_get_max_threads();
    unsigned long int _imopVarPre271;
    void *_imopVarPre272;
    _imopVarPre271 = nclusters * sizeof(float *);
    _imopVarPre272 = malloc(_imopVarPre271);
    clusters = (float **) _imopVarPre272;
    unsigned long int _imopVarPre275;
    void *_imopVarPre276;
    _imopVarPre275 = nclusters * nfeatures * sizeof(float);
    _imopVarPre276 = malloc(_imopVarPre275);
    clusters[0] = (float *) _imopVarPre276;
    for (i = 1; i < nclusters; i++) {
        clusters[i] = clusters[i - 1] + nfeatures;
    }
    for (i = 0; i < nclusters; i++) {
        signed long int _imopVarPre278;
        _imopVarPre278 = random();
        int n = (int) _imopVarPre278 % npoints;
        for (j = 0; j < nfeatures; j++) {
            clusters[i][j] = feature[n][j];
        }
    }
    for (i = 0; i < npoints; i++) {
        membership[i] = -1;
    }
    unsigned long int _imopVarPre281;
    void *_imopVarPre282;
    _imopVarPre281 = sizeof(int);
    _imopVarPre282 = calloc(nclusters, _imopVarPre281);
    new_centers_len = (int *) _imopVarPre282;
    unsigned long int _imopVarPre285;
    void *_imopVarPre286;
    _imopVarPre285 = nclusters * sizeof(float *);
    _imopVarPre286 = malloc(_imopVarPre285);
    new_centers = (float **) _imopVarPre286;
    unsigned long int _imopVarPre290;
    int _imopVarPre291;
    void *_imopVarPre292;
    _imopVarPre290 = sizeof(float);
    _imopVarPre291 = nclusters * nfeatures;
    _imopVarPre292 = calloc(_imopVarPre291, _imopVarPre290);
    new_centers[0] = (float *) _imopVarPre292;
    for (i = 1; i < nclusters; i++) {
        new_centers[i] = new_centers[i - 1] + nfeatures;
    }
    if (!is_perform_atomic) {
        unsigned long int _imopVarPre295;
        void *_imopVarPre296;
        _imopVarPre295 = nthreads * sizeof(int *);
        _imopVarPre296 = malloc(_imopVarPre295);
        partial_new_centers_len = (int **) _imopVarPre296;
        unsigned long int _imopVarPre300;
        int _imopVarPre301;
        void *_imopVarPre302;
        _imopVarPre300 = sizeof(int);
        _imopVarPre301 = nthreads * nclusters;
        _imopVarPre302 = calloc(_imopVarPre301, _imopVarPre300);
        partial_new_centers_len[0] = (int *) _imopVarPre302;
        for (i = 1; i < nthreads; i++) {
            partial_new_centers_len[i] = partial_new_centers_len[i - 1] + nclusters;
        }
        unsigned long int _imopVarPre305;
        void *_imopVarPre306;
        _imopVarPre305 = nthreads * sizeof(float **);
        _imopVarPre306 = malloc(_imopVarPre305);
        partial_new_centers = (float ***) _imopVarPre306;
        unsigned long int _imopVarPre309;
        void *_imopVarPre310;
        _imopVarPre309 = nthreads * nclusters * sizeof(float *);
        _imopVarPre310 = malloc(_imopVarPre309);
        partial_new_centers[0] = (float **) _imopVarPre310;
        for (i = 1; i < nthreads; i++) {
            partial_new_centers[i] = partial_new_centers[i - 1] + nclusters;
        }
        for (i = 0; i < nthreads; i++) {
            for (j = 0; j < nclusters; j++) {
                unsigned long int _imopVarPre313;
                void *_imopVarPre314;
                _imopVarPre313 = sizeof(float);
                _imopVarPre314 = calloc(nfeatures, _imopVarPre313);
                partial_new_centers[i][j] = (float *) _imopVarPre314;
            }
        }
    }
    if (_debug) {
        timing = omp_get_wtime();
    }
    int _imopVarPre323;
    do {
        delta = 0.0;
        if (is_perform_atomic) {
#pragma omp parallel private(i, j, index) firstprivate(npoints, nclusters, nfeatures) shared(feature, clusters, membership, new_centers, new_centers_len)
            {
#pragma omp for schedule(static) reduction(+:delta) nowait
                for (i = 0; i < npoints; i++) {
                    float *_imopVarPre316;
                    int _imopVarPre317;
                    _imopVarPre316 = feature[i];
                    _imopVarPre317 = find_nearest_point(_imopVarPre316, nfeatures, clusters, nclusters);
                    index = _imopVarPre317;
                    if (membership[i] != index) {
                        delta += 1.0;
                    }
                    membership[i] = index;
#pragma omp atomic
                    new_centers_len[index]++;
                    for (j = 0; j < nfeatures; j++) {
#pragma omp atomic
                        new_centers[index][j] += feature[i][j];
                    }
                }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
            }
        } else {
#pragma omp parallel shared(feature, clusters, membership, partial_new_centers, partial_new_centers_len)
            {
                int _imopVarPre318;
                _imopVarPre318 = omp_get_thread_num();
                int tid = _imopVarPre318;
#pragma omp for private(i, j, index) firstprivate(npoints, nclusters, nfeatures) schedule(static) reduction(+:delta) nowait
                for (i = 0; i < npoints; i++) {
                    float *_imopVarPre320;
                    int _imopVarPre321;
                    _imopVarPre320 = feature[i];
                    _imopVarPre321 = find_nearest_point(_imopVarPre320, nfeatures, clusters, nclusters);
                    index = _imopVarPre321;
                    if (membership[i] != index) {
                        delta += 1.0;
                    }
                    membership[i] = index;
                    partial_new_centers_len[tid][index]++;
                    for (j = 0; j < nfeatures; j++) {
                        partial_new_centers[tid][index][j] += feature[i][j];
                    }
                }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
            }
            for (i = 0; i < nclusters; i++) {
                for (j = 0; j < nthreads; j++) {
                    new_centers_len[i] += partial_new_centers_len[j][i];
                    partial_new_centers_len[j][i] = 0.0;
                    for (k = 0; k < nfeatures; k++) {
                        new_centers[i][k] += partial_new_centers[j][i][k];
                        partial_new_centers[j][i][k] = 0.0;
                    }
                }
            }
        }
        for (i = 0; i < nclusters; i++) {
            for (j = 0; j < nfeatures; j++) {
                if (new_centers_len[i] > 0) {
                    clusters[i][j] = new_centers[i][j] / new_centers_len[i];
                }
                new_centers[i][j] = 0.0;
            }
            new_centers_len[i] = 0;
        }
        delta /= npoints;
        _imopVarPre323 = delta > threshold;
        if (_imopVarPre323) {
            _imopVarPre323 = loop++ < 500;
        }
    } while (_imopVarPre323);
    if (_debug) {
        double _imopVarPre325;
        _imopVarPre325 = omp_get_wtime();
        timing = _imopVarPre325 - timing;
        printf("nloops = %2d (T = %7.4f)", loop, timing);
    }
    float *_imopVarPre327;
    _imopVarPre327 = new_centers[0];
    free(_imopVarPre327);
    free(new_centers);
    free(new_centers_len);
    return clusters;
}
float *extract_moments(float *data, int num_elts , int num_moments) {
    int i;
    int j;
    float *moments;
    unsigned long int _imopVarPre330;
    void *_imopVarPre331;
    _imopVarPre330 = sizeof(float);
    _imopVarPre331 = calloc(num_moments, _imopVarPre330);
    moments = (float *) _imopVarPre331;
    for (i = 0; i < num_elts; i++) {
        moments[0] += data[i];
    }
    moments[0] = moments[0] / num_elts;
    for (j = 1; j < num_moments; j++) {
        moments[j] = 0;
        for (i = 0; i < num_elts; i++) {
            int _imopVarPre334;
            float _imopVarPre335;
            double _imopVarPre336;
            _imopVarPre334 = j + 1;
            _imopVarPre335 = (data[i] - moments[0]);
            _imopVarPre336 = pow(_imopVarPre335, _imopVarPre334);
            moments[j] += _imopVarPre336;
        }
        moments[j] = moments[j] / num_elts;
    }
    return moments;
}
void zscore_transform(float **data, int numObjects , int numAttributes) {
    float *single_variable;
    float *moments;
    int i;
    int j;
    unsigned long int _imopVarPre339;
    void *_imopVarPre340;
    _imopVarPre339 = sizeof(float);
    _imopVarPre340 = calloc(numObjects, _imopVarPre339);
    single_variable = (float *) _imopVarPre340;
    for (i = 0; i < numAttributes; i++) {
        for (j = 0; j < numObjects; j++) {
            single_variable[j] = data[j][i];
        }
        moments = extract_moments(single_variable, numObjects, 2);
        double _imopVarPre343;
        double _imopVarPre344;
        _imopVarPre343 = (double) moments[1];
        _imopVarPre344 = sqrt(_imopVarPre343);
        moments[1] = (float) _imopVarPre344;
        for (j = 0; j < numObjects; j++) {
            data[j][i] = (data[j][i] - moments[0]) / moments[1];
        }
        free(moments);
    }
    free(single_variable);
}
int cluster(int perform_fuzzy_kmeans, int is_perform_valid , int is_perform_atomic , int is_perform_assign , int numObjects , int numAttributes , float **attributes , int use_zscore_transform , int min_nclusters , int max_nclusters , float fuzzyq , float threshold , int *best_nclusters , float ***cluster_centres , int *cluster_assign , float *validity , double *cluster_timing , double *valid_timing) {
    int i;
    int itime;
    int nclusters;
    int *membership;
    float **tmp_cluster_centres;
    float min_valid = 3.40282346638528859812e+38F;
    double assign_timing;
    if (!perform_fuzzy_kmeans) {
        unsigned long int _imopVarPre347;
        void *_imopVarPre348;
        _imopVarPre347 = numObjects * sizeof(int);
        _imopVarPre348 = malloc(_imopVarPre347);
        membership = (int *) _imopVarPre348;
    }
    if (use_zscore_transform) {
        zscore_transform(attributes, numObjects, numAttributes);
    }
    if (_debug) {
        printf("Initial min_nclusters = %d max_nclusters = %d\n", min_nclusters, max_nclusters);
    }
    itime = 0;
    for (nclusters = min_nclusters; nclusters <= max_nclusters; nclusters++) {
        srandom(7);
        double _imopVarPre349;
        _imopVarPre349 = omp_get_wtime();
        cluster_timing[itime] = _imopVarPre349;
        if (perform_fuzzy_kmeans) {
            tmp_cluster_centres = fuzzy_kmeans_cluster(is_perform_atomic, attributes, numAttributes, numObjects, nclusters, fuzzyq, threshold);
        } else {
            tmp_cluster_centres = kmeans_clustering(is_perform_atomic, attributes, numAttributes, numObjects, nclusters, threshold, membership);
        }
        double _imopVarPre351;
        _imopVarPre351 = omp_get_wtime();
        cluster_timing[itime] = _imopVarPre351 - cluster_timing[itime];
        if (is_perform_valid) {
            double _imopVarPre352;
            _imopVarPre352 = omp_get_wtime();
            valid_timing[itime] = _imopVarPre352;
            float _imopVarPre353;
            _imopVarPre353 = fuzzy_validity(attributes, numAttributes, numObjects, tmp_cluster_centres, nclusters, fuzzyq);
            validity[itime] = _imopVarPre353;
            if (_debug) {
                float _imopVarPre355;
                _imopVarPre355 = validity[itime];
                printf("K = %2d  validity = %6.4f\n", nclusters, _imopVarPre355);
            }
            if (validity[itime] < min_valid) {
                if (*cluster_centres) {
                    float *_imopVarPre357;
                    _imopVarPre357 = (*cluster_centres)[0];
                    free(_imopVarPre357);
                    float **_imopVarPre359;
                    _imopVarPre359 = *cluster_centres;
                    free(_imopVarPre359);
                }
                *cluster_centres = tmp_cluster_centres;
                min_valid = validity[itime];
                *best_nclusters = nclusters;
                if (!perform_fuzzy_kmeans) {
                    unsigned int _imopVarPre362;
                    unsigned long int _imopVarPre363;
                    _imopVarPre362 = __builtin_object_size(cluster_assign, 0);
                    _imopVarPre363 = numObjects * sizeof(int);
                    __builtin___memcpy_chk(cluster_assign, membership, _imopVarPre363, _imopVarPre362);
                }
            } else {
                float *_imopVarPre365;
                _imopVarPre365 = tmp_cluster_centres[0];
                free(_imopVarPre365);
                free(tmp_cluster_centres);
            }
            double _imopVarPre367;
            _imopVarPre367 = omp_get_wtime();
            valid_timing[itime] = _imopVarPre367 - valid_timing[itime];
        } else {
            if (*cluster_centres) {
                float *_imopVarPre369;
                _imopVarPre369 = (*cluster_centres)[0];
                free(_imopVarPre369);
                float **_imopVarPre371;
                _imopVarPre371 = *cluster_centres;
                free(_imopVarPre371);
            }
            *cluster_centres = tmp_cluster_centres;
            *best_nclusters = nclusters;
        }
        if (_debug) {
            double _imopVarPre373;
            _imopVarPre373 = cluster_timing[itime];
            printf("K = %2d T_cluster = %7.4f", nclusters, _imopVarPre373);
            if (is_perform_valid) {
                double _imopVarPre375;
                _imopVarPre375 = valid_timing[itime];
                printf(" T_valid = %7.4f", _imopVarPre375);
            }
            printf("\n");
        }
        itime++;
    }
    if (perform_fuzzy_kmeans) {
        if (is_perform_assign) {
            if (_debug) {
                assign_timing = omp_get_wtime();
            }
#pragma omp parallel shared(cluster_assign, attributes, cluster_centres, best_nclusters) firstprivate(numObjects, numAttributes) private(i)
            {
#pragma omp for schedule(static) nowait
                for (i = 0; i < numObjects; i++) {
                    int _imopVarPre379;
                    float **_imopVarPre380;
                    float *_imopVarPre381;
                    int _imopVarPre382;
                    _imopVarPre379 = *best_nclusters;
                    _imopVarPre380 = *cluster_centres;
                    _imopVarPre381 = attributes[i];
                    _imopVarPre382 = find_nearest_point(_imopVarPre381, numAttributes, _imopVarPre380, _imopVarPre379);
                    cluster_assign[i] = _imopVarPre382;
                }
// #pragma omp dummyFlush BARRIER_START
#pragma omp barrier
            }
            if (_debug) {
                double _imopVarPre384;
                _imopVarPre384 = omp_get_wtime();
                assign_timing = _imopVarPre384 - assign_timing;
                printf("cluster assign timing = %8.4f sec\n", assign_timing);
            }
        }
    }
    if (!perform_fuzzy_kmeans) {
        free(membership);
    }
    return 0;
}
void usage(char *argv0) {
    char *help = "Usage: %s [switches] -i filename\n" "       -i filename:     file containing data to be clustered\n" "       -b               input file is in binary format\n" "       -q fuzziness:    fuzziness factor for fuzzy clustering\n" "                        1.0 is non-fuzzy up to infinity\n" "       -f               to perform fuzzy kmeans clustering\n" "                        default is regular kmeans clustering\n" "       -m max_clusters: maximum number of clusters allowed\n" "       -n min_clusters: minimum number of clusters allowed\n" "       -z             : don't zscore transform data\n" "       -t threshold   : threshold value\n" "       -p nproc       : number of threads\n" "       -v             : calculate the validity \n" "       -a             : perform atomic OpenMP pragma\n" "       -s             : perform object assignment if fuzzy performs\n" "       -o             : output timing results, default 0\n" "       -d             : enable debug mode\n";
    fprintf(__stderrp, help, argv0);
    int _imopVarPre386;
    _imopVarPre386 = -1;
    exit(_imopVarPre386);
}
int main(int argc, char **argv) {
    int opt;
    extern char *optarg;
    float fuzzyq = 1.5;
    int max_nclusters = 13;
    int min_nclusters = 4;
    char *filename = 0;
    float *buf;
    float **attributes;
    float **cluster_centres = ((void *) 0);
    int i;
    int j;
    int best_nclusters;
    int *cluster_assign;
    int numAttributes;
    int numObjects;
    int use_zscore_transform = 1;
    char line[1024];
    int isBinaryFile = 0;
    int nloops;
    int len;
    int nthreads;
    int perform_fuzzy_kmeans = 0;
    int is_perform_valid = 0;
    int is_perform_atomic = 0;
    int is_perform_assign = 0;
    int is_perform_output = 0;
    int _timing_tables;
    float *validity;
    float threshold = 0.001;
    double sum;
    double timing;
    double min_timing = 3.40282346638528859812e+38F;
    double io_timing;
    double *clustering_timing;
    double *valid_timing;
    double *min_cluster_timing;
    double *min_valid_timing;
    _debug = 0;
    nthreads = 0;
    int _imopVarPre388;
    _imopVarPre388 = getopt(argc, argv, "p:i:q:m:n:t:avbzdfso");
    while ((opt = _imopVarPre388) != (-1)) {
        switch (opt) {
            case 'i': filename = optarg;
            break;
            case 'b': isBinaryFile = 1;
            break;
            case 'q': fuzzyq = atof(optarg);
            break;
            case 'f': perform_fuzzy_kmeans = 1;
            break;
            case 't': threshold = atof(optarg);
            break;
            case 'm': max_nclusters = atoi(optarg);
            break;
            case 'n': min_nclusters = atoi(optarg);
            break;
            case 'z': use_zscore_transform = 0;
            break;
            case 'p': nthreads = atoi(optarg);
            break;
            case 'v': is_perform_valid = 1;
            break;
            case 'a': is_perform_atomic = 1;
            break;
            case 's': is_perform_assign = 1;
            break;
            case 'o': is_perform_output = 1;
            break;
            case 'd': _debug = 1;
            break;
            char *_imopVarPre390;
            case '?': _imopVarPre390 = argv[0];
            usage(_imopVarPre390);
            break;
            char *_imopVarPre392;
            default: _imopVarPre392 = argv[0];
            usage(_imopVarPre392);
            break;
        }
        _imopVarPre388 = getopt(argc, argv, "p:i:q:m:n:t:avbzdfso");
    }
    if (filename == 0) {
        char *_imopVarPre394;
        _imopVarPre394 = argv[0];
        usage(_imopVarPre394);
    }
    if (nthreads > 0) {
        omp_set_num_threads(nthreads);
    }
    numAttributes = numObjects = 0;
    io_timing = omp_get_wtime();
    if (isBinaryFile) {
        int infile;
        int _imopVarPre396;
        _imopVarPre396 = open(filename, 0x0000, "0600");
        if ((infile = _imopVarPre396) == -1) {
            int *_imopVarPre416;
            int _imopVarPre417;
            char *_imopVarPre418;
            _imopVarPre416 = __error();
            _imopVarPre417 = (*_imopVarPre416);
            _imopVarPre418 = strerror(_imopVarPre417);
            fprintf(__stderrp, "Error: file %s (%s)\n", filename, _imopVarPre418);
            exit(1);
        }
        unsigned long int _imopVarPre421;
        int *_imopVarPre422;
        _imopVarPre421 = sizeof(int);
        _imopVarPre422 = &numObjects;
        read(infile, _imopVarPre422, _imopVarPre421);
        unsigned long int _imopVarPre425;
        int *_imopVarPre426;
        _imopVarPre425 = sizeof(int);
        _imopVarPre426 = &numAttributes;
        read(infile, _imopVarPre426, _imopVarPre425);
        if (_debug) {
            printf("File %s contains numObjects = %d\n", filename, numObjects);
            printf("File %s, number of attributes in each point = %d\n", filename, numAttributes);
        }
        unsigned long int _imopVarPre429;
        void *_imopVarPre430;
        _imopVarPre429 = numObjects * numAttributes * sizeof(float);
        _imopVarPre430 = malloc(_imopVarPre429);
        buf = (float *) _imopVarPre430;
        unsigned long int _imopVarPre433;
        void *_imopVarPre434;
        _imopVarPre433 = numObjects * sizeof(float *);
        _imopVarPre434 = malloc(_imopVarPre433);
        attributes = (float **) _imopVarPre434;
        unsigned long int _imopVarPre437;
        void *_imopVarPre438;
        _imopVarPre437 = numObjects * numAttributes * sizeof(float);
        _imopVarPre438 = malloc(_imopVarPre437);
        attributes[0] = (float *) _imopVarPre438;
        for (i = 1; i < numObjects; i++) {
            attributes[i] = attributes[i - 1] + numAttributes;
        }
        unsigned long int _imopVarPre440;
        _imopVarPre440 = numObjects * numAttributes * sizeof(float);
        read(infile, buf, _imopVarPre440);
        close(infile);
    } else {
        FILE *infile;
        struct __sFILE *_imopVarPre442;
        _imopVarPre442 = fopen(filename, "r");
        if ((infile = _imopVarPre442) == ((void *) 0)) {
            int *_imopVarPre462;
            int _imopVarPre463;
            char *_imopVarPre464;
            _imopVarPre462 = __error();
            _imopVarPre463 = (*_imopVarPre462);
            _imopVarPre464 = strerror(_imopVarPre463);
            fprintf(__stderrp, "Error: file %s (%s)\n", filename, _imopVarPre464);
            exit(1);
        }
        char *_imopVarPre466;
        _imopVarPre466 = fgets(line, 1024, infile);
        while (_imopVarPre466 != ((void *) 0)) {
            char *_imopVarPre468;
            _imopVarPre468 = strtok(line, " \t\n");
            if (_imopVarPre468 != 0) {
                numObjects++;
            }
            _imopVarPre466 = fgets(line, 1024, infile);
        }
        rewind(infile);
        char *_imopVarPre470;
        _imopVarPre470 = fgets(line, 1024, infile);
        while (_imopVarPre470 != ((void *) 0)) {
            char *_imopVarPre472;
            _imopVarPre472 = strtok(line, " \t\n");
            if (_imopVarPre472 != 0) {
                void *_imopVarPre475;
                char *_imopVarPre476;
                _imopVarPre475 = ((void *) 0);
                _imopVarPre476 = strtok(_imopVarPre475, " ,\t\n");
                while (_imopVarPre476 != ((void *) 0)) {
                    numAttributes++;
                    _imopVarPre475 = ((void *) 0);
                    _imopVarPre476 = strtok(_imopVarPre475, " ,\t\n");
                }
                break;
            }
            _imopVarPre470 = fgets(line, 1024, infile);
        }
        if (_debug) {
            printf("File %s contains numObjects = %d\n", filename, numObjects);
            printf("File %s, number of attributes in each point = %d\n", filename, numAttributes);
        }
        unsigned long int _imopVarPre479;
        void *_imopVarPre480;
        _imopVarPre479 = numObjects * numAttributes * sizeof(float);
        _imopVarPre480 = malloc(_imopVarPre479);
        buf = (float *) _imopVarPre480;
        unsigned long int _imopVarPre483;
        void *_imopVarPre484;
        _imopVarPre483 = numObjects * sizeof(float *);
        _imopVarPre484 = malloc(_imopVarPre483);
        attributes = (float **) _imopVarPre484;
        unsigned long int _imopVarPre487;
        void *_imopVarPre488;
        _imopVarPre487 = numObjects * numAttributes * sizeof(float);
        _imopVarPre488 = malloc(_imopVarPre487);
        attributes[0] = (float *) _imopVarPre488;
        for (i = 1; i < numObjects; i++) {
            attributes[i] = attributes[i - 1] + numAttributes;
        }
        rewind(infile);
        i = 0;
        char *_imopVarPre490;
        _imopVarPre490 = fgets(line, 1024, infile);
        while (_imopVarPre490 != ((void *) 0)) {
            char *_imopVarPre492;
            _imopVarPre492 = strtok(line, " \t\n");
            if (_imopVarPre492 == ((void *) 0)) {
                continue;
            }
            for (j = 0; j < numAttributes; j++) {
                void *_imopVarPre497;
                char *_imopVarPre498;
                double _imopVarPre499;
                _imopVarPre497 = ((void *) 0);
                _imopVarPre498 = strtok(_imopVarPre497, " ,\t\n");
                _imopVarPre499 = atof(_imopVarPre498);
                buf[i] = _imopVarPre499;
                i++;
            }
            _imopVarPre490 = fgets(line, 1024, infile);
        }
        fclose(infile);
    }
    double _imopVarPre501;
    _imopVarPre501 = omp_get_wtime();
    io_timing = _imopVarPre501 - io_timing;
    unsigned long int _imopVarPre504;
    void *_imopVarPre505;
    _imopVarPre504 = numObjects * sizeof(int);
    _imopVarPre505 = malloc(_imopVarPre504);
    cluster_assign = (int *) _imopVarPre505;
    nloops = 8;
    len = max_nclusters - min_nclusters + 1;
    unsigned long int _imopVarPre508;
    void *_imopVarPre509;
    _imopVarPre508 = sizeof(float);
    _imopVarPre509 = calloc(len, _imopVarPre508);
    validity = (float *) _imopVarPre509;
    unsigned long int _imopVarPre512;
    void *_imopVarPre513;
    _imopVarPre512 = sizeof(double);
    _imopVarPre513 = calloc(len, _imopVarPre512);
    clustering_timing = (double *) _imopVarPre513;
    unsigned long int _imopVarPre516;
    void *_imopVarPre517;
    _imopVarPre516 = sizeof(double);
    _imopVarPre517 = calloc(len, _imopVarPre516);
    valid_timing = (double *) _imopVarPre517;
    unsigned long int _imopVarPre520;
    void *_imopVarPre521;
    _imopVarPre520 = sizeof(double);
    _imopVarPre521 = calloc(len, _imopVarPre520);
    min_cluster_timing = (double *) _imopVarPre521;
    unsigned long int _imopVarPre524;
    void *_imopVarPre525;
    _imopVarPre524 = sizeof(double);
    _imopVarPre525 = calloc(len, _imopVarPre524);
    min_valid_timing = (double *) _imopVarPre525;
    for (i = 0; i < nloops; i++) {
        float *_imopVarPre532;
        unsigned int _imopVarPre533;
        unsigned long int _imopVarPre534;
        float *_imopVarPre535;
        _imopVarPre532 = attributes[0];
        _imopVarPre533 = __builtin_object_size(_imopVarPre532, 0);
        _imopVarPre534 = numObjects * numAttributes * sizeof(float);
        _imopVarPre535 = attributes[0];
        __builtin___memcpy_chk(_imopVarPre535, buf, _imopVarPre534, _imopVarPre533);
        timing = omp_get_wtime();
        cluster_centres = ((void *) 0);
        float ***_imopVarPre538;
        int *_imopVarPre539;
        _imopVarPre538 = &cluster_centres;
        _imopVarPre539 = &best_nclusters;
        cluster(perform_fuzzy_kmeans, is_perform_valid, is_perform_atomic, is_perform_assign, numObjects, numAttributes, attributes, use_zscore_transform, min_nclusters, max_nclusters, fuzzyq, threshold, _imopVarPre539, _imopVarPre538, cluster_assign, validity, clustering_timing, valid_timing);
        double _imopVarPre541;
        _imopVarPre541 = omp_get_wtime();
        timing = _imopVarPre541 - timing;
        if (_debug) {
            printf("nloop = %d cluster() time = %.4f\n", i, timing);
        }
        if (timing < min_timing) {
            min_timing = timing;
            for (j = 0; j < len; j++) {
                min_cluster_timing[j] = clustering_timing[j];
                min_valid_timing[j] = valid_timing[j];
            }
        }
    }
    if (is_perform_output) {
        int _imopVarPre543;
        _imopVarPre543 = omp_get_max_threads();
        printf("Number of threads = %d\n", _imopVarPre543);
        printf("File %s contains  numObjects = %d, each of size %d\n", filename, numObjects, numAttributes);
        if (perform_fuzzy_kmeans) {
            if (is_perform_assign) {
                printf("**** Fuzzy Kmeans (Loop N first) with assign ****");
            } else {
                printf("**** Fuzzy Kmeans (Loop N first) without assign ****");
            }
        } else {
            printf("Performing **** Regular Kmeans (Loop N first) ****");
        }
        if (is_perform_atomic) {
            printf(" use atomic pragma ******\n");
        } else {
            printf(" use array reduction ******\n");
        }
        _timing_tables = 1;
        if (_timing_tables) {
            printf(" K, Tcluster,   Tvalid,   Tsum\n");
        }
        for (i = 0; i < len; i++) {
            if (_timing_tables) {
                double _imopVarPre548;
                double _imopVarPre549;
                double _imopVarPre550;
                int _imopVarPre551;
                _imopVarPre548 = min_cluster_timing[i] + min_valid_timing[i];
                _imopVarPre549 = min_valid_timing[i];
                _imopVarPre550 = min_cluster_timing[i];
                _imopVarPre551 = min_nclusters + i;
                printf("%2d, %8.4f, %8.4f, %8.4f\n", _imopVarPre551, _imopVarPre550, _imopVarPre549, _imopVarPre548);
            } else {
                int _imopVarPre553;
                _imopVarPre553 = min_nclusters + i;
                printf("for %2d clusters: ", _imopVarPre553);
                float _imopVarPre555;
                _imopVarPre555 = validity[i];
                printf("validity = %6.4f", _imopVarPre555);
                double _imopVarPre557;
                _imopVarPre557 = min_cluster_timing[i];
                printf(" T_cluster = %8.4f", _imopVarPre557);
                double _imopVarPre559;
                _imopVarPre559 = min_valid_timing[i];
                printf(" T_valid = %8.4f", _imopVarPre559);
                double _imopVarPre561;
                _imopVarPre561 = min_cluster_timing[i] + min_valid_timing[i];
                printf(" T_sum = %8.4f\n", _imopVarPre561);
            }
        }
        for (i = 0; i < 79; i++) {
            printf("-");
        }
        printf("\n");
        sum = 0.0;
        for (i = 0; i < len; i++) {
            sum += min_cluster_timing[i];
        }
        if (_timing_tables) {
            printf("  , %8.4f,", sum);
        } else {
            printf("sum                                              %.4f", sum);
        }
        sum = 0.0;
        for (i = 0; i < len; i++) {
            sum += min_valid_timing[i];
        }
        if (_timing_tables) {
            printf(" %8.4f,", sum);
        } else {
            printf("           %8.4f", sum);
        }
        for (i = 0; i < len; i++) {
            sum += min_cluster_timing[i];
        }
        if (_timing_tables) {
            printf(" %8.4f\n", sum);
        } else {
            printf("           %.4f\n", sum);
        }
        printf("I/O time = %8.4f\n", io_timing);
        if (!_timing_tables) {
            printf("Conclude : best no. of clusters found = %d\n", best_nclusters);
            printf("Total timing = %10.4f sec\n", min_timing);
        }
        for (i = 0; i < 79; i++) {
            printf("-");
        }
        printf("\n");
    }
    free(min_valid_timing);
    free(min_cluster_timing);
    free(valid_timing);
    free(clustering_timing);
    free(validity);
    free(cluster_assign);
    free(attributes);
    float *_imopVarPre563;
    _imopVarPre563 = cluster_centres[0];
    free(_imopVarPre563);
    free(cluster_centres);
    free(buf);
    return 0;
}
