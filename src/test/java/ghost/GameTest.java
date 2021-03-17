package ghost;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

class GameTest {

    /**
     * Test for Waka colliding with a ghost (not in FRIGHTENED mode)
     */
    @Test
    public void testGame_checkCollision() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);
        
        game.waka.setX(5);
        game.waka.setY(10);
        game.waka.setSprite(app.loadImage("src/main/resources/playerClosed.png"));
        
        game.ghosts = new ArrayList<>(Arrays.asList(new Ghost(5, 6, app.loadImage("src/main/resources/ambusher.png"))));
        
        game.checkCollision();
        
        // If they collide, waka will lose a life. 
        assertEquals(game.wakaLives - 1, game.waka.getLives());
        // Waka and all ghosts will go back to their initial positions
        assertEquals(game.waka.getInitialPosition()[0], game.waka.getX());
        assertEquals(game.waka.getInitialPosition()[1], game.waka.getY());
    }


    /**
     * Test for Waka colliding with a ghost in FRIGHTENED mode.
     * Test for Waka colliding with a ghost (not in FRIGHTENED mode) if 
     * some ghosts have been removed from the grid. 
     */
    @Test
    public void testGame_checkCollision_FRIGHTENED() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);
        
        game.waka.setX(5);
        game.waka.setY(10);
        game.waka.setSprite(app.loadImage("src/main/resources/playerClosed.png"));
        game.ghosts = new ArrayList<>(Arrays.asList(
            new Ghost(5, 6, app.loadImage("src/main/resources/ambusher.png")),
            new Ghost(60, 80, app.loadImage("src/main/resources/ambusher.png"))));
        
        game.mode = Mode.FRIGHTENED;
        game.checkCollision();

        // If Waka collides with a ghost in FRIGHTENED mode, the ghost will be removed from the grid. 
        assertTrue(game.ghosts.get(0).isRemoved);

        // Change mdoe to SCATTER
        game.mode = Mode.SCATTER;
        game.waka.setX(60);
        game.waka.setY(70);
        
        // If a ghost has been removed from the grid, and if Waka collides with 
        // a ghost that is not in FRIGHTENED mode, the ghost will be restored. 
        game.checkCollision();
        assertFalse(game.ghosts.get(0).isRemoved);
    }


    /**
     * Test for checking if the time for FRIGHTENED mode has run out
     */
    @Test
    public void testGame_isFrightenedEnd() {
        App app = new App();
        Game game = new Game(app);

        game.frightenedLength = 10;
        game.setFrightenedStartTime(5);
        game.setTimeElapsed(15);
        assertTrue(game.isFrightenedEnd());

        game.setFrightenedStartTime(20);
        game.setTimeElapsed(25);
        assertFalse(game.isFrightenedEnd());
    }


    /**
     * Test for the movement of ghosts in FRIGHTENED mode
     */
    @Test
    public void testGame_manageGameFlow_frightenedMovement() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);

        game.waka.setSprite(app.loadImage("src/main/resources/playerClosed.png"));

        game.ghosts = new ArrayList<>(Arrays.asList(
            new Ghost(5, 6, app.loadImage("src/main/resources/ambusher.png")),
            new Ghost(60, 80, app.loadImage("src/main/resources/ambusher.png"))));

        for (Ghost ghost : game.ghosts) {
            ghost.currentDirection = Direction.UP;
            ghost.nextDirection = Direction.UP;
        }

        game.frightenedLength = 10;
        game.setFrightenedStartTime(20);
        game.setTimeElapsed(25);
        game.mode = Mode.FRIGHTENED;
        game.manageGameFlow();
    }



    /**
     * Test for increasing modeTimeCounter by 1 when 1 second has passed.
     */
    @Test
    public void testGame_manageModes_increaseCounter() {
        App app = new App();
        Game game = new Game(app);

        game.modeLength = new ArrayList<>(Arrays.asList(7, 10, 15));
        game.setModeTimeCounter(1);
        
        // If frameCounter is 0, modeTimeCounter will not increase
        game.setFrameCounter(0);
        game.manageModes();
        assertEquals(1, game.getModeTimeCounter());

        game.setFrameCounter(120);
        game.manageModes();
        assertEquals(2, game.getModeTimeCounter());
    }


    /**
     * Test for switching to next mode
     */
    @Test
    public void testGame_manageModes_switchingModes() {
        App app = new App();
        Game game = new Game(app);

        game.modeLength = new ArrayList<>(Arrays.asList(7, 10, 15));
        game.setFrameCounter(50);
        game.setModeCounter(0);
        game.setModeTimeCounter(7);

        game.manageModes();

        assertEquals(1, game.getModeCounter());
        assertEquals(Mode.CHASE, game.mode);
    }


    /**
     * Test for the case that all the elements of modeLength 
     * have been traversed and it will start from the beginning.
     */
    @Test
    public void testGame_manageModes_restart() {
        App app = new App();
        Game game = new Game(app);

        game.modeLength = new ArrayList<>(Arrays.asList(7, 10, 15));
        game.setModeCounter(3);

        game.manageModes();

        assertEquals(0, game.getModeCounter());
    }


    /**
     * Test for Waka eating fruit
     */
    @Test
    public void testGame_eatFruit() {
        App app = new App();
        Game game = new Game(app);

        game.fruits = new ArrayList<>(Arrays.asList(new Fruit(1, 1, null), new SuperFruit(5, 5, null)));

        // eat normal fruit
        game.waka.setX(1);
        game.waka.setY(1);
        game.eatFruit();
        assertTrue(game.fruits.get(0).getIsEaten());

        //eat super fruit
        game.waka.setX(5);
        game.waka.setY(5);
        game.eatFruit();
        assertEquals(Mode.FRIGHTENED, game.mode);
        assertTrue(game.fruits.get(0).getIsEaten());

        // The fruit that has been eaten will be omitted
        game.eatFruit();
    }


    /**
     * Test for drawing ghosts in FRIGHTENED mode
     */
    @Test
    public void testGame_drawGhosts_FRIGHTENED() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);

        game.ghosts = new ArrayList<>(Arrays.asList(
            new Ghost(5, 6, app.loadImage("src/main/resources/ambusher.png")),
            new Ghost(60, 80, app.loadImage("src/main/resources/ambusher.png"))));

        game.mode = Mode.FRIGHTENED;

        game.drawGhosts();

        // if the ghost has been removed, omit it and continue drawing the next ghost
        game.ghosts.get(0).isRemoved = true;
        game.drawGhosts();
    }


    /**
     * Test for drawing Waka pointing to 4 directions
     */
    @Test
    public void testGame_drawWaka() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);

        game.waka.setIsClosed(false);

        game.waka.currentDirection = Direction.UP;
        game.drawWaka();

        game.waka.currentDirection = Direction.DOWN;
        game.drawWaka();

        game.waka.currentDirection = Direction.LEFT;
        game.drawWaka();

        game.waka.currentDirection = Direction.RIGHT;
        game.drawWaka();
    }


    /**
     * Test for drawing fruits that have been eaten
     */
    @Test
    public void testGame_drawFruits() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);

        game.fruits = new ArrayList<>(Arrays.asList(new Fruit(0, 0, null)));

        game.fruits.get(0).setIsEaten(true);

        game.drawFruits();
    }


    /**
     * Test for winning the game by collecting all fruits
     */
    @Test
    public void testGame_isCollectAllFruits() {
        App app = new App();
        Game game = new Game(app);
        
        game.fruits = new ArrayList<>(Arrays.asList(new Fruit(0, 0, null), new Fruit(1, 1, null)));
        for (Fruit fruit : game.fruits) {
            fruit.setIsEaten(true);
        }

        assertTrue(game.isCollectAllFruits());
    }


    /**
     * Test for displaying winning screen
     */
    @Test
    public void testGame_displayWinScreen() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);
        app.delay(1000);

        game.displayWinScreen();
    }


     /**
     * Test for displaying losing screen
     */
    @Test
    public void testGame_displayLoseScreen() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);
        app.delay(1000);

        game.displayLoseScreen();
    }


    /**
     * Test for winning and losing the game
     */
    @Test
    public void testGame_manageGameFlow_winningAndLosing() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);

        // collect all fruits
        game.fruits = new ArrayList<>(Arrays.asList(new Fruit(0, 0, null), new Fruit(1, 1, null)));
        for (Fruit fruit : game.fruits) {
            fruit.setIsEaten(true);
        }

        // setup for Waka and ghosts
        game.waka.setSprite(app.loadImage("src/main/resources/playerClosed.png"));

        game.ghosts = new ArrayList<>(Arrays.asList(
            new Ghost(5, 6, app.loadImage("src/main/resources/ambusher.png")),
            new Ghost(60, 80, app.loadImage("src/main/resources/ambusher.png"))));

        for (Ghost ghost : game.ghosts) {
            ghost.currentDirection = Direction.UP;
            ghost.nextDirection = Direction.UP;
        }

        game.modeLength = new ArrayList<>(Arrays.asList(7, 10, 15));

        game.manageGameFlow();

        // won the game
        assertTrue(game.getIsWon());

        // reset all fruits
        for (Fruit fruit : game.fruits) {
            fruit.setIsEaten(false);
        }

        game.setIsWon(false);

        // lost the game
        game.waka.setLives(0);
        game.manageGameFlow();
        assertTrue(game.getIsLost());
    }


    /**
     * Test for restarting the game. 
     * All the elements of the game will be reset.
     */
    @Test
    public void testGame_restartGame() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);

        game.fruits = new ArrayList<>(Arrays.asList(new Fruit(0, 0, null), new Fruit(1, 1, null)));

        // setup for Waka and ghosts
        game.waka.setSprite(app.loadImage("src/main/resources/playerClosed.png"));

        game.ghosts = new ArrayList<>(Arrays.asList(
            new Ghost(5, 6, app.loadImage("src/main/resources/ambusher.png")),
            new Ghost(60, 80, app.loadImage("src/main/resources/ambusher.png"))));

        game.modeLength = new ArrayList<>(Arrays.asList(7, 10, 15));

        game.restartGame();

        assertEquals(game.waka.initialPosition[0], game.waka.getX());
        assertEquals(game.waka.initialPosition[1], game.waka.getY());

        assertEquals(game.wakaLives, game.waka.getLives());

        assertNull(game.waka.currentDirection);
        assertNull(game.waka.nextDirection);

        assertEquals(0, game.getModeCounter());
        assertEquals(0, game.getModeTimeCounter());

    }


    /**
     * Test for handling displaying time of winning and losing screen
     */
    @Test
    public void testGame_handleGameEnd() {
        App app = new App();
        Game game = new Game(app);
        PApplet.runSketch(new String[]{"test"}, app);

        game.fruits = new ArrayList<>(Arrays.asList(new Fruit(0, 0, null), new Fruit(1, 1, null)));

        // setup for Waka and ghosts
        game.waka.setSprite(app.loadImage("src/main/resources/playerClosed.png"));

        game.ghosts = new ArrayList<>(Arrays.asList(
            new Ghost(5, 6, app.loadImage("src/main/resources/ambusher.png")),
            new Ghost(60, 80, app.loadImage("src/main/resources/ambusher.png"))));

        game.modeLength = new ArrayList<>(Arrays.asList(7, 10, 15));

        // displaying time is ended, restart the game
        game.setGameOverTime(15);
        game.setTimeElapsed(25);
        game.handleGameEnd();

        // displaying time is not ended
        game.setGameOverTime(15);
        game.setTimeElapsed(20);
        // won the game
        game.setIsWon(true);
        game.handleGameEnd();

        // reset the winning status of the game
        game.setIsWon(false);

        // lost the game
        game.setIsLost(true);
        game.handleGameEnd();
    }
}