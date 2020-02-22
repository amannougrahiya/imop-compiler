union ut3 {
	int ut3i;
	int ut33i;
} utName, utName2;
typedef struct st3 {
	int st3i;
} stName, stName2;
stName s;
struct test {
	int itest;
	int itest2;
	struct test5 {
		int t1;
		int t2;
	} tt, ttt;
};
int g1;
typedef struct test t;
struct st {
	int i;
	struct test t1;
} u, v, p;
struct st w;
struct st2 {
	int i;
} u2, v2;
int main() {
	struct st i;
	i = v;
	v = u;
	u = w;
	struct st2 i2;
	i2 = v2;
}
