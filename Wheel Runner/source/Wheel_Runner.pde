//Tom  Yan
//Feb 18, 2016
//A platformer where the central platform rotates and the player can switch between inside and outside.
//The objective is to reach the end while avoiding the red wall.
//Platforms will allow the player to move faster relative to the rotation.

float r; //value of rotate variable in radians (player controlled)
float r2; //value of rotate (automated)
float wallR; //position of the wall in randians

Player p = new Player(0, 250); //instantiates new Player object

boolean keys[]  = new boolean[4]; //array for key presses (so you can press multiple at once)

public ArrayList<Block> b = new ArrayList(); //Array list of blocks
public ArrayList<Button> buttons = new ArrayList(); //Array list of buttons

boolean gameStart = false; //if game has started
boolean mainMenu = true; //if the player is on the main menu
boolean helpMenu = false; //if the player is on the help menu
boolean settingsMenu = false; //if the player is on the settings menu
boolean creditsMenu = false; //if the player is on the credits menu
boolean endGame = false; //if the end game has been reached
boolean loseGame = false; //if the game is lost

int score = 0; //game score

float speed = 0.05; //speed of rotation

//end game animation
int frameCounter = 0; //frames that have run since animation started
int letterX, letterY; //used for positioning letters
int maxLetters = 0; //maximum letters on display
//words to "type" by the computer
String text = "display text";
String text2 = "score";
String text3 = "credits";


void setup() {
  size(800, 800); //size of canvas
  background(0); //sets background colour to black

  generateBlocks(); //generates all the blocks

  //initialize buttons
  buttons.add(new Button(260, 240, 280, 75)); //start game
  buttons.add(new Button(350, 390, 100, 30)); //help
  buttons.add(new Button(350, 440, 100, 30)); //settings
  buttons.add(new Button(350, 490, 100, 30)); //credits
  buttons.add(new Button(350, 590, 100, 30)); //exit
  buttons.add(new Button(350, 540, 100, 30)); //back
}

void generateBlocks() {//generates all the blocks randomly
  b.clear(); //clears existing ArrayList
  //initialize ArrayList of blocks randomly
  for (float i = 0; i < 30 * PI; i += PI / 2) {
    int y = (int) (Math.random() * 150 + 600); //random y value outside of circle
    int y2 = (int) (Math.random() * 150 + 250); //random y value inside circle
    
    float start = (float) (Math.random() * PI / 4 + i); //random start value from a point
    float stop = (float) (Math.random() * PI / 4 + start + PI/8); //stop value following the start value
    
    b.add(new Block(y, start, stop)); //adds platform on the outside of the circle
    b.add(new Block(y2, start + PI*2, stop + PI*2)); //adds platform on the inside of the circle
    //System.out.println("Block start: " + start + ", block stop: "  + stop);
  }
  b.add(new Block(600, PI * 31, PI * 32)); //adds the end block (will be green)
}

