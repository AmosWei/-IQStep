package comp1110.ass2;

/**
 * Created by Daniel on 30/09/2017.
 */
public interface boardInterface {
    /*Rules of game:
    (0) All rings of a piece are placed within the board i.e. a ring cannot be occupy a '.' tile unless it has value NR.
    (1) An upper ring can only be placed on UNOCCUPIED_NO_PEG tile
    (2) A lower ring can only be placed on UNOCCUPIED_PEG tile
    (3) A piece cannot be placed if ANY of its lower rings are adjacent to an upper ring of any other piece
        ('adjacency' excludes diagonal adjacency)
   (4) No piece may be used more than once
    */

    /*implements 4 rules of the game (0-4).
    Toggle allows specific (or combination of rules) rules to be toggled on and off.*/
    boolean legalMove(Piece p, int toggleON);

    /*update the board after piece placement;
    toggle turns on and off rules regarding piece placement*/
    void updateBoardState(Piece p, int toggle);

    void removePiece(Piece p); //undo piece placement.
    void printOutBoardState(); //essentially toString() method for the board state.
    boolean checkGameEnd();
}
