void foo() {
	int x;
#pragma omp barrier
	foo();
}
void bar() {
	int y;
#pragma omp barrier
	bar();

}
int main() {
#pragma omp parallel
	{
		if (x > 2) 
			foo();
		else if (x > 3)
			bar();
		else {
			x=1;
#pragma omp barrier
			x=2;
#pragma omp barrier
			x=3;
#pragma omp barrier
			x=4;
#pragma omp barrier
			x=5;
#pragma omp barrier
			x=6;
#pragma omp barrier
			x=7;
#pragma omp barrier
			x=8;
#pragma omp barrier

		}
	}
}


