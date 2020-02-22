int main() {
int x ;
#pragma omp parallel
#pragma omp task final(x > 1)
x = x + 1;
}
