int x;
int main () {
#pragma omp master
	{

	}
#pragma omp master
	{
		int x;
	}
}
