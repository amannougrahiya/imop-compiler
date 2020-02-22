int foo()  {
	return 2;
}
int main () {
	int x;
	for (x = 0; foo() < 20; x+=foo());
}
