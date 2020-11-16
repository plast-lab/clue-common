package org.clyze.analysis

import groovy.transform.CompileStatic

@CompileStatic
enum ResolverType {
    LOCAL_FILE, URL, MAVEN_ARTIFACT, ZIP
}
