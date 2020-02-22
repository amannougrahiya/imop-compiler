int x;
int main () {
#pragma omp master
	{
		11;
	}
#pragma omp master
	{
		int x;
	}
}
