#include <stdio.h>

main(){
	int a;
	float b;
	a = 2;
	printf("a is %d\n", a);
	a = a + 2;
	printf("a is %d\n", a);
	b = 3.5;
	a++;
	printf("a is %d\n", a);
	b++;
	printf("hello hello\n");

	int x;
	x = 1;
	int y;
	if(a > b && a > 0){
		if(a < b){
			b = a * b;
		} else{
			while(x > 0){
				for(y = 0; y < 5; y++){
					a = a + 10;
	printf("a is %d\n", a);
				}
				x--;
			}	
		}
	} else {
		a = a + 3 + 14 / b;
	printf("a is %d\n", a);
	}
	
	int i;
	
	for(i = 0; i < 5; i++){
		a = a + 2;
	printf("a is %d\n", a);
	}
	
	printf("hello world!\n");
	printf("a and b are %d and %f %d\n", a, b);
	switch(a){
		case 2: b = 3; break;
		case 4: b = 5; 
		case 6: b = b + 7; break;
		case 8: b = 9; break;
		default: b = a / 3;
	}
}
