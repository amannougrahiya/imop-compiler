int main(int argc, char *argv[]) {
	int i = 0;
#pragma omp atomic update
	i = i + 1;
	switch(i) {
		case -1: if (1) { }
		case 0: if(1) {
					case 6: while(1)
								case 7: return;
				}	
				i = 10;
				break;
		case 1: if(1) i = 11;
				l1: i = 12;
				break;
		case 2: goto l1;
		default: l2: i = 13; 
	}
	l3: i = 10;
	if (1) {
		goto l1;
	}
}
