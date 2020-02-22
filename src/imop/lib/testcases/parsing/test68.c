#include<omp.h>
int main() {
	//volatile int arr[1000][1000][2];
	//arr[0][0] = 0;
	volatile int sh;
#pragma omp parallel
	{
		long long i, j, k;
		//arr[0][0][0] = 0;
		for(i = 0; i < 5000; i++)
		for(j = 0; j < 100000; j++) {
#pragma omp flush
		}
	}
}
