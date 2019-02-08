package Backgammon;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

class Classes {

    static class Board {
        private static Strip[] stripArray = new Strip[24];
        /*
        TODO add a Vbox(?) to the bar/bearoff grid then implement these lists
        static ArrayList<Piece> bar = new ArrayList<>();
        static ArrayList[] bearoff = new ArrayList[2];
        */
        static Color currentTurn;

        static void setInitialpos(GridPane[] p) {
            int offset = 0;
            for (GridPane pane : p) {
                for (int i = 0; i < pane.getChildren().size(); i++) {
                    int x = i + 6 * offset;
                    stripArray[x] = new Strip((VBox) pane.getChildren().get(i), x);
                }
                offset++;
            }

            Piece[] black = new Piece[15];
            Piece[] white = new Piece[15];
            for (int i = 0; i < 15; i++) {
                black[i] = new Piece(Color.BLACK);
                white[i] = new Piece(Color.WHITE);
            }
            stripArray[0].insert(black, 0, 4);
            stripArray[4].insert(white, 0, 2);
            stripArray[6].insert(white, 3, 7);
            stripArray[11].insert(black, 5, 6);
            stripArray[12].insert(white, 8, 12);
            stripArray[16].insert(black, 7, 9);
            stripArray[18].insert(black, 10, 14);
            stripArray[23].insert(white, 13, 14);

        }

        static void insertToStrip(Piece piece, int stripID) {
            stripArray[stripID].insert(piece);
        }

        static Strip getStrip(int index) {
            return stripArray[index];
        }

        static Strip getStrip(VBox box) {
            for (Strip strip : stripArray) {
                if (strip.vBox.equals(box))
                    return strip;
            }
            return null;
        }

        static void makeMove(Move move) { //TODO add logic for pushing off pieces to the bar - ATM it's an invalid move if there's 1 piece.
            if (!validMove(move))
                return;
            stripArray[move.orgStrip].pop();
            stripArray[move.destStrip].insert(new Piece(move.color));
        }

        static boolean validMove(Move move) {

            if (move.orgStrip < 0 || move.destStrip < 0 || move.orgStrip > 23 || move.destStrip > 23) // If outside of array, it's an invalid move
                return false;

            Strip dest = getStrip(move.destStrip);
            Strip org = getStrip(move.orgStrip);

            if (org.quantity == 0)   // If there is no piece to move, it's an invalid move
                return false;

            if ((org.pieceColor != dest.pieceColor)) // If the dest strip has pieces of the opposite color, it's an invalid move
                return (dest.pieceColor == Color.NONE);
            return true;
        }

        static Move[] findAllvalidMoves() { // Maybe change this to some other method, depends what comes in handy
            return null;
        }

        static Color nextTurn() {
            currentTurn = currentTurn == Color.BLACK ? Color.WHITE : Color.BLACK;
            return currentTurn;
        }


    }
}

class Move {
    int orgStrip;
    int destStrip;
    Color color;

    Move(int orgStrip, int destStrip, Color color) {
        this.color = color;
        this.orgStrip = orgStrip;
        this.destStrip = destStrip;
    }

    @Override
    public String toString() {
        if (Classes.Board.validMove(this))
            return "Move: Origin: " + orgStrip + " Destination: " + destStrip;
        else
            return "Invalid move";
    }
}

class Piece {
    Color color;
    ImageView imgView;

    Piece(Color color) {
        this.color = color;
        String url = color == Color.WHITE ? "Backgammon/res/piece-white.png" : "Backgammon/res/piece-black.png";
        Image image = new Image(url);
        imgView = new ImageView();
        imgView.setImage(image);
        imgView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            //TODO what happens when a piece is clicked
        });
    }
}

class Strip {
    VBox vBox;
    int stripID; // Maybe not needed
    int quantity = 0; // Amount of pieces in this strip
    Color pieceColor = Color.NONE;

    Strip(VBox strip, int stripID) {
        this.vBox = strip;
        this.stripID = stripID;
    }

    void insert(Piece[] pieces, int start, int stop) {
        for (int i = start; i <= stop; i++) {
            insert(pieces[i]);
        }
    }

    void insert(Piece piece) {
        pieceColor = piece.color;
        vBox.getChildren().add(piece.imgView);
        quantity++;
    }

    void pop() {
        vBox.getChildren().remove(--quantity);
        if (quantity == 0)
            pieceColor = Color.NONE;
    }
}