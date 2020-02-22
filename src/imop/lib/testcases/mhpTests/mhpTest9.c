int sum[10];
int arr[10];
int *p = &sum;
int main() {
	p = &sum;
#pragma omp parallel
	{
		int x; // private
		if (1 > 2) {
			if (x < 1000) {
				p[0] = arr[0];
			}
#pragma omp barrier
			x = sum[1];
		} else {
#pragma omp barrier
		}
	}
}
