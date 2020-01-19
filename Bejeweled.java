/**
* Bejeweled.java (Skeleton)
*
* This class represents a Bejeweled (TM)
* game, which allows player to make moves
* by swapping two pieces. Chains formed after
* valid moves disappears and the pieces on top
* fall to fill in the gap, and new random pieces
* fill in the empty slots.  Game ends after a
* certain number of moves or player chooses to 
* end the game.
File: Bejeweled.java
Name: Emily Jiao
Class: ICS3U1-01
Date:
Description:
*/

   import java.awt.Color;
   import java.util.*;

    public class Bejeweled {
   
      /* 
   	 * Constants
   	 */  
      final Color COLOUR_DELETE = Color.RED;
      final Color COLOUR_SELECT = Color.YELLOW;
   
      final int CHAIN_REQ = 3;	// minimum size required to form a chain
      final int NUMMOVE = 10;		// number of moves to be play in one game
      final int EMPTY = -1; 		// represents a slot on the game board where the piece has disappear  
      
      final int NUMPIECESTYLE;   // number of different piece style
      final int NUMROW;		  		// number of rows in the game board
      final int NUMCOL;	 	  		// number of columns in the game boar
   
   	
   	/* 
   	 * Global variables
   	 */   
      BejeweledGUI gui;	// the object referring to the GUI, use it when calling methods to update the GUI
      
      int board[][];		// the 2D array representing the current content of the game board
      
      boolean firstSelection = true;		// indicate if the current selection is the selection of the first piece
      int slot1Row, slot1Col;		// store the location of the first selection
      
      int score;						// current score of the game
      int numMoveLeft = NUMMOVE;				// number of move left for the game
// extension variables
//    	   char difficulty;
//          boolean chosen = true;
//          final int EASY = 45;
//          final int MEDIUM = 45;
//          final int HARD = 60;
// 			final int EMOVE = 15;
// 			final int MMOVE = 10;
// 			final int HMOVE = 8;
      /**************************
       * Constructor: Bejeweled
       **************************/
       public Bejeweled(BejeweledGUI gui) {
         this.gui = gui;
         NUMPIECESTYLE = gui.NUMPIECESTYLE;
         NUMROW = gui.NUMROW;
         NUMCOL = gui.NUMCOL;
         
      	// TO DO:  
      	// - creation of arrays
      	// - initialization of variables 
      	// - initialization of game board (on 2D array and on GUI)
         board = new int [NUMROW][NUMCOL];
         initBoard();
         Scanner sc = new Scanner (System.in);

// // Extension: difficulty of games 
//          System.out.println("Enter difficulty \ne = easy \nm = medium \nh = hard \nany other character = default game");
//          difficulty = sc.nextLine().charAt(0);
//          if (difficulty == 'e'){
//             System.out.println("Earn "+EASY+" points in "+EMOVE+" moves!");
// 				numMoveLeft = EMOVE;
//          } else if (difficulty == 'm'){
//             System.out.println("Earn "+MEDIUM+" points in "+MMOVE+" moves!");
// 				numMoveLeft = MMOVE;
//          } else if (difficulty == 'h'){
//             System.out.println("Earn "+HARD+" points in "+HMOVE+" moves!");
// 				numMoveLeft = HMOVE;
//          } else {
//             System.out.println("Default game will occur");
//             chosen = false;
//          }
// displaying the moves left for the beginning of the game
         gui.setMoveLeft(numMoveLeft);
			cascade();
			for (int i = 0; i < NUMROW; i++){
               for (int j = 0; j < NUMCOL; j++){
                  gui.unhighlightSlot(i,j);
               }
          }
			 gui.setScore(score);
      }
   
   
      /*****************************************************
       * play
       * This method is called when a piece is clicked.  
   	 * Parameter "row" and "column" is the location of the 
   	 * piece that is clicked by the player
       *****************************************************/
       public void play (int row, int column) {
      	 // TO DO:  implement the logic of the game
			cascade();
// declaring variables
			int hori1, hori2, vert1, vert2;
			int left1, left2, down1, down2;
         boolean unhighlighted = false;
// if structure to store the first selection and highlight
         if (firstSelection){
            slot1Row = row;
            slot1Col = column;
            gui.highlightSlot(row,column,COLOUR_SELECT);
         } else {
// nested if structure to check if the pieces are adjacent, if deselection is used, or invalid move
            if (adjacentPieces(slot1Row, slot1Col, row, column)){
               gui.highlightSlot(row,column,COLOUR_SELECT);
               swap(slot1Row,slot1Col, row, column);
            } 
            else if (slot1Row == row && slot1Col == column) {
//unhighlights for deselection
               gui.unhighlightSlot(row,column);
               unhighlighted = true;
            } 
            else {
// shows invalid message
               gui.showInvalidMoveMessage();
               gui.unhighlightSlot(slot1Row,slot1Col);
            }
         }
// checks if it is not first selection, meant to be unhighlighted, and if the pieces are adjacent
         if (!firstSelection && !unhighlighted && adjacentPieces(slot1Row, slot1Col, row, column)){
// storing the addition of two methods into variables to avoid calling it
				hori1 = rightCheck(slot1Row, slot1Col)-1+leftCheck(slot1Row, slot1Col);
    			vert1 = upCheck(slot1Row, slot1Col)-1+downCheck(slot1Row, slot1Col);        
				vert2 = upCheck(row, column)-1+downCheck(row, column);
				hori2 = rightCheck(row, column)-1+leftCheck(row, column);
				left1 = leftCheck(slot1Row, slot1Col);
				left2 = leftCheck(row, column);
				down1 = downCheck(slot1Row, slot1Col);
				down2 = downCheck(row, column);
            numMoveLeft--;
// if structure to check size number, highlight, and check if valid
// checks for chains created that involved two of the selected pieces
				if (hori1 >= CHAIN_REQ && vert1 >= CHAIN_REQ){
// updates the gui board to show the pieces swapped
               gui.setPiece(row, column, board[row][column]);
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
// for loop to highlight and make tiles empty
               for (int i = 0; i < hori1; i++){
                  gui.highlightSlot(slot1Row-left1+i+1, slot1Col, COLOUR_DELETE);
     					board[slot1Row-left1+i+1][slot1Col] = EMPTY;          
					}
// second for loop to do the same thing but vertical chain
               for (int i = 0; i < vert1; i++){
                  gui.highlightSlot(slot1Row, slot1Col-down1+i+1, COLOUR_DELETE);
     					board[slot1Row][slot1Col-down1+i+1] = EMPTY;          
					}
// displays chain size message
					gui.showChainSizeMessage(hori1+vert1-1);
// adds onto score
               score += hori1+vert1-1;
            } else if (hori2 >= CHAIN_REQ && vert2 >= CHAIN_REQ){
               gui.setPiece(row, column, board[row][column]);
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
               for (int i = 0; i < hori2; i++){
                  gui.highlightSlot(row-left2+i+1, column, COLOUR_DELETE);
     					board[row-left2+i+1][column] = EMPTY;          
					}
               for (int i = 0; i < vert2; i++){
                  gui.highlightSlot(row, column-down2+i+1, COLOUR_DELETE);
     					board[row][column-down2+i+1] = EMPTY;          
					}
					gui.showChainSizeMessage(hori2+vert2-1);
               score += hori2+vert2-1;
            } else if (hori1 >= CHAIN_REQ && vert2 >= CHAIN_REQ){
               gui.setPiece(row, column, board[row][column]);
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
               for (int i = 0; i < hori1; i++){
                  gui.highlightSlot(slot1Row-left1+i+1, slot1Col, COLOUR_DELETE);
						board[slot1Row-left1+i+1][slot1Col] = EMPTY;
               }
               for (int i = 0; i < vert2; i++){
                  gui.highlightSlot(row, column-down2+i+1, COLOUR_DELETE);
     					board[row][column-down2+i+1] = EMPTY;          
					}
               gui.showChainSizeMessage(hori1+vert2);
               score += hori1+vert2;
            } else if (hori2 >= CHAIN_REQ && vert1 >= CHAIN_REQ){
               gui.setPiece(row, column, board[row][column]);
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
               for (int i = 0; i < hori2; i++){
                  gui.highlightSlot(row-left2+i+1, column, COLOUR_DELETE);
     					board[row-left2+i+1][column] = EMPTY;          
					}
               for (int i = 0; i < vert1; i++){
                  gui.highlightSlot(slot1Row, slot1Col-down1+i+1, COLOUR_DELETE);
						board[slot1Row][slot1Col-down1+i+1] = EMPTY;
               }
               gui.showChainSizeMessage(hori2+vert1);
               score += hori2+vert1;
            } else if (hori1 >= CHAIN_REQ && hori2 >= CHAIN_REQ && row == slot1Row){
               gui.setPiece(row, column, board[row][column]);
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
               for (int i = 0; i < hori1; i++){
                  gui.highlightSlot(slot1Row-left1+i+1, slot1Col, COLOUR_DELETE);
						board[slot1Row-left1+i+1][slot1Col] = EMPTY;
               }
               for (int i = 0; i < hori2; i++){
                  gui.highlightSlot(row-left2+i+1, column, COLOUR_DELETE);
						board[row-left2+i+1][column] = EMPTY;
               }
               gui.showChainSizeMessage(hori1+hori2);
               score += hori1+hori2;
            }else if (vert1 >= CHAIN_REQ && vert2 >= CHAIN_REQ && column == slot1Col){
               gui.setPiece(row, column, board[row][column]);
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
               for (int i = 0; i < vert1; i++){
                  gui.highlightSlot(slot1Row, slot1Col-down1+i+1, COLOUR_DELETE);
						board[slot1Row][slot1Col-down1+i+1] = EMPTY;
               }
               for (int i = 0; i < vert2; i++){
                  gui.highlightSlot(row, column-down2+i+1, COLOUR_DELETE);
						board[row][column-down2+i+1] = EMPTY;
               }
					gui.showChainSizeMessage(vert1+vert2);
               score += vert1+vert2;
               
// checks for one chain formed by one piece 
            }else if(hori1 >= CHAIN_REQ){
               gui.setPiece(row, column, board[row][column]);
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
//for loop for highlighting
               for (int i = 0; i < hori1; i++){
                  gui.highlightSlot(slot1Row-left1+i+1, slot1Col, COLOUR_DELETE);
               	board[slot1Row-left1+i+1][slot1Col] = EMPTY;
					}
					gui.showChainSizeMessage(hori1);
               score += hori1;
            }else if (vert1 >= CHAIN_REQ){
               gui.setPiece(row, column, board[row][column]);
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
					for (int i = 0; i < vert1; i++){
                  gui.highlightSlot(slot1Row, slot1Col-down1+i+1, COLOUR_DELETE);
     					board[slot1Row][slot1Col-down1+i+1] = EMPTY;          
					}
					gui.showChainSizeMessage(vert1);
					score += vert1;
            }else if (vert2 >= CHAIN_REQ){
               gui.setPiece(row, column, board[row][column]);
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
					for (int i = 0; i < vert2; i++){
                  gui.highlightSlot(row, column-down2+i+1, COLOUR_DELETE);
     					board[row][column-down2+i+1] = EMPTY;          
					}
					gui.showChainSizeMessage(vert2);
               score += vert2;
            } else if (hori2 >= CHAIN_REQ){
               gui.setPiece(row, column, board[row][column]);
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
					for (int i = 0; i < hori2; i++){
                  gui.highlightSlot(row-left2+i+1, column, COLOUR_DELETE);
     					board[row-left2+i+1][column] = EMPTY;          
					}
					gui.showChainSizeMessage(hori2);
               score += hori2;
            } else {
            swap(slot1Row,slot1Col, row, column);
            gui.showInvalidMoveMessage();
            numMoveLeft++;
            }
			shiftDown();
			cascade();
             for (int i = 0; i < NUMROW; i++){
               for (int j = 0; j < NUMCOL; j++){
                  gui.unhighlightSlot(i,j);
               }
            }
         } 
         firstSelection = !firstSelection;
         gui.setScore(score);
         gui.setMoveLeft(numMoveLeft);
//extension - difficulty of games
         if (numMoveLeft == 0){
//             if (difficulty == 'e' || difficulty == 'm' && score >= MEDIUM || difficulty == 'h' && score >= HARD){
//                System.out.println("You win!");
//             } else if (chosen){
//                System.out.println("You lose :(");
//             }
            endGame();
         }
      }
   	
   //initializes the board for the start of the game
       public void initBoard(){
// for loop to run through each coordinate in the array
         for (int i = 0; i < NUMROW; i++){
            for (int j = 0; j < NUMCOL; j++){
               board[i][j] = (int)(Math.random()*(NUMPIECESTYLE));
               gui.setPiece(i,j, board[i][j]);
            }
         }
      }
   
 // checking if it is a valid move (pieces selected are adjacent)
       public boolean adjacentPieces (int x1, int y1, int x2, int y2){
 // if structure to check the condition
         if (Math.abs(x1-x2) == 1 && y1-y2 == 0 || Math.abs(y1-y2) == 1 && x1-x2 == 0){
            return true;
         }
         else {
            return false;
         }
      }
   	
// checks how many of the same tiles are on the right for points
       public int rightCheck(int x1, int y1){
         int count = 0;
 // while loop to keep counting the tiles until the value of the tiles next to each other are not the same
         while (x1+count < NUMROW && board[x1][y1] == board[x1+count][y1]){
            count++;
         }
         return count;
      }
   
// checks how many of the same tiles are on the left
       public int leftCheck(int x1, int y1){
         int count = 0;
         while (x1-count >= 0 && board[x1][y1] == board[x1-count][y1]){
            count++;
         }
         return count;
      }
   	
// checks how many of the same tiles are above
       public int upCheck(int x1, int y1){
         int count = 0;
         while (y1+count < NUMCOL && board[x1][y1] == board[x1][y1+count]){
            count++;
         }
         return count;
      }
   	
// checks how many of the same tiles are below
       public int downCheck(int x1, int y1){
         int count = 0;
         while (y1-count >= 0 && board[x1][y1] == board[x1][y1-count]){
            count++;
         }
         return count;
      }
   	
// swaps the two selected tiles
       public void swap (int x1, int y1, int x2, int y2){
         int temp;
         temp = board[x1][y1];
         board[x1][y1] = board[x2][y2];
         board[x2][y2] = temp;
      }
// sets the value of the tile to -1 when a combo is created to delete all tiles in the combo
      public void shiftDown(){
// nested for loop to go through the entire board
			for (int j = NUMCOL-1; j >= 0; j--){
            int not = 0;
			   int count = 0;
				for (int i = NUMROW-1; i >= 0; i--){
// searches for the empty tiles
					if (board[i][j] == EMPTY){
						count++;
                  if (count == 1){
                     not = i;
                  }
               }
             }
// for loop to shift down
               for (int a = 0; a < not-count+1; a++){
                  board[not-a][j] = board[not-count-a][j];
               }
// replaces the top tiles in the column with the -1 tiles to random
               for (int b = 0; b < count; b++){
                  board[b][j] = (int)(Math.random()*NUMPIECESTYLE);
               }
			}
			             for (int i = 0; i < NUMROW; i++){
               for (int j = 0; j < NUMCOL; j++){
                  gui.unhighlightSlot(i,j);
               }
            }
         updateBoard();
		}
         
// updating gui board
      public void updateBoard(){
         for (int i = 0; i < NUMROW; i++){
            for (int j = 0; j < NUMCOL; j++){
               gui.setPiece(i,j,board[i][j]);
            }
         }
      }
// Extension - cascade effect
 		public void cascade(){
				int temp;
			for (int j = 0; j < NUMCOL; j++){
				int right;
				for (int i = 0; i < NUMROW; i++){
					right =	rightCheck(i, j);
					if (right >= CHAIN_REQ){
						for (int a = 0; a < right; a++){
                  gui.highlightSlot(i+a, j, COLOUR_DELETE);
     					board[i+a][j] = EMPTY;          
						}
					gui.showChainSizeMessage(right);
               score += right;
						shiftDown();
					}
				}
			}
			for (int j = 0; j < NUMCOL; j++){
				int down;
				for (int i = 0; i < NUMROW; i++){
					down = downCheck(i, j);
					if (down >= CHAIN_REQ){
						for (int a = 0; a < down; a++){
                  gui.highlightSlot(i, j-a, COLOUR_DELETE);
     					board[i][j-a] = EMPTY;          
						}
					gui.showChainSizeMessage(down);
               score += down;
						shiftDown();
					}
				}
			}
			
		}
      /*****************************************************
       * endGame
       * This method is called when the player clicks on the
   	 * "End Game" button
   	 *****************************************************/
       public void endGame() {
       //shows the game over message
         gui.showGameOverMessage(score,NUMMOVE-numMoveLeft);
       }
   
   }