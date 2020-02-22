int main() {
	int x = 10;
	{
		int x = 20;
		{
			int x = 30;
		}
		x =40;
	}
	x = 50;
	{
	}
}
