#include<stdio.h>
#define SIZE 10
typedef int (*f)(int);
int foo(int a) {
	int i = SIZE;
	i = foo(1) + 1;
	return i;
}
f e(int a) {
	int (*fptr)(int);
	fptr = foo;
}
int main() {
	(e(1))(1);
//	int i;
//	i = i + foo(foo(2));
}
