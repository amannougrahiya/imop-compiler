int main() {
#pragma omp sections
	{
	}
#pragma omp sections
	{
#pragma omp section
		{
			int x;
		}
	}
#pragma omp sections
	{
#pragma omp section
		{
			100;
		}
#pragma omp section
		{
			101;
		}
	}
#pragma omp sections
	{
#pragma omp section
		{
			103;
		}
	}
#pragma omp sections
	{
#pragma omp section
		{
			int x;
		}
#pragma omp section
		{
			105;
		}
#pragma omp section
		{
			int x;
		}
	}
#pragma omp sections
	{
#pragma omp section
		{
			int x;
		}
#pragma omp section
		{
			int x;
		}
	}
}
