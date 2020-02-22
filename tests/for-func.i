void get_seq(int *len);
int main () {
    get_seq((int *) 0);
}
int strlen(int *len) {
	return 0;
}
void get_seq(int *line) {      
	int i;
l1: l2: for (i = 1; i <= strlen(line); i++) {
        if (line[i] != ' ')
            break;
    }
}
