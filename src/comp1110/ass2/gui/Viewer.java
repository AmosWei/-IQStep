// useful link:  https://piazza.com/class/j57yzo1o6wb7jp?cid=739

package comp1110.ass2.gui;

import comp1110.ass2.StepsGame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.sun.glass.ui.Cursor.setVisible;

/**
 * A very simple viewer for piece placements in the steps game.
 *
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int ROW_NUM = 5;
    private static final int COL_NUM = 10;
    private static final int PIECE_IMAGE_SIZE = (int) ((3 * SQUARE_SIZE) * 1.33);
    private static final float SQUARE_OPACITY = 0.3f;
    private static final int VIEWER_WIDTH = 750;
    private static final int VIEWER_HEIGHT = 500;
    private static final int PIECE_NUM = 8;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYabcdefghijklmnopqrstuvwxy";
    private static final String URI_BASE = "assets/";

    /* node groups */
    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group pieces = new Group();
    private final Group tiles = new Group();

    TextField textField;

    private static List<Character> moveSequence = new ArrayList<>();

    //FIXME Task 4
    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {
        pieces.getChildren().clear();   //fixed by amos
        if(!(StepsGame.isPlacementWellFormed(placement))){
            throw new IllegalArgumentException("Entered placement is invalid. " +
                "Please make sure you enter 3 characters within the range A-H, A-H, (A-Y, a-y)");
         }
         for (int a = 0 ; a < placement.length(); a+=3) {   //fixed bugs by amos
             //Create the piece to place
             FXPiece chosenPiece = new FXPiece(placement.charAt(a), placement.charAt(a+1), placement.charAt(a+2));
             chosenPiece.setOrientation(placement.charAt(a+1));
             chosenPiece.setLocation(placement.charAt(a+2));
             chosenPiece.OrientPiece();
             chosenPiece.placePiece();

            /* //Remove piece if already placed on board.
             for (int i = 0; i < moveSequence.size(); i = i + 1) {
                 if (moveSequence.get(i) == placement.charAt(0)) {
                     moveSequence.remove(i);
                     pieces.getChildren().remove(i);
                     i = moveSequence.size(); //
                 }
             }
            */
             pieces.getChildren().add(chosenPiece);
             moveSequence.add(chosenPiece.getPiece());

             //Track move sequence
             for (int i = 0; i < moveSequence.size(); i = i + 1) {
                 System.out.print(moveSequence.get(i));
             }
             System.out.println();
         }
    }

////PIECES////
    public class FXPiece extends ImageView {
        char piece;
        char orientation;
        char location;

        //Creates image of piece and sets flip state
        FXPiece(char piece, char orientation, char location) {
            this.piece = piece;
            this.orientation = orientation;
            this.location = location;

            if (charToInt(orientation) <= 'A' && charToInt(orientation) <= charToInt('D')) {
                Image image = new Image(Viewer.class.getResource(URI_BASE + piece + 'A' + ".png").toString());
                setImage(image);
            } else if (charToInt(orientation) <= 'E' && charToInt(orientation) <= charToInt('H')) {
                Image image = new Image(Viewer.class.getResource(URI_BASE + piece + 'E' + ".png").toString());
                setImage(image);
            }

            setFitHeight(PIECE_IMAGE_SIZE);
            setFitWidth(PIECE_IMAGE_SIZE);
        }

        //Sets roation of piece
        public void OrientPiece() {
            switch (orientation) {
                case 'A':
                case 'E':
                    setRotate(0);
                    break;
                case 'B':
                case 'F':
                    setRotate(90);
                    break;
                case 'C':
                case 'G':
                    setRotate(180);
                    break;
                case 'D':
                case 'H':
                    setRotate(270);
                    break;
            }
        }

        //Sets location of piece
        public void placePiece() {
            Map<Character, Integer> mapX = charToCoord('x');
            Map<Character, Integer> mapY = charToCoord('y');

            setTranslateX(SQUARE_SIZE * mapX.get(location) - SQUARE_SIZE);
            setTranslateY(SQUARE_SIZE * mapY.get(location) - SQUARE_SIZE);
        }

    public void setLocation(char location) {
            this.location = location;
    }

    public void setOrientation(char orientation) {
            this.orientation = orientation;
    }

    public char getPiece() {
            return piece;
    }

    public Map<Character, Integer> charToCoord(char coord) {
            Map<Character, Integer> map = new HashMap<>();
            int k = 0;
            int i = 0;
            int j = 0;


            for (i = 0; i < ROW_NUM; i = i + 1) {
                for (j = 0; j < COL_NUM; j = j + 1) {
                    if (coord == 'x') {
                        k = j;
                    } else if (coord == 'y') {
                        k = i;
                    }
                    map.put(ALPHABET.charAt(j + COL_NUM * i), k);
                    //System.out.println(ALPHABET.charAt(j + COL_NUM * i) + " " + "(" + i + ", " + j + ")");
                }
            }
            return map;
        }

     }

    public static int charToInt(char letter) {
        return ALPHABET.indexOf(letter);
    }

    public static char intToChar(int num) {
        return ALPHABET.charAt(num);
    }

  /////BOARD!!!///

    class Tile extends ImageView {
        Tile(double x, double y) {
            Circle tileImg = new Circle(x, y, SQUARE_SIZE / 3);
            tileImg.setFill(Color.GREY);
            tileImg.setOpacity(SQUARE_OPACITY);
            tileImg.setTranslateX(SQUARE_SIZE);
            tileImg.setTranslateY(SQUARE_SIZE);
            tiles.getChildren().add(tileImg);
        }
    }

      public void makeBoard() {
            for (int j = 0; j < SQUARE_SIZE * 5; j = j + SQUARE_SIZE) {
                if (j % 120 == 0) {
                    for (int i = 0; i < SQUARE_SIZE * 10; i = i + SQUARE_SIZE) {
                        if (i % 120 == 0) {
                            tiles.getChildren().add(new Tile(i, j));
                        }
                    }
                } else if (j % 120 != 0) {
                    for (int i = 0; i < SQUARE_SIZE * 10; i = i + SQUARE_SIZE) {
                        if (i % 120 != 0) {
                            tiles.getChildren().add(new Tile(i, j));
                        }
                    }
                }
            }
        }
    /**
     * Create a basic text field for input and a refresh button.
     */

    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField ();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);

        }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("StepsGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);
        root.getChildren().add(tiles);
        root.getChildren().add(pieces);

        makeControls();
        makeBoard();

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}

