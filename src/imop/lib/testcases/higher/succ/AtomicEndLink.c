int x;
int main() {
	int t;
#pragma omp atomic read
	t = x;
#pragma omp atomic update
	x = x + 1;
}
