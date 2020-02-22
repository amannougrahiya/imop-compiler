int foo (int i, int j, int k) {}
int main () {
	int i = 10;
	foo(foo (i++, i, ++i), i, ++i + i ++);
}
