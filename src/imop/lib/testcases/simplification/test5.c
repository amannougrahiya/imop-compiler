int main () {
	int i;
	int j = 10;
#pragma omp parallel for ordered default(shared) private(i)
	for(i = 0; i < j; i++) {
#pragma omp atomic update
		i = i + 1;
	}
}
