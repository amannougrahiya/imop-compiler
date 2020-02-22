int partAdd(int a[5])
{
	int sum = 0;
	int i;
	for(i=0;i<5;i++)
	{
		sum += a[i];
	}
	return sum;
}

int* arr;

void main()
{
	int a[10];
	arr = a;
	int i;
	for(i=0;i<5;i = i + 3)
	{
			a[i] = i;
			partAdd(a[5]);
	}
	int sum1;
	int sum2;
	sum1 = partAdd(a[5]);
	sum2 = partAdd((a+5)[5]);
}
