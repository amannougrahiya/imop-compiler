#include<stdio.h>
int printf();
int foo() {
	printf("foo\n");
	return 0;
}
int bar() {
	printf("bar\n");
	return 1;
}
int main() {
	int a = 10;
	int b;
	b = foo() + (bar() && 1);	
}	
