/*
	Json4tubes.cpp - Library for controlling PixelTubes with JSON
	based on ArduinoJson (https://github.com/bblanchon/ArduinoJson)
	Created by M. Frank, 2018
*/

#include "Arduino.h"
#include "ArduinoJson.h"
#include "Json4tubes.h"

Json4tubes::Json4tubes(PixelTubes *tubes) {
	_tubes = tubes;
	_timeoutFront = 0;
	_timeoutBack = 0;
}

DisplayMode Json4tubes::getDisplayMode(const char* string) {
  if ( !strcmp(string, "FRONT_ONLY")) return FRONT_ONLY;
  if ( !strcmp(string, "BACK_ONLY")) return BACK_ONLY;
  if ( !strcmp(string, "COPY")) return COPY;
  if ( !strcmp(string, "MIRROW_TO_BACK")) return MIRROW_TO_BACK;
  if ( !strcmp(string, "MIRROW_TO_FRONT")) return MIRROW_TO_FRONT;
  return FRONT_ONLY;
}

Action Json4tubes::getAction(const char* string) {
  if ( !strcmp(string, "SET_FPS")) return SET_FPS;
  if ( !strcmp(string, "SET_BRIGHTNESS")) return SET_BRIGHTNESS;
  if ( !strcmp(string, "SET_FPS")) return SET_FPS;
  if ( !strcmp(string, "SET_COLOR")) return SET_COLOR;
  if ( !strcmp(string, "SET_TEXT")) return SET_TEXT;
  if ( !strcmp(string, "SET_COLUMN")) return SET_COLUMN;
  if ( !strcmp(string, "SET_MATRIX")) return SET_MATRIX;
  if ( !strcmp(string, "GET_STATE")) return GET_STATE;
  if ( !strcmp(string, "GET_INDEX")) return GET_INDEX;
  if ( !strcmp(string, "GET_TEST")) return GET_TEST;
  return INVALID_ACTION;
}

String Json4tubes::parseInput(const char* input) {
  DynamicJsonBuffer jsonBuffer(5000);
  JsonObject& rootIn = jsonBuffer.parseObject(input);
  JsonObject& rootOut = jsonBuffer.createObject();
  if (!rootIn.success()) {
    rootOut["success"] = false;
    rootOut["buffer"] = jsonBuffer.size();
    rootOut["input"] = input;
  } else {
    enum Action action = rootIn["request"].success() ? getAction(rootIn["request"]) : GET_STATE;
    switch (action) {
      case GET_STATE: {
        return getState();
        break;
      }
      case SET_MATRIX: {
        rootOut["response"] = "SET_MATRIX";
        rootOut["success"] = true;
        JsonArray& front = rootIn["front"];
        JsonArray& back = rootIn["back"];
        rootOut["front"] = front.size();
        rootOut["back"] = back.size();
        json2matrix(front, FRONT_ONLY);
        json2matrix(back, BACK_ONLY);
        break;
      }
      case SET_COLUMN: {
        rootOut["response"] = "SET_COLUMN";
        rootOut["success"] = true;
        JsonArray& column = rootIn["column"];
        rootOut["column"] = column.size();
        enum DisplayMode displayMode = rootIn["displayMode"].success() ? getDisplayMode(rootIn["displayMode"]) : FRONT_ONLY;
        column2matrix(column, displayMode);
        break;
      }
      case SET_BRIGHTNESS: {
        rootOut["response"] = "SET_BRIGHTNESS";
        if (rootIn["brightness"].success()) {
          rootOut["success"] = true;
          int brightness = rootIn["brightness"];
          brightness = (brightness > 255) ? 255 : brightness;
          brightness = (brightness < 0) ? 0 : brightness;
          FastLED.setBrightness(brightness);
          rootOut["brightness"] = brightness;
        } else if (rootIn["brightnessFront"].success()) {
          rootOut["success"] = true;
          int brightness = rootIn["brightnessFront"];
          brightness = (brightness > 255) ? 255 : brightness;
          brightness = (brightness < 0) ? 0 : brightness;
          rootOut["brightnessFront"] = brightness;
        } else if (rootIn["brightnessBack"].success()) {
          rootOut["success"] = true;
          int brightness = rootIn["brightnessBack"];
          brightness = (brightness > 255) ? 255 : brightness;
          brightness = (brightness < 0) ? 0 : brightness;
          rootOut["brightnessBack"] = brightness;
        } else {
          rootOut["success"] = false;
          rootOut["error"] = "no JSON member for brightness";
        }
        break;
      }
      case SET_FPS : {
        rootOut["response"] = "SET_FPS";
        if (rootIn["fps"].success()) {
          rootOut["success"] = true;
          _fps = (rootIn["fps"] < 1) ? 1 : rootIn["fps"];		  
        } else {
          rootOut["success"] = false;
        }        
        rootOut["fps"] = _fps;
        break;
      }
      case GET_TEST : {
        rootOut["response"] = "GET_TEST";         
        int8_t x = rootIn["x"].success() ? rootIn["x"] : 0;
        int8_t y = rootIn["y"].success() ? rootIn["y"] : 0;
        bool back = rootIn["back"].success() ? rootIn["back"] : false;
        bool mirrow = rootIn["mirrow"].success() ? rootIn["mirrow"] : false;
        int16_t index = _tubes->getIndex(x,y,back,mirrow);
        if (index < 0) {
          rootOut["success"] = false;
        } else {
          rootOut["success"] = true;
          rootOut["x"] = x;
          rootOut["y"] = y;
          rootOut["back"] = back;
          rootOut["mirrow"] = mirrow;
          rootOut["index"] = index;
        }
        break;
      }
      default : {
        rootOut["response"] = "INVALID_ACTION";
        rootOut["success"] = false;
      }
    }
  }
  String result = "";
  rootOut.printTo(result);
  return result;
}

