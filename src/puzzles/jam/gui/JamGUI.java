package puzzles.jam.gui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.jam.model.JamModel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * The GUI for Jam
 *
 * @author Owen Whitman
 */
public class JamGUI extends Application  implements Observer<JamModel, String>  {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private final static String X_CAR_COLOR = "#DF0101";
    private final static String[] CAR_COLORS = new String[]{"#32CD32", "#F5D033", "#2271B3", "#d3f705", "#05f7c3", "#f70572", "#9a05f7", "#763C28", "#3F888F", "#8B8C7A", "#721422", "#C6A664", "#287233", "#B44C43", "#f1e740","#fc03ec", "#924E7D", "#999950","#f5a142"};
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int ICON_SIZE = 75;

    private Button[][] grid;
    private JamModel model;
    private BorderPane borderPane;
    private String currentfile;
    private Label top;
    private Stage stage;

    /**
     * Initialize the GUI
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        this.model = new JamModel();
        model.addObserver(this);
        model.loadBoardFromFile(filename);

    }

    /**
     * @param stages the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stages) throws Exception {
        stage = stages;
        borderPane = new BorderPane();

        currentfile = model.getLastfile();
        String[] currentfilename = currentfile.split("/");
        HBox topbox = new HBox(0);
        topbox.setAlignment(Pos.CENTER);
        top = new Label("Loaded " + currentfilename[currentfilename.length-1]);
        topbox.getChildren().add(top);
        borderPane.setTop(topbox);

        this.grid = new Button[model.getNumrows()][model.getNumcols()];

        newGrid();

        HBox Bottombox = new HBox(0);
        Bottombox.setAlignment(Pos.CENTER);
        Button load = new Button("Load");
        load.setOnAction((event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/jam"));
            fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File filechosen = fileChooser.showOpenDialog(stage);
            model.loadBoardFromFile(String.valueOf(filechosen));
        }));
        Button reset = new Button("Reset");
        reset.setOnAction((event -> {
            model.loadBoardFromFile(currentfile);
        }));
        Button hint = new Button("Hint");
        hint.setOnAction((event -> {
            model.getHint();
        }));
        Bottombox.getChildren().addAll(load, reset, hint);

        borderPane.setBottom(Bottombox);


        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param jamModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(JamModel jamModel, String message) {
        if(this.grid == null){
            return;
        }

        this.model = jamModel;
        if(Objects.equals(message, JamModel.LOADED)){
            try{
            start(stage);
            } catch (Exception e) {
                newGrid();
            }
        }
        else{
        setBackground();
        setTop(message);}

    }

    /**
     * main method, executes the program
     * @param args the name of the file to open
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamGUI filename");
        } else {
            Application.launch(args);
        }
    }

    /**
     * Adds or removes colors to the tiles
     */
    public void setBackground(){
        for (int r = 0; r < model.getNumrows(); r++) {
            for (int c = 0; c < model.getNumcols(); c++) {
                char peice = model.board.getBoard(r, c);

                if(peice == '.'){
                    this.grid[r][c].setStyle(null);
                    this.grid[r][c].getStyleClass().add( "button");
                    this.grid[r][c].setText("");
                }

                else{
                    this.grid[r][c].setStyle(
                            "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-background-color: " + getColor(peice) + ";" +
                                    "-fx-font-weight: bold;");
                    this.grid[r][c].setText(String.valueOf(peice));
                }
            }
        }
    }

    /**
     * creates a new grid based off of the model
     */
    public void newGrid(){
        GridPane center = new GridPane();
        for (int r = 0; r < model.getNumrows(); r++) {
            for (int c = 0; c < model.getNumcols(); c++) {
                Button button = getButton(model.board.getBoard(r,c));
                int finalR = r;
                int finalC = c;
                button.setOnAction((event -> {
                    model.select(finalR, finalC);
                }));
                this.grid[r][c] = button;
                center.add(button, c, r);
            }
        }
        borderPane.setCenter(center);
    }

    /**
     * sets the text of the messsage on the top
     * @param message what to set the top message to
     */
    public void setTop(String message){

        String[] currentfilename;

        switch (message) {
            case JamModel.LOADED -> {
                currentfile = model.getLastfile();
                currentfilename = currentfile.split("/");
                top.setText("Loaded: " + currentfilename[currentfilename.length - 1]);
            }
            case JamModel.LOADED_FAILED -> top.setText("Failed to load: " + model.getLastfile());
            case JamModel.RESET -> top.setText("Puzzle reset!");
            case JamModel.SELECTIONOUTOFBOUNDS -> top.setText("How?");
            case JamModel.SELECTED ->
                    top.setText("Selected (" + model.getSelection()[0] + ", " + model.getSelection()[1] + ")");
            case JamModel.NOCAR ->
                    top.setText("No car at (" + model.getSelection()[0] + ", " + model.getSelection()[1] + ")");
            case JamModel.MOVED ->
                    top.setText("Moved from (" + model.getSelection()[0] + ", " + model.getSelection()[1] + ") to (" + model.getSelectedto()[0] + ", " + model.getSelectedto()[1] + ")");
            case JamModel.CANTMOVE ->
                    top.setText("Can't move from (" + model.getSelection()[0] + ", " + model.getSelection()[1] + ") to (" + model.getSelectedto()[0] + ", " + model.getSelectedto()[1] + ")");
            case JamModel.HINT -> top.setText("Next step!");
            case JamModel.NOSOLUTION -> top.setText("No solution");
            case JamModel.SOLVED -> top.setText(JamModel.SOLVED);
        }
    }

    /**
     * created a new button based off of the letter
     * @param letter the letter of the new button
     * @return the new button 
     */
    public Button getButton(char letter){
        Button button = new Button();

       if (letter != '.') {
            button.setStyle(
                    "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                            "-fx-background-color: " + getColor(letter) + ";" +
                            "-fx-font-weight: bold;");
            button.setText(String.valueOf(letter));
        }
        button.setMinSize(ICON_SIZE, ICON_SIZE);
        button.setMaxSize(ICON_SIZE, ICON_SIZE);

        return button;
    }
    public String getColor(char letter){
        if(letter == 'X') {
            return X_CAR_COLOR;
        }
        if(letter != '.'){
            int posinalaphbet = (int) letter - 65;
            return CAR_COLORS[posinalaphbet];
        }
        return null;
    }
}
