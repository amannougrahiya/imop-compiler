int main() {
	int x = 10;
#pragma omp parallel
	{
		int localX = 2;
#pragma omp single
		{
			x = x + 10;
		}
		localX = x;
	}
	x = 30;
}
