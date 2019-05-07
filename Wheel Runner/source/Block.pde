//Tom Yan
//Feb 18, 2016
//Block Class (platforms)

class Block {
  int y; //y position of the block
  int h; //height of the block
  float start; //start position of the arc (in radians)
  float stop; //stop position of the arc (in radians)
  
  ///constructor that takes all values and sets default height to 20
  Block(int y, float start, float stop) {
    this.y = y;
    h = 20;
    this.start = start;
    this.stop = stop;
  }
  
  void draw() {
    stroke(255); //draw it as white
    if (start == PI * 31) { //if it is the last block
      stroke(0, 255, 0); //draw it as green
    }
    
    //draws two arcs to simulate a platform
    arc(0, 0, y, y, start, stop);
    arc(0, 0, y+h, y+h, start, stop);
  }
  
  //return string representation of a block
  String toString() {
     return "Height: " + h + "\nStart: " + start + "\nStop: " + stop;
  }

}