int main() {
	int x;
#pragma omp parallel
	{
		0;
		if (1) {
			2;
			if (3) {
				x = 0;
				4;
#pragma omp barrier
				5;
			} else {
				6;
#pragma omp barrier
				7;
			}
			8;
		} else {
			9;
			if (10) {
				11;
#pragma omp barrier
				12;
			} else {
				13;
#pragma omp barrier
				x;
				14;
			}
			15;
		}
		16;
	}
	x = 10;
	17;
}
