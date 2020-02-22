int main() {
	int x = 10;
	switch (x) {
	case 0:
		x = 10;
		break;
	case 1:
		x = 20;
		break;
	case 3:
		x = 30;
		break;
	default:
		x = 40;
		break;
	}
	switch (x) {
	case 0:
		x = 50;
	default:
		x = 80;
	}
	switch (x) {
	case 0:
		x = 50;
	case 1:
		x = 60;
	case 3:
		x = 70;
	}
	switch ('a') {
	case 'b':
		x = 11;
		break;
	case 'a':
		x = 12;
		break;
	default:
		x = 13;
	}
	switch (2) {
	case 1:
		x = 11;
		break;
	case 2:
		x = 12;
		break;
	default:
		x = 13;
	}
	switch (3) {
	case 1:
		x = 11;
		break;
	case 2:
		x = 12;
		break;
	}
	switch (3) {
	case 1:
		x = 11;
		break;
	case 2:
		x = 12;
		break;
	default:
		x = 13;
	}
	x = 100;
}
