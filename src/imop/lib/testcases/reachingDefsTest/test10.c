int g1 = 10;
void bar() {
	0;
	if (1) {
		2;
		l1:
#pragma omp barrier
		3;
	} else {
		4;
		l2:
#pragma omp barrier
		5;
		bar();
		6;
	}
	7;
}
void foo() {
	8;
	if (9) {
		10;
		l3:
#pragma omp barrier
		11;
	} else {
		12;
		l4:
#pragma omp barrier
		13;
		foo();
		14;
	}
	15;
}
int main() {
#pragma omp parallel
	{
		16;
		if (17) {
			18;
			foo();
			19;
		} else {
			20;
			bar();
			21;
		}
		22;
		l5:
#pragma omp barrier
		23;
	}
}
