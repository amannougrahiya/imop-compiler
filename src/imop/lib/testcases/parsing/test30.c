#include<stdio.h>
#include<omp.h>
int g = 10;
void foo() {
	int x;
	x = 10;
}
int main () {
	int x = 0;
#pragma omp parallel private(x)
	{
		int i;
		i = x + i + g;
		foo();
		for (i = 0; i < 4; i++) {
#pragma omp critical
			{
				printf("Iteration %d by Thread %d\n", i, omp_get_thread_num());
			}
#pragma omp single nowait
			{
				printf("Single of iteration %d executed by %d\n", i, omp_get_thread_num());
				#pragma omp atomic update
				x = x + 1;
			}
		}
	}
	printf("Value of x: %d\n", x);
}
