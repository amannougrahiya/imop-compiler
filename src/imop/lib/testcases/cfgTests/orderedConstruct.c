int main() {
	int x = 10;
#pragma omp parallel
	{
		int localX, localY;
#pragma omp for
		for(localX = 0; local < 10; localX++) {
#pragma omp ordered
			{
				localY = x;
			}
		}
		localX = 20;
	}
	x = 30;
}
