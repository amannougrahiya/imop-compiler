int x;
void foo() {
	return;
}
void foobar() {
	if (1) {
		int x;
		return;
	}
}
void foobar() {
	if (1) {
		return;
	}
}
int feebar() {
	if (1) {
		return 10;
	}
}
int feebar2() {
	if (1) {
		return 10;
	} else {
		int x;
		return 11;
	}
}
void bar() {
	if (1) {
		x + 13;
		l1: return;
	} else {
		return;
	}
}
