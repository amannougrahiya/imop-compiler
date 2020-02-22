int main() {
	int x;
#pragma omp parallel
	{
		0;
		int p;
		if (1) {
			2;
			if (3) {
#pragma omp atomic write
				x = 0;
				4;
#pragma omp barrier
				5+x;
			} else {
				x=6;
#pragma omp barrier
				7+x;
			}
			x= 8;
		} else {
			9+x;
			if (x+10) {
				x=11;
#pragma omp barrier
				x=12+x;
			} else {
				13+x;
#pragma omp atomic read
				p = x;
#pragma omp barrier
				x+p;
				14;
			}
			15+x;
		}
		16+x;
	}
	x = 10;
	17+x;
}
