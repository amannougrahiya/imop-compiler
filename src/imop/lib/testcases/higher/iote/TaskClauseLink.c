int x;
int main() {
	int x;
#pragma omp task if (1)
	{

	}
#pragma omp task
	{
		int x;
	}
}
