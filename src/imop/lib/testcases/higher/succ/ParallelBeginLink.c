int main() {
#pragma omp parallel
	{
		int x;
	}
#pragma omp parallel if (1)
	{

	}
#pragma omp parallel
	{

	}
}
