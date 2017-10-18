package comp1110.ass2;

import java.util.*;


import static comp1110.ass2.ListCoord.LETTERS_ENCODING_TILES;

/**
 * Created by Daniel on 4/09/2017.
 *All code in GenerateSolutions class is by Daniel Lai (u6352900).
 */
public class GenerateSolutions {
    private static final int NUM_SHAPES = 8;
    private static final int NUM_ORIENTATIONS = 8;
    private static final String SHAPES = "ABCDEFGH";
    private static final String OREINTATIONS = "ABCDEFGH"; //redundant but just for clarity when reading code
    private static final List<String> ALL_PIECE_STATES = parseTriplets(generateAllPieceStates());
    private static final String CORNERS = "Lg";
    private static final String INNER_TILES = "LMNOPQRSVdghijklmn";
    private static final String CENTRAL_TILES = "WXYabc";

    private static final int TOGGLE_ON_ALL_RULES = 0;
    private static final int TOGGLE_ON_RULES_ZERO_TO_TWO = 1;
    private static final int TOGGLE_ON_RULE_THREE_ONLY = 2; //note implicitly includes rule 0, check BoardGUI Class, legalMove() method.

    public static final List<String> MAX_AVAILABLE_MOVES = (maxAvailableMoves());


//////////////////////////////*GENERATING ALL PIECE STATES AS 3-LETTER STRINGS*/////////////////////////////////////////

    //Generates 8*50 (NUM_LOCATIONS*NUM_ORIENTATIONS) possible piece states for a single piece
    public static String generateAllPieceStates() {

        String s = "";
        for (int m = 0; m < NUM_SHAPES; m = m + 1) {
            for (int n = 0; n < NUM_ORIENTATIONS; n = n + 1) {
                for (int i = 0; i < LETTERS_ENCODING_TILES.length(); i = i + 1) {
                    s = s + SHAPES.charAt(m) + OREINTATIONS.charAt(n) + LETTERS_ENCODING_TILES.charAt(i);
                }
            }
        }
        return s;
    }

    //Parses an entire string into groups of three length strings, and stores this in a list.
    public static List<String> parseTriplets(String s) {
        int counter = 0;
        List<String> allTriplets = new ArrayList<>();
        for (int i = 0; i < s.length(); i = i + 3) {
            String triplet = s.substring(i, i + 3);
            allTriplets.add(triplet);
        }
        return allTriplets;
    }

////////////////////////////////CONVERTING 3-LETTER STRINGS TO PIECE STATES/////////////////////////////////////////////

    //Given a 3 letter string, sets the values of the fields for a Piece constructor.
    public static Piece stringToPieceState(String triplet) {
        char k = triplet.charAt(0);
        Piece p = Piece.nil;
        switch (k) {
            case 'A':
                Piece.A.setPiece(triplet);
                p = Piece.A;
                break;
            case 'B':
                Piece.B.setPiece(triplet);
                p = Piece.B;
                break;
            case 'C':
                Piece.C.setPiece(triplet);
                p = Piece.C;
                break;
            case 'D':
                Piece.D.setPiece(triplet);
                p = Piece.D;
                break;
            case 'E':
                Piece.E.setPiece(triplet);
                p = Piece.E;
                break;
            case 'F':
                Piece.F.setPiece(triplet);
                p = Piece.F;
                break;
            case 'G':
                Piece.G.setPiece(triplet);
                p = Piece.G;
                break;
            case 'H':
                Piece.H.setPiece(triplet);
                p = Piece.H;
                break;
        }
        return p;
    }


////////////////////////////////////////////GET ALL LEGAL FIRST MOVES///////////////////////////////////////////////////
    /*We should have 3200 unique encodings that are well-formed piece placements.
    *This represents all possible next moves at a given point in the game, and this pool is what will be filtered.
    * MAX_AVAILABLE_MOVES (holds 812 legal first moves) in GenerateSolutions class filters based on the rules implemented in BoardGUI Class.
    * Playing the game will reduce the number of moves; adding pieces to board adds constraints, hence the first moves represent the maximum
    * number of moves
    * Proper implementation of rules are checked via Steve's tests for tasks 5 and 6.*/

    //Get all legal first moves.
    public static List<Board> getFirstMoves() {
        List<Board> firstMoves = new ArrayList<>();

        for (int i = 0; i < ALL_PIECE_STATES.size(); i = i + 1) {
            Board newGame = Board.newGame();
            newGame.updateBoardState(stringToPieceState(ALL_PIECE_STATES.get(i)),TOGGLE_ON_ALL_RULES);

            if (newGame.getPiecesUsed().size() == 2) {
                firstMoves.add(newGame);
            }
        }
        return firstMoves;
    }


