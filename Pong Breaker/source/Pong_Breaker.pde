import processing.serial.*; //import the Serial library
import java.util.ArrayList; //import the ArrayList library

boolean keys[] = new boolean[4];
boolean gameOver = false;
Serial myPort;  //the Serial port object
String val;
// since we're doing serial handshaking, 
// we need to check if we've heard from the microcontroller
boolean firstContact = false;

int p1x = 0; //top player x value
int p2x = 0; //bottom player x value
int p1y = 40; //top player y value
int p2y = 450; //bottom player y value

int player1Lives = 3; //number of lives for p1
int player2Lives = 3; //number of lives for p2

boolean p1Launched = false; //if ball has launched from p1 paddle
boolean p2Launched = false; //if ball has launched from p2 paddle

final int h = 10; //height of paddle
final int w = 80; //width of paddle

ArrayList<Ball> balls = new ArrayList(); //ArrayList of balls
ArrayList<Brick> bricks = new ArrayList(); //ArrayList of bricks
void setup() {
  size(500, 500); //make our canvas 200 x 200 pixels big
  //initialize your serial port and set the baud rate to 9600 
  
  //Comment out to use Arduino
  //myPort = new Serial(this, Serial.list()[1], 9600);
  //myPort.bufferUntil('\n');


  balls.add(new Ball(p1x + 40, 55, 0, 0)); //add new ball for p1
  balls.add(new Ball(p2x + 40, 445, 0, 0)); //add new ball for p2

  //initializes the bricks
  for (int i = 0; i < 5; i++) {
    for (int j = 0; j < 5; j++) {
      bricks.add(new Brick(50 + i*80, 187 + j*25, 80, 25));
    }
  }
}

void draw() {
  background(255); //sets background to white
  fill(0); //set the fill colour to black
  if (player1Lives == 0 || player2Lives == 0) {
    gameOver = true;
    rect(-10, -10, 500, 500);
    fill(255, 255, 255);
    String s = "Game Over, press 'r' to restart";
    text(s, 10, 10, 70, 80);  // Text wraps within text box
  } else {
    gameOver = false;

    noStroke();

    //draws the paddles
    rect(p1x, p1y, w, h);
    rect(p2x, p2y, w, h);

    stroke(0);
    fill(255);


     if (keys[0]) {
     p2x-=10;
     if (p2x < 0) {
     p2x = 0;
     }
     }
     if (keys[1]) {
     p2x+=10;
     if (p2x >420) {
     p2x = 420;
     }
     }
     if (keys[2]) {
     p1x-=10;
     if (p1x < 0) {
     p1x = 0;
     }
     }
     if (keys[3]) {
     p1x+=10;
     if (p1x >420) {
     p1x = 420;
     }
     }


    //ball collision
    for (int i = 0; i < balls.size(); i++) {
      ellipse((float)((balls.get(i)).getX()), (float)((balls.get(i)).getY()), (float)10, (float)10);
      balls.get(i).moveBall();
      if ((balls.get(i)).getY() <= 50) {
        if (((balls.get(i)).getX()) < (p1x + 80) && ((balls.get(i)).getX()) > (p1x)) {


          float offset = - (p1x + 40 - balls.get(i).ballx) / 7 + 0.1;

          balls.get(i).xspeed = offset;
          balls.get(i).yspeed = Math.abs((float)Math.sqrt(25/ balls.get(i).xspeed * balls.get(i).xspeed));
        } else {

          player1Lives--;
          if (i==0) {
            p1Launched = false;
            balls.get(i).ballx = p1x + 40;
            balls.get(i).bally = 55;
            balls.get(i).xspeed = 0;
            balls.get(i).yspeed = 0;
          } else {
            p2Launched = false;
            balls.get(i).ballx = p2x + 40;
            balls.get(i).bally = 445;
            balls.get(i).xspeed = 0;
            balls.get(i).yspeed = 0;
          }
        }
      }
      if ((balls.get(i)).getY() >= 450) {
        if (((balls.get(i)).getX()) < (p2x + 80) && ((balls.get(i)).getX()) > (p2x)) {

          float offset = - (p2x + 40 - balls.get(i).ballx) / 7 - 0.1;

          balls.get(i).xspeed = offset;
          balls.get(i).yspeed = -Math.abs((float)Math.sqrt(25/ balls.get(i).xspeed * balls.get(i).xspeed));
        } else {

          player2Lives--;
          if (i==0) {
            p1Launched = false;
            balls.get(i).ballx = p1x + 40;
            balls.get(i).bally = 55;
            balls.get(i).xspeed = 0;
            balls.get(i).yspeed = 0;
          } else {
            p2Launched = false;
            balls.get(i).ballx = p2x + 40;
            balls.get(i).bally = 445;
            balls.get(i).xspeed = 0;
            balls.get(i).yspeed = 0;
          }
        }
      }
    }

    //Collision detection of bricks
    for (int i = 0; i < bricks.size(); i++) { //iterates for every brick
      float x1 = bricks.get(i).x + 40; //adds the half width of a brick to the x value to get x value for center of the brick
      float y1 = bricks.get(i).y + 12.5; //adds the half height of a brick to the y value to get y value for center of the brick

      for (int j = 0; j < balls.size(); j++) { //iterates through every ball
        float x2 = (float) balls.get(j).ballx + 5;
        float y2 = (float) balls.get(j).bally + 5;

        if (!bricks.get(i).cleared) { //if the brick hasn't been cleared yet
          if (x2 < x1 + 45 && x2 > x1 - 45) { //if the x value for the ball is within the center of the brick
            if (y2 < y1 + 17.5 && y2 > y1 - 17.5) { //if the y value for the ball is within the center of the brick
              bricks.get(i).cleared = true; //clears the brick
              if (Math.min(Math.abs(y2-(y1+17.5)), Math.abs(y2-(y1-17.5))) < Math.min(Math.abs(x2-(x1-45)), Math.abs(x2-(x1+45)))) { //determines which side (top/bottom or left/right)
                balls.get(j).yspeed *= -1; //changes direction of y speed (top/bottom collision)
              } else {
                balls.get(j).xspeed *= -1; //changes direction of x speed (left/right collision)
              }
            }
          }
        }
      }
    }

    if (!p1Launched) {
      balls.get(0).ballx = p1x + 40; //sets x position of ball to the paddle if it hasn't been launched
    }
    if (!p2Launched) {
      balls.get(1).ballx = p2x + 40; //sets x position of ball to the paddle if it hasn't been launched
    }

    for (int i = 0; i < bricks.size(); i++) {
      if (!bricks.get(i).cleared) {
        rect(bricks.get(i).x, bricks.get(i).y, bricks.get(i).w, bricks.get(i).h); //draws all the bricks if it hasn't been cleared
      }
    }
  }
}

