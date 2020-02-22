int main() {
	int x = 5;
#pragma omp parallel
	{	
		int localX;
		localX = x;
	}	
	x = 10;
}
