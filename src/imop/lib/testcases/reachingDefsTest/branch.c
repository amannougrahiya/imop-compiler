int main() {
	int x = 10;
	int z = 11;
	int y;
	if (x > 20) {
		y = x;
		x = 12;
	}
	else {
		x = 13 + x++;
	}
	y = x + z;
}
