#include<omp.h>
int main() {
	omp_lock_t a;
	omp_lock_t b;
	omp_init_set_lock(&a);
}
