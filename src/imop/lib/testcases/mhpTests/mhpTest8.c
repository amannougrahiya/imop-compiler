void foo() {
	int x = 10;
	foo();
#pragma omp barrier
}
int foobar() {

}
int bar(){
	foobar();
}
void painter() {
	printf("Something");
}
int main() {
#pragma omp parallel
	{
		int z = 10;
		foo();
		int q = 40;
	}
	foo();
	int y = 20;
	int p = bar();
	printf("%d", p);
	painter();
}
