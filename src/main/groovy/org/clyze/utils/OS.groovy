package org.clyze.utils

import groovy.transform.TypeChecked

@TypeChecked
class OS {
    static boolean macOS = System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0
}
