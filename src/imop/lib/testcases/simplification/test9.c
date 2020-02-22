struct student {
	int a;
} st1, st2;
struct student foo() {
//struct student foo(struct student st3) {
//	st2.a = 0;
	return st2;
}
int main() {
//	struct student stMain;
//	stMain.a = 10;
	while (foo())
		;
//	while(foo(&stMain));
	return;
}
