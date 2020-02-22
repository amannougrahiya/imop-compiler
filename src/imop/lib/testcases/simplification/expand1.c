int main() {
	int X = 0;
	int Y = 10;
	while (1) {
#pragma omp parallel
		{
#pragma omp atomic
			X = X + 1;
#pragma omp atomic
			Y = Y + 1;
		}
		if (Y < 10) {
//			Y = Y + 1;
			break;
		}
	}
	i++;
}
