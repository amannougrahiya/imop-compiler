int x;
int main() {
	x = 0;
#pragma omp parallel
	{
		x = 1;
#pragma omp barrier
j: 		x = 2;
#pragma omp barrier
		x = 3;
#pragma omp barrier
g: 		x = 4;
  	 	foo();
	}
}
void foo () {
w: while (x > 0) {
		if (x == 3) {
#pragma omp barrier
			x = 11;
//#pragma omp parallel
			{
				x = 12;
#pragma omp barrier
				x = 13;
			}
			goto g;
		}
	}
g : x = 9;
}
