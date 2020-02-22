struct student {
	int a;
	int *b;
};
void foo(int * arrP, int a, int b) {
	arrP[0] = a + b;
}
int bar(int a, int b, struct student *st) {
#pragma omp parallel private(a)
	{
		a += b;
		a = st->a;
	}
	if (a > b) {
		return a + st.a;
	} else {
		return b;
	}
}

void bar2(int a[], int b) {
#pragma omp parallel
	{
		a[0] += 1;
	}
	a[0] += 1;
	return;
}

int main() {
	struct student st1;
	st1.a = 10;
	st1.b = &a;
	int x = 0;
	int z = 10;
	int y = x++;
//	l: x++;
//	int arr[10];
	if (1 > 2) {
		l3: z = z + x;
		goto l3;
	}
	int i = 0;
//	while(i++ < 10) {
	while (i < 10) {
#pragma omp parallel shared(x)
		{
//		foo(arr, x, y);
			z += y;
			z = 10;
		}
		i++;
	}
	if (z) {
//		goto l;
	}
	int arrZ[10];
	for (;;) {
		z = 11;
		int t;
		if (1) {
			t = bar(z, z + 11, &st1);
		} else {
			bar2(arrZ, z);
		}
		t++;
		return 1;
	}
}
