void foo (int a) {
	0;
	if (1) {
		2;
#pragma omp barrier
		3;
	} else {
		4;
		foo();
		5;
	}
}
int main() {
#pragma omp parallel
	{
		6;
		if (7) {
			8;
			foo(9);
			10;
		} else {
			11;
#pragma omp barrier
			12;
#pragma omp barrier
			13;
		}
		14;
	}
}
