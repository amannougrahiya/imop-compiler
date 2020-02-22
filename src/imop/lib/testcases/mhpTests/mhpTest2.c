int foo();
int bar();
int main() {
#pragma omp parallel
	{
		int x;
		x = x + 1;
		x = foo() + bar();

	}

}
