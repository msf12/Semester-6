#include <stdio.h>

int main(){
	int x = 10;
	float y = 12;
	char c = 'C';
	char a = 65;

	printf("x = %d\n", x); //printing the value of x
	printf("y = %f\n", y);//to be completed: printing the value of y

	printf("c = %c\n", c); //printing the value of c
	printf("a = %c\n", a); //to be completed: printing the value of a;

	//printing the sizes of each data type
	
	//to be completed: printing the size of int in bytes
	printf("size of signed int in bytes is %ld.\n", sizeof x);

	//printing the size of float in bytes
	printf("Size of signed float in bytes is %ld.\n", sizeof y);
	
	//to be completed: printing the size of character in bytes
	printf("Size of character in bytes is %ld.\n", sizeof c);

	return 0;
}