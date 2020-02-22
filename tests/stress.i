typedef double * DBLPTR;
void foo(double *a) {

}
int main() {
    double ** X = 0;
    foo (*(DBLPTR *) (DBLPTR *) ((DBLPTR *)X + 10));
}
