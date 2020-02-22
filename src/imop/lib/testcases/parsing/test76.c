int main () {
	int x = 2;
#pragma omp parallel num_threads(2), if(x > 2) default(shared)
	{

	}
}
