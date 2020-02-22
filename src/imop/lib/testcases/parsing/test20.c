int main() {
	typedef int unsigned (*foo)[10];
	foo xyz(int, int);
	//int (far[])(int, int);
}
int foobar(int (*)(int, int), int);
