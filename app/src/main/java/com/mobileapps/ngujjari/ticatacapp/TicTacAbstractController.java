package com.mobileapps.ngujjari.ticatacapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by ngujjari on 12/19/15.
 */
public  abstract class  TicTacAbstractController {

        private static final String TAG = "TicTacAbstractCntrl";

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

    protected void log(String tag, String msg)
    {
        Log.v(TAG,tag + "  ==  " + msg);
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


    public abstract ActionTakenBean predictUserinput(String player);
    public abstract int predictUserinput(String inputType, String player);


    protected boolean isValidMove(int fromNd, int toNd)
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

    protected List<Integer> remainedMovesList(Integer moveNd, List<Integer> tList)
    {
        Integer[] validNds = validMovesMap.get(moveNd);
        List<Integer> validList = new ArrayList<Integer>((List<Integer>) Arrays.asList(validNds));
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

                Set<Integer> setA = new HashSet(Arrays.asList(nodes));
                Set<Integer> setB = new HashSet(tList);
                setA.removeAll(setB);

                log(TAG,"Entered value is already exist, Please choose from " + setA.toString());
                msgList.add(MSG_1001);
                return returnVal;
            }
            returnVal = true;
            log(TAG,"Accepted !! "  + returnVal);
            return returnVal;
        }

        log(TAG, "Enter valid integer from 1,2,3,4,5,6,7,8,9");
        return returnVal;
    }



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




    public class Rank{
        public Integer rank;
        public List<List<Integer>> rankList;
        public Integer fromNd;
        public Integer toNd;

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



    public int getRandomNum()
    {
        Random rn = new Random();
        return rn.nextInt(9)+1;
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

            log(TAG + "execute(int a, int b) : ", " tList " + tList.size());
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

    public int validateInput(int a, String player, Set<Integer> abList, List<Integer> tList)
    {

        Set<Integer> setA = new HashSet(Arrays.asList(nodes));
        Set<Integer> setB = new HashSet(tList);
        setA.removeAll(setB);
        log(TAG,player+" : move pawn from "+abList.toString() +" to " + setA.toString() );


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
                msgList.add(TicTacAbstractController.MSG_1003);
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
                    msgList.add(TicTacAbstractController.MSG_1002);
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
                msgList.add(TicTacAbstractController.MSG_1002);
                return 1;
            }

        }
        catch(NumberFormatException e)
        {
            log(TAG,player+" : Invalid input enter again !!" );
            //takeInput(player, abList, tList);
            msgList.clear();
            msgList.add(TicTacAbstractController.MSG_1002);
            return 1;
        }

        // return Integer.parseInt(s);

        return 0;
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
                msgList.add(TicTacAbstractController.MSG_1003);
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
                    msgList.add(TicTacAbstractController.MSG_1002);
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
                    msgList.add(TicTacAbstractController.MSG_1002);
                }
                return 1;
            }

        }
        catch(NumberFormatException e)
        {
            log(TAG,player+" : Invalid input enter again !!" );
            //takeInput(player, abList, tList);
            msgList.clear();

            msgList.add(TicTacAbstractController.MSG_1002);
            return 1;
        }

        // return Integer.parseInt(s);

        return 0;
    }


}
