package com.mobileapps.ngujjari.ticatacapp;

import android.util.Log;

import com.mobileapps.ngujjari.ticatacapp.highcomplexity.Board;
import com.mobileapps.ngujjari.ticatacapp.highcomplexity.Move;
import com.mobileapps.ngujjari.ticatacapp.highcomplexity.Point;
import com.mobileapps.ngujjari.ticatacapp.highcomplexity.PointsAndScores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ngujjari on 12/19/15.
 */
public class MediumComplexity extends TicTacAbstractController{

    private static final String TAG = "MediumComplexity";

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

    @Override
    public ActionTakenBean predictUserinput(String player) // Low Complex  Drag Drop
    {
        ActionTakenBean playerAction = new ActionTakenBean(player, null, null);
        setBoard();
        bd.uptoDepth=2;
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
    public int predictUserinput(String inputType, String player) { // Single Input
        setBoard();
        bd.uptoDepth=4;
        bd.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
        //bd.displayBoard();
        for (PointsAndScores pas : bd.rootsChildrenScore)
            Log.v(TAG, "Move: " + pas.getMove() + " Score: " + pas.getScore());
        Move mv = bd.returnBestMove();
        // Point fromPt = mv.getFromPt();
        Point toPt = mv.getToPt();
        return toPt.getNode();
    }

   // @Override
    public ActionTakenBean predictUserinput1(String player)
    {
        boolean hit = false;
        int returnVal = -1;
        ActionTakenBean playerAction = new ActionTakenBean(player, null, null);
        List<Rank> rankList = new ArrayList<Rank>();
        Set<Integer> abList = null;
        if(player.equals("Player1")){
            abList = aList;
        }
        else{
            abList = bList;
        }

        playerAction = this.getNextStep(player);

        log(TAG, "Return playerAction == " + playerAction);
        return playerAction;
    }

    //@Override
    public int predictUserinput1(String inputType, String player)
    {
        boolean hit = false;
        int returnVal = -1;
        if(inputType.equals("singleNd"))
        {
            int a = -1;

            while(!hit){
                if(a < 0) a = getRandomNum();

                if(tList.size() == 0) { hit = true; returnVal = a;}

                Set<Integer> setA = new HashSet(Arrays.asList(nodes));
                Set<Integer> setB = new HashSet(tList);
                setA.removeAll(setB);

                if(tList.contains(new Integer(a)) && tList.size() < 6)
                {

                    log(TAG+"predictUserinput(String inputType, String player) ","Entered value "+ a +" is already exist, Please choose from " + setA.toString());
                    a = randomValueArray(setA.toArray());
                    log(TAG+"predictUserinput(String inputType, String player) ","Returned value is "+ a +" , from " + setA.toString());
                    continue;

                }


                ActionTakenBean  actionTakenBean = (player.equals("Player1")) ? predictSingleInput("Player1", setA) : predictSingleInput("Player2", setA) ;
                returnVal = (actionTakenBean.getToNd() != null) ? actionTakenBean.getToNd().intValue() : -1 ;

              /*  if(player.equals("Player1")){
                    returnVal = pridict(this.tList, this.aList, setA);
                }
                else{
                    returnVal = pridict(this.tList, this.bList, setA);
                }*/

                if(returnVal < 1) { returnVal = a; }

                if(returnVal > 0)hit = true;
            }
        }
        return returnVal;
    }

    public ActionTakenBean predictSingleInput(String player, Set<Integer> validNds)
    {
        boolean hit = false;
        int returnVal = -1;
        ActionTakenBean playerAction = new ActionTakenBean(player, null, null);
        List<Rank> rankList = new ArrayList<Rank>();
        Set<Integer> abList = null;
        if(player.equals("Player1")){
            abList = aList;
        }
        else{
            abList = bList;
        }

        if(abList.size() > 0)
        {
            for (Integer fromNd : abList) {  // @TODO modify to check for max rank from all remained moves
                List<Integer> remainedMoves = this.remainedMovesList(fromNd, tList);
                Set<Integer> aListOtherNds = new HashSet<Integer>(abList);
                aListOtherNds.remove(fromNd);
                rankList.addAll(calculateRank(fromNd, aListOtherNds, remainedMoves));
            }


            int rankVal = 3;
            Rank finalrank = null;
            List<List<Integer>> returnRankNds = new ArrayList<List<Integer>>();
            int currentRank = rank(abList, returnRankNds);
            log(TAG, "predictSingleInput currentRank === " + currentRank +"  rankList size: "+rankList.size());
            if(rankList.size() > 0){
                Collections.sort(rankList, new Comparator<Rank>() {
                    @Override
                    public int compare(Rank rank, Rank t1) {
                        return rank.rank.intValue() > t1.rank.intValue() ? -1 : 1;
                    }
                });
            }

            for (Iterator<Rank> rankIte = rankList.iterator(); rankIte.hasNext(); ) {
                Rank rank = rankIte.next();
                // if (rank.rank == rankVal) {
                rankVal = rank.rank;
                finalrank = rank;
                log(TAG, "predictSingleInput finalrank.rank === " + finalrank.rank);
                if (finalrank != null && finalrank.rank >= currentRank) {
                    //playerAction.setFromNd(finalrank.fromNd);
                    for (List<Integer> rankListOP : finalrank.rankList) {
                        for (Integer toNd : rankListOP) { // calculate nd which not there in aList
                            log(TAG, toNd + " == "+abList.contains(toNd)+ " predictSingleInput isValidSingleInput === " + isValidSingleInput(validNds, toNd));
                            if ((!abList.contains(toNd)) && isValidSingleInput(validNds, toNd)) {
                                playerAction.setToNd(toNd);
                                playerAction.setRank(finalrank.rank);
                                break;
                            }

                        }
                    }
                    //
                }
                // }

                if (playerAction.getToNd() != null && playerAction.getToNd().intValue() > 0) break; // Come out of the loop
                if (rankIte.hasNext() == false) { // last object in the list, decrement the rankVal
                    rankVal--;
                }
            }



        }else{ // first selection
            playerAction.setToNd(Integer.valueOf(randomValueArray(validNds.toArray())));
            playerAction.setRank(-1);
        }

        checkOtherPlayerRank(player, playerAction, validNds, -1); // -1 since single input - no from node
        if(playerAction.getToNd() == null || playerAction.getToNd().intValue() == -1){
            if(validNds.contains(1)){
                playerAction.setToNd(1);
                log(TAG, "predictSingleInput set dafault 1 nd ....Return playerAction == "+playerAction);
            }
        }

        log(TAG, "predictSingleInput Return playerAction == "+playerAction);
        return playerAction;
    }

    private boolean isValidSingleInput(Set<Integer> validNbrs, Integer nd)
    {
        if(validNbrs.contains(nd)){
            return true;
        }

        return false;
    }


    private void checkOtherPlayerRank(String currentPlayer, ActionTakenBean cPlayerAction, Set<Integer> validNds, Integer fromNd)
    {
        Set<Integer> abList = null;

        if(currentPlayer.equals("Player1")){
            abList = bList;  // set otherPlayer
        }
        else{
            abList = aList;
        }
        log(TAG, "checkOtherPlayerRank abList "+ abList );// currentRank === " + currentRank +"  rankList size: "+returnRankNds.size());
        List<List<Integer>> returnRankNds = new ArrayList<List<Integer>>();
        int currentRank = rank(abList, returnRankNds);
        if(currentRank == 2){
            List<Integer> returnNds = new ArrayList<Integer>();

            for(List<Integer> rankNds : returnRankNds){

                for (Integer rankNd : rankNds) {
                    if (!abList.contains(rankNd)) {
                        if(fromNd == null || fromNd.intValue() < 0) {
                            returnNds.add(rankNd);
                        }else{
                            if(fromNd.intValue() != rankNd.intValue() && isValidMove(fromNd.intValue(), rankNd.intValue())){
                                returnNds.add(rankNd);

                            }
                        }
                    }
                }

            }
            log(TAG, "checkOtherPlayerRank returnNds " + returnNds + "  , validNds = " + validNds);
            for (Integer returnNd : returnNds){
                if(validNds.contains(returnNd)){
                    cPlayerAction.setToNd(returnNd);
                    log(TAG, "checkOtherPlayerRank returnNd " + returnNd );
                    return;
                }
            }

        }

        if(cPlayerAction != null && cPlayerAction.getRank() != null && cPlayerAction.getRank().intValue() < currentRank){

            log(TAG, "checkOtherPlayerRank currentRank === " + currentRank +"  rankList size: "+returnRankNds.size());
            Integer toNd = cPlayerAction.getToNd();
            if(fromNd != null && fromNd.intValue() > 0){

            }

        }


    }
    private List<Rank> calculateRank(Integer fromNd, Set<Integer> aListOtherNds, List<Integer>  remainedMoves)
    {
        List<Rank> rankReturnList = new ArrayList<Rank>();
        for(Integer nextMv : remainedMoves)
        {
            //Set<Integer> targetNds = new HashSet<Integer>(aListOtherNds);
            Set<Integer> targetNds = new HashSet<Integer>();
            targetNds.add(nextMv);
            targetNds.addAll(aListOtherNds);
            List<List<Integer>> returnRankNds = new ArrayList<List<Integer>>();
            int rank = rank(targetNds, returnRankNds);
            log(TAG, "calculateRank  === fromNd = "+fromNd +" nextMv=  "+nextMv +" rank = "+rank ); //+"  returnRankNds= "+ ((ArrayList)returnRankNds.toArray()[0]).toArray().toString());
            if(rank > -1) {
                Rank rankC = new Rank();
                rankC.fromNd = fromNd;
                rankC.rank = rank;
                returnRankNds.clear();
                List newArry = new ArrayList<Integer>();
                newArry.add(nextMv);
                returnRankNds.add(newArry);
                rankC.rankList = returnRankNds;
                rankReturnList.add(rankC);
            }
        }
        return rankReturnList;
    }




    private int rank(Set<Integer> targetNds, List<List<Integer>> returnRank)
    {
        List<List<Integer>> rank1Nds = new ArrayList<List<Integer>>();
        List<List<Integer>> rank2Nds = new ArrayList<List<Integer>>();
        List<List<Integer>> rank3Nds = new ArrayList<List<Integer>>();

        for(Integer nd : targetNds)
        {
            for(List<Integer> nodes : winNodesList){
                if(nodes.contains(nd)){
                    rank1Nds.add(nodes);
                }
            }
        }
        for(List<Integer> rk1nodes : rank1Nds){
            int rank2Cnt = 0;
            for(Integer nd : targetNds)
            {
                if(rk1nodes.contains(nd)){
                    rank2Cnt++;
                    if(rank2Cnt == 2)rank2Nds.add(rk1nodes);
                }
            }
        }

        for(List<Integer> rk2nodes : rank2Nds){
            int rank3Cnt = 0;
            for(Integer nd : targetNds)
            {
                if(rk2nodes.contains(nd)){
                    rank3Cnt++;
                    if(rank3Cnt == 3)rank3Nds.add(rk2nodes);
                }
            }
        }

        if(rank3Nds.size() > 0) { returnRank.addAll(rank3Nds); return 3;};
        if(rank2Nds.size() > 0) { returnRank.addAll(rank2Nds); return 2;};
        if(rank1Nds.size() > 0) { returnRank.addAll(rank1Nds); return 1;};

        return -1;
    }



    public ActionTakenBean getNextStep(String player) {


        // TODO Auto-generated method stub
        //int[] fromNds = {7, 4, 2};
        ActionTakenBean playerAction = new ActionTakenBean(player, null, null);

    	/*List<Integer> remainedMoves7 = new ArrayList<Integer>();
    	remainedMoves7.add(6);

    	List<Integer> remainedMoves4 = new ArrayList<Integer>();
    	remainedMoves4.add(3);

    	List<Integer> remainedMoves2 = new ArrayList<Integer>();
    	remainedMoves2.add(9);
    	remainedMoves2.add(3);*/
        //TestMatchSequence ms = new TestMatchSequence();
        //List <List<Integer>> remainedMoves = new ArrayList<List<Integer>>();

        //this.aList.add(7); this.aList.add(4); this.aList.add(2);
        //this.bList.add(8); this.bList.add(5); this.bList.add(1);
        //  this.aList.add(2); this.aList.add(8); this.aList.add(1);
        //this.bList.add(9); this.bList.add(3); this.bList.add(6);

        //this.tList.addAll(this.aList);
        //this.tList.addAll(this.bList);
        Set<Integer> bList = this.bList;
        // A [7, 4, 2]
        // B [8, 5, 1]
        // X [3, 6, 9]

        Set<ActionTakenBean> notAllowedNodesList = new HashSet<ActionTakenBean>();
        List<MyRank> tRanksList = new ArrayList<MyRank>();
        for(Integer fromNd : bList)
        {
            List<Integer>  myremainedMoves = this.remainedMovesList(fromNd, this.tList);
            Set<Integer> bListOtherNds = new HashSet<Integer>(bList);
            bListOtherNds.remove(fromNd);
            for(Integer nextMv : myremainedMoves)
            {
                Set<Integer> tempABListNds = new HashSet<Integer>();
                tempABListNds.add(nextMv);
                tempABListNds.addAll(bListOtherNds);
                List<List<Integer>> returnRankNds = new ArrayList<List<Integer>>();
                int rank = this.rank(tempABListNds, returnRankNds);
                this.log(TAG, "calculateRank  === fromNd = " + fromNd + " nextMv=  " + nextMv + " rank = " + rank + "  returnRankNds= " + ((ArrayList) returnRankNds.toArray()[0]).toArray().toString());

                /*****SET RANK *****/
                MyRank myRank = new MyRank();
                myRank.bList = tempABListNds; // Not sure. Ignore . should not be used.
                myRank.aList = this.aList;
                myRank.fromNd = fromNd;
                myRank.toNd = nextMv;
                myRank.rank = rank;

                if(rank != 3)
                {
                    List<Integer>  tempTListNds = new ArrayList<Integer>();
                    tempTListNds.addAll(tempABListNds);
                    tempTListNds.addAll(this.aList);

                    for(Integer tempFrom : this.aList){
                        List<Integer>  myTempremainedMoves = this.remainedMovesList(tempFrom, tempTListNds);
                        Set<Integer> aTempListOtherNds = new HashSet<Integer>(this.aList);
                        aTempListOtherNds.remove(tempFrom);
                        for(Integer nextTempMv : myTempremainedMoves)
                        {
                            Set<Integer> tempNds = new HashSet<Integer>();
                            tempNds.add(nextTempMv);
                            tempNds.addAll(aTempListOtherNds);
                            List<List<Integer>> returnTempRankNds = new ArrayList<List<Integer>>();
                            int temprank = this.rank(tempNds, returnTempRankNds);
                            this.log(TAG, "xxcalculateRank  === xxfromNd = " + tempFrom + " xxnextMv=  " + nextTempMv + " xxrank = " + temprank + "  returnRankNds= " + ((ArrayList) returnTempRankNds.toArray()[0]).toArray().toString());

                            MyRank myXXRank = new MyRank();
                            myXXRank.aList = tempNds;
                            myXXRank.bList = tempABListNds;
                            myXXRank.fromNd = tempFrom;
                            myXXRank.toNd = nextTempMv;
                            myXXRank.rank = temprank;



                            // Compute second level ranks for B nodes after A or XX are moved
                            List<Integer>  tempAATListNds = new ArrayList<Integer>();
                            tempAATListNds.addAll(tempNds);
                            tempAATListNds.addAll(tempABListNds);

                            for(Integer tempbFrom : tempABListNds){
                                List<Integer>  myBTempremainedMoves = this.remainedMovesList(tempbFrom, tempAATListNds);
                                Set<Integer> aTempBListOtherNds = new HashSet<Integer>(tempABListNds);
                                aTempBListOtherNds.remove(tempbFrom);
                                for(Integer nextBTempMv : myBTempremainedMoves)
                                {
                                    Set<Integer> tempBNds = new HashSet<Integer>();
                                    tempBNds.add(nextBTempMv);
                                    tempBNds.addAll(aTempBListOtherNds);

                                    List<List<Integer>> returnTempBRankNds = new ArrayList<List<Integer>>();
                                    int tempBrank = this.rank(tempBNds, returnTempBRankNds);
                                    this.log(TAG, "bbcalculateRank  === bbfromNd = " + tempbFrom + " nextBTempMv=  " + nextBTempMv + " tempBrank = " + tempBrank + "  returnRankNds= " + ((ArrayList) returnTempRankNds.toArray()[0]).toArray().toString());

                                    MyRank myABRank = new MyRank();
                                    myABRank.bList = tempBNds;
                                    myABRank.aList = tempNds;
                                    myABRank.fromNd = tempbFrom;
                                    myABRank.toNd = nextBTempMv;
                                    myABRank.rank = tempBrank;
                                    myXXRank.bbRanks.add(myABRank);   // bbRank is a sub list in xxRank
                                }

                            } // bbRank End (Current Next ranks)

                            myRank.xxRanks.add(myXXRank);

                        }

                    } // xxRank End

                    tempTListNds.clear();
                    tempTListNds.addAll(tempABListNds);
                    tempTListNds.addAll(this.aList);

                    for(Integer tempFrom : tempABListNds){
                        List<Integer>  myTempremainedMoves = this.remainedMovesList(tempFrom, tempTListNds);
                        Set<Integer> aTempListOtherNds = new HashSet<Integer>(tempABListNds);
                        aTempListOtherNds.remove(tempFrom);
                        for(Integer nextTempMv : myTempremainedMoves)
                        {
                            Set<Integer> tempNds = new HashSet<Integer>();
                            tempNds.add(nextTempMv);
                            tempNds.addAll(aTempListOtherNds);
                            List<List<Integer>> returnTempRankNds = new ArrayList<List<Integer>>();
                            int temprank = this.rank(tempNds, returnTempRankNds);
                            this.log(TAG, "abcalculateRank  === abfromNd = " + tempFrom + " abnextMv=  " + nextTempMv + " abrank = " + temprank + "  returnRankNds= " + ((ArrayList) returnTempRankNds.toArray()[0]).toArray().toString());

                            MyRank myABRank = new MyRank();
                            myABRank.bList = tempABListNds;
                            myABRank.aList = this.aList;
                            myABRank.fromNd = tempFrom;
                            myABRank.toNd = nextTempMv;
                            myABRank.rank = temprank;
                            myRank.abRanks.add(myABRank);
                        }

                    } // abRank End (Current Next ranks)
                } // rank != 3
                tRanksList.add(myRank);
            }
        } // end for

        boolean targetFound = false;
        for(MyRank r : tRanksList){
            if(r.rank == 3){  // Check Current player , can win in the next step
                playerAction.setFromNd(r.fromNd);
                playerAction.setToNd(r.toNd);
                this.log(TAG, "Main  === HIT AB First level rank = " + playerAction);
                targetFound = true;
                break;
            }
        }

        if(!targetFound) {
            for(MyRank r : tRanksList){

                for(MyRank xxR : r.xxRanks){
                    if(xxR.rank == 3 && !targetFound){ // Other Player about to win. STOP it if possible.

                        for(Integer fromNd : bList)
                        {
                            List<Integer>  myremainedMoves = this.remainedMovesList(fromNd, this.tList);
                            if(myremainedMoves.contains(xxR.toNd))
                            {
                                playerAction.setFromNd(fromNd);
                                playerAction.setToNd(xxR.toNd);
                                this.log(TAG, "Main  === HIT XX First level rank = " + playerAction);
                                targetFound = true;
                                break;
                            }
                        }

                    }
                }
            }
        }

        if(!targetFound) {
            int rank3Cnt = 0;
            List<MyRank> abRanksList = new ArrayList<MyRank>();
            for(MyRank r : tRanksList){

                for(MyRank xxR : r.xxRanks){
                    for(MyRank bbR : xxR.bbRanks) {
                        if (bbR.rank == 3) { // Other Player about to win. STOP it if possible.
                            rank3Cnt++;
                            abRanksList.add(bbR);
                        }
                    }
                }
            }

            this.log(TAG, "getNextStep4 = " + playerAction + " targetFound = "+targetFound);
            if(!targetFound) {
                // Choose B node with , with lower A or xx options.
                // Chose B node with A or XX nodes having only winning nodes (Rank 3). If not don't choose ex. Rank 3 B nodes in XX  along with other B nodes

                for(MyRank r : tRanksList) {

                    for (MyRank xxR : r.xxRanks) {

                        if(xxR.rank == 3){
                            ActionTakenBean notAllowedBean = new ActionTakenBean(player, r.fromNd, r.toNd);
                            this.log(TAG, "Not Allowed Node  = " + notAllowedBean);
                            notAllowedNodesList.add(notAllowedBean);
                        }
                        Set<Integer> fromSet = new HashSet<Integer>();
                        for (MyRank bbR : xxR.bbRanks) {
                            fromSet.add(bbR.fromNd);
                            if(bbR.rank == 3){

                                xxR.xxTobbRank3Cnt++;
                            }
                        }
                        if(fromSet.size() == 1 && fromSet.contains(new Integer(1))){
                            ActionTakenBean notAllowedBean = new ActionTakenBean(player, r.fromNd, r.toNd);
                            this.log(TAG, "Not Allowed Node  = " + notAllowedBean);
                            notAllowedNodesList.add(notAllowedBean);
                        }
                    }

                }


                for(MyRank r : tRanksList) {
                    if(r.xxRanks.size() == 1 && r.xxRanks.get(0).xxTobbRank3Cnt > 0){
                        playerAction.setFromNd(r.fromNd);
                        playerAction.setToNd(r.toNd);
                        this.log(TAG, "Main  === HIT BB Second level rank , One XXNd with rank3 bbNd = " + playerAction);
                        if(notAllowedNodesList.contains(playerAction)){
                            this.log(TAG, " Filter this node 1....... " + playerAction + " targetFound = "+targetFound);
                            continue;
                        }
                        targetFound = true;
                        break;
                    }
                }
                if(!targetFound)
                {
                    this.log(TAG, "getNextStep5 = " + playerAction + " targetFound = "+targetFound);
                    int leastXXCnt = -1;
                    // MyRank tempR = null;
                    boolean playerSelected = false;
                    TreeSet<Integer> rankSizeSet = new TreeSet<Integer>();
                    for (MyRank r : tRanksList) {
                        rankSizeSet.add(r.xxRanks.size());

                    }

                    for (Integer xxsize : rankSizeSet){
                        if(!playerSelected) {
                            for (MyRank r : tRanksList) {
                                if (xxsize == r.xxRanks.size() && xxsize.equals(r.xxRanks.size())) {
                                    playerAction.setFromNd(r.fromNd);
                                    playerAction.setToNd(r.toNd);
                                    this.log(TAG, " Selecting playerAction ....... " + playerAction + " targetFound = " + targetFound + "  xxsize = "+xxsize);
                                    playerSelected = true;
                                    if (notAllowedNodesList.contains(playerAction)) {
                                        playerSelected = false;
                                        leastXXCnt = -1;
                                        this.log(TAG, " Filter this node 2....... " + playerAction + " targetFound = " + targetFound);
                                        continue;

                                    }
                                    break;
                                }
                            }
                        }
                    }
                    targetFound = true;

                }

                this.log(TAG, "getNextStep6 = " + playerAction + " targetFound = "+targetFound);

            }
        }

        this.log(TAG, "Main  === playerAction = " + playerAction);

        // this.execute(1, 2);
        //log(TAG, this.getRandomNum());

        return playerAction;
    }

}
