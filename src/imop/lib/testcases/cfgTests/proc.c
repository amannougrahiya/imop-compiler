int g1 = 10, g2 = 20;
int foo(int a, int b, int c) {
	a = a + b + c;
	a = bar();
	g1 = 10;
	return a;
}

int bar() {
	int z = 10;
	z = foo(z, z, z);
	z = bar();
	return z;
}

int norecurse() {
	g1 = g2++;
	return g1;
}

int main() {
	int x;
	x = 0;
	int y, z;
	y = z = 0;
	x = x + 4;
#pragma omp flush
	x = norecurse();
	if (0) {
		x = foo(x, y, g1);
	} else {
		x = norecurse();
	}
	x = g1;
}
