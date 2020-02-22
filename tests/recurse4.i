void A();
void B();
void C();
void D();
void E();
void F();
void A(int a) {
	int x = 10;
	if (x < 10) {
		A(5);
	} else if (x < 10) {
		B(4);
	} else if (x < 10) {
		C(4);
	} else if (x < 10) {
		D(4);
//    } else if (x < 10) {
//        E();
//    } else if (x < 10) {
//        F();
	} else {
		return;
	}
}
void B(int b) {
	int x = 10;
	if (x < 10) {
		A(6);
	} else if (x < 10) {
		B(3);
	} else if (x < 10) {
		C(3);
	} else if (x < 10) {
		D(3);
//    } else if (x < 10) {
//        E();
//    } else if (x < 10) {
//        F();
	} else {
		return;
	}
}
void C(int c) {
	int x = 10;
	if (x < 10) {
		A(7);
	} else if (x < 10) {
		B(2);
	} else if (x < 10) {
		C(2);
	} else if (x < 10) {
		D(2);
//    } else if (x < 10) {
//        E();
//    } else if (x < 10) {
//        F();
	} else {
		return;
	}
}
void D(int d) {
	int x = 10;
	if (x < 10) {
		A(8);
	} else if (x < 10) {
		B(1);
	} else if (x < 10) {
		C(1);
	} else if (x < 10) {
		D(1);
//    } else if (x < 10) {
//        E();
//    } else if (x < 10) {
//        F();
	} else {
		return;
	}
}
void E() {
//    int x = 10;
//    if (x < 10) {
//        A();
//    } else if (x < 10) {
//        B();
//    } else if (x < 10) {
//        C();
//    } else if (x < 10) {
//        D();
//    } else if (x < 10) {
//        E();
//    } else if (x < 10) {
//        F();
//    } else {
//        return;
//    }
}
void F() {
//    int x = 10;
//    if (x < 10) {
//        A();
//    } else if (x < 10) {
//        B();
//    } else if (x < 10) {
//        C();
//    } else if (x < 10) {
//        D();
//    } else if (x < 10) {
//        E();
//    } else if (x < 10) {
//        F();
//    } else {
//        return;
//    }
}
int main() {
#pragma omp parallel
	{
		A();
	}
}
