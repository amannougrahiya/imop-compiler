int x;
int main() {
	int x;
#pragma omp task if (1) final(1)
	{
		11;
	}
#pragma omp task
	{
		int x;
	}
}
