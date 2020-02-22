int main() {
	int x;
#pragma omp parallel
	{
		x = 0;
		0;
		if (1) {
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
			}
			8;
		}
		9;
#pragma omp barrier
		10;
	}
}