String Json4tubes::getState() {
  // buffer
  const int capacity = JSON_OBJECT_SIZE(9)  // root
    +2*JSON_OBJECT_SIZE(3)                  // data front+back
    +2*JSON_ARRAY_SIZE(_tubes->getCount())               // matrix front+back
    +2*_tubes->getCount()*JSON_ARRAY_SIZE(_tubes->getSize());         // rows for each matrix
  DynamicJsonBuffer jsonBuffer(capacity);

  // root
  JsonObject& root = jsonBuffer.createObject();
  root["response"] = "GET_STATE";
  root["success"] = true;
  root["heap"] = ESP.getFreeHeap();
  //root["fragmentation"] = ESP.getHeapFragmentation();
  root["frequency"] = ESP.getCpuFreqMHz();
  root["brightness"] = FastLED.getBrightness();
  root["fps"] = _fps;

  // front
  JsonObject& front = root.createNestedObject("front");
  front["brightness"] = _tubes->getBrightnessFront();
  front["timeout"] = _timeoutFront;
  JsonArray& frontMatrix = front.createNestedArray("matrix");
  matrix2json(frontMatrix);

  // back
  JsonObject& back = root.createNestedObject("back");  
  back["brightness"] = _tubes->getBrightnessBack();
  back["timeout"] = _timeoutBack;
  JsonArray& backMatrix = back.createNestedArray("matrix");
  matrix2json(backMatrix, true);

  // result
  String result;
  root.printTo(result);
  return result;
}

void Json4tubes::matrix2json (JsonArray& matrix, bool back) {
  for(int x=0; x<_tubes->getCount(); x++){
    JsonArray& rows = matrix.createNestedArray();
    for(int y=0; y<_tubes->getSize(); y++) {
	  rows.add(_tubes->getRgb(x,y,back));
    }
  }
}

void Json4tubes::column2matrix(JsonArray& array, enum DisplayMode displayMode){
  uint32_t column[array.size()];
  uint8_t i = 0;
  for (uint32_t row : array) {
	column[i] = row;
	i++;
  }
  _tubes->addColumn(column,displayMode);
  setTimeout(displayMode);
}

void Json4tubes::json2matrix(JsonArray& matrix, DisplayMode displayMode) {
  for(int x=0; x<_tubes->getCount(); x++){
    for(int y=0; y<_tubes->getSize(); y++) {
	  _tubes->setPixel(x,y,str2hex(matrix[x][y]),displayMode);
	  setTimeout(displayMode);
    }
  }
}

uint32_t Json4tubes::str2hex(String input) {
  return (uint32_t) strtoul(input.c_str(), 0, 16);
}

// getter
uint8_t Json4tubes::getFps() { return _fps; }
uint32_t Json4tubes::getTimeoutFront() { return _timeoutFront; }
uint32_t Json4tubes::getTimeoutBack() { return _timeoutBack; }

// setter
void Json4tubes::setFps(uint8_t fps) { _fps = fps; }
void Json4tubes::decTimeoutFront() { _timeoutFront--; }
void Json4tubes::decTimeoutBack() { _timeoutBack--; }
void Json4tubes::setTimeout(DisplayMode displayMode, uint8_t delay) {
	uint32_t time = delay * _fps;
	switch (displayMode) {
		case FRONT_ONLY : _timeoutFront = time; break;
		case BACK_ONLY : _timeoutBack = time; break;
		default : _timeoutFront = _timeoutBack = time;
	}
}
		