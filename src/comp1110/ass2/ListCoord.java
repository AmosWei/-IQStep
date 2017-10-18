package comp1110.ass2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static comp1110.ass2.Board.BOARD_NUM_ROWS;
import static comp1110.ass2.Board.BOARD_NUM_COLS;

/**
 * Created by Daniel on 30/08/2017.
 * All code in ListCoord class is by Daniel Lai (u6352900).
 */
public class ListCoord {
    public static final List<Coord> LIST_OF_COORDS = ListCoord.setListCoord();
    public static final Map<Coord, Character> COORD_TO_LETTER_MAP = CoordToChar();
    public static final Map<Character, Coord> LETTER_TO_COORD_MAP = CharToCoord();

    public static final String LETTERS_ENCODING_TILES = "ABCDEFGHIJKLMNOPQRSTUVWXYabcdefghijklmnopqrstuvwxy";


    public ListCoord() {
    }

    public static List<Coord> setListCoord() {
        List<Coord> listOfCoords = new ArrayList<>();
        for (int i = -1; i < 6; i = i + 1) {
            for (int j = -1; j < 11; j = j + 1) {
                Coord c = new Coord(i, j);
                listOfCoords.add(new Coord(i, j));
            }
        }
        return listOfCoords;
    }

    public static Map<Coord, Character> CoordToChar() {
        Map<Coord, Character> map = new HashMap<>();
        for (int s = 0; s < BOARD_NUM_ROWS; s = s + 1) {
            for (int r = 0; r < BOARD_NUM_COLS; r = r + 1) {
                map.put(LIST_OF_COORDS.get(1 + r + 12 + s * 12), LETTERS_ENCODING_TILES.charAt(r + s * BOARD_NUM_COLS));
            }
        }
        for (int r = 0; r < 12; r = r + 1) {
            map.put(LIST_OF_COORDS.get(r), '.');
        }
        for (int r = LIST_OF_COORDS.size() - 12; r < LIST_OF_COORDS.size(); r = r + 1) {
            map.put(LIST_OF_COORDS.get(r), '.');
        }

        for (int s = 1; s < 6; s = s + 1) {
            for (int r = 0; r < 2; r = r + 1) {
                map.put(LIST_OF_COORDS.get(r * 11 + s * 12), '.');
            }
        }

        return map;
    }


    public static Map<Character, Coord> CharToCoord() {
        Map<Character, Coord> map = new HashMap<>();
        for (int s = 0; s < BOARD_NUM_ROWS; s = s + 1) {
            for (int r = 0; r < BOARD_NUM_COLS; r = r + 1) {
                map.put(LETTERS_ENCODING_TILES.charAt(r + s * BOARD_NUM_COLS), LIST_OF_COORDS.get(1 + (r + 12 + s * 12)));
            }
        }
        return map;
    }

    public static void main(String[] args) {
        for (int i = 0; i < LIST_OF_COORDS.size(); i = i + 1)
            System.out.println("Char: (" + CoordToChar().get(LIST_OF_COORDS.get(i)) + ") , Coord: " + (LIST_OF_COORDS.get(i).toString()));
    }

    /**
     * Created by Daniel on 30/09/2017.
     */
    public static class Coord {
        private int x;
        private int y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            String s = "( " + Integer.toString(getX()) + ", " + Integer.toString(getY()) + ")";
            return s;
        }

    }
}

