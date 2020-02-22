#include<stdio.h>
int foo() {
	printf("Hello\n");
	return 0;
}
int bar() {
	printf("Called bar\n");
	return 0; 
}
int main(){
	int a[2][3];
	a[bar()][1 && foo()] = 0;
}
