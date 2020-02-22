int main() {
	int X = 0;
	int Y = 0;
#pragma omp parallel
	{
		int abc;
		while (1) {
			abc = 0 + 3;
#pragma omp atomic
			Y = Y + 1;
#pragma omp barrier
#pragma omp atomic
			Y = Y + 2;
#pragma omp barrier
			if (!abc) {
#pragma omp single nowait
				{
					X = X + 1;
				}
				break;
			}
#pragma omp atomic
			Y = Y + 1;
			X = 1;
		}
	}
}
