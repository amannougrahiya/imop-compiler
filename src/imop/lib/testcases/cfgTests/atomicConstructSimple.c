int main() {
	int x = 0;
#pragma omp parallel
	{
		int localX = 10;
#pragma omp critical
#pragma omp flush
//#pragma omp atomic update
//		x += localX;
	}
#pragma omp parallel
	{
		int localX = 10;
#pragma omp critical
#pragma omp atomic update
		x += localX;
	}
}	
