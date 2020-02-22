int main() {
#pragma omp parallel
	{
		0;
		if (1) {
			2;
#pragma omp barrier
			3;
#pragma omp barrier
			4;
#pragma omp barrier
			5;
		} else {
			6;
			while (7) {
				8;
#pragma omp barrier
				9;
#pragma omp barrier
				10;
			}
			11;
		}
		12;
	}
}
