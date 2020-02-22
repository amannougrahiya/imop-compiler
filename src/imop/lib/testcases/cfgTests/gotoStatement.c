int bar() {
	return 4;
}
int foo(int i) {
}
int main() {
	int x = 10, i;
	for (i = 10; i < 12; i++) {
		if (i == 11) {
			goto Label1;
		} else {
			goto Label4;
		}
	}
	x = 20;
	Label1: x = 0 && (1 < 3);
	goto Label2;
	Label4: Label5: goto Label2;

	Label2: {
		x = 10;
		if (1) {
			return 1;
		} else {
			Label3: Cat: {
				int y;
				L1: l2: continue;
				x = 11;
			}
			return foo(bar());
		}
	}
}
