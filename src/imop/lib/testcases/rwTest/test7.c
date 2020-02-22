int g1;
void bar() {
	g1=0;
#pragma omp barrier
	g1=1;
}
void foo() {
	2+g1;
#pragma omp barrier
	g1=3;
	bar();
	4+g1;
}
int main () {
#pragma omp parallel
	{
		g1=5;
		if (6) {
			g1=7;
			foo();
			8+g1;
		} else {
			g1=9;
#pragma omp barrier
			10+g1;
#pragma omp barrier
			g1=11;
		}
		12+g1;
	}
}
