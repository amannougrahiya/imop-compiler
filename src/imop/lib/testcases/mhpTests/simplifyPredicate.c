int main() {
	int s = 0;
#pragma omp parallel
	{
		int x;
		x = 0;
		while (!x) {
			s = 1;
#pragma omp barrier
			s = 2;
#pragma omp barrier
			s = 3;
#pragma omp barrier
		}
		x++;
	}
	x++;
}
