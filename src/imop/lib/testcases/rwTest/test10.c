int g1 = 10;
void bar() {
	0;
	if (1) {
		g1 = 2;
		l1:
#pragma omp barrier
		3+g1;
	} else {
		g1 = 4;
		l2:
#pragma omp barrier
		5+g1;
		bar();
		6+g1;
	}
	g1=7;
}
void foo() {
	int x = 0;
	8+x;
	if (9) {
		x= 10;
		l3:
#pragma omp barrier
		x=11;
	} else {
		12+x;
		l4:
#pragma omp barrier
		x=13;
		foo();
		14+x;
	}
	x = 15 + x;
}
int main() {
#pragma omp parallel
	{
		16+x;
		if (17) {
			x = 18;
			foo();
			19+x;
		} else {
			x=20;
			bar();
			21+x;
		}
		x = 22+x;
		l5:
#pragma omp barrier
		23+x;
	}
}
