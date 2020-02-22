int main() {
#pragma omp parallel if (1) num_threads(2)
	{
		int x;
	}
#pragma omp parallel if (1) num_threads(2)
	{

	}
}
