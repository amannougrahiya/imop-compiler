int main() {
	int i = 0;
	int arr[500];
	int t1;
	int t2;
	int X = 1;
	int Y;
	if (X == 1) {
		Y = 2;
	} else {
		Y = 3;
	}
	i = 1;
	t1 = 4;
	int jj;
	jj = 10;
	while (i < 4) {
		jj = t1;
		t1 = jj + i;
		i++;
		t2 = t1 + 10;
		tc11: arr[1 * t2 + t1] = 10;
//		tc11: arr[2 * t2 + 1] = 10;
	}
	while (1) {
		t1 = 7;
		for (i = X; i < 15;) {
			t1 = i;
			i++;
			t2 = t1 * 10;
			arr[1 * t2 + t1] = 10;
		}
		t1 = 6;
		for (i = Y; i < 100;) {
			t1 = i;
			i++;
			t2 = t1 + 10;
			arr[1 * t2 + t1] = 10;
		}
		t1 = 5;
		for (i = 1; i < 99; i++) {
			if (i != 99) {
				t1 = i;
			}
			if (t1 < 13) {
				t2 = t1 + 11;
			} else {
				t2 = t1 + 20;
			}
			int x;
			tc12: x = arr[t2 + 1 * t1];
//			tc12: x = arr[2 * t1];
		}
		if (t2 > 0) {
			break;
		}
	}
}
