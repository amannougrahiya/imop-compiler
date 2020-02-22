int foo(int a[]) {
#pragma omp parallel
	{
		a[0] = 1;
	}
}
int main() {
	int arr[10];
	int x;
	foo(arr);
	x = 10;
}
