#include <ESP8266WiFi.h>
#include <ESP8266mDNS.h>
#include <WiFiUdp.h>
#include <ArduinoOTA.h>
#include <FastLED.h>
#include <ESP8266WebServer.h>
#include <WebSocketsServer.h>
#include <NTPClient.h>
#include <PixelTubes.h>
#include <Json4tubes.h>
#include <Text4tubes.h>
#include <FontMatrise.h>
#include "html.h"

/***************************
 * Properties
 ***************************/
#define FASTLED_ALLOW_INTERRUPTS 0
#define DATA_PIN        6
#define COLOR_ORDER    GRB
#define LED_TYPE        WS2812B
#define BRIGHTNESS 255
#define FRAMES_PER_SECOND  100
#define COUNT 10
#define SIZE 30

// WiFi
const char* ssid = "<SSID>";
const char* password = "<PASSWORD>";

// Server
ESP8266WebServer webServer(80); 
WebSocketsServer webSocket = WebSocketsServer(81);
const uint8_t maxConnections = 10;
bool subscribers[maxConnections];

// NTP
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "europe.pool.ntp.org", 3600, 60000);

// LEDs
uint8_t brightnessFront = 100;
uint8_t brightnessBack = BRIGHTNESS;
const int numLeds = 2*COUNT*SIZE;
CRGB leds[numLeds];
PixelTubes tubes = PixelTubes(leds, SIZE, COUNT, brightnessFront, brightnessBack);
Json4tubes json = Json4tubes(&tubes);
uint8_t gHue = 0;

// text
Text4tubes tubeText1 = Text4tubes(&tubes);
Text4tubes tubeText2 = Text4tubes(&tubes);
Text4tubes tubeText3 = Text4tubes(&tubes);
const byte text1[] = { EFFECT_HSV_AH "\x00\xff\xff\x40\xff\xff" "   Hallo Karlsruhe!" };
const byte text2[] = { EFFECT_HSV_AH "\x00\xff\xff\x40\xff\xff" "   heute ist" };
const byte text3[] = { "..." };

/***************************
 * Methods
 ***************************/
void refresh() {
  FastLED.show();
  for(int i = 0; i<maxConnections; i++) {
    if(subscribers[i]) {
      webSocket.sendTXT(i, json.getState().c_str());
    }
  }
}

// web server request
void on_request() {
  webServer.sendHeader("Access-Control-Allow-Origin", "*");
  webServer.sendHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
  webServer.sendHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
  webServer.send(200, "application/json", json.parseInput(webServer.arg("plain").c_str()));
}

// web socket request
void webSocketEvent(uint8_t num, WStype_t type, uint8_t * payload, size_t length) {
  switch(type) {
    case WStype_DISCONNECTED: {
      subscribers[num] = false;
      char messageBuffer[100];
          snprintf(messageBuffer, sizeof(messageBuffer), "Teilnehmer [%u] getrennt", num);
          webSocket.broadcastTXT(messageBuffer);
      break;
    }
    case WStype_CONNECTED: {
      IPAddress ip = webSocket.remoteIP(num);
      char messageBuffer[100];
      snprintf(messageBuffer, sizeof(messageBuffer), "Neuer Teilnehmer: [%u] IP: %d.%d.%d.%d url: %s\n", num, ip[0], ip[1], ip[2], ip[3], payload);
      webSocket.broadcastTXT(messageBuffer);
      break;
    }
    case WStype_TEXT: {
      const char* input = (const char*) payload;
      if (input[0] == '{') {
        String output = json.parseInput(input);
        webSocket.sendTXT(num, output);
      } else {
        String message = input;
        webSocket.sendTXT(num, "input: " + message);
        if(message == "subscribe" || message == "\"subscribe\"") {
          if(maxConnections > num) {
            subscribers[num] = true;
            char messageBuffer[100];
              snprintf(messageBuffer, sizeof(messageBuffer), "sussessfully subscribed: [%u]", num);
              webSocket.broadcastTXT(messageBuffer);
          } else {
            webSocket.sendTXT(num, "too many connections");
          }        
        } else if (message == "unsubscribe" || message == "\"unsubscribe\"") {
          subscribers[num] = false;
          webSocket.sendTXT(num, "sussessfully unsubscribed");  
        } else {
          webSocket.sendTXT(num, "invalid command \"" + message + "\"");
        }
      }
    }
  }
}

void screensaver(bool back = false, bool mirrow = false) 
{
  // fade matrix to black (front or back)
  if (mirrow) {
    fadeToBlackBy(leds, numLeds, 10);
  } else {
    for (int x=0;x<COUNT;x++) {
      for (int y=0;y<SIZE;y++) {
        leds[tubes.getIndex(x,y,back)].fadeToBlackBy(10);
      }
    }
  }
  // light random pixel with random color (front or back)
  int8_t x = random(COUNT);
  int8_t y = random(SIZE);
  int8_t hue = gHue + random(64);
  if (mirrow) {
    leds[tubes.getIndex(x,y)] += CHSV( hue, 200, brightnessFront);
    leds[tubes.getIndex(x,y,false,true)] += CHSV( hue, 200, brightnessBack);
  } else {
    int8_t brightness = back ? brightnessBack : brightnessFront ;
    leds[tubes.getIndex(x,y,back)] += CHSV( hue, 200, brightness);
  }  
  refresh();
}

