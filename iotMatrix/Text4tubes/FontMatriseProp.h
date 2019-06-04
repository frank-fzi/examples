#ifndef FontMatrise_h
#define FontMatrise_h

const uint8_t MatriseFontData[] = {
		FONT_PROPORTIONAL | 5,  // Font Maximum Width
        5,  // Font Width
        7,  // Font Height
        32, // Font First Character
        127,// Font Last Character
        5, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,  // Code for char  
        3, 0x20, 0x20, 0x20, 0x20, 0x20, 0x00, 0x20,  // Code for char !
        5, 0x50, 0x50, 0x50, 0x00, 0x00, 0x00, 0x00,  // Code for char "
        5, 0x50, 0x50, 0xf8, 0x50, 0xf8, 0x50, 0x0A,  // Code for char #
        5, 0x20, 0x78, 0x80, 0x70, 0x08, 0xf0, 0x20,  // Code for char $
        5, 0xc8, 0xc8, 0x10, 0x20, 0x40, 0x98, 0x98,  // Code for char %
        5, 0x60, 0x90, 0x80, 0x78, 0x90, 0x90, 0x60,  // Code for char &
        5, 0x10, 0x20, 0x40, 0x00, 0x00, 0x00, 0x00,  // Code for char '
        5, 0x10, 0x20, 0x40, 0x40, 0x40, 0x20, 0x10,  // Code for char (
        5, 0x40, 0x20, 0x10, 0x10, 0x10, 0x20, 0x40,  // Code for char )
        5, 0x00, 0xa8, 0x70, 0x20, 0x70, 0xa8, 0x00,  // Code for char *
        5, 0x00, 0x20, 0x20, 0xf8, 0x20, 0x20, 0x00,  // Code for char +
        5, 0x00, 0x00, 0x00, 0x60, 0x60, 0x20, 0x40,  // Code for char ,
        5, 0x00, 0x00, 0x00, 0xf8, 0x00, 0x00, 0x00,  // Code for char -
        5, 0x00, 0x00, 0x00, 0x00, 0x00, 0x60, 0x60,  // Code for char .
        5, 0x10, 0x10, 0x20, 0x20, 0x20, 0x40, 0x40,  // Code for char /
        5, 0x70, 0x88, 0xc8, 0xa8, 0x98, 0x88, 0x70,  // Code for char 0
        5, 0x20, 0x60, 0x20, 0x20, 0x20, 0x20, 0x70,  // Code for char 1
        5, 0x70, 0x88, 0x08, 0x10, 0x20, 0x40, 0xf8,  // Code for char 2
        5, 0xf8, 0x08, 0x10, 0x30, 0x08, 0x88, 0x70,  // Code for char 3
        5, 0x10, 0x30, 0x50, 0x90, 0xf8, 0x10, 0x10,  // Code for char 4
        5, 0xf8, 0x80, 0xf0, 0x08, 0x08, 0x88, 0x70,  // Code for char 5
        5, 0x30, 0x40, 0x80, 0xf0, 0x88, 0x88, 0x70,  // Code for char 6
        5, 0xf8, 0x08, 0x10, 0x20, 0x20, 0x20, 0x20,  // Code for char 7
        5, 0x70, 0x88, 0x88, 0x70, 0x88, 0x88, 0x70,  // Code for char 8
        5, 0x70, 0x88, 0x88, 0x78, 0x08, 0x10, 0x60,  // Code for char 9
        5, 0x00, 0x60, 0x60, 0x00, 0x60, 0x60, 0x00,  // Code for char :
        5, 0x60, 0x60, 0x00, 0x60, 0x60, 0x20, 0x40,  // Code for char ;
        5, 0x10, 0x20, 0x40, 0x80, 0x40, 0x20, 0x10,  // Code for char <
        5, 0x00, 0x00, 0xf8, 0x00, 0xf8, 0x00, 0x00,  // Code for char =
        5, 0x80, 0x40, 0x20, 0x10, 0x20, 0x40, 0x80,  // Code for char >
        5, 0x70, 0x88, 0x08, 0x10, 0x20, 0x00, 0x20,  // Code for char ?
        5, 0x70, 0x88, 0xa8, 0xd8, 0xb0, 0x80, 0x70,  // Code for char @
        5, 0x20, 0x50, 0x88, 0x88, 0xf8, 0x88, 0x88,  // Code for char A
        5, 0xf0, 0x88, 0x88, 0xf0, 0x88, 0x88, 0xf0,  // Code for char B
        5, 0x70, 0x88, 0x80, 0x80, 0x80, 0x88, 0x70,  // Code for char C
        5, 0xe0, 0x90, 0x88, 0x88, 0x88, 0x90, 0xe0,  // Code for char D
        5, 0xf8, 0x80, 0x80, 0xf0, 0x80, 0x80, 0xf8,  // Code for char E
        5, 0xf8, 0x80, 0x80, 0xf0, 0x80, 0x80, 0x80,  // Code for char F
        5, 0x70, 0x88, 0x80, 0xb8, 0x88, 0x88, 0x78,  // Code for char G
        5, 0x88, 0x88, 0x88, 0xf8, 0x88, 0x88, 0x88,  // Code for char H
        5, 0x70, 0x20, 0x20, 0x20, 0x20, 0x20, 0x70,  // Code for char I
        5, 0x08, 0x08, 0x08, 0x08, 0x18, 0x88, 0x70,  // Code for char J
        5, 0x88, 0x90, 0xa0, 0xc0, 0xa0, 0x90, 0x88,  // Code for char K
        5, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0xf8,  // Code for char L
        5, 0x88, 0xd8, 0xa8, 0xa8, 0x88, 0x88, 0x88,  // Code for char M
        5, 0x88, 0x88, 0xc8, 0xa8, 0x98, 0x88, 0x88,  // Code for char N
        5, 0x70, 0x88, 0x88, 0x88, 0x88, 0x88, 0x70,  // Code for char O
        5, 0xf0, 0x88, 0x88, 0x88, 0xf0, 0x80, 0x80,  // Code for char P
        5, 0x70, 0x88, 0x88, 0x88, 0xa8, 0x98, 0x78,  // Code for char Q
        5, 0xf0, 0x88, 0x88, 0xf0, 0xa0, 0x90, 0x88,  // Code for char R
        5, 0x70, 0x88, 0x80, 0x70, 0x08, 0x88, 0x70,  // Code for char S
        5, 0xf8, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,  // Code for char T
        5, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x70,  // Code for char U
        5, 0x88, 0x88, 0x88, 0x88, 0x88, 0x50, 0x20,  // Code for char V
        5, 0x88, 0x88, 0x88, 0xa8, 0xa8, 0xa8, 0x50,  // Code for char W
        5, 0x88, 0x88, 0x50, 0x20, 0x50, 0x88, 0x88,  // Code for char X
        5, 0x88, 0x88, 0x88, 0x50, 0x20, 0x20, 0x20,  // Code for char Y
        5, 0xf8, 0x08, 0x10, 0x20, 0x40, 0x80, 0xf8,  // Code for char Z
        5, 0x70, 0x40, 0x40, 0x40, 0x40, 0x40, 0x70,  // Code for char [
        5, 0x40, 0x40, 0x20, 0x20, 0x20, 0x10, 0x10,  // Code for char BackSlash
        5, 0x70, 0x10, 0x10, 0x10, 0x10, 0x10, 0x70,  // Code for char ]
        5, 0x20, 0x50, 0x88, 0x00, 0x00, 0x00, 0x00,  // Code for char ^
        5, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xf8,  // Code for char _
        5, 0x40, 0x20, 0x10, 0x00, 0x00, 0x00, 0x00,  // Code for char `
        5, 0x00, 0x00, 0x70, 0x88, 0x88, 0x98, 0x68,  // Code for char a
        5, 0x80, 0x80, 0xb0, 0xc8, 0x88, 0x88, 0xf0,  // Code for char b
        5, 0x00, 0x00, 0x70, 0x88, 0x80, 0x80, 0x70,  // Code for char c
        5, 0x08, 0x08, 0x68, 0x98, 0x88, 0x88, 0x78,  // Code for char d
        5, 0x00, 0x00, 0x70, 0x88, 0xf8, 0x80, 0x70,  // Code for char e
        5, 0x30, 0x40, 0xe0, 0x40, 0x40, 0x40, 0x40,  // Code for char f
        5, 0x78, 0x88, 0x88, 0x98, 0x68, 0x08, 0x70,  // Code for char g
        5, 0x80, 0x80, 0xb0, 0xc8, 0x88, 0x88, 0x88,  // Code for char h
        5, 0x20, 0x00, 0x60, 0x20, 0x20, 0x20, 0x70,  // Code for char i
        5, 0x10, 0x00, 0x30, 0x10, 0x10, 0x10, 0x60,  // Code for char j
        5, 0x80, 0x80, 0x88, 0x90, 0xa0, 0xd0, 0x88,  // Code for char k
        3, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x20,  // Code for char l
        5, 0x00, 0x00, 0xd0, 0xa8, 0xa8, 0xa8, 0xa8,  // Code for char m
        5, 0x00, 0x00, 0xb0, 0xc8, 0x88, 0x88, 0x88,  // Code for char n
        5, 0x00, 0x00, 0x70, 0x88, 0x88, 0x88, 0x70,  // Code for char o
        5, 0xf0, 0x88, 0x88, 0xc8, 0xb0, 0x80, 0x80,  // Code for char p
        5, 0x78, 0x88, 0x88, 0x98, 0x68, 0x08, 0x08,  // Code for char q
        5, 0x00, 0x00, 0xa0, 0xd0, 0x80, 0x80, 0x80,  // Code for char r
        5, 0x00, 0x00, 0x70, 0x80, 0x70, 0x08, 0xf0,  // Code for char s
        5, 0x40, 0x40, 0xf0, 0x40, 0x40, 0x40, 0x30,  // Code for char t
        5, 0x00, 0x00, 0x88, 0x88, 0x88, 0x98, 0x68,  // Code for char u
        5, 0x00, 0x00, 0x88, 0x88, 0x88, 0x50, 0x20,  // Code for char v
        5, 0x00, 0x00, 0xa8, 0xa8, 0xa8, 0xa8, 0x50,  // Code for char w
        5, 0x00, 0x00, 0x88, 0x50, 0x20, 0x50, 0x88,  // Code for char x
        5, 0x88, 0x88, 0x88, 0x98, 0x68, 0x08, 0x70,  // Code for char y
        5, 0x00, 0x00, 0xf8, 0x10, 0x20, 0x40, 0xf8,  // Code for char z
        5, 0x10, 0x20, 0x20, 0x40, 0x20, 0x20, 0x10,  // Code for char {
        5, 0x20, 0x20, 0x20, 0x00, 0x20, 0x20, 0x20,  // Code for char |
        5, 0x40, 0x20, 0x20, 0x10, 0x20, 0x20, 0x40,  // Code for char }
        5, 0x00, 0x00, 0x48, 0xb0, 0x00, 0x00, 0x00,  // Code for char ~
        5, 0x70, 0x50, 0x50, 0x50, 0x50, 0x50, 0x70   // Code for char 
				};

#endif
