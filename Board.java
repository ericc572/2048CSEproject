//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  Brandon Williams                                        //
// Date:    1/17/15                                                 //
//------------------------------------------------------------------//

/**     Sample Board
 *
 *      0   1   2   3
 *  0   -   -   -   -
 *  1   -   -   -   -
 *  2   -   -   -   -
 *  3   -   -   -   -
 *
 *  The sample board shows the index values for the columns and rows
 */

/* Edited by: 
 * cs login: cs8bakk
 * name: Eric Chen
 * 2/5/2015
 * Board.java 
 */
import java.util.*;
import java.io.*;
/* This class sets up the board implementation for the game 2048. Will usually 
* involve row i and column j 
*/

public class Board
{
    public final int NUM_START_TILES = 2;
    public final int TWO_PROBABILITY = 90;
    public final int GRID_SIZE;


    private final Random random;
    private int[][] grid;
    private int score;

    //This class will set up constructs a fresh board with random tiles
    public Board(int boardSize, Random random)
    {
      //Initialize all instance variables, create new grid with passed size
      this.random = random;
      GRID_SIZE = boardSize; 
      grid = new int [GRID_SIZE][GRID_SIZE];
      score = 0;
      int n = 0;

      //To start the game, add two tiles
      while (n < NUM_START_TILES) {
        this.addRandomTile();
        n++;
      }

    }


    // Construct a board based off of an input file 
    // Take parameters inputBoard, board to be loaded and random number random
    public Board(String inputBoard, Random random) throws IOException
    {
      //To load files, scan through the input file. The first two will be 
      //the size of the grid, and the score. Then, loop through the remaining 
      //integers and append them to the grid. 

      this.random = random;  
      Scanner scan = new Scanner (new File (inputBoard));  
      GRID_SIZE = scan.nextInt();
      score = scan.nextInt();
      grid = new int [GRID_SIZE][GRID_SIZE];
    
      //Loops through rows, then columns
      for (int i = 0; i < grid.length ; i++) {
        for (int j = 0; j < grid[i].length; j++) {
           grid[i][j] = scan.nextInt();
        } 
        //For each row, start a new line.
        System.out.println();
      }
    
    }

