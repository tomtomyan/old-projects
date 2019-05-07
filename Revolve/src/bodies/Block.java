/*
Sam Macpherson, Tom Yan, Aseem Malhotra
Dec 16, 2015
An abstract class used for the Blocks heirarchy
*/
package bodies;

public abstract class Block implements BlockActions {
    //the x & y coordinates of the block, and it's width and height
    protected int x;
	protected int y;
	protected int w;
	protected int h; 
    
    /**
     * Creates a block with some defaulted properties.
     */
    public Block() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
    }
    
    /**
     * Creates a block with a defined x and y, with the default w and h
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        w = 20;
        h = 20;
    }
    
    /**
     * Creates a block with all the features defined by the code
     * @param x the x coordinate
     * @param y the y coordinate
     * @param w the width
     * @param h the height
     */
    public Block(int x, int y, int w, int h) {
        this(x,y);
        this.w = w;
        this.h = h;
    }

    /**
     * Gets the x coordinate of a block
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate of a block
     * @param x the new x coordinate for the block
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate for a block
     * @return the y coordinate being asked for
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate for a block
     * @param y the new y coordinate for the block
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the width of a block
     * @return the block's width
     */
    public int getW() {
        return w;
    }

    /**
     * Sets the width of a block
     * @param w the new width for the block
     */
    public void setW(int w) {
        this.w = w;
    }

    /**
     * Gets the height of a block
     * @return the block's height
     */
    public int getH() {
        return h;
    }

    /**
     * Sets the height of a block 
     * @param h the new height for the block
     */
    public void setH(int h) {
        this.h = h;
    }

}
