package comp1110.ass2;
import java.util.*;

import static comp1110.ass2.ListCoord.LETTERS_ENCODING_TILES;
import static comp1110.ass2.Piece.LR;
import static comp1110.ass2.Piece.NR;
import static comp1110.ass2.Piece.UR;
//
/**
 * Created by Daniel on 30/08/2017.
 * All code in BoardGUI Class is by Daniel Lai (u6352900).
 */
public class Board implements boardInterface{
    public static final int BOARD_NUM_ROWS = 5;
    public static final int BOARD_NUM_COLS = 10;
    public static final int UNOCCUPIED_NO_PEG = 0;
    public static final int UNOCCUPIED_PEG = 1;
    public static final int OCCUPIED_LOWER_RING = 2;
    public static final int OCCUPIED_UPPER_RING = 3;
    public static final Set<Piece> EVERY_PIECE = new HashSet<>(Arrays.asList(Piece.nil, Piece.A, Piece.B, Piece.C, Piece.D, Piece.E, Piece.F, Piece.G, Piece.H));
    //public static final Set<Piece> INITIALISE_PIECES_USED = new HashSet<>(Arrays.asList(Piece.nil));
    //public static final Map<Character, Integer> BLANK_BOARD = setBlankBoardState();

    private Map<Character, Integer> boardState;
    private Set<Piece> piecesUsed;
    private String moveSequence;

    public Board(Map<Character, Integer> boardState, Set<Piece> piecesUsed, String moveSequence) {
        this.boardState = boardState;
        this.piecesUsed = piecesUsed;
        this.moveSequence = moveSequence;
    }
    public Board(){}

///////////////////*RULES OF GAME *///////////////////////
    public boolean checkGameEnd() {
    return piecesUsed.size() == EVERY_PIECE.size()
            && countBlankTilesAtEndGame();
}

    public boolean countBlankTilesAtEndGame(){
        int count = 0;
        for(int i = 0; i < LETTERS_ENCODING_TILES.length(); i = i + 1){
            int tileVal = boardState.get(LETTERS_ENCODING_TILES.charAt(i));
            if( tileVal == 0 || tileVal == 1){
                count = count + 1;
            }
        }
        return count == 7;
    }

    public boolean legalMove(Piece p, int toggleRule){
        switch(toggleRule) {
            case 0: return validPiecePlacement(p) && checkAdjBoardVals(p) && !piecesUsed.contains(p);
            case 1: return validPiecePlacement(p);
            case 2: return checkAdjBoardVals(p) && withinBounds(p);
        }
        return true;
    }

    public boolean withinBounds(Piece p){
        int countNumRings = 0;
        List<Character> posOfRings = p.setRingCoords();
        for (int i = 0; i < 9; i = i + 1) {
            if (posOfRings.get(i) != '.' && p.placePiece().get(posOfRings.get(i)) > 0) {
                countNumRings = countNumRings + 1;
            }
        }
        return countNumRings == Piece.countNumRings(p.getShape());
    }

    /*Implements rules 1, 2 and rule 0:
    (0) All rings of a piece are placed within the board i.e. a ring cannot be occupy a '.' tile unless it has value NR.
    (1) An upper ring can only be placed on UNOCCUPIED_NO_PEG tile
    (2) a lower ring can only be placed on UNOCCUPIED_PEG tile*/
///Write a test for this.

    public boolean validPiecePlacement(Piece p) {
        int irrelevantTiles = 0; //num of the '.' coord (using letter encoding for coord) PLUS countNoRings.
        int countNoRings = 0; //num of rings with NR value.
        int counter = 0;
        List<Character> posOfRings = p.setRingCoords();

        //Counts tiles obeying rule 1 and 2, and for counts irrelevant tiles
        for (int i = 0; i < 9; i = i + 1) {
            if (posOfRings.get(i) == '.' || p.placePiece().get(posOfRings.get(i)) == 0) {
                irrelevantTiles = irrelevantTiles + 1;
            }

            if(p.placePiece().get(posOfRings.get(i)) == NR){
                countNoRings = countNoRings + 1;
            }

            //Rule 1
            if (posOfRings.get(i) != '.'
                    && p.placePiece().get(posOfRings.get(i)) == UR
                    && boardState.get(posOfRings.get(i)) == UNOCCUPIED_NO_PEG) {
                counter = counter + 1;
            }

            //Rule 2
            if (posOfRings.get(i) != '.'
                    && p.placePiece().get(posOfRings.get(i)) == LR
                    && boardState.get(posOfRings.get(i)) == UNOCCUPIED_PEG) {
                counter = counter + 1;
            }
        }
        return counter + irrelevantTiles == 9 && countNoRings != 9;
        //Note: If countNoRings == 9 then we know we have a 'null mapping' ie. a piece without any rings.
        // This piece is constructed by placePiece() method if at least one of its rings it out of bounds.
    }

    /* Implments rule 3: A piece cannot be placed if ANY of its lower rings are adjacent to an upper ring of any other piece
    ('adjacency' excludes diagonal adjacency)

    Test cases:
    (1) Test  BGS, DEQ (invalid order), compared to DEQ, BGS (valid order).
    (2) Test EAT and DEQ (invalid order), compared to DEQ and EAT (valid order)*/

