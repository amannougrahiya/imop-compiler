int x;
int main() {
	int i;
#pragma omp for
	for (i = 0; i < 10; i++) {
		123;
	}
#pragma omp for
	for (i = 0; i < 10; i++) {
		int x;
	}
#pragma omp for
	for (i = 0; i < 10; i++) {
		int y;
		continue;
	}
}
