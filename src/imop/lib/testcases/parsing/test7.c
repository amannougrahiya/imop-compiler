int foo() {
	printf("hello\n");
}
double bar() {

}
int main() {
	int x = 10;
	printf("%d\n", sizeof(foo() + bar()));
	printf("%d\n", sizeof(bar()));
	printf("%d\n", x);
}
