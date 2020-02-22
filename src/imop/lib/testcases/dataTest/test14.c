static double forcing[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5 + 1];
static double forcing2[12];
double *p;
int *p2;
int x, y;
int *p3 = &y;
int *p4 = p3;
void foo(double arr[]) {
	arr[1] = 10;
	arr = forcing[0][0][0];
}
void bar(double arr[]) {
	arr[2] = 10;
}
int main() {
	p2 = &x;
	forcing2[1] = 10;
	p = forcing2;
	p = &forcing2;
	forcing[0][0][0][0] = 0.0f;
	forcing2[0] = 0.0f;
	(&forcing2)[2] = 1;
	p[0] = 0.0f;
	foo(forcing2);
	*p3 = 3;
	bar(forcing2);
	p3 = &x;
	forcing2[1] = 11;
	*p = 13;
	*p3 = 10;
}
