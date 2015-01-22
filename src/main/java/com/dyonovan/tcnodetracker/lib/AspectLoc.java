package com.dyonovan.tcnodetracker.lib;

public class AspectLoc {

    public int x, y, z, distance;
    public double hasAer, hasOrdo, hasTerra, hasPerdito, hasIgnis, hasAqua;

    public AspectLoc(int x, int y, int z, int distance, double hasAer, double hasAqua, double hasIgnis,
                     double hasOrdo, double hasPerdito, double hasTerra) {
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
