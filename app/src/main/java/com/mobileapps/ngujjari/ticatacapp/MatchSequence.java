package com.mobileapps.ngujjari.ticatacapp;

/**
 * Created by ngujjari on 1/4/16.
 */

        import android.util.Log;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Collections;
        import java.util.Comparator;
        import java.util.HashMap;
        import java.util.HashSet;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Random;
        import java.util.Scanner;
        import java.util.Set;
        import java.util.TreeSet;

public class MatchSequence implements Runnable   {

    private static final String TAG = "MatchSequence";

    int totalNodes = 9;
    static Integer nodes[] = {1,2,3,4,5,6,7,8,9};
    static int winNodes[][] = {{2,3,4},{4,5,6},{6,7,8},{8,9,2},{2,1,6},{8,1,4},{7,1,3},{9,1,5}};
    static Integer adjNodes[][] = {{2,3,4,5,6,7,8,9},{9,1,3},{2,1,4},{3,1,5},{4,1,6},{5,1,7},{6,1,8},{7,1,9},{8,1,2}};
    int pn = 1;
    int npn[] = {2,3,4,5,6,7,8,9};
    Set<Integer> aList = new HashSet<Integer>();
    Set<Integer> bList = new HashSet<Integer>();
    List<Integer> tList = new ArrayList<Integer>();
    static HashMap<Integer, Integer[]> validMovesMap = new HashMap<Integer, Integer[]>();
    HashMap<String, ActionTakenBean> actionTakenBeanHashMap = new HashMap<String, ActionTakenBean>();
    static List<List<Integer>> winNodesList = new ArrayList<List<Integer>>();

    String player = "";
    String previousPlayer = "";
    boolean isWon = false;
    boolean flipPlayer = true;
    boolean actionSuccess = false;
    boolean dragStatus = false;
    boolean level2 = true;

    public static final String MSG_1001 = "Entered value is already exist";

    public static final String MSG_1002 = "Enter drag drop values correctly ....";
    public static final String MSG_1003 = "Select TO Node";

    List<String> msgList = new ArrayList<String>();

    private void log(String tag, String msg)
    {
        Log.v(TAG, tag + "  ==  " + msg);
        // System.out.println(tag + "  ==  " + msg);
    }
    static
    {
        for(int i = 1; i <=9 ; i++)
        {
            validMovesMap.put(i, adjNodes[i-1]);
        }

        for(int i = 0; i <8 ; i++)
        {
            List<Integer> nodes = new ArrayList<Integer>();
            for(int j= 0 ; j< winNodes[i].length; j++)
            {
                nodes.add(winNodes[i][j]);
            }
            winNodesList.add(nodes);
        }

    }
    private boolean isWon(String player, Set<Integer> checkList)
    {
        boolean isWon = false;
        for (int ma = 0; ma < winNodes.length; ma++) {
            List<Integer> tempList = new ArrayList<Integer>();
            for (int mb = 0; mb < winNodes[ma].length; mb++) {
                int p1 = winNodes[ma][mb];
                //  log(TAG,p1);
                tempList.add(new Integer(p1));
            }
            if(checkList.containsAll(tempList))
            {
                isWon = true;
                // log(TAG, "onClick method Begin   Button ID : " + v.getId());
                log(TAG,player + " WON the GAME !!!!!!!!!!!!!! number are : " + tempList.toString());
                System.out.println(player + " WON the GAME !!!!!!!!!!!!!! number are : " + tempList.toString());
            }

        }

        return isWon;

    }

    private int takeInput(String player)
    {
        Scanner in = new Scanner(System.in);
        log(TAG,player+" : Enter an integer in 1,2,3,4,5,6,7,8,9" );
        String s = in.nextLine();
        if(s.equals("e") || s.equals("exit"))
        {
            return -1;
        }
        else{
            return Integer.parseInt(s);
        }
        // int a = in.nextInt();

        // return a;
    }

    private boolean isValidMove(int fromNd, int toNd)
    {

        Integer[] vlu = validMovesMap.get(fromNd);
        // log(TAG,"vlu == "+vlu);
        for(int k = 0; k < vlu.length; k++)
        {
            if(toNd==vlu[k])
            {
                return true;
            }
        }

        return false;
    }

    private List<Integer> remainedMovesList(Integer moveNd, List<Integer> tList)
    {
        Integer[] validNds = validMovesMap.get(moveNd);
        List<Integer> validList = new ArrayList<Integer>((List<Integer>)Arrays.asList(validNds));
        validList.removeAll(tList);

        return validList;
    }

