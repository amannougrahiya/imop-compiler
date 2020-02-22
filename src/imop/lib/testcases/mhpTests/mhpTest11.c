int x1;
int y1;
struct s4 {
	int p;
};
int main() {
	int s2;
#pragma omp parallel
	{
		int shared = 0;
		int x;
		while (1) {
			if (shared) {
				11;
#pragma omp barrier
				l1: x = s2;
				break;
			} else {
				12;
				l2: s2 = 10;
#pragma omp barrier
			}
#pragma omp single
			{
				shared = 1;
			}
		}
	}
}
