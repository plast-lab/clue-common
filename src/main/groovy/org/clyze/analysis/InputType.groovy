package org.clyze.analysis

enum InputType {
    INPUT("input", [ResolverType.LOCAL_FILE, ResolverType.URL, ResolverType.MAVEN_ARTIFACT]),
    LIBRARY("library", [ResolverType.LOCAL_FILE, ResolverType.URL, ResolverType.MAVEN_ARTIFACT]),
    PLATFORM("platform", [ResolverType.LOCAL_FILE, ResolverType.URL, ResolverType.ZIP]),
    HEAPDL("heap dump", [ResolverType.LOCAL_FILE, ResolverType.URL, ResolverType.ZIP]),
    MISC("misc", [ResolverType.LOCAL_FILE, ResolverType.URL]),
    MISC_FILE("misc (file only)", [ResolverType.LOCAL_FILE]),

    private final String title
    private final Set<ResolverType> resolvers

    InputType(String title, List<ResolverType> resolvers) {
        this.title = title
        this.resolvers = resolvers as Set
    }

    String toString() { title }

    Set<ResolverType> getResolverTypes() { resolvers }
}
