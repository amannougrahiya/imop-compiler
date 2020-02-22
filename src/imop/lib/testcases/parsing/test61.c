int foo()  {
	return 2;
}
int main () {
	int x;
	for (x = foo(); foo() < 20; x+=foo());
}
