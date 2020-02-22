extern int omp_get_thread_num();
extern int printf(char *[], ...);
int main () {
	int X = 0;
	int Y = 0;
#pragma omp parallel num_threads(2)
	{
		if (omp_get_thread_num() == 0) {
			X = 42;
#pragma omp atomic write
			Y = 1;
		} else {
			int t1;
			while (1) {
#pragma omp atomic read
				t1 = Y;
				if (t1) {
					break;
				}
			}
			int t2 = X;
			printf("t2: %d\n", t2);
		}
	}
}
