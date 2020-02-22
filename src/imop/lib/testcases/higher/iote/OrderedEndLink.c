int x;
int main() {
	int i;
#pragma omp for
	for (i = 0; i < 10; i++) {
#pragma omp ordered
		{
			100;
		}
#pragma omp ordered
		{
			int x;
		}
	}
}
