#include<stdio.h>
int foo() {return 0;};
int foobar(int i, int j, int k, int l, int m) {printf("%d, %d, %d, %d, %d\n", i, j, k, l, m);}
int f1() {printf("F1\n"); return 2;};
int f2() {printf("F2\n"); return 2;};
int main() {
	int i = 10;
	int a = 3;

	a = 10;
	foobar (a, a++, ((+a)), a++, a);
	a = f1() * f2();

	a = a?f1():a?f2():f1();
}
