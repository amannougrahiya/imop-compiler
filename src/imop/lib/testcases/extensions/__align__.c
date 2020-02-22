/* align.c */
#include <stdio.h>
typedef struct {
    double dvalue;
    int ivalue;
} showal;
int main(int argc,char *argv[])
{
    printf("__alignof__(char)=%d\n",__alignof__(char));
    return(0);
}
