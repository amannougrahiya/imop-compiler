int g1;
void bar();
void foo() {
	0;
	g1;
	g1 = 20;
#pragma omp barrier
	1;
#pragma omp barrier
	2;
#pragma omp barrier
	g1;
	3;
}
void foobar() {
	4;
#pragma omp barrier
	5;
	g1;
	g1 = 30;
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
			g1 = 10;
			foobar();
			16;
			break;
		}
		17;
	}
}
