int g1;
int g2;
int g3;
int g4;
#pragma omp threadprivate(g3)
int foo(int aFoo, int  , int cFoo);
void testFoo() {
    g4++;
    auto int aFoo;
#pragma omp flush
    im1: aFoo = 17;
    if (aFoo == 0) {
        l1Foo: aFoo = 10;
        if (aFoo != 10) {
            im2: foo(1, 1, 1);
#pragma omp flush
            im3: goto l1Foo;
        }
    }
    if (aFoo < 10) {
        lFoo: return;
    }
    aFoo = 20;
    g4 = 1;
    goto lFoo;
}
int my(int a) {
    im4: return a;
}
int foo(int aFoo, int bFoo , int cFoo) {
    int tempo;
    tempo = 0;
    tempo += 1;
    if (aFoo > 10) {
        testFoo();
    }
    if (bFoo > 20) {
        return aFoo + bFoo + cFoo;
    }
    g1 = 10;
    g2 = 100 + g1;
    int _imopVarPre0;
    lNotTestFoo: _imopVarPre0 = my(18);
    return _imopVarPre0;
}
int newFoo(int a, int b , int c) {
    int x = 0;
    int y1;
    y1 = 11;
    p: x = x + 10;
    x = y1 + 11;
    x = x + 33;
    return (a + b + c) * x;
}
int main(int argc, char *argv[]) {
    int x = 0;
    int y1;
    y1 = 11;
    p: x = x + 10;
    x = y1 + argc;
    x = x + 33;
    int iter;
    im81: if (1 < 2) {
#pragma omp parallel
        {
#pragma omp sections nowait
            {
#pragma omp section
                {
testThisNonLeaf: // #pragma omp dummyFlush CRITICAL_START written() read()
#pragma omp critical
                    {
                        x = x + 6;
                    }
// #pragma omp dummyFlush CRITICAL_END written() read()
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        }
    }
im51: 
#pragma omp parallel
    {
#pragma omp for nowait
        for (iter = 0; iter < 8; iter++) {
            int x1;
            int y1;
            int _imopVarPre1;
            _imopVarPre1 = my(8);
            x1 += _imopVarPre1;
            foo(x1, y1, 1);
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
    int _imopVarPre2;
    _imopVarPre2 = my(40);
#pragma omp parallel if(_imopVarPre2)
    {
        int z = 0;
        static int i;
        static int y;
        g3 = g3 + 1;
        i++;
im52: 
#pragma omp for nowait
        for (iter = 0; iter < 8; iter++) {
im6: // #pragma omp dummyFlush ATOMIC_START written() read()
#pragma omp atomic update
            z = z + 5;
// #pragma omp dummyFlush ATOMIC_END written() read()
im7: // #pragma omp dummyFlush ORDERED_START written() read()
#pragma omp ordered
            {
                int x1;
                int y1;
                x1 = 1;
                foo(x1, y1, 1);
                y = 10;
            }
// #pragma omp dummyFlush ORDERED_END written() read()
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
im82: 
#pragma omp sections nowait
        {
#pragma omp section
            {
im9: // #pragma omp dummyFlush CRITICAL_START written() read()
#pragma omp critical
                {
                    x = x + 6;
                }
// #pragma omp dummyFlush CRITICAL_END written() read()
            }
#pragma omp section
            {
                testThis: while (0) {
// #pragma omp dummyFlush TASK_START written() read()
im10: 
#pragma omp task
                    {
                        y = y + 7;
                    }
// #pragma omp dummyFlush TASK_END written() read()
                }
                y = y + 5;
                y = y + 10;
                foo(1, 1, 3);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
im11: 
#pragma omp single nowait
        {
            x = x + 2;
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
im12: 
#pragma omp master
        {
            x = x + 1;
        }
        foo(x, y, 3);
        y = 1;
        x = 10;
im13: 
#pragma omp flush
        y = y;
    }
    if (x > 11) {
        x = x - 1;
    }
    if (1 > 2) {
        x = x * 8;
    } else {
        x = x + 9;
    }
    im14: while (x != 0) {
        x = x - 1;
    }
    im15: do {
        x = x + 1;
        if (0) {
            im16: continue;
        }
    } while (x == 10);
    myfor: for (x = 0; x < 10; x++) {
        g1 = x;
    }
    im17: switch (x) {
        case 1: testerLabel: x = x + 11;
        break;
        case 2: x = x + 12;
        default: x = x + 13;
        im18: break;
    }
    newFoo(1, 1, 3);
}
