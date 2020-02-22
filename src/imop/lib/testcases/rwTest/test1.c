int main () {
	int x;
#pragma omp parallel
	{
		0;
		if (1) {
			2;
			if (3) {
#pragma omp atomic write
				x = 10;
				4;
#pragma omp barrier
				x = x + 5;
			} else {
				6+x;
#pragma omp barrier
				7+x;
			}
			x = 8;
		} else {
			x = 9 + x;
			if (10) {
				11+x;
			} else {
				x=12;
			}
			13+x;
#pragma omp barrier
			x;
			x=14;
		}
		15+x;
	}
}
