int main() {
	int x = 10;
#pragma omp parallel
	{
		int localX = x;
#pragma omp flush
		localX = 10;
#pragma omp barrier
		localX = 10;
#pragma omp taskwait
		localX = 10;
#pragma omp taskyield
		localX = 10;
	}
	x = 20;
}
