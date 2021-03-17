package ghost;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


class ChaserTest {

    /**
     * Test for setting chasing direction 
     */
    @Test
    public void testChaser_setChaseDirection() {
        Chaser chaser = new Chaser(0, 0, null);

        Waka waka = new Waka(0, 0, null);
        chaser.setWaka(waka);

       chaser.setChaseDirection(false);
      }


    /**
     * Test for movement handling in CHASE mode
     */
    @Test
    public void testChaser_handleMovement_CHASE() {
        App app = new App();
        Game game = new Game(app);

        game.chasers = new ArrayList<>(Arrays.asList(new Chaser(0, 0, null)));
        Waka waka = new Waka(0, 0, null);
        game.chasers.get(0).setWaka(waka);

        game.mode = Mode.CHASE;
        Chaser.handleMovement(game);
    }
}