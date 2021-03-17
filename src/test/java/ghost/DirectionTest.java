package ghost;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    /**
     * Test for converting an index to its corrosponding direction
     */
    @Test
    public void testDirection_convertIndexToDirection() {
        assertEquals(Direction.UP, Direction.convertIndexToDirection(1));
        assertEquals(Direction.DOWN, Direction.convertIndexToDirection(-1));
        assertEquals(Direction.LEFT, Direction.convertIndexToDirection(2));
        assertEquals(Direction.RIGHT, Direction.convertIndexToDirection(-2));
        assertNull(Direction.convertIndexToDirection(0));
    }
}