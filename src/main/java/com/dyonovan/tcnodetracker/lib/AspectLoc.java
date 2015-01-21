package com.dyonovan.tcnodetracker.lib;

public class AspectLoc {

    public int x, y, z;
    public boolean hasAer, hasOrdo, hasTerra, hasPerdito, hasIgnis, hasAqua;

    public AspectLoc(int x, int y, int z, boolean hasAer, boolean hasAqua, boolean hasIgnis,
                           boolean hasOrdo, boolean hasPerdito, boolean hasTerra) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.hasAer = hasAer;
        this.hasAqua = hasAqua;
        this.hasIgnis = hasIgnis;
        this.hasOrdo = hasOrdo;
        this.hasPerdito = hasPerdito;
        this.hasTerra = hasTerra;
    }
}
