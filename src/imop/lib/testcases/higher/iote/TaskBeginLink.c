int x;
int main() {
#pragma omp task
	{

	}
#pragma omp task
	{
		int x;
	}
}
