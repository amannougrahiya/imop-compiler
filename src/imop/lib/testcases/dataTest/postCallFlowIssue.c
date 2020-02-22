int g1;
static void foo();
int main() {
#pragma omp parallel
	{
		int i = 10;
		if (i < 5) {
			g1 = 5;
			foo();
			foo();
			i = i + 1;
		}
	}
}
static void foo() {
	g1 = g1 + 1;
}
