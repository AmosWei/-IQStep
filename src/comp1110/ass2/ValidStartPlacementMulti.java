package comp1110.ass2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static comp1110.ass2.GenerateSolutions.*;

/**
 * Created by Daniel on 27/09/2017.
 */
public class ValidStartPlacementMulti {
    private static final int TOGGLE_ON_ALL_RULES = 0;
    private static final int NUM_OF_START_PLACEMENTS_CHECKED = 3;
    private static final List<String> FIRST_MOVES = filterByInnerTiles(filterByIsolatedTiles(Board.newGame(), MAX_AVAILABLE_MOVES, 2));

    protected String startingPlacement;
    protected List<String> solutions;

    ValidStartPlacementMulti(String startingPlacement, List<String> solutions) {
        this.startingPlacement = startingPlacement;
        this.solutions = solutions;
    }

    @Override
    public String toString() {
        return "(Start placement: " + startingPlacement + ", Solutions: " + solutions + ")";
    }


    public static List<String> filterByIsolatedTiles(Board b, List<String> availableMoves, int threshhold){
        List<String> filteredMoves = new ArrayList<>();
        for(String move: availableMoves){
            int numIsoTilesBefore = countIsolatedTiles(b);
            b.updateBoardState(stringToPieceState(move), TOGGLE_ON_ALL_RULES);
            int numIsoTilesAfter = countIsolatedTiles(b);
            b.removePiece(stringToPieceState(move.substring(move.length() - 3)));

            int numIsoTilesDiff = numIsoTilesAfter - numIsoTilesBefore;
            boolean isoTileConstraint = numIsoTilesDiff <= threshhold && numIsoTilesAfter <= 7;
            if(isoTileConstraint){
                filteredMoves.add(move);
            }
        }
        return filteredMoves;
    }

    public static String generateRandomPiecePlacement(Board b){
        Random gen = new Random();
        String piecePlacement = "";

        if(b.getPiecesUsed().isEmpty()){
            int randomIndex = gen.nextInt(FIRST_MOVES.size());
            piecePlacement = FIRST_MOVES.get(randomIndex);
        }

        else{
         List<String> availableMoves = filterByIsolatedTiles(Board.newGame(), MAX_AVAILABLE_MOVES, 1);
         if(!availableMoves.isEmpty()){
             int randomIndex = gen.nextInt(availableMoves.size());
             piecePlacement = availableMoves.get(randomIndex);
           }
        }
        return piecePlacement;
    }

    public static String generateRandomStartPlacement(){
        System.out.println("Generating a start placement...");
        Board b = Board.newGame();
        while(b.getMoveSequence().length() < 3*3){
            b = Board.newGame();
            for(int i = 0; i < 100; i = i + 1){
                String piecePlacement = generateRandomPiecePlacement(b);
                b.updateBoardState(stringToPieceState(piecePlacement), TOGGLE_ON_ALL_RULES);
                if(b.getMoveSequence().length() >= 3*3){break;}
            }
        }

        System.out.println("Start placement: " + b.getMoveSequence());
        return b.getMoveSequence();
    }

    public static ValidStartPlacementMulti generateValidStartPlacement(){
        String startPlacement = generateRandomStartPlacement();
        System.out.println("Checking " + startPlacement + " is valid (i.e. has solutions.)");
        List<String> solutions = removeOrderingOnSol(myGetSolutions(startPlacement, MAX_AVAILABLE_MOVES));
        ValidStartPlacementMulti vsp = new ValidStartPlacementMulti(startPlacement, solutions);
        if(!solutions.isEmpty()){System.out.println(startPlacement + " is valid. Has at least one solution." + System.lineSeparator());}
        else{System.out.println(startPlacement + " is invalid. Has no solutions." + System.lineSeparator());}
        return vsp;
    }

    public static List<ValidStartPlacementMulti> getStartPlacements(){
        List<ValidStartPlacementMulti> validStartPlacements = new ArrayList<>();
        for(int i = 0; i < NUM_OF_START_PLACEMENTS_CHECKED; i = i + 1){
            ValidStartPlacementMulti vsp = generateValidStartPlacement();
            if(!vsp.solutions.isEmpty()){
                validStartPlacements.add(vsp);
            }
        }
        return validStartPlacements;
    }


    //Run this program and add output to ValidStartingPlacement textFile.
    public static void main(String[] args) {
        double startTime = System.currentTimeMillis();
        List<ValidStartPlacementMulti> startPlacements = getStartPlacements();
        System.out.println();
        double endTime = System.currentTimeMillis();
        double time = endTime - startTime;

        System.out.println("Copy and paste the output below into the ValidStartingPlacementTextFile:" + System.lineSeparator());
        for(ValidStartPlacementMulti sp : startPlacements){
            System.out.println(sp.toString());
        }
        System.out.println("The time taken was " + time/1000 + "(s), to get "+ startPlacements.size()
                + " valid start placements after trying " + NUM_OF_START_PLACEMENTS_CHECKED + " start placements.");
    }
}