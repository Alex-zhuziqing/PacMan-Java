package ghost;

import processing.core.PApplet;
import processing.core.PImage;

public class SuperFruit extends Fruit {

    public SuperFruit(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

    /**
     * Draw the sprite
     * 
     * @param app PApplet
     */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x - 10, this.y - 10);
    }
    
}