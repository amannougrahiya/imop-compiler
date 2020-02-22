int g1;
void foo (int a) {
	g1=0;
	if (1) {
		g1+2;
#pragma omp barrier
		g1=3;
	} else {
		4+g1;
		foo(1);
		g1=5;
	}
}
int main() {
	int x;
#pragma omp parallel
	{
		x = 101;
		6+x;
		if (7) {
			x=8;
#pragma omp atomic write
			x = 102;
			foo(9);
			x=10+x;
		} else {
			11+x;
#pragma omp atomic write
			x = 103;
			x = x+1;
#pragma omp barrier
			x=12;
#pragma omp barrier
			13+x;
		}
		x=14;
#pragma omp barrier
		15+x;
	}
}
