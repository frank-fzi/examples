/*
	Json4tubes.h - Library for controlling PixelTubes with JSON
	based on ArduinoJson
	Created by M. Frank, 2018
*/

#ifndef Json4tubes_h
#define Json4tubes_h

#include "Arduino.h"
#include "ArduinoJson.h"
#include "PixelTubes.h"

enum Action {	SET_BRIGHTNESS,
				SET_FPS,
				SET_COLOR,
				SET_TEXT,
				SET_COLUMN,
				SET_MATRIX,
				GET_STATE,
				GET_INDEX,
				GET_TEST,
				INVALID_ACTION	};


class Json4tubes
{
	public:
		Json4tubes(PixelTubes *tubes);
		
		String parseInput(const char* input);		
		void matrix2json (JsonArray& matrix, bool back = false);
		void column2matrix(JsonArray& column, enum DisplayMode displayMode = FRONT_ONLY);
		void json2matrix(JsonArray& matrix, DisplayMode displayMode = FRONT_ONLY);
		String getState();	
		uint8_t getFps();
		void setFps(uint8_t fps);
		uint32_t getTimeoutFront();
		uint32_t getTimeoutBack();
		void setTimeout(DisplayMode displayMode, uint8_t delay = 1);
		void decTimeoutFront();
		void decTimeoutBack();
	private:
		PixelTubes *_tubes;
		uint8_t _fps;
		uint32_t _timeoutFront;
		uint32_t _timeoutBack;
		DisplayMode getDisplayMode(const char* string);
		Action getAction(const char* string);
		uint32_t str2hex(String input);		
};

#endif