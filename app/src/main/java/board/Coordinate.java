package board;

/**
 * Represents a coordinate for a chess board's grid
 * @author Ammaar Muhammad Iqbal
 */
public class Coordinate {

    private int x;
    private int y;

    /**
     * Create a coordinate for a chess board's grid
     * @param x X index of grid
     * @param y Y index of grid
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X index of grid
     */
    public int getX() {
        return x;
    }

    /**
     * @return Y index of grid
     */
    public int getY() {
        return y;
    }

    /**
     * @param obj Other object
     * @return True if x and y values are equal, else false
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate))
            return false;
        Coordinate o = (Coordinate) obj;
        return this.x == o.x && this.y == o.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
