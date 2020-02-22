int main () {
	int x;
#pragma omp parallel
	{
		0;
		x = 0;
		if (1) {
			2;
			if (3) {
				l1:
#pragma omp atomic write
				x = 10;
				4;
				l2:
#pragma omp barrier
				5;
			} else {
				6;
				l3:
#pragma omp barrier
				7;
			}
			8;
		} else {
			9;
			if (10) {
				11;
			} else {
				12;
			}
			13;
			l4:
#pragma omp barrier
			x;
			14;
		}
		15;
	}
}
