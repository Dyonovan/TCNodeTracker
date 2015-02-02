package com.dyonovan.tcnodetracker.lib;

public class AspectLoc {

    public int x, y, z, distance, dimID;
    public int hasAer, hasOrdo, hasTerra, hasPerdito, hasIgnis, hasAqua;
    public String type;

    public AspectLoc(int x, int y, int z, int dimID, int distance, String type, int hasAer, int hasAqua, int hasIgnis,
                     int hasOrdo, int hasPerdito, int hasTerra) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = dimID;
        this.distance = distance;
        this.type = type;
        this.hasAer = hasAer;
        this.hasAqua = hasAqua;
        this.hasIgnis = hasIgnis;
        this.hasOrdo = hasOrdo;
        this.hasPerdito = hasPerdito;
        this.hasTerra = hasTerra;
    }
}
