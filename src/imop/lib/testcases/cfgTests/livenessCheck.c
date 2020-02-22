int main () {
	int shared;
	int dummy;
#pragma omp parallel
	{
		int local;
		if (1 > 2) {
			local = 2;
#pragma omp atomic write
			shared = 1;
			local = local + 5;
#pragma omp barrier
		} else {
			int local1;
#pragma omp barrier
#pragma omp atomic read
			local1 = shared;
			local = 14;
			local = local + 1;
		}
	}

}