void snowFlakes() {
  int8_t x = random(COUNT);
  for (int8_t y=0;y<4;y++) {
    tubes.shiftDown();
    tubes.setPixel(x,0,CHSV(0,0,255-y*80),COPY);
    refresh();    
    //delay(random(100));
  }
}

byte* getWochentag(byte day) {
  String date = timeClient.getFormattedDate().substring(0,10);
  if (date.equals("2018-12-02")) {
     return (byte*) "   erster Advent";
  } else if (date.equals("2018-12-09")) {
     return (byte*) "   zweiter Advent";
  } else if (date.equals("2018-12-16")) {
     return (byte*) "   dritter Advent";
  } else if (date.equals("2018-12-23")) {
     return (byte*) "   vierter Advent";
  } else if (date.equals("2018-12-24")) {
     return (byte*) "   Heiligabend";
  } else if (date.equals("2018-12-25")) {
     return (byte*) "   Weihnachten";
  } else if (date.equals("2018-12-26")) {
     return (byte*) "   Weihnachten";
  } else if (date.equals("2018-12-31")) {
     return (byte*) "   Silvester";
  } else if (date.equals("2019-01-01")) {
     return (byte*) "   Neujahr";
  }
  switch (day) {
    case 0 : return (byte*) "   Sonntag";
    case 1 : return (byte*) "   Montag";
    case 2 : return (byte*) "   Dienstag";
    case 3 : return (byte*) "   Mittwoch";
    case 4 : return (byte*) "   Donnerstag";
    case 5 : return (byte*) "   Freitag";
    case 6 : return (byte*) "   Samstag";
  }
}

byte* getGreeting(byte hours) {
  if (hours < 11) {
    return (byte*) "   Guten Morgen Karlsruhe!";
  } else if (hours < 18) {
    return (byte*) "   Guten Tag Karlsruhe!";
  } else {
    return (byte*) "   Guten Abend Karlsruhe!";
  }
}

void showText() {  
  if (tubeText1.UpdateText(COPY) == -1) {    
    if (tubeText2.UpdateText(COPY) == -1) {      
        if (tubeText3.UpdateText(COPY) == -1) {   
         byte* greeting = getGreeting(timeClient.getHours());    
         tubeText1.SetText(greeting, 27); 
         tubeText2.SetText((unsigned char *)text2, sizeof(text2) - 1);
         byte* day = getWochentag(timeClient.getDay());    
         tubeText3.SetText(day, 13); 
      } 
    } 
  }
  refresh();
}

void on_homepage() {
  webServer.sendHeader("Access-Control-Allow-Origin", "*");
  webServer.send(200, "text/html", FPSTR(index_html));
}

/***************************
 * Main
 ***************************/
void setup() {
  // LED setup
  FastLED.addLeds<LED_TYPE,DATA_PIN,COLOR_ORDER>(leds, numLeds).setCorrection(TypicalLEDStrip);
  FastLED.setBrightness(BRIGHTNESS);
  json.setFps(FRAMES_PER_SECOND);

  // OTA setup
  Serial.begin(115200);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  while (WiFi.waitForConnectResult() != WL_CONNECTED) {
    FastLED.showColor(CRGB::Red);
    Serial.println("Connection Failed! Rebooting...");
    delay(5000);
    ESP.restart();
  }
  FastLED.showColor(CRGB::Green);
  ArduinoOTA.setHostname("PixelTubes");
  ArduinoOTA.begin();

  // time setup
  timeClient.begin();

  // web server setup
  webServer.on("/",   on_homepage);
  webServer.on("/request", on_request);
  webServer.begin();

  // web socket setup
  webSocket.begin();
  webSocket.onEvent(webSocketEvent);

  // text setup
  tubeText1.SetFont(MatriseFontData);
  tubeText2.SetFont(MatriseFontData);
  tubeText3.SetFont(MatriseFontData);
  tubeText1.Init(COUNT, 10, 0, 20);
  tubeText2.Init(COUNT, 10, 0, 10);
  tubeText3.Init(COUNT, 10, 0, 0);
  tubeText1.SetFrameRate(2);
  tubeText2.SetFrameRate(2);
  tubeText3.SetFrameRate(2);
  tubeText1.SetTextColrOptions( COLR_HSV | COLR_GRAD_AH, 0x00, 0xff, 0xff, 0x40, 0xff, 0xff);
  tubeText2.SetTextColrOptions( COLR_HSV | COLR_GRAD_AH, 0x00, 0xff, 0xff, 0x40, 0xff, 0xff);
  tubeText3.SetTextColrOptions( COLR_HSV | COLR_GRAD_AH, 0x00, 0xff, 0xff, 0x40, 0xff, 0xff);
}
  
void loop() {
  // handle requests and updates
  ArduinoOTA.handle();
  webServer.handleClient();
  webSocket.loop();
  timeClient.update();
  
  // LED loop
  if (json.getTimeoutFront() == 0 && json.getTimeoutBack() == 0) {
    screensaver(false,true);
    // snowFlakes();
    showText();
  } else {
      if (json.getTimeoutFront() > 0) {
      json.decTimeoutFront();
    } else {
      screensaver();
    }
    if (json.getTimeoutBack() > 0) {
      json.decTimeoutBack();
    } else {
      screensaver(true);
    }
  }
  FastLED.delay(1000/json.getFps()); 
  EVERY_N_MILLISECONDS( 20 ) { gHue++; }
}
