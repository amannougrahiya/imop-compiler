int x;
int main() {
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
