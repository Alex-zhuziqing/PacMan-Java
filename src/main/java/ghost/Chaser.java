package ghost;

import processing.core.PImage;


public class Chaser extends Ghost{

    public Chaser(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }


    /**
     * Set Chaser's next moving direction when in SCATTER mode.
     * 
     * <p> The target location is top left corner.
     * 
     * @param isDebug Indicates if the app is in DEBUG mode
     */
    public void setScatterDirection(boolean isDebug) {
        this.resetNextDirection();
        this.setNextDirection(0, 0);

        if (isDebug && !(this.isRemoved)) {
            this.app.stroke(255, 255, 255);
            this.app.line(this.getX(), this.getY(), 0, 0);
        }
    }


    /**
     * Set Chaser's next moving direction when in CHASE mode.
     * 
     * <p> The target location is waka's postion.
     * 
     * @param isDebug (boolean) Indicates if the app is in DEBUG mode
     */
    public void setChaseDirection(boolean isDebug) {
        this.resetNextDirection();
        setNextDirection(this.getWaka().getX(), this.getWaka().getY());

        if (isDebug && !(this.isRemoved)) {
            this.app.stroke(255, 255, 255);
            this.app.line(this.getX(), this.getY(), this.getWaka().getX(), this.getWaka().getY());
        }
    }


    /**
     * Handle the logic of Chaser's movement.
     * 
     * It behaves differently in different modes. 
     * It can only move if there is no wall in its direction of moving
     * 
     * @param game Game object
     */
    public static void handleMovement(Game game) {

        for (Chaser chaser : game.chasers){

            if (game.mode == Mode.SCATTER){
                chaser.setScatterDirection(game.isDebug);
            }
            else if (game.mode == Mode.CHASE) {
                chaser.setChaseDirection(game.isDebug);
            }

            if (chaser.isMovable(chaser.nextDirection)) {
                chaser.setCurrentToNext();
            } 
    
            if (chaser.isMovable(chaser.currentDirection)) {
                chaser.move();
            }
        }

    }
}