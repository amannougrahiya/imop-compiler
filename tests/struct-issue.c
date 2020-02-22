struct Patient {
   int id;
};
void foo(struct Patient **pat) {
}
int main() {
	struct Patient *p1;
	foo (&p1);
}
