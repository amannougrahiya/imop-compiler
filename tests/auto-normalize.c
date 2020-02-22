int foo () {
	return 0;
}
int main() {
	int x = 10;
	int y = 11;
	while (x < y) {
		if (x == 11) {
			continue;
		}
		x++;
	}
}
