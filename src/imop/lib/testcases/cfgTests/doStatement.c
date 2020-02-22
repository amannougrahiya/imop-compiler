int main() {
	int x = 10;
	do {
		x--;
	} while (0);
	do {
		x--;
		if (x > 10) {
			break;
		}
	} while (1);
	do {
		x--;
	} while (x != 0);
	x = 20;
}
