int x;
int main() {
#pragma omp sections
	{
#pragma omp section
		{
			int x;
		}
	}
}
