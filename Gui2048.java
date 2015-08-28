/* Name: Eric Chen
 * cslogin: cs8bakk
 * Date: 3/5/15
 * File: Gui2048.java
 */

/** Gui2048.java */
/** PA8 Release */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

//This class will set up the gameplay for the game 2048, using the Board
//class that I had created in PA4.
public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(238, 228, 228);
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(240,250, 240, 0.5); //238, 228 218

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); // For tiles >= 8
    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); // For tiles < 8

    /** Add your own Instance Variables here */
    //Initialize instance variables, so we can access them throughout the class
    //Use constants as variables instead of magic numbers
    private final int padding_size = 10;
    private final int rect_size = 100;
    private GridPane myPane = new GridPane();
    private BorderPane border = new BorderPane();
    private Label score; 
    
    //This method start will set up the GUI, using a BorderPane and a GridPane
    @Override
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));

        /** Add your Code for the GUI Here */
        //initialize variables that will be referenced throughout the method 
        int gridSize = board.GRID_SIZE;
        int [][] grid = board.getGrid();
        Text number = new Text(); 

        //Title to fit into top of border pane. Set Font to Bold Comic Sans
        Label title = new Label("  2048");
        title.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
        FontPosture.ITALIC, 40));
        
        //Stackpane that will contain score and title.
        //As a label, place in score in top left
        StackPane scoreT = new StackPane(); 
        score = new Label(" SCORE: " + 0);
        score.setFont(Font.font("Times New Roman", 20));
        scoreT.setAlignment(score, Pos.TOP_LEFT);
        scoreT.setMargin(score, new Insets(3,10,10,3));

        //Add Label titles and score to stackpane, after setting margins 
        scoreT.getChildren().addAll(title, score);
        
        //put stackPane ScoreT on the top, and in the middle put GridPane
        border.setTop(scoreT);
        border.setCenter(myPane);
        border.setStyle("-fx-background-color:rgb(187,173,160)"); 
        
        //Set padding_size, alignment, and vertical gaps accordingly
        myPane.setAlignment(Pos.CENTER);
        myPane.setPadding(new Insets(padding_size, padding_size, 
        padding_size, padding_size));
        myPane.setHgap(10);
        myPane.setVgap(10);
        

        //Place tiles inside the GridPane with StackPanes
        updateBoard();
        
        //set the scene to the whole borderPane
        Scene myScene = new Scene (border);
        //creates a new KeyHandler object
        myScene.setOnKeyPressed(new KeyHandler ());
        
        //Ultimately sets the scene to the stage, and shows
        primaryStage.setTitle("2048 Game");
        primaryStage.setScene(myScene);
        primaryStage.show();
    }

    /** Add your own Instance Methods Here */
    
    //When game is over, create a transparent overlay that goes over the game.
    //The game should not be playable anymore. Method is called when gameIsOVer
    //returns true
    private void gameOver() {
      
      //Create new StackPane, and add a Rectangle and Text to it. 
      StackPane overStack = new StackPane();
      Rectangle gameOver = new Rectangle (450, 450);
      Text gameOverText = new Text ("Game Over!");
        
      //Set Rectangle Color to an opaque color, and align text in middle
      gameOver.setFill(COLOR_GAME_OVER);
      gameOverText.setFont(Font.font("Impact", 40));
      overStack.getChildren().addAll(gameOver, gameOverText);

      //Add all these items to a final stackPane, and set this as the center
      //of the borderPane
      StackPane finalGrid = new StackPane();
      finalGrid.getChildren().addAll(myPane, overStack);
      border.setCenter(finalGrid);

    }
   
    //Every time tiles are moved, update score 
    //do this by resetting score text
    private void updateScore() {
      score.setText("SCORE: " + board.getScore());
    }
    
    //updateBoard method: everytime called it will re-initialize gridPane 
    //of StackPane tiles.
    private void updateBoard () {
      //First check if game is over. If true, run gameOver.
      if (board.isGameOver()) {
         gameOver(); 
      }
      
      //intialize variables using Board's getter methods
      int gridSize = board.GRID_SIZE;
      int [][] grid = board.getGrid();
      int score = board.getScore();
      Text number = new Text(" ");
       
      //Loop through columns and rows of grid
      for (int i = 0; i < gridSize; i++) {
         for (int j = 0; j < gridSize; j++) {
            //Create new StackPanes, which will contain a Rectangle and Text
            //This will make the Tiles. Text will be the number at grid[i][j]
            StackPane stack = new StackPane();
            Rectangle r = new Rectangle (rect_size, rect_size);
            //Set curved arcs of the rectangle
            r.setArcWidth(10);
            r.setArcHeight(10);
             
            //If grid[i][j] is 0, number will be empty. Set a different color
            if (grid [i][j] == 0) {
              number = new Text (" ");
              r.setFill(COLOR_EMPTY);
            } else {
              number = new Text (" " + grid[i][j]);
              fillColor(grid[i][j], r);
            }
            
            //If the number is less than 8, fill text with dark color
            //else, text will be white.
            if (grid[i][j] < 8) {
              number.setFill(COLOR_VALUE_DARK);
            } else if (grid[i][j] >= 8) {
              number.setFill(COLOR_VALUE_LIGHT);
            }
            
            //If the number is 4 digits, set to smaller font, preserving
            //the margins of the whole grid.
            if (grid[i][j] > 999) { 
              number.setFont(Font.font("Times New Roman", FontWeight.BOLD,
              24));
            }
            
            //else, set size to 36
            else { 
              number.setFont(Font.font("Times New Roman", FontWeight.BOLD,
              36));
            }
            
            //Add the rectangles and numbers to the stack, then add to GridPane
            stack.setMargin(number, new Insets(10,10,10,0));

            stack.getChildren().addAll(r, number);
            myPane.add(stack, j, i );
          }
       }
    
    }
   
    //This method, when called upon, will fill in the passed rectangles
    //with the supposedly color
    private static void fillColor(int x, Rectangle r) {
      if (x == 2) {
        r.setFill(COLOR_2);
      } else if (x == 4) {
        r.setFill(COLOR_4);
      } else if (x == 8) {
        r.setFill(COLOR_8);
      } else if (x == 16) {
        r.setFill(COLOR_16);
      } else if (x == 32) {
        r.setFill(COLOR_32);
      } else if (x == 64) {
        r.setFill(COLOR_64);
      } else if (x == 128) {
        r.setFill(COLOR_128);
      } else if (x == 256) {
        r.setFill(COLOR_256); 
      } else if (x == 512) {
        r.setFill(COLOR_512);
      } else if (x == 1024) {
        r.setFill(COLOR_1024);
      } else if (x == 2048) {
        r.setFill(COLOR_2048);
      } else {
        r.setFill(COLOR_OTHER);
      }
  
  }
   
    //KeyHandler class to handle KeyEvent inputs from the user
    private class KeyHandler implements EventHandler <KeyEvent> {
        //override method handle  
        public void handle (KeyEvent e) {
          //getCode to get the KeyEvent entry from the user
          KeyCode move = e.getCode();
          
          //If s is entered, save board to outputBoard
          if (move == KeyCode.S) {
            System.out.println("Saving Board to " + outputBoard);
            //Must use try/catch statement for IO exception handle
            try {
              board.saveBoard(outputBoard);
            } catch (IOException n) {
              System.out.println("Null Pointer Exception");
            }
          }
         
         //If "w", or UP arrow is entered, move up
         if (move == KeyCode.UP || move == KeyCode.W) {
           board.move(Direction.UP);
           System.out.println("Moving <UP>");
           board.addRandomTile();
           updateBoard();
           updateScore();
         } 
         
         //If "a" or LEFT, move left
         if (move == KeyCode.LEFT || move == KeyCode.A) {
           board.move(Direction.LEFT);
           System.out.println("Moving <LEFT>");
           board.addRandomTile();
           updateBoard();
           updateScore();
         } 
         
         //If "s" or DOWN arrow, move down
         if (move == KeyCode.DOWN || move == KeyCode.S) {
           board.move(Direction.DOWN);
           System.out.println("Moving <DOWN>");
           board.addRandomTile();
           updateBoard();
           updateScore();
         } 
         
         //If "D" or RIGHT arrow, move Right
         if (move == KeyCode.RIGHT || move == KeyCode.D) {
           board.move(Direction.RIGHT);
           System.out.println("Moving <RIGHT>");
           board.addRandomTile();
           updateBoard();
           updateScore();
         } 
         
      }

  }
     

    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments 
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument 
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + " was thrown while creating a " +
                               "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                               "Constructor is broken or the file isn't " +
                               "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message 
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be used to save the 2048 board");
        System.out.println("                If none specified then the default \"2048.board\" file will be used");
        System.out.println("  -s [size]  -> Specifies the size of the 2048 board if an input file hasn't been");
        System.out.println("                specified.  If both -s and -i are used, then the size of the board");
        System.out.println("                will be determined by the input file. The default size is 4.");
    }


 

}
