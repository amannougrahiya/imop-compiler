int g1;
void foo () {
	0;
#pragma omp barrier
	1;
}
int main() {
#pragma omp parallel
	{
		int p;
		2;
		if (3) {
			4;
			g1 = 10;
			foo ();
			5;
		} else {
			6;
//#pragma omp atomic read
//			p = g1;
#pragma omp barrier
			7;
		}
		if (8) {
			9;
			foo();
			10;
		} else {
			11;
#pragma omp barrier
			12;
		}
		g1;
		13;
	}
}
