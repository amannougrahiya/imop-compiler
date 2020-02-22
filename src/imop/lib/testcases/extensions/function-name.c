#include<stdio.h>
int main() {
	char *here = "Line " __LINE__ " of " __FILE__ " in " __FUNCTION__;
	char *where = "Line " __LINE__ " of " __FILE__ " in " __PRETTY_FUNCTION__;
	printf("%s   %s", here, where);
}
