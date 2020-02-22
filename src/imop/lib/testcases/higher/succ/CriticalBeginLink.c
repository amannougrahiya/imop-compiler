int x;
int main () {
#pragma omp critical
	{
		11;
	}
#pragma omp critical
	{
		int x;
	}
}
