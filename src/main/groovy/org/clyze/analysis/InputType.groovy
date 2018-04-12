package org.clyze.analysis

import static ResolverType.*

public final enum InputType {
    INPUT("input", LOCAL_FILE, URL, MAVEN_ARTIFACT),
    LIBRARY("library", LOCAL_FILE, URL, MAVEN_ARTIFACT),
    HPROF("heap dump", LOCAL_FILE, URL, ZIP),
    DYNAMIC("dynamic class", LOCAL_FILE, ZIP)

    private final String title
    private final Set<ResolverType> resolvers

    public InputType(String title, ResolverType[] resolvers) {
        this.title = title
        this.resolvers = resolvers as Set
    }

    @Override
    public String toString() {
        return title
    }

    public Set<ResolverType> getResolverTypes() {
        return resolvers
    }
}
