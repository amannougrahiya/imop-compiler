void bar();
void foo() {
	0;
#pragma omp barrier
	1;
#pragma omp barrier
	2;
#pragma omp barrier
	3;
}
void foobar() {
	4;
#pragma omp barrier
	5;
#pragma omp barrier
	6;
#pragma omp barrier
	7;
}
int main() {
#pragma omp parallel
	{
		8;
		switch (9) {
		case 1:
			10;
			bar();
			11;
			break;
		case 2:
			13;
			foo();
			14;
			break;
		default:
			15;
			foobar();
			16;
			break;
		}
		17;
	}
}
