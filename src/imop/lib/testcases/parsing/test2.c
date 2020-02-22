int foo(int i, int j, int k){
	printf("Hello");
}
void bar(int i, int j) {

}
int main(){
	int i;
	i = 10 && 8 || 3;
	i = 5 || 4;
	i = (foo (i+3, i-2, i), 4);
	printf("Something");
	i = 11?22:33;
	bar(i++, i-3);
}

