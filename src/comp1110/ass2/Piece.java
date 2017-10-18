package comp1110.ass2;
import java.util.*;
import static comp1110.ass2.ListCoord.LETTER_TO_COORD_MAP;
import static comp1110.ass2.ListCoord.LIST_OF_COORDS;
import static comp1110.ass2.ListCoord.COORD_TO_LETTER_MAP;

/**
 * Created by Daniel on 30/08/2017.
 */
public enum Piece implements pieceInterface{
    A('A', 'A', 'A'),
    B('B', 'A', 'A'),
    C('C', 'A', 'A'),
    D('D', 'A', 'A'),
    E('E', 'A', 'A'),
    F('F', 'A', 'A'),
    G('G', 'A', 'A'),
    H('H', 'A', 'A'),
    nil('z', 'z', 'z');

    private char location;
    private char shape;
    private char orientation;

    public static final int NR = 0;
    public static final int LR = 1;
    public static final int UR = 2;

    Piece(char shape, char orientation, char location) {
        this.location = location;
        this.shape = shape;
        this.orientation = orientation;
    }

////////////////*PIECE PLACEMENT*/////////////////////

     //MAPS RING VALUE TO BOARD COORD (AKA RingPos)
    //IMPORTANT NOTE: The '.' key refers to multiple tiles. But the key in Hash map can refer to only 1 unique value.
    //This is not a problem, so long as the mapping is recognised as illegal.
    //Specifically, legal mappings will have all '.' referring to value 0 (No ring value). See pieceWithinBounds() method.
    public Map<Character, Integer> placePiece() {
        List<Character> ringPos = ringCoords(location);
        List<Integer> ringVals = orientPiece();

        Map<Character, Integer> map = new HashMap<>();
        if(pieceWithinBounds(ringPos, ringVals)){
            for(int i = 0; i < 9; i = i + 1) {
                if (ringPos.get(i) != '.') {
                    map.put(ringPos.get(i), ringVals.get(i));
                }
                else if (ringPos.get(i) == '.') {
                    map.put(ringPos.get(i), 0);
                }
            }
        }

        //Rule 0: Piece within bounds! (i.e. placed onto the board)
        else{ //Null map
            for(int i = 0; i < 9; i = i + 1){
                map.put(ringPos.get(i), 0);
            }
        }

        return map;
    }

    //Checks that the piece is placed within the 5x10 board.
    public boolean pieceWithinBounds(List<Character> ringPos, List<Integer> ringVals){
        int acc = 0;
        int numberOfOutOfBoundsTiles = 0;
        for(int i = 0; i < 9; i = i + 1){
            if(ringPos.get(i) == '.'){
                numberOfOutOfBoundsTiles = 1 + numberOfOutOfBoundsTiles;
            }

            if(ringPos.get(i) == '.' && ringVals.get(i) == 0){
                acc = acc + 1;
            }
        }
        return numberOfOutOfBoundsTiles == acc;
    }


   /////////////////*SHAPE*/////////////////////

    //Create shape of piece. int 0 equals no ring (NR), int 1 equals lower ring (LR), int 2 equals upper ring (UR).
    public List<Integer> shapePiece(char shape) {
        List<Integer> defaultRingVal = new ArrayList<>();
        switch (shape) {
            case 'A':
                defaultRingVal = new ArrayList<>(Arrays.asList(LR, UR, NR, UR, LR, UR, LR, NR, NR));
                break;
            case 'B':
                defaultRingVal = new ArrayList<>(Arrays.asList(NR, UR, NR, NR, LR, UR, NR, UR, LR));
                break;
            case 'C':
                defaultRingVal = new ArrayList<>(Arrays.asList(NR, UR, NR, NR, LR, UR, LR, UR, NR));
                break;
            case 'D':
                defaultRingVal = new ArrayList<>(Arrays.asList(NR, UR, NR, UR, LR, NR, NR, UR, LR));
                break;
            case 'E':
                defaultRingVal = new ArrayList<>(Arrays.asList(NR, UR, NR, UR, LR, NR, LR, UR, NR));
                break;
            case 'F':
                defaultRingVal = new ArrayList<>(Arrays.asList(NR, NR, LR, NR, LR, UR, LR, UR, NR));
                break;
            case 'G':
                defaultRingVal = new ArrayList<>(Arrays.asList(NR, UR, LR, NR, LR, UR, LR, UR, NR));
                break;
            case 'H':
                defaultRingVal = new ArrayList<>(Arrays.asList(NR, UR, LR, UR, LR, NR, NR, UR, LR));
                break;
        }

        return defaultRingVal;
    }

