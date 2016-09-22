
#include <NewPing.h> //ultrasonic library
String distance="0";
String parking_result="";
NewPing sonar(3, 2, 200);//trig,echo,max
int slot1 = A0;//ldr slot1
int slot2 = A1;//ldr slot2
int slot3 = A2;//ldr slot3
int slot4 = A3;//ldr slot4
int temp_sensor1 = A4;
int temp_sensor2 = A5;// select the input pin for ldr
int reading=0;
float temp1=0;
float temp2=0;
int length_of_container=9;//9cm is the total length of my water container 


void setup() {
  // put your setup code here, to run once:

  pinMode(13,OUTPUT); //GATE (LED) PIN
  
   pinMode(9, OUTPUT); //Motor pins
 pinMode(10, OUTPUT); //Motor pins
Serial.begin(9600);

}

void loop() {
  // put your main code here, to run repeatedly:
if(Serial.available())
{

  char s=Serial.read();
 //IF THERE IS SOME MESSAGE FROM PI THEN SAVE IT IN s
  if(s=='c') //motor on
 {
   
   
 digitalWrite(9, HIGH);
  digitalWrite(10, LOW);
 }
 else if(s=='d')//motor off
 {

    
 digitalWrite(9, LOW);
  digitalWrite(10, LOW);
 }
  else if(s=='e')//LED ON
 {
   digitalWrite(13, HIGH);
 }
  else if(s=='f')//LED OFF
 {
   digitalWrite(13, LOW);
 }
 
}
parking_result="";
int slot1_value = analogRead(slot1);
int slot2_value = analogRead(slot2);
int slot3_value = analogRead(slot3);
int slot4_value = analogRead(slot4);// read the value from the sensor
//if your sensor is giving some different values on object placed
//then change below values accordingly
if(slot1_value>1000){parking_result="1!";}else{parking_result="0!";}
if(slot2_value>1000){parking_result+="1!";}else{parking_result+="0!";}
if(slot3_value>1000){parking_result+="1!";}else{parking_result+="0!";}
if(slot4_value>1000){parking_result+="1!";}else{parking_result+="0!";}

distance = String(scan());//returns waterlevel,convert int to string
temp1=get_temperature_1();//get temperature 1
temp2=get_temperature_2();//get temperature 2



Serial.println(parking_result+"-"+distance+"-"+String(temp1)+"!"+String(temp2)+"-");

delay(5000);//interval to update device
}
int scan()
{
 int uS = sonar.ping();

  return (length_of_container-(uS / US_ROUNDTRIP_CM)); 

 
}
float get_temperature_1()
{
  int rawvoltage= analogRead(temp_sensor1);
float millivolts= (rawvoltage/1024.0) * 5000;
float c= millivolts/10;
return c;

}
float get_temperature_2()
{
  int rawvoltage= analogRead(temp_sensor2);
float millivolts= (rawvoltage/1024.0) * 5000;
float c= millivolts/10;
return c;
}
