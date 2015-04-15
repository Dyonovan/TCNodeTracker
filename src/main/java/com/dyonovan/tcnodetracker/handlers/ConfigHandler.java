package com.dyonovan.tcnodetracker.handlers;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    public static int arrowX, arrowY;
    public static boolean altArrow;
    public static double arrowSize;
    public static Configuration config;

    public static void init(File file) {
        if (config == null)
            config = new Configuration(file);
        config.load();

        arrowX      = config.get("Arrow Location", "XCoord", 0).getInt();
        arrowY      = config.get("Arrow Location", "YCoord", 0).getInt();

        altArrow    = config.get("Arrow Type", "Use Alt Arrow Texture", false).getBoolean();

        arrowSize   = config.get("Arrow Size", "Arrow Size 0-1", 1).getDouble();

        config.save();
    }
}
