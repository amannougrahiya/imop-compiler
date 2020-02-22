int main() {
	int i = 0;
	switch(i) {
		case 0: i = 10;
				break;
		case 1: i = 11;
				l1: i = 12;
				break;
		case 2: goto l1;
		default: l2: i = 13; 
	}
	l3: i = 10;
	goto l1;
}
