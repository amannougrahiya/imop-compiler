int x;
int main () {
#pragma omp critical
	{
		100;
	}
#pragma omp critical
	{
		int x;
	}
}
