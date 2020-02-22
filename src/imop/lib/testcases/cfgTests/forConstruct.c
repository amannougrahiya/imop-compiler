int main() {
	int x = 5;
#pragma omp parallel
	{
		int localX;
		localX = x;
		int i;
		#pragma omp for private(i)
		for(i = 0; i < 10; i++) {
			localX = 10;
		}
		localX = 10;
	}

}
