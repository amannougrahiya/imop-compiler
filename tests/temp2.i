int main(int argc, char *argv[]) {
    double mean = 10.0;
    double w[500][500];
#pragma omp parallel shared(w)
    {
        int i, j;
#pragma omp for nowait
        for (i = 1; i < 500 - 1; i++) {
            for (j = 1; j < 500 - 1; j++) {
                w[i][j] = mean;
            }
        }
    }
    return 0;
}
