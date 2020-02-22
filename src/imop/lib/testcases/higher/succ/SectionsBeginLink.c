int x;
int main() {
#pragma omp sections
	{
#pragma omp section
		{
			111;
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
			111;
		}
	}
#pragma omp sections
	{
	}
}
