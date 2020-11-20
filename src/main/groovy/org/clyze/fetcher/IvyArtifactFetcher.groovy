package org.clyze.fetcher

import org.apache.ivy.Ivy
import org.apache.ivy.core.IvyContext
import org.apache.ivy.plugins.resolver.BintrayResolver
import org.apache.ivy.plugins.resolver.DependencyResolver
import org.apache.ivy.plugins.resolver.URLResolver
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.apache.ivy.core.report.ArtifactDownloadReport
import org.apache.ivy.core.report.ResolveReport
import org.apache.ivy.core.resolve.ResolveOptions
import org.apache.ivy.plugins.parser.xml.XmlModuleDescriptorWriter

class IvyArtifactFetcher implements ArtifactFetcher {

    Artifact fetch(String id, Repo repo, boolean ignoreSources) {

        String[] dep = id.split(":")
        if (dep.size() < 2) {
            throw new RuntimeException("Invalid artifact '${id}', should be of the form group:name:version")
        }

        Ivy defaultIvy = Ivy.newInstance()
        defaultIvy.configureDefault()

        ResolveReport report = (ResolveReport) Ivy.newInstance(defaultIvy.getSettings()).execute(new Ivy.IvyCallback() {
            @Override
            Object doInIvyContext(Ivy ivy, IvyContext context) {

                DependencyResolver resolver
                if (repo == Repo.MAVEN_CENTRAL ) {
                    resolver = new URLResolver()
                    resolver.setM2compatible(true)
                    resolver.addArtifactPattern('http://repo1.maven.org/maven2/[organisation]/[module]/[revision]/[artifact](-[revision]).[ext]')
                }
                else {
                    resolver = new BintrayResolver()
                }
                resolver.setName('crawler')
                ivy.getSettings().addResolver(resolver)
                ivy.getSettings().setDefaultResolver('crawler')

                File ivyfile = File.createTempFile('ivy', '.xml')
                ivyfile.deleteOnExit()

                DefaultModuleDescriptor md = DefaultModuleDescriptor.newDefaultInstance(
                        ModuleRevisionId.newInstance(dep[0], dep[1] + '-caller', 'working')
                )

                DefaultDependencyDescriptor dd = new DefaultDependencyDescriptor(
                        md, ModuleRevisionId.newInstance(dep[0], dep[1], dep[2]), true, true, true
                )
                md.addDependency(dd)

                XmlModuleDescriptorWriter.write(md, ivyfile)

                String[] confs = ['default']
                ResolveOptions resolveOptions = new ResolveOptions().
                        setConfs(confs).
                        setTransitive(true)//.setArtifactFilter(FilterHelper.getArtifactTypeFilter(ARTIFACT_TYPES))

                return ivy.resolve(ivyfile.toURI().toURL(), resolveOptions)
            }
        })


        ArtifactDownloadReport[] reports = report.getAllArtifactsReports()

        //debug
        println 'All artifact reports:'
        reports.each { ArtifactDownloadReport rpt ->
            rpt.with {
                println "${getLocalFile().getName()}, ${getDownloadStatus()}, ${getType()}, ${getArtifact()}, ${getExt()}"
            }
        }

        ArtifactDownloadReport srcReport = reports.find { it.getType() == 'source' }
        // Java: the first jar is the Artifact
        ArtifactDownloadReport jarReport = reports.find { it.getType() == 'jar' || it.getType() == 'maven-plugin' || it.getType() == 'bundle' }
        // Android: the first aar is the Artifact
        ArtifactDownloadReport aarReport = reports.find { it.getType() == 'aar' }

        File codeLocalFile
        def deps = null
        if (aarReport) {
            // Try to find the sources (they are not always the first).
            String aarName = aarReport.getLocalFile().getName()
            String sourcesJarName = aarName[0..-5] + "-sources.jar"
            println "Looking for ${sourcesJarName}..."
            srcReport = reports.find { it.getLocalFile().getName() == sourcesJarName }
            if (!srcReport) {
                throw new RuntimeException("Could not find sources for Android application ${id}")
            }
            codeLocalFile = aarReport.getLocalFile()
            // deps = reports.findAll { (it.getType() == 'jar' || it.getType() == 'bundle') && it != aarReport }
        } else if (jarReport) {
            codeLocalFile = jarReport.getLocalFile()
            deps = reports.findAll { (it.getType() == 'jar' || it.getType() == 'bundle') && it != jarReport && (!badDependency(it.getLocalFile().getName())) }
            if (!srcReport && !ignoreSources) {
                throw new RuntimeException("No sources for ${id}")
            }
        } else {
            throw new RuntimeException("No binaries for ${id}")
        }
        checkArtifactFile(codeLocalFile, dep, reports)

        Artifact artifact = new Artifact(
            id          : id,
            report      : report,
            jar         : codeLocalFile,
            sourcesJar  : ignoreSources ? null : srcReport?.getLocalFile(),
            dependencies: deps.collect { it.getLocalFile().canonicalPath }
        )
        return artifact
    }

    private static void checkArtifactFile(File file, String[] dep, ArtifactDownloadReport[] reports) {
        String n = file.getName()
        for (String d : dep) {
            if (n.contains(d))
                return
        }
        String note
        if (dep.size() > 2) {
            ArtifactDownloadReport adr = reports.find {
                String name = it.getLocalFile().getName()
                (name.contains(dep[2]) && (!name.contains("-sources")) && (!name.contains("-javadoc")))
            }
            note = "(Did you mean ${adr.getLocalFile().getName()} of type = \"${adr.getType()}\"?)"
        } else {
            note = ""
        }
        throw new RuntimeException("${n} doesn't look like the correct artifact JAR for ${dep} ${note}")
    }

    // Returns true on dependencies that crash Doop. Subclasses that
    // need more control over dependencies should override this method.
    @SuppressWarnings("GrMethodMayBeStatic")
    protected boolean badDependency(String depName) {
        false
    }
}
