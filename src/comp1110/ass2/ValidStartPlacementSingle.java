package comp1110.ass2;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
        import java.util.Random;

        import static comp1110.ass2.GenerateSolutions.*;

/**
 * Created by Daniel on 2/10/2017.
 */
public class ValidStartPlacementSingle extends ValidStartPlacementMulti{
    private static final List<ValidStartPlacementSingle> DICTIONARY = dictionary();

    //Not everything added to dictionary from dsdgd.
    ValidStartPlacementSingle(String startingPlacement, List<String> solutions){
        super(startingPlacement, solutions);
    }

    public static List<ValidStartPlacementSingle> dictionary(){
        List<ValidStartPlacementSingle> dictionary = new ArrayList<>();

        dictionary.add(new ValidStartPlacementSingle("EHSBHnFBg",
                new ArrayList<>(Arrays.asList("EHSBHnFBgHGQAHOGBLDAiCDk", "EHSBHnFBgHGQAHOGDLDAiCDk", "EHSBHnFBgHGlABPGBiDCNCBL"))));

        dictionary.add(new ValidStartPlacementSingle("BHnEHSFCL",
                new ArrayList<>(Arrays.asList("BHnEHSFCLGCgHGQAHODAiCDk", "BHnEHSFCLGCgHGlACiCCNDDP", "BHnEHSFCLGEQHBNADgDAiCDk", "BHnEHSFCLHGlCEjGCNADgDDP", "BHnEHSFCLHGlGAPDBNADgCDi"))));

        dictionary.add(new ValidStartPlacementSingle("FBgEFBDHn",
                new ArrayList<>(Arrays.asList("EFBCEnBFqAESFBiHANGEQDAk", "EFBCEnBFqAESFBiHGQGBkDDN", "EFBCEnBFqAESFBiHGQGDkDDN", "EFBCEnBFqAESFCNGEQHAiDAk", "EFBCEnBFqAESFCNHGlGAPDAi",
                        "EFBCEnBFqAESFCNHGlGCPDAi", "EFBCEnBFqAESFCNHGlGEjDDP", "EFBCEnBFqAESFGhHANGEQDAk", "EFBCEnBFqAESFGhHGQGBkDDN", "EFBCEnBFqAESFGhHGQGDkDDN", "EFBCEnBFqAESFHMGEQHAiDAk",
                        "EFBCEnBFqAESFHMHGlGAPDAi", "EFBCEnBFqAESFHMHGlGCPDAi", "EFBCEnBFqAESFHMHGlGEjDDP", "EFBCEnBFqAESGEQHBNDAiFEl", "EFBCEnBFqAESHGQDGOFElGBi", "EFBCEnBFqAESHGQDGOFElGDi",
                        "EFBCEnBFqAESHGQFDkGBiDDN", "EFBCEnBFqAESHGQFElGBiDDN", "EFBCEnBFqAESHGlFAPGBiDDN", "EFBCEnBFqAESHGlFFQGBiDDN", "EFBCEnBFqAESHGlGAPDBNFEj", "EFBCEnBFqDAWHEOGGQADkFGS",
                        "EFBCEnBFqDHSHGQAHOFElGBi", "EFBCEnBFqDHSHGQAHOFElGDi", "EFBCEnBFqFBiHANACkGFSDDP", "EFBCEnBFqFCNHAiACkGFSDDP", "EFBCEnBFqFGhHANACkGFSDDP", "EFBCEnBFqFHMHAiACkGFSDDP",
                        "EFBCEnBFqHAWDEOGGQADkFGS"))));

        dictionary.add(new ValidStartPlacementSingle("FBgEFBDHn",
                new ArrayList<>(Arrays.asList("FBgEFBDHnAESHGlBEjGCNCCP", "FBgEFBDHnAESHGlCEjGCNBBG", "FBgEFBDHnAESHGlCEjGCNBDP", "FBgEFBDHnAESHGlGBiCCNBBG", "FBgEFBDHnAESHGlGBiCCNBDP",
                        "FBgEFBDHnCCWHEOGGQADkBCT", "FBgEFBDHnHAWCGOGGQADkBCT"))));

        dictionary.add(new ValidStartPlacementSingle("FCLDGSBHn",
                new ArrayList<>(Arrays.asList("FCLDGSBHnGCgHGlACiCCNECP", "FCLDGSBHnHGlCEjGCNADgECP"))));

        dictionary.add(new ValidStartPlacementSingle("FBgEHSBHn",
                new ArrayList<>(Arrays.asList("FBgEHSBHnHGQAHOGBLDAiCDk", "FBgEHSBHnHGQAHOGDLDAiCDk", "FBgEHSBHnHGlABPGBiDCNCBL"))));

        dictionary.add(new ValidStartPlacementSingle("FCLDGSBHn",
                new ArrayList<>(Arrays.asList("FCLDGSBHnGCgHGlACiCCNECP", "FCLDGSBHnHGlCEjGCNADgECP"))));

        dictionary.add(new ValidStartPlacementSingle("FBgEFBBGS",
                new ArrayList<>(Arrays.asList("FBgEFBBGSGHnHGQAHOCAkDAW", "FBgEFBBGSGHnHGQAHOCAkDCi", "FBgEFBBGSGHnHGQAHOCDWDBk"))));

        dictionary.add(new ValidStartPlacementSingle("EEnFCLBFq",
                new ArrayList<>(Arrays.asList("EEnFCLBFqAESGEQHBNDAiCDk", "EEnFCLBFqAESHGlCEjGCNDDP", "EEnFCLBFqAESHGlGAPDBNCDi", "EEnFCLBFqAESHGlGBiCCNDDP"))));

        dictionary.add(new ValidStartPlacementSingle("EEnFCLBFq",
                new ArrayList<>(Arrays.asList("EEnFCLBFqAESGEQHBNDAiCDk", "EEnFCLBFqAESHGlCEjGCNDDP", "EEnFCLBFqAESHGlGAPDBNCDi", "EEnFCLBFqAESHGlGBiCCNDDP"))));

        dictionary.add(new ValidStartPlacementSingle("DGSEFBBFq,",
                new ArrayList<>(Arrays.asList("DGSEFBBFqCHnHGQAHOFElGBi", "DGSEFBBFqCHnHGQAHOFElGDi", "DGSEFBBFqGHnHGQAHOCAkFEX", "DGSEFBBFqGHnHGQAHOCAkFGh", "DGSEFBBFqGHnHGQAHOCDWFHj"))));

        return dictionary;
    }

