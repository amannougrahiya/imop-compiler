void foo(int x) {
}
int main() {
 int x = 10;
 for (x = 1; x < 10; x++) {
     foo(x);
  x = x;
 }
}
