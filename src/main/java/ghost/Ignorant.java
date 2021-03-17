package ghost;

import processing.core.PImage;

public class Ignorant extends Ghost{

    public Ignorant(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

    /**
     * Set Ignorant's next moving direction when in SCATTER mode.
     * 
     * <p> The target location is bottom left corner.
     * 
     * @param isDebug Indicates if the app is in DEBUG mode
     */
    public void setScatterDirection(boolean isDebug) {
        this.resetNextDirection();
        this.setNextDirection(0, 576);

        if (isDebug && !(this.isRemoved)) {
            this.app.stroke(255, 255, 255);
            this.app.line(this.getX(), this.getY(), 0, 576);
        }
    }


    /**
     * Set Ignorant's next moving direction when in CHASE mode.
     * 
     * <p> If more than 8 units (8 * 16 pixels) away from Waka (straight line distance), 
     * target location is Waka. Otherwise, target location is bottom left corner.
     * 
     * @param isDebug (boolean) Indicates if the app is in DEBUG mode
     */
    public void setChaseDirection(boolean isDebug) {
        this.resetNextDirection();

        double distanceToWaka = Math.sqrt(Math.pow(this.getWaka().getX() - this.getX(), 2) + 
            Math.pow(this.getWaka().getY() - this.getY(), 2));

        if (distanceToWaka > 8 * 16) {
            this.setNextDirection(this.getWaka().getX(), this.getWaka().getY());

            if (isDebug && !(this.isRemoved)) {
                this.app.stroke(255, 255, 255);
                this.app.line(this.getX(), this.getY(), this.getWaka().getX(), this.getWaka().getY());
            }
        } else {
            this.setNextDirection(0, 576);

            if (isDebug && !(this.isRemoved)) {
                this.app.stroke(255, 255, 255);
                this.app.line(this.getX(), this.getY(), 0, 576);
            }
        }    
    }


    /**
     * Handle the logic of Ignorant's movement.
     * 
     * It behaves differently in different modes. 
     * It can only move if there is no wall in its direction of moving
     * 
     * @param game Game object
     */
    public static void handleMovement(Game game) {
        for (Ignorant ignorant : game.ignorants){

            if (game.mode == Mode.SCATTER){
                ignorant.setScatterDirection(game.isDebug);
            }
            else if (game.mode == Mode.CHASE) {
                ignorant.setChaseDirection(game.isDebug);
            }

            if (ignorant.isMovable(ignorant.nextDirection)) {
                ignorant.setCurrentToNext();
            } 
    
            if (ignorant.isMovable(ignorant.currentDirection)) {
                ignorant.move();
            }
        }
    }
}