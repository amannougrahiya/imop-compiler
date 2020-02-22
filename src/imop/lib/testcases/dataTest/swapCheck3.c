int main() {
	int A = 5;
	int B = 10;
	int C;
	int D = 0;
#pragma omp parallel
	{
#pragma omp atomic
		D = D + 1;
		int x = 11;
		while (1) {
#pragma omp single nowait
			{
				l1: C = A;
				l2: A = B;
				l3: B = C;
			}
#pragma omp barrier
#pragma omp master
			{
				l4: D = D + A + B;
			}
#pragma omp barrier
#pragma omp single nowait
			{
				l5: C = A;
				l6: A = B;
				l7: B = C;
			}
#pragma omp barrier
#pragma omp master
			{
				l8: D = D + A + B;
			}
#pragma omp barrier
#pragma omp single nowait
			{
				l9: C = A;
				l10: A = B;
				l11: B = C;
			}
#pragma omp barrier
#pragma omp master
			{
				l12: D = D + A + B;
			}
#pragma omp barrier
#pragma omp single nowait
			{
				l13: C = A;
				l14: A = B;
				l15: B = C;
			}
#pragma omp barrier
#pragma omp master
			{
				l16: D = D + A + B;
			}
#pragma omp barrier
			if (x > 10) {
				break;
			}
		}
	}
}
