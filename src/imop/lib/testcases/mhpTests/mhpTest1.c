void foo(int x) {
	int y = 10, z = 20;
	if (x > 2) {
		y = 10;
#pragma omp barrier
		z = y;
	} else {
#pragma omp barrier
	}
	int j = 0;
	for (j = 0; j < 10; j++) {
#pragma omp barrier
	}

}
int main() {
	int y;
	int z = 10;
#pragma omp parallel
	{
		int x = 1;
		int a = 10;
#pragma imop predicate sve
		if (a > 10) {
			if (x > 0) {
				x = x + 1;
#pragma omp barrier
				y = 6 + 3;
			} else {
				x = x + 2;
#pragma omp barrier
				y = x + 4;

			}
		} else {
#pragma omp barrier
#pragma omp barrier
		}
#pragma imop predicate sve
		foo(x); 
#pragma omp barrier
	}
}
