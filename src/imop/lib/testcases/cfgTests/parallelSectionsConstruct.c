int main() {
	int x = 10;
	int localX = 5;
#pragma omp parallel sections
	{
#pragma omp section
		{
			localX = x;
		}
#pragma omp section
		{
			localX = x + 10;
		}
#pragma omp section
		{
			localX = x + 20;
		}
	}
	x = localX;
}
