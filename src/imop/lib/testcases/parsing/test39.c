#include<stdio.h>
int main () {
	int x = 1;
	int y = (++x) + x;
	printf("%d", y);
	#pragma omp parallel single
	{
		printf("Hello");
	}
}
