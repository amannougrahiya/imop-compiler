int foo () {return 200;}
int main() {
	int u[120], **q;
	int max1 = 100;
	int max2;
#pragma omp parallel
	{
		int i;
		int j;
		int max2;
		int **p;
		p = q;
		p[0] = u;
		max2 = foo();
#pragma omp for
		for (i = 1; i < max2; i++) {
			tc11: (*(p + (2 * i)))[0] = 100;
		}
#pragma omp barrier
#pragma omp for
		for (i = 0; i < max1; i++) {
			tc12: j = u[2 * i + 1];
		}
	}
}
