//Created by Wangchao Wei 9/24
package comp1110.ass2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import static comp1110.ass2.Piece.flipPiece;

import static org.junit.Assert.assertTrue;

public class CheckFlipTest {
        @Test
    public void CheckFlipTest1(){
        ArrayList<Integer> ary = new ArrayList<>();
        ary.addAll(Arrays.asList(0,0,0,2,1,1,2,2,2));
            ArrayList<Integer> ary1 = new ArrayList<>();
            ary1.addAll(Arrays.asList(0,0,0,2,2,1,1,1,1));
        assertTrue(flipPiece(ary).equals(ary1));
    }
}
