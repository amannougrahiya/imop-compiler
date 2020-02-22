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

		}
#pragma omp section
		{

		}
	}
#pragma omp sections
	{
#pragma omp section
		{

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
