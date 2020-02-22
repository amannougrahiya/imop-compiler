int main() {
	int x = 0;
#pragma omp parallel
	{
		int localX = 10;
#pragma omp atomic update
		x += localX;
		localX += x;
	}
	x = 20;
}	
