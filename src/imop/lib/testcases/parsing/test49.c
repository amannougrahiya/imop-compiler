int main () {
{int x = 10, y; {int x = 3; x = 4;} y = x;}
{int x = 10, y; {{int x = 3; x = 4;} y = 2;} y = x;}
{int x = 10, y; {{int z = 3; x = 4;} y = 2;} y = x;}
{int x = 10, y; {int z = 3;} {int z = 2;}}
}
