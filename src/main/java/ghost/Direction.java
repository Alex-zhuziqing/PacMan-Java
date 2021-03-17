package ghost;

public enum Direction {
    UP(1),
    DOWN(-1),
    LEFT(2),
    RIGHT(-2);

    private int index;

    private Direction(int index) {
        this.index = index;
    }

    /**
     * Return the index of the direction
     * @return (int) indext of the direction
     */
    public int getIndex() {
        return this.index;
    }


    /**
     * Convert the index to its corrosponding direction.
     * 
     * @param index (int) valid indices: 1, -1, 2, -2
     * @return (Direction) the direction corrosponding to the index
     */
    public static Direction convertIndexToDirection(int index) {
        if (index == 1) {
            return UP;
        }
        else if (index == -1) {
            return DOWN;
        }
        else if (index == 2) {
            return LEFT;
        }
        else if (index == -2) {
            return RIGHT;
        }
        else {
            return null;
        }
    }
}