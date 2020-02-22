int x;
int main () {
#pragma omp master
	{
		100;
	}
#pragma omp master
	{
		int x;
	}
}
