int x;
int main() {
	do {
		100;
	} while (1);
	do {
		int x;
	} while (1);
	do {
		if (1) {
			continue;
		} else if (2) {
			continue;
		}
	} while (3);
	do {
		if (1) {
			continue;
		} else if (2) {
			continue;
		}
		break;
	} while (4);
	l1: do {
		if (1) {
			continue;
		} else if (2) {
			int x;
			continue;
		} else if (3) {
			break;
		}
	} while (!3);
}
