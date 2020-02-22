int main() {
	int x = 10;
	int y;
	{
		int x = 15;
		y = x;
	}
	x = 5;
}
