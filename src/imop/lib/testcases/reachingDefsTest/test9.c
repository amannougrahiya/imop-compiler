int g1;
void foo (int a) {
	0;
	if (1) {
		2;
#pragma omp barrier
		3;
	} else {
		4;
		foo(0);
		5;
	}
}
int main() {
	int x;
#pragma omp parallel
	{
		x = 101;
		6;
		if (7) {
			8;
#pragma omp atomic write
			x = 102;
			foo(9);
			10;
		} else {
			11;
#pragma omp atomic write
			x = 103;
			x = x;
#pragma omp barrier
			12;
#pragma omp barrier
			13;
		}
		14;
#pragma omp barrier
		15;
	}
}
