int foo () {
	printf("\nWorld");
	return 1;
}
int bar(int i) {
	return i;
}

int main () {
	int i = 10;
#pragma omp parallel if (bar(foo()))
	{
		printf("\nHello");
	}
}
