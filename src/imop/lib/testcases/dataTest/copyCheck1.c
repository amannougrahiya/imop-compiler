int main() {
	int X, Y = 0;
	X = Y;
	if (X < Y) {
		int P;
		P = X + Y;
	} else if (2 * X < Y) {
		Y = X + 3;
		goto L1;
	} else if (3 * X < Y) {
		L1: Y = X;
	}
	if (4 * X < Y) {
		5 != 6;
		l1: X = 10;
		Y = Y + 1;
	} else if (5 * X < Y) {
		int Q = 10;
		Y = Q + 3;
		if (6 * X < Y) {
			Y = X + 4;
		} else {
			Q = Y + 13;
		}
	}
}
