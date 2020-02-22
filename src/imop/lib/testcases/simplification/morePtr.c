static int m1[11];
static int m2[11];
static int m3[11];
int foo(int a) {
	return 0;
}
static void psinv(double ***r, double ***u, int n1, int n2, int n3, double c[4],
		int k) {

}
static void mg3P(double ****u, double ***v, double ****r, double a[4],
		double c[4], int n1, int n2, int n3, int k) {
	int i = foo(3);
	psinv(r[k], u[k], m1[k], m2[k], m3[k], c, k);
}
int main() {
	double ****u, ***v, ****r, a[4], c[4];
	int n1, n2, n3, k;
	mg3P(u, v, r, a, c, n1, n2, n3, k);
}

