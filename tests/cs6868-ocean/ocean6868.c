/*
 * =====================================================================================
 *
 *       Filename:  simulate.c
 *
 *    Description:  Code to simulate Ocean currents.
 *
 *        Version:  1.0
 *        Created:  03/03/2018 09:59:42 IST
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  Krishna A, and students of CS6868.
 *
 * =====================================================================================
 */
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <omp.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>


int min(int a, int b) { return a <= b ? a : b; }

int simulate_ocean_currents(double **A, int n, double tol){
	int done = 0;
	double diff; 
	double old;
	int iter = 0;

	double **B, **C;
	B = (double **) malloc(n*sizeof(double*));
	int k;
	for (k = 0; k < n; k++){
		B[k]=(double *) malloc(n*sizeof(double));
		memcpy(B[k], A[k], n*sizeof(double));
	}

	while (!done){
		iter ++;
		diff = 0;      /* init */
		int i, j;
		for (i=1;i<n-1; ++i){ /* skip border elems */
			for (j=1; j<n-1; ++j){ /* skip border elems */
				old = A[i][j];  
				B[i][j] = (A[i][j] + A[i][j-1] + A[i-1][j] + A[i][j+1] + A[i+1][j])/5.0; /*average */
				diff += fabs(B[i][j] - old);
			}
		}
		C = A; A = B; B = C; // exchange.
		if (diff/(n*n) < tol) done = 1;
	}
	return iter;

}
int simulate_ocean_currents_parallel(double **A, int dim, double tol, int procs){
	int done = 0, iter = 0;
	double diff = 0;

	double **B, **C;
	B = (double **) malloc(dim*sizeof(double *));
#pragma omp parallel num_threads(procs) shared(A, B, dim)
	{
		int tid = omp_get_thread_num();
		int start = min(dim, tid*dim/procs);
		int end = min(dim, (tid + 1)*dim/procs);
		int i, j;
		for (i = start; i < end; ++i) {
			B[i] = (double *) malloc (dim*sizeof(double));
			memcpy(B[i], A[i], dim*sizeof(double));
		}
	}

	int chunk = 1 + (dim-3)/procs;
#pragma omp parallel num_threads(procs) firstprivate(done)
	{
		int tid = omp_get_thread_num();
		int start = 1 + min(dim - 2, tid*chunk);
		int end = 1 + min(dim - 2, (tid+1)*chunk);
		double old, mydiff;
		while (!done) {
#pragma omp single
			iter++; diff = 0;
#pragma omp barrier
			mydiff = 0;
			int i, j;
			for (i = start; i < end; ++i) {
				for (j = 1; j < dim-1; ++j) {
					old = A[i][j];
					B[i][j] = (A[i][j] + A[i][j-1] + A[i-1][j] + A[i][j+1] + A[i+1][j])/5.0;
					mydiff += fabs(B[i][j] - old);
				}
			}
#pragma omp atomic
			diff += mydiff;
#pragma omp barrier
			done = diff/(dim*dim) < tol;
#pragma omp single
			{
				C = A; A = B; B = C;
			}
		}
	}

	return iter;
}

/* read input from the standard input, after allocating the array */
double ** read_input (int n){
	double **X;
	X = (double **)malloc(n*sizeof(double*));
	int i, j;
	for (i=0;i<n;++i){
		X[i]=(double *)malloc(n*sizeof(double));
		for (j=0;j<n;++j)
			scanf("%lf",&X[i][j]);
	}

	return X;
}

/* output the final grid. */
void print_output(double **A, int n, int niter){

	printf("Number of iterations = %d\n", niter);

	int i, j;
	for (i=0;i<n;++i){
		for (j=0;j<n;++j)
			printf("%lf ",A[i][j]);
		printf("\n");
	}
	printf("\n");

}

/* Print the time statistics */
void print_statistics(struct timeval start_time,struct timeval end_time)
{
	printf("Start time:\t%lf \n", start_time.tv_sec+(start_time.tv_usec/1000000.0));
	printf("End time:\t%lf\n", end_time.tv_sec+(end_time.tv_usec/1000000.0));
	printf("Total time: \t%lf (s)\n", end_time.tv_sec - start_time.tv_sec + ((end_time.tv_usec - start_time.tv_usec)/1000000.0));
}

/* Error in command line arguments. Print usage and exit. */
void print_usage_and_exit(char *prog){
	fprintf(stderr, "Usage: %s <nprocs> <tol> <-serial|-parallel>\n", prog);
	exit(1);
}


int main(int argc, char **argv){
	struct timeval start_time, end_time; 
	int num_iter = 0;
	double tol;
	double **A;
	int procs;
	int dim;
	if (argc != 4){
		print_usage_and_exit(argv[0]);
	}
	sscanf(argv[1],"%d",&procs);
	sscanf(argv[2],"%lf",&tol);
	char *option = argv[3];

	if (option == NULL || (strcmp(option,"-serial") != 0 &&
				strcmp(option,"-parallel") != 0 ))
		print_usage_and_exit(argv[0]);

	printf("Options: Procs = %d, Tol = %lf, Execution%s\n\n",procs, tol, option);

	// printf("Dimensions = ");
	scanf("%d", &dim);
	A = read_input(dim);

	// Calculate start time 
	gettimeofday(&start_time, NULL);

	if (strcmp(option,"-serial") == 0)
		num_iter=simulate_ocean_currents(A, dim, tol);
	else 
		num_iter=simulate_ocean_currents_parallel(A, dim, tol, procs);

	// Calculate end time 
	gettimeofday(&end_time, NULL);
	// Print Statistics
	print_output(A, dim, num_iter);
	print_statistics(start_time,end_time);

}
