int p1control = 0; //potentiometer pin for player 1
int p2control = 1; //potentiometer pin for player 2
int p1start = 8; //push button (launch ball) pin for player 1 
int p2start = 6; //push button (launch ball) pin for player 2

int p1speaker = 2;
int p2speaker = 3;

void setup() {
  pinMode(p1control, INPUT_PULLUP); // Set pin as INPUT
  pinMode(p2control, INPUT_PULLUP); // Set pin as INPUT
  pinMode(p1start, INPUT); // Set pin as INPUT
  pinMode(p2start, INPUT); // Set pin as INPUT
  //initialize serial communications at a 9600 baud rate
  Serial.begin(9600);
  establishContact();  // send a byte to establish contact until receiver responds
}

void loop() {
  int p1Reading = analogRead(p1control); //takes the reading of potentiometer
  int p2Reading = analogRead(p2control); //takes the reading of potentiometer
  //prints info to Serial so processing can read the info
  Serial.println(10000 + p1Reading);
  Serial.println(20000 + p2Reading);
  
  int val1 = digitalRead(p1start); //reads the button state
  int val2 = digitalRead(p2start); //reads the button state
  
  if (val1 == HIGH) {
    Serial.println(3); //sends a 3 to Serial if it is pushed
    tone(p1speaker, 5000);
  } 
  if (val2 == HIGH) {
    Serial.println(4); //sends a 4 to Serial if it is pushed
    tone(p2speaker, 5000);
  } 
  delay(10); //delay so it doesn't go too fast
  noTone(p1speaker);
  noTone(p2speaker);
}

//Interfacing with processing
void establishContact() {
  while (Serial.available() <= 0) {
    Serial.println("A");   //send a capital A
    delay(300);
  }
}

