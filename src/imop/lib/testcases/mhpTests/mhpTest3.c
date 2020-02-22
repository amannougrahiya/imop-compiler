int g;
int foo() {
	int x;
	x = 10;
#pragma omp barrier
	g = 10;
	int y;
	y = 10;
}
int func1() {
	int f11;
	foo();
	int f12;
}
int func2() {
	int f21;
	g;
	foo();
	int f22;
}
int main() {
#pragma omp parallel
	{
		int a = 10;
		if(a > 12)
			func1();
		else
			func2();
	}
}
