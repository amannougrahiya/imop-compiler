#include<omp.h>
int main () {
	int s1 = 0, s2 = 0, s3 = 0;
#pragma omp parallel
	{
		int tid = omp_get_thread_num();
		int i;
#pragma omp for
		for (i = 0; i < 10; i++) {

		}
		if (tid % 2 == 0) {
#pragma omp critical
			{
				s1 += tid;
				s2 += tid;
			}
		} else if (tid == 3){
#pragma omp critical
			{
				s1 += tid;
			}
		} else {
#pragma omp critical
			{
				s2 += tid;
			}

#pragma omp critical
			{
				s1 += tid;
			}
		}
	}

}
