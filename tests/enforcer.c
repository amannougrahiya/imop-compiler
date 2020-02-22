int foo () {
	return 3;
}

int main () {
	int x = 10;
#pragma omp parallel
	x++;
#pragma omp for
	for (x = 0; x < 10; x++)
		x = x;
#pragma omp sections
	{
#pragma omp section
		x++;
#pragma omp section
		x+=2;
	}
#pragma omp single
#pragma omp task
#pragma omp parallel for
	for (x = 0; x < 12; x++)
		x = x + 0;
#pragma omp parallel sections
	{
#pragma omp section
		x++;
#pragma omp section
		x+=2;
	}
#pragma omp master
	x++;
#pragma omp critical
	x++;
#pragma omp ordered
	x++;
	if (x == x)
		x++;
	switch (x)
	case 1: x;
	while (x)
		x++;
	do
		x++;
	while (x == x);
	for (x = 10; x < 10; x++)
		x++;
    if (x == x)
        x++;
    else 
        x--;
}
