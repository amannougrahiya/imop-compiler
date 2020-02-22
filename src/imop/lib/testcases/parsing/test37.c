int main() {
	int a, b, c;
#pragma omp parallel
	{
#pragma omp critical
		{
#pragma omp atomic read
			b = a;
#pragma omp atomic
			a = a + 2; 
#pragma omp critical (name)
			{
				b++;
			}
		}
		a++;
	}
}
