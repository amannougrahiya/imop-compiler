int x;
int main() {
#pragma omp task
	{
		14;
	}
#pragma omp task
	{
		{
			int x;
		}
	}
#pragma omp task
	{
		int x;
	}
}
