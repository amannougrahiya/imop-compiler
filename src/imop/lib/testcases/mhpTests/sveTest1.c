int main() {
#pragma omp parallel
	{
		int best;
		int j3[10];
		j3[0] = 1;
		best = j3[0];
		if (best) {
			// Do something.
		}
	}
}
