package game;

import board.Board;
import board.Square;
import chess.BadInputException;
import chess.Color;
import chess.IllegalMoveException;
import piece.*;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Represents a game
 * @author Ammaar Muhammad Iqbal
 */
public class Game {

    private Board board;
    private Player currentPlayer;
    private Player otherPlayer;
    private boolean drawOffered;
    HashMap<String, PieceConstructor> promotions;

    /**
     * Create a new game
     * @throws BadInputException If can't find player kings
     */
    public Game() throws BadInputException {
        this.board = new Board();
        this.currentPlayer = new Player(Color.White, (King) this.board.getSquare('e', 1).getPiece());
        this.otherPlayer = new Player(Color.Black, (King) this.board.getSquare('e', 8).getPiece());
        this.drawOffered = false;
        this.promotions = new HashMap<>();
        promotions.put("R", Rook::new);
        promotions.put("N", Knight::new);
        promotions.put("B", Bishop::new);
        promotions.put("Q", Queen::new);
    }

    /**
     * Start playing the game
     */
    public void start() {
        Scanner input = new Scanner(System.in);
        while (true) {

            // Get input
            System.out.println(this.board);
            System.out.print("\n" + currentPlayer + "'s move: ");
            String in = input.nextLine();
            String[] move = in.split(" ");
            try {
                if (move.length == 1) {

                    // Handle accepting draw offer
                    if (move[0].trim().equals("draw")) {
                        if (!this.drawOffered)
                            throw new IllegalMoveException(this.otherPlayer + " did not offer a draw");
                        System.out.println("\ndraw");
                        break;
                    }

                    // Handle resigning
                    else if (move[0].trim().equals("resign")) {
                        System.out.println("\n" + this.otherPlayer + " wins");
                        break;
                    }

                    // Handle erroneous input
                    else throw new IllegalMoveException("Not a valid option: " + move[0]);
                }

                // Handle regular moves and draw offers
                else if (move.length >= 2 || move.length <= 4) {

                    // Defining variables
                    Square from = this.board.getSquare(move[0]);
                    Square to = this.board.getSquare(move[1]);
                    boolean drawOffered = move.length > 2 && move[move.length - 1].trim().equals("draw?");
                    boolean promotionRequested = move.length > 2 && promotions.containsKey(move[2].trim());
                    boolean isFromPawn = from.getPiece() instanceof Pawn;
                    boolean isWhite = this.currentPlayer.getColor() == Color.White;
                    int toY = to.getCoordinate().getY();
                    boolean isToEnd = isWhite ? toY == 7 : toY == 0;
                    boolean isFromPawnToEnd = isFromPawn && isToEnd;

                    // Check if moving own piece
                    if (!from.hasPiece() || from.getPiece().getColor() != this.currentPlayer.getColor())
                        throw new IllegalMoveException("You can only move your own piece");

                    // If more than 2 arguments, check if valid
                    int badOption = -1;
                    if (move.length == 4) {
                        if (!promotionRequested) badOption = 2;
                        else if (!drawOffered) badOption = 3;
                    } else if (move.length == 3 && !drawOffered && !promotionRequested) badOption = 2;
                    if (badOption != -1) throw new IllegalMoveException("Not a valid option: " + move[badOption]);
                    if (promotionRequested && !isFromPawnToEnd)
                        throw new IllegalMoveException("You cannot request for promotion in this move");

                    // Move the piece
                    String error = from.getPiece().move(to);
                    if (error != null) throw new IllegalMoveException(error);

                    // Check if current player is in check
                    if (this.currentPlayer.getKing().isInCheck()) {
                        to.getPiece().goBack();
                        throw new IllegalMoveException("You cannot be in check");
                    }

                    // Promote pawns at end
                    else if (isFromPawnToEnd) {
                        String key = promotionRequested ? move[2] : "Q";
                        to.getPiece().changeTo(this.promotions.get(key));
                    }

                    // Current player wins if other player is in checkmate
                    if (this.otherPlayer.getKing().isInCheckMate()) {
                        System.out.println("\n" + this.board + "\n\n" + this.currentPlayer + " wins");
                        break;
                    }

                    // Game is draw if other player is in stalemate
                    if (this.otherPlayer.getKing().isInStaleMate()) {
                        System.out.println("\n" + this.board + "\n\n" + " Stalemate: draw");
                        break;
                    }

                    // Let other player know if s/he is in check
                    if (this.otherPlayer.getKing().isInCheck())
                        System.out.println(("\n" + this.otherPlayer + " is in check"));

                    // Swap the playes
                    Player temp = this.currentPlayer;
                    this.currentPlayer = this.otherPlayer;
                    this.otherPlayer = temp;

                    // Set draw offered
                    this.drawOffered = drawOffered;
                }

                // Handle erroneous number of move arguments
                else throw new BadInputException("A move only accepts 1, 2, or 3 arguments");
            }

            // Handle Exceptions
            catch (BadInputException e) {
                System.out.println("\nBad input: " + e.getMessage());
            } catch (IllegalMoveException e) {
                System.out.println("\nIllegal move, try again: " + e.getMessage());
            }
            System.out.println();
        }
    }
}
