int main() {
	int x = 10;
#pragma omp parallel
	{
		int localX = 9;
#pragma omp task
		{
			localX = x;
		}
		localX = 11;
	}
	localX = 12;
}
