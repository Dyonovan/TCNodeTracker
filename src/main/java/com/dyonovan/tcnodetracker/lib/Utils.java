package com.dyonovan.tcnodetracker.lib;

import java.util.regex.Pattern;

public class Utils {

    public final static Pattern patternInvalidChars = Pattern.compile("[^a-zA-Z0-9_]");

    public static String invalidChars(String s) {

        return patternInvalidChars.matcher(s).replaceAll("_");
    }
}
