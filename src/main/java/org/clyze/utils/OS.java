package org.clyze.utils;

class OS {
    public static final boolean macOS = System.getProperty("os.name").toLowerCase().contains("mac");
    public static final boolean win   = System.getProperty("os.name").toLowerCase().contains("windows");
    public static final boolean linux = System.getProperty("os.name").toLowerCase().contains("linux");
}
