package chess;

/**
 * Thrown on bad user input
 * @author Ammaar Muhammad Iqbal
 */
public class BadInputException extends Exception {

    /**
     * @param message Details on the bad input
     */
    public BadInputException(String message) {
        super(message);
    }
}