//keyboard controls
void keyPressed() {
  if (key == 'r' || key == 'R') {
    if (gameOver) {
      player1Lives = 3;
      p1Launched = false;
      balls.get(0).ballx = p1x + 40;
      balls.get(0).bally = 55;
      balls.get(0).xspeed = 0;
      balls.get(0).yspeed = 0;
      player2Lives = 3;
      p2Launched = false;
      balls.get(1).ballx = p2x + 40;
      balls.get(1).bally = 445;
      balls.get(1).xspeed = 0;
      balls.get(1).yspeed = 0;
      for (int i = 0; i < 25; i++) {
        bricks.get(i).cleared = false;
      }
    }
  } else if (key == 'a' || key == 'A') {
    keys[0] = true;
  } else if (key == 'd' || key == 'D') {
    keys[1] = true;
  }
  if (keyCode == LEFT) {
    keys[2] = true;
  } else if (keyCode == RIGHT) {
    keys[3] = true;
  }
  if (key == 't') {  
    if (!p1Launched) {

      balls.get(0).changeSpeed(5, 5);
      p1Launched = true;
    }
  }
  if (key == 'y') {
    if (!p2Launched) {
      balls.get(1).changeSpeed(5, -5);
      p2Launched = true;
    }
  }
}

void keyReleased() {
  if (key == 'a' || key == 'A') {
    keys[0] = false;
  }
  if (key == 'd' || key == 'D') {
    keys[1] = false;
  }
  if (keyCode == LEFT) {
    keys[2] = false;
  }
  if (keyCode == RIGHT) {
    keys[3] = false;
  }
}

//Interfacing with Arduino
void serialEvent(Serial myPort) {
  //put the incoming data into a String - 
  //the '\n' is our end delimiter indicating the end of a complete packet
  val = myPort.readStringUntil('\n');
  //make sure our data isn't empty before continuing
  if (val != null) {

    //trim whitespace and formatting characters (like carriage return)
    val = trim(val);
    println(val);


    //look for our 'A' string to start the handshake
    //if it's there, clear the buffer, and send a request for data
    if (firstContact == false) {
      if (val.equals("A")) {
        myPort.clear();
        firstContact = true;
        myPort.write("A");
        println("contact");
      }
    } else { //if we've already established contact, keep getting and parsing data
      println(val);

      if (val.substring(0, 1).equals("1")) {
        p1x = (int) (Integer.parseInt(val.substring(1)) / 2.44);
      } else if (val.substring(0, 1).equals("2")) {
        p2x = (int) (Integer.parseInt(val.substring(1)) / 2.44);
      } else if (val.equals("3")) {
        if (!p1Launched) {

          balls.get(0).changeSpeed(0, 5);
          p1Launched = true;
        }
      } else if (val.equals("4")) {
        if (!p2Launched) {
          balls.get(1).changeSpeed(0, -5);
          p2Launched = true;
        }
      }
    }
  }
}


//Ball class
public class Ball {
  float ballx; 
  float bally;
  float yspeed; 
  float xspeed;

  public Ball(float bx, float by, float ys, float xs) {
    this.ballx = bx;
    this.bally = by;
    this.yspeed = ys;
    this.xspeed = xs;
  }

  public void changeSpeed(float x, float y) {
    this.yspeed = y;
    this.xspeed = x;
  }

  public float getX() {
    return this.ballx;
  }

  public float getY() {
    return this.bally;
  }

  public float getSpeedX() {
    return this.xspeed;
  }

  public float getSpeedY() {
    return this.yspeed;
  }

  public void moveBall() {
    this.ballx += this.xspeed;
    this.bally += this.yspeed;
    if (ballx > 500 || ballx < 0) {
      xspeed *= -1;
    }
    if (bally < 0 || bally > 500) {
      yspeed *= -1;
    }
  }
}

//Brick class
public class Brick {
  int x = 0;
  int y = 0;
  int w = 0;
  int h = 0;
  boolean cleared;

  //constructor for brick class - takes all parameters and sets cleared to false by default
  public Brick(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    cleared = false;
  }
}