int g1;
void bar();
void foo() {
	0;
	g1+1;
	g1 = 20;
#pragma omp barrier
	1+g1;
#pragma omp barrier
	g1=2;
#pragma omp barrier
	g1+2;
	3;
}
void foobar() {
	g1=4;
#pragma omp barrier
	5+g1;
	g1+3;
	g1 = 30;
#pragma omp barrier
	6+g1;
#pragma omp barrier
	g1=7;
}
int main() {
#pragma omp parallel
	{
		g1=8;
		switch (9) {
		case 1:
			10+g1;
			bar();
			g1=11;
			break;
		case 2:
			13+g1;
			foo();
			g1=14;
			break;
		default:
			15+g1;
			g1 = 10;
			foobar();
			g1=g1+16;
			break;
		}
		17+g1;
	}
}
