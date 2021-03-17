package ghost;

import java.util.ArrayList;

public class Game {
    public App app;

    public Waka waka;
    public int wakaLives;

    public Map map;

    /**
     * A general list that stores all ghosts
     */
    public ArrayList<Ghost> ghosts;
    
    // store the 4 types of ghosts separately
    public ArrayList<Ambusher> ambushers;
    public ArrayList<Chaser> chasers;
    public ArrayList<Ignorant> ignorants;
    public ArrayList<Whim> whims;

    /**
     * store all fruits in the grid
     */
    public ArrayList<Fruit> fruits;

    /**
     * store all walls in the grid
     */
    public ArrayList<Wall> walls;

    public Mode mode;
    public Mode lastMode;

    /**
     * If the app is in DEBUG mode, isDebug is true, otherwise false.
     */
    public boolean isDebug;

    /**
     * Count for modes.
     * <P> Even number for SCATTER mode, odd number for CHASE mode
     */
    private int modeCounter;

    private boolean isWon;
    private boolean isLost;

    public ArrayList<Integer> modeLength;
    public int frightenedLength;

    /**
     * store the start time of FRIGHTENED mode 
     */
    private int frightenedStartTime;

    /**
     * The time that the game has run
     */
    private int timeElapsed;

    /**
     * The start time of the previous mode
     */
    //private int prevModeStartTime;

    private int modeTimeCounter;

    /**
     * Count for restart time (10 seconds)
     */
    private int gameOverTime;

   /**
    * store the path (in the form of a coordinate (x, y))
    * that Waka can travel through (all elements except for walls)
    */
    public ArrayList<int[]> paths;

    /**
     * count for the number of frames 
     */
    private int frameCounter;


    public Game(App app) {

        this.app = app;

        this.waka = new Waka(0, 0, null);
        this.wakaLives = 0;

        this.ghosts = new ArrayList<>();
        this.ambushers = new ArrayList<>();
        this.chasers = new ArrayList<>();
        this.ignorants = new ArrayList<>();
        this.whims = new ArrayList<>();

        this.fruits = new ArrayList<>();

        this.isWon = false;
        this.isLost = false;

        this.mode = Mode.SCATTER;
        this.lastMode = Mode.SCATTER;
        this.modeCounter = 0;

        this.isDebug = false;

        this.timeElapsed = this.app.time / 1000;
        this.modeTimeCounter = 0;

        this.gameOverTime = 0;
        this.modeLength = new ArrayList<Integer>();
        this.frightenedLength = 0;
        this.frightenedStartTime = 0;

        this.map = new Map(this);

        this.walls = new ArrayList<Wall>();
        this.paths = new ArrayList<int[]>();

        this.frameCounter = 0;
    }

    public boolean getIsWon() {
        return this.isWon;
    }

    public boolean getIsLost() {
        return this.isLost;
    }

    public void setIsWon(boolean isWon) {
        this.isWon = isWon;
    }

    public void setIsLost(boolean isLost) {
        this.isLost = isLost;
    }

    public void setFrightenedStartTime(int time) {
        this.frightenedStartTime = time;
    }

