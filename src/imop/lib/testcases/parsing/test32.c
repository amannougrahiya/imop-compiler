#include<stdio.h>
#include<omp.h>
#include<unistd.h>
int main () {
	int x = 0;
#pragma omp parallel
	{
		int i;
		for (i = 0; i < 4; i++) {
#pragma omp single nowait
			{
				printf("Starting omp-single of iteration %d executed by %d\n", i, omp_get_thread_num());
				#pragma omp atomic update
				x = x + 1;
				if (i == 1) {
					sleep(1);
				}
				printf("Finishing omp-single of iteration %d executed by %d\n", i, omp_get_thread_num());
			}
		}
	}
	printf("Value of x: %d\n", x);
}
