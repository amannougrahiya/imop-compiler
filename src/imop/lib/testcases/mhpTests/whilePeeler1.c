int bar();
void foo(int i, int y) {
	bar();
}
int main() {
	int i = 10;
	int y = 11;
	i = i + 1;
#pragma omp parallel
	{
		i = i + 3;
#pragma omp barrier
		while (1) {
			i = 10;
#pragma omp barrier
			i = i + 1;
#pragma omp barrier
			y = y + 1;
#pragma omp barrier
			break;
		}
	}
	foo(i, y);
}
