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
			12;
		}
#pragma omp section
		{
			13;
		}
	}
#pragma omp sections
	{
#pragma omp section
		{
			14;
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
			15;
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
