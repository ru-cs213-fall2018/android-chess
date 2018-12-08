package chess;

/**
 * Exception thrown when an illegal move is made
 * @author Ammaar Muhammad Iqbal
 */
public class IllegalMoveException extends Exception {

    /**
     * @param message Details on the illegal move
     */
    public IllegalMoveException(String message) {
        super(message);
    }
}