    private String remainedMoves(Integer moveNd, List<Integer> tList)
    {
        Integer[] validNds = validMovesMap.get(moveNd);
        List<Integer> validList = new ArrayList<Integer>((List<Integer>)Arrays.asList(validNds));
        validList.removeAll(tList);

        return validList.toString();
    }
    /*private int takeInput(String player, Set<Integer> abList, List<Integer> tList)
    {
        Scanner in = new Scanner(System.in);
        Set<Integer> setA = new HashSet(Arrays.asList(nodes));
        Set<Integer> setB = new HashSet(tList);
        setA.removeAll(setB);
        log(TAG,player+" : move pawn from "+abList.toString() +" to " + setA.toString() );
        String s = in.nextLine();
        if(s.equals("e") || s.equals("exit"))
        {
            return -1;
        }
        else{
            StringTokenizer tokens = new StringTokenizer(s, ",");

            try{
                int fromNd = Integer.parseInt(tokens.nextToken());
                int toNd = Integer.parseInt(tokens.nextToken());

                log(TAG,player+" : moved pawn from "+fromNd +" to " + toNd);

                if(abList.contains(new Integer(fromNd)) && setA.contains(new Integer(toNd)))
                {
                    log(TAG,"Valid input");

                    if(!isValidMove(fromNd, toNd))
                    {

                        log(TAG,"Invalid Move !! valid moves are .. "+abList.toArray()[0] +" -> " + remainedMoves((Integer)abList.toArray()[0],tList)
                                + " , "+abList.toArray()[1] +" -> "+remainedMoves((Integer)abList.toArray()[1],tList)
                                + " , "+abList.toArray()[2] +" -> "+remainedMoves((Integer)abList.toArray()[2],tList));
                        //takeInput(player, abList, tList);
                        return 1;
                    }
                    abList.remove(new Integer(fromNd));
                    abList.add(new Integer(toNd));
                }
                else
                {
                    log(TAG,player+" : InValid input try again !!");
                    //takeInput(player, abList, tList);
                    return 1;
                }

            }
            catch(NumberFormatException e)
            {
                log(TAG,player+" : Invalid input enter again !!" );
                //takeInput(player, abList, tList);
                return 1;
            }

            // return Integer.parseInt(s);
        }
        return 0;
    }
*/
    private boolean validateInput(int a)
    {
        List<Integer> allNodes = Arrays.asList(nodes);
        boolean returnVal = false;
        log(TAG," validateInput begin input = " + a);
        if(allNodes.contains(new Integer(a)))
        {
            if(tList.contains(new Integer(a)) && tList.size() < 6)
            {
                returnVal = false;
                // Integer tListArray[] = (Integer[])tList.toArray();
                Set<Integer> setA = new HashSet(Arrays.asList(nodes));
                Set<Integer> setB = new HashSet(tList);
                setA.removeAll(setB);
                // List<Integer> tempArray = ((List)allNodes).clone();
                log(TAG,"Entered value is already exist, Please choose from " + setA.toString());
                msgList.add(MSG_1001);
                return returnVal;
            }
            returnVal = true;
            log(TAG,"Accepted !! "  + returnVal);
            return returnVal;
        }

        log(TAG,"Enter valid integer from 1,2,3,4,5,6,7,8,9" );
        return returnVal;
    }

    // Take the input until user enter exit or e
	/*
    public void runAlg()
    {
        int a = 1;
        String player = "Player1";
        boolean isWon = false;
        // a = this.takeInput();
        while( a != -1 && !isWon)
        {

            if(tList.size() < 6)
            {
                a = this.takeInput(player);
                if(!validateInput(a))
                {
                    continue;
                }
                 log(TAG,"You entered number "+a);

                if(player.equals("Player1"))
                {
                    aList.add(new Integer(a));
                    //  log(TAG,"Player1 array = "+aList.toArray().toString());
                    isWon = isWon("Player1",aList);
                }
                else
                {
                    bList.add(new Integer(a));
                    isWon = isWon("Player2",bList);
                }

                tList.clear();
                tList.addAll(aList);
                tList.addAll(bList);



            }
            else
            {
                 log(TAG,"Lets play the game !!! !!!!");
                Set<Integer> abList =  player.equals("Player2") ? bList : aList;
                a = 1;
                while(a == 1){
                    a = this.takeInput(player, abList, tList);
                }
                isWon = player.equals("Player2") ? isWon("Player2",bList) : isWon("Player1",aList);
                tList.clear();
                tList.addAll(aList);
                tList.addAll(bList);

            }
            player = player.equals("Player2") ? "Player1" : "Player2";
        }
         log(TAG,"You exited the program ");



    }*/

