package puzzles.tilt.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private Image greendot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));
    private final Image bluedot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"));
    private final Image blocker = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"));
    private final Image hole = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"));

    private TiltModel model;
    private Stage stage;
    private Button up;
    private Button down;
    private Button left;
    private Button right;
    private GridPane board;
    private Label message;

    /**
     * Loads board from file
     */
    public void loadFromFile(){
        FileChooser chooserFile = new FileChooser();
        chooserFile.setTitle("Load game board");
        chooserFile.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/tilt"));
        chooserFile.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*txt"));
        try {
            model.loadBoardFromFile(chooserFile.showOpenDialog(stage),"File Loaded");
        }catch(NullPointerException e){
            throw e;
            //message.setText("Message: Load File Failure");
        }
    }

    /**
     * Initializes model and adds observer
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        model = new TiltModel(filename);
        model.addObserver(this);
    }

    /**
     * Starts the gui
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        BorderPane outerGui = new BorderPane();
        board = new GridPane();
        outerGui.setCenter(board);

        VBox topSide = new VBox();
        message = new Label("Game Loaded");
        message.setFont(new Font(20));

        up = new Button("^");
        up.setAlignment(Pos.TOP_CENTER);
        up.setOnAction(event -> model.goUp());
        up.setPrefSize(1000,50);
        up.setFont(new Font(20));
        topSide.getChildren().addAll(message,up);
        topSide.setAlignment(Pos.CENTER);
        outerGui.setTop(topSide);

        VBox buts = new VBox();
        Button load = new Button("Load");
        load.setOnAction(event -> loadFromFile());
        load.setFont(new Font(20));
        Button reset = new Button("Reset");
        reset.setOnAction(event -> model.reset());
        reset.setFont(new Font(20));
        Button hint = new Button("Hint");
        hint.setOnAction(event -> model.getHint());
        hint.setFont(new Font(20));
        buts.setAlignment(Pos.CENTER);
        buts.getChildren().addAll(load, reset, hint);

        HBox rightSide = new HBox();
        right = new Button(">");
        right.setAlignment(Pos.CENTER);
        right.setOnAction(event -> model.goRight());
        right.setPrefSize(50,500);
        right.setFont(new Font(20));
        rightSide.getChildren().addAll(right);
        outerGui.setRight(rightSide);

        FlowPane leftSide = new FlowPane();
        left = new Button("<");
        left.setAlignment(Pos.CENTER);
        left.setOnAction(event -> model.goLeft());
        left.setPrefSize(50,500);
        left.setFont(new Font(20));
        leftSide.getChildren().addAll(left, buts);
        outerGui.setLeft(leftSide);

        VBox downSide = new VBox();
        down = new Button("v");
        down.setAlignment(Pos.CENTER);
        down.setOnAction(event -> model.goDown());
        down.setPrefSize(1000,50);
        down.setFont(new Font(20));
        downSide.getChildren().addAll(down);
        downSide.setAlignment(Pos.CENTER);
        outerGui.setBottom(downSide);

        board.setAlignment(Pos.valueOf("CENTER"));
        outerGui.setCenter(board);

        this.update(model,"");
        Scene scene = new Scene(outerGui);
        stage.setScene(scene);
        stage.setHeight(850);
        stage.setWidth(1500);
        stage.show();
    }

    /**
     * Updates the gui based on the input from the controller
     * @param tiltModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel tiltModel, String message) {

        up.setStyle("-fx-background-color: #dddddd; ");
        down.setStyle("-fx-background-color: #dddddd; ");
        left.setStyle("-fx-background-color: #dddddd; ");
        right.setStyle("-fx-background-color: #dddddd; ");
        switch (message) {
            case "Hint: N" -> up.setStyle("-fx-background-color: #ffff00; ");
            case "Hint: S" -> down.setStyle("-fx-background-color: #ffff00; ");
            case "Hint: E" -> right.setStyle("-fx-background-color: #ffff00; ");
            case "Hint: W" -> left.setStyle("-fx-background-color: #ffff00; ");
        }
        this.message.setText(" Moves: "+ model.getMoves()+ " "+ message);

        board.getChildren().clear();
        for (int row = 0; row < model.getDimension(); row++) {
            for (int col = 0; col < model.getDimension(); col++) {
                Button spot = new Button();
                spot.setPrefSize(80, 80);
                spot.setMaxSize(80, 80);

                if (model.getBoard(row, col) == 'G') {
                    ImageView green = new ImageView(greendot);
                    green.setFitHeight(70);
                    green.setFitWidth(70);
                    spot.setGraphic(green);
                } else if (model.getBoard(row, col) == 'B') {
                    ImageView blue = new ImageView(bluedot);
                    blue.setFitHeight(70);
                    blue.setFitWidth(70);
                    spot.setGraphic(blue);
                } else if (model.getBoard(row, col) == '*') {
                    ImageView gray = new ImageView(blocker);
                    gray.setFitHeight(70);
                    gray.setFitWidth(70);
                    spot.setGraphic(gray);
                } else if (model.getBoard(row, col) == 'O') {
                    ImageView drop = new ImageView(hole);
                    drop.setFitHeight(70);
                    drop.setFitWidth(70);
                    spot.setGraphic(drop);
                }board.add(spot, col, row);
            }
        }
    }

    /**
     * Main for the gui, parses the input
     * @param args input
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
        } else {Application.launch(args);}
    }
}
