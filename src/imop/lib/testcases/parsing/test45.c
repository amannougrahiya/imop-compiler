#include<stdio.h>
struct temp {
	int a;	
};
int foo () {
	printf("Hello");
	return 2;
}
int bar () {
	printf("World");
	return 3;
}
int main() {
	int x = 10;
	int am = x, b = x++, c = x;
	printf("\n %d, %d, %d", am, b, c);
	printf("\nFurther\n");
	int a[] = {foo(), bar(), x, ++x, x};	
	int i;
	for (i = 0; i < 5; i++) {
		printf("\n%d", a[i]);
	}
}
