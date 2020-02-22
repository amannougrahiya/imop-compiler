void foo1();
void foo2();
void foo3();
int main() {
#pragma omp parallel
	{
		foo1();
	}
#pragma omp parallel
	{
		foo2();
	}
#pragma omp parallel
	{
		foo3();
	}
}
