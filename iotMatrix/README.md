# iotMatrix

iotMatrix is a collection of an Arduino script and three dedicated libraries that enables a microcontroller unit like the ESP8266 to control a double-sided matrix of WS2812B pixels. It allows to show text, images or other content based on NTP, Web requests or WebSocket inputs. 

To include it in your network, add your data to iotMatrix.ino before writing the script to ESP8266:
```Arduino
// WiFi
const char* ssid = "<SSID>";
const char* password = "<PASSWORD>";
```