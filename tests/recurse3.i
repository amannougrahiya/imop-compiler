void A();
void B();
void A() {
    int x = 10;
    if (x < 10) {
        B();
    }
}
void B() {
    int x = 10;
    if (x < 10) {
        A();
    }
}
int main() {
#pragma omp parallel
    {
        A();
    }
}
