package com.mobileapps.ngujjari.ticatacapp.highcomplexity;

import android.util.Log;

import com.mobileapps.ngujjari.ticatacapp.ActionTakenBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Board implements Cloneable{
	   // List<Point> availablePoints;
	    Scanner scan = new Scanner(System.in);
	    int[][] board = new int[3][3]; 
	    static Integer adjNodes[][] = {{2,3,4,5,6,7,8,9},{9,1,3},{2,1,4},{3,1,5},{4,1,6},{5,1,7},{6,1,8},{7,1,9},{8,1,2}};
	    public List<PointsAndScores> rootsChildrenScore = new ArrayList<PointsAndScores>();
	    String tst = "";
    //Set this to some value if you want to have some specified depth limit for search
        public int uptoDepth = 3;

	    public Board(){}
	    public Board(int[][] board ){ this.board = board;}
	    public static int[][] deepCopy(int[][] original) {
	        if (original == null) {
	            return null;
	        }

	        final int[][] result = new int[original.length][];
	        for (int i = 0; i < original.length; i++) {
	            result[i] = Arrays.copyOf(original[i], original[i].length);
	            // For Java versions prior to Java 6 use the next:
	            // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
	        }
	        return result;
	    }

	    public Board clone()
	    {
	    	Board b;
	    	try {
	    		 b = new Board();
	    		 b.board = board.clone();
	    		} 
	    	catch (Exception e) 
	    	{					
	    		e.printStackTrace();			
	    		throw new RuntimeException();		
	    	}
	    	return b;
	    }
	    public int evaluateBoard() {
	        int score = 0;

	        //Check all rows
	        for (int i = 0; i < 3; ++i) {
	            int blank = 0;
	            int X = 0;
	            int O = 0;
	            for (int j = 0; j < 3; ++j) {
	                if (board[i][j] == 0) {
	                    blank++;
	                } else if (board[i][j] == 1) {
	                    X++;
	                } else {
	                    O++;
	                }

	            } 
	            score+=changeInScore(X, O); 
	        }

	        //Check all columns
	        for (int j = 0; j < 3; ++j) {
	            int blank = 0;
	            int X = 0;
	            int O = 0;
	            for (int i = 0; i < 3; ++i) {
	                if (board[i][j] == 0) {
	                    blank++;
	                } else if (board[i][j] == 1) {
	                    X++;
	                } else {
	                    O++;
	                } 
	            }
	            score+=changeInScore(X, O);
	        }

	        int blank = 0;
	        int X = 0;
	        int O = 0;

	        //Check diagonal (first)
	        for (int i = 0, j = 0; i < 3; ++i, ++j) {
	            if (board[i][j] == 1) {
	                X++;
	            } else if (board[i][j] == 2) {
	                O++;
	            } else {
	                blank++;
	            }
	        }

	        score+=changeInScore(X, O);

	        blank = 0;
	        X = 0;
	        O = 0;

	        //Check Diagonal (Second)
	        for (int i = 2, j = 0; i > -1; --i, ++j) {
	            if (board[i][j] == 1) {
	                X++;
	            } else if (board[i][j] == 2) {
	                O++;
	            } else {
	                blank++;
	            }
	        }

	        score+=changeInScore(X, O);

	        return score;
	    }
	    private int changeInScore(int X, int O){
	        int change;
	        if (X == 3) {
	            change = 100;
	        } else if (X == 2 && O == 0) {
	            change = 10;
	        } else if (X == 1 && O == 0) {
	            change = 1;
	        } else if (O == 3) {
	            change = -100;
	        } else if (O == 2 && X == 0) {
	            change = -10;
	        } else if (O == 1 && X == 0) {
	            change = -1;
	        } else {
	            change = 0;
	        } 
	        return change;
	    }
	    

	    
	    public int alphaBetaMinimax(int alpha, int beta, int depth, int turn){
	    	Log.v("Board","alpha = "+alpha+ " , beta = "+beta+" depth = "+depth +" turn = "+turn +" rootsChildrenScore = "+rootsChildrenScore.size());
	        if(beta<=alpha){ 
	        	//System.out.println("Pruning at depth = "+depth);
	        	if(turn == 1) return Integer.MAX_VALUE; 
	        	else return Integer.MIN_VALUE; 
	        }
	        boolean isGameOver = isGameOver();
	      //  System.out.println("isGameOver = "+isGameOver );
	        
	        if(depth == uptoDepth || isGameOver) {
	        	
	        	return evaluateBoard();
	        }
	        
	        List<Move> pointsAvailable = getAvailableStates(turn);
	        
	        //if(pointsAvailable.isEmpty()) return 0;
	        
	        if(depth==0) rootsChildrenScore.clear(); 
	        
	        int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;
	        
	        for(int i=0;i<pointsAvailable.size(); ++i){
	            Move mv = pointsAvailable.get(i);
	            
	            int currentScore = 0;
	           // displayBoard() ;
	            if(turn == 1){
	                placeAMove(mv, 1); 
	               // displayBoard() ;
	                currentScore = alphaBetaMinimax(alpha, beta, depth+1, 2);
	                maxValue = Math.max(maxValue, currentScore); 
	                
	                //Set alpha
	                alpha = Math.max(currentScore, alpha);
	                
	                if(depth == 0)
	                    rootsChildrenScore.add(new PointsAndScores(currentScore, mv));
	            }else if(turn == 2){
	                placeAMove(mv, 2);
	               // displayBoard() ;
	                currentScore = alphaBetaMinimax(alpha, beta, depth+1, 1); 
	                minValue = Math.min(minValue, currentScore); 
	                
	                //Set beta
	                beta = Math.min(currentScore, beta);
	            }
	            //System.out.println("Reset : mv "+mv+ " , player = "+turn);
	            //reset board
	            if(mv.fromPt != null ) board[mv.fromPt.x][mv.fromPt.y] = turn;
	            board[mv.toPt.x][mv.toPt.y] = 0; 
	            
	            //If a pruning has been done, don't evaluate the rest of the sibling states
	            if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break;
	        }
	        return turn == 1 ? maxValue : minValue;
	    }  

	    public boolean isGameOver() {
	        //Game is over is someone has won, or board is full (draw)
	        return (hasXWon() || hasOWon());
	    }

	    public boolean hasXWon() {
	        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)) {
	            //System.out.println("X Diagonal Win");
	            return true;
	        }
	        for (int i = 0; i < 3; ++i) {
	            if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)
	                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1))) {
	                // System.out.println("X Row or Column win");
	                return true;
	            }
	        }
	        return false;
	    }

	    public boolean hasOWon() {
	        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 2) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 2)) {
	            // System.out.println("O Diagonal Win");
	            return true;
	        }
	        for (int i = 0; i < 3; ++i) {
	            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 2)
	                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 2)) {
	                //  System.out.println("O Row or Column win");
	                return true;
	            }
	        }

	        return false;
	    }

	    public List<Move> getAvailableStates(int turn) {
	    	
	    	List<Move> availableMoves = new ArrayList<Move>();
	    	
	    	// calculate available empty positions on the board
	    	List<Point> localAvailablePoints = new ArrayList<Point>();
	        for (int i = 0; i < 3; ++i) {
	            for (int j = 0; j < 3; ++j) {
	                if (board[i][j] == 0) {
	                	localAvailablePoints.add(new Point(i, j));
	                }
	            }
	        }
	        
	        // calculate turn positions on board
	        List<Point> currentTurnPoints = new ArrayList<Point>();
	    	for (int i = 0; i < 3; ++i) {
	            for (int j = 0; j < 3; ++j) {
	                if (board[i][j] == turn) {
	                	currentTurnPoints.add(new Point(i, j));
	                }
	            }
	        }
	    	
	    	// if board player 3 moves not completed yet. return default available moves
	    	if(currentTurnPoints.size() < 3) {
	    		for(Point lp : localAvailablePoints){
	    			Move mv = new Move(null, lp);
	    			availableMoves.add(mv);
	    		}
	    			return availableMoves; 
	    	}
	    	else{ // If no. of moves = 3
	    		
	    		for(Point turnPt : currentTurnPoints){
	    			List<Move> tempmvs = getValidMoves(turnPt); // Expecting all unique moves , as from nodes will be different
	    			availableMoves.addAll(tempmvs);
	    		}
	    	}
	        return availableMoves;
	    }

	    public List<Point> getAdjucentNodes(Point pt)
	    {
	    	List<Point> adjPoints = new ArrayList<Point>();
	    	Point adPt = adjNode(pt.x -1, pt.y);
	    	if(adPt != null )adjPoints.add(adPt);
	    	
	    	adPt = adjNode(pt.x +1, pt.y);
	    	if(adPt != null )adjPoints.add(adPt);
	    	
	    	adPt = adjNode(pt.x , pt.y -1);
	    	if(adPt != null )adjPoints.add(adPt);
	    	
	    	adPt = adjNode(pt.x, pt.y + 1);
	    	if(adPt != null )adjPoints.add(adPt);
	    	
	    	// Diagonals
	    	List<Point> diagList = adjDiagonalNode(pt.x , pt.y);
	    	if(diagList != null )adjPoints.addAll(diagList);
	    	
	    		    	
	    	return adjPoints;
	    }
	    
	    public List<Point> adjDiagonalNode(int x, int y)
	    {
	    	int cnt = 0;
	    	List<Point> adjPoints = new ArrayList<Point>();
	    /*	for(int i = -2; i <= 2; i++) @TODO Automate later
	    	{
	    		int p = x + i; 
	    		int k = y + i; 
		    	if(p != x && k != y && p >= 0 && p <= 2 && k >= 0 && k <=2 )
		    	{ // boudaries
		    		cnt ++;
		    		adjPoints.add(new Point(p, k));
	        	}
	    	}
	    	if(cnt == 2){
	    		return adjPoints;	    		
	    	}*/
	    	
	    	if(x == 0 && y == 0) adjPoints.add(new Point(1,1));
	    	if(x == 0 && y == 2) adjPoints.add(new Point(1,1));
	    	if(x == 2 && y == 0) adjPoints.add(new Point(1,1));
	    	if(x == 2 && y == 2) adjPoints.add(new Point(1,1));
	    	
	    	if(x == 1 && y == 1) { 
	    		adjPoints.add(new Point(0,0)); 
	    		adjPoints.add(new Point(0,2));
	    		adjPoints.add(new Point(2,0));
	    		adjPoints.add(new Point(2,2));
	    	}
	    	
	    	if(adjPoints.size() == 0) return null;
	    	return adjPoints;
	    }
	    
	    public Point adjNode(int p, int k)
	    {
	    	if(p >= 0 && p <= 2 && k >= 0 && k <=2 ){ // boudaries
	    		return new Point(p, k);
        	}
	    	return null;
	    }
	    
	    public List<Move> getValidMoves(Point turnPt)
	    {
	    	List<Move> validMoves = new ArrayList<Move>();
	    	//Move mv = new Move();
	    	List<Point> adjnodes = this.getAdjucentNodes(turnPt);
	    	for(Point pt : adjnodes){ // get all eligible moves from the from turnPt
	    		if( board[pt.x][pt.y] == 0){
	    			validMoves.add(new Move(turnPt, pt));
	    		} 	    		
	    	}	    		    		   
	    	return validMoves;	    	
	    }
	    public boolean isValidMove(int turn){
	    	List<Point> currentTurnPoints = new ArrayList<Point>();
	    	for (int i = 0; i < 3; ++i) {
	            for (int j = 0; j < 3; ++j) {
	                if (board[i][j] == turn) {
	                	currentTurnPoints.add(new Point(i, j));
	                }
	            }
	        }
	    	
	    	
	    	return false;
	    }

	public void placeAMove(ActionTakenBean playerAction, Move mv, int player) {

		Point fromPt = mv.fromPt;
		Point toPt = mv.toPt;
		if(fromPt == null){
			//System.out.println("placeAMove : From Pt is null, toPt = "+toPt + " , player = "+player);
			board[toPt.x][toPt.y] = player;   //player = 1 for X, 2 for O
			playerAction.setFromNd(null);
		}
		else{
			//System.out.println("placeAMove : fromPt = "+fromPt +", toPt = "+toPt +" , player = "+player);
			board[fromPt.x][fromPt.y] = 0; // hard coded to '0' and releasing the node
			board[toPt.x][toPt.y] = player;

		}
	}


	public void placeAMove(Move mv, int player) {
	    	
	    	Point fromPt = mv.fromPt;
	    	Point toPt = mv.toPt;
	    	if(fromPt == null){
	    		//System.out.println("placeAMove : From Pt is null, toPt = "+toPt + " , player = "+player);
	    		board[toPt.x][toPt.y] = player;   //player = 1 for X, 2 for O
	    	}
	    	else{
	    		//System.out.println("placeAMove : fromPt = "+fromPt +", toPt = "+toPt +" , player = "+player);
	    		board[fromPt.x][fromPt.y] = 0; // hard coded to '0' and releasing the node
	    		board[toPt.x][toPt.y] = player;
	    	}
	    }

	    public Move returnBestMove() {
	        int MAX = -100000;
	        int best = -1;
	        int MIN = 999999;
	        for (int i = 0; i < rootsChildrenScore.size(); ++i) {
	            if (MAX < rootsChildrenScore.get(i).score) {
	                MAX = rootsChildrenScore.get(i).score;
	                best = i;
	            }
	        	/*if (MIN > rootsChildrenScore.get(i).score) {  // @ TODO Comment if necessary
	                MIN = rootsChildrenScore.get(i).score;
	                best = i;
	            }*/
	        }
            Log.v("Board", "rootsChildrenScore.size() == "+rootsChildrenScore.size() +", best = "+best);
	        return rootsChildrenScore.get(best).move;
	    }

	    void takeHumanInput() {
	        System.out.println("Your move: FAker  ");
	        int x = scan.nextInt();
	        int y = scan.nextInt();
	        Point point = new Point(x, y);
	       // placeAMove(point, 2);
	    }

	    public void displayBoard() {
	        //System.out.println();
			Log.v("Board", "\n");
	        for (int i = 0; i < 3; ++i) {
	            for (int j = 0; j < 3; ++j) {
					Log.v("Board", board[i][j] + " ");
	            }
				Log.v("Board", "\n");

	        }
	    } 
	    
	    public boolean canDragTheInputOnBoard() {
	        
	    	int cnt = 0;
	        for (int i = 0; i < 3; ++i) {
	            for (int j = 0; j < 3; ++j) {
	                if (board[i][j] == 0){
	                	cnt ++;
	                }
	            }	            
	        }
	        if(cnt > 3) return false;
	        
	        return true;
	    } 
	    
	    public void resetBoard() {
	        for (int i = 0; i < 3; ++i) {
	            for (int j = 0; j < 3; ++j) {
	                board[i][j] = 0;
	            }
	        }
	    }

	public void setPoint(Point p, int player)
	{
		board[p.x][p.y] = player;
	}
}
