int bar();
char * foo() {
	int y;
	bar();
#pragma omp barrier
}
char bar() {
	int z;
#pragma omp barrier
	foo();
}
int main() {
#pragma omp parallel
	{	
		int x;
		foo();
		x = 10;
	}
}
