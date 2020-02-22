int bar() {
#pragma omp single nowait
	{

	}
}
int foo() {
	int x = 0;
	while (x < 10) {
#pragma omp single nowait
		{
			shared += 1;
		}
		x++;
		bar();
	}
	return 0;
}
int main() {
	int shared = 0;
#pragma omp parallel
	{
		int x = 0;
		while (x < 10) {
#pragma omp single nowait
			{
				shared += 1;
			}
			x++;
		}
		foo();
	}
}
