int main() {
	int x = 10, y = 5;
	for (;1;) {
		x++;
		x = 10;
		if (x > 10) {
			break;
		}
	}
	for (;0;) {
		x++;
		x = 11;
	}
	11;
	for(;;x++) {
		y = x;
		break;
	}
	12;
	for(;x>20;x++){
		y = x;
	}
	13;
	for(x=10;x>30;x++){
		y = x;
	}
	14;
	for(x=10;;x++){
		y = x;
		break;
	}
	15;
	for(;;) {
		y = x;
		break;
	}
	16;
	for(;x>20;){
		y = x;
	}
	17;
	for(x=10;x>30;){
		y = x;
	}
	18;
	for(x=10;;){
		y = x;
		break;
	}
	119;
	for(y=0;y<10;y++)
		x = y;

}
