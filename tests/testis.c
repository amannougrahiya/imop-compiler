
/*[]*/
struct __sFILEX ;
/*[]*/
int printf(const char *restrict , ...);
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
typedef int INT_TYPE;
/*[]*/
INT_TYPE *key_buff_ptr_global;
/*[]*/
int passed_verification;
/*[]*/
INT_TYPE key_array[(1 << 16)];
/*[]*/
INT_TYPE key_buff1[(1 << 16)];
/*[]*/
INT_TYPE key_buff2[(1 << 16)];
/*[]*/
INT_TYPE partial_verify_vals[5];
/*[]*/
INT_TYPE test_index_array[5];
/*[]*/
INT_TYPE test_rank_array[5];
/*[]*/
INT_TYPE S_test_index_array[5] = {48427, 17148 , 23627 , 62548 , 4431};
/*[]*/
INT_TYPE S_test_rank_array[5] = {0, 18 , 346 , 64917 , 65463};
/*[]*/
INT_TYPE W_test_index_array[5] = {357773, 934767 , 875723 , 898999 , 404505};
/*[]*/
INT_TYPE W_test_rank_array[5] = {1249, 11698 , 1039987 , 1043896 , 1048018};
/*[]*/
INT_TYPE A_test_index_array[5] = {2112377, 662041 , 5336171 , 3642833 , 4250760};
/*[]*/
INT_TYPE A_test_rank_array[5] = {104, 17523 , 123928 , 8288932 , 8388264};
/*[]*/
INT_TYPE B_test_index_array[5] = {41869, 812306 , 5102857 , 18232239 , 26860214};
/*[]*/
INT_TYPE B_test_rank_array[5] = {33422937, 10244 , 59149 , 33135281 , 99};
/*[]*/
INT_TYPE C_test_index_array[5] = {44172927, 72999161 , 74326391 , 129606274 , 21736814};
/*[]*/
INT_TYPE C_test_rank_array[5] = {61147, 882988 , 266290 , 133997595 , 133525895};
/*[]*/
double randlc(double *X, double *A);
/*[]*/
void full_verify(void );
/*[1; ]*/
/*[1; ]*/
/*[1; ]*/
double randlc(double *X, double *A) {
/*[1; ]*/
    /*[1; ]*/
    static int KS = 0;
    /*[1; ]*/
    static double R23;
    /*[1; ]*/
    static double R46;
    /*[1; ]*/
    static double T23;
    /*[1; ]*/
    static double T46;
    /*[1; ]*/
    double T1;
    /*[1; ]*/
    double T2;
    /*[1; ]*/
    double T3;
    /*[1; ]*/
    double T4;
    /*[1; ]*/
    double A1;
    /*[1; ]*/
    double A2;
    /*[1; ]*/
    double X1;
    /*[1; ]*/
    double X2;
    /*[1; ]*/
    double Z;
    /*[1; ]*/
    int i;
    /*[1; ]*/
    int j;
    /*[1; ]*/
    /*[1; ]*/
    if (KS == 0) {
    /*[1; ]*/
        /*[1; ]*/
        R23 = 1.0;
        /*[1; ]*/
        R46 = 1.0;
        /*[1; ]*/
        T23 = 1.0;
        /*[1; ]*/
        T46 = 1.0;
        /*[1; ]*/
        /*[1; ]*/
        /*[1; ]*/
        /*[1; ]*/
        for (i = 1; i <= 23; i++) {
        /*[1; ]*/
            /*[1; ]*/
            R23 = 0.50 * R23;
            /*[1; ]*/
            T23 = 2.0 * T23;
        }
        /*[1; ]*/
        /*[1; ]*/
        /*[1; ]*/
        /*[1; ]*/
        for (i = 1; i <= 46; i++) {
        /*[1; ]*/
            /*[1; ]*/
            R46 = 0.50 * R46;
            /*[1; ]*/
            T46 = 2.0 * T46;
        }
        /*[1; ]*/
        KS = 1;
    }
    /*[1; ]*/
    T1 = R23 * *A;
    /*[1; ]*/
    j = T1;
    /*[1; ]*/
    A1 = j;
    /*[1; ]*/
    A2 = *A - T23 * A1;
    /*[1; ]*/
    T1 = R23 * *X;
    /*[1; ]*/
    j = T1;
    /*[1; ]*/
    X1 = j;
    /*[1; ]*/
    X2 = *X - T23 * X1;
    /*[1; ]*/
    T1 = A1 * X2 + A2 * X1;
    /*[1; ]*/
    j = R23 * T1;
    /*[1; ]*/
    T2 = j;
    /*[1; ]*/
    Z = T1 - T23 * T2;
    /*[1; ]*/
    T3 = T23 * Z + A2 * X2;
    /*[1; ]*/
    j = R46 * T3;
    /*[1; ]*/
    T4 = j;
    /*[1; ]*/
    *X = T3 - T46 * T4;
    /*[1; ]*/
    return (R46 * *X);
}
/*[1; ]*/
/*[1; ]*/
/*[1; ]*/
void create_seq(double seed, double a) {
/*[1; ]*/
    /*[1; ]*/
    double x;
    /*[1; ]*/
    int i;
    /*[1; ]*/
    int k;
    /*[1; ]*/
    k = (1 << 11) / 4;
    /*[1; ]*/
    /*[1; ]*/
    /*[1; ]*/
    /*[1; ]*/
    for (i = 0; i < (1 << 16); i++) {
    /*[1; ]*/
        /*[1; ]*/
        double *_imopVarPre16;
        /*[1; ]*/
        double *_imopVarPre17;
        /*[1; ]*/
        double _imopVarPre18;
        /*[1; ]*/
        _imopVarPre16 = &a;
        /*[1; ]*/
        _imopVarPre17 = &seed;
        /*[1; ]*/
        _imopVarPre18 = randlc(_imopVarPre17, _imopVarPre16);
        /*[1; ]*/
        /*[1; ]*/
        x = _imopVarPre18;
        /*[1; ]*/
        double *_imopVarPre21;
        /*[1; ]*/
        double *_imopVarPre22;
        /*[1; ]*/
        double _imopVarPre23;
        /*[1; ]*/
        _imopVarPre21 = &a;
        /*[1; ]*/
        _imopVarPre22 = &seed;
        /*[1; ]*/
        _imopVarPre23 = randlc(_imopVarPre22, _imopVarPre21);
        /*[1; ]*/
        /*[1; ]*/
        x += _imopVarPre23;
        /*[1; ]*/
        double *_imopVarPre26;
        /*[1; ]*/
        double *_imopVarPre27;
        /*[1; ]*/
        double _imopVarPre28;
        /*[1; ]*/
        _imopVarPre26 = &a;
        /*[1; ]*/
        _imopVarPre27 = &seed;
        /*[1; ]*/
        _imopVarPre28 = randlc(_imopVarPre27, _imopVarPre26);
        /*[1; ]*/
        /*[1; ]*/
        x += _imopVarPre28;
        /*[1; ]*/
        double *_imopVarPre31;
        /*[1; ]*/
        double *_imopVarPre32;
        /*[1; ]*/
        double _imopVarPre33;
        /*[1; ]*/
        _imopVarPre31 = &a;
        /*[1; ]*/
        _imopVarPre32 = &seed;
        /*[1; ]*/
        _imopVarPre33 = randlc(_imopVarPre32, _imopVarPre31);
        /*[1; ]*/
        /*[1; ]*/
        x += _imopVarPre33;
        /*[1; ]*/
        key_array[i] = k * x;
    }
}
/*[8; 16; ]*/
void full_verify() {
/*[8; 16; ]*/
    /*[8; 16; ]*/
    INT_TYPE i;
    /*[8; 16; ]*/
    INT_TYPE j;
    /*[8; 16; ]*/
    /*[8; 16; ]*/
    /*[8; 16; ]*/
    /*[8; 16; ]*/
    for (i = 0; i < (1 << 16); i++) {
    /*[8; 16; ]*/
        /*[8; 16; ]*/
        key_array[--key_buff_ptr_global[key_buff2[i]]] = key_buff2[i];
    }
    /*[8; 16; ]*/
    j = 0;
    /*[8; 16; ]*/
    /*[8; 16; ]*/
    /*[8; 16; ]*/
    /*[8; 16; ]*/
    for (i = 1; i < (1 << 16); i++) {
    /*[8; 16; ]*/
        /*[8; 16; ]*/
        /*[8; 16; ]*/
        if (key_array[i - 1] > key_array[i]) {
        /*[8; 16; ]*/
            /*[8; 16; ]*/
            j++;
        }
    }
    /*[8; 16; ]*/
    /*[8; 16; ]*/
    if (j != 0) {
    /*[8; 16; ]*/
        /*[8; 16; ]*/
        printf("Full_verify: number of keys out of sort: %d\n", j);
        /*[8; 16; ]*/
    } else {
    /*[8; 16; ]*/
        /*[8; 16; ]*/
        passed_verification++;
    }
}
/*[4; ]*/
/*[4; ]*/
void rank(int iteration) {
/*[4; ]*/
    /*[4; ]*/
    INT_TYPE i;
    /*[4; ]*/
    INT_TYPE k;
    /*[4; ]*/
    11 - 9;
    /*[4; ]*/
    INT_TYPE prv_buff1[(1 << 11)];
    /*[4; ]*/
#pragma omp master
    {
    /*[4; ]*/
        /*[4; ]*/
        key_array[iteration] = iteration;
        /*[4; ]*/
        key_array[iteration + 10] = (1 << 11) - iteration;
        /*[4; ]*/
        /*[4; ]*/
        /*[4; ]*/
        /*[4; ]*/
        for (i = 0; i < 5; i++) {
        /*[4; ]*/
            /*[4; ]*/
            partial_verify_vals[i] = key_array[test_index_array[i]];
        }
        /*[4; ]*/
        /*[4; ]*/
        /*[4; ]*/
        /*[4; ]*/
        for (i = 0; i < (1 << 11); i++) {
        /*[4; ]*/
            /*[4; ]*/
            key_buff1[i] = 0;
        }
    }
    /*[4; ]*/
    // #pragma omp dummyFlush BARRIER_START written([key_buff1.f, key_array.f, partial_verify_vals.f]) read([key_buff2, key_array, key_array.f, key_buff2.f, i])
    /*[4; ]*/
#pragma omp barrier
    /*[5; 8; ]*/
    /*[5; 8; ]*/
    /*[5; 8; ]*/
    /*[5; 8; ]*/
    for (i = 0; i < (1 << 11); i++) {
    /*[5; 8; ]*/
        /*[5; 8; ]*/
        prv_buff1[i] = 0;
    }
    /*[5; 8; ]*/
#pragma omp for nowait
    /*[5; 8; ]*/
    /*[5; 8; ]*/
    /*[5; 8; ]*/
    for (i = 0; i < (1 << 16); i++) {
    /*[5; 8; ]*/
        /*[5; 8; ]*/
        key_buff2[i] = key_array[i];
        /*[5; 8; ]*/
        prv_buff1[key_buff2[i]]++;
    }
    /*[5; 8; ]*/
    /*[5; 8; ]*/
    /*[5; 8; ]*/
    /*[5; 8; ]*/
    for (i = 0; i < (1 << 11) - 1; i++) {
    /*[5; 8; ]*/
        /*[5; 8; ]*/
        prv_buff1[i + 1] += prv_buff1[i];
    }
    /*[5; 8; ]*/
    // #pragma omp dummyFlush CRITICAL_START written([key_buff2.f]) read([key_buff1.f, key_buff1])
    /*[5; 8; ]*/
#pragma omp critical
    {
    /*[5; 8; ]*/
        /*[5; 8; ]*/
        /*[5; 8; ]*/
        /*[5; 8; ]*/
        /*[5; 8; ]*/
        for (i = 0; i < (1 << 11); i++) {
        /*[5; 8; ]*/
            /*[5; 8; ]*/
            key_buff1[i] += prv_buff1[i];
        }
    }
    /*[5; 8; ]*/
    // #pragma omp dummyFlush CRITICAL_END written([key_buff1.f]) read([])
    /*[5; 8; ]*/
    // #pragma omp dummyFlush BARRIER_START written([]) read([key_buff1.f, test_rank_array.f, key_buff1, test_rank_array, passed_verification, partial_verify_vals, partial_verify_vals.f, printf, _imopVarPre35])
    /*[5; 8; ]*/
#pragma omp barrier
    /*[6; ]*/
#pragma omp master
    {
    /*[6; ]*/
        /*[6; ]*/
        /*[6; ]*/
        /*[6; ]*/
        /*[6; ]*/
        for (i = 0; i < 5; i++) {
        /*[6; ]*/
            /*[6; ]*/
            k = partial_verify_vals[i];
            /*[6; ]*/
            int _imopVarPre35;
            /*[6; ]*/
            _imopVarPre35 = 0 <= k;
            /*[6; ]*/
            /*[6; ]*/
            if (_imopVarPre35) {
            /*[6; ]*/
                /*[6; ]*/
                _imopVarPre35 = k <= (1 << 16) - 1;
            }
            /*[6; ]*/
            /*[6; ]*/
            if (_imopVarPre35) {
            /*[6; ]*/
                /*[6; ]*/
                /*[6; ]*/
                switch ('S') {
                /*[]*/
                    /*[6; ]*/
                    /*[6; ]*/
                    case 'S': if (i <= 2) {
                    /*[6; ]*/
                        /*[6; ]*/
                        /*[6; ]*/
                        if (key_buff1[k - 1] != test_rank_array[i] + iteration) {
                        /*[6; ]*/
                            /*[6; ]*/
                            printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                            /*[6; ]*/
                        } else {
                        /*[6; ]*/
                            /*[6; ]*/
                            passed_verification++;
                        }
                    } else {
                    /*[6; ]*/
                        /*[6; ]*/
                        /*[6; ]*/
                        if (key_buff1[k - 1] != test_rank_array[i] - iteration) {
                        /*[6; ]*/
                            /*[6; ]*/
                            printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                            /*[6; ]*/
                        } else {
                        /*[6; ]*/
                            /*[6; ]*/
                            passed_verification++;
                        }
                    }
                    /*[6; ]*/
                    break;
                    /*[]*/
                    /*[]*/
                    case 'W': if (i < 2) {
                    /*[]*/
                        /*[]*/
                        /*[]*/
                        if (key_buff1[k - 1] != test_rank_array[i] + (iteration - 2)) {
                        /*[]*/
                            /*[]*/
                            printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                            /*[]*/
                        } else {
                        /*[]*/
                            /*[]*/
                            passed_verification++;
                        }
                    } else {
                    /*[]*/
                        /*[]*/
                        /*[]*/
                        if (key_buff1[k - 1] != test_rank_array[i] - iteration) {
                        /*[]*/
                            /*[]*/
                            printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                            /*[]*/
                        } else {
                        /*[]*/
                            /*[]*/
                            passed_verification++;
                        }
                    }
                    /*[]*/
                    break;
                    /*[]*/
                    /*[]*/
                    case 'A': if (i <= 2) {
                    /*[]*/
                        /*[]*/
                        /*[]*/
                        if (key_buff1[k - 1] != test_rank_array[i] + (iteration - 1)) {
                        /*[]*/
                            /*[]*/
                            printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                            /*[]*/
                        } else {
                        /*[]*/
                            /*[]*/
                            passed_verification++;
                        }
                    } else {
                    /*[]*/
                        /*[]*/
                        /*[]*/
                        if (key_buff1[k - 1] != test_rank_array[i] - (iteration - 1)) {
                        /*[]*/
                            /*[]*/
                            printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                            /*[]*/
                        } else {
                        /*[]*/
                            /*[]*/
                            passed_verification++;
                        }
                    }
                    /*[]*/
                    break;
                    /*[]*/
                    case 'B': ;
                    /*[]*/
                    int _imopVarPre36;
                    /*[]*/
                    int _imopVarPre37;
                    /*[]*/
                    _imopVarPre36 = i == 1;
                    /*[]*/
                    /*[]*/
                    if (!_imopVarPre36) {
                    /*[]*/
                        /*[]*/
                        _imopVarPre37 = i == 2;
                        /*[]*/
                        /*[]*/
                        if (!_imopVarPre37) {
                        /*[]*/
                            /*[]*/
                            _imopVarPre37 = i == 4;
                        }
                        /*[]*/
                        _imopVarPre36 = _imopVarPre37;
                    }
                    /*[]*/
                    /*[]*/
                    if (_imopVarPre36) {
                    /*[]*/
                        /*[]*/
                        /*[]*/
                        if (key_buff1[k - 1] != test_rank_array[i] + iteration) {
                        /*[]*/
                            /*[]*/
                            printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                            /*[]*/
                        } else {
                        /*[]*/
                            /*[]*/
                            passed_verification++;
                        }
                    } else {
                    /*[]*/
                        /*[]*/
                        /*[]*/
                        if (key_buff1[k - 1] != test_rank_array[i] - iteration) {
                        /*[]*/
                            /*[]*/
                            printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                            /*[]*/
                        } else {
                        /*[]*/
                            /*[]*/
                            passed_verification++;
                        }
                    }
                    /*[]*/
                    break;
                    /*[]*/
                    /*[]*/
                    case 'C': if (i <= 2) {
                    /*[]*/
                        /*[]*/
                        /*[]*/
                        if (key_buff1[k - 1] != test_rank_array[i] + iteration) {
                        /*[]*/
                            /*[]*/
                            printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                            /*[]*/
                        } else {
                        /*[]*/
                            /*[]*/
                            passed_verification++;
                        }
                    } else {
                    /*[]*/
                        /*[]*/
                        /*[]*/
                        if (key_buff1[k - 1] != test_rank_array[i] - iteration) {
                        /*[]*/
                            /*[]*/
                            printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                            /*[]*/
                        } else {
                        /*[]*/
                            /*[]*/
                            passed_verification++;
                        }
                    }
                    /*[]*/
                    break;
                }
            }
        }
        /*[6; ]*/
        /*[6; ]*/
        if (iteration == 10) {
        /*[6; ]*/
            /*[6; ]*/
            key_buff_ptr_global = key_buff1;
        }
    }
}
/*[]*/
/*[]*/
/*[]*/
int main(int argc, char **argv) {
/*[]*/
    /*[]*/
    int _imopVarPre39;
    /*[]*/
    int i;
    /*[]*/
    int iteration;
    /*[]*/
    int nthreads = 1;
    /*[]*/
    double timecounter;
    /*[]*/
    /*[]*/
    /*[]*/
    /*[]*/
    for (i = 0; i < 5; i++) {
    /*[]*/
        /*[]*/
        /*[]*/
        switch ('S') {
        /*[]*/
            /*[]*/
            case 'S': test_index_array[i] = S_test_index_array[i];
            /*[]*/
            test_rank_array[i] = S_test_rank_array[i];
            /*[]*/
            break;
            /*[]*/
            case 'A': test_index_array[i] = A_test_index_array[i];
            /*[]*/
            test_rank_array[i] = A_test_rank_array[i];
            /*[]*/
            break;
            /*[]*/
            case 'W': test_index_array[i] = W_test_index_array[i];
            /*[]*/
            test_rank_array[i] = W_test_rank_array[i];
            /*[]*/
            break;
            /*[]*/
            case 'B': test_index_array[i] = B_test_index_array[i];
            /*[]*/
            test_rank_array[i] = B_test_rank_array[i];
            /*[]*/
            break;
            /*[]*/
            case 'C': test_index_array[i] = C_test_index_array[i];
            /*[]*/
            test_rank_array[i] = C_test_rank_array[i];
            /*[]*/
            break;
        }
    }
    /*[]*/
#pragma omp parallel private(iteration)
    {
    /*[1; ]*/
        /*[1; ]*/
        double _imopVarPre42;
        /*[1; ]*/
        int _imopVarPre43;
        /*[1; ]*/
#pragma omp master
        {
        /*[1; ]*/
            /*[1; ]*/
            ;
            /*[1; ]*/
            printf("\n\n NAS Parallel Benchmarks 2.3 OpenMP C version" " - IS Benchmark\n\n");
            /*[1; ]*/
            /*[1; ]*/
            _imopVarPre39 = (1 << 16);
            /*[1; ]*/
            printf(" Size:  %d  (class %c)\n", _imopVarPre39, 'S');
            /*[1; ]*/
            /*[1; ]*/
            printf(" Iterations:   %d\n", 10);
            /*[1; ]*/
            /*[1; ]*/
            timer_clear(0);
            /*[1; ]*/
            /*[1; ]*/
            create_seq(314159265.00, 1220703125.00);
            /*[1; ]*/
        }
        /*[1; ]*/
        int iteration_imopVarPre75;
        /*[1; ]*/
        iteration_imopVarPre75 = 1;
        /*[1; ]*/
        INT_TYPE i_imopVarPre76;
        /*[1; ]*/
        INT_TYPE k;
        /*[1; ]*/
        11 - 9;
        /*[1; ]*/
        INT_TYPE prv_buff1[(1 << 11)];
        /*[1; ]*/
#pragma omp master
        {
        /*[1; ]*/
            /*[1; ]*/
            key_array[iteration_imopVarPre75] = iteration_imopVarPre75;
            /*[1; ]*/
            key_array[iteration_imopVarPre75 + 10] = (1 << 11) - iteration_imopVarPre75;
            /*[1; ]*/
            /*[1; ]*/
            /*[1; ]*/
            /*[1; ]*/
            for (i_imopVarPre76 = 0; i_imopVarPre76 < 5; i_imopVarPre76++) {
            /*[1; ]*/
                /*[1; ]*/
                partial_verify_vals[i_imopVarPre76] = key_array[test_index_array[i_imopVarPre76]];
            }
            /*[1; ]*/
            /*[1; ]*/
            /*[1; ]*/
            /*[1; ]*/
            for (i_imopVarPre76 = 0; i_imopVarPre76 < (1 << 11); i_imopVarPre76++) {
            /*[1; ]*/
                /*[1; ]*/
                key_buff1[i_imopVarPre76] = 0;
            }
        }
        /*[1; ]*/
        // #pragma omp dummyFlush BARRIER_START written([key_buff1.f, key_array.f, test_rank_array.f, seed, T46, R46, KS, test_index_array.f, partial_verify_vals.f, T23, R23, _imopVarPre39]) read([key_buff2, key_array, key_array.f, key_buff2.f, i_imopVarPre76])
        /*[1; ]*/
#pragma omp barrier
        /*[13; ]*/
        /*[13; ]*/
        /*[13; ]*/
        /*[13; ]*/
        for (i_imopVarPre76 = 0; i_imopVarPre76 < (1 << 11); i_imopVarPre76++) {
        /*[13; ]*/
            /*[13; ]*/
            prv_buff1[i_imopVarPre76] = 0;
        }
        /*[13; ]*/
#pragma omp for nowait
        /*[13; ]*/
        /*[13; ]*/
        /*[13; ]*/
        for (i_imopVarPre76 = 0; i_imopVarPre76 < (1 << 16); i_imopVarPre76++) {
        /*[13; ]*/
            /*[13; ]*/
            key_buff2[i_imopVarPre76] = key_array[i_imopVarPre76];
            /*[13; ]*/
            prv_buff1[key_buff2[i_imopVarPre76]]++;
        }
        /*[13; ]*/
        /*[13; ]*/
        /*[13; ]*/
        /*[13; ]*/
        for (i_imopVarPre76 = 0; i_imopVarPre76 < (1 << 11) - 1; i_imopVarPre76++) {
        /*[13; ]*/
            /*[13; ]*/
            prv_buff1[i_imopVarPre76 + 1] += prv_buff1[i_imopVarPre76];
        }
        /*[13; ]*/
        // #pragma omp dummyFlush CRITICAL_START written([key_buff2.f]) read([key_buff1.f, key_buff1])
        /*[13; ]*/
#pragma omp critical
        {
        /*[13; ]*/
            /*[13; ]*/
            /*[13; ]*/
            /*[13; ]*/
            /*[13; ]*/
            for (i_imopVarPre76 = 0; i_imopVarPre76 < (1 << 11); i_imopVarPre76++) {
            /*[13; ]*/
                /*[13; ]*/
                key_buff1[i_imopVarPre76] += prv_buff1[i_imopVarPre76];
            }
        }
        /*[13; ]*/
        // #pragma omp dummyFlush CRITICAL_END written([key_buff1.f]) read([key_buff1.f, test_rank_array.f, key_buff1, test_rank_array, passed_verification, partial_verify_vals, partial_verify_vals.f, printf, _imopVarPre35])
        /*[13; ]*/
#pragma omp master
        {
        /*[13; ]*/
            /*[13; ]*/
            /*[13; ]*/
            /*[13; ]*/
            /*[13; ]*/
            for (i_imopVarPre76 = 0; i_imopVarPre76 < 5; i_imopVarPre76++) {
            /*[13; ]*/
                /*[13; ]*/
                k = partial_verify_vals[i_imopVarPre76];
                /*[13; ]*/
                int _imopVarPre35;
                /*[13; ]*/
                _imopVarPre35 = 0 <= k;
                /*[13; ]*/
                /*[13; ]*/
                if (_imopVarPre35) {
                /*[13; ]*/
                    /*[13; ]*/
                    _imopVarPre35 = k <= (1 << 16) - 1;
                }
                /*[13; ]*/
                /*[13; ]*/
                if (_imopVarPre35) {
                /*[13; ]*/
                    /*[13; ]*/
                    /*[13; ]*/
                    switch ('S') {
                    /*[]*/
                        /*[13; ]*/
                        /*[13; ]*/
                        case 'S': if (i_imopVarPre76 <= 2) {
                        /*[13; ]*/
                            /*[13; ]*/
                            /*[13; ]*/
                            if (key_buff1[k - 1] != test_rank_array[i_imopVarPre76] + iteration_imopVarPre75) {
                            /*[13; ]*/
                                /*[13; ]*/
                                printf("Failed partial verification: " "iteration %d, test key %d\n", iteration_imopVarPre75, i_imopVarPre76);
                                /*[13; ]*/
                            } else {
                            /*[13; ]*/
                                /*[13; ]*/
                                passed_verification++;
                            }
                        } else {
                        /*[13; ]*/
                            /*[13; ]*/
                            /*[13; ]*/
                            if (key_buff1[k - 1] != test_rank_array[i_imopVarPre76] - iteration_imopVarPre75) {
                            /*[13; ]*/
                                /*[13; ]*/
                                printf("Failed partial verification: " "iteration %d, test key %d\n", iteration_imopVarPre75, i_imopVarPre76);
                                /*[13; ]*/
                            } else {
                            /*[13; ]*/
                                /*[13; ]*/
                                passed_verification++;
                            }
                        }
                        /*[13; ]*/
                        break;
                        /*[]*/
                        /*[]*/
                        case 'W': if (i_imopVarPre76 < 2) {
                        /*[]*/
                            /*[]*/
                            /*[]*/
                            if (key_buff1[k - 1] != test_rank_array[i_imopVarPre76] + (iteration_imopVarPre75 - 2)) {
                            /*[]*/
                                /*[]*/
                                printf("Failed partial verification: " "iteration %d, test key %d\n", iteration_imopVarPre75, i_imopVarPre76);
                                /*[]*/
                            } else {
                            /*[]*/
                                /*[]*/
                                passed_verification++;
                            }
                        } else {
                        /*[]*/
                            /*[]*/
                            /*[]*/
                            if (key_buff1[k - 1] != test_rank_array[i_imopVarPre76] - iteration_imopVarPre75) {
                            /*[]*/
                                /*[]*/
                                printf("Failed partial verification: " "iteration %d, test key %d\n", iteration_imopVarPre75, i_imopVarPre76);
                                /*[]*/
                            } else {
                            /*[]*/
                                /*[]*/
                                passed_verification++;
                            }
                        }
                        /*[]*/
                        break;
                        /*[]*/
                        /*[]*/
                        case 'A': if (i_imopVarPre76 <= 2) {
                        /*[]*/
                            /*[]*/
                            /*[]*/
                            if (key_buff1[k - 1] != test_rank_array[i_imopVarPre76] + (iteration_imopVarPre75 - 1)) {
                            /*[]*/
                                /*[]*/
                                printf("Failed partial verification: " "iteration %d, test key %d\n", iteration_imopVarPre75, i_imopVarPre76);
                                /*[]*/
                            } else {
                            /*[]*/
                                /*[]*/
                                passed_verification++;
                            }
                        } else {
                        /*[]*/
                            /*[]*/
                            /*[]*/
                            if (key_buff1[k - 1] != test_rank_array[i_imopVarPre76] - (iteration_imopVarPre75 - 1)) {
                            /*[]*/
                                /*[]*/
                                printf("Failed partial verification: " "iteration %d, test key %d\n", iteration_imopVarPre75, i_imopVarPre76);
                                /*[]*/
                            } else {
                            /*[]*/
                                /*[]*/
                                passed_verification++;
                            }
                        }
                        /*[]*/
                        break;
                        /*[]*/
                        case 'B': ;
                        /*[]*/
                        int _imopVarPre36;
                        /*[]*/
                        int _imopVarPre37;
                        /*[]*/
                        _imopVarPre36 = i_imopVarPre76 == 1;
                        /*[]*/
                        /*[]*/
                        if (!_imopVarPre36) {
                        /*[]*/
                            /*[]*/
                            _imopVarPre37 = i_imopVarPre76 == 2;
                            /*[]*/
                            /*[]*/
                            if (!_imopVarPre37) {
                            /*[]*/
                                /*[]*/
                                _imopVarPre37 = i_imopVarPre76 == 4;
                            }
                            /*[]*/
                            _imopVarPre36 = _imopVarPre37;
                        }
                        /*[]*/
                        /*[]*/
                        if (_imopVarPre36) {
                        /*[]*/
                            /*[]*/
                            /*[]*/
                            if (key_buff1[k - 1] != test_rank_array[i_imopVarPre76] + iteration_imopVarPre75) {
                            /*[]*/
                                /*[]*/
                                printf("Failed partial verification: " "iteration %d, test key %d\n", iteration_imopVarPre75, i_imopVarPre76);
                                /*[]*/
                            } else {
                            /*[]*/
                                /*[]*/
                                passed_verification++;
                            }
                        } else {
                        /*[]*/
                            /*[]*/
                            /*[]*/
                            if (key_buff1[k - 1] != test_rank_array[i_imopVarPre76] - iteration_imopVarPre75) {
                            /*[]*/
                                /*[]*/
                                printf("Failed partial verification: " "iteration %d, test key %d\n", iteration_imopVarPre75, i_imopVarPre76);
                                /*[]*/
                            } else {
                            /*[]*/
                                /*[]*/
                                passed_verification++;
                            }
                        }
                        /*[]*/
                        break;
                        /*[]*/
                        /*[]*/
                        case 'C': if (i_imopVarPre76 <= 2) {
                        /*[]*/
                            /*[]*/
                            /*[]*/
                            if (key_buff1[k - 1] != test_rank_array[i_imopVarPre76] + iteration_imopVarPre75) {
                            /*[]*/
                                /*[]*/
                                printf("Failed partial verification: " "iteration %d, test key %d\n", iteration_imopVarPre75, i_imopVarPre76);
                                /*[]*/
                            } else {
                            /*[]*/
                                /*[]*/
                                passed_verification++;
                            }
                        } else {
                        /*[]*/
                            /*[]*/
                            /*[]*/
                            if (key_buff1[k - 1] != test_rank_array[i_imopVarPre76] - iteration_imopVarPre75) {
                            /*[]*/
                                /*[]*/
                                printf("Failed partial verification: " "iteration %d, test key %d\n", iteration_imopVarPre75, i_imopVarPre76);
                                /*[]*/
                            } else {
                            /*[]*/
                                /*[]*/
                                passed_verification++;
                            }
                        }
                        /*[]*/
                        break;
                    }
                }
            }
            /*[13; ]*/
            /*[13; ]*/
            if (iteration_imopVarPre75 == 10) {
            /*[13; ]*/
                /*[13; ]*/
                key_buff_ptr_global = key_buff1;
            }
        }
        /*[13; ]*/
        // #pragma omp dummyFlush BARRIER_START written([key_buff_ptr_global, passed_verification]) read([key_array, key_array.f, timer_start, key_buff1, partial_verify_vals, test_index_array.f, test_index_array, printf])
        /*[13; ]*/
#pragma omp barrier
        /*[7; ]*/
#pragma omp master
        {
        /*[7; ]*/
            /*[7; ]*/
            passed_verification = 0;
            /*[7; ]*/
            /*[7; ]*/
            if ('S' != 'S') {
            /*[7; ]*/
                /*[7; ]*/
                printf("\n   iteration\n");
                /*[7; ]*/
            }
            /*[7; ]*/
            timer_start(0);
            /*[7; ]*/
        }
        /*[4; 7; ]*/
        /*[17; 4; 7; ]*/
        /*[17; ]*/
        /*[4; 7; ]*/
        for (iteration = 1; iteration <= 10; iteration++) {
        /*[17; 4; 7; ]*/
            /*[17; 4; 7; ]*/
#pragma omp master
            {
            /*[17; 4; 7; ]*/
                /*[17; 4; 7; ]*/
                /*[17; 4; 7; ]*/
                if ('S' != 'S') {
                /*[17; 4; 7; ]*/
                    /*[17; 4; 7; ]*/
                    printf("        %d\n", iteration);
                    /*[17; 4; 7; ]*/
                }
            }
            /*[17; 7; ]*/
            INT_TYPE i_imopVarPre77;
            /*[17; 7; ]*/
            INT_TYPE k_imopVarPre78;
            /*[17; 7; ]*/
            11 - 9;
            /*[17; 7; ]*/
            INT_TYPE prv_buff1_imopVarPre79[(1 << 11)];
            /*[17; 7; ]*/
#pragma omp master
            {
            /*[17; 7; ]*/
                /*[17; 7; ]*/
                key_array[iteration] = iteration;
                /*[17; 7; ]*/
                key_array[iteration + 10] = (1 << 11) - iteration;
                /*[17; 7; ]*/
                /*[17; 7; ]*/
                /*[17; 7; ]*/
                /*[17; 7; ]*/
                for (i_imopVarPre77 = 0; i_imopVarPre77 < 5; i_imopVarPre77++) {
                /*[17; 7; ]*/
                    /*[17; 7; ]*/
                    partial_verify_vals[i_imopVarPre77] = key_array[test_index_array[i_imopVarPre77]];
                }
                /*[17; 7; ]*/
                /*[17; 7; ]*/
                /*[17; 7; ]*/
                /*[17; 7; ]*/
                for (i_imopVarPre77 = 0; i_imopVarPre77 < (1 << 11); i_imopVarPre77++) {
                /*[17; 7; ]*/
                    /*[17; 7; ]*/
                    key_buff1[i_imopVarPre77] = 0;
                }
            }
            /*[17; 7; ]*/
            // #pragma omp dummyFlush BARRIER_START written([key_buff1.f, key_array.f, passed_verification, partial_verify_vals.f]) read([key_buff2, key_array, key_array.f, key_buff2.f, i_imopVarPre77])
            /*[17; 7; ]*/
#pragma omp barrier
            /*[16; ]*/
            /*[16; ]*/
            /*[16; ]*/
            /*[16; ]*/
            for (i_imopVarPre77 = 0; i_imopVarPre77 < (1 << 11); i_imopVarPre77++) {
            /*[16; ]*/
                /*[16; ]*/
                prv_buff1_imopVarPre79[i_imopVarPre77] = 0;
            }
            /*[16; ]*/
#pragma omp for nowait
            /*[16; ]*/
            /*[16; ]*/
            /*[16; ]*/
            for (i_imopVarPre77 = 0; i_imopVarPre77 < (1 << 16); i_imopVarPre77++) {
            /*[16; ]*/
                /*[16; ]*/
                key_buff2[i_imopVarPre77] = key_array[i_imopVarPre77];
                /*[16; ]*/
                prv_buff1_imopVarPre79[key_buff2[i_imopVarPre77]]++;
            }
            /*[16; ]*/
            /*[16; ]*/
            /*[16; ]*/
            /*[16; ]*/
            for (i_imopVarPre77 = 0; i_imopVarPre77 < (1 << 11) - 1; i_imopVarPre77++) {
            /*[16; ]*/
                /*[16; ]*/
                prv_buff1_imopVarPre79[i_imopVarPre77 + 1] += prv_buff1_imopVarPre79[i_imopVarPre77];
            }
            /*[16; ]*/
            // #pragma omp dummyFlush CRITICAL_START written([key_buff2.f]) read([key_buff1.f, key_buff1])
            /*[16; ]*/
#pragma omp critical
            {
            /*[16; ]*/
                /*[16; ]*/
                /*[16; ]*/
                /*[16; ]*/
                /*[16; ]*/
                for (i_imopVarPre77 = 0; i_imopVarPre77 < (1 << 11); i_imopVarPre77++) {
                /*[16; ]*/
                    /*[16; ]*/
                    key_buff1[i_imopVarPre77] += prv_buff1_imopVarPre79[i_imopVarPre77];
                }
            }
            /*[16; ]*/
            // #pragma omp dummyFlush CRITICAL_END written([key_buff1.f]) read([key_buff1.f, test_rank_array.f, key_buff1, test_rank_array, _imopVarPre35, passed_verification, partial_verify_vals, partial_verify_vals.f, printf])
            /*[16; ]*/
#pragma omp master
            {
            /*[16; ]*/
                /*[16; ]*/
                /*[16; ]*/
                /*[16; ]*/
                /*[16; ]*/
                for (i_imopVarPre77 = 0; i_imopVarPre77 < 5; i_imopVarPre77++) {
                /*[16; ]*/
                    /*[16; ]*/
                    k_imopVarPre78 = partial_verify_vals[i_imopVarPre77];
                    /*[16; ]*/
                    int _imopVarPre35;
                    /*[16; ]*/
                    _imopVarPre35 = 0 <= k_imopVarPre78;
                    /*[16; ]*/
                    /*[16; ]*/
                    if (_imopVarPre35) {
                    /*[16; ]*/
                        /*[16; ]*/
                        _imopVarPre35 = k_imopVarPre78 <= (1 << 16) - 1;
                    }
                    /*[16; ]*/
                    /*[16; ]*/
                    if (_imopVarPre35) {
                    /*[16; ]*/
                        /*[16; ]*/
                        /*[16; ]*/
                        switch ('S') {
                        /*[]*/
                            /*[16; ]*/
                            /*[16; ]*/
                            case 'S': if (i_imopVarPre77 <= 2) {
                            /*[16; ]*/
                                /*[16; ]*/
                                /*[16; ]*/
                                if (key_buff1[k_imopVarPre78 - 1] != test_rank_array[i_imopVarPre77] + iteration) {
                                /*[16; ]*/
                                    /*[16; ]*/
                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i_imopVarPre77);
                                    /*[16; ]*/
                                } else {
                                /*[16; ]*/
                                    /*[16; ]*/
                                    passed_verification++;
                                }
                            } else {
                            /*[16; ]*/
                                /*[16; ]*/
                                /*[16; ]*/
                                if (key_buff1[k_imopVarPre78 - 1] != test_rank_array[i_imopVarPre77] - iteration) {
                                /*[16; ]*/
                                    /*[16; ]*/
                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i_imopVarPre77);
                                    /*[16; ]*/
                                } else {
                                /*[16; ]*/
                                    /*[16; ]*/
                                    passed_verification++;
                                }
                            }
                            /*[16; ]*/
                            break;
                            /*[]*/
                            /*[]*/
                            case 'W': if (i_imopVarPre77 < 2) {
                            /*[]*/
                                /*[]*/
                                /*[]*/
                                if (key_buff1[k_imopVarPre78 - 1] != test_rank_array[i_imopVarPre77] + (iteration - 2)) {
                                /*[]*/
                                    /*[]*/
                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i_imopVarPre77);
                                    /*[]*/
                                } else {
                                /*[]*/
                                    /*[]*/
                                    passed_verification++;
                                }
                            } else {
                            /*[]*/
                                /*[]*/
                                /*[]*/
                                if (key_buff1[k_imopVarPre78 - 1] != test_rank_array[i_imopVarPre77] - iteration) {
                                /*[]*/
                                    /*[]*/
                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i_imopVarPre77);
                                    /*[]*/
                                } else {
                                /*[]*/
                                    /*[]*/
                                    passed_verification++;
                                }
                            }
                            /*[]*/
                            break;
                            /*[]*/
                            /*[]*/
                            case 'A': if (i_imopVarPre77 <= 2) {
                            /*[]*/
                                /*[]*/
                                /*[]*/
                                if (key_buff1[k_imopVarPre78 - 1] != test_rank_array[i_imopVarPre77] + (iteration - 1)) {
                                /*[]*/
                                    /*[]*/
                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i_imopVarPre77);
                                    /*[]*/
                                } else {
                                /*[]*/
                                    /*[]*/
                                    passed_verification++;
                                }
                            } else {
                            /*[]*/
                                /*[]*/
                                /*[]*/
                                if (key_buff1[k_imopVarPre78 - 1] != test_rank_array[i_imopVarPre77] - (iteration - 1)) {
                                /*[]*/
                                    /*[]*/
                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i_imopVarPre77);
                                    /*[]*/
                                } else {
                                /*[]*/
                                    /*[]*/
                                    passed_verification++;
                                }
                            }
                            /*[]*/
                            break;
                            /*[]*/
                            case 'B': ;
                            /*[]*/
                            int _imopVarPre36;
                            /*[]*/
                            int _imopVarPre37;
                            /*[]*/
                            _imopVarPre36 = i_imopVarPre77 == 1;
                            /*[]*/
                            /*[]*/
                            if (!_imopVarPre36) {
                            /*[]*/
                                /*[]*/
                                _imopVarPre37 = i_imopVarPre77 == 2;
                                /*[]*/
                                /*[]*/
                                if (!_imopVarPre37) {
                                /*[]*/
                                    /*[]*/
                                    _imopVarPre37 = i_imopVarPre77 == 4;
                                }
                                /*[]*/
                                _imopVarPre36 = _imopVarPre37;
                            }
                            /*[]*/
                            /*[]*/
                            if (_imopVarPre36) {
                            /*[]*/
                                /*[]*/
                                /*[]*/
                                if (key_buff1[k_imopVarPre78 - 1] != test_rank_array[i_imopVarPre77] + iteration) {
                                /*[]*/
                                    /*[]*/
                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i_imopVarPre77);
                                    /*[]*/
                                } else {
                                /*[]*/
                                    /*[]*/
                                    passed_verification++;
                                }
                            } else {
                            /*[]*/
                                /*[]*/
                                /*[]*/
                                if (key_buff1[k_imopVarPre78 - 1] != test_rank_array[i_imopVarPre77] - iteration) {
                                /*[]*/
                                    /*[]*/
                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i_imopVarPre77);
                                    /*[]*/
                                } else {
                                /*[]*/
                                    /*[]*/
                                    passed_verification++;
                                }
                            }
                            /*[]*/
                            break;
                            /*[]*/
                            /*[]*/
                            case 'C': if (i_imopVarPre77 <= 2) {
                            /*[]*/
                                /*[]*/
                                /*[]*/
                                if (key_buff1[k_imopVarPre78 - 1] != test_rank_array[i_imopVarPre77] + iteration) {
                                /*[]*/
                                    /*[]*/
                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i_imopVarPre77);
                                    /*[]*/
                                } else {
                                /*[]*/
                                    /*[]*/
                                    passed_verification++;
                                }
                            } else {
                            /*[]*/
                                /*[]*/
                                /*[]*/
                                if (key_buff1[k_imopVarPre78 - 1] != test_rank_array[i_imopVarPre77] - iteration) {
                                /*[]*/
                                    /*[]*/
                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i_imopVarPre77);
                                    /*[]*/
                                } else {
                                /*[]*/
                                    /*[]*/
                                    passed_verification++;
                                }
                            }
                            /*[]*/
                            break;
                        }
                    }
                }
                /*[16; ]*/
                /*[16; ]*/
                if (iteration == 10) {
                /*[16; ]*/
                    /*[16; ]*/
                    key_buff_ptr_global = key_buff1;
                }
            }
            /*[16; ]*/
            // #pragma omp dummyFlush BARRIER_START written([key_buff_ptr_global, passed_verification]) read([key_array, key_array.f, key_buff1, partial_verify_vals, test_index_array.f, test_index_array, printf])
            /*[16; ]*/
