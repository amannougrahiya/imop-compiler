struct st {
	int * value;
};
int *fptr1(int *p, char *argv[]) {
	int *((*x)[2])();
	x = 10;
}
int **fptr2(int *p, char *argv[]) {
	int *((*x)[2])();
	x = 10;
}
void *fptr3(int *p, char *argv[]) {
	int *((*x)[2])();
	x = 10;
}
int main() {
	int **p;
	int *r, q;
	r = &q;
	p = &r;
	*p = fptr1();
	fptr2();
	fptr3();
}
