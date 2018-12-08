package piece;

import board.Board;
import board.Coordinate;
import board.Square;
import chess.Color;

import java.util.List;

/**
 * Represents a rook
 * @author Ammaar Muhammad Iqbal
 */
public class Rook extends Piece {

    /**
     * Create a new rook
     * @param board Board the rook is on
     * @param square Square the rook is on
     * @param color Color of the rook
     */
    public Rook(Board board, Square square, Color color) {
        super(board, square, color, 4);
    }

    @Override
    protected Coordinate nextCoordinate(int pathNum, int stepNum, Coordinate c) {
        if (pathNum == 1) return new Coordinate(c.getX(), this.yForward(stepNum));
        else if (pathNum == 2) return new Coordinate(c.getX(), this.yBack(stepNum));
        else if (pathNum == 3) return new Coordinate(this.xRight(stepNum), c.getY());
        else return new Coordinate(this.xLeft(stepNum), c.getY());
    }

    @Override
    protected boolean stepCondition(int pathNum, int stepNum, Coordinate c) {
        return true;
    }

    @Override
    protected String canMove(boolean found, int pathNum, List<Square> path) {
        if (!found) return "A rook can't move there";
        if (!Board.isPathClear(path)) return "The rook's path is blocked";
        return null;
    }

    /**
     * @return ASCII representation of the rook
     */
    @Override
    public String toString() {
        return super.toString() + "R";
    }
}