    public static int countNumRings(char shape){
        switch(shape){
            case 'A': case 'G': case 'H': return 6;
            case 'B': case 'C': case 'D': case 'E': case 'F': return 5;
        }
        return 0;
    }

    ////////////////////* LOCATION *///////////////////////////

    //Given a location param, determine the coords (as enocoded by letters A-Y, a-y) where rings are placed on board
    public List<Character> setRingCoords() {
        return ringCoords(location);
    }

    public static List<Character> ringCoords(char location) {
        List ringPos = new ArrayList();
        ListCoord.Coord c = LETTER_TO_COORD_MAP.get(location);
        int homeIndex = LIST_OF_COORDS.indexOf(c);

        for (int k = 0; k < 3; k = k + 1) {
            for (int i = homeIndex - 13; i < homeIndex - 10; i = i + 1) {
                ringPos.add(COORD_TO_LETTER_MAP.get(LIST_OF_COORDS.get(i + k * 12)));
            }
        }
        return ringPos;
    }


    ///////////////////*ORIENTATION*/////////////////////////
    //To achieve rotation and flipping, rings values are manipulated whilst ring positions kept constant.

    //Determine whether to flip the piece.
    public List<Integer> orientPiece(){
        List<Integer> ringVals = shapePiece(shape);

        switch (getOrientation()) {
            case 'E':
            case 'F':
            case 'G':
            case 'H':
                ringVals = flipPiece(ringVals); break;
        }

        //Determine rotation of piece.
        switch (getOrientation()) {
            case 'A':
            case 'E':
                ringVals = rotatePieceNTimes(ringVals, 0);
                break;
            case 'B':
            case 'F':
                ringVals = rotatePieceNTimes(ringVals, 1);
                break;
            case 'C':
            case 'G':
                ringVals = rotatePieceNTimes(ringVals, 2);
                break;
            case 'D':
            case 'H':
                ringVals = rotatePieceNTimes(ringVals, 3);
                break;
        }

        return ringVals;
    }

    //rotates the piece. Order of indices after 1 clockwise rotation is: (6, 3, 0, 7, 4, 1, 8, 5, 2)
    public static List<Integer> rotatePiece(List<Integer> ringVals){
        List<Integer> rotated = new ArrayList<>();
        for(int s = 6; s < 9; s = s + 1){
            for (int r = s; r >= s - 6  ; r = r - 3) {
                rotated.add(ringVals.get(r));
            }
        }

        return rotated;
    }

    public static List<Integer> rotatePieceNTimes(List<Integer> ringVals, int numRot){
        switch(numRot) {
            case 0 : break;
            case 1: ringVals = rotatePiece(ringVals); break;
            case 2: ringVals = rotatePiece(rotatePiece(ringVals)); break;
            case 3: ringVals = rotatePiece(rotatePiece(rotatePiece(ringVals))); break;
        }
        return ringVals;
    }

    //Flips the piece over.
    // (1) Swap col 0 (list indices 0-2) and col 3 (list indices 6-8),
    // (2) Toggle upper rings to lower rings and vice versa.

    public static List<Integer> flipPiece(List<Integer> ringVals) {
        List<Integer> flippedRingVals = new ArrayList<>();
            for(int i = 0; i < 3; i = i + 1){
                for(int j = 2; j >= 0; j = j - 1){
                    flippedRingVals.add(ringVals.get(j + i*3));
                }
            }
            for (int r = 0; r < 9; r = r + 1) {
            if (flippedRingVals.get(r) == LR) {
                flippedRingVals.set(r, UR);
            }
            else if (flippedRingVals.get(r) == UR) {
                flippedRingVals.set(r, LR);
            }
        }
        return flippedRingVals;
    }

    ///////////////*GETTERS & SETTERS*///////////////

    public void setPiece(String triplet) {
        setShape(triplet.charAt(0));
        setOrientation(triplet.charAt(1));
        setLocation(triplet.charAt(2));
    }

    public void setOrientation(char orientation) {
        this.orientation = orientation;
    }
    public void setShape(char shape) {
        this.shape = shape;
    }
    public void setLocation(char location) {this.location = location;}

    public char getOrientation() {
        return orientation;
    }
    public char getLocation() {
        return location;
    }
    public char getShape() {
        return shape;
    }

}
