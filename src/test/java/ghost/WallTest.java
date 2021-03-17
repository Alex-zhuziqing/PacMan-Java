package ghost;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;

class WallTest{

    /**
     * Test for getting the width of the sprite
     */
    @Test
    public void testWall_getWidth() {
        App app = new App();
        PApplet.runSketch(new String[]{"test"}, app);

        Wall wall = new Wall(0, 0, app.loadImage("src/main/resources/horizontal.png"));

        assertEquals(16, wall.getWidth());
    }


     /**
     * Test for getting the height of the sprite
     */
    @Test
    public void testWall_getHeight() {
        App app = new App();
        PApplet.runSketch(new String[]{"test"}, app);

        Wall wall = new Wall(0, 0, app.loadImage("src/main/resources/horizontal.png"));

        assertEquals(16, wall.getHeight());
    }
}