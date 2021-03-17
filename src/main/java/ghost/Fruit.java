package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public class Fruit{

    public int x;
    public int y;

    /**
     * If the fruit is eaten, isEaten is true. 
     * Otherwise, false.
     */
    private boolean isEaten;

    public PImage sprite;

    public Fruit(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.isEaten = false;
        this.sprite = sprite;
    }
    
    public boolean getIsEaten() {
        return this.isEaten;
    }

    public void setIsEaten(boolean isEaten) {
        this.isEaten = isEaten;
    }

    /**
     * Draw the sprite
     * 
     * @param app PApplet
     */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }
}
