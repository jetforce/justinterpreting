INCLUDE,#include
STDIO,<stdio.h>
MAIN,main
OPENP,(
CLOSEP,)
OPENB,{
TFLOAT,float
VAR,f
SEMICOLON,;
VAR,f
EQUALS,=
FLOAT,1.1299
SEMICOLON,;
TFLOAT,float
VAR,g
SEMICOLON,;
VAR,g
EQUALS,=
FLOAT,1.12E10
SEMICOLON,;
PRINTF,printf
OPENP,(
STR,"%f"
COMMA,,
VAR,f
CLOSEP,)
SEMICOLON,;
PRINTF,printf
OPENP,(
STR,"%d"
COMMA,,
VAR,g
CLOSEP,)
SEMICOLON,;
TINT,int
VAR,i
SEMICOLON,;
FOR,for
OPENP,(
VAR,i
EQUALS,=
INT,0
SEMICOLON,;
VAR,i
CLESSTHAN,<
INT,5
SEMICOLON,;
VAR,i
ADD,+
ADD,+
CLOSEP,)
OPENB,{
PRINTF,printf
OPENP,(
STR,"Hello World!"
CLOSEP,)
SEMICOLON,;
IF,if
OPENP,(
VAR,i
CLESSTHAN,<
INT,2
CLOSEP,)
OPENB,{
TINT,int
VAR,j
SEMICOLON,;
SCANF,scanf
OPENP,(
STR,"%d"
COMMA,,
AMPERSAND,&
VAR,j
CLOSEP,)
SEMICOLON,;
PRINTF,printf
OPENP,(
STR,"%d"
COMMA,,
VAR,j
CLOSEP,)
SEMICOLON,;
CLOSEB,}
SWITCH,switch
OPENP,(
VAR,i
CLOSEP,)
OPENB,{
CASE,case
INT,1
COLON,:
PRINTF,printf
OPENP,(
STR,"Current integer is one"
CLOSEP,)
SEMICOLON,;
BREAK,break
SEMICOLON,;
CASE,case
INT,2
COLON,:
PRINTF,printf
OPENP,(
STR,"Current integer is two"
CLOSEP,)
SEMICOLON,;
BREAK,break
SEMICOLON,;
CASE,case
INT,3
COLON,:
PRINTF,printf
OPENP,(
STR,"Current integer is three"
CLOSEP,)
SEMICOLON,;
BREAK,break
SEMICOLON,;
DEFAULT,default
COLON,:
PRINTF,printf
OPENP,(
STR,"Current integer is either four or five"
CLOSEP,)
SEMICOLON,;
CLOSEB,}
CLOSEB,}
WHILE,while
OPENP,(
VAR,i
CMORETHAN,>
INT,5
LAND,&&
VAR,i
CLESSTHAN,<
INT,10
CLOSEP,)
OPENB,{
SCANF,scanf
OPENP,(
STR,"%d"
COMMA,,
AMPERSAND,&
VAR,i
CLOSEP,)
SEMICOLON,;
PRINTF,printf
OPENP,(
STR,"%d"
COMMA,,
VAR,i
CLOSEP,)
SEMICOLON,;
CLOSEB,}
PRINTF,printf
OPENP,(
STR,"Bye World!"
CLOSEP,)
SEMICOLON,;
CLOSEB,}
