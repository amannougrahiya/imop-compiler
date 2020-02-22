int main() {
	int g1 = 0;
#pragma omp parallel
	{
		if (1) {
			g1 = 20;
			l1:
#pragma omp barrier
			g1 + 2;
		} else {
			g1;
			g1 = 10;
			l2:
#pragma omp barrier
			g1 + 3;
		}
		g1 = g1 + 1;
	}
}
