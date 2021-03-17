package ghost;

import processing.core.PImage;

import java.util.ArrayList;

public class Whim extends Ghost{

    public ArrayList<Chaser> chasers;

    public Whim(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }


    /**
     * Set Whim's next moving direction when in SCATTER mode.
     * 
     * <p> The target location is bottom right corner.
     * 
     * @param isDebug Indicates if the app is in DEBUG mode
     */
    public void setScatterDirection(boolean isDebug) {
        this.resetNextDirection();
        this.setNextDirection(448, 576);

        if (isDebug && !(this.isRemoved)) {
            this.app.stroke(255, 255, 255);
            this.app.line(this.getX(), this.getY(), 448, 576);
        }
    }


    /**
     * Set Whim's next moving direction when in CHASE mode.
     * 
     * <p> The target location is double the vector from Chaser to 2 grid spaces (2 * 16) ahead of Waka
     * If there is no Chaser present in the grid, then switch to SCATTER mode until a Chaser is restored.
     * 
     * @param isDebug (boolean) Indicates if the app is in DEBUG mode
     */
    public void setChaseDirection(boolean isDebug) {

        // If there is no chaser present in the map, then switch to SCATTER mode
        if (this.chasers.size() == 0) {
            this.setScatterDirection(isDebug);
            return;
        }

        this.resetNextDirection();

        // Take the first chaser from chasers list
        Chaser chaser = this.chasers.get(0);

        int xIncrement = 0;
        int yIncrement = 0;

        if (this.getWaka().currentDirection == Direction.UP) {
            yIncrement = - 2 * 16;
        } else if (this.getWaka().currentDirection == Direction.DOWN) {
            yIncrement = 2 * 16;
        } else if (this.getWaka().currentDirection == Direction.LEFT) {
            xIncrement = - 2 * 16;
        } else if (this.getWaka().currentDirection ==  Direction.RIGHT) {
            xIncrement = 2 * 16;
        }

        int newX = chaser.getX() + (this.getWaka().getX() + xIncrement - chaser.getX()) * 2;
        int newY = chaser.getY() + (this.getWaka().getY() + yIncrement - chaser.getY()) * 2;

        // In cases where the target location is outside the bounds of the grid, the closest point is used.
        if (newX < 0) {
            newX = 0;
        } if (newX > 448) {
            newX = 448;
        } if (newY < 0) {
            newY = 0;
        } if (newY > 576) {
            newY = 576;
        }

        setNextDirection(newX, newY);

        if (isDebug && !(this.isRemoved)) {
            this.app.stroke(255, 255, 255);
            this.app.line(this.getX(), this.getY(), newX, newY);
        }
    }


    /**
     * Handle the logic of Whim's movement.
     * 
     * It behaves differently in different modes. 
     * It can only move if there is no wall in its direction of moving
     * 
     * @param game Game object
     */
    public static void handleMovement(Game game) {
        for (Whim whim : game.whims){

            if (game.mode == Mode.SCATTER){
                whim.setScatterDirection(game.isDebug);
            }
            else if (game.mode == Mode.CHASE) {
                whim.setChaseDirection(game.isDebug);
            }

            if (whim.isMovable(whim.nextDirection)) {
                whim.setCurrentToNext();
            } 
    
            if (whim.isMovable(whim.currentDirection)) {
                whim.move();
            }
        }
    }

}