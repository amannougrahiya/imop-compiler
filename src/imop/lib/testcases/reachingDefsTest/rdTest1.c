int main() {
	int i = 0;
	int w;
	int wPrime = 10;
	int u, q;
#pragma omp parallel
	{
		while (1) {
			i++;
#pragma omp single
			{
				w = wPrime;
				u = 11;
			}
			if (i >= 100) {
				break;
			}
			u = wPrime + 1;
			q = w + 1;
			wPrime = w;
		}
		wPrime = w + 1;
	}
}
