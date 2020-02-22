int main() {
	int x = 10;
	do {
	if (1) {
		int j;
		if (x > 10) {
			x = 10;
		} else {
			x = 11;
		}
		x;
		x = x + 1;
	}
	} while(2*3+5 > x);
	for (x = 10; x < 20 && x; x++) {
		int p;
		if (x > 10) {
			p = 10;
		} else {
			p = 11;
		}
		lab1: x = x + 1;
	}
	while(2*3+5 > x) {
		int k;
		if (x > 10) {
			lab2: x = 10;
		} else {
			x = 11;
			goto lab1;
		}
		x = x + 1;
		goto lab2;
	}
}