    public boolean validateInputA(int a)
    {

        if(tList.size() < 6) {
            // a = this.takeInput(player);
            if (!validateInput(a)) {
                actionSuccess = false;
                return actionSuccess;
            }
            actionSuccess = true;
            log(TAG,"You entered number " + a);
            //  return actionSuccess;
        }else
        {

        }
        return actionSuccess;
    }

    private void takeInput(int a, String player)
    {
        ActionTakenBean playerAction = actionTakenBeanHashMap.get(player);

        if(playerAction != null)
        {
            String playerIn = playerAction.getPlayer();
            Integer fromNd = playerAction.getFromNd();
            Integer toNd = playerAction.getToNd();
            if(fromNd != null && !fromNd.equals(new Integer(-1)))
            {
                playerAction.setToNd(a);
            }
            else {
                playerAction.setFromNd(a);
            }
        }
        else
        {
            actionTakenBeanHashMap.put(player, new ActionTakenBean(player, a, -1));
        }

    }
    public int validateInput(int a, int b, String player, Set<Integer> abList, List<Integer> tList)
    {

        Set<Integer> setA = new HashSet(Arrays.asList(nodes));
        Set<Integer> setB = new HashSet(tList);
        setA.removeAll(setB);
        log(TAG,player+" : move pawn from "+abList.toString() +" to " + setA.toString() );



        //   StringTokenizer tokens = new StringTokenizer(s, ",");

        try{
            ActionTakenBean playerAction = actionTakenBeanHashMap.get(player);
            if(playerAction == null) playerAction = new ActionTakenBean(player, -1, -1);
            playerAction.reset(player);
            takeInput(a, player);
            takeInput(b, player);
            playerAction = actionTakenBeanHashMap.get(player);
            int fromNd = playerAction.getFromNd();
            int toNd = playerAction.getToNd();

            // int fromNd = Integer.parseInt(tokens.nextToken());
            // int toNd = Integer.parseInt(tokens.nextToken());
            log(TAG,"From  Node = "+fromNd + " To Node = "+toNd);

            if(toNd < 0)
            {
                log(TAG,"SELECT TO Node ........................");
                dragStatus = true;
                msgList.clear();
                msgList.add(MatchSequence.MSG_1003);
                return 1;
            }
            else if(fromNd == toNd) {
                return 0;
            }
            else if(abList.contains(new Integer(fromNd)) && setA.contains(new Integer(toNd)))
            {
                log(TAG,"Valid input");

                if(!isValidMove(fromNd, toNd))
                {

                    log(TAG,"Invalid Move !! valid moves are .. "+abList.toArray()[0] +" -> " + remainedMoves((Integer)abList.toArray()[0],tList)
                            + " , "+abList.toArray()[1] +" -> "+remainedMoves((Integer)abList.toArray()[1],tList)
                            + " , "+abList.toArray()[2] +" -> "+remainedMoves((Integer)abList.toArray()[2],tList));
                    //takeInput(player, abList, tList);
                    msgList.clear();
                    playerAction.reset(player);
                    msgList.add(MatchSequence.MSG_1002);
                    dragStatus = true;
                    return 1;
                }
                abList.remove(new Integer(fromNd));
                abList.add(new Integer(toNd));
                msgList.clear();
                dragStatus = false;
            }
            else
            {
                log(TAG,player+" : InValid input try again !!");
                //takeInput(player, abList, tList);
                msgList.clear();
                if(fromNd != toNd) {
                    msgList.add(MatchSequence.MSG_1002);
                }
                return 1;
            }

        }
        catch(NumberFormatException e)
        {
            log(TAG,player+" : Invalid input enter again !!" );
            //takeInput(player, abList, tList);
            msgList.clear();

            msgList.add(MatchSequence.MSG_1002);
            return 1;
        }

        // return Integer.parseInt(s);

        return 0;
    }

