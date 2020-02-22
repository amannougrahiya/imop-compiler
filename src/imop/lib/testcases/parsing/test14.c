void *malloc(unsigned long );
struct voidStr{
	int base;
	int bound;
	void * val;
};
struct intStr{
	int base;
	int bound;
	int * val;
};
int main() {
    struct intStr p;
    struct intStr x;
    int _imopVarPre2;
    struct voidStr _imopVarPre3;
    _imopVarPre2 = 200 + 1;
    _imopVarPre3.val = malloc(_imopVarPre2);
    p.val = (int *) _imopVarPre3.val;
    x.val = p.val;
}
