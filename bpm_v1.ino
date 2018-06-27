#include <ESP8266WiFi.h>

const int maxAvgSample = 20;
int rate[maxAvgSample];                    // used to hold last ten IBI values
unsigned long sampleCounter = 0;          // used to determine pulse timing
unsigned long lastBeatTime = 0;           // used to find the inter beat interval
int P =512;                      // used to find peak in pulse wave
int T = 512;                     // used to find trough in pulse wave
int thresh = 512;                // used to find instant moment of heart beat
int amp = 100;                   // used to hold amplitude of pulse waveform
boolean firstBeat = true;        // used to seed rate array so we startup with reasonable BPM
boolean secondBeat = true;       // used to seed rate array so we startup with reasonable BPM
int BPM;                   // used to hold the pulse rate
int Signal;                // holds the incoming raw data
int IBI = 600;             // holds the time between beats, the Inter-Beat Interval
boolean Pulse = false;     // true when pulse wave is high, false when it's low
boolean QS = false;  

int c=0;
int sb=0;

const char* ssid = "Redmi";
const char* password = "45678901";

const char* server = "api.thingspeak.com";
String apiKey = "GR0LAO4MJZDHZZ4M";  

const String ifttt_trigger = "Heart_attack";
const String ifttt_key = "kC5uBCvbLmKvSnintdCRo";

const char hostname[] = "maker.ifttt.com";
const String uri = "/trigger/" + ifttt_trigger +
                    "/with/key/" + ifttt_key;

WiFiClient client;

void setup() {
  Serial.begin(115200);
  delay(10);
  
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password); 
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected"); 
  delay(5);
  Test();
  delay(2);
}

void loop() {  
  Test();
  delay(2);
  sb+=BPM;
  if(c>=8000)
  {
    sb=sb/8000;
    Serial.println(sb);
    if (client.connect(server,80))   
                      {  
                            
                             String postStr = apiKey;
                             postStr +="&field1=";
                             postStr += String(sb);
                             postStr += "\r\n\r\n";
 
                             client.print("POST /update HTTP/1.1\n");
                             client.print("Host: api.thingspeak.com\n");
                             client.print("Connection: close\n");
                             client.print("X-THINGSPEAKAPIKEY: "+apiKey+"\n");
                             client.print("Content-Type: application/x-www-form-urlencoded\n");
                             client.print("Content-Length: ");
                             client.print(postStr.length());
                             client.print("\n\n");
                             client.print(postStr);
                             delay(500);
                             client.stop();
                        }
          if (sb<120) {
 
    // Connect to server
    if ( client.connect(hostname, 80)) {
 
    client.print("GET " + uri + " HTTP/1.1\r\n" + 
                "Host: " + hostname + "\r\n" +
                "Connection: close\r\n\r\n");
    delay(500);
    }
    client.stop();
    }
    c=0;
    sb=0;
  }   
 }
void Test()
{
  c++;

      Signal = analogRead(A0);              // read the Pulse Sensor 
        sampleCounter += 2;                         // keep track of the time in mS with this variable
    int N = sampleCounter - lastBeatTime;       // monitor the time since the last beat to avoid noise

      if(Signal < thresh && N > (IBI/5)*3){       // avoid dichrotic noise by waiting 3/5 of last IBI
        if (Signal < T){                        // T is the trough
            T = Signal;                         // keep track of lowest point in pulse wave 
         }
       }
      
    if(Signal > thresh && Signal > P){          // thresh condition helps avoid noise
        P = Signal;                             // P is the peak
       }                                        // keep track of highest point in pulse wave
    
  //  NOW IT'S TIME TO LOOK FOR THE HEART BEAT
  // signal surges up in value every time there is a pulse
if (N > 250){                                   // avoid high frequency noise
  if ( (Signal > thresh) && (Pulse == false) && (N > (IBI/5)*3) ){        
    Pulse = true;                               // set the Pulse flag when we think there is a pulse
    //digitalWrite(blinkPin,HIGH);                // turn on pin 13 LED
    IBI = sampleCounter - lastBeatTime;         // measure time between beats in mS
    lastBeatTime = sampleCounter;               // keep track of time for next pulse
         
         if(firstBeat){                         // if it's the first time we found a beat, if firstBeat == TRUE
             firstBeat = false;                 // clear firstBeat flag
             return;                            // IBI value is unreliable so discard it
            }   
         if(secondBeat){                        // if this is the second beat, if secondBeat == TRUE
            secondBeat = false;                 // clear secondBeat flag
               for(int i=0; i<=maxAvgSample-1; i++){         // seed the running total to get a realisitic BPM at startup
                    rate[i] = IBI;                      
                    }
            }
          
    // keep a running total of the last 10 IBI values
    word runningTotal = 0;                   // clear the runningTotal variable    

    for(int i=0; i<=(maxAvgSample-2); i++){                // shift data in the rate array
          rate[i] = rate[i+1];              // and drop the oldest IBI value 
          runningTotal += rate[i];          // add up the 9 oldest IBI values
        }
        
    rate[maxAvgSample-1] = IBI;                          // add the latest IBI to the rate array
    runningTotal += rate[maxAvgSample-1];                // add the latest IBI to runningTotal
    runningTotal /= maxAvgSample;                     // average the last 10 IBI values 
    BPM = 60000/runningTotal;               // how many beats can fit into a minute? that's BPM!
    QS = true;                              // set Quantified Self flag 
    // QS FLAG IS NOT CLEARED INSIDE THIS ISR
    }                       
}

  if (Signal < thresh && Pulse == true){     // when the values are going down, the beat is over
      //digitalWrite(blinkPin,LOW);            // turn off pin 13 LED
      Pulse = false;                         // reset the Pulse flag so we can do it again
      amp = P - T;                           // get amplitude of the pulse wave
      thresh = amp/2 + T;                    // set thresh at 50% of the amplitude
      P = thresh;                            // reset these for next time
      T = thresh;
     }
  
  if (N > 2500){                             // if 2.5 seconds go by without a beat
      thresh = 512;                          // set thresh default
      P = 512;                               // set P default
      T = 512;                               // set T default
      lastBeatTime = sampleCounter;          // bring the lastBeatTime up to date        
      firstBeat = true;                      // set these to avoid noise
      secondBeat = true;                     // when we get the heartbeat back
     }
  
                            
}
