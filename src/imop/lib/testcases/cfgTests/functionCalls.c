int foobar() {
	int x;
	bar(1, 0);
#pragma omp barrier
	x = 10;
}
int bar(int x, int y) {
	foobar();
}
int foo() {
	int i = 10, j = 20;
	bar(i, j);
//	foobar();
}

int main() {
	int p = 10, q = 30;
	foo();
//	bar(p, q);
//	foobar();
}

