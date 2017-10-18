//https://www.youtube.com/watch?v=VW_KGaohpng

package comp1110.ass2;

import java.lang.reflect.Array;
import java.util.*;

import static comp1110.ass2.GenerateSolutions.*;

/**
 * This class provides the text interface for the Steps Game
 *
 * The game is based directly on Smart Games' IQ-Steps game
 * (http://www.smartgames.eu/en/smartgames/iq-steps)
 */
public class StepsGame {

    /**
     * Determine whether a piece placement is well-formed according to the following:
     * - it consists of exactly three characters
     * - the first character is in the range A .. H (shapes)
     * - the second character is in the range A .. H (orientations)
     * - the third character is in the range A .. Y and a .. y (locations)
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */

    //Task 2 By Wangchao Wei 22/8
   public static boolean isPiecePlacementWellFormed(String piecePlacement) {
        char[] c1 = piecePlacement.toCharArray();
        if (c1[0] >= 'A' && c1[0] <= 'H') {
            if (c1[1] >= 'A' && c1[1] <= 'H') {
                if ((c1[2] >= 'A' && c1[2] <= 'Y') || (c1[2] >= 'a' && c1[2] <= 'y')) {
                    return true;
                }
            }
        } return false;
    }


        // FIXME Task 2: determine whether a piece placement is well-formed

    /**
     * Determine whether a placement string is well-formed:
     *  - it consists of exactly N three-character piece placements (where N = 1 .. 8);
     *  - each piece placement is well-formed
     *  - no shape appears more than once in the placement
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */

    //Task 3 By Amos 24/8

   public static boolean isPlacementWellFormed(String placement) {
        if (placement != null) {
            if (placement.length() % 3 == 0 && placement.length() <= 24 && placement.length() >= 3) {
                int i = 0;
                boolean s = true;
                while (i < placement.length()) {
                    String c = placement.substring(i, i + 3);
                    i = i + 3;
                    s = s && isPiecePlacementWellFormed(c);
                }
                if (s) {
                    int n = placement.length() / 3;
                    int a = 0;
                    int b = 0;          // take the first element from every piece placement and form a array
                    Character[] t = new Character[n];
                    while (a < placement.length()) {
                        t[b] = placement.charAt(a);
                        a += 3;
                        b += 1;
                    }
                    HashSet<Character> mySet = new HashSet<>(Arrays.asList(t));
                    // A set can not contain repeat elements
                    return mySet.size() == t.length;

                }
            }
        }
        return false;
    }
    // FIXME Task 3: determine whether a placement is well-formed

   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //All code for task 5, 6, 9 by Daniel Lai (u6352900)
    /**
     * Determine whether a placement sequence is valid.  To be valid, the placement
     * sequence must be well-formed and each piece placement must be a valid placement
     * (with the pieces ordered according to the order in which they are played).
     *
     * @param placement A placement sequence string
     * @return True if the placement sequence is valid
     */

    public static boolean isPlacementSequenceValid(String placement) {
        List<String> triplets = parseTriplets(placement);
        for(String t: triplets){
            if(!isPiecePlacementWellFormed(t))
                return false;
        }
        Board b = GenerateSolutions.updateBoardTo(placement);
        String moveSeq = b.getMoveSequence();
        return moveSeq.length() == placement.length();
    }
    // FIXME Task 5: determine whether a placement sequence is valid


    /**
     * Given a string describing a placement of pieces and a string describing
     * an (unordered) objective, return a set of all possible next viable
     * piece placements.   A viable piece placement must be a piece that is
     * not already placed (ie not in the placement string), and which will not
     * obstruct any other unplaced piece.
     *
     * @param placement A valid sequence of piece placements where each piece placement is drawn from the objective
     * @param objective A valid game objective, but not necessarily a valid placement string
     * @return An set of viable piece placements
     */
    public static Set<String> getViablePiecePlacements(String placement, String objective) {
        List<String> placedPieces = parseTriplets(placement);
        List<String> objectivePieces = parseTriplets(objective);
        if (!objectivePieces.containsAll(placedPieces)) return new HashSet<String>();
        int moveNum = placement.length()/3;
        List<String> solutions = myGetSolutions(placement, parseTriplets(objective));
        Set<String> nextMovesSet = new HashSet<>();
        for(String s: solutions){
            for(int i = 0; i < parseTriplets(s).size(); i = i + 1){
                if(i == moveNum){
                    nextMovesSet.add(parseTriplets(s).get(i));
                }
            }
        }
        return nextMovesSet;
    }

    // FIXME Task 6: determine the correct order of piece placements

    /**
     * Return an array of all unique (unordered) solutions to the game, given a
     * starting placement.   A given unique solution may have more than one than
     * one placement sequence, however, only a single (unordered) solution should
     * be returned for each such case.
     *
     * @param placement  A valid piece placement string.
     * @return An array of strings, each describing a unique unordered solution to
     * the game given the starting point provided by placement.
     */
    static String[] getSolutions(String placement) {

        return GenerateSolutions.getSolutions(placement);
    }
        // FIXME Task 9: determine all solutions to the game, given a particular starting placement
}
