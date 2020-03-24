package org.clyze.utils

import groovy.transform.TypeChecked

@TypeChecked
class OS {
    static boolean macOS = System.getProperty("os.name").toLowerCase().contains("mac")
    static boolean win   = System.getProperty("os.name").toLowerCase().contains("windows")
}
