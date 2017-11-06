package org.clyze.utils

import java.nio.file.*;

public class AARUtils {

    // Decompress AAR and find its classes.jar, which is then saved
    // using the value of parameter 'targetJar'. Any other .jar
    // entries are locally extracted and their paths are pushed into
    // 'extraJars'. Temporary directories created go into 'tmpDirs'.
    private static void unpackClassesJarFromAAR(File localAAR, String targetJar,
                                                Set<String> extraJars,
                                                Set<String> tmpDirs) {
        boolean classesJarFound = false
        def zipFile = new java.util.zip.ZipFile(localAAR)
        zipFile.entries().each {
            if (it.name.equals('classes.jar')) {
                File cj = new File(targetJar)
                cj.newOutputStream() << zipFile.getInputStream(it)
                classesJarFound = true
            } else if (it.name.endsWith(".jar")) {
                // Extract to temporary directory & add to return set.
                File ej = new File(createTmpDir(tmpDirs) + "/" + basename(it.name, ""))
                ej.newOutputStream() << zipFile.getInputStream(it)
                extraJars << ej.canonicalPath
            }
        }
        if (!classesJarFound) {
            String aarPath = localAAR.getCanonicalPath()
            throwRuntimeException("No classes.jar found in ${aarPath}")
        }
    }

    // Create temporary directory and add it to 'tmpDirs' (so that it
    // can be later deleted). Preferred to File.deleteOnExit(), since
    // this code may be used in a server context and thus never exit.
    public static String createTmpDir(Set<String> tmpDirs) {
        String tmpDir = Files.createTempDirectory("aar").toString()
        tmpDirs?.add(tmpDir)
        return tmpDir
    }

    // Transforms a set of Java archives: JAR archives are returned,
    // while AARs are searched for JAR entries, which are returned.
    // Parameter 'tmpDirs' collects any temporary directories created.
    public static List<String> toJars(List<String> archives, boolean ignore,
                                      Set<String> tmpDirs) {
        List<String> jars = []
        archives.each { String ar ->
            if (ar.endsWith(".jar")) {
                jars << ar
            } else if (ar.endsWith(".aar")) {
                String tmpDir = createTmpDir(tmpDirs)
                String jar = tmpDir + "/" + basename(ar, ".aar") + ".jar"
                Set<String> extraJars = new HashSet<>()
                unpackClassesJarFromAAR(new File(ar), jar, extraJars, tmpDirs)
                // println "Extracted ${jar} from ${ar}"
                jars << jar
                if (extraJars.size() > 0) {
                    // println "Extracted ${extraJars} from ${ar}"
                    jars.addAll(extraJars)
                }
            } else {
                if (ignore) {
                    println "Ignoring file of unknown type: ${ar}"
                } else {
                    jars << ar
                }
            }
        }
        return jars
    }

    private static String basename(String path, String ext) {
        int sIdx = path.lastIndexOf(File.separator)
        int extSz = ext.length()
        return path.substring(sIdx == -1 ? 0 : sIdx, path.length() - extSz)
    }

}