void draw() {
  background(0); //sets the background to black
  noFill(); //only shows the outline of shapes
  stroke(255); //sets outline to white

  if (!gameStart) { //if game hasn't started, show the menu screen
    ellipse(400, 400, 500, 500); //draws the circle surrounding the menu

    if (mainMenu) { //displays main menu
      for (int i = 0; i < 5; i++) { //iterates through for loop to see if a button is pressed
        buttons.get(i).draw(); //draws each button
        if (buttons.get(0).mousePressed()) { //start game
          //sets initial rotation values
          generateBlocks();
          r = PI; 
          r2 = 0;
          gameStart = true;
          loseGame = false;
        }
        if (buttons.get(1).mousePressed()) { //help
          mainMenu = false;
          helpMenu = true;
        }
        if (buttons.get(2).mousePressed()) { //settings
          mainMenu = false;
          settingsMenu = true;
        }
        if (buttons.get(3).mousePressed()) { //credits
          mainMenu = false;
          creditsMenu = true;
        }
        if (buttons.get(4).mousePressed()) { //exit
          System.exit(0); //exits game
        }
      }

      //dislays menu text on top of buttons
      fill(255);
      textFont(createFont("Consolas", 48));
      text("Start Game", 270, 295);
      textFont(createFont("Consolas", 20));
      text("Help", 379, 411);
      text("Settings", 357, 461);
      text("Credits", 362, 511);
      text("Exit", 378, 611);
    } else { //displays other menus
      buttons.get(5).draw(); //draws back button
      if (buttons.get(5).mousePressed()) { //if back button is pressed
        //sets main menu to true and every other menu to false
        mainMenu = true;
        helpMenu = false;
        settingsMenu = false;
        creditsMenu = false;
      }
      
      //draws back text
      fill(255);
      text("Back", 379, 561);
      
      if (helpMenu) { //if on help menu
        //draw the following text
        text("Use WASD for movement", 290, 250);
        text(" It is possible to switch between", 210, 300);
        text("inside and outside of the circle", 220, 325);
        text("Platforms will increase your speed", 210, 375);
        text("The objective of the game is to", 220, 425);
        text("reach the end", 330, 450);
        
      } else if (settingsMenu) { //if on settings menu
        //draw the following text
        text("Coming Soon", 342, 375);
        
      } else if (creditsMenu) { //if on credits menu
        //draw the following text
        text("Made using Processing 3.0.2", 254, 300);
        text("Programmer: Tom Yan", 300, 450);
      }
    }
  } else if (endGame) { //if end game is reached (simulates typing on linux terminal)
    //use the following font to simulate a linux terminal
    fill(0, 255, 0);
    noStroke();
    textFont(createFont("Consolas", 20));
    text("tom@localhost:~$", 50, 50);

    frameCounter++; //increases frame counter
  
    //initial position of letters
    letterX = 235;
    letterY = 50;
    
    //draws out the first word
    for (int i = 0; i < maxLetters && i < text.length(); i++) {
      text(text.substring(i, i+1), letterX, letterY);
      letterX += 10;
    }
    
    //if it goes beyond the first word
    if (maxLetters > text.length()) {
      //draws additional text
      text("Congratulations, you have beaten this game!", 50, 75);
      text("tom@localhost:~$", 50, 100);
      
      //increases position of letters
      letterX = 235;
      letterY = 100;
      
      //after it waits for frame counter to go up
      if (frameCounter > 320) {
        
        //draw second word
        for (int i = 0; i + text.length() < maxLetters && i < text2.length(); i++) {
          text(text2.substring(i, i+1), letterX, letterY);
          letterX += 10;
        }
        
        //increase max letters every 15 frames
        if (frameCounter % 15 == 0) {
          maxLetters++;
        }
      }
      
      //if it goes beyond the first and second word
      if (maxLetters > text.length() + text2.length()) {
        //draws additional text
        text("Your score is " + score, 50, 125);
        text("tom@localhost:~$", 50, 150);
        
        //increases position of letters
        letterX = 235;
        letterY = 150;
        
        //if letters have reached over 30
        if (maxLetters > 30) {
          
          //draw third word
          for (int i = 0; i + 30 < maxLetters && i < text3.length(); i++) {
            text(text3.substring(i, i+1), letterX, letterY);
            letterX += 10;
          }
        }
        
        //if it goes beyond the first, second and third word
        if (maxLetters > 30 + text3.length()) {
          //draws additional text
          text("Programmed by Tom Yan", 50, 175);
          text("tom@localhost:~$", 50, 200);
          
          //increases position of letters
          letterX = 235;
          letterY = 200;
          
          //draws exit button
          stroke(0, 255, 0);
          buttons.get(5).draw();
          fill(0, 255, 0);
          text("Exit", 379, 561);
          
          if (buttons.get(5).mousePressed()) { //exit button
            //returns user back to main menu if it's presssed
            gameStart = false;
            endGame = false;
            mainMenu = true;
            frameCounter = 0;
            maxLetters = 0;
          }
        }
      }
    } else {
      //increase max letters every 15 frames
      if (frameCounter % 15 == 0) {
        maxLetters++;
      }
    }
    
    fill(0, 255, 0);
    noStroke();
    //draws the little square beside the typed character every 20 frames
    if (floor(frameCounter/20) % 2 == 0) {
      rect(letterX+5, letterY-15, 10, 15);
    }
  } else if (loseGame) { //if the player reaches the red wall
    ellipse(400, 400, 500, 500); //draws circle that surrounds
    
    //draws restart button
    buttons.get(0).draw();
    if (buttons.get(0).mousePressed()) {
      //restart game if it is pressed
      generateBlocks();
      r = PI;
      r2 = 0;
      gameStart = true;
      loseGame = false;
    }
    
    //draws restart text
    fill(255);
    textFont(createFont("Consolas", 48));
    text("Restart", 310, 295);
    
    //draws exit button
    buttons.get(5).draw();
    if (buttons.get(5).mousePressed()) {
      //returns to main menu if it is pressed
      mainMenu = true;
      helpMenu = false;
      gameStart = false;
      loseGame = false;
      settingsMenu = false;
      creditsMenu = false;
    }
    
    //draws exit text
    fill(255);
    textFont(createFont("Consolas", 20));
    text("Exit", 379, 561);
    
    //draws score
    text("Score: " + score, 350, 420);
  } else { //if the game has started
    pushMatrix(); //add new transformation matrix
    fill(0);
    noFill(); //only shows the outline of shapes  

    rotate(PI); //intiially rotates the canvas by PI radians (180 degrees)
    translate(-400, -400); //translates the screen left up so that the middle is (0, 0)
    rotate(r2); //rotates the canvas by r2 (self rotate)

    wallR = -r2; //position of wall is negative of r2 so it's always on the left
    //draws red wall
    fill(255, 0, 0);
    arc(0, 0, 1200, 1200, wallR - PI/32, wallR + PI/32);
    
    //draws an invisible circle at the center so wall doesn't look like arc
    fill(0);
    noStroke();
    ellipse(0, 0, 200, 200);
    stroke(255);
    
    r2 -= speed; //sets rotate to negative speed to go backward    

    noFill();
    pushMatrix(); //adds a new transformation matrix (for rotating platforms)

    ellipse(0, 0, 500, 500); //creates a circle at the origin with a radius of 500/2
    rotate(r); //rotates the canvas about the origin by r radians

    //uses the array of keys to perform actions based on what key is pressed
    if (keys[0]) { //w key
      //flips side or jump depending on side
      if (!p.outside) {
        p.flipSide();
      } else {
        p.jump();
      }
    }
    if (keys[1]) { //a key
      //moves both rotational matrixes back
      r2 -= speed;
      r += 0.03;
    }
    if (keys[2]) { //s key
      //flips side or jump depending on side
      if (p.outside) {
        p.flipSide();
      } else {
        p.jump();
      }
    }
    if (keys[3]) { //d key
      //moves forward
      r -= 0.03;
      r2 += 0.023;
    }
    
    //draws all the blocks in the ArrayList if it's within a certain range of the player
    for (int i = 0; i < b.size(); i++) {
      if (b.get(i).start * -1 < r && b.get(i).start * -1> r- PI * 3 / 2) {
        b.get(i).draw();
      }
    }
    
    popMatrix(); //pops the transformation matrix

    //lose condition (if the wall reaches you
    if (wallR % PI > (PI/2 - PI/32) && wallR % PI < (PI/2 + PI/32)) {
      loseGame = true;
    }

    p.draw(); //draws the player (triangle)

    popMatrix(); //pops the transformation matrix
    
    //calculates score and displays it on top left corner
    fill(255);
    score = (int) Math.floor((int)((PI-r)*100));
    text("Score: " + score, 50, 50);    
  }
}

void keyPressed() {
  //if a key is pressed the element will be set to true
  if (key == 'w') {
    keys[0] = true;
  }
  if (key == 'a') {
    keys[1] = true;
  }
  if (key == 's') {
    keys[2] = true;
  }
  if (key == 'd') {
    keys[3] = true;
  }
  if (key == 'g') { //skip to the end game animation
    endGame = true;
  }
}

void keyReleased() {
  //if a key is released the element will be set to false
  if (key == 'w') {
    keys[0] = false;
  }
  if (key == 'a') {
    keys[1] = false;
  }
  if (key == 's') {
    keys[2] = false;
  }
  if (key == 'd') {
    keys[3] = false;
  }
}