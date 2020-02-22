#include<stdlib.h>
#include<sys/time.h>
#include<stdio.h>
#include<omp.h>
#define SIZE 20
#define THRESHOLD 0.1
int main(int argc, char *argv[]) {
	double threshold = THRESHOLD;
	if (argc == 2) {
		sscanf(argv[1], "%lf", &threshold);
	}
	printf("Threshold: %lf\n", threshold);
	// Getting current time to help randomize the system
	srand(10);

	// Initializing the 2D ocean		
	float arr[SIZE][SIZE];
	int p=0, q=0;
	for(p=0; p < SIZE; p++) {
		for(q = 0; q < SIZE; q++) {
			arr[p][q] = rand() % (SIZE*10);		
		}
	}
	// Printing the Input Ocean
	for(p = 0; p < SIZE; p++) {
		for(q = 0; q < SIZE; q++) {
			fprintf(stderr, "%.2f\t", arr[p][q]);
		}
		fprintf(stderr, "\n");
	} 


	float diff = 0;
	// Propagating the Ocean Waves
#pragma omp parallel shared(diff)
	{
		/**
		  fprintf(stderr, "\n Hello World by Thread # %d\n", omp_get_thread_num());
		 **/
		int done = 0;
		float mydiff = 0;
		int loop = 0;
		struct timeval tvStart, tvStop;
		double start = (double) clock();
		gettimeofday(&tvStart, 0);
		while(!done) {
			loop++;
			mydiff = 0;
			diff = 0;
#pragma omp barrier
			int mymin = (SIZE/omp_get_num_threads())*omp_get_thread_num(); // Assuming SIZE%omp_get_num_threads()==0
			int mymax = mymin + SIZE/omp_get_num_threads();
			int i, j;
			for (i = mymin; i < mymax; i++) { // i spans from  mymin (inclusive) to mymax (exclusive)
				for(j = 0; j < SIZE; j++) {
					float temp = arr[i][j];
					arr[i][j] = 0.2 * (arr[i][j] + ((i+1)>=SIZE?0:arr[i+1][j]) + ((j+1)>=SIZE?0:arr[i][j+1]) + ((i-1)<0?0:arr[i-1][j]) + ((j-1)<0?0:arr[i][j-1]));
					mydiff += arr[i][j] < temp ? temp - arr[i][j] : arr[i][j] -temp;
				}
			}
#pragma omp critical
			{
				diff += mydiff;
			}
#pragma omp barrier
			if(((float)diff)/(SIZE*SIZE) < threshold || loop > 100000) {
				done = 1;
			}
#pragma omp barrier
		}
		double stop = (double)clock();
		gettimeofday(&tvStop, 0);
		fprintf(stdout, "Time taken by thread %d, for %d loops is %ld microseconds.\n",omp_get_thread_num(), loop, 1000000*(tvStop.tv_sec-tvStart.tv_sec) + (tvStop.tv_usec-tvStart.tv_usec));
	}
	for(p = 0; p < SIZE; p++) {
		for(q = 0; q < SIZE; q++) {
			fprintf(stderr, "%.2f\t", arr[p][q]);
		}
		fprintf(stderr, "\n");
	} 
}
















