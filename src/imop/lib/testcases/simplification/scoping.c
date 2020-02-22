int bar() {
	int x = 10;
	{
		int y = 19 + x;
		int z = 11;
		for (;;) {
			z++;
			{
				int x = 11;
				x++;
			}
			z = x - 1;
		}
	}
}
int foo() {
	int a = 10 + bar();
	while (1) {
		a = 10;
		{
			int a;
			a = 15;
		}
		a--;
		break;
	}
}
int main() {
	int x = 10;
	int y = 5;
	int q = 11;
	l1: l2: {
		int z = 10 + x + foo();
		int i;
		l3: l4: i = z + 11 - y - q;
#pragma omp parallel
#pragma omp for
		for (i = 0; i < 100; i++) {
			int x;
			x = 11;
			{
				x = 11;
				l5: {
					int q = 10;
					x = 15 + q;
				}
				x++;
				l6: {
					int p = 0;
					l11: y = y - 1;
#pragma omp critical
					{
						l10: y += p + q;
					}
					p++;
				}
			}
		}
	}
}
