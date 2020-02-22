struct { int a;} b;
int foo(int a, int b, ...) {
#pragma omp cancel parallel
	return a;
}
int bar(int a, int b) {
	return b;
}
void pr(char * str) {}
int main() {
	int y = 10;
	int i[4];
	int a = 10;
	int (*fptr[4])(int, int);
	int p[4];
	p[3] = 0;
	fptr[3] = &foo;
	pr("Below");
	i[3] = fptr[3](a * 10, bar(2, p[3]));
#pragma omp parallel
	{

	}
}
