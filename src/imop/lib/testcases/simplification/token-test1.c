int main() {
	int arr[1];
	int a, b, c, d, e;
	a = 0;
	b = 1;
	c = 2;
	d = 3;
	e = 4;
	arr[(a++) + -b + &c + *d + e + !(a ^ b) + (a & c) + (a | d) / (~c)
			- (a == b) * (a != c) + (a < c) % (a >= b)] = 0;
	arr[a + b - c * d - e * ++f] = 1;
	arr[a - b - c] = 3;
}
