//Created by Wangchao Wei 9/24
package comp1110.ass2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import static comp1110.ass2.Piece.rotatePieceNTimes;

import static org.junit.Assert.assertTrue;

public class CheckRotateTest {
        @Test
    public void rotatePieceNTimesTestRotate() {
            ArrayList<Integer> ary = new ArrayList<>();
            ary.addAll(Arrays.asList(0,0,0,2,1,1,2,2,2));
            assertTrue("Your failed when it rotate 0 time ",rotatePieceNTimes(ary, 0).equals(ary));
        }
        @Test
    public void rotatePieceNTimesTestRotateOne(){
            ArrayList<Integer> ary = new ArrayList<>();
            ary.addAll(Arrays.asList(0,0,0,2,1,1,2,2,2));
            ArrayList<Integer> ary1 = new ArrayList<>();
            ary1.addAll(Arrays.asList(2,2,0,2,1,0,2,1,0));
            assertTrue("Your failed when it rotate 1 time ",rotatePieceNTimes(ary,1).equals(ary1));
        }
        @Test
    public void rotatePieceNTimesTestRotateTwo(){
            ArrayList<Integer> ary = new ArrayList<>();
            ary.addAll(Arrays.asList(0,0,0,2,1,1,2,2,2));

            ArrayList<Integer> ary2 = new ArrayList<>();
            ary2.addAll(Arrays.asList(2,2,2,1,1,2,0,0,0));
            assertTrue("Your failed when it rotate 2 time ",rotatePieceNTimes(ary,2).equals(ary2));
    }
        @Test
    public void rotatePieceNTimesTestRotateThree(){
            ArrayList<Integer> ary = new ArrayList<>();
            ary.addAll(Arrays.asList(0,0,0,2,1,1,2,2,2));
            ArrayList<Integer> ary3 = new ArrayList<>();
            ary3.addAll(Arrays.asList(0,1,2,0,1,2,0,2,2));

            assertTrue("Your failed when it rotate 3 time ",rotatePieceNTimes(ary,3).equals(ary3));

    }
}
