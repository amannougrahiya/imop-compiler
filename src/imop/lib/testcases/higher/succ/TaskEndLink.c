int x;
int main() {
#pragma omp task
	{
		11;
	}
#pragma omp task
	{
		int x;
	}
}
