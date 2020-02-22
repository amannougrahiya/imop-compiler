void foo(int x, int y);
void bar(int a, int b);
void foobar() {
	int p;
	p = 10;
}
void aman() {
	{
		int ** a;
	}
}
void f(int * abc) {
	return;
}
int main() {
	int a;
	foobar(10);
	foo(1, 2);
//	foobar(20);
}
void foo(int x, int y) {
	int i;
	foobar();
	if (1) {
		l: bar(y, x);
	} else {
		j: i = 10;
	}
	f(&i);
	if (1) {
		return;
	} else {
		goto j;
	}
}

void bar(int a, int b) {
	int j;
	if (1) {
		foo(b, a);
	} else {
		j = 20;
	}
}
