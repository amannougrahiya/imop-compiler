int g1 = 10;
int g2 = 20;
void foo () {
	0;
	l1:
#pragma omp barrier
	1;
}
void bar() {
	0;
	l2:
#pragma omp barrier
	1;
}
int main() {
#pragma omp parallel
	{
		2;
//		g1 = 20;
		if (3) {
			4;
			foo ();
			5;
		} else {
			6;
			g2 = 10;
			l3:
#pragma omp barrier
			7;
		}
		foobar(g1);
		foobar(g2);
		if (8) {
			9;
			bar();
			10;
		} else {
			11;
			l4:
#pragma omp barrier
			12;
		}
		13;
	}
}
