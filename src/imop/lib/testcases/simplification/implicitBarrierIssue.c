void abc() {
}
int main() {
#pragma omp parallel
	{
		int i;
#pragma omp for
		for (i = 0; i < 100; i++) {
			abc();
		}
	}
}
