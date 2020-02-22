int main() {
		int y;
#pragma omp parallel
	{
		int x = 1;
		if (x > 0) {
			x = x + 1;
#pragma omp barrier
			y = 6 + 3;
		}
		else {
			x = x + 2;
#pragma omp barrier
			y = x + 4;
		}
	}
}
