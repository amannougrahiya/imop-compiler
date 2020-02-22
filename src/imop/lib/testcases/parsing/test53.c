#include<stdio.h>
int printf(){}
int foo() {
	printf("\nHello");
	return 0;
}
int bar() {
	printf("\nCalled bar");
	return 0; 
}
int main(){
	int (*fooptr[5])();
	fooptr[0] = foo;
	foo(fooptr[bar()](foo()));
}