   //Given an objective returns smallest length substring of that obejctive that still has unique solution.
    public String getUniqueStartPlacement(int chooseObj){
        String objective = solutions.get(chooseObj);
        String validStartPoint = "";
        List<String> solutions;

        for(int i = objective.length(); i > startingPlacement.length(); i = i - 3){
            String startPoint = objective.substring(0, i);
            solutions = removeOrderingOnSol(myGetSolutions(startPoint, MAX_AVAILABLE_MOVES));
            if(solutions.size() == 1){
                validStartPoint = startPoint;
            }
        }
        System.out.println(validStartPoint);
        return validStartPoint; //will never return "", every objective minus one move will have unique solution.
    }

    public static List<String> getUniqueStartPlacements(){
        List<String> uniqueStartPlacements = new ArrayList<>();
        for(int j = 0; j < DICTIONARY.size(); j = j + 1){
            for(int i = 0; i < DICTIONARY.get(j).solutions.size(); i = i + 1){
                uniqueStartPlacements.add(DICTIONARY.get(j).getUniqueStartPlacement(i));
            }
        }
        return uniqueStartPlacements;
    }


    public static List<String> getRandomValidUniqueStarts(){
        ValidStartPlacementMulti random = generateValidStartPlacement();
        ValidStartPlacementSingle startPoint = new ValidStartPlacementSingle(random.startingPlacement, random.solutions);
        List<String> uniquesValidStartPlacements = new ArrayList<>();
        for(int i = 0; i < startPoint.solutions.size(); i = i + 1){
            uniquesValidStartPlacements.add(startPoint.getUniqueStartPlacement(i));
        }
        return uniquesValidStartPlacements;
    }

