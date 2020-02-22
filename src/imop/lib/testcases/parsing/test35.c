#include<stdio.h>
int foo () {
	printf("Start Foo.\n");
	printf("End Foo.\n");
	return 0;
}
int main() {
	int a;
#pragma omp parallel
	{
		if (!omp_get_thread_num()) {
#pragma omp critical
			{
				printf("Hello World\n");
				sleep(3);
				printf("Why are you so weird, world?\n", a);
			}
		} else {
			int i;
			for (i = 0; i < 3; i++) {
#pragma omp atomic update
				a = a + foo();
			}
		}
	}
	printf("%d", a);
}
