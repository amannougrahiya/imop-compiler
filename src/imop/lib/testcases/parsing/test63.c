struct abc {int a; int b;};
void foo(int a) {};
extern int printf();
extern int prinf() {
	struct abc obj;
	printf(foo(obj.a + 1));
}
int main () {
	struct abc obj;
	printf(foo(obj.a + 1));
}
