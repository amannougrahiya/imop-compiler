#include<stdio.h>
int foo(int i, int j) {printf("Foo %d, ", i); return 5;}
int bar() {printf("Bar "); return 5;}
int foobar() {printf("FooBar"); return 5;}
int abc(int i, int j, int k) {printf("%d, %d, %d\n",  i, j, k);return 0;}
void test(){}
int main () {
	int i = 10;
	int c = 0;
	int a = foo (i, 3) + bar() * foobar();
	int b = (++i) + foo(foobar(), bar()) + (++i) + foo(i, 4);
	int d = 12;
	int x = i++, y = i, z = ++x;
	printf("%d, %d\n", ++c, c);

	abc(foo(bar(), foobar()), foobar(), bar());
	c = 0;
	d = 2;
	abc(c, c++, c+d);

	d = 10;
	abc(foo(d, d), foo(d++, 0), bar());
	if (!d) {
		printf("Hello, %d");
	}
	d?test():test();
}
