/*
	PixelTubes.h - Library for handling double sided pixel tubes
	based on FastLED
	Created by M. Frank, 2018
*/

#ifndef PixelTubes_h
#define PixelTubes_h

#include "Arduino.h"
#include "FastLED.h"

enum DisplayMode {	FRONT_ONLY, 
					BACK_ONLY,
					COPY,
					MIRROW_TO_BACK,
					MIRROW_TO_FRONT	};

class PixelTubes
{
	public:
		PixelTubes(CRGB leds[], uint8_t size, uint8_t count, uint8_t brightnessFront = 255, uint8_t brightnessBack = 255);		
		uint16_t getIndex(uint8_t x, uint8_t y, bool back = false, bool mirrow = false);	
		void setPixel(uint8_t x, uint8_t y, CRGB pixel, enum DisplayMode displayMode = FRONT_ONLY);
		uint32_t getRgb(uint8_t x, uint8_t y, bool back = false);
		void addColumn(uint32_t column[], enum DisplayMode displayMode);
		void shiftUp();
		void shiftDown();
		void shiftLeft();
		void shiftRight();
		uint8_t getSize();
		uint8_t getCount();
		uint8_t getBrightnessFront();
		uint8_t getBrightnessBack();
	private:
		CRGB* _leds;				// pointer to external CRGB array
		CRGB _outOfBounds;
		uint8_t _size;				// size of each pixel tube (single side)
		uint8_t _count;				// total count of pixel tubes
		uint8_t _brightnessFront;
		uint8_t _brightnessBack;
		
		uint32_t crgb2rgb (CRGB crgb);
		uint32_t crgb2rgba (CRGB crgb);
		CRGB rgba2crgb (uint32_t rgba);
		uint32_t str2hex(String input);
};

#endif