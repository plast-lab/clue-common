package org.clyze.utils

import groovy.transform.TypeChecked

@TypeChecked
class OS {
    static final boolean macOS = System.getProperty("os.name").toLowerCase().contains("mac")
    static final boolean win   = System.getProperty("os.name").toLowerCase().contains("windows")
    @SuppressWarnings('unused')
    static final boolean linux = System.getProperty("os.name").toLowerCase().contains("linux")
}
