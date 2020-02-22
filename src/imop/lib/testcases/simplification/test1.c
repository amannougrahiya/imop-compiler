int foo(int argc) {
	return argc;
}
int main () {
	int i;
	int j;
	i = 10;
	j = i && i;
	{
		int i;
	}
#pragma omp parallel if (foo(1))
	{

	}
}