    public boolean checkAdjBoardVals(Piece p) {
        int counter = 0;
        int outOfBounds = 0; //tiles located at '.'

        //Set coords (letter encoding) around home ring of a piece.
        List<Character> posOfRings = p.setRingCoords();

        //Define 'test rings' as each coord from the list given by posOfRing.
        //Generate a list of adjacent coords 'surrounding' each test ring.
        //List of 'surrounding' coords includes coords of the test ring.
        List<List<Character>> listOfAdjCoords = new ArrayList<>();

        for(int i = 0; i < 9; i = i + 1){
            if(p.placePiece().get(posOfRings.get(i)) == LR){
            listOfAdjCoords.add(p.ringCoords(posOfRings.get(i)));
            }
        }

        //Rule 3
        for(int i =  0; i < listOfAdjCoords.size(); i = i +1){

            List<Character> adjCoords = listOfAdjCoords.get(i);

            for(int k = 1; k <= 7 ; k = k + 2){
                if(adjCoords.get(k) == '.'){
                    outOfBounds = outOfBounds + 1;
                }

                if(adjCoords.get(k) != '.'
                        && getBoardState().get(adjCoords.get(k)) != OCCUPIED_UPPER_RING){
                    counter = counter + 1;
                }

            }
        }
        return counter == (listOfAdjCoords.size()*4 - outOfBounds);
    }


////////////////////////*BOARD STATES*//////////////////////////////

    public static Board newGame(){
        Board newGame = new Board(setBlankBoardState(), new HashSet<>(Arrays.asList(Piece.nil)), "");
        return newGame;
    }
//Returns a blank board; i.e. no occupied tiles.
    public static Map<Character, Integer> setBlankBoardState(){
        Map<Character, Integer> map = new HashMap<>();
        for(int k = 0; k < 5; k = k + 1){
            for(int i = k*10; i < k*10 + 10; i = i + 1){
                if(i%2 == 0 && k%2 == 0){
                    map.put(LETTERS_ENCODING_TILES.charAt(i), UNOCCUPIED_PEG);
                }
                if(i%2 != 0 && k%2 == 0) {
                    map.put(LETTERS_ENCODING_TILES.charAt(i), UNOCCUPIED_NO_PEG);
                }
                if(i%2 == 0 && k%2 != 0) {
                    map.put(LETTERS_ENCODING_TILES.charAt(i), UNOCCUPIED_NO_PEG);
                }
                if((i%2 != 0 && k%2 != 0)){
                    map.put(LETTERS_ENCODING_TILES.charAt(i), UNOCCUPIED_PEG);
                }
            }
        }
        return map;
    }

    //Given a Piece(shape, orientation, location), returns an updated board state
    //Update board does not create a new boardState object and does not create a new piecesUsed object
    //Update board changes the values of the existing objects in the constructor

    public void updateBoardState(Piece p, int toggle){
            List<Character> posOfRings = p.setRingCoords();
            if(legalMove(p, toggle)){
                for(int i = 0; i < 9; i = i + 1){
                    if(posOfRings.get(i) != '.' && p.placePiece().get(posOfRings.get(i)) == LR){
                        boardState.put(posOfRings.get(i), OCCUPIED_LOWER_RING);
                    }
                    if(posOfRings.get(i) != '.' && p.placePiece().get(posOfRings.get(i)) == UR){
                        boardState.put(posOfRings.get(i), OCCUPIED_UPPER_RING);
                    }
                }

               piecesUsed.add(p);
               moveSequence = moveSequence + (Character.toString(p.getShape())
                       + Character.toString(p.getOrientation())
                       + Character.toString(p.getLocation()));
                }
    }

    public void removePiece(Piece p){
        List<Character> posOfRings = p.setRingCoords();
        if(piecesUsed.contains(p)){
            for(int i = 0; i < 9; i = i + 1){
                if(posOfRings.get(i) != '.' && p.placePiece().get(posOfRings.get(i)) != 0){
                    if(boardState.get(posOfRings.get(i)) == 3){
                        boardState.put((posOfRings.get(i)), 0);
                    }
                    else if(boardState.get(posOfRings.get(i)) == 2){
                        boardState.put((posOfRings.get(i)), 1);
                    }
                }
            }
            piecesUsed.remove(p);
            moveSequence = moveSequence.substring(0, moveSequence.length() - 3);
        }
    }

    public Set usedPieces() {
        Set<Piece> usedPieces = new HashSet<>();
        System.out.print("Pieces used: ");
        for(Piece piece: piecesUsed){
            System.out.print(piece + " ");
        }
        System.out.println();
        return usedPieces;
    }

    public static Set<Piece> availablePieces(Board b) {
        Set<Piece> usedPieces = b.getPiecesUsed();
        Set<Piece> availablePieces = new HashSet<>();
        for (Piece p : EVERY_PIECE) {
            if (!usedPieces.contains(p)) {
                availablePieces.add(p);
            }
        }
        return availablePieces;
    }

    //toString() method; use this method to troubleshoot.
    public void printOutBoardState(){
        for(int i = 0; i < BOARD_NUM_COLS*BOARD_NUM_ROWS; i = i + 1){
            System.out.print(LETTERS_ENCODING_TILES.charAt(i)
                    + ", " + getBoardState().get(LETTERS_ENCODING_TILES.charAt(i)) + "| ");
            if((i+1)%10 == 0 && i > 0){
                System.out.println();
            }
        }
    }

//////////////////////////*GETTERS AND SETTERS*////////////////////

    public void setBoardState(Map<Character, Integer> boardState) {
        this.boardState = boardState;
    }
    public Map<Character, Integer> getBoardState() {
        return boardState;
    }
    public Set<Piece> getPiecesUsed() {
        return piecesUsed;
    }
    public void setPiecesUsed(Set<Piece> piecesUsed) {
        this.piecesUsed = piecesUsed;
    }
    public String getMoveSequence() {
        return moveSequence;
    }


}

