package com.dyonovan.tcnodetracker.lib;

import java.util.Date;
import java.util.HashMap;

public class NodeList {

    public HashMap<String,Integer> aspect;
    public String type, mod;
    public int dim, x, y, z;
    public Date date;

    public NodeList(HashMap<String,Integer> aspect, int dim, String type, String mod, int x, int y, int z, Date date) {

        this.aspect = aspect;
        this.dim = dim;
        this.type = type;
        this.mod = mod;
        this.x = x;
        this.y = y;
        this.z = z;
        this.date = date;
    }

}
