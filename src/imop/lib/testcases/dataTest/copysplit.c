int main () {
	int X = 9;
	int diff = 10;
	int diff1 = 20;
#pragma omp parallel
	{
		while (1) {
			if (X < 10) {
#pragma omp single
				{
					diff = diff1;
				}
#pragma omp barrier
				break;
			}
#pragma omp barrier
		}
	}
	X = diff;
}
