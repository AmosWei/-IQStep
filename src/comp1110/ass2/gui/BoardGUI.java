package comp1110.ass2.gui;

//import comp1110.ass2.Board;
import comp1110.ass2.Board;
import javafx.application.Application;
import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
//import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
//import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

//import static comp1110.ass2.GenerateSolutions.applyNextMovesFoldNtimes;
//import static comp1110.ass2.GenerateSolutions.updateBoardTo;
//import static comp1110.ass2.StepsGame.getViablePiecePlacements;
import static comp1110.ass2.StepsGame.getViablePiecePlacements;
import static comp1110.ass2.StepsGame.isPlacementSequenceValid;
import static comp1110.ass2.ValidStartPlacementSingle.pickStart;


public class BoardGUI extends Application {
    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int GAME_WIDTH = 933;
    private static final int GAME_HEIGHT = 700;
    private static final int PIECE_IMAGE_SIZE = (int) ((3 * SQUARE_SIZE) * 1.33);
    private static final int MARGIN_X = 60;
    private static final int MARGIN_Y = MARGIN_X;
    private static final float SQUARE_OPACITY = 0.3f;

    /* marker for unplaced masks */
    private static final String NOT_PLACED = "Z";

    /* where to find media assets */
    private static final String URI_BASE = "assets/";

    /* Loop in public domain CC 0 http://www.freesound.org/people/oceanictrancer/sounds/211684/ */
    private static final String LOOP_URI = BoardGUI.class.getResource(URI_BASE + "bgm.wav").toString();
    private AudioClip loop;

    /* game variables */
    private boolean loopPlaying = false;

    /* node groups */
    private final Group root = new Group();
    private final Group pieces = new Group();
    private final Group spieces = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group tiles = new Group();
    private final Group unDPieces = new Group();
    private final Group background1 = new Group();
    private final Group background2 = new Group();
    private final Group backButton = new Group();
    private final Group homeTitle = new Group();
    private final Group resetButton = new Group();
    private final Group playButton = new Group();
    private final Group muteButton = new Group();
    private final Group hint = new Group();
    private final Group completion = new Group();
    private final Group hintButton = new Group();

    /* the difficulty slider */
    private final Slider difficulty = new Slider();

    /* the state of the masks */
    private String[] piecestate = new String[8];//  all off screen to begin with
    static private String placement;
    static private String theSolution;

    // FIXME Task 7: Implement a basic playable Steps Game in JavaFX that only allows pieces to be placed in valid places
    // FIXME Task 8: Implement starting placements
    // FIXME Task 10: Implement hints
    // FIXME Task 11: Generate interesting starting placements


    /**
     * An inner class that represents masks used in the game.
     * Each of these is a visual representaton of an underlying mask.
     */

    //To get the images of the piece and set the size, orientation, position,
    class FXPiece extends ImageView {
        char piece;

        /**
         * Construct a particular playing piece
         * @param piece The letter representing the piece to be created.
         */
        FXPiece(char piece) {
            if (!(piece >= 'A' && piece <= 'H')) {
                throw new IllegalArgumentException("Bad piece: \"" +piece + "\"");
            }
            setImage(new Image(BoardGUI.class.getResource(URI_BASE + piece + "A.png").toString()));
            this.piece = piece;
            setFitHeight(PIECE_IMAGE_SIZE);
            setFitWidth(PIECE_IMAGE_SIZE);
        }


        FXPiece(char piece,char orientation){
            this(piece);
            if (orientation < 'A' || orientation > 'H'){
                throw new IllegalArgumentException("Bad orientation string: " + orientation);
            }
            if (orientation <= 'D'){
                setRotate((orientation-'A')*90);
            }else {
                setImage(new Image(BoardGUI.class.getResource(URI_BASE + piece + "E.png").toString()));
                setRotate((orientation-'E')*90);
            }
        }

