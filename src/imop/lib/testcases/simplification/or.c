int foo(int a) {
	int i, j;
	i = foo(1) + 2;
	j = (foo(3));
//	j = foo(foo(1));
	return i;
}
int main() {
}
