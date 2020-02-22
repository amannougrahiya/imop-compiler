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
		else
			bar();
	}
}


