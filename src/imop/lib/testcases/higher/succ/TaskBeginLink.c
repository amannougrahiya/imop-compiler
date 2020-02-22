int x;
int main() {
#pragma omp task if (1)
	{
		11;
	}
#pragma omp task
	{
		12;
	}
#pragma omp task
	{
		int x;
	}
}
