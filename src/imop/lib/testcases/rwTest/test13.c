int g1 = 0;
int main() {
#pragma omp parallel
	{
		if (1) {
			g1 = 20;
#pragma omp barrier
		} else {
			g1 = 10;
#pragma omp barrier
		}
		g1 = g1 + 1;
	}
}
