package com.dyonovan.tcnodetracker.lib;

import java.util.HashMap;

public class NodeList {

    public HashMap aspect;
    public String type;
    public int x, y, z;

    public NodeList(HashMap aspect, String type, int x, int y, int z) {

        this.aspect = aspect;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;

    }

}
