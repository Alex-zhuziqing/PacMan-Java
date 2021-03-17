package ghost;

import processing.core.PImage;

public class Waka extends MovableObject{

    private int lives;
    private boolean isClosed;

   
    public Waka(int x, int y, PImage sprite) {
        super(x, y, sprite);

        this.lives = 0;
        this.isClosed = true;
    }

   
    public int getLives() {
        return this.lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean getIsClosed() {
        return this.isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }


    /**
     * Reverse the boolean isClosed.
     * <p> If it is true, it becomes false;
     * if it is false, it becomes true.
     */
    public void reverseIsClosed() {
        if (this.isClosed) {
            this.isClosed = false;
        } else {
            this.isClosed = true;
        }
    }

}