#include<stdio.h>
int foo() {}
int main () {
	if (1) {
		int i, j;
		for(i = 0; i < 100000L; i++) {
			for(j = 0; j < 1000L; j++) {
				foo();
			}
		}
	}
}
