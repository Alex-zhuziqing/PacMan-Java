package ghost;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


class IgnorantTest {

    /**
     * Test for setting chasing direction
     */
    @Test
    public void testIgnorant_setChaseDirection() {
        Ignorant ignorant = new Ignorant(0, 0, null);

        Waka waka = new Waka(0, 0, null);
        ignorant.setWaka(waka);

        ignorant.setX(200);
        ignorant.setY(300);
        ignorant.setChaseDirection(false);

        ignorant.setX(1);
        ignorant.setY(1);
        ignorant.setChaseDirection(false);
      }


    /**
     * Test for movement handling in CHASE mode
     */
    @Test
    public void testIgnorant_handleMovement_CHASE() {
        App app = new App();
        Game game = new Game(app);

        game.ignorants = new ArrayList<>(Arrays.asList(new Ignorant(0, 0, null)));
        Waka waka = new Waka(0, 0, null);
        game.ignorants.get(0).setWaka(waka);

        game.mode = Mode.CHASE;
        Ignorant.handleMovement(game);
    }
}