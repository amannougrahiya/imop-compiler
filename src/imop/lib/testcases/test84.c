void foo(int a[][3]) {

}
int main() {
	int d[3][2][3];
	foo(d[0]);
	int (*p)[3];
	p = d[0];
	foo(p);
}
