//#include<omp.h>
int main() {
#pragma omp parallel
	{
		int i, x;
		int y;
#pragma omp for nowait
		for(i = 0; i < 10; i++) {
#pragma omp critical (someName)
			printf("1.) Iteration %d with thread %d\n", i, omp_get_thread_num());
			if(omp_get_thread_num()%2 == 0)
				sleep(5);
		}
#pragma omp for nowait
		for(i = 0; i < 10; i++) {
#pragma omp critical
			{
				printf("2.) Iteration %d with thread %d\n", i, omp_get_thread_num());
#pragma omp atomic
				x = x + 1;
#pragma omp atomic update
				x = x + 1;
#pragma omp atomic read
				y = x;
#pragma omp atomic write
				x = y;
#pragma omp atomic capture
				x = y = 10;
			}
		}
#pragma omp atomic
				x = x + 1;
#pragma omp atomic update
				x = x + 1;
#pragma omp atomic read
				y = x;
#pragma omp atomic write
				x = y;
#pragma omp atomic capture
				x = y = 10;
		y++;
	}
}
