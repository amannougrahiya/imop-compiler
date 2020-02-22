int G1 = 10;
typedef int hello;
struct student {
	int why;
	char student;
};
int tee(int k) {
	if (k > 12) {
		tee(k - 1);
	}
	return k;
}
void foo(int arg[10], int b[], int d[], struct student st1) {
	static struct student st5;
	int X = 10;
	int A[10];
	struct student1 {
		int why;
		int always;
		char student;
	};
#pragma omp parallel
	{
		int i = 11;
		if (i != 1) {
			return;
		}
		i = i + X + arg[3];
//		l1: arg = A;
	}
	st1 = st5;
}
int bar(int x, int y) {
	static int G1 = 100;
	static int G2 = 200;
#pragma omp barrier
	typedef char hello1;
	typedef char hi1;
	int Y = 11 + y;
	typedef char hru;
	if (Y < 11) {
		l4: G1 = G1 + y + Y;
		return G1 + 179 + y;
	}
	G2 = G2 + y;
	y = tee(y);
	return G2 + y;
}
int main() {
	int arg[10];
	struct student stX;
	int Y = 0;
	int *b, c = 10;
	b = &c;
	int y = 15;
	l2: arg[3] = 11;
	foo(arg, &c, &c, stX);
	{
		int Y = 10 + y;
		typedef int *hi;
#pragma omp parallel
		{
			int t1;
			l3: t1 = bar(5, c);
		}
	}
	Y = arg[1] + G1;
}
