int main() {
#pragma omp parallel
	{
		int i, j, k;
		for (i = 0; i < 100000; i++)
		for (j = 0; j < 100000; j++) {
//#pragma omp barrier
		}
	}
}

