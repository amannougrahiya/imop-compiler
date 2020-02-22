#include<stdio.h>
int foo(int i) {printf("%d\n", i);return 10;}
int main() {
	int i = 10;
	int j = i + foo(++i);
	printf("%d, %d\n", j, i);
}
