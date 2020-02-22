int g1 = 10;
int g2 = 20;
void foo () {
	0+g1;
	l1:
#pragma omp barrier
	1+g2;
}
void bar() {
	g1 = 0;
	l2:
#pragma omp barrier
	g2 = 1;
}
int main() {
#pragma omp parallel
	{
		g1+2;
  		g1 = 20;
		if (3+g1) {
			g2+4;
			foo ();
			5+g2;
		} else {
			g2 = 6;
			g2 = 10;
			l3:
#pragma omp barrier
			g1=7;
		}
		foobar(g1);
		foobar(g2);
		if (8) {
			9+g2;
			bar();
			g1=10;
		} else {
			g1=11+g2;
			l4:
#pragma omp barrier
			12+g1;
		}
		g2=13;
	}
}
