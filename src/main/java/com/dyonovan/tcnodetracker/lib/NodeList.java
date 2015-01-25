package com.dyonovan.tcnodetracker.lib;

import java.util.HashMap;

public class NodeList {

    public HashMap<String,Integer> aspect;
    public String type;
    public int dim, x, y, z;

    public NodeList(HashMap<String,Integer> aspect, int dim, String type, int x, int y, int z) {

        this.aspect = aspect;
        this.dim = dim;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;

    }

}
