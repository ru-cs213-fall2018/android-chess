package board;

import chess.BadInputException;
import chess.Color;
import piece.*;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a chess board
 * @author Ammaar Muhammad Iqbal
 */
public class Board {

    private Square[][] grid;

    /**
     * Creates a new board and fills the grid appropriately
     */
    public Board() {

        // Initialize the grid
        this.grid = new Square[8][8];

        // Fill the grid with squares
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Color color;
                if ((i+j)%2 == 0) color = Color.Black;
                else color = Color.White;
                this.grid[i][j] = new Square(new Coordinate(i, j), color);
            }
        }

        // Add pieces to the board
        for (int i : new int[]{0,1,6,7}) {
            Color color = i < 2 ? Color.White : Color.Black;
            for (int j = 0; j < 8; j++) {
                Square square = grid[j][i];
                if (i == 1 || i == 6) square.setPiece(new Pawn(this, square, color));
                else if (j == 0 || j ==7) square.setPiece(new Rook(this, square, color));
                else if (j == 1 || j ==6) square.setPiece(new Knight(this, square, color));
                else if (j == 2 || j ==5) square.setPiece(new Bishop(this, square, color));
                else if (j == 3) square.setPiece(new Queen(this, square, color));
                else square.setPiece(new King(this, square, color));
            }
        }
    }

    /**
     * Get coordinate of the grid
     * @param file File or column used to get x value
     * @param row Row used to get y value
     * @return The requested coordinate
     * @throws BadInputException If file or row doesn't exist
     */
    public static Coordinate getCoordinate(char file, int row) throws BadInputException {

        // Check if row is valid
        if (row > 8 || row < 1)
            throw new BadInputException("Row " + row + " doesn't exist");

        // Set the right x and y values for grid
        int y = row - 1;
        int x;
        if (file == 'a')
            x = 0;
        else if (file == 'b')
            x = 1;
        else if (file == 'c')
            x = 2;
        else if (file == 'd')
            x = 3;
        else if (file == 'e')
            x = 4;
        else if (file == 'f')
            x = 5;
        else if (file == 'g')
            x = 6;
        else if (file == 'h')
            x = 7;

        // Check if file is valid
        else
            throw new BadInputException("File " + file + " doesn't exist");

        // Return the coordinate
        return new Coordinate(x, y);
    }

    /**
     * Get a coordinate of the grids
     * @param fileRank A string that contains the file at the
     *                 first char and rank at the second
     * @return The requested coordinate
     * @throws BadInputException If the requested coordinate doesn't exist
     */
    public static Coordinate getCoordinate(String fileRank) throws BadInputException {

        // Trim the input
        fileRank = fileRank.trim();

        // Check if right size
        if (fileRank.length() != 2) throw new BadInputException("Incorrect file-rank format: " + fileRank);

        // Return the coordinate
        try {
            char file = fileRank.charAt(0);
            int rank = Integer.parseInt(String.valueOf(fileRank.charAt(1)));
            return Board.getCoordinate(file, rank);
        }

        // Check if rank is a valid number
        catch (NumberFormatException e) {
            throw new BadInputException("Rank must be a number: " + fileRank);
        }
    }

    /**
     * @param c Coordinate to check it it is in the board
     * @return True if it is in the board, false otherwise
     */
    public static boolean isInBoard(Coordinate c) {
        return c.getX() >= 0 && c.getX() <= 7 && c.getY() >= 0 && c.getY() <= 7;
    }

    /**
     * @param path List of squares that represent a path from
     *             the first element to the last element
     * @return True if all squares in between don't have a piece,
     * false otherwise
     */
    public static boolean isPathClear(List<Square> path) {
        for (int i = 1; i < path.size() - 1; i++) {
            if (path.get(i).hasPiece())
                return false;
        }
        return true;
    }

    /**
     * Get a square from the board
     * @param c The coordinate of the requested square
     * @return The requested square
     */
    public Square getSquare(Coordinate c) {
        return this.grid[c.getX()][c.getY()];
    }

    /**
     * Get a square from the board
     * @param file File or column where the square is located
     * @param row Row where the square is located
     * @return The requested square
     * @throws BadInputException If file or row doesn't exist
     */
    public Square getSquare(char file, int row) throws BadInputException {
        Coordinate c = Board.getCoordinate(file, row);
        return this.getSquare(c);
    }

    /**
     * Get a square from the board
     * @param fileRank A string that contains the file at the
     *                 first char and rank at the second
     * @return The requested square
     * @throws BadInputException If the requested square doesn't exist
     */
    public Square getSquare(String fileRank) throws BadInputException {
        Coordinate c = Board.getCoordinate(fileRank);
        return this.getSquare(c);
    }

    /**
     * Check if square is safe from the opposing color
     * @param square The square to check for safety
     * @param color The color of the hypothetical piece
     *              that would be on square. Basically checks if
     *              any of the pieces of the opposite color can
     *              attack this square.
     * @return True if square is safe, false otherwise
     */
    public boolean isSafe(Square square, Color color) {
        return this.isEverySquare(s -> {
            Piece p = s.getPiece();
            boolean notSafe = s.hasPiece() &&
                    p.getColor() != color &&
                    p.canAttack(square);
            return !notSafe;
        });
    }

    /**
     * Checks if predicate returns true for every square in the board
     * @param predicate Predicate that will be passed every square in the board
     * @return True if all squares pass test, false otherwise
     */
    public boolean isEverySquare(Predicate<Square> predicate) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square s = this.grid[i][j];
                if (!predicate.test(s)) return false;
            }
        }
        return true;
    }

    /**
     * @return ASCII representation of how the board looks
     */
    @Override
    public String toString() {
        String ret = "";
        for (int j = 8; j > 0; j--) {
            for (int i = 0; i < 8; i++)
                ret += this.grid[i][j-1] + " ";
            ret += j + "\n";
        }
        return ret + " a  b  c  d  e  f  g  h";
    }
}