    public int validateInput(int a, String player, Set<Integer> abList, List<Integer> tList)
    {

        Set<Integer> setA = new HashSet(Arrays.asList(nodes));
        Set<Integer> setB = new HashSet(tList);
        setA.removeAll(setB);
        log(TAG,player+" : move pawn from "+abList.toString() +" to " + setA.toString() );



        //   StringTokenizer tokens = new StringTokenizer(s, ",");

        try{

            takeInput(a, player);
            ActionTakenBean playerAction = actionTakenBeanHashMap.get(player);
            int fromNd = playerAction.getFromNd();
            int toNd = playerAction.getToNd();

            // int fromNd = Integer.parseInt(tokens.nextToken());
            // int toNd = Integer.parseInt(tokens.nextToken());
            log(TAG,"From  Node = "+fromNd + " To Node = "+toNd);

            if(toNd < 0)
            {
                log(TAG,"SELECT TO Node ........................");
                dragStatus = true;
                msgList.clear();
                msgList.add(MatchSequence.MSG_1003);
                return 1;
            }
            else if(abList.contains(new Integer(fromNd)) && setA.contains(new Integer(toNd)))
            {
                log(TAG,"Valid input  fromNd "+fromNd + "  toNd =  "+toNd);
                if(fromNd == toNd)
                {
                    return 0;
                }
                else if(!isValidMove(fromNd, toNd))
                {

                    log(TAG,"Invalid Move !! valid moves are .. "+abList.toArray()[0] +" -> " + remainedMoves((Integer)abList.toArray()[0],tList)
                            + " , "+abList.toArray()[1] +" -> "+remainedMoves((Integer)abList.toArray()[1],tList)
                            + " , "+abList.toArray()[2] +" -> "+remainedMoves((Integer)abList.toArray()[2],tList));
                    //takeInput(player, abList, tList);
                    msgList.clear();
                    playerAction.reset(player);
                    msgList.add(MatchSequence.MSG_1002);
                    dragStatus = true;
                    return 1;
                }
                abList.remove(new Integer(fromNd));
                abList.add(new Integer(toNd));
                msgList.clear();
                dragStatus = false;
            }
            else
            {
                log(TAG,player+" : InValid input try again !!");
                //takeInput(player, abList, tList);
                msgList.clear();
                msgList.add(MatchSequence.MSG_1002);
                return 1;
            }

        }
        catch(NumberFormatException e)
        {
            log(TAG,player+" : Invalid input enter again !!" );
            //takeInput(player, abList, tList);
            msgList.clear();
            msgList.add(MatchSequence.MSG_1002);
            return 1;
        }

        // return Integer.parseInt(s);

        return 0;
    }

    public boolean execute(int a)
    {

        //int a = 1;
        msgList.clear();
        actionSuccess = false;
        if(tList.size() < 6)
        {
            // a = this.takeInput(player);

            if(!validateInput(a))
            {
                actionSuccess = false;
                return actionSuccess;
            }
            log(TAG +"execute(int a) : ", player +" You entered number "+a);

            if(player.equals("Player1"))
            {
                aList.add(new Integer(a));
                //  log(TAG,"Player1 array = "+aList.toArray().toString());
                System.out.println("execute(int a) : "+ player +" You entered number "+a  +"  array : "+aList.toString());
                isWon = isWon("Player1",aList);
            }
            else
            {
                bList.add(new Integer(a));
                System.out.println("execute(int b) : "+ player +" You entered number "+a  +"  array : "+bList.toString());
                isWon = isWon("Player2",bList);
            }

            tList.clear();
            tList.addAll(aList);
            tList.addAll(bList);



        }
        else
        {
            log(TAG + "execute(int a) : ", "Lets play the game !!! !!!!");

            Set<Integer> abList =  player.equals("Player2") ? bList : aList;
            int validateInputReturn = validateInput(a, player, abList, tList );
            if(validateInputReturn != 0)
            {
                actionSuccess = false;
                log(TAG +"execute(int a) : "," Msg Size:  " + msgList.size()  );
                // msgList.add(MSG_1002);
                return actionSuccess;
            }

            isWon = player.equals("Player2") ? isWon("Player2",bList) : isWon("Player1",aList);
            tList.clear();
            actionTakenBeanHashMap.clear();
            tList.addAll(aList);
            tList.addAll(bList);

        }
        actionSuccess = true;
        // player = player.equals("Player2") ? "Player1" : "Player2";
        return actionSuccess;
    }

