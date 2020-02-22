int g1;
int foo() {
	g1 = 10;
}
int main() {
	int x;
	int y;
	int a;
	x = 10;
	y = 10 + x;
	for (x = 1; x < 10; x++) {
		a = 10;
		y = a;
		if (0) {
			goto l;
		}
	}
	g1 = 3;
	foo();
	l:
	a = y + 2;
	a = y;
}
