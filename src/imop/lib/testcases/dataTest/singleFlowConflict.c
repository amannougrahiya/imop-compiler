int main() {
//	int X = 0;
	int *A, *B, *C;
	int p[20], q[20];
	int X = 0;
#pragma omp parallel
	{
#pragma omp atomic
		X = X +1;
#pragma omp single nowait
		{
			C = A;
			A = B;
			B = C;
		}
#pragma omp barrier
		int i = 10;
		B[i] = A[i] + 10;
	}
}
