package piece;

import board.Board;
import board.Coordinate;
import board.Square;
import chess.Color;

import java.util.List;

/**
 * Represents a knight
 * @author Ammaar Muhammad Iqbal
 */
public class Knight extends Piece {

    /**
     * Creates a knight
     * @param board The board the knight is on
     * @param square The square the knight is on
     * @param color The color of the knight
     */
    public Knight(Board board, Square square, Color color) {
        super(board, square, color, 8);
    }

    @Override
    protected Coordinate nextCoordinate(int pathNum, int stepNum, Coordinate c) {
        if (pathNum == 1) return new Coordinate(this.xRight(1), yForward(2));
        else if (pathNum == 2) return new Coordinate(this.xRight(2), yForward(1));
        else if (pathNum == 3) return new Coordinate(this.xRight(2), yBack(1));
        else if (pathNum == 4) return new Coordinate(this.xRight(1), yBack(2));
        else if (pathNum == 5) return new Coordinate(this.xLeft(1), yBack(2));
        else if (pathNum == 6) return new Coordinate(this.xLeft(2), yBack(1));
        else if (pathNum == 7) return new Coordinate(this.xLeft(2), yForward(1));
        else return new Coordinate(this.xLeft(1), yForward(2));
    }

    @Override
    protected boolean stepCondition(int pathNum, int stepNum, Coordinate c) {
        return stepNum <= 2;
    }

    @Override
    protected String canMove(boolean found, int pathNum, List<Square> path) {
        if (!found) return "A knight can't move there";
        return null;
    }

    /**
     * @return ASCII representaion of the knight
     */
    @Override
    public String toString() {
        return super.toString() + "N";
    }
}
