/*
	PixelTubes.cpp - Library for handling double sided pixel tubes
	based on FastLED
	Created by M. Frank, 2018
*/

#include "Arduino.h"
#include "FastLED.h"
#include "PixelTubes.h"

PixelTubes::PixelTubes(CRGB leds[], uint8_t size, uint8_t count, uint8_t brightnessFront, uint8_t brightnessBack) {
	_leds = leds;
	_size = size;
	_count = count;
	_brightnessFront = brightnessFront;
	_brightnessBack = brightnessBack;
}

uint16_t PixelTubes::getIndex(uint8_t x, uint8_t y, bool back, bool mirrow) {
  if(x >= _count || y >= _size) {
    return -1;
  } else if (back){
    if (mirrow) {
      return (_count-x-1)*2*_size+y;
    } else {
      // return (2*_count-2*x)*_size-y-1;
	  return _size*(1+2*(_count-1))-2*x*_size+y;
    }
  } else {
    if (mirrow) {
      // return 2*_size*(x+1)-y-1;
	  return 2*x*_size+_size+y;
    } else {
      // return 2*_size*x+y;
	  return 2*x*_size+_size-y-1;
    }
  }
}

void PixelTubes::setPixel(uint8_t x, uint8_t y, CRGB pixel, enum DisplayMode displayMode){
	switch (displayMode) {
	case FRONT_ONLY :
	  _leds[getIndex(x,y)] = pixel;  
		// scale8_video(pixel,_brightnessFront);
	  break;
	case BACK_ONLY :
	  _leds[getIndex(x,y,true)] = pixel; 
	  break;
	case COPY : 
	  _leds[getIndex(x,y)] = pixel; 
	  _leds[getIndex(x,y,true)] = pixel; 
	  break;
	case MIRROW_TO_BACK :
	  _leds[getIndex(x,y)] = pixel; 
	  _leds[getIndex(x,y,false,true)] = pixel; 
	  break;
	case MIRROW_TO_FRONT :
	  _leds[getIndex(x,y,true)] = pixel; 
	  _leds[getIndex(x,y,true,true)] = pixel; 
	  break;
	}
}

uint32_t PixelTubes::getRgb(uint8_t x, uint8_t y, bool back) {
	return crgb2rgb(_leds[getIndex(x,y,back)]);
}

void PixelTubes::addColumn(uint32_t column[], enum DisplayMode displayMode){
  for(int x=0; x<_count; x++) {
    for(int y=0; y<_size; y++) {
      uint32_t colorcode = column[y];
      switch (displayMode) {
        case FRONT_ONLY :
          _leds[getIndex(x,y)] = (x+1==_count) ? rgba2crgb(colorcode) : _leds[getIndex(x+1,y)];          
          break;
        case BACK_ONLY :
          _leds[getIndex(x,y,true)] = (x+1==_count) ? rgba2crgb(colorcode) : _leds[getIndex(x+1,y,true)];
          break;
        case COPY : 
          _leds[getIndex(x,y)] = (x+1==_count) ? rgba2crgb(colorcode) : _leds[getIndex(x+1,y)];
          _leds[getIndex(x,y,true)] = (x+1==_count) ? rgba2crgb(colorcode) : _leds[getIndex(x+1,y,true)];
          break;
        case MIRROW_TO_BACK :
          _leds[getIndex(x,y)] = (x+1==_count) ? rgba2crgb(colorcode) : _leds[getIndex(x+1,y)];
          _leds[getIndex(x,y,false,true)] = (x+1==_count) ? rgba2crgb(colorcode) : _leds[getIndex(x+1,y,false,true)];;
          break;
        case MIRROW_TO_FRONT :
          _leds[getIndex(x,y,true)] = (x+1==_count) ? rgba2crgb(colorcode) : _leds[getIndex(x+1,y,true)];
          _leds[getIndex(x,y,true,true)] = (x+1==_count) ? rgba2crgb(colorcode) : _leds[getIndex(x+1,y,true,true)];
          break;
      }
    }
  }
}

void PixelTubes::shiftLeft() {
  for(int x=0; x<_count; x++) {
    for(int y=0; y<_size; y++) {
      _leds[getIndex(x,y)] = (x+1==_count) ? CRGB::Black : _leds[getIndex(x+1,y)];
	  _leds[getIndex(x,y,true)] = (x+1==_count) ? CRGB::Black : _leds[getIndex(x+1,y,true)];
    }
  }
}

void PixelTubes::shiftRight() {
  for(int x=_count; x>=0; x--) {
    for(int y=_size; y>=0; y--) {
      _leds[getIndex(x,y)] = (x-1<0) ? CRGB::Black : _leds[getIndex(x-1,y)];
	  _leds[getIndex(x,y,true)] = (x-1<0) ? CRGB::Black : _leds[getIndex(x-1,y,true)];
    }
  }
}

void PixelTubes::shiftUp() {
  for(int x=0; x<_count; x++) {
    for(int y=0; y<_size; y++) {
      _leds[getIndex(x,y)] = (y+1==_size) ? CRGB::Black : _leds[getIndex(x,y+1)];
	  _leds[getIndex(x,y,true)] = (y+1==_size) ? CRGB::Black : _leds[getIndex(x,y+1,true)];
    }
  }
}

void PixelTubes::shiftDown() {
  for(int x=0; x<_count; x++) {
    for(int y=_size; y>=0; y--) {
      _leds[getIndex(x,y)] = (y-1<0) ? CRGB::Black : _leds[getIndex(x,y-1)];
	  _leds[getIndex(x,y,true)] = (y-1<0) ? CRGB::Black : _leds[getIndex(x,y-1,true)];
    }
  }
}

uint8_t PixelTubes::getSize() {	return _size; }
uint8_t PixelTubes::getCount() { return _count; }
uint8_t PixelTubes::getBrightnessFront() { return _brightnessFront; }
uint8_t PixelTubes::getBrightnessBack() { return _brightnessBack; }

// get 24-bit 0xRRGGBB color code from CRGB
uint32_t PixelTubes::crgb2rgb (CRGB crgb) {
  uint32_t rgb;
  rgb |= crgb.r << 16;
  rgb |= crgb.g << 8;
  rgb |= crgb.b << 0;
  return rgb;
}

// get 32-bit 0xRRGGBBAA color code from CRGB
uint32_t PixelTubes::crgb2rgba (CRGB crgb) {
  uint32_t rgba;
  rgba |= crgb.r << 24;
  rgba |= crgb.g << 16;
  rgba |= crgb.b << 8;
  rgba |= 255 << 0;
  return rgba;
}

// allow assignment from 32-bit 0xRRGGBBAA color code
CRGB PixelTubes::rgba2crgb (uint32_t rgba) {
    uint8_t r = (rgba >> 24) & 0xFF;
    uint8_t g = (rgba >> 16) & 0xFF;
    uint8_t b = (rgba >>  8) & 0xFF;
    uint8_t a = (rgba >>  0) & 0xFF;
    return CRGB(r,g,b);
}