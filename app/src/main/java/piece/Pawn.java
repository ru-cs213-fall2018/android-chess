package piece;

import board.Board;
import board.Coordinate;
import board.Square;
import chess.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pawn
 * @author Ammaar Muhammad Iqbal
 */
public class Pawn extends Piece {

    private boolean lastMoveDoube;
    private Pawn enpassantPawn;

    /**
     * Creates a new pawn
     * @param board Board the pawn is on
     * @param square Square the pawn is on
     * @param color Color of the pawn
     */
    public Pawn(Board board, Square square, Color color) {
        super(board, square, color, 3);
        this.lastMoveDoube = false;
        this.enpassantPawn = null;
    }

    @Override
    public String move(Square square) {
        this.lastMoveDoube = this.isDoubleMove(square);
        this.enpassantPawn = this.canEnpassant(square);
        if (this.enpassantPawn != null) {
            this.enpassantPawn.getSquare().removePiece();
            this.forceMove(square);
            return null;
        }
        return super.move(square);
    }

    @Override
    public void goBack() {
        if (this.enpassantPawn != null)
            this.enpassantPawn.getSquare().setPiece(this.enpassantPawn);
        super.goBack();
    }

    @Override
    public String canMove(Square square) {
        String error = super.canMove(square);
        if (error == null) return error;
        return this.canEnpassant(square) != null ? null : error;
    }

    @Override
    protected Coordinate nextCoordinate(int pathNum, int stepNum, Coordinate c) {
        if (pathNum == 1) return new Coordinate(c.getX(), this.yForward(stepNum));
        else if (pathNum == 2) return new Coordinate(this.xLeft(stepNum), this.yForward(stepNum));
        else return new Coordinate(this.xRight(stepNum), this.yForward(stepNum));
    }

    @Override
    protected boolean stepCondition(int pathNum, int stepNum, Coordinate c) {
        if (pathNum == 1 && !this.hasMoved()) return stepNum <= 3;
        else return stepNum <= 2;
    }

    @Override
    protected String canMove(boolean found, int pathNum, List<Square> path) {
        if (!found) return "Pawns can not move there";
        if (!Board.isPathClear(path)) return "The pawn's path is blocked";
        if (pathNum == 1 && path.get(path.size() - 1).hasPiece())
            return "A pawn can not attack in this direction";
        else if (pathNum != 1 && !path.get(path.size() - 1).hasPiece())
            return "A pawn can only attack in that direction";
        return null;
    }

    /**
     * Checks if going to square is a double move
     * @param square Destination
     * @return True if it is a double move, else false
     */
    private boolean isDoubleMove(Square square) {
        Coordinate from = this.getSquare().getCoordinate();
        Coordinate dub = new Coordinate(from.getX(), this.yForward(2));
        Coordinate to = square.getCoordinate();
        return to.equals(dub);
    }

    /**
     * @param square The destination
     * @return The pawn to remove if this pawn can make
     * enpassant move to square, else null
     */
    private Pawn canEnpassant(Square square) {
        Coordinate from = this.getSquare().getCoordinate();
        Coordinate to = square.getCoordinate();
        Coordinate leftForward = new Coordinate(this.xLeft(1), this.yForward(1));
        Coordinate left = new Coordinate(this.xLeft(1), from.getY());
        Coordinate rightForward = new Coordinate(this.xRight(1), this.yForward(1));
        Coordinate right = new Coordinate(this.xRight(1), from.getY());
        Square pawnSquare = null;
        if (to.equals(leftForward)) pawnSquare = this.getBoard().getSquare(left);
        else if (to.equals(rightForward)) pawnSquare = this.getBoard().getSquare(right);
        else return null;
        if (pawnSquare == null || !(pawnSquare.getPiece() instanceof Pawn)) return null;
        Pawn pawn = (Pawn) pawnSquare.getPiece();
        return pawn.lastMoveDoube ? pawn : null;
    }

    /**
     * @param square Square to check if this piece can attack
     * @return True if pawn can attack square, else false
     */
    @Override
    public boolean canAttack(Square square) {
        ArrayList<Coordinate> attackSpots = new ArrayList<>();
        attackSpots.add(new Coordinate(this.xLeft(1), this.yForward(1)));
        attackSpots.add(new Coordinate(this.xRight(1), this.yForward(1)));
        boolean isAttack = attackSpots.contains(square.getCoordinate());
        return super.canAttack(square) && isAttack;
    }

    /**
     * @return ASCII representation of the pawn
     */
    @Override
    public String toString() {
        return super.toString() + "p";
    }
}
