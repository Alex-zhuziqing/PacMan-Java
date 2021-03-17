package ghost;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class MovableObject{

    private int x;
    private int y;
    
    private PImage sprite;

    private int speed;

    private ArrayList<Wall> walls;
    private ArrayList<int[]> paths;

    public int[] initialPosition;

    public Direction currentDirection;
    public Direction nextDirection;

    public MovableObject(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.speed = 0;
        this.initialPosition = new int[2];
        this.walls = new ArrayList<>();
        this.paths = new ArrayList<int[]>();
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PImage getSprite() {
        return this.sprite;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ArrayList<Wall> getWalls() {
        return this.walls;
    }

    public void setWalls(ArrayList<Wall> walls) {
        this.walls = walls;
    }

    public ArrayList<int[]> getPaths() {
        return this.paths;
    }

    public void setPaths(ArrayList<int[]> paths) {
        this.paths = paths;
    }

    public int getWidth() {
        return this.sprite.width;
    }

    public int getHeight() {
        return this.sprite.height;
    }

    public int[] getInitialPosition() {
        return this.initialPosition;
    }

    public void setInitialPosition(int[] initialPosition) {
        this.initialPosition = initialPosition;
    }


    /**
     * Make the object back to its initial position.
     */
    public void backToInitialPosition() {
        this.x = this.initialPosition[0];
        this.y = this.initialPosition[1];
    }

   
    /**
     * Draw the sprite
     * 
     * Shift the sprite 4 pixels to leftwards and 4 pixels upwards.
     * @param app PApplet
     */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x - 4, this.y - 4);
    }


    /**
     * Reset the current direction as null.
     */
    public void resetCurrentDirection() {
        this.currentDirection = null;
    }


    /**
     * Reset the next direction as null.
     */
    public void resetNextDirection() {
        this.nextDirection = null;
    }


    /**
     * Set the current direction to the next direction
     */
    public void setCurrentToNext() {
        this.currentDirection = this.nextDirection;
    }


    /**
     * Move in the current direction
     */
    public void move() {
        if (this.currentDirection == Direction.UP) {
            this.y -= this.speed;
        } else if (this.currentDirection == Direction.DOWN) {
            this.y += this.speed;
        } else if (this.currentDirection == Direction.LEFT) {
            this.x -= this.speed;
        } else if (this.currentDirection == Direction.RIGHT) {
            this.x += this.speed;
        }
    }
 

    /**
     * Check if the object is movable to the given direction.
     * 
     * If there is no wall in the given direction, it can move and returns true.
     * Otherwise, return false;
     * 
     * @param direction The direction the object intends to move.
     * @return true if it is movable. Otherwise, false
     */
    public boolean isMovable(Direction direction){
        boolean hasPath = false;
        boolean isWall = false;

        for (int[] p : paths) {
            if (direction == Direction.UP) {
                if (this.x == p[0] && this.y - 16 == p[1]) {
                    hasPath = true;
                    break;
                }
            }
            else if (direction == Direction.DOWN) {
                if (this.x == p[0] && this.y + 16 == p[1]) {
                    hasPath = true;
                    break;
                }
            }
            else if (direction == Direction.LEFT) {
                if (this.x - 16 == p[0] && this.y == p[1]) {
                    hasPath = true;
                    break;
                }
            }
            else if (direction == Direction.RIGHT) {
                if (this.x + 16 == p[0] && this.y == p[1]) {
                    hasPath = true;
                    break;
                }
            }
        }

        
        for (Wall w : walls) {

            if (direction == Direction.UP) {
                if (this.y - 16 == w.getY()) {
                    isWall = true;
                    break;
                }
            }
            else if (direction == Direction.DOWN) {
                if (this.y + 16 == w.getY()) {
                    isWall = true;
                    break;
                }
            }
            else if (direction == Direction.LEFT) {
                if (this.x - 16 == w.getX()) {
                    isWall = true;
                    break;
                }
            }
            else if (direction == Direction.RIGHT) {
                if (this.x + 16 == w.getX()) {
                    isWall = true;
                    break;
                }
            }
        }

        return hasPath || !(isWall);
    }
}