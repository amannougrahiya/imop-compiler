int main() {
	int a = 0, b = 0, c = 0;
	int x = 10, y = 42;

	if (a == b) {
		if (a > 10) { // P'
			if (b > 1) { // P0
				l3: if (c > 2) { // P1
					l1: x = 1; // X
				} else {
					goto l2;
				}
			} else {
				l4: if (a > 15) { // P2
					goto l3;
				} else {
					goto l2;
				}
			}
		} else {
			if (c < b) { // P4
				goto l4;
			} else {
				goto exit;
			}
		}
		l2: y = 1; // Y
	} else {

	}
	exit: ;
}
