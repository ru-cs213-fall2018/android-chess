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

    List<Move> moves;

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

    public class Move implements Serializable {
        Coordinate from;
        Coordinate to;
        PieceConstructor pieceConstructor;

        public Move(Coordinate from, Coordinate to) {
            this.from = from;
            this.to = to;
        }

        public Move(Coordinate from, Coordinate to, Piece piece) {
            this.from = from;
            this.to = to;
            this.setPieceConstructor(piece);
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

        private void setPieceConstructor(Piece piece) {
            if (piece == null) pieceConstructor = null;
            else if (piece instanceof Pawn) pieceConstructor = Pawn::new;
            else if (piece instanceof Rook) pieceConstructor = Rook::new;
            else if (piece instanceof Knight) pieceConstructor = Knight::new;
            else if (piece instanceof Bishop) pieceConstructor = Bishop::new;
            else if (piece instanceof Queen) pieceConstructor = Queen::new;
            else if (piece instanceof King) pieceConstructor = King::new;
        }
    }
}
