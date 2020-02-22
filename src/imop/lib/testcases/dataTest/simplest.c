int main() {
	int x;
#pragma omp parallel
	{
		int p;
		if (1) {
#pragma omp atomic write
			x = 0;
		} else {
#pragma omp atomic read
			p = x;
		}
#pragma omp barrier
		x;
	}
}
