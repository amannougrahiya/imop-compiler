int foo(int x) {
	return 15;
}
int main() {
	int x = 0;
#pragma omp parallel
	{
#pragma omp atomic
		x += foo(foo(0));
	}
}
