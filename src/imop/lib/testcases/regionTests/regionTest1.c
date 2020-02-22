void foo(int a) {
	bar(a-1);
#pragma omp parallel
	{
		int b;
		bar(a-1);
		b = 20;
	}
}
void bar(int a) {
	if (a > 10) {
		foo(a-1);
	}
}
int main () {
	int x;
#pragma omp parallel
	{
		int x;
		x = 10 + 20;
		foo(x);
	}
	if (x < 10) {
#pragma omp parallel
		{
			int y;
			y = 10;
	//		foo(y);
		}
	}
	else {
#pragma omp parallel
		{
			int z;
			z = 10;
	//		foo(z);
		}
	}
}
