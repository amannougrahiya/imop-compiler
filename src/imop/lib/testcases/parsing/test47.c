#include<stdio.h>
void foo(int i, int j) {
	printf("%d, %d\n", i, j);
}

int main() {
	int x = 10;
	if (++x == x+1) {
		printf("Yes.\n");
	}
	if (x+1 == ++x) {
		printf("Sure.\n");
	}

	foo((++x), (x+1));
	foo((x+1), (x++));

	x = x + 1;
	if (x == (x+1)) {
		printf("Yes!\n");
	}
	x = x + 1;
	if ((x+1) == x) {
		printf("Sure!\n");
	}
}
