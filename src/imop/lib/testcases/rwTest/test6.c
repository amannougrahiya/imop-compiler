int g1;
void foo () {
	g1=0;
#pragma omp barrier
	1+g1;
}
int main() {
#pragma omp parallel
	{
		int p;
		g1=2;
		if (3) {
			p=4;
			g1 = 10;
			foo ();
			5+g1;
		} else {
			p=6+g1;
//#pragma omp atomic read
//			p = g1;
#pragma omp barrier
			g1=7;
		}
		if (8) {
			9=g1;
			foo();
			g1=10+g1;
		} else {
			11+g1;
#pragma omp barrier
			12=g1;
		}
		g1;
		13;
	}
}
