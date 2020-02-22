void bar() {
	0;
#pragma omp barrier
	1;
}
void foo() {
	2;
#pragma omp barrier
	3;
	bar();
	4;
}
int main () {
#pragma omp parallel
	{
		5;
		if (6) {
			7;
			foo();
			8;
		} else {
			9;
#pragma omp barrier
			10;
#pragma omp barrier
			11;
		}
		12;
	}
}
