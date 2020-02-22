int main() {
	int X = 42;
	int Y = 10;
	int Z = 20;
#pragma omp parallel
	{
		int t1;
		while (1) {
			Z = Y;
			if (X < 10) {
				Y = Y + 1;
#pragma omp barrier
				break;
			}
			t1 = 10;
#pragma omp barrier
			X = Z + Y + 3;
#pragma omp barrier
		}
	}
}
