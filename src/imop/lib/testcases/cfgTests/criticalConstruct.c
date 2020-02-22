int main() {
	int x = 10;
#pragma omp parallel
	{
		int localX = 20;
#pragma omp critical
		{
			localX += 20;
			localX -= 10;
		}
		localX = x;
	}
	x = 20;
}
