int main() {
	int x = 1;
#pragma omp parallel
	{
		int localX = 2;
#pragma omp master
		{
			localX = x;
		}
		localX = 20;
	}
}
