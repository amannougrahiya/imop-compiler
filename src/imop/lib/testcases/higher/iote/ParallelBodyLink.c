int x;
int main() {
#pragma omp parallel
	{
		int x;
		x = 10;
	}
#pragma omp parallel
	{
		123;
	}
}

