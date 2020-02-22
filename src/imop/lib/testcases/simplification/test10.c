struct student {
	int a;
} st1, st2;
struct student foo() {
	return st1;
}
struct student bar(struct student * st4) {
	return foo();
}
int main() {
	struct student st3 = bar(&st2);
	st3.a = 10;
	st2.a = st3.a;
	struct student * st5 = &st2;
	struct student st4 = bar(st5);
	st4.a = 14;
}
