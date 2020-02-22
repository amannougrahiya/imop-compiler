int printf(const char *restrict , ...);
extern void timer_clear(int );
extern void timer_start(int );
typedef int INT_TYPE;
int passed_verification;
void create_seq(double seed, double a) {
    int k;
    k = (1 << 11) / 4;
}
void rank(int iteration) {
    INT_TYPE i;
#pragma omp master
    {
        int m1 = 0;
        m1++;
    }
#pragma omp barrier
    int b1 = 0;
    b1++;
#pragma omp barrier
#pragma omp master
    {
        int m2 = 0;
        m2++;
    }
}
int main(int argc, char **argv) {
    int iteration;
    printf(" Iterations:   %d\n", 10);
    timer_clear(0);
    create_seq(314159265.00, 1220703125.00);
#pragma omp parallel
    {
        rank(1);
    }
    passed_verification = 0;
    timer_start(0);
#pragma omp parallel private(iteration)
    {
        for (iteration = 1; iteration <= 10; iteration++) {
            int x = 0;
            x++;
        }
    }
}
