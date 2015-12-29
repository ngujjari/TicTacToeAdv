package com.mobileapps.ngujjari.ticatacapp;

/**
 * Created by ngujjari on 11/12/15.
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyRank {

    public Integer rank;
    public Integer fromNd;
    public Integer toNd;
    public Set<Integer> aList = new HashSet<Integer>();
    public Set<Integer> bList = new HashSet<Integer>();

    public List<MyRank> xxRanks = new ArrayList<MyRank>();
    public List<MyRank> abRanks = new ArrayList<MyRank>();
    public List<MyRank> bbRanks = new ArrayList<MyRank>();

    public int xxTobbRank3Cnt = 0;
    public int xxCnt = 0;
}
