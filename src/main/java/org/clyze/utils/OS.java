package org.clyze.utils;

/**
 * A simple class to detect the operating system at runtime.
 */
public class OS {
    private OS() {}

    /** True on macOS. */
    public static final boolean macOS = System.getProperty("os.name").toLowerCase().contains("mac");
    /** True on Windows. */
    public static final boolean win   = System.getProperty("os.name").toLowerCase().contains("windows");
    /** True on Linux. */
    public static final boolean linux = System.getProperty("os.name").toLowerCase().contains("linux");
}