    public boolean execute(int a, int b)
    {

        //int a = 1;
        actionSuccess = false;
        if(tList.size() < 6)
        {
            msgList.clear();
            //msgList.add("execute: INvalid move !!");

            log(TAG +"execute(int a, int b) : "," tList "+tList.size());
        }
        else
        {
            log(TAG +"execute(int a, int b) : ","Lets play the game !!! !!!!");
            msgList.clear();
            Set<Integer> abList =  player.equals("Player2") ? bList : aList;
            int validateInputReturn = validateInput(a,b, player, abList, tList );
            if(validateInputReturn != 0)
            {
                actionSuccess = false;
                log(TAG +"execute(int a, int b) : "," Msg Size:  " + msgList.size()  );
                // msgList.add(MSG_1002);
                return actionSuccess;
            }
            System.out.println(player +"  == "+abList +" moved from "+a +"  to "+b);
            isWon = player.equals("Player2") ? isWon("Player2",bList) : isWon("Player1",aList);
            tList.clear();
            actionTakenBeanHashMap.clear();
            tList.addAll(aList);
            tList.addAll(bList);

        }
        actionSuccess = true;
        // player = player.equals("Player2") ? "Player1" : "Player2";
        return actionSuccess;
    }


    /*
        // Take the input until user enter exit or e
        public void runAlg()
        {
            int a = 0;

            boolean isWon = false;
            int fromNd = -1;
            int toNd = -1;
            boolean executeStep = false;
            // a = this.takeInput();
            this.player = "Player1";
            while( a != -1 && !isWon)
            {

                if(tList.size() < 6)
                {
                    toNd =  predictUserinput("singleNd", player);
                    if(toNd == -1){
                        toNd = getRandomNum();
                    }

                    log(TAG, "runAlg toNd before execute == "+ toNd +" "+" from palyer = "+player);
                    executeStep = this.execute(toNd);
                }
                else
                {
                    log(TAG, "runAlg Lets play the game !!! !!!!  player = " +player);
                    ActionTakenBean userInput =  predictUserinput(player);
                    executeStep = this.execute(userInput.getFromNd(), userInput.getToNd());
                    //break;

                }
                if(this.isWon == true) {
                    log(TAG, "runAlg aList: "+aList +"   bList: "+ bList);
                    break;
                }
                if(executeStep == true){
                    this.flipPlayer = true;
                    this.player = this.player.equals("Player2") ? "Player1" : "Player2";
                }

            }
            log(TAG, "runAlg: You exited the program ");

        }

    */
    public <T> List<T> twoDArrayToList(T[][] twoDArray) {
        List<T> list = new ArrayList<T>();
        for (T[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
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

    public class Rank{
        public Integer rank;
        public List<List<Integer>> rankList;
        public Integer fromNd;
        public Integer toNd;

    }


    public ActionTakenBean predictUserinput(String player)
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

        log(TAG, "Return playerAction == "+playerAction);
        return playerAction;
    }

    public ActionTakenBean predictUserinputOld(String player)
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

        List<Integer> validTargetNdsList = new ArrayList<Integer>((List<Integer>)Arrays.asList(nodes));
        validTargetNdsList.removeAll(tList);

        // aList , tList, validMoves, remainedMoves
        for(Integer fromNd : abList){
            List<Integer>  remainedMoves = this.remainedMovesList(fromNd, tList);
            Set<Integer> aListOtherNds = new HashSet<Integer>(abList);
            aListOtherNds.remove(fromNd);
            rankList.addAll(calculateRank(fromNd, aListOtherNds, remainedMoves));
        }
        int rankVal = -1;
        Rank finalrank = null;
        List<List<Integer>> returnRankNds = new ArrayList<List<Integer>>();
        int currentRank = rank(abList, returnRankNds);
        log(TAG, "currentRank === "+currentRank);
        for(Rank rank : rankList){
            if(rank.rank > rankVal){
                rankVal = rank.rank;
                finalrank = rank;
            }
        }
        log(TAG, "finalrank === "+finalrank +"  , "+finalrank.rank);
        if(finalrank != null && finalrank.rank >= currentRank){

            for(List<Integer> rankListOP : finalrank.rankList)
            {
                for(Integer toNd : rankListOP){ // calculate nd which not there in aList
                    for(Integer fromNd : abList){
                        log(TAG, "finalrank ===  finalrank.fromNd "+finalrank.fromNd +" fromNd = "+fromNd +"  , toNd = "+toNd);
                        if(fromNd.intValue() != toNd.intValue() && validTargetNdsList.contains(toNd.intValue()) && isValidMove(fromNd.intValue(), toNd.intValue()) ){
                            if((!otherPlayerWon(player, fromNd))) {
                                log(TAG, "Return finalrank ===  finalrank.fromNd = "+finalrank.fromNd +"  , toNd = "+toNd);
                                playerAction.setFromNd(finalrank.fromNd);
                                playerAction.setToNd(toNd);
                                break;
                            }
                        }
                    }
                }
            }
            //
        }


        log(TAG, "playerAction == "+playerAction);
        if(playerAction.getToNd() == null || playerAction.getToNd() <1){

            log(TAG, playerAction.getPlayer() +" == "+playerAction.getFromNd() + "TO Node NOT Found !!!!!!! PLEASE VERIFY ###############");
            for(Integer fromNd : abList){
                List<Integer>  remainedMoves = this.remainedMovesList(fromNd, tList);
                for(Integer mv : remainedMoves){
                    if(validTargetNdsList.contains(mv) && isValidMove(fromNd, mv) ){
                        if((!otherPlayerWon(player, fromNd))) {
                            playerAction.setFromNd(fromNd);
                            playerAction.setToNd(mv);
                            break;
                        }
                    }
                }

            }

        }
        Set<Integer> validNds = new HashSet<Integer>();
        for(Integer nd : validTargetNdsList){
            validNds.add(nd);
        }
        checkOtherPlayerRank(player, playerAction, validNds, playerAction.getFromNd()); // -1 since single input - no from node
        log(TAG, "Return playerAction == "+playerAction);
        return playerAction;
    }

    private boolean otherPlayerWon(String player, Integer fromNd)
    {

        boolean returnVal = false;
        Set<Integer> abList = null;
        String localPlayer = "";
        if(player.equals("Player1")){
            abList = bList;
            localPlayer = "Player2";
        }
        else{
            abList = aList;
            localPlayer = "Player1";
        }


        if(abList.size() > 1) // dragging
        {
            for (Integer selectedNd : abList) {
                Set<Integer> targetList = new HashSet<Integer>(abList);
                targetList.remove(selectedNd);
                targetList.add(fromNd);
                if (isWon(localPlayer, targetList)) {
                    log(TAG, "otherPlayerWon  true , Take other From  !!!!!!!!!!!!!!!! fromNd = " +fromNd);
                    returnVal = true;
                }
            }
        }

        log(TAG, "check otherPlayerWon == "+fromNd +" returnVal = "+returnVal);

        return returnVal;
    }

    public int predictUserinput(String inputType, String player)
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
/*
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
            */
        }
        //log(TAG, "calculateRank  === nextMv=  "+nextMv +" rank = "+rank ); //+"  returnRankNds= "+ ((ArrayList)returnRankNds.toArray()[0]).toArray().toString());


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
    public int pridict(List<Integer> tList , Set<Integer> abPrevList, Set<Integer> validNbrs)
    {
        int returnNbr = -1;
        int[][] rankArray = null;
        log(TAG +"  pridict : ","abPrevList size " + abPrevList.size());
        if(abPrevList.size() > 0){
            Iterator<Integer> abNumIter = abPrevList.iterator();
            int fromNd = abNumIter.next();
            int[][] rank1Array = compareRank(winNodes, fromNd);
            rankArray = rank1Array;

            if(rankArray != null && rankArray.length > 0) // rank array no's should match validNbrs
            {
                int[][] toArray = new int [rankArray.length+1][];
                int idx = 0;
                for(int i = 0 ; i< rankArray.length; i++)
                {
                    int[] arr = rankArray[i];
                    if(arr != null) {
                        for(int j = 0 ; j< arr.length; j++)
                        {
                            for(Integer validNbr : validNbrs){
                                if(validNbr.intValue() == arr[j]) // get 1 rank list
                                {
                                    toArray[idx] = arr;
                                    idx++;
                                }
                            }
                        }
                    }
                }
                if(idx > 0){
                    rankArray = toArray;
                }

            }
            log(TAG+"  pridict : ","rankArray1 " + rankArray);

            if(rankArray != null && rankArray.length > 0) // rank array no's should match validNbrs
            {

                if(abNumIter.hasNext() && rankArray.length > 0){
                    int fromNd2 = abNumIter.next();
                    int[][] rank2Array = compareRank(rankArray, fromNd2);
                    rankArray = rank2Array;
                }
            }

        }





        log(TAG+"  pridict : ","rankArrayFinal " + Arrays.toString(rankArray));
        ArrayList<Integer> predictArr = new ArrayList<Integer>();
        if(rankArray != null && rankArray.length > 0) // rank array no's should match validNbrs
        {
            for(int i = 0 ; i< rankArray.length; i++)
            {
                int[] arr = rankArray[i];
                log(TAG+"  pridict : ", i +" = arr " + Arrays.toString(arr) +" validNbrs == "+validNbrs.toString());
                if(arr != null){
                    for(int j = 0 ; j< arr.length-1; j++)
                    {
                        boolean found = false;
                        for(Integer validNbr : validNbrs){
                            if(validNbr.intValue() != arr[j]) // get 1 rank list
                            {
                                continue;
                            }
                            else
                            {
                                //predictArr[idx] = arr[j];

                                found = true;
                                break;
                            }
                        }

                        for(Integer abPrevListNbr : abPrevList){
                            if(abPrevListNbr.intValue() == arr[j]) // get 1 rank list
                            {
                                continue;
                            }
                            else
                            {
                                if(found == true){
                                    predictArr.add(arr[j]);

                                }
                            }
                        }
                    }
                }
            }

            log(TAG+"  pridict : ","predictArr " + predictArr.toString());


        }
        if(predictArr != null && predictArr.size() > 0)
        {
            returnNbr = randomValueArray(predictArr);
        }
        else
        {
            log(TAG+"pridict : "," No PREDICTION ; PICK from VAlid list  " + Arrays.toString(validNbrs.toArray()));
            returnNbr = randomValueArray(validNbrs.toArray());
        }



        log(TAG+"pridict : ","returnNbr ======= " + returnNbr);
        return returnNbr;
    }

