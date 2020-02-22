int * foo(int *a, int *b, int *c) {
	return b;
}
int main() {
	int x;
//	int arr[10];
	int *y;
//	int *z = &x;
	int l, m, n;
	int *z;
//	z = foo (&x, &l, &n);
	z = (int *) malloc(sizeof(int) * 10);

	int i;
	int arr[10];
	for (i = 0; i < 10; i++) {
		arr[i] += 1;
	}
	z = arr;
	int *w, *p;
	if (0) {
		z = p;
		p = w;
	} else {
		w = &x;
	}
	x;
////////
	y = (int *) arr;
	int *p, *q;
	q = &x;
	p = q;
	q = z;
	int *z;
	z = (1 > 10) ? arr : y;
	int *a, *b;
	a = &x;
	int **p;
	p = &a;
	b = *p;

// TEST1:
//	int x;
//	int y;
//	y = x;
//	int arr[10];
//	arr[0] = x;
//	int *z;
//	z = arr;
//	int *p;
//	p = *z;
}
