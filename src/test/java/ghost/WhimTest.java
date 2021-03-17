package ghost;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


class WhimTest {

    /**
     * Test for setting chasing direction
     */
    @Test
    public void testWhim_setChaseDirection() {
        Whim whim = new Whim(0, 0, null);

        Waka waka = new Waka(0, 0, null);
        whim.setWaka(waka);

        whim.chasers = new ArrayList<>(Arrays.asList());
        whim.setChaseDirection(false);

        whim.chasers = new ArrayList<>(Arrays.asList(new Chaser(2, 2, null)));

        waka.currentDirection = Direction.UP;
        whim.setChaseDirection(false);

        waka.currentDirection = Direction.DOWN;
        whim.setChaseDirection(false);

        waka.currentDirection = Direction.LEFT;
        whim.setChaseDirection(false);

        waka.currentDirection = Direction.RIGHT;
        whim.setChaseDirection(false);

        waka.setX(448);
        waka.setY(576);
        whim.setChaseDirection(false);
      }


    /**
     * Test for movement handling in CHASE mode
     */
    @Test
    public void testWhim_handleMovement_CHASE() {
        App app = new App();
        Game game = new Game(app);

        game.whims = new ArrayList<>(Arrays.asList(new Whim(0, 0, null)));
        game.whims.get(0).chasers = new ArrayList<>(Arrays.asList(new Chaser(2, 2, null)));
        
        Waka waka = new Waka(0, 0, null);
        game.whims.get(0).setWaka(waka);

        game.mode = Mode.CHASE;
        Whim.handleMovement(game);
    }
}