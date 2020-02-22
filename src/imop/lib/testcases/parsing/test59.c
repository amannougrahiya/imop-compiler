int printf(){}
int cat() {
	printf("Hello\n");
}
int (*foo())(){
	int (*ptr)();
	ptr = cat;
	return ptr;
}
int main() {
	(foo());
}
