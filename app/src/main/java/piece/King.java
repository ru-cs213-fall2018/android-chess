package piece;

import board.Board;
import board.Coordinate;
import board.Square;
import chess.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a king
 * @author Ammaar Muhammad Iqbal
 */
public class King extends Piece {

    private Castle lastCastle;

    /**
     * Creates a king
     * @param board Board the king is on
     * @param square Square the king is on
     * @param color Color of the king
     */
    public King(Board board, Square square, Color color) {
        super(board, square, color, 8);
        this.lastCastle = null;
    }

    @Override
    public String move(Square square) {

        // Try to castle
        Castle castle = new Castle(this, square);
        if (castle.go()) {
            this.lastCastle = castle;
            return null;
        }

        // Try regular move
        String error = super.canMove(square);
        if (error == null) {
            this.forceMove(square);
            this.lastCastle = null;
        }
        return error;
    }

    @Override
    public String canMove(Square square) {
        String error = super.canMove(square);
        if (error == null) return error;
        return new Castle(this, square).can() ? null : error;
    }

    @Override
    public void goBack() {
        if (this.lastCastle != null) this.lastCastle.goBack();
        else super.goBack();
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
        return stepNum <= 2;
    }

    @Override
    protected String canMove(boolean found, int pathNum, List<Square> path) {
        if (!found) return "A king can't move there";
        return null;
    }

    /**
     * Checks if the king is in danger
     * @return True if the king can be attacked, false otherwise
     */
    public boolean isInCheck() {
        return this.isInCheck(this.getSquare());
    }

    /**
     * Checks if this king is in check at square
     * @param square The hypothetical square the king
     *               will check its check status from
     * @return True if king can be attacked at square, else false
     */
    public boolean isInCheck(Square square) {
        return !this.isSafe(square);
    }

    /**
     * Checks if this king is in check if the piece goes to
     * a square
     * @param piece The piece if moved hypothetically
     * @param square The hypothetical destination of the piece
     * @return True if king is still in check after the
     * hypothetical move, else false
     */
    public boolean isInCheck(Piece piece, Square square) {
        if (piece.move(square) == null) {
            boolean ret = this.isInCheck();
            piece.goBack();
            return ret;
        } else return this.isInCheck();
    }

    /**
     * Checks if king is in checkmate
     * @return True if the kink can't move anywhere safe
     * and is currently under attack
     */
    public boolean isInCheckMate() {
        if (!this.isInCheck()) return false;
        return this.getBoard().isEverySquare(s1 -> {
            if (s1.hasPiece() && s1.getPiece().getColor() == this.getColor())
                return this.getBoard().isEverySquare(s2 -> this.isInCheck(s1.getPiece(), s2));
            else return true;
        });
    }

    /**
     * Checks if the king is in stalemate
     * @return True if the king is, false otherwise
     */
    public boolean isInStaleMate() {
        if (this.isInCheck()) return false;
        return this.getBoard().isEverySquare(s1 -> {
            Piece p = s1.getPiece();
            if (s1.hasPiece() && p.getColor() == this.getColor())
                return this.getBoard().isEverySquare(s2 -> p.canMove(s2) != null);
            else return true;
        });
    }

    /**
     * @return ASCII representation of the kning
     */
    @Override
    public String toString() {
        return super.toString() + "K";
    }

    /**
     * Class to handle kings castling
     * @author Ammaar Muhammad Iqbal
     */
    private class Castle {

        private boolean can;
        private King king;
        private Square destination;
        private Rook rook;
        private Square rookDestination;

        /**
         * Creates castling options for a king
         * @param king The king intending to castle
         * @param square The destination of the king
         */
        public Castle(King king, Square square) {

            // Check if king has moved already
            if (king.hasMoved()) {
                this.can = false;
                return;
            }
            boolean destinationValid = false;
            this.rook = null;
            ArrayList<Square> path = new ArrayList<>();
            out: for (int i = 0; i < 2; i++) {

                // Clear the path
                path.clear();
                Coordinate c = king.getSquare().getCoordinate();
                for (int j = 1; Board.isInBoard(c); j++) {

                    // Add to path
                    Square s = king.getBoard().getSquare(c);
                    path.add(s);

                    // Check if destination is valid
                    if (j == 3 && square.equals(s)) destinationValid = true;
                    else if (j == 3) break;

                    // Check if rook is there and valid
                    Piece p = s.getPiece();
                    if (p instanceof Rook && !p.hasMoved()) {
                        this.rook = (Rook) p;
                        break out;
                    }
                    c = new Coordinate(i == 0 ? king.xRight(j) : king.xLeft(j), c.getY());
                }
            }

            // Fill fields
            this.can = destinationValid && Board.isPathClear(path) && this.rook != null
                    && !king.isInCheck() && !king.isInCheck(path.get(1)) && !king.isInCheck(path.get(2));
            if (this.can) {
                this.king = king;
                this.destination = path.get(2);
                this.rookDestination = path.get(1);
            }
        }

        /**
         * @return True if king can castle to square,
         * else returns false
         */
        public boolean can() {
            return can;
        }

        /**
         * Castle the king to square if it can
         * @return True if castled, false otherwise
         */
        public boolean go() {
            if (this.can()) {
                this.king.forceMove(this.destination);
                this.rook.forceMove(this.rookDestination);
            }
            return this.can();
        }

        /**
         * Revert the castle
         * @throws UnsupportedOperationException If king or rook can't go back
         */
        public void goBack() {
            this.king.goBack();
            this.rook.goBack();
        }
    }
}
