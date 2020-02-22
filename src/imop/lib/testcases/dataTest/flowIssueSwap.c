int main () {
	int Y = 4;
	int Z;
	Z = Y;
#pragma omp parallel
	{
		if (Y > 4) {
			int t1;
#pragma omp atomic read
			t1 = Z;
		} else {
#pragma omp atomic write
			Y = 10;
		}
#pragma omp barrier
		int t2;
#pragma omp atomic read
		t2 = Z;
	}
}
