int main() {
	int X; 
#pragma omp parallel
	{
		int i;
#pragma omp sections
		{
#pragma omp section
			X = X + 1;
#pragma omp section
			X = X - 1;
#pragma omp section
			for (i = 0; i < 10; i++)
				X++;
		}
	}
}
