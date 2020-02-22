#include<stdio.h>
void foo(int i, int j) {
	printf("%d, %d\n", i, j);
}
int main () {
	int x = 10, y = 10;
	y = x++;
	// foo(x+1, x++);
	//foo(y, x++);
}
