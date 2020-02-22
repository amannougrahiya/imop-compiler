int main() {
	int i = 0;
	int j = 0;
	int a = 0;
	int b = 0;
	int arr[500];
	for (i = 0; i < 100; i++) {
		int t1 = i;
		int t2 = t1 + 10;
		tc11: arr[1 * t2 + t1] = 10;
	}
	for (j = 0; j < 100; j++) {
		int t1 = j;
		int t2 = t1 + 11;
		int x;
		tc12: x = arr[t2 + 1 * t1];
	}
}