    private int[][] compareRank(int[][] fromArray, int fromNd)
    {
        int[][] toArray = new int [fromArray.length+1][];
        int idx = 0;
        for(int i = 0 ; i< fromArray.length; i++)
        {
            int[] arr = fromArray[i];
            if(arr != null){
                for(int j = 0 ; j< arr.length; j++)
                {
                    if(fromNd == arr[j]) // get 1 rank list
                    {
                        toArray[idx] = arr;
                        idx++;
                    }
                }
            }
        }

        return toArray;
    }

    public static  int randomValueArray(List<Integer> values) {
        int[] target = new int[10];
        int j = 0;
        for(Integer i : values){
            if(i.intValue() > 0){
                target[j] = i.intValue();
                j++;
            }
        }
        int index = (int)Math.round(Math.random() * (j-1));
        //log(TAG, j+" randomValueArray index======== "+index);
        return target[index];
    }

    public static  int randomValueArray(Object[] values) {
        int[] target = new int[9];
        int j = 0;
        for(int i=0; i < values.length; i++){
            if((Integer)values[i] > 0){
                target[j] = (Integer)values[i];
                j++;
            }
        }
        int index = (int)Math.round(Math.random() * (j-1));
        //log(TAG, j+" randomValueArray index======== "+index);
        return target[index];
    }

