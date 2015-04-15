package com.dyonovan.tcnodetracker.handlers;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static int arrowX, arrowY;

    public static void init(Configuration config) {

        config.load();

        arrowX      = config.get("Arrow Location", "XCoord", 0).getInt();
        arrowY      = config.get("Arrow Location", "YCoord", 0).getInt();

        config.save();
    }
}