    public void setTimeElapsed(int timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public int getModeCounter() {
        return this.modeCounter;
    }

    public void setModeCounter(int modeCounter) {
        this.modeCounter = modeCounter;
    }

    public int getModeTimeCounter() {
        return this.modeTimeCounter;
    }

    public void setModeTimeCounter(int modeTimeCounter) {
        this.modeTimeCounter = modeTimeCounter;
    }

    public void setFrameCounter(int frameCounter) {
        this.frameCounter = frameCounter;
    }

    public void setGameOverTime(int time) {
        this.gameOverTime = time;
    }


    /**
     * Manage the overall game flow.
     * 
     * <p> This is the main method of the Game class. It manages the overall game flow including
     * the movement of Waka, the movement of ghosts, the collision between Waka and ghosts,
     * eating fruits of Waka, switching modes, timing the game and winning or losing of the game. 
     */
    public void manageGameFlow() {

        this.timeElapsed = this.app.time / 1000;

        if (this.isWon || this.isLost){
            this.handleGameEnd();
        }

        else {

            // win the game
            if (this.isCollectAllFruits()) {
                this.gameOverTime = this.timeElapsed;
                this.isWon = true;
            }
            
            // lose the game
            if (this.waka.getLives() == 0) {
                this.gameOverTime = this.timeElapsed;
                this.isLost = true;
            }
    
            // move Waka
            this.moveWaka();
    
            // eat fruit
            this.eatFruit();
    
            // move ghosts depending on modes
            if (this.mode == Mode.FRIGHTENED && !(this.isFrightenedEnd())) {
                Ghost.moveFrightenedGhost(this);
            }
            else {
                // move ghosts
                this.manageModes();
                this.moveGhost();
            }
    
            // check if Waka and ghosts collide
            this.checkCollision();
    
            // draw all the elements
            this.drawWalls();
            this.drawFruits();
            this.drawGhosts();
            this.drawWaka();
            this.drawWakaLives();    
        }
    }
        

    /**
     * Draw walls
     */
    public void drawWalls() {
        for (Wall wall : this.walls) {
            if (wall.isEmpty) {
                continue;
            } else {
                wall.draw(app);
            }  
        } 
    }


    /**
     * Draw fruits.
     * 
     * <p> Draw those fruits that are not eatten by Waka
     */
    public void drawFruits() {
       for (Fruit fruit: this.fruits) {
            if (fruit.getIsEaten()) {
                continue;
            }
            if (fruit instanceof SuperFruit) {
                SuperFruit superFruit = (SuperFruit) fruit;
                superFruit.draw(app);
            }
            else {
                fruit.draw(app);
            }
        } 
    }


    /**
     * Draw Waka.
     * 
     * <p> Handle the graphics for Waka. 
     * <p> Waka switches between open mouth and closed mouth for every 8 frames.
     * <p> Waka's mouth is always pointing to the direction it is currently moving.
     */
    public void drawWaka() {

        this.frameCounter++;

        if (this.frameCounter % 8 == 0) {
            this.waka.reverseIsClosed();
        }

        if (this.waka.getIsClosed()) {
            this.waka.setSprite(app.loadImage("src/main/resources/playerClosed.png"));
        } else if (this.waka.currentDirection == Direction.UP) {
            this.waka.setSprite(app.loadImage("src/main/resources/playerUp.png"));
        } else if (this.waka.currentDirection == Direction.DOWN) {
            this.waka.setSprite(app.loadImage("src/main/resources/playerDown.png"));
        } else if (this.waka.currentDirection == Direction.LEFT) {
            this.waka.setSprite(app.loadImage("src/main/resources/playerLeft.png"));
        } else if (this.waka.currentDirection == Direction.RIGHT) {
            this.waka.setSprite(app.loadImage("src/main/resources/playerRight.png"));
        }
        this.waka.draw(app);
    }


    /**
     * Draw Waka's lives at bottoms left corner of the window.
     */
    public void drawWakaLives() {
        // draw the number of lives at bottom left corner of the grid
        for (int i = 0; i < this.waka.getLives(); i++) {
            Waka wakaTemp = new Waka(5 + i * 27, 545, app.loadImage("src/main/resources/playerRight.png"));
            wakaTemp.draw(app);
        }
    }


    /**
     * Draw ghosts.
     * 
     * <p> If in FRIGHTENED mode, all ghosts have the same sprite.
     * <p> If they are removed by Waka in FRIGHTENED mode, they will not be drawn.
     */
    public void drawGhosts() {

        for (Ghost ghost : this.ghosts) {
            
            if (ghost.isRemoved) {
                continue;
            }
            if (this.mode == Mode.FRIGHTENED) {
                ghost.setSprite(app.loadImage("src/main/resources/frightened.png"));
            } else {
                if (ghost instanceof Ambusher) {
                    ghost.setSprite(app.loadImage("src/main/resources/ambusher.png"));
                } else if (ghost instanceof Chaser) {
                    ghost.setSprite(app.loadImage("src/main/resources/chaser.png"));
                } else if (ghost instanceof Ignorant) {
                    ghost.setSprite(app.loadImage("src/main/resources/ignorant.png"));
                } else {
                    ghost.setSprite(app.loadImage("src/main/resources/whim.png"));
                }
            }
            ghost.draw(app);
        }
    }


    /**
     * When Waka is at the same grid as fruit, fruit is eaten by Waka
     */
    public void eatFruit() {
        
        for (Fruit fruit : this.fruits) {
            if (fruit.x == this.waka.getX() && fruit.y == this.waka.getY()) {
                if (fruit instanceof SuperFruit) {
                
                    // If Waka eats the super fruit, initialize frightenedStartTime as current time.
                    if (!fruit.getIsEaten()) {
                        this.frightenedStartTime = this.timeElapsed;
                        this.lastMode = this.mode;
                        this.mode = Mode.FRIGHTENED;
                    }
                }
    
                fruit.setIsEaten(true);
            }
        }
    }


    /**
     * Manage the movement of Waka
     * 
     * <p> First, it checks if the next direction (set by users pressing keys) is movable.
     * If it is movable, then set the current direction to it. 
     * If not, then continue moving in its current direction.
     * 
     * <p> Second, it checks if it is movable if it moves in the current direction. 
     * If it is movable, then move.
     * If not, then stop.
     */
    public void moveWaka() {

        if (this.waka.isMovable(this.waka.nextDirection)) {
            this.waka.setCurrentToNext();
        }

        if (this.waka.isMovable(this.waka.currentDirection)) {
            this.waka.move();
        } 
    }

    
    /**
     * Manage the modes of the game.
     * 
     * <p> If the time for the current mode is ended, switch to the other mode. 
     */
    public void manageModes() {
        // The game runs 60 frames per second, if frameCounter is divisible by 60, then one second has passed.
        if (this.frameCounter % 60 == 0 && this.frameCounter != 0) {
            this.modeTimeCounter++;
        }

        // If all the elements of modeLength have been traversed through, then start from the beginning.
        if (this.modeCounter > this.modeLength.size() - 1) {
            this.modeCounter = 0;
        }

        if (this.modeTimeCounter == this.modeLength.get(this.modeCounter)) {
            this.modeTimeCounter = 0;
            this.modeCounter++;
        }

        /**
         *  If modeCounter is an even number, then switch to SCATTER mode
         *  If modeCounter is an odd number, then switch to CHASE mode
        */ 
        if (modeCounter % 2 == 0) {
            this.mode = Mode.SCATTER;
        }
        else {
            this.mode = Mode.CHASE;
        }
    }


    /**
     * Manage the movement of ghosts.
     */
    public void moveGhost() {

        Ambusher.handleMovement(this);
        Chaser.handleMovement(this);
        Ignorant.handleMovement(this);
        Whim.handleMovement(this);
    }


    /**
     * Check if FRIGHTENED mode has ended.
     * 
     * <p> Return true if FRIGHTENED mode has eneded. Otherwise, return false.
     * @return boolean 
     */
    public boolean isFrightenedEnd() {
        int frightenedTimeElapsed = this.timeElapsed - this.frightenedStartTime;
        if (frightenedTimeElapsed == this.frightenedLength) {
            this.mode = this.lastMode;
            return true;
        }
        return false;
    }


    /**
     * Handles collision
     * 
     * <p> If Waka collides with a ghost, it will lose a life and 
     * Waka and all the ghosts will go back to their initial positions.
     */
    public void collide() {

        this.waka.setLives(this.waka.getLives() - 1);

        this.waka.backToInitialPosition();

        for (Ghost ghost : this.ghosts) {
            ghost.backToInitialPosition();
        }
    }
    

    /**
     * Handle collision logic.
     * 
     * <p> Waka and ghosts will collide if either of the 4 corners of 
     * their sprites overlap. 
     */
    public void checkCollision() {

        int wakaLeft = this.waka.getX();
        int wakaRight = this.waka.getX() + this.waka.getWidth();
        int wakaTop = this.waka.getY();
        int wakaBottom = this.waka.getY() + this.waka.getHeight();

        for (Ghost ghost : this.ghosts) {

            if (ghost.isRemoved) {
                continue;
            }
            
            int ghostLeft = ghost.getX();
            int ghostRight = ghost.getX() + ghost.getWidth();
            int ghostTop = ghost.getY();
            int ghostBottom = ghost.getY() + ghost.getHeight();

            if (wakaRight > ghostLeft && wakaLeft < ghostRight && wakaBottom > ghostTop && wakaTop < ghostBottom) {
                // If Waka and ghost collide in FRIGHTENED mode, Waka will not lose a life and the ghost will be eaten
                if (this.mode == Mode.FRIGHTENED) {
                    ghost.isRemoved = true;
                } 

                else {  
                    /**
                     * If Waka is hit by a ghost not in FRIGHTENED mode, 
                     * then return all the ghosts that have been removed.
                     */
                    for (Ghost ghost1 : this.ghosts) {
                        if (ghost1.isRemoved) {
                            ghost1.isRemoved = false;
                        }
                    }
                    this.collide();
                }
            }
        }
    }


    /**
     * Check if all the fruits in the map have been collected by Waka
     * 
     * @return (boolean) Return true if all the fruits have been collected by Waka. 
     * Otherwise, return false.
     */
    public boolean isCollectAllFruits() {

        for (Fruit fruit : this.fruits) {
            if (!(fruit.getIsEaten())) {
                return false;
            }
        }
        return true;
    }
    

    /**
     * Render the YOU WIN screen 
     * 
     * <p> Note: To ensure it it displays properly, users must have PressStart2P-Regular font installed 
     * in their computers.
     */
    public void displayWinScreen() {
        this.app.text("YOU WIN", 140, 240);
    }


    /**
     * Render the GAME OVER screen 
     * 
     * <p> Note: To ensure it it displays properly, users must have PressStart2P-Regular font installed 
     * in their computers.
     */
    public void displayLoseScreen() {
        this.app.text("GAME OVER", 115, 240);
    }

    /**
     * Handle the time for displaying the game ending screen.
     * 
     * <p> If the game is won or lost, display the win or lose screen for 10 seconds. 
     * If 10 seconds have passed, it will restart the game.
     */
    public void handleGameEnd() {
        if (this.gameOverTime + 10 == this.timeElapsed) {
            this.restartGame();
        }
        else {
            if (this.isWon) {
                this.displayWinScreen();
            } 
            else {
                this.displayLoseScreen();
            }
        }
    }


    /**
     * Restart the game.
     * 
     * <p> Restart the game by resetting all the elements, with all Fruit, SuperFruit,
     * Ghosts, and lives restored.
     */
    public void restartGame() {

        for (Fruit fruit : this.fruits) {
            fruit.setIsEaten(false);
        }

        for (Ghost ghost : this.ghosts) {
            ghost.backToInitialPosition();
            ghost.isRemoved = false;
        }

        this.waka.backToInitialPosition();
        this.waka.setLives(this.wakaLives);
        this.waka.resetCurrentDirection();
        this.waka.resetNextDirection();

        this.isWon = false;
        this.isLost = false;

        this.modeCounter = 0;
        this.modeTimeCounter = 0;
        this.mode = Mode.SCATTER;
        this.lastMode = Mode.SCATTER;
    
    }
}
