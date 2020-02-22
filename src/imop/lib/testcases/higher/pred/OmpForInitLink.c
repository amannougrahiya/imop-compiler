int x;
int main() {
	int i;
#pragma omp for
	for (i = 0; i < 10; i++) {

	}
#pragma omp for
	for (i = 0; i < 10; i++) {
		continue;
	}
#pragma omp for
	for (i = 0; i < 10; i++) {
		if (1) {
			continue;
		}
	}
#pragma omp for
	for (i = 0; i < 10; i++) {
		int x;
		x = 10;
	}
}
