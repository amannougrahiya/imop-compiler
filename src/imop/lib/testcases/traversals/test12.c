int x = 0;
void foo() {
	if (1) {
		return;
	} else {
		return;
	}
}
int main(int argc, char * argv[]) {
	do {

	} while (2);
	x++;
	int i;
	for (i = 0; i < 10; i++) {

	}
//	foo();
//	return;
//	int i;
//	i = 10;
//#pragma omp master
//	{
//		int i;
//		i++;
//	}
//	foo();
//	l1: l2: 55+i;
//	50+3;
//#pragma omp for
//	for (i = 0; i < 10; i++) {
//		int x;
////		if (1) {
//		x + 13;
//		continue;
////		}
//	}
//
//	l1: 33;
//	if (1) {
//		int x;
//		x + 11;
//	}
//	if (2) {
//		int x;
//		x + 12;
//	} else {
//		int x;
//		x + 13;
//	}
//	if (1) {
//		int x;
//		x+1;
//		return 1;
//	}
//	l1: l2: 31;
//	32;
//	33;
//	int x;
//	if (11) {
//	}
//#pragma omp task if (1) final(1)
//	{
//
//	}
//	if (51) {
//
//	} else {
//
//	}
//
	int k;
	while (k) {

	}
	while (1) {
		int x;
		x + 3;
		continue;
	}
	while (1) {
		if (1) {
			continue;
		} else {
			break;
		}
	}
	while (0) {
		13;
	}
	while (1) {
		break;
	}
//	switch(i) {
//	case 1:
//		11;
//		break;
//	}
//	switch (i) {
//	case 1:
//		11;
//		int x;
//		x + 3;
//		break;
//	case 2:
//		break;
//	}
//	switch (i) {
//	case 1:
//		11;
//		int x;
//		x + 3;
//	case 2:
//		break;
//	}
//	switch (i) {
//	case 1:
//		11;
//		int x;
//		x + 3;
//		break;
//	default:
//		break;
//	}
//	switch (i) {
//	case 1:
//		11;
//		int x;
//		x + 3;
//		break;
//	default:
//		;
//	}
//	if (1) {
//		goto l1;
//	} else if (2){
//		goto l2;
//	}
//	int j = 10;
//	do {
//		int x;
//		x + 33;
//		continue;
//	} while (j++ < 10);
//#pragma omp parallel
//#pragma omp for
//	for (j = 0; j< 10; j++) {
//		continue;
//	}
//	do {
//		int x;
//		x+3;
//		if (1) {
//			break;
//		} else if (2) {
//			continue;
//		} else if (3) {
//			int x;
//			x+13;
//			continue;
//		}
//	} while (x);
//	while (x) {
//		if (1) {
//			break;
//		} else if (2) {
//			continue;
//		} else if (3) {
//			continue;
//		}
//	}
//
//	for (i = 0; i < 10; i++) {
//		int x;
//		x + 2;
//		continue;
//	}
//	for (i = 0; i < 10;) {
//		if (1) {
//			if (11) {
//				int x;
//				x + 3;
//			}
//			continue;
//		}
//		if (12) {
////			if (11) {
//			int i;
//			i + 3;
////			}
//			continue;
//		}
////		if (1) {
////			int x;
////			x + 333;
////			continue;
////		} else if (2) {
////			int x;
////			x + 3;
////			break;
////		}
//	}

//	for (i = 0; i < 10; i++) {
//		int i;
//		i++;
//		if (1) {
//			break;
//		} else if (2) {
//			int x;
//			x + 3;
//			continue;
//		} else if (3) {
//			continue;
//		}
//		111;
//	}
//
//	for (; i < 10; i++) {
//		if (1) {
//			break;
//		} else if (2) {
//			continue;
//		} else if (3) {
//			continue;
//		}
//		111;
//	}
//
//	for (; i < 10;) {
//		int x;
//		x + 3;
//		if (1) {
//			break;
//		} else if (2) {
//			continue;
//		} else if (3) {
//			continue;
//		}
//		111;
//	}
//
//	for (i = 0; i < 10; i++) {
//		if (1) {
//			break;
//		} else if (2) {
//			continue;
//		} else if (3) {
//			continue;
//		}
//		111;
//	}
//
//	do {
//		8;
//	} while (7);
//#pragma omp parallel num_threads(1)
//	{
//		int x = 0;
//		31+x;
//	}
//
//#pragma omp parallel
//	{
//#pragma omp single
//		{
//			int x;
//			x+31;
//		}
//
//#pragma omp task if (1)
//		{
//			int x;
//			x+41;
//		}
//
//#pragma omp master
//		{
//			int x;
//			x+61;
//		}
//
//#pragma omp critical
//		{
//			int x;
//			x+51;
//		}
//
//#pragma omp atomic write
//		x = 61;
//
//#pragma omp ordered
//		{
//			int x;
//			x+71;
//		}
//	}
//
//#pragma omp parallel
//	{
//#pragma omp sections
//		{
//#pragma omp section
//			{
//				int x;
//				x+31;
//			}
//#pragma omp section
//			{
//				int x;
//				x+41;
//			}
//		}
//	}
//////////////////////////
//#pragma omp parallel
//	{
//#pragma omp for
//		for (i = 0; i < 8; i++) {
//			111;
//			if (1) {
//				continue;
//			}
////			if (1) {
////				break;
////			} else if (2) {
////				continue;
////			} else if (3) {
////				continue;
////			}
//		}
//	}
////////////////////////
//
//#pragma omp parallel
//	{
//		3;
//	}
}
