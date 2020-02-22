#include<omp.h>

int main() {
	int x = 0, copyX = 0;
	int flag = 0;
#pragma omp parallel
	{
		if (!omp_get_thread_num()) {
			x = 10; // If we rename this x to copyX, without renaming the read in the else block,
			        // the program semantics will change.
			flag = 1;
#pragma omp flush
		} else {
			while(flag != 1) {
#pragma omp flush
			}
			x = x + 1;
		}

	}
}
