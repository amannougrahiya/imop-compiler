int g1 = 10;
void foo() {
test: g1++;
}
void bar(int x) {
	int y = 10;
	if (x < 10) {
		bar(x++);
	}
}
void foobar(int x) {
	x = 10;
	int yz = 11;
}
int main() {
	int x = 10;
	int z = 11;
#pragma omp parallel
	{
#pragma omp atomic update
		z = 0;
#pragma omp barrier
		if (x < 9) {
#pragma omp atomic update
			z++;
#pragma omp barrier
		} else {
#pragma omp barrier
#pragma omp atomic update
			z++;
		}
	}
	z = 11;
#pragma omp parallel
	{
#pragma omp atomic update
		z++;
#pragma omp barrier
#pragma omp atomic update
		x++;
	}
	int thisVar = 14;
test: x++;
	  while (x > 8) {
l2: x--;
	if (x == 1) {
thisStmt: x = 3 + x++ + thisVar;
		  foo();
		  int z = 10;
l1: x = 10;
	bar(z);
	}
	bar(10);
	  }
	  do {
		  x--;
		  x = x + 2;
		  x = x - g1;
		  thisVar++;
	  } while (x > 0);
	  while (thisVar++ < 5) {
		  if (thisVar == 10) {
			  continue;
		  }
	  }
}
