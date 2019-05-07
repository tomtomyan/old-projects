//Tom Yan
//Feb 18, 2016
//Button class

class Button {
  int x; //x position
  int y; //y position
  int w; //width
  int h; //height
  
  //constructor that takes all values and sets them
  public Button (int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }
  
  void draw() {
    //if mouse is hovering the button
    if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h) {
      fill(51); //fill slightly grey
    } else {
      fill(0); //fill black
    }
    rect(x, y, w, h, 10); //draws a rectangle for the button (last parameter is for rounded corners)
  }
  
  //if mouse is pressing the button
  boolean mousePressed() {
    if (mousePressed && mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h) {
      return true;
    } else {
      return false;
    }
  }
  
}