int main() {
	int x;
	{
		int x;
		l1: l2: x = 10;
		l3: x++;
	}
	{
		int x;
		return;
	}
}