    public static List<String> filterUniquesByLength(int length, List<String> uniques){
        List<String> filteredUniques = new ArrayList<>();
        for(String u : uniques){
            if(u.length() == length){
                filteredUniques.add(u);
            }
        }
        return filteredUniques;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        /*Dictionary method: */
        /*List<String> unfilteredUniques = getUniqueStartPlacements();
        for(int i = 0; i < 8; i = i +1){
            List<String> uniques = filterUniquesByLength(i*3, unfilteredUniques);
            for(String u: uniques){
                System.out.println(u);*/

        /*Get all uniques from a single random start point
        * Method is essentially the same as dictionary, but dictionary started with known start placements pre-generated
        * This method starts with a single randomly generated start placement.*/
        double startTime = System.currentTimeMillis();
        List<String> unfilteredUniques = getRandomValidUniqueStarts();
        double endTime = System.currentTimeMillis();
        double time = endTime - startTime;
        if(time > 120*1000){
            System.exit((int)time/1000);
        }

        System.out.println(System.lineSeparator() + "Copy and paste into TaskEightGetStartPlacement inner Class, into the array in dictionary().");
        for(int i = 0; i < 8; i = i +1){
            List<String> uniques = filterUniquesByLength(i*3, unfilteredUniques);
            for(String u: uniques){
                System.out.println("\"" + u + "\"" + ", " + "\"" + removeOrderingOnSol(myGetSolutions(u, maxAvailableMoves())) + "\"");
            }
        }

        System.out.println("Time taken: " + time/1000 + "(s).");
    }

    /**
     * Created by Daniel on 2/10/2017.
     */
    // FIXME Task 8: Determine starting placements that have solutions. Implement pickStart() into JavaFX BoardGUI.


    public static String[][] dictionary1(){
        return new String[][]{
                {"EHSBHnFBgHGl", "EHSBHnFBgHGlABPGBiDCNCBL"},
                {"BHnEHSFCLGEQ", "BHnEHSFCLGEQHBNADgDAiCDk"},
                {"EFBCEnBFqDAW", "EFBCEnBFqDAWHEOGGQADkFGS"},
                {"EFBCEnBFqHAW", "EFBCEnBFqHAWDEOGGQADkFGS"},
                {"FBgEFBDHnCCW", "FBgEFBDHnCCWHEOGGQADkBCT"},
                {"FBgEFBDHnHAW", "FBgEFBDHnHAWCGOGGQADkBCT"},
                {"DHnBGKEHSGEQ", "DHnBGKEHSGEQHBNADgFElCDi"},
                {"DHnBFqEFBCCW", "DHnBFqEFBCCWHEOGGQADkFGS"},
                {"DHnBFqEFBHAW","DHnBFqEFBHAWCGOGGQADkFGS"},
                {"FBgCEnEHSHGl", "FBgCEnEHSHGlABPGBiDCNBCL"},
                {"CEnBGKEHSGEQ", "CEnBGKEHSGEQHBNADgDAiFEl"},
                {"FBgCEnEFBDAW", "FBgCEnEFBDAWHEOGGQADkBCT"},
                {"CHSFCLBFqAHQ", "CHSFCLBFqAHQHBNDAiGHlEAo"}
        };
    }

    private static final String[][] DICTIONARY_OF_STARTS = dictionary1();

    /*Use this method for task 8*/
    public static String[] pickStart(int i){
        String[] startPoint = DICTIONARY_OF_STARTS[i];
        return startPoint;
    }
}

