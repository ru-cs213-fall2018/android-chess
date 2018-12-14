package com.qwezey.androidchess;

import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.logic.piece.Bishop;
import com.qwezey.androidchess.logic.piece.King;
import com.qwezey.androidchess.logic.piece.Knight;
import com.qwezey.androidchess.logic.piece.Pawn;
import com.qwezey.androidchess.logic.piece.Piece;
import com.qwezey.androidchess.logic.piece.PieceConstructor;
import com.qwezey.androidchess.logic.piece.Queen;
import com.qwezey.androidchess.logic.piece.Rook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameRecord implements Serializable {

    private List<Move> moves;

    public GameRecord() {
        moves = new ArrayList<>();
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public void undoMove() {
        if (moves.size() > 0) {
            moves.remove(moves.size() - 1);
        }
    }

    public List<Move> getMoves() {
        return moves;
    }

    public static class Move implements Serializable {
        private Coordinate from;
        private Coordinate to;
        private PieceConstructor pieceConstructor;

        public Move(Coordinate from, Coordinate to) {
            this.from = from;
            this.to = to;
        }

        public Move(Coordinate from, Coordinate to, PieceConstructor pieceConstructor) {
            this.from = from;
            this.to = to;
            this.pieceConstructor = pieceConstructor;
        }

        public Coordinate getFrom() {
            return from;
        }

        public Coordinate getTo() {
            return to;
        }

        public PieceConstructor getPieceConstructor() {
            return pieceConstructor;
        }
    }
}
