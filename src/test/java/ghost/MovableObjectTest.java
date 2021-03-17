package ghost;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;
import processing.core.PImage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;


class MovableObjectTest {

    /**
     * Test for getX() and setX(int x)
     */
    @Test
    public void testMovableObject_getX_setX() {
        MovableObject waka = new Waka(1, 1, null);
        waka.setX(0);
        assertEquals(0, waka.getX());
    }

    /**
     * Test for getY() and setY(int y)
     */
    @Test
    public void testMovableObject_getY_setY() {
        MovableObject waka = new Waka(1, 1, null);
        waka.setY(0);
        assertEquals(0, waka.getY());
    }

    /**
     * Test for getSprite() and setSprite(PImage sprite)
     */
    @Test
    public void testMovableObject_getSprite_setSprite() {
        App app = new App();
        PApplet.runSketch(new String[]{"test"}, app);
        PImage image = app.loadImage("src/test/resources/playerClosed.png");
        MovableObject waka = new Waka(1, 1, null);
        waka.setSprite(image);
        assertEquals(image, waka.getSprite());
    }

    /**
     * Test for getSpeed() and setSpeed(int speed)
     */
    @Test
    public void testMovableObject_getSpeed_setSpeed() {
        MovableObject waka = new Waka(1, 1, null);
        waka.setSpeed(5);
        assertEquals(5, waka.getSpeed());
    }

    /**
     * Test for getWalls() and setWalls(ArrayList<Wall>)
     */
    @Test
    public void testMovableObject_getWalls_setWalls() {
        MovableObject waka = new Waka(1, 1, null);
        ArrayList<Wall> walls = new ArrayList<>(Arrays.asList(new Wall(), new Wall()));
        waka.setWalls(walls);
        assertEquals(walls, waka.getWalls());
    }

    /**
     * Test for getPaths() and setPaths(ArrayList<int[]>)
     */
    @Test
    public void testMovableObject_getPaths_setPaths() {
        MovableObject waka = new Waka(1, 1, null);
        ArrayList<int[]> paths = new ArrayList<>(Arrays.asList(new int[]{1, 1}, new int[]{2, 2}));
        waka.setPaths(paths);
        assertEquals(paths, waka.getPaths());
    }

    /**
     * Test for getWidth() and getHeight()
     */
    @Test
    public void testMovableObject_getWidth_getHeight() {
        App app = new App();
        PApplet.runSketch(new String[]{"test"}, app);
        MovableObject waka = new Waka(1, 1, app.loadImage("src/main/resources/playerClosed.png"));
        assertEquals(24, waka.getWidth());
        assertEquals(26, waka.getHeight());
    }

    /**
     * Test for getInitialPosition() and setInitialPosition(int[] initialPosition)
     */
    @Test
    public void testMovableObject_getInitialPosition_setInitialPosition() {
        MovableObject waka = new Waka(1, 1, null);
        int[] initialPosition = new int[]{10, 10};
        waka.setInitialPosition(initialPosition);
        assertEquals(initialPosition, waka.getInitialPosition());
    }

    /**
     * Test for making MovableObject back to its initial position
     */
    @Test
    public void testMovableObject_backToInitialPosition() {
        MovableObject waka = new Waka(1, 1, null);
        int[] initialPosition = new int[]{10, 10};
        waka.setInitialPosition(initialPosition);
        waka.backToInitialPosition();
        assertEquals(10, waka.getX());
        assertEquals(10, waka.getY());
    }

    /**
     * Test for drawing the sprite
     */
    @Test
    public void testMovableObject_draw() {
        App app = new App();
        PApplet.runSketch(new String[]{"test"}, app);
        app.delay(1000);
    }

    /**
     * Test for resetting the current direction to null
     */
    @Test
    public void testMovableObject_resetCurrentDirection() {
        MovableObject waka = new Waka(1, 1, null);
        waka.currentDirection = Direction.UP;
        waka.resetCurrentDirection();
        assertNull(waka.currentDirection);
    }

    /**
     * Test for moving the MovableObject
     */
    @Test
    public void testMovableObject_move() {
        MovableObject waka = new Waka(1, 1, null);
        waka.setSpeed(1);

        // moving up
        waka.currentDirection = Direction.UP;
        waka.move();
        assertEquals(0, waka.getY());
    
        // moving down
        waka.currentDirection = Direction.DOWN;
        waka.move();
        assertEquals(1, waka.getY());
    }

    /**
     * Test for checking if the object is movable
     */
    @Test
    public void testMovableObject_isMovable() {
        MovableObject waka = new Waka(1, 1, null);
        ArrayList<int[]> paths = new ArrayList<>(Arrays.asList(new int[]{1, 17}));
        waka.setPaths(paths);
        ArrayList<Wall> walls = new ArrayList<>(Arrays.asList(new Wall(0, 0, null)));
        waka.setWalls(walls);
        assertTrue(waka.isMovable(Direction.DOWN));
    }
}