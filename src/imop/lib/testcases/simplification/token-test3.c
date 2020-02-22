int main() {
	int X = 0;
	int Y = 1;
#pragma omp parallel
	{
		int i = 0 + X - 1, j = 1;
		if (X < Y) {
			i = 10;
		} else {
			i = i + 34 - j;
		}
	}
}