    //get the maximum pool of possible moves on any given turn
    public static List<String> maxAvailableMoves() {
        List<String> availableMoves = new ArrayList<>();
        for (Board b : getFirstMoves()) {
            availableMoves.add(b.getMoveSequence());
        }
        return availableMoves;
    }


//////////////////////////////////////////////GET SOLUTIONS/////////////////////////////////////////////////////////////

//Given a single BoardGUI(boardState, piecesUsed, moveSequence), generate all possible next moves.
    public static List<String> getNextMoves(Board b, int moveNum, List<String> availableMoves) {
        List<String> nextMoveSeq = new ArrayList<>();
        List<String> filteredMoves = composedFilter(b, availableMoves);

        for (String move : filteredMoves) {
            Piece piecePlacement = stringToPieceState(move);

            int numIsoBeforeMove = countIsolatedTiles(b);
            b.updateBoardState(piecePlacement, TOGGLE_ON_ALL_RULES);
            int numIsoAfterMove = countIsolatedTiles(b);
            int isoCountDifference = numIsoAfterMove - numIsoBeforeMove;
            boolean isolatedTilesConstraint = isoCountDifference <= 3 && isoCountDifference + numIsoBeforeMove <= 7;

            String t = b.getMoveSequence();
                if (t.length() == moveNum * 3) {
                    if(isolatedTilesConstraint && allCornersValid(b)){
                        nextMoveSeq.add(t);
                    }
                    b.removePiece(piecePlacement);
                }
        }
        return (nextMoveSeq);
    }

    //Given a move sequence, update a blank board to the state specified by the move sequence.
    public static Board updateBoardTo(String incompletePlacement) {
        Board b = Board.newGame();
        List<String> triplets = parseTriplets(incompletePlacement);
        for (String tr : triplets){
            int prevMoveSeqLength = b.getMoveSequence().length();
            b.updateBoardState(stringToPieceState(tr), TOGGLE_ON_ALL_RULES);
            if(prevMoveSeqLength == b.getMoveSequence().length()){
                break;
            }
        }
        return b;
    }

    //Given a list of starting placements, find the next valid move sequences for each starting placement.
    public static List<String> getNextMovesFold(List<String> prevMoves, int moveNum, List<String> availableMoves) {
        List<String> nextMoveSequences = new ArrayList<>();
        List<Board> nextBoards = new ArrayList<>();
        for (String pr : prevMoves) {
            nextBoards.add(updateBoardTo(pr));
        }
        for (Board b : nextBoards) {
            nextMoveSequences.addAll(getNextMoves(b, moveNum, availableMoves));
        }
        return (nextMoveSequences);
    }

    public static List<String> applyNextMovesFoldNtimes(String startPoint, List<String> availableMoves, int numApplic) {
        int moveNum = 1 + (startPoint.length() / 3);
        List<String> nextMoves = getNextMoves(updateBoardTo(startPoint), moveNum, availableMoves);
        for(int i = moveNum; i < numApplic; i = i + 1) {
            moveNum = moveNum + 1;
            nextMoves = getNextMovesFold(nextMoves, moveNum, availableMoves);
        }
        return nextMoves;
    }

    public static List<String> myGetSolutions(String startPoint, List<String> availableMoves) {
        List<String> solutions = applyNextMovesFoldNtimes(startPoint, availableMoves, 8);
        return (solutions);
    }

    public static String[] getSolutions(String placement) {
        System.out.println("Generating solutions for " + placement + "...");
        String[] array;
        List<String> solutions = removeOrderingOnSol(myGetSolutions(placement, MAX_AVAILABLE_MOVES));
        array = solutions.toArray(new String[solutions.size()]);
        System.out.println("The solutions for " + placement + " are: ");
        for(String a: array){
            System.out.println(a);
        }
        System.out.println();
        return array;
    }

