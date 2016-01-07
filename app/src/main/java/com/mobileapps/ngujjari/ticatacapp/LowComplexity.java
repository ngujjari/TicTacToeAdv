package com.mobileapps.ngujjari.ticatacapp;

import android.util.Log;

import com.mobileapps.ngujjari.ticatacapp.highcomplexity.Board;
import com.mobileapps.ngujjari.ticatacapp.highcomplexity.Move;
import com.mobileapps.ngujjari.ticatacapp.highcomplexity.Point;
import com.mobileapps.ngujjari.ticatacapp.highcomplexity.PointsAndScores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ngujjari on 12/19/15.
 */
public class LowComplexity extends TicTacAbstractController{

    private static final String TAG = "LowComplexity";

    Board bd = new Board();
    Map<Integer, Point> nodeMap = new HashMap<>();
    Map<Integer, Point> noMap = new HashMap<>();

    private void setBoard(){
        bd.resetBoard();
        nodeMap.put(1, new Point(1,1));
        nodeMap.put(2, new Point(0,2));
        nodeMap.put(3, new Point(1,2));
        nodeMap.put(4, new Point(2,2));
        nodeMap.put(5, new Point(2,1));
        nodeMap.put(6, new Point(2,0));
        nodeMap.put(7, new Point(1,0));
        nodeMap.put(8, new Point(0,0));
        nodeMap.put(9, new Point(0,1));

        for(Integer a : aList) // User Move
        {
            bd.setPoint((Point) nodeMap.get(a), 2);
        }
        for(Integer b : bList) // Computer move
        {
            bd.setPoint((Point)nodeMap.get(b), 1);
        }
    }

   /* @Override
    public ActionTakenBean predictUserinput(String player)
    {
        ActionTakenBean playerAction = new ActionTakenBean(player, null, null);
        setBoard();
        bd.uptoDepth=1;
        bd.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
        Move mv = bd.returnBestMove();
        Point fromPt = mv.getFromPt();
        Point toPt = mv.getToPt();
        if(fromPt != null ) playerAction.setFromNd(fromPt.getNode());
        if(toPt != null ) playerAction.setToNd(toPt.getNode());
        log(TAG, "Return playerAction == " + playerAction);
        return playerAction;
    }

    @Override
    public int predictUserinput(String inputType, String player) {
        setBoard();
        bd.uptoDepth=1;
        bd.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
        //bd.displayBoard();
        for (PointsAndScores pas : bd.rootsChildrenScore)
            Log.v(TAG, "Move: " + pas.getMove() + " Score: " + pas.getScore());
        Move mv = bd.returnBestMove();
        // Point fromPt = mv.getFromPt();
        Point toPt = mv.getToPt();
        return toPt.getNode();
    }*/

    @Override
    public ActionTakenBean predictUserinput(String player)  // Drag Drop
    {
        ActionTakenBean playerAction = new ActionTakenBean(player, null, null);
        /*setBoard();
        bd.uptoDepth=1;
        bd.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
        Move mv = bd.returnBestMove();
        Point fromPt = mv.getFromPt();
        Point toPt = mv.getToPt();*/
        playerAction = super.getNextStep(player);

        log(TAG, "Return playerAction == " + playerAction);
        return playerAction;
    }

    @Override
    public int predictUserinput(String inputType, String player) {  // Single Input
        setBoard();
        bd.uptoDepth=2;
        bd.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
        //bd.displayBoard();
        for (PointsAndScores pas : bd.rootsChildrenScore)
            Log.v(TAG, "Move: " + pas.getMove() + " Score: " + pas.getScore());
        Move mv = bd.returnBestMove();
        // Point fromPt = mv.getFromPt();
        Point toPt = mv.getToPt();
        return toPt.getNode();
    }
}
