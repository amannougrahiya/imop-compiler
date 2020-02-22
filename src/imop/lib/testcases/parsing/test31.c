#include<stdio.h>
#include<omp.h>
int main () {
	int x = 0;
#pragma omp parallel
	{
		int i;
		for (i = 0; i < 4; i++) {
#pragma omp critical
			{
				printf("Iteration %d by Thread %d\n", i, omp_get_thread_num());
			}
#pragma omp task
			{
				printf("Single of iteration %d executed by %d\n", i, omp_get_thread_num());
				#pragma omp atomic update
				x = x + 1;
			}
		}
	}
	printf("Value of x: %d\n", x);
}
