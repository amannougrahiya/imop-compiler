int main() {
	int X, Y;
	X = 0;
	Y = 0;
#pragma omp parallel
	{
		int diff;
		diff = 9;
		while (X < 10) {
//		while (1) {
			X = X + 2;
#pragma omp barrier
			if (diff < 10) {
				break;
			}
			Y = Y + 1;
			X = X + 1;
#pragma omp barrier
			Y = Y + 2;
#pragma omp barrier
		}
		X = Y + 10;
	}
}
//int main() {
//	int X, Y;
//	X = 0;
//	Y = 0;
//#pragma omp parallel
//	{
//		int diff;
//		diff = 9;
//		while (X < 10) {
//			X = X + 2;
//#pragma omp barrier
//			if (diff < 10) {
//				break;
//			}
//			Y = Y + 1;
//			X = X + 1;
//#pragma omp barrier
//			Y = Y + 2;
//#pragma omp barrier
//		}
//		X = Y + 10;
//	}
//}
