package piece;

import board.Board;
import board.Coordinate;
import board.Square;
import chess.Color;

import java.util.List;

/**
 * Represents a bishop
 * @author Ammaar Muhammad Iqbal
 */
public class Bishop extends Piece {

    /**
     * Creates a new bishop
     * @param board Board that the bishop is on
     * @param square Square that the bishop is on
     * @param color Color of the bishop
     */
    public Bishop(Board board, Square square, Color color) {
        super(board, square, color, 4);
    }

    @Override
    protected Coordinate nextCoordinate(int pathNum, int stepNum, Coordinate c) {
        if (pathNum == 1) return new Coordinate(this.xRight(stepNum), this.yForward(stepNum));
        else if (pathNum == 2) return new Coordinate(this.xRight(stepNum), this.yBack(stepNum));
        else if (pathNum == 3) return new Coordinate(this.xLeft(stepNum), this.yBack(stepNum));
        else return new Coordinate(this.xLeft(stepNum), this.yForward(stepNum));
    }

    @Override
    protected boolean stepCondition(int pathNum, int stepNum, Coordinate c) {
        return true;
    }

    @Override
    protected String canMove(boolean found, int pathNum, List<Square> path) {
        if (!found) return "A bishop can't go there";
        if (!Board.isPathClear(path)) return "The bishop's path is blocked";
        return null;
    }

    /**
     * @return ASCII representation of the bishop
     */
    @Override
    public String toString() {
        return super.toString() + "B";
    }
}
