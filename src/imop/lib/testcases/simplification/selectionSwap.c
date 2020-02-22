int main() {
	int x;

	// Case 1: If without any else.
	if (x) {
#pragma omp parallel
		{
			int i;
			i = 10;
		}
		x = 5;
	}

	if (x) {
#pragma omp parallel
		{
			int i;
			i = 10;
		}
		x = 5;
	} else {
		x = 3;
	}

	if (x) {
#pragma omp parallel
		{
			int i;
			i = 10;
		}
		x = 5;
	} else {
#pragma omp parallel
		{
			x = 3;
		}
		x = 11;
	}

	if (x) {
		x = 13;
	} else {
#pragma omp parallel
		{
			int i;
			i = 10;
		}
		x = 15;
	}

	if (x) {
#pragma omp parallel
		{
			x = 3;
		}
		x = 13;
	} else {
#pragma omp parallel
		{
			int i;
			i = 10;
		}
		x = 15;
	}
}
