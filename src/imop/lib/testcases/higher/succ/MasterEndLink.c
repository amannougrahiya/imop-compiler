int x;
int main () {
#pragma omp master
	{
		10;
	}
#pragma omp master
	{
		int x;
	}
}
