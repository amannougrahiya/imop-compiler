#include<stdio.h>
int main () {
	int shared = 0, pri = 0;
#pragma omp parallel private(pri)
	{
		int pri= 0;
#pragma omp atomic update
		shared = shared + 1;
#pragma omp atomic update
		pri = pri + shared++;
	}
	printf ("Shared=%d, Private=%d", shared, pri);
}
