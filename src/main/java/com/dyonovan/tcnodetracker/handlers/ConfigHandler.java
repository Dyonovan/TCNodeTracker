package com.dyonovan.tcnodetracker.handlers;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class ConfigHandler {

    public static int arrowX, arrowY;
    public static Configuration config;

    public static void init(File file) {
        if (config == null)
            config = new Configuration(file);
        config.load();

        arrowX      = config.get("Arrow Location", "XCoord", 0).getInt();
        arrowY      = config.get("Arrow Location", "YCoord", 0).getInt();

        config.save();
    }
}
