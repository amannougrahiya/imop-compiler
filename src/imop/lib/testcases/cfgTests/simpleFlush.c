int main() {
	int x;
	{
		int x;
#pragma omp flush
	}
	{
		int x;
#pragma omp flush
	}
}
