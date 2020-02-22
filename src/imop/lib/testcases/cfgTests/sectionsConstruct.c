int main() {
	int x = 10;
#pragma omp parallel
	{
		int localX;
#pragma omp sections
		{
#pragma omp section
 			{
 				localX = 10;
 			}
 #pragma omp section
 			{
 				localX = 5;
 			}
 #pragma omp section
 			{
 				localX = 2;
 			}
 		}
	}
	x = 20;
}
