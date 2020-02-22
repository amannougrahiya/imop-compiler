void bar(int py) {
#pragma omp parallel
	{
		printf("%d", py);
	}

}
void foo(void) {
#pragma omp parallel
	{
		int x;
		x = 100000;
	}
	int xyp = 10;
	int z;
	return;
}
int main() {
	foo();
	bar();
	int y;
#pragma omp parallel
	{
//		void (* fptr)();
//		fptr = bar;
//		fptr();
		0;
		if (1) {
			2;
			if (3) {
				4;
#pragma omp barrier
				5;
			} else {
				6;
#pragma omp barrier
				7;
			}
			8;
		} else {
			9;
			if (10) {
				11;
			} else {
				12;
			}
			13;
#pragma omp barrier
			14;
		}
		15;
	}
}