        /**
         * Construct a particular playing piece at a particular place on the
         * board at a given orientation.
         * @param position A character describing the position and orientation of the piece
         */
        FXPiece(char piece,char orientation, char position) {
            this(piece,orientation);
            if (Character.toString(position).equals(NOT_PLACED)) {
                int homeX = (piece - 'A')%4;
                setLayoutX(homeX*SQUARE_SIZE*4);
                int homeY = (piece - 'A')/4;
                setLayoutY(homeY*470-50);
                setScaleX(0.5);
                setScaleY(0.5);
                setOpacity(0.15);
            } else {
                int y;
                int x;
                if (position < 'A' || ('Y' < position && position < 'a') || position > 'x') {
                    throw new IllegalArgumentException("Bad position string: " + position);
                }
                if (position < 'Z') {
                    y = (position - 'A') / 10;
                    x = (position - 'A') % 10;
                } else {
                    y = (position - 'A' - 7) / 10;
                    x = (position - 'A' - 7) % 10;
                }
                setLayoutX(x * SQUARE_SIZE + MARGIN_X);
                setLayoutY(y * SQUARE_SIZE + MARGIN_Y);
                piecestate[piece - 'A'] = Character.toString(piece) + Character.toString(orientation) + Character.toString(position);
            }
        }
    }


    /**
     * This class extends FXPiece with the capacity for it to be dragged and dropped,
     * and snap-to-grid.
     */
    class DraggableFXPiece extends FXPiece {
        int homeX, homeY;           // the position in the window where the mask should be when not on the board
        double mouseX, mouseY;      // the last known mouse positions (used when dragging)
        char orientation;

        /**
         * Construct a draggable piece
         * @param piece The piece identifier ('A' - 'H')
         */
        DraggableFXPiece(char piece) {
            super(piece);
            homeX = (piece - 'A')%4;
            setLayoutX(homeX*SQUARE_SIZE*4);
            homeY = (piece - 'A')/4;
            setLayoutY(homeY*470-50);
            setScaleX(0.5);
            setScaleY(0.5);
            orientation = 'A';
            /* event handlers */
            setOnScroll(event -> {            // scroll to change orientation
                hideCompletion();
                rotate();
                event.consume();
            });
            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                setScaleY(1);
                setScaleX(1);
            });
            setOnMouseDragged(event -> {      // mouse is being dragged
                hideCompletion();
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });
            setOnMouseReleased(event -> {     // drag is complete
                snapToGrid();
            });
        }


