int x;
int main() {
#pragma omp parallel
	{
		int x;
		123;
	}
#pragma omp parallel
	{
		123;
	}
}
