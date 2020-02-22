int x;
int main () {
#pragma omp critical
	{

	}
#pragma omp critical
	{
		int x;
	}
}
