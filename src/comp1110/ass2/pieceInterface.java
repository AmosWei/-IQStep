package comp1110.ass2;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 30/09/2017.
 */
public interface pieceInterface{
    List<Integer> shapePiece(char shape); //Builds the shape of the piece.
    List<Integer> orientPiece();
    List<Character> setRingCoords(); //Position rings of piece onto board.
    Map<Character, Integer> placePiece(); //Maps ring values of a piece onto a location (encoded as char) on the board.
}

