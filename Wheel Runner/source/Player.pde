//Tom Yan
//Feb 18, 2016
//Player class

class Player {
  float x; //x position of player
  float y; //y position of player

  float vY; //velocity in y direction of player
  final float g = 1; //acceleration of gravity

  boolean grounded; //if the player is grounded
  boolean jumping; //if the player is jumping
  boolean outside; //if the player is outside the circle
  
  //constructor that sets default values as well as take in x, y coordinates
  Player (float x, float y) {
    this.x = x;
    this.y = y;
    vY = 0;
    jumping = false;
    grounded = true;
    outside = true;
  }
  
  //jump function
  void jump() {
    if (!jumping && grounded) { //if the player is grounded and not currently jumping
      if (outside) {
        vY += 15; //increase y velocity if outside of circle
      } else {
        vY -= 15; //decrease y velocity if inside of circle
      }
      
      //sets jumping to true and grounded to false
      jumping = true;
      grounded = false;
    }
  }

  //function to flip the side (outside/inside) of the player
  void flipSide() {
    //will only flip if it is within a certain range of the ground circle
    if (outside && y < 255) {
      outside = false;
    } else if (!outside && y > 245) {
      outside = true;
    }
  }

  void draw() {
    //add acceleration of gravity to y velocity
    if (outside) {
      vY -= g;
    } else {
      vY += g;
    }

    grounded = false; //sets grounded to false so it can always test collision
    
    //tests collision for each block
    for (int i = 0; i < b.size(); i++) {
      collisionDetect(b.get(i));
    }

    if (outside) {
      if (y < 250) {
        //if it is outside and touching the ground it will set the speed to be faster
        jumping = false;
        grounded = true;
        vY = 0;
        y = 250;
        speed = 0.03;
      }
    } else {
      if (y > 250) {
        //if it is inside and touching the ground it will set the speed to be faster
        jumping = false;
        grounded = true;
        vY = 0;
        y = 250;
        speed = 0.03;
      }
    }

    if (grounded) { //sets y velocity to zero if player is grounded
      vY = 0;
    }

    y += vY; //add y velocity to y position
    
    stroke(255);
    //draws triangle as the player depending on if it's outside or inside
    if (outside) {
      triangle(x-10, y, x+10, y, x, y+15);
    } else {
      triangle(x-10, y, x+10, y, x, y-15);
    }
  }


  //vector based collision detection that takes in a block as a parameter
  void collisionDetect(Block b) {
    //get the vector of block width and player in radians (replacing x-axis) and y
    float vR = (-r + PI/2) - ((b.start + (b.stop-b.start) / 2)), 
      vY = (y + 7.5) - (b.y/2 + (b.h / 2)), 
      // add the half widths and half heights of the player and the block
      hWidths = 0.035 + (b.stop - b.start) / 2, 
      hHeights = 7.5 + (b.h / 2);

    // if the x and y vector are less than the half width or half height, then they must be colliding
    if (Math.abs(vR) < hWidths && Math.abs(vY) < hHeights) {
      if (b.start == PI * 31) { //if player is touching the end block
        endGame = true; //plays the end game animation
      }
      //calculates the offset of the vector so it can be added depending on which side it is colliding
      float oX = hWidths - Math.abs(vR), 
        oY = hHeights - Math.abs(vY);
        //0.004 radians approximately equals one x unit in the circle
      if (oX / 0.004 >= oY) { //if the offset for x is greater than y
        if (outside) {
          if (vY > 0) {
            //bottom collision
            y += oY;
            jumping = false;
            grounded = true;
            speed = 0.01;
          } else {
            //top collision
            y -= oY;
            vY *= -1;
          }
        } else {
          if (vY > 0) {
            //bottom collision
            y += oY;
            vY *= -1;
          } else {
            //top collision
            y -= oY;
            jumping = false;
            grounded = true;
            speed = 0.01;
          }
        }
      } else { //if the offset for y is greater for y than x
        if (vR > 0) {
          //left collision
          r += oX;
          jumping = false;
        } else {
          //right collision
          r -= oX;
          jumping = false;
        }
      }
    }
  }
}