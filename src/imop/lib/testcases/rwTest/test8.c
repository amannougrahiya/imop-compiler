int g1;
void foo (int a) {
	0+g1;
	if (1) {
		g1=2;
#pragma omp barrier
		3+g1;
	} else {
		g1=4;
		foo(3);
		g1+5;
	}
}
int main() {
#pragma omp parallel
	{
		6+g1;
		if (7) {
			g1=8;
			foo(9);
			g1=10;
		} else {
			11+g1;
#pragma omp barrier
			g1=12;
#pragma omp barrier
			g1=13+g1;
		}
		g1=14;
	}
}
