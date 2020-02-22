int main() {
	int x = 1;
#pragma omp parallel
	{
		int p;
		if (0) {
			while (0) {
#pragma omp atomic read
				p = x;
			}
		} else {
#pragma omp atomic write
			x = 0;
		}
	}
}
