typedef double REAL;
typedef REAL * REALPTR;
typedef unsigned long PTR;
int main() {
    REAL *S1, *A21, *A22;
    PTR TempMatrixOffset = 0;
    PTR MatrixOffsetA = 0;
    (* (REALPTR) ( ((PTR) S1) + TempMatrixOffset ) ) = (* (REALPTR) ( ((PTR) A21) + MatrixOffsetA ) ) - (* (REALPTR) ( ((PTR) A22) + MatrixOffsetA ) );
}
