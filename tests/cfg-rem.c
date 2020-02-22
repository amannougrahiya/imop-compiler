int main() {
	int x = 0;
	while (x < 10) {
		if (x == 9) {
l1:	continue;
		} else {
			while (x < 5) {
l2:				x += 2;
			}
		}
		x++;
	}
}
