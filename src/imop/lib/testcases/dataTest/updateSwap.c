int main() {
	int A = 10;
	int B;
	B = A;
	B = B + A;
	/*
	 * Testing no-swap for shared update.
	 */
	int C = 10;
	int D;
	D = C;
	int X = D;
#pragma omp parallel
	{
#pragma omp atomic
		D = D + 1;
	}
	/*
	 * Testing swap for private update.
	 */
	int Y = 10;
	int Z;
	Z = Y;
#pragma omp parallel
	{
		int C1 = 10;
		int D1;
		D1 = C1;
		D1;
		D1;
		D1 = D1 + 1;
		if (D1 == 10) {
			int t1;
//#pragma omp barrier
#pragma omp atomic read
			t1 = Z;
		} else {
#pragma omp atomic write
			Y = 11;
//#pragma omp barrier
		}
	}
}
