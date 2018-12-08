package game;

import chess.Color;
import piece.King;

/**
 * Represents a player
 * @author Ammaar Muhammad Iqbal
 */
public class Player {

    private Color color;
    private King king;

    /**
     * Creates a new player
     * @param color Color of the player
     * @param king The king of this player
     */
    public Player(Color color, King king) {
        this.color = color;
        this.king = king;
    }

    /**
     * @return Color of the player
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return King of this player
     */
    public King getKing() {
        return king;
    }

    @Override
    public String toString() {
        return this.color.toString();
    }
}
