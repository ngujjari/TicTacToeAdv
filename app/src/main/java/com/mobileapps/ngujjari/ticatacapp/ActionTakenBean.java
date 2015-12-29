package com.mobileapps.ngujjari.ticatacapp;

/**
 * Created by ngujjari on 2/21/15.
 */
public class ActionTakenBean {
    private String player;

    private Integer fromNd;
    private Integer toNd;

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            ActionTakenBean actionTakenBean = (ActionTakenBean) object;
            if (this.player == actionTakenBean.getPlayer()
                    && this.fromNd == actionTakenBean.getFromNd() && this.toNd == actionTakenBean.getToNd()
                    && this.player.equals(actionTakenBean.getPlayer())
                    && this.fromNd.equals(actionTakenBean.getFromNd()) && this.toNd.equals(actionTakenBean.getToNd())) {
                result = true;
            }
        }
        return result;
    }

    // just omitted null checks
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 7 * hash + this.player.hashCode();
        hash = 7 * hash + this.fromNd.hashCode();
        hash = 7 * hash + this.toNd.hashCode();
        return hash;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    private Integer rank;

    public Integer getRank() {
        return rank;
    }

    public ActionTakenBean(String player, Integer fromNd, Integer toNd)
    {
        this.player = player;
        this.fromNd = fromNd;
        this.toNd = toNd;
        this.rank = -1;
    }

    public void reset(String player)
    {
        this.player = player;
        this.fromNd = -1;
        this.toNd = -1;
        this.rank = -1;
    }
    public Integer getFromNd() {
        return fromNd;
    }

    public void setFromNd(Integer fromNd) {
        this.fromNd = fromNd;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Integer getToNd() {
        return toNd;
    }

    public void setToNd(Integer toNd) {
        this.toNd = toNd;
    }

    public String toString()
    {
        return "player == "+player +" fromNd== "+fromNd+" toNd== "+toNd;
    }



}
