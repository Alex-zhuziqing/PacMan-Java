package ghost;


import processing.core.PImage;


public class Ambusher extends Ghost{

    public Ambusher(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }


    /**
     * Set Ambusher's next moving direction when in SCATTER mode.
     * 
     * <p> The target location is top right corner.
     * 
     * @param isDebug Indicates if the app is in DEBUG mode
     */
    public void setScatterDirection(boolean isDebug) {
        this.resetNextDirection();
        this.setNextDirection(448, 0);

        if (isDebug && !(this.isRemoved)) {
            this.app.stroke(255, 255, 255);
            this.app.line(this.getX(), this.getY(), 448, 0);
        }
    }

    
    /**
     * Set Ambusher's next moving direction when in CHASE mode.
     * 
     * <p> The target location is four grid spaces ahead of Waka (4 * 16 pixels) (based on Waka’s current direction).
     * 
     * @param isDebug Indicates if the app is in DEBUG mode
     */
    public void setChaseDirection(boolean isDebug) {

        int nextX = this.getWaka().getX();
        int nextY = this.getWaka().getY();

        this.resetNextDirection();


        if (this.getWaka().currentDirection == Direction.UP) {
            nextY -= 64;
        }
        else if (this.getWaka().currentDirection == Direction.DOWN) {
            nextY += 64;
        }
        else if (this.getWaka().currentDirection == Direction.LEFT) {
            nextX -= 64;
        }
        else if (this.getWaka().currentDirection == Direction.RIGHT) {
            nextX += 64;
        }

        setNextDirection(nextX, nextY);

        if (isDebug && !(this.isRemoved)) {
            this.app.stroke(255, 255, 255);
            this.app.line(this.getX(), this.getY(), nextX, nextY);
        }
    }


    /**
     * Handle the logic of Ambusher's movement.
     * 
     * It behaves differently in different modes. 
     * It can only move if there is no wall in its direction of moving
     * 
     * @param game Game object
     */
    public static void handleMovement(Game game) {
        for (Ambusher ambusher : game.ambushers){

            if (game.mode == Mode.SCATTER){
                ambusher.setScatterDirection(game.isDebug);
            }   
            else if (game.mode == Mode.CHASE) {
                ambusher.setChaseDirection(game.isDebug);
            }

            if (ambusher.isMovable(ambusher.nextDirection)) {
                ambusher.setCurrentToNext();
            } 
    
            if (ambusher.isMovable(ambusher.currentDirection)) {
                ambusher.move();
            }
        }
    }

}