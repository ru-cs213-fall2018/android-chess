package piece;

import board.Board;
import board.Coordinate;
import board.Square;
import chess.Color;

import java.util.List;

/**
 * Represents a queen
 * @author Ammaar Muhammad Iqbal
 */
public class Queen extends Piece {

    /**
     * Creates a queen
     * @param board Board the queen is on
     * @param square Square the queen is on
     * @param color Color of the queen
     */
    public Queen(Board board, Square square, Color color) {
        super(board, square, color, 8);
    }

    @Override
    protected Coordinate nextCoordinate(int pathNum, int stepNum, Coordinate c) {
        if (pathNum == 1) return new Coordinate(c.getX(), this.yForward(stepNum));
        else if (pathNum == 2) return new Coordinate(this.xRight(stepNum), this.yForward(stepNum));
        else if (pathNum == 3) return new Coordinate(this.xRight(stepNum), c.getY());
        else if (pathNum == 4) return new Coordinate(this.xRight(stepNum), this.yBack(stepNum));
        else if (pathNum == 5) return new Coordinate(c.getX(), this.yBack(stepNum));
        else if (pathNum == 6) return new Coordinate(this.xLeft(stepNum), this.yBack(stepNum));
        else if (pathNum == 7) return new Coordinate(this.xLeft(stepNum), c.getY());
        else return new Coordinate(this.xLeft(stepNum), this.yForward(stepNum));
    }

    @Override
    protected boolean stepCondition(int pathNum, int stepNum, Coordinate c) {
        return true;
    }

    @Override
    protected String canMove(boolean found, int pathNum, List<Square> path) {
        if (!found) return "A queen can't go there";
        if (!Board.isPathClear(path)) return "The queen's path is blocked";
        return null;
    }

    /**
     * @return ASCII representation of the queen
     */
    @Override
    public String toString() {
        return super.toString() + "Q";
    }
}
