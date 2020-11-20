package org.clyze

import org.clyze.analysis.InputType
import org.clyze.input.DefaultInputResolutionContext
import spock.lang.Specification

class TestResolvers extends Specification {
    def "Resolve Maven artifact"() {
        when:
        File tmpDir = File.createTempDir()
        tmpDir.deleteOnExit()
        println "Using temporary directory: ${tmpDir.canonicalPath}"
        DefaultInputResolutionContext ctx = new DefaultInputResolutionContext(DefaultInputResolutionContext.defaultResolver(tmpDir))
        ctx.add('org.apache.ant:ant:1.10.9', InputType.INPUT)
        ctx.resolve()
        def codeJar = ctx.allInputs.find { File f -> println f; f.name == 'ant-1.10.9.jar' }
        def sourcesJar = ctx.allSourceFiles.find { File f -> println f; f.name == 'ant-1.10.9-sources.jar' }

        then:
        codeJar != null
        sourcesJar != null
    }
}