    public static List<String> composedFilter(Board initialState ,List<String> availableMoves){
    return filterBySearchRegions(initialState, filterMovesByPiecesUsed(initialState, availableMoves));
    }

/////////////////////////////////////////////FILTERS AND HEURISTICS/////////////////////////////////////////////////////
//FILTER 0: INNER TILES
    //Pieces A,C,D,F,G,H must occupy an inner tile only. Pieces B, E do not have to occupy an inner tile but may.
    public static List<String> filterByInnerTiles(List<String> availableMoves){
        String innerTiles = "LMNOPQRSdnmlkjihgV";
        List<Piece> flexiblePieces = new ArrayList<>();

        flexiblePieces.add(Piece.B);
        flexiblePieces.add(Piece.E);

        List<Character> innerTilesList = new ArrayList<>();
        for(int i = 0; i < innerTiles.length(); i = i + 1){
            innerTilesList.add(innerTiles.charAt(i));
        }
        List<String> filteredMoves = new ArrayList<>();
        for(String move: availableMoves){
            if(innerTilesList.contains(move.charAt(2)) && (move.charAt(0) != 'B' || move.charAt(0) != 'E')){
                filteredMoves.add(move);
            }
            else if((move.charAt(0) == 'B' || move.charAt(0) == 'E')){
                filteredMoves.add(move);
            }
        }
        return filteredMoves;
    }

//FILTER 1: PIECES USED
    //filters available moves by eliminating the pieces already used.
    public static List<String> filterMovesByPiecesUsed(Board b, List<String> availableMoves) {
        List<Character> usedPieces = new ArrayList<>();
        List<String> filteredMoves = new ArrayList<>();
        for (Piece p : b.getPiecesUsed()) {
            usedPieces.add(p.getShape());
        }

        for (String x : availableMoves) {
            if (!usedPieces.contains(x.charAt(0))) {
                filteredMoves.add(x);
            }
        }
        return filteredMoves;
    }

//FILTER 3: SEARCH REGIONS
    /*Filter available moves by returning tiles satisfying the definition of a valid search region.
    A valid search region must have:
     5-9 unoccupied tiles,
     No upper rings in adj tiles one unit in the N,S,E,W directions.
    */
    public static List<String> filterBySearchRegions(Board b, List<String> availableMoves) {
        List<String> filteredMoves = new ArrayList<>();
        List<Character> allSearchRegions = findSearchRegions(b, 9);
        for (String move : availableMoves) {
                if(allSearchRegions.contains(move.charAt(2))){
                    filteredMoves.add(move);
                }
            }
        return filteredMoves;
    }

    //A search region is a 3x3 grid that has exactly 5 - 9 unoccupied tiles
    public static List<Character> findSearchRegions(Board b, int threshhold) {
        List<Character> searchRegions = new ArrayList<>();

        for (int i = 0; i < LETTERS_ENCODING_TILES.length(); i = i + 1) {
            char loc = LETTERS_ENCODING_TILES.charAt(i);


            if (isSearchRegion(b, loc, threshhold)) {
                searchRegions.add(loc);
            }
        }
        return searchRegions;
    }

    //Search region condition
    public static boolean isSearchRegion(Board b, char loc, int threshhold) {
        int homeRingVal = b.getBoardState().get(loc);
        return !containsAdjUpperRing(b, loc)
                && 5 <= countUnoccupiedTiles(b, loc)
                && countUnoccupiedTiles(b, loc) <= threshhold //3x3 grid must have 5-9 unoccupied tiles, else cannot put piece in it.
                && (homeRingVal != 2 && homeRingVal != 3);

    }
    //count the number of 1's and 0's from a 9x9 grid given a 'home' location.
    public static int countUnoccupiedTiles(Board b, char loc) {
        return countTilesWithVal(b, loc, 0) + countTilesWithVal(b, loc, 1);
    }

    public static int countTilesWithVal(Board b, char loc, int val) {
        List<Character> grid = Piece.ringCoords(loc);
        int acc = 0;
        for (Character ringPos : grid) {
            int tileVal = -1;
            if (ringPos != '.') {
                tileVal = b.getBoardState().get(ringPos);
            }
            if (tileVal == val) {
                acc = acc + 1;
            }
        }
        return acc;
    }

    //count number of 3's in N, S, E, W locations given a 'home' location.
    public static boolean containsAdjUpperRing(Board b, char loc) {
        List<Character> grid = Piece.ringCoords(loc);
        int acc = 0;
        List<Integer> tiles = new ArrayList<>(Arrays.asList(1,3,5,7));
        for (Integer i: tiles) {
            int tileVal = -1;
            if (grid.get(i) != '.') {
                tileVal = b.getBoardState().get(grid.get(i));
            }
            if (tileVal == 3) {
                return true;
            }
        }
        return false;
    }

//FILTER 4: ISOLATED TILES:
    public static int countIsolatedTiles(Board b){
        int countIsoTiles = 0;
        for(int i = 0; i < LETTERS_ENCODING_TILES.length(); i = i + 1){
            if(isIsolatedTile(b, LETTERS_ENCODING_TILES.charAt(i))){
                countIsoTiles = countIsoTiles + 1;
            }
        }
        return countIsoTiles;
    }

    public static String getIsolatedTiles(Board b){
        String isoTiles = "";
        for(int i = 0; i < LETTERS_ENCODING_TILES.length(); i = i + 1){
            if(isIsolatedTile(b, LETTERS_ENCODING_TILES.charAt(i))){
                isoTiles = isoTiles + LETTERS_ENCODING_TILES.charAt(i);
            }
        }
        return isoTiles;
    }

