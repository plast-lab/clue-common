package org.clyze.analysis

import static ResolverType.*

enum InputType {
    INPUT("input", [LOCAL_FILE, URL, MAVEN_ARTIFACT]),
    LIBRARY("library", [LOCAL_FILE, URL, MAVEN_ARTIFACT]),
    HPROF("heap dump", [LOCAL_FILE, URL, ZIP]),
    DYNAMIC("dynamic class", [LOCAL_FILE, ZIP])

    private final String title
    private final Set<ResolverType> resolvers

    InputType(String title, List<ResolverType> resolvers) {
        this.title = title
        this.resolvers = resolvers as Set
    }

    String toString() { title }

    Set<ResolverType> getResolverTypes() { resolvers }
}
