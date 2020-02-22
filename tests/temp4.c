#include<stdio.h>
#include<stdlib.h>
int main() {
    int *ptr, *ptr2, var;
    for (var = 0; var != 10; var++) {
        ptr = (int *) malloc(5 * sizeof(int));
        ptr2 = (int *) malloc(5 * sizeof(int));
        ptr[0] = var;
        free(ptr);
        ptr = ptr2;
        free(ptr2);
		if (ptr2 == NULL) {
			printf("\n ptr2 is null now.");
		} else {
			printf("\n ptr2 is a dangling pointer to %p", ptr2);
		}
		ptr = NULL;
    }
}
