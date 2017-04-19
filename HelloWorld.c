#include <stdio.h>

main() {
	float f;
	f = 1.1299;

	float g;
	g = 1.12E10;
	
	printf("%f", f);
	printf("%d", g);
	
	int i;
	for(i = 0; i < 5; i++) {
		printf("Hello World!\n");
		
		if(i < 2) {
			int j;
			scanf("%d", &j);
			printf("%d\n", j);
		}
		
		switch(i) {
			case 1: printf("Current integer is one\n"); 
			break;
			case 2: printf("Current integer is two\n"); 
			break;
			case 3: printf("Current integer is three\n"); 
			break;
			default: printf("Current integer is either four or five\n");
		}
	}

	while(i > 4 && i < 10) {
		scanf("%d", &i);
		printf("%d\n", i);
	}

	printf("Bye World!\n");
}
