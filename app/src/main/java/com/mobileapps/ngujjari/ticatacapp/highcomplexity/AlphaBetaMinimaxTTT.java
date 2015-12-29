package com.mobileapps.ngujjari.ticatacapp.highcomplexity;

import java.util.Random;

public class AlphaBetaMinimaxTTT {
	 public static void main(String[] args) { 
	        Board b = new Board();
	        Random rand = new Random();

	        b.displayBoard();
	       /* b.board[1][1] = 1;
	        b.board[0][0] = 1;
	        b.board[1][2] = 1;
	        
	        b.board[1][0] = 2;
	        //b.board[2][2] = 2;
	        b.board[2][1] = 2;
	        
	        b.board[0][0] = 1;
	        b.board[2][0] = 1;
	        b.board[1][2] = 1;
	        
	        b.board[1][0] = 2;
	        //b.board[2][2] = 2;
	        b.board[1][1] = 2;
	        */
	        System.out.println("Who's gonna move first? (1)Computer (2)User: ");
	        int choice = b.scan.nextInt();
	        if (choice == 1) {
	            Point p = new Point(rand.nextInt(3), rand.nextInt(3));
	            Move mv = new Move(null, p);
	            b.placeAMove(mv, 1);
	            b.displayBoard();
	        }

	        b.displayBoard();
	        while (!b.isGameOver()) {
	        	Move userMv = null;
	           
	            if(b.canDragTheInputOnBoard()){
	            	System.out.print ("Drag : Your move: ");
	            	System.out.println("From Move : ");
	            	Point fromMove = new Point(b.scan.nextInt(), b.scan.nextInt());
	            	System.out.println("To move: ");
	            	Point toMove = new Point(b.scan.nextInt(), b.scan.nextInt());
	            	userMv = new Move(fromMove, toMove);
	            }
	            else{ // Single input
	            	System.out.println("Your move: ");
	            	Point userMove = new Point(b.scan.nextInt(), b.scan.nextInt());
	            	userMv = new Move(null, userMove);
	            }
	            	           
	            b.placeAMove(userMv, 2); //2 for O and O is the user
	            b.displayBoard();
	            if (b.isGameOver()) break;
	            
	            Board backupBoard = new Board();
	            backupBoard.board = Board.deepCopy(b.board);
	            
	            System.out.println("before 1: ");
	            //b.displayBoard();
	            backupBoard.displayBoard();
	           
	            b.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
	            //System.out.println("After1 : ");
	           // b.displayBoard();
	            //backupBoard.displayBoard();
	            for (PointsAndScores pas : b.rootsChildrenScore) 
	                System.out.println("Move: " + pas.move + " Score: " + pas.score);
	            
	            System.out.println("before : ");
	            backupBoard.displayBoard();
	           
	            backupBoard.placeAMove(b.returnBestMove(), 1);
	            System.out.println("After : ");
	            b.board = backupBoard.board;
	            backupBoard = null;
	            b.displayBoard();
	        }
	        if (b.hasXWon()) {
	            System.out.println("Unfortunately, you lost!");
	        } else if (b.hasOWon()) {
	            System.out.println("You win!");
	        } else {
	            System.out.println("It's a draw!");
	        }
	    }
}
