package com.mobileapps.ngujjari.ticatacapp;

import java.util.List;

/**
 * Created by ngujjari on 9/11/15.
 */
public class Rank {

    public Integer getId() {
        return id;
    }

    public List<Object> getNodes() {
        return nodes;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNodes(List<Object> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", nodes=" + nodes +
                '}';
    }

    private Integer id;
    private List<Object> nodes;
}
