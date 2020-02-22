void foo();
void max () {
	int z = 0;
	z = 10;
	foo();
	z = 33;
#pragma omp barrier
	z = 15;
}
void bar() {
	int y;
	if (2) {
		y = 6;
		max();
	} else {
#pragma omp barrier

	}
}
void foo () {
	int x = 0;
	if (1) {
		bar();
#pragma omp barrier
		x = 4;
	} else {
		bar();
#pragma omp barrier
		x = 3;
	}
}
int main() {
#pragma omp parallel
	foo();
}
