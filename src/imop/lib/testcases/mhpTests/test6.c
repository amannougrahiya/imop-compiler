void foo () {
	0;
	l1:
#pragma omp barrier
	1;
}
int main() {
#pragma omp parallel
	{
		2;
		if (3) {
			4;
			foo ();
			5;
		} else {
			6;
			l2:
#pragma omp barrier
			7;
		}
		if (8) {
			9;
			foo();
			10;
		} else {
			11;
			l3:
#pragma omp barrier
			12;
		}
		13;
	}
}
