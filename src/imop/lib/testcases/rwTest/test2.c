int main() {
	int x;
#pragma omp parallel
	{
		0;
		if (1) {
			x = 0;
			2;
#pragma omp barrier
			x;
			3;
		} else {
			4;
			while (5) {
				6;
#pragma omp barrier
				7;
				x = 10;
#pragma omp barrier
			}
			8;
		}
		9;
#pragma omp barrier
		10;
	}
}
