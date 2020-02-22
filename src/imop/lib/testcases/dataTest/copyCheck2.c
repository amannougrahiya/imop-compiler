int main() {
	int X, Y;
	int P = 10;
	l2: X = P;
	Y = 10;
	X = Y;
	l1: P = X + 1;
	while (1) {
		Y = Y + 1;
		if (X < Y) {
			break;
		}
	}
	P = Y++ + X;
	Y = X + 1 + P;
	while (1) {
		Y = Y + 1;
	}
}
