#include <ESP8266WiFi.h>//Enables WiFi functionalities
#include "ThingSpeak.h"
// Pins
const int temp_Pin = A0;

// WiFi config
const char ssid[] = "Redmi";
const char password[] = "45678901";

// ThingSpeak config
const int channel_id = 414656;
const char write_api_key[] = "VB43C2P4BC32WVRV";

// WiFiClient object
WiFiClient client;

void setup() {
  
  // Initialize Serial
  Serial.begin(9600);
  delay(100);
  // Connect to WiFi
  Serial.print("Connecting to ");
  Serial.print(ssid);
  WiFi.begin(ssid, password);
  while ( WiFi.status() != WL_CONNECTED ) {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  // Show that we are connected
  Serial.println("Connected!");

  // Connect to ThingSpeak
  ThingSpeak.begin(client);
}

void loop() {

  // Read temperature sensor (remember to multiply by 3!)
int val = analogRead(temp_Pin);
float mv = ( val/1024.0)*3300; 
float cel = mv/10;
  Serial.print("Voltage: ");
  Serial.print(val);
  Serial.print("V degC: ");
  Serial.println(cel);
  // Write values to our ThingSpeak channel
  Serial.println("Posting " + String(cel, 2) + " to ThingSpeak");
  ThingSpeak.setField(1, String(cel, 2));
  ThingSpeak.writeFields(channel_id, write_api_key);
  delay(30000);
}