//


        /**
         * Snap the mask to the nearest grid position (if it is over the grid)
         */
        private void snapToGrid() {
            if (onBoard()) {
                setLayoutX(Math.round((float)(getLayoutX() - MARGIN_X)/SQUARE_SIZE)*SQUARE_SIZE + MARGIN_X);
                setLayoutY(Math.round((float)(getLayoutY() - MARGIN_Y)/SQUARE_SIZE)*SQUARE_SIZE + MARGIN_Y);
                setPosition();
            }

            else {
                snapToHome();
            }
        }

        /**
         * @return true if the mask is on the board
         */
        private boolean onBoard() {
            char position;
            int x = Math.round((float)(getLayoutX() - MARGIN_X)/SQUARE_SIZE);
            int y = Math.round((float)(getLayoutY() - MARGIN_Y)/SQUARE_SIZE);


            if (y*10+x>=0 && y*10+x <= 24){
                position = (char) (y*10+x +'A');
            }else{
                position = (char) (y*10+x +'a' -25);
            }

            String name = placement;

            for (int i = 0 ; i < name.length() ; i += 3 ){
                if (name.charAt(i)==piece){
                    name = name.substring(0,i) + name.substring(i+3,name.length());
                    break;
                }
            }
            String newPiece = Character.toString(piece) + Character.toString(orientation) + Character.toString(position);

            return isPlacementSequenceValid(name+newPiece);
        }


        /**
         * Snap the mask to its home position (if it is not on the grid)
         */
        private void snapToHome() {
            setImage(new Image(BoardGUI.class.getResource(URI_BASE + piece + "A.png").toString()));
            setLayoutX(homeX*SQUARE_SIZE*4);
            setLayoutY(homeY*470 - 50);
            setRotate(0);
            setScaleX(0.5);
            setScaleY(0.5);
            orientation = 'A';
            piecestate[piece - 'A'] = Character.toString(piece) + Character.toString(orientation) + NOT_PLACED;
            for (int i = 0 ; i < placement.length() ; i += 3 ){
                if (placement.charAt(i) == piece){
                    placement = placement.substring(0,i) + placement.substring(i+3,placement.length());
                    break;
                }
            }
        }


        /**
         * Rotate the mask by 90 degrees (unless this is mask zero and there is a constraint on mask zero)
         */
        private void rotate() {
            if (canRotate()) {
                if (piecestate[piece-'A'].charAt(2) == 'Z') {
                    if (orientation =='D'){
                        setImage(new Image(BoardGUI.class.getResource(URI_BASE + piece + "E.png").toString()));
                        setRotate(0);
                        orientation = 'E';
                        piecestate[piece-'A'] = Character.toString(piece) + Character.toString(orientation) + NOT_PLACED;
                    }else if (orientation == 'H'){
                        setImage(new Image(BoardGUI.class.getResource(URI_BASE + piece + "A.png").toString()));
                        setRotate(0);
                        orientation = 'A';
                        piecestate[piece - 'A'] = Character.toString(piece) + Character.toString(orientation) + NOT_PLACED;
                    }else {
                        setRotate((getRotate() + 90) % 360);
                        orientation = (char)(orientation+1);
                        piecestate[piece-'A'] = Character.toString(piece) + Character.toString(orientation) + NOT_PLACED;
                    }

                }
                else {
                    for (int i = 0 ; i < placement.length() ; i += 3 ){
                        if (placement.charAt(i) == piece){
                            placement = placement.substring(0,i) + placement.substring(i+3,placement.length());
                            break;
                        }
                    }
                    char position;
                    int x = Math.round((float)(getLayoutX() - MARGIN_X)/SQUARE_SIZE);
                    int y = Math.round((float)(getLayoutY() - MARGIN_Y)/SQUARE_SIZE);
                    if (y*10+x>=0 && y*10+x <= 24){
                        position = (char) (y*10+x +'A');
                    }else{
                        position = (char) (y*10+x +'a' -25);
                    }
                    char o = piecestate[piece-'A'].charAt(1);
                    char oo = (char) ((o - 'A') / 4 * 4 + 'A');
                    String newPiece1 = Character.toString(piece) + Character.toString((char)((orientation+1-oo)%4+oo)) + Character.toString(position);
                    String newPiece2 = Character.toString(piece) + Character.toString((char)((orientation+2-oo)%4+oo)) + Character.toString(position);
                    String newPiece3 = Character.toString(piece) + Character.toString((char)((orientation+3-oo)%4+oo)) + Character.toString(position);
                    if (isPlacementSequenceValid(placement+newPiece1)) {
                        setRotate((getRotate() + 90) % 360);
                        piecestate[piece-'A'] = newPiece1;
                        orientation = (char)((orientation+1-oo)%4+oo);
                    } else if (isPlacementSequenceValid(placement+newPiece2)){
                        setRotate((getRotate() + 180) % 360);
                        piecestate[piece-'A'] = newPiece2;
                        orientation = (char)((orientation+2-oo)%4+oo);
                    }else {
                        setRotate((getRotate() + 270) % 360);
                        piecestate[piece-'A'] = newPiece3;
                        orientation = (char)((orientation+3-oo)%4+oo);
                    }
                    placement = placement + piecestate[piece-'A'];
                }
            }
        }

        /**
         * @return true if this mask can be rotated
         */
        private boolean canRotate() {
            if (piecestate[piece-'A'].charAt(2) == 'Z')
                return true;
            String name = placement;
            for (int i = 0 ; i < name.length() ; i += 3 ){
                if (name.charAt(i)==piece){
                    name = name.substring(0,i) + name.substring(i+3,name.length());
                    break;
                }
            }
            char position;
            int x = Math.round((float)(getLayoutX() - MARGIN_X)/SQUARE_SIZE);
            int y = Math.round((float)(getLayoutY() - MARGIN_Y)/SQUARE_SIZE);
            if (y*10+x>=0 && y*10+x <= 24){
                position = (char) (y*10+x +'A');
            }else{
                position = (char) (y*10+x +'a' -25);
            }
            char o = piecestate[piece-'A'].charAt(1);
            char oo = (char) ((o - 'A') / 4 * 4 + 'A');  //ABCD -> A, EFGH -> E
            String newPiece1 = Character.toString(piece) + Character.toString((char)((orientation+1-oo)%4+oo)) + Character.toString(position);
            String newPiece2 = Character.toString(piece) + Character.toString((char)((orientation+2-oo)%4+oo)) + Character.toString(position);
            String newPiece3 = Character.toString(piece) + Character.toString((char)((orientation+3-oo)%4+oo)) + Character.toString(position);
            return isPlacementSequenceValid(name+newPiece1) || isPlacementSequenceValid(name+newPiece2) || isPlacementSequenceValid(name+newPiece3);
        }


        /**
         * Determine the grid-position of the origin of the mask (0 .. 15)
         * or -1 if it is off the grid, taking into account its rotation.
         */
        private void setPosition() {
            char position;
            int x = Math.round((float)(getLayoutX() - MARGIN_X)/SQUARE_SIZE);
            int y = Math.round((float)(getLayoutY() - MARGIN_Y)/SQUARE_SIZE);
            if (y*10+x>=0 && y*10+x <= 24){
                position = (char) (y*10+x +'A');
            }else{
                position = (char) (y*10+x +'a' -25);
            }
            piecestate [piece - 'A'] = Character.toString(piece) + Character.toString(orientation) + Character.toString(position);
            for (int i = 0 ; i < placement.length() ; i += 3 ){
                if (placement.charAt(i) == piece){
                    placement = placement.substring(0,i) + placement.substring(i+3,placement.length());
                    break;
                }
            }
            placement = placement + piecestate [piece - 'A'];
            showCompletion();
        }


        /** @return the mask placement represented as a string */
        public String toString() {
            return piecestate[ piece - 'A'];
        }
    }

    /**
     * Set up event handlers for the main game
     *
     * @param scene  The Scene used by the game.
     */
    private void setUpHandlers(Scene scene) {
        /* create handlers for key press and release events */
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M) {
                toggleSoundLoop();
                if (loopPlaying) hidePlayMuteButton(false);
                else hidePlayMuteButton(true);
                event.consume();
            } else if (event.getCode() == KeyCode.Q) {
                Platform.exit();
                event.consume();
            } else if (event.getCode() == KeyCode.SLASH) {
                showHint();
                event.consume();
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SLASH) {
                hideHint();
                event.consume();
            }
        });
    }


    /**
     * Set up the sound loop (to play when the 'M' key is pressed)
     */
    private void setUpSoundLoop() {
        try {
            loop = new AudioClip(LOOP_URI);
            loop.setCycleCount(1238570953);
        } catch (Exception e) {
            System.err.println(":-( something bad happened ("+LOOP_URI+"): "+e);
        }
    }


    /**
     * Turn the sound loop on or off
     */
    private void toggleSoundLoop() {
        if (loopPlaying)
            loop.stop();
        else
            loop.play();
        loopPlaying = !loopPlaying;
    }

    /**
     * Turn the sound loop on
     */
    private void playSound() {
        if (!loopPlaying) {
            loop.play();
            loopPlaying = true;
        }
    }

    /**
     * Turn the sound loop off
     */
    private void stopSound() {
        if (loopPlaying) {
            loop.stop();
            loopPlaying = false;
        }
    }

    /**
     * Put all of the masks back in their home position
     */
    private void resetPieces() {
        pieces.toFront();
        for (Node n: pieces.getChildren()) {
            ((DraggableFXPiece) n).snapToHome();
        }
    }


    /**
     * Create the controls that allow the game to be restarted and the difficulty
     * level set.
     */
    private void makeControls() {
        ImageView button = new ImageView();
        button.setImage(new Image(BoardGUI.class.getResource(URI_BASE + "startbutton.png").toString()));
        button.setLayoutX(GAME_WIDTH/2-230);
        button.setLayoutY(GAME_HEIGHT-150);
        button.setScaleX(0.3);
        button.setScaleY(0.3);
        button.setOnMouseClicked(event -> newGame());
        controls.getChildren().add(button);

//        difficulty.setMin(0);
//        difficulty.setMax(10);
//        difficulty.setValue(0);
//        difficulty.setShowTickLabels(true);
//        difficulty.setShowTickMarks(true);
//        difficulty.setMajorTickUnit(5);
//        difficulty.setMinorTickCount(1);
//        difficulty.setSnapToTicks(true);
//
//        difficulty.setLayoutX(GAME_WIDTH/2 - 80);
//        difficulty.setLayoutY(GAME_HEIGHT - 40);
//        controls.getChildren().add(difficulty);
//
//        final Label difficultyCaption = new Label("Difficulty:");
//        difficultyCaption.setTextFill(Color.GREY);
//        difficultyCaption.setLayoutX(GAME_WIDTH/2 - 150);
//        difficultyCaption.setLayoutY(GAME_HEIGHT - 40);
//        controls.getChildren().add(difficultyCaption);
    }


    /**
     * Create the message to be displayed when the player completes the puzzle.
     */
    private void makeCompletion() {
        ImageView gif = new ImageView();
        gif.setImage(new Image(BoardGUI.class.getResource(URI_BASE + "welldone.gif").toString()));
        gif.setScaleX(1);
        gif.setScaleY(1);
        gif.setLayoutX(240);
        gif.setLayoutY(160);
        completion.getChildren().add(gif);

        ImageView wellDone = new ImageView();
        wellDone.setImage(new Image(BoardGUI.class.getResource(URI_BASE + "welldone2.png").toString()));
        wellDone.setScaleX(0.6);
        wellDone.setScaleY(0.6);
        wellDone.setLayoutX(290);
        wellDone.setLayoutY(300);
        completion.getChildren().add(wellDone);
    }


    /**
     * Show the completion message
     */
    private void showCompletion() {
        if (placement.length() == 24) {
            completion.toFront();
            completion.setOpacity(1);
            pieces.setOpacity(0.3);
            spieces.setOpacity(0.3);
        }
    }


    /**
     * Hide the completion message
     */
    private void hideCompletion() {
        completion.toBack();
        completion.setOpacity(0);
    }

    class Tile extends ImageView {
        Tile(double x, double y) {
            Circle tileImg = new Circle(PIECE_IMAGE_SIZE-SQUARE_SIZE+x, PIECE_IMAGE_SIZE-SQUARE_SIZE+y, SQUARE_SIZE / 3);
            tileImg.setFill(Color.GREY);
            tileImg.setOpacity(SQUARE_OPACITY);
            tiles.getChildren().add(tileImg);
        }
    }

    private void makeBoard() {
        tiles.getChildren().clear();
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

    private void makeDraggablePieces() {
        pieces.getChildren().clear();
        spieces.getChildren().clear();
       /* Random gen = new Random();
        index = gen.nextInt(15);
        placement = pickStart(index);*/
        System.out.println(placement);
        HashSet<Character> startingPieces = new HashSet<>();
        for (int i = 0; i < placement.length(); i+=3) {
            spieces.getChildren().add(new FXPiece(placement.charAt(i), placement.charAt(i+1), placement.charAt(i+2)));
            startingPieces.add(placement.charAt(i));
            System.out.println(placement.substring(i,i+3));
            piecestate[placement.charAt(i)-'A'] = placement.substring(i,i+3);
        }
        for (int i = 'A'; i <= 'H'; i++) {
            if (!startingPieces.contains((char)i)) {
                DraggableFXPiece piece = new DraggableFXPiece((char) i);
                pieces.getChildren().add(piece);
                piecestate[i-'A'] = Character.toString((char)i) + "AZ";

            }
        }
        pieces.toFront();
    }

    private void makeUndraggablePieces() {
        unDPieces.getChildren().clear();
        for (char p = 'A'; p <= 'H'; p++)
            unDPieces.getChildren().add(new FXPiece(p, 'A', 'Z'));
    }

    private static void generateStartingPlacement() {
        Random gen = new Random();
        int index = gen.nextInt(10);
        placement = pickStart(index)[0];
        theSolution = pickStart(index)[1];
    }

    private void makeBackground(int n) {
        ImageView back = new ImageView();
        back.setImage(new Image(BoardGUI.class.getResource(URI_BASE + "back" + Integer.toString(n) + ".png").toString()));
        back.toBack();
        if (n==2) {
            background2.getChildren().add(back);
            background2.setScaleX(1.2);
            background2.setScaleY(1.2);
            background2.setLayoutX(50);
            background2.setLayoutY(50);
        }
        else {
            background1.getChildren().add(back);
            background1.setScaleX(1.8);
            background1.setScaleY(1.8);
            background1.setLayoutX(145);
            background1.setLayoutY(150);
        }
    }

    private void hideBackground(int n, boolean hide) {
        if (n == 2) {
            if (hide)
                background2.setOpacity(0);
            else
                background2.setOpacity(0.2);
        } else {
            if (hide)
                background1.setOpacity(0);
            else
                background1.setOpacity(0.3);
        }
    }

    private void makeTitle() {
        ImageView title = new ImageView();
        title.setImage(new Image(BoardGUI.class.getResource(URI_BASE + "name.png").toString()));
        title.setLayoutX(GAME_WIDTH/2 - 350);
        title.setLayoutY(GAME_HEIGHT/2 - 150);
        homeTitle.getChildren().add(title);
    }

    private void hideTitle() {
        homeTitle.setOpacity(0);
    }

    private void showTitle() {
        homeTitle.setOpacity(1);
    }

    private void makeResetButton() {
        ImageView button = new ImageView();
        button.setImage(new Image(BoardGUI.class.getResource(URI_BASE + "reset.png").toString()));
        button.setLayoutX(GAME_WIDTH-180);
        button.setLayoutY(230);
        button.setScaleX(0.3);
        button.setScaleY(0.3);
        button.setOnMouseClicked(event -> newGame());
        resetButton.getChildren().add(button);
    }

    private void showResetButton() {
        resetButton.setOpacity(1);
    }

    private void hideResetButton() {
        resetButton.setOpacity(0);
    }

    private void makeBackButton() {
        ImageView back = new ImageView();
        back.setImage(new Image(BoardGUI.class.getResource(URI_BASE + "backbutton.png").toString()));
        back.setScaleX(0.2);
        back.setScaleY(0.2);
        back.setLayoutX(-120);
        back.setLayoutY(200);
        back.toFront();
        back.setOnMouseClicked(event -> {
            hideBackground(2,true);
            hideBackground(1,false);
            backButton.getChildren().clear();
            board.getChildren().clear();
            pieces.getChildren().clear();
            spieces.getChildren().clear();
            unDPieces.getChildren().clear();
            tiles.getChildren().clear();
            showTitle();
            loop.stop();
            loopPlaying = false;
            makeControls();
            hideResetButton();
            hideCompletion();
            hideHintButton(true);
        });
        backButton.getChildren().add(back);
    }

    private void makePlayButton() {
        playButton.getChildren().clear();
        ImageView play = new ImageView();
        play.setImage(new Image(BoardGUI.class.getResource(URI_BASE + "play.png").toString()));
        play.setOnMouseClicked(event -> {
            stopSound();
            hidePlayMuteButton(true);
        });
        play.toFront();
        play.setLayoutX(-30);
        play.setLayoutY(GAME_HEIGHT-150);
        play.setScaleX(0.2);
        play.setScaleY(0.2);
        playButton.getChildren().add(play);
    }

    private void makeMuteButton() {
        muteButton.getChildren().clear();
        ImageView mute = new ImageView();
        mute.setImage(new Image(BoardGUI.class.getResource(URI_BASE + "mute.png").toString()));
        mute.setOnMouseClicked(event -> {
            playSound();
            hidePlayMuteButton(false);
        });
        mute.toFront();
        mute.setLayoutX(-30);
        mute.setLayoutY(GAME_HEIGHT-150);
        mute.setScaleX(0.2);
        mute.setScaleY(0.2);
        muteButton.getChildren().add(mute);
    }

    private void hidePlayMuteButton(boolean hidePlay) {
        if (hidePlay) {
            playButton.getChildren().clear();
            makeMuteButton();
        } else {
            muteButton.getChildren().clear();
            makePlayButton();
        }
    }

    private void hintButton(){
        ImageView hint = new ImageView();
        hint.setImage(new Image(BoardGUI.class.getResource(URI_BASE + "hint.png").toString()));
        hint.setOnMousePressed(event -> showHint());
        hint.setOnMouseReleased(event -> hideHint());
        hint.toFront();
        hint.setLayoutX(80);
        hint.setLayoutY(GAME_HEIGHT-150);
        hint.setScaleX(0.25);
        hint.setScaleY(0.25);
        hintButton.getChildren().add(hint);
    }

    private void hideHintButton(boolean hide) {
        if (hide)
            hintButton.getChildren().clear();
        else
            hintButton();
    }

    private void showHint() {
        hint.getChildren().clear();
        Set<String> next = getViablePiecePlacements(placement, theSolution);
        if (next.size() == 0) {
            FXPiece piece = new FXPiece(placement.charAt(placement.length()-3), placement.charAt(placement.length()-2), placement.charAt(placement.length()-1));
            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-1.0);
            piece.setEffect(blackout);
            hint.getChildren().add(piece);
            pieces.setOpacity(0);
        } else {
            String nextPiece = "";
            for (String piece: next) {
                nextPiece = piece;
                break;
            }
            pieces.setOpacity(0);
            FXPiece piece = new FXPiece(nextPiece.charAt(0), nextPiece.charAt(1), 'Z');
            piece.setOpacity(1);
            hint.getChildren().add(piece);
        }
    }

    private void hideHint() {
        pieces.setOpacity(1);
        hint.getChildren().clear();
    }

    /**
     * Start a new game, resetting everything as necessary
     */
    private void newGame() {
        try {
            playSound();
            generateStartingPlacement();
            makeBoard();
            hideCompletion();
            makeUndraggablePieces();
            makeDraggablePieces();
            hideBackground(2,false);
            hideBackground(1,true);
            hideTitle();
            makeBackButton();
            showCompletion();
            controls.getChildren().clear();
            showResetButton();
            hidePlayMuteButton(false);
            pieces.setOpacity(1);
            spieces.setOpacity(1);
            hideHintButton(false);
        } catch (IllegalArgumentException e) {
            System.err.println("Uh oh. "+ e);
            e.printStackTrace();
            Platform.exit();
        }
        resetPieces();
    }


    /**
     * The entry point for JavaFX.  This method gets called when JavaFX starts
     * The key setup is all done by this method.
     *
     * @param primaryStage The stage (window) in which the game occurs.
     * @throws Exception exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BoardGUI");
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);
        root.getChildren().add(background1);
        root.getChildren().add(background2);
        root.getChildren().add(unDPieces);
        root.getChildren().add(pieces);
        root.getChildren().add(spieces);
        root.getChildren().add(controls);
        root.getChildren().add(tiles);
        root.getChildren().add(backButton);
        root.getChildren().add(homeTitle);
        root.getChildren().add(resetButton);
        root.getChildren().add(playButton);
        root.getChildren().add(muteButton);
        root.getChildren().add(hint);
        root.getChildren().add(completion);
        root.getChildren().add(hintButton);

        setUpHandlers(scene);
        setUpSoundLoop();
        makeControls();
        makeCompletion();
        hideCompletion();
        makeBackground(1);
        makeBackground(2);
        hideBackground(2,true);
        hideBackground(1,false);
        makeTitle();
        makeResetButton();
        hideResetButton();
        hintButton();
        hideHintButton(true);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

