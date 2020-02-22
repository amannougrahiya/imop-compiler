#include<limits.h>
void foo(int j[5][5]) {}
int main() {
	int (*j)[5];
	foo(j);
}
