package com.dyonovan.tcnodetracker.lib;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class AspectLoc {

    public int x, y, z, distance, dimID;
    public int hasAer, hasOrdo, hasTerra, hasPerdito, hasIgnis, hasAqua;
    public String type;
    public HashMap<String, Integer> compound;
    public Date date;

    public AspectLoc(int x, int y, int z, int dimID, Date date, int distance, String type, int hasAer, int hasAqua, int hasIgnis,
                     int hasOrdo, int hasPerdito, int hasTerra, HashMap<String, Integer> compound) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = dimID;
        this.date = date;
        this.distance = distance;
        this.type = type;
        this.hasAer = hasAer;
        this.hasAqua = hasAqua;
        this.hasIgnis = hasIgnis;
        this.hasOrdo = hasOrdo;
        this.hasPerdito = hasPerdito;
        this.hasTerra = hasTerra;
        this.compound = compound;
    }

    public static Comparator<AspectLoc> getDistComparator() {
        return new Comparator<AspectLoc>() {
            @Override
            public int compare(AspectLoc o1, AspectLoc o2) {
                return o1.distance - o2.distance;
            }
        };
    }

    public static Comparator<AspectLoc> getAerComparator() {
        return new Comparator<AspectLoc>() {
            @Override
            public int compare(AspectLoc o1, AspectLoc o2) {
                return o1.hasAer - o2.hasAer;
            }
        };
    }

    public static Comparator<AspectLoc> getAquaComparator() {
        return new Comparator<AspectLoc>() {
            @Override
            public int compare(AspectLoc o1, AspectLoc o2) {
                return o1.hasAqua - o2.hasAqua;
            }
        };
    }

    public static Comparator<AspectLoc> getIgnisComparator() {
        return new Comparator<AspectLoc>() {
            @Override
            public int compare(AspectLoc o1, AspectLoc o2) {
                return o1.hasIgnis - o2.hasIgnis;
            }
        };
    }

    public static Comparator<AspectLoc> getOrdoComparator() {
        return new Comparator<AspectLoc>() {
            @Override
            public int compare(AspectLoc o1, AspectLoc o2) {
                return o1.hasOrdo - o2.hasOrdo;
            }
        };
    }

    public static Comparator<AspectLoc> getPerdComparator() {
        return new Comparator<AspectLoc>() {
            @Override
            public int compare(AspectLoc o1, AspectLoc o2) {
                return o1.hasPerdito - o2.hasPerdito;
            }
        };
    }

    public static Comparator<AspectLoc> getTerraComparator() {
        return new Comparator<AspectLoc>() {
            @Override
            public int compare(AspectLoc o1, AspectLoc o2) {
                return o1.hasTerra - o2.hasTerra;
            }
        };
    }
}
