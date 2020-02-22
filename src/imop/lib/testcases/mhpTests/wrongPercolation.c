#include<stdio.h>
#include<omp.h>
int main() {
	int shared = 42;
	int shared2 = 42;
#pragma omp parallel
	{
		if (omp_get_thread_num() == 0) {
			while (1) {
#pragma omp barrier
				if (shared2 > 23) {
#pragma omp atomic write
					shared = 10;
					break;
				}
#pragma omp atomic write
				shared = 0;
			}
		} else {
#pragma omp barrier
			int t = 0;
#pragma omp atomic read
			t = shared;
			printf("%d\n", t);
		}
	}
}
