struct am {
	int a;
}; 
struct am bar() { // function returning struct am.
	struct am hi;
	return hi;
}
struct am (* caller)(); // pointer to function returning struct am.
struct am (*weird(int something))() { // function that takes an int and returns pointer to function returning struct am.
	return caller;
}
int foo() {
	return 0;
}
int main() {
	{
		struct am4 {
			int x;
		} p; 
		int a3;
		typedef struct {
			int x5;
		} x3, x4;
	}
	int a = foo(), b = 10 && 11;
	caller = bar;
	int hello = 0 > 1? (weird+0)(10+2)().a : 3;
l1: a + hello + b;
#pragma omp master
	a = b;
#pragma omp single
	{
		int x = 0;
		x++;
	}
#pragma omp for
	for (a = 10; a < 11; a++) {
		a = a + 1;
	}
}
int intfoo(int arg) {
	return arg;
}
int simplify() {
	int a = foo(), b = 10 && 11;
	int c, d;
	c = 2 || 3;
	d = 3 && 4;
	c = (3,4);
	c = 3 ? 0:1;
	c++;
	c + foo();
	intfoo(2 + 3);
	foo();
	return 0;
}
