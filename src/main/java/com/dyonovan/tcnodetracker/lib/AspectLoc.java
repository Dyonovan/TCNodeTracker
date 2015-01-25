package com.dyonovan.tcnodetracker.lib;

public class AspectLoc {

    public int x, y, z, distance;
    public int hasAer, hasOrdo, hasTerra, hasPerdito, hasIgnis, hasAqua;

    public AspectLoc(int x, int y, int z, int distance, int hasAer, int hasAqua, int hasIgnis,
                     int hasOrdo, int hasPerdito, int hasTerra) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.distance = distance;
        this.hasAer = hasAer;
        this.hasAqua = hasAqua;
        this.hasIgnis = hasIgnis;
        this.hasOrdo = hasOrdo;
        this.hasPerdito = hasPerdito;
        this.hasTerra = hasTerra;
    }
}
