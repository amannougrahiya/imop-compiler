void A();
void B();
void C();
void D();
void E();
void F();
void A() {
    int x = 10;
    if (x < 10) {
        A();
    } else if (x < 10) {
        B();
    } else if (x < 10) {
        C();
    } else if (x < 10) {
        D();
    } else if (x < 10) {
        E();
    } else if (x < 10) {
        F();
    } else {
        return;
    }
}
void B() {
    int x = 10;
    if (x < 10) {
        A();
    } else if (x < 10) {
        B();
    } else if (x < 10) {
        C();
    } else if (x < 10) {
        D();
    } else if (x < 10) {
        E();
    } else if (x < 10) {
        F();
    } else {
        return;
    }
}
void C() {
    int x = 10;
    if (x < 10) {
        A();
    } else if (x < 10) {
        B();
    } else if (x < 10) {
        C();
    } else if (x < 10) {
        D();
    } else if (x < 10) {
        E();
    } else if (x < 10) {
        F();
    } else {
        return;
    }
}
void D() {
    int x = 10;
    if (x < 10) {
        A();
    } else if (x < 10) {
        B();
    } else if (x < 10) {
        C();
    } else if (x < 10) {
        D();
    } else if (x < 10) {
        E();
    } else if (x < 10) {
        F();
    } else {
        return;
    }
}
void E() {
    int x = 10;
    if (x < 10) {
        A();
    } else if (x < 10) {
        B();
    } else if (x < 10) {
        C();
    } else if (x < 10) {
        D();
    } else if (x < 10) {
        E();
    } else if (x < 10) {
        F();
    } else {
        return;
    }
}
void F() {
    int x = 10;
    if (x < 10) {
        A();
    } else if (x < 10) {
        B();
    } else if (x < 10) {
        C();
    } else if (x < 10) {
        D();
    } else if (x < 10) {
        E();
    } else if (x < 10) {
        F();
    } else {
        return;
    }
}
int main() {
#pragma omp parallel
    {
        A();
    }
}
