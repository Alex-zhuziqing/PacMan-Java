package ghost;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


class AmbusherTest {

    /**
     * Test for setting chasing direction
     */
    @Test
    public void testAmbusher_setChaseDirection() {
        Ambusher ambusher = new Ambusher(0, 0, null);
        ambusher.currentDirection = Direction.UP;

        Waka waka = new Waka(0, 0, null);
        ambusher.setWaka(waka);

        waka.currentDirection = Direction.UP;
        ambusher.setChaseDirection(false);

        waka.currentDirection = Direction.DOWN;
        ambusher.setChaseDirection(false);

        waka.currentDirection = Direction.LEFT;
        ambusher.setChaseDirection(false);

        waka.currentDirection = Direction.RIGHT;
        ambusher.setChaseDirection(false);
      }


    /**
     * Test for movement handling in CHASE mode
     */
    @Test
    public void testAmbusher_handleMovement_CHASE() {
        App app = new App();
        Game game = new Game(app);

        game.ambushers = new ArrayList<>(Arrays.asList(new Ambusher(0, 0, null)));
        Waka waka = new Waka(0, 0, null);
        game.ambushers.get(0).setWaka(waka);

        game.mode = Mode.CHASE;
        Ambusher.handleMovement(game);
    }
}