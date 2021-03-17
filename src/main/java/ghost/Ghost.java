package ghost;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Random;
import java.lang.Math;

public class Ghost extends MovableObject{

    private Waka waka;

    /**
     * If the ghost is eaten by Waka in FRIGHTENED mode, then isEaten is true
     */
    public boolean isRemoved;

    public PApplet app;

    public Ghost(int x, int y, PImage sprite) {
        super(x, y, sprite);
        this.isRemoved = false;
    }

    public Waka getWaka() {
        return this.waka;
    }
    
    public void setWaka(Waka waka) {
        this.waka = waka;
    }


    /**
     * Determine the direction in which the ghost moves will produce shortest straight-line distance and set the 
     * next moving direction to it.
     * 
     * Note: The ghost will not move to the direction that is opposed to the direction it is currently moving.
     * @param targetX The x-coordinate of the target location
     * @param targetY The y-coordinate of the target location
     */
    public void setNextDirection(int targetX, int targetY) {

        Direction result  = null;
        double minDistance = 1000.0;
        int speed = this.getSpeed();

        ArrayList<Direction> movableDirection = new ArrayList<>(Arrays.asList
        (
        Direction.UP,
        Direction.DOWN,
        Direction.LEFT,
        Direction.RIGHT)
        );

        // If the ghost is currently moving upwards, or there is a wall downwards, then it can not move downwards.
        if (this.currentDirection == Direction.UP || !(this.isMovable(Direction.DOWN))) {
            movableDirection.remove(Direction.DOWN);
        } 
        // If the ghost is currently moving downwards, or there is a wall upwards, then it can not move upwards.
        if (this.currentDirection == Direction.DOWN || !(this.isMovable(Direction.UP))) {
            movableDirection.remove(Direction.UP);
        } 
        // If the ghost is currently moving leftwards, or there is a wall rightwards, then it can not move rightwards.
        if (this.currentDirection == Direction.LEFT || !(this.isMovable(Direction.RIGHT))) {
            movableDirection.remove(Direction.RIGHT);
        }
        // If the ghost is currently moving rightwards, or there is a wall leftwards, then it can not move rightwards.
        if (this.currentDirection == Direction.RIGHT || !(this.isMovable(Direction.LEFT))) {
            movableDirection.remove(Direction.LEFT);
        }

        
        for (Direction direction : movableDirection) {
            
            if (direction == Direction.UP) {
                int nextUpY =  this.getY() - speed;
                double upDistance = Math.sqrt(Math.pow(targetX - this.getX(), 2) + Math.pow(targetY - nextUpY, 2));
                if (upDistance < minDistance) {
                    minDistance = upDistance;
                    result = Direction.UP;
                }
            }
    
            if (direction == Direction.DOWN) {
                int nextDownY = this.getY() + speed;
                double downDistance = Math.sqrt(Math.pow(targetX - this.getX(), 2) + Math.pow(targetY - nextDownY, 2));
                if (downDistance < minDistance) {
                    minDistance = downDistance;
                    result = Direction.DOWN;
                }
            }
    
            if (direction == Direction.LEFT) {
                int nextLeftX = this.getX() - speed;
                double leftDistance = Math.sqrt(Math.pow(targetX - nextLeftX, 2) + Math.pow(targetY - this.getY(), 2));
                if (leftDistance < minDistance) {
                    minDistance = leftDistance;
                    result = Direction.LEFT;
                }
            }
    
            if (direction == Direction.RIGHT) {
                int nextRightX = this.getX() + speed;
                double rightDistance = Math.sqrt(Math.pow(targetX - nextRightX, 2) + Math.pow(targetY - this.getY(), 2));
                if (rightDistance < minDistance) {
                    minDistance = rightDistance;
                    result = Direction.RIGHT;
                }
            }
        }

       this.nextDirection = result;
    }


    /**
     * Manage the movement of ghosts in FRIGHTENED mode.
     * 
     * <p> The ghosts move randomly when they are in FRIGHTENED mode,
     * they only go back when they have no other options.
     */ 
    public static void moveFrightenedGhost(Game game) {
    
        for (Ghost ghost : game.ghosts) {

            ArrayList<Direction> availableDirection = new ArrayList<>();

            if (ghost.isMovable(Direction.UP)) {
                availableDirection.add(Direction.UP);
            } 
            if (ghost.isMovable(Direction.DOWN)) {
                availableDirection.add(Direction.DOWN);
            }
            if (ghost.isMovable(Direction.LEFT)) {
                availableDirection.add(Direction.LEFT);
            }
            if (ghost.isMovable(Direction.RIGHT)) {
                availableDirection.add(Direction.RIGHT);
            } 

            
            while (true) {
                Random random = new Random();
                // randomly generate a number int the range [-2, 2]
                int nextDirectionInt = random.nextInt(5) - 2;

                Direction nextDirection = Direction.convertIndexToDirection(nextDirectionInt);

                // If the number generated is 0 or the number is not in the availableDirection list, 
                // then generate again.
                if (nextDirection == null || !(availableDirection.contains(nextDirection))) {
                    continue;
                }

                // If nextDirection is the opposite of the current direction, and
                // there are more than one available directions (this means the nextDirection is not the 
                // only one option), then generate again.
                if (nextDirection.getIndex() == -ghost.currentDirection.getIndex() && availableDirection.size() != 1) {
                    continue;
                }

                ghost.resetNextDirection();
                
                ghost.nextDirection = nextDirection;

                ghost.setCurrentToNext();
                ghost.move();
                break;
            
            }
        }            
    }
}