#pragma omp barrier
        }
        /*[17; 7; ]*/
        // #pragma omp dummyFlush BARRIER_START written([passed_verification]) read([key_array, key_buff2, key_buff1.f, key_array.f, key_buff2.f, key_buff_ptr_global, c_print_results, _imopVarPre42, _imopVarPre43, timer_read, full_verify, printf, nthreads, nullCell, timecounter, passed_verification, timer_stop])
        /*[17; 7; ]*/
#pragma omp barrier
        /*[8; 16; ]*/
#pragma omp master
        {
        /*[8; 16; ]*/
            /*[8; 16; ]*/
            timer_stop(0);
            /*[8; 16; ]*/
            /*[8; 16; ]*/
            timecounter = timer_read(0);
            /*[8; 16; ]*/
            /*[8; 16; ]*/
            full_verify();
            /*[8; 16; ]*/
            /*[8; 16; ]*/
            /*[8; 16; ]*/
            if (passed_verification != 5 * 10 + 1) {
            /*[8; 16; ]*/
                /*[8; 16; ]*/
                passed_verification = 0;
            }
            /*[8; 16; ]*/
            _imopVarPre42 = ((double) (10 * (1 << 16))) / timecounter / 1000000.;
            /*[8; 16; ]*/
            _imopVarPre43 = (1 << 16);
            /*[8; 16; ]*/
            c_print_results("IS", 'S', _imopVarPre43, 0, 0, 10, nthreads, timecounter, _imopVarPre42, "keys ranked", passed_verification, "3.0 structured", "21 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "randlc");
            /*[8; 16; ]*/
        }
    }
}
