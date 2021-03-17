package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public class Wall{

    private int x;
    private int y;

    private PImage sprite;

    public boolean isEmpty;

    public Wall() {
        this.isEmpty = true;
    }

    public Wall(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.isEmpty = false;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.sprite.width;
    }

    public int getHeight() {
        return this.sprite.height;
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