int x;
int main() {
#pragma omp single
	{
		int x;
	}
#pragma omp single
	{
		13;
	}
}
