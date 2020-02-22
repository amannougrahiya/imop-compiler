int printf();
int foo() {
	printf("\nHello");
	return 0;
}
int bar() {
	printf("\nWorld");
	return 0;
}
int main() {
	int a[2][3];
	(a[foo() && 0 ])[bar() && 0] = 10;
}