    // Saves the current board to a file
    //Parameters: outputBoard file to save as
    //Return type: void
    public void saveBoard(String outputBoard) throws IOException
    {
      //Similar to loading files, create PrintWriter. First print the grid 
      //size and score, then loop through for the rest of the board.
      PrintWriter pw = new PrintWriter (outputBoard);
      pw.println(GRID_SIZE); 
      pw.println(score);  

      //Loop through rows then columns
      for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[i].length; j++) {
           pw.print(grid[i][j] + " ");
        }
        //New row, print to new line.
        pw.println();
      }
      pw.close();  
   }

    // Adds a Random Tile (of value 2 or 4) to a 
    // Random Empty space on the board
    //Return type: void, no parameters/arguments
    public void addRandomTile()
    { 
      // We do this by counting the number of empty spaces on the board.
      // Then, append these indexes to an Integer ArrayList. Find the indexes
      // at location, and add a random tile 2 or 4.

      int count = 0;
      int location = 0;
      List <Integer> list = new ArrayList <>();

      //Loop through rows and columns, appending the i and j values to list
      //in chronological order. j will always come after i
      for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[i].length; j++) {
           if (grid[i][j] == 0) { 
             count ++;
             list.add(i);
             list.add(j);
           }
        }
     }
      
      //If count is 0, no more empty tiles. Exit the program.
      if (count == 0) {
        return;
      }
       
      //Initialize random integers location 1 to count - 1 and value 1-99
      location = random.nextInt(count);
      int value = random.nextInt(100);
      
      //index we are trying to get is 2*location, and 2*location+1 in the grid
      int index = 2*location;
     
      //Based on constant TWO_PROBABILITY place either a 2 or a 4 tile.
      if (value < TWO_PROBABILITY) {
        grid[list.get(index)][list.get(index+1)] = 2;
      } else {       
        grid[list.get(index)][list.get(index+1)] = 4;
      }

      
    }
        
    
    // Check to see if we have a game over
  
   /* Method isGameOver: Check if game is over using canMove conditions.
    * if neither of the canMove in certain directions return true,
    * then the Game is over (return true)
    */
    
    public boolean isGameOver()
    {
      if (canMove(Direction.DOWN) == false && canMove(Direction.UP) == false &&
    	  canMove(Direction.LEFT) == false && canMove(Direction.RIGHT) == false) {
    	   System.out.println("Game is Over..");
    	   return true;
      }
      return false;
    }

    // Determine if we can move in a given direction
    // Use different helper methods specified by direction
    public boolean canMove(Direction direction)
    {
      if (direction == Direction.LEFT) {
        return canMoveLeft();
      }

      else if (direction == Direction.UP) {
        return canMoveUp();
      } 

      else if (direction == Direction.DOWN) {
        return canMoveDown();
      } 

      else if (direction == Direction.RIGHT) {
        return canMoveRight();
      }

      return true;   
    }
    
    /* Helper method for canMove: looping through each index in the grid,
     * If number not equals to 0, check the number above it in the same column.
     * If number above it is 0, or the same, it will either slide or combine. 
     * Else return false.
     */
    private boolean canMoveUp() {
      //Start from 1 because we check the i-1 against it anyways
      for (int i = 1; i < grid.length; i++) {
        for (int j = 0; j < grid[i].length; j++) {
          if (grid [i][j] != 0) {
            //if tile above is 0 or same, move is valid
            if (grid[i-1][j] == 0 || grid[i][j] == grid[i-1][j]) {
              return true;
            } 
          }
        }
      } 
      return false;
    }
    
    /* Helper method for canMove: loop through index in the grid
     * in the down direction. If number is not 0, check number below in the
     * same column. If the next number is 0 or the same, move is valid.
     * Else return false.
     */
    private boolean canMoveDown() {
     //Must end at size - 1, because we are accessing the + 1 element.
     for (int i = 0; i < GRID_SIZE-1; i++) {
        for (int j = 0; j < GRID_SIZE; j++) {
          if (grid[i][j] != 0) {
             //If next number is 0 or same, move is valid
             if (grid[i+1][j] == 0 || grid[i][j] == grid[i+1][j]) {
                return true;
             } 
          }
        }
      }
      return false;
    }
    
    /*Similar helper method for canMove: loop through indexes in grid
     *If number is not 0, check number to the left of it. If number is 0 or
     * the same, move is valid.
     * else return false
     */
    private boolean canMoveLeft() {
      for (int i = 0; i < GRID_SIZE; i++) {
        //Must start at 1 because we are accessing the - 1 index
        for (int j = 1; j < GRID_SIZE; j++) {
        	if (grid [i][j] != 0) {
                if (grid[i][j-1] == 0 || grid[i][j] == grid[i][j-1]) {
                  return true;
                } 
           }
        }
      }
      return false;
    }
    
    /* Similar helper method for canMove: loop through indexes in grid
     * If number is not 0, check number to the right of it in the row.
     * If next number is 0 or same, move is valid.
     * else return false 
     */
    private boolean canMoveRight() {
      for (int i = 0; i < grid.length; i++) {
       //Must end at size - 1, to ensure it stays in bounds
       for (int j = 0; j < grid[i].length - 1; j++) {
          if (grid[i][j] != 0) {
            if (grid[i][j+1] == 0 || grid[i][j] == grid[i][j+1]) {
              return true;
            } 
          }
        }
      }
      return false;
    }

    // Perform a move Operation given the direction as an argument
    // Use separate helper methods: move*Direction*Blank and move*Direction*Add
    // moveBlank moves the tiles in given direction when there are empty tiles
    // moveAdd adds tiles if they are similar, and places them accordingly.
    // if successful, return true
    public boolean move(Direction direction)
    {
      if (direction == Direction.LEFT) {
        moveLeftBlank();
        moveLeftAdd();
      }

     else if (direction == Direction.UP) {
        moveUpBlank();
        moveUpAdd();
      } 

      else if (direction == Direction.DOWN) {
        moveDownBlank();
        moveDownAdd();
      } 

      else if (direction == Direction.RIGHT) {
        moveRightBlank();
        moveRightAdd();
      }
      return true;
    }
    
    /* helper method: moveLeftBlank with type void
     * Loop through indexes of the grid, checking if they are 0
     * If 0, check number to the right. while in bounds and number is also 0,
     * skip it. If not 0, swap with a value that is still 0.
     */
    private void moveLeftBlank() {
    	if (canMoveLeft()) {
        //loop through rows i and columns j
    		for (int i = 0; i < GRID_SIZE; i++) {
    			for (int j = 0; j < GRID_SIZE; j++) {
    			  //Check if index is 0
            if (grid[i][j] == 0) {
    					int x = j + 1;
              //While x is between 0 and grid size, and grid at i/x is 0
              //increment x to look for the next number.
    					while (x < GRID_SIZE && grid [i][x] == 0) {
    						x++;
    					}
    					//if grid at i/x is not 0, and is still within bounds,
              //swap it with the leftmost 0. Set grid at i/x back to 0.
    					if (x < GRID_SIZE) {
    						grid[i][j] = grid[i][x];
    						grid[i][x] = 0;
    					}
    				}			
    			}
    		}
    	}
    }
    /* helper method: moveLeftAdd with type void
     * loop through grid values, from left to right
     * if next number is not 0 and equal to the current number, add at left pos.
     * Slide remaining nonzero values down and increment sum.
     */
    private void moveLeftAdd() {
      //initialize variable sum to hold sum
    	int sum = 0;
      //loop through rows i and columns  j
      //j must end at size -1 to preserve bounds
    	for (int i = 0; i < GRID_SIZE; i++) {
    		for (int j = 0; j < GRID_SIZE-1; j++) {
    			int x = j + 1;
            //if number after is not equal 0 or equal to current number, add
    				if (grid[i][x] != 0 && grid[i][j] == grid[i][x]) {
    					sum = grid [i][j] + grid[i][x];
    					grid[i][j] = sum;
             // while next (current + 2) remains within bounds, slide over 
             // and increment x. This ensures staying in bounds, and the case 
             // of 4 0 2 2 (makes sure slides all the way over)
    			    	while (x+1 < GRID_SIZE) {
    					  	grid[i][x] = grid[i][x+1];
    				    	x++;
    				  	} 
              //Increment score
    					score += sum;
    					grid[i][x] = 0;
    				}
    		}
    			
    	}
    }
    

    /* Similar helper method moveRightBlank in the right direction.
     * Moves all nonzero numbers to rightmost.
     * loop through grid, finding indexes with 0. 
     * if next number to the left is 0, skip 
     * if not swap with original index
     */
    private void moveRightBlank() {
    	if (canMoveRight()) {
    		for (int i = 0; i < GRID_SIZE; i++) {
    			//Index must start from the end becausse we check inwards out
          for (int j = GRID_SIZE-1; j >= 0; j--) {
    				if (grid[i][j] == 0) {
    					int x = j - 1;
    					//while x stays in bounds, and grid at i/x is 0, skip
    					while (x >= 0 && grid [i][x] == 0) {
    						x--;
    					}
    					//when it is nonzero, swap with original index
    					if (x >= 0) {
    						grid[i][j] = grid[i][x];
    						grid[i][x] = 0;
    					}
    				}			
    			}
    		}
    	}
   }
    
    /* Helper method moveRightAdd after moveRightBlank, 
     * checks if tiles are equal and add them.
     * if number before is not equal 0, and equal to current number, add them
     */
    private void moveRightAdd() {
    	int sum = 0;
    	for (int i = 0; i < GRID_SIZE; i++) {
        //Must decrement from size-1 because we start from the right corner
    		for (int j = GRID_SIZE-1; j > 0; j--) {
    			int x = j - 1;
            //if index before is not 0 and equal to previous, add and set to
            //original (the rightmost)
    				if (grid[i][x] != 0 && grid[i][j] == grid[i][x]) {
    					sum = grid [i][j] + grid[i][x];
    					grid[i][j] = sum;
              //while x stays within bounds, slide tile before to the right
    					while (x-1 >= 0) {
    						grid[i][x] = grid[i][x-1];
    						x--;
   						}
              //increment sum
    					score += sum;
    					grid[i][x] = 0;
    				}
    		}
    			
    	}
    }
       
    /* Helper method moveUpBlank in direction UP
     * moves all tiles nonzero in the upwards direction
     * this time swap columns and rows to increment up/down
     */
    private void moveUpBlank() {
    	if (canMoveUp()) {
        //loop through grid finding values of zero
    		for (int i = 0; i < GRID_SIZE; i++) {
    			for (int j = 0; j < GRID_SIZE; j++) {
    				if (grid[i][j] == 0) {
              //check if next value below is 0, if so skip  
    					int x = i + 1;
    					while (x < GRID_SIZE && grid [x][j] == 0) {
    						x++;
    					}
    					if (x < GRID_SIZE) {
    						grid[i][j] = grid[x][j];
    						grid[x][j] = 0;
    					}
    				}			
    			}
    		}
    	}
   
    }
    // Private helper method moveUpAdd in up direction
    // checks if tiles are equal and adds them
    // If number below is equal to the current number, add them at the topmost
    private void moveUpAdd() {
    	int sum = 0;
      //must loop through size - 1 to ensure bounds
    	for (int i = 0; i < GRID_SIZE-1; i++) {
    		for (int j = 0; j < GRID_SIZE; j++) {
    			int x = i + 1;
            //If tile below is not equal to zero and equal to current tile,
            // add together and replace current tile
    				if (grid[x][j] != 0 && grid[i][j] == grid[x][j]) {
    					sum = grid [i][j] + grid[x][j];
    					grid[i][j] = sum;
              //while x stays within bounds slide tile upwards
    					while (x+1 < GRID_SIZE) {
    						grid[x][j] = grid[x+1][j];   						
    						x++;
    					} 
              //increment sum
    					score += sum;
    					grid[x][j] = 0;
    					
    				}
    		}
    			
    	}
    }
    
    // private helper method moveDownBlank: moves numeric tiles towards bottom
    // If current number is 0, slide rest of tiles down
    private void moveDownBlank() {
    	if (canMoveDown()) {
          //Start at end of column and decrement
    			for (int j = GRID_SIZE-1; j >= 0; j--) {
    				for (int i = 0; i < GRID_SIZE; i++) {
    					//if current value is 0 
              if (grid[j][i] == 0) {
    						int x = j - 1;
    					  //while next value is zero, skip
    						while (x >= 0 && grid [x][i] == 0) {
    							x--;
    						}	
    					  //if it is nonzero swap with current indexed value
    						if (x >= 0) {
    							grid[j][i] = grid[x][i];
    							grid[x][i] = 0;
    						}
    					}			
    				}
    			}
    	}
    }
    
    // Private helper method moveDownAdd(): adds like tiles together downwards
    // If tile above is non zero and equal to current, collapse them 
    private void moveDownAdd() {
    	int sum = 0;
    	//Start at end of column and decrement
      for (int i = GRID_SIZE-1; i > 0; i--) {
    		for (int j = 0; j < GRID_SIZE; j++) {
    			int x = i - 1;
            //If next value is nonzero and equal to current value
    				if (grid[x][j] != 0 && grid[i][j] == grid[x][j]) {
    					sum = grid [i][j] + grid[x][j];
    					grid[i][j] = sum;
              //while x stays in bounds slide tile
    					while (x- 1 >= 0) {
    						grid[x][j] = grid[x-1][j];   						
    						x--;
    					} 
              //increment sum
    					score += sum;
    					grid[x][j] = 0;
    				}
    		}
    			
    	}
      } 
   
   // Return the reference to the 2048 Grid
    public int[][] getGrid()
    {
        return grid;
    }

    // Return the score
    public int getScore()
    {
        return score;
    }

    @Override
    public String toString()
    {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                                    String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }
}