    public static boolean isIsolatedTile(Board b, char location){
        return isObstructed(b, location) || isSurrounded(b, location);
    }

    public static boolean isObstructed(Board b, char location){
        List<Character> adjLoc = getNorthSouthEastWestAdjTiles(b, location);
        if(adjLoc.isEmpty()){
            return false;
        }
        for(Character loc : adjLoc){
            if(loc != '.'){
                int tileVal = b.getBoardState().get(loc);
                if(tileVal == 3){return true;}
            }
        }
        return  false;
    }

    public static boolean isSurrounded(Board b, char location) {
        List<Character> adjLoc = getNorthSouthEastWestAdjTiles(b, location);
        if(adjLoc.isEmpty()){
            return false;
        }
        for (Character loc : adjLoc) {
            if (loc != '.') {
                int tileVal = b.getBoardState().get(loc);
                if (!isObstructed(b, loc) && tileVal != 2) {
                    return false;
                }
            }
        }
        return true;
    }

    //Returns N,S,E,W positions around an unoccupied tile.
    public static List<Character> getNorthSouthEastWestAdjTiles(Board b, char location){
        List<Character> adjLoc = new ArrayList<>();
        List<Character> grid = Piece.ringCoords(location); //3x3 grid
        int centrePosOfGrid = b.getBoardState().get(Piece.ringCoords(location).get(4));
        boolean unoccupiedAtCentre = centrePosOfGrid == 0 || centrePosOfGrid == 1;
        if(unoccupiedAtCentre){
            for(int i = 0; i < grid.size(); i = i + 1) {
                if (i == 1 || i == 3 || i == 5 || i == 7) {
                    adjLoc.add(grid.get(i));
                }
            }
        }
        return adjLoc;
    }

//FILTER 6: VALID CORNERS RULE
/*See GenerateSolutionTests for explanation of why this rule works.
    /*Implementation: A corner tile ('L' or 'g') is invalid if it:
    has greater than or equal to 5 unoccupied tiles
    and is obstructed by an upper ring (i.e. not a valid search region).
    Code below written as a negation of the above statement*/

    public static boolean allCornersValid(Board b){
        int countTrue = 0;
        for(int i = 0; i < CORNERS.length(); i = i + 1){
                /*Boolean condition checks:
                *return false if: tile position has an upper ring obstruction
                *AND 3x3 grid around tile position has >= 5 unoccupied tiles*/
            if(isSearchRegion(b, CORNERS.charAt(i), 9)
                    || countUnoccupiedTiles(b, CORNERS.charAt(i)) < 5){
                countTrue = countTrue + 1;
            }
        }
        return countTrue == CORNERS.length();
    }


//////////////////////////////////////////REMOVING ORDER ON SOLUTIONS///////////////////////////////////////////////////

    //Duplicate game states can be filtered out, i.e. there may be more than 1 way to get to the same game state.
    public static List<String> removeOrderingOnSol(List<String> solutions) {
        List<String> filteredSol = new ArrayList<>();
        List<String> storePals = new ArrayList<>();

        for (int i = 0; i < solutions.size(); i = i + 1) {
            for (int j = 0; j < solutions.size(); j = j + 1) {
                if (isAnagram(solutions.get(i), solutions.get(j))) {
                    storePals.add(solutions.get(j));
                }
            }
            if (!filteredSol.contains(storePals.get(0))) {
                filteredSol.add(storePals.get(0));
            }
            storePals.clear();
        }
        return filteredSol;
    }

    //Note this always works because we are guaranteed that s1 and s2 have no repeated triplets.
    // otherwise a contains method of anagram checking would not always work.
    /*e.g. Letting a and b stand for triplets (strings with 3 char length)
    the case is 'abba' an anagram of 'baba' will never be considered because the string 'abba' cannot exist as it has 2 b's.*/
    public static boolean isAnagram(String s1, String s2) {
        int countTrue = 0;
        List<String> s1Triplets = parseTriplets(s1);
        List<String> s2Triplets = parseTriplets(s2);

        if (s1.length() == s2.length() && s1.length() % 3 == 0) {
            for (String triplet : s1Triplets) {
                if (s2Triplets.contains(triplet)) {
                    countTrue = countTrue + 1;
                }
            }
        }
        return countTrue == s1Triplets.size();
    }

    public static void main(String[] args) {
        Board b = updateBoardTo("FBgEFBDHnHAW");
        System.out.println(b.getMoveSequence());
        System.out.println(StepsGame.isPlacementSequenceValid("FBgEFBDHnHAWGAi"));


    }



}






/* */