    public static  int randomValue(int... values) {
        int[] target = new int[9];
        int j = 0;
        for(int i=0; i < values.length; i++){
            if(values[i] > 0){
                target[j] = values[i];
                j++;
            }
        }
        int index = (int)Math.round(Math.random() * (j-1));
        //log(TAG, (j-1)+" index======== "+index);
        return target[index];
    }

    public int getRandomNum()
    {
        Random rn = new Random();
        return rn.nextInt(9)+1;
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
    		/* if(rank3Cnt > 1){
    			 List<Integer>  mostLikelyNds = new ArrayList<Integer>();
    			 for(MyRank abR : abRanksList){



		                 for(Integer tempFrom : abR.aList)
		                 {
		                	 List<Integer>  tempTListNds = new ArrayList<Integer>();
			                 tempTListNds.addAll(abR.aList);
			                 tempTListNds.addAll(abR.bList);

			                 List<Integer>  myTempremainedMoves = this.remainedMovesList(tempFrom, tempTListNds);
			                 Set<Integer> aTempListOtherNds = new HashSet<Integer>(abR.aList);
			                 aTempListOtherNds.remove(tempFrom);
			                 for(Integer nextTempMv : myTempremainedMoves)
			                 {
			                	 Set<Integer> tempNds = new HashSet<Integer>();
			                	 tempNds.add(nextTempMv);
			                	 tempNds.addAll(aTempListOtherNds);
			                     List<List<Integer>> returnTempRankNds = new ArrayList<List<Integer>>();
			                     int temprank = this.rank(tempNds, returnTempRankNds);
			                     this.log(TAG, "abcalculateRank second level === abfromNd = "+tempFrom +" abnextMv=  "+nextTempMv +" abrank = "+temprank  +"  returnRankNds= "+ ((ArrayList)returnTempRankNds.toArray()[0]).toArray().toString());

			                     tempTListNds.clear();
				                 tempTListNds.addAll(tempNds);
				                 tempTListNds.addAll(abR.bList);
				                 boolean rank3Rec = false;
				                 for(Integer tempABRFrom : abR.bList){
					                 List<Integer>  myABRTempremainedMoves = this.remainedMovesList(tempABRFrom, tempTListNds);
					                 Set<Integer> aTempABRListOtherNds = new HashSet<Integer>(abR.bList);
					                 aTempABRListOtherNds.remove(tempABRFrom);
					                 for(Integer nextABRTempMv : myABRTempremainedMoves)
					                 {
					                	 Set<Integer> tempABRNds = new HashSet<Integer>();
					                	 tempABRNds.add(nextABRTempMv);
					                	 tempABRNds.addAll(aTempABRListOtherNds);
					                     List<List<Integer>> returnABRTempRankNds = new ArrayList<List<Integer>>();
					                     int tempABRrank = this.rank(tempABRNds, returnABRTempRankNds);
					                     this.log(TAG, "abcalculateRank  === tempABRFrom = "+tempABRFrom +" abnextMv=  "+nextABRTempMv +" tempABRrank = "+tempABRrank  +"  returnRankNds= "+ ((ArrayList)returnTempRankNds.toArray()[0]).toArray().toString());
					                     if(tempABRrank == 3) rank3Rec = true;

					                 }

				                 }
				                 if(!rank3Rec == true){ // Not find any rank 3 records , that should be ur most bet
				                	 mostLikelyNds.add(nextTempMv);
				                 }
			                 }

		                 }
    			 }
    			 // Begin
    			 for(MyRank r : tRanksList){

        			 for(MyRank abR : r.bbRanks){
        				 if(abR.rank == 3 ){ // Other Player about to win. STOP it if possible.
			    			 for(Integer fromNd : bList)
					    	 {
					             List<Integer>  myremainedMoves = this.remainedMovesList(fromNd, this.tList);
					             if(fromNd.equals(abR.fromNd)&& myremainedMoves.contains(abR.toNd)
					            		 && mostLikelyNds.contains(abR.toNd))
					             {

					            	 playerAction.setFromNd(fromNd);
			    	    			 playerAction.setToNd(abR.toNd);
			    	    			 this.log(TAG, "Main  === HIT AB Second level multiple rank = "+playerAction );
			    	    			 targetFound = true;
			    	    			 break;
					             }
					    	 }
        				 }
        			 }
    			 }
    			 // End

    		 }else */
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
                        /*
                        for (MyRank r : tRanksList) {
                            r.xxCnt = r.xxRanks.size();

                            if (leastXXCnt == -1) {
                                leastXXCnt = r.xxCnt;
                                playerAction.setFromNd(r.fromNd);
                                playerAction.setToNd(r.toNd);
                                playerSelected = true;
                                if(notAllowedNodesList.contains(playerAction)){
                                    playerSelected = false;
                                    leastXXCnt = -1;
                                    this.log(TAG, " Filter this node 2....... " + playerAction + " targetFound = "+targetFound);
                                    continue;

                                }
                                continue;
                            }
                            else if (r.xxCnt > leastXXCnt) {
                                playerSelected = false;
                                continue;
                            } else {
                                playerAction.setFromNd(r.fromNd);
                                playerAction.setToNd(r.toNd);
                                playerSelected = true;
                                this.log(TAG, "Main  === HIT BB Second level rank , select lease XX nodes = " + playerAction);
                                // break; wait until all iterations...
                                if(notAllowedNodesList.contains(playerAction)){
                                    this.log(TAG, " Filter this node ....... " + playerAction + " targetFound = "+targetFound);
                                    playerSelected = false;
                                    continue;

                                }
                            }
                        }
                        targetFound = true;
                        */
                }

                this.log(TAG, "getNextStep6 = " + playerAction + " targetFound = "+targetFound);
                /*
                    for(MyRank r : tRanksList){

                        for(MyRank abR : r.bbRanks){
                            if(abR.rank == 3 ){ // Other Player about to win. STOP it if possible.
                                for(Integer fromNd : bList)
                                {
                                    List<Integer>  myremainedMoves = this.remainedMovesList(fromNd, this.tList);
                                    if(myremainedMoves.contains(r.toNd))
                                    {

                                        playerAction.setFromNd(fromNd);
                                        playerAction.setToNd(r.toNd);
                                        this.log(TAG, "Main  === HIT AB Second level rank = " + playerAction);
                                        targetFound = true;
                                        break;
                                    }
                                }
                            }
                        }
                    } */
            }
        }

        this.log(TAG, "Main  === playerAction = " + playerAction);

        // this.execute(1, 2);
        //log(TAG, this.getRandomNum());

        return playerAction;
    }



    @Override
    public void run() {
        // TODO Auto-generated method stub
        this.execute(1);
    }
}
