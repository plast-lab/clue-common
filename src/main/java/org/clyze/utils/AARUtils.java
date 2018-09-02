package org.clyze.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.clyze.utils.JHelper.throwRuntimeException;

public class AARUtils {

    // Decompress AAR and find its classes.jar, which is then saved
    // using the value of parameter 'targetJar'. Any other .jar
    // entries are locally extracted and their paths are pushed into
    // 'extraJars'. Temporary directories created go into 'tmpDirs'.
    private static void unpackClassesJarFromAAR(File localAAR, String targetJar,
                                                Set<String> extraJars,
                                                Set<String> tmpDirs)
            throws IOException {
        boolean classesJarFound = false;
        ZipFile zipFile = new java.util.zip.ZipFile(localAAR);
        for (ZipEntry it: Collections.list(zipFile.entries())) {
            String name = it.getName();
            if (name.equals("classes.jar")) {
                File cj = new File(targetJar);
                IOUtils.copy(zipFile.getInputStream(it), new FileOutputStream(cj));
                classesJarFound = true;
            } else if (name.endsWith(".jar")) {
                // Extract to temporary directory & add to return set.
                File ej = new File(createTmpDir(tmpDirs) + "/" + basename(name, ""));
                IOUtils.copy(zipFile.getInputStream(it), new FileOutputStream(ej));
                extraJars.add(ej.getCanonicalPath());
            }
        }
        if (!classesJarFound) {
            String aarPath = localAAR.getCanonicalPath();
            throwRuntimeException("No classes.jar found in " + aarPath);
        }
    }

    // Create temporary directory and add it to 'tmpDirs' (so that it
    // can be later deleted). Preferred to File.deleteOnExit(), since
    // this code may be used in a server context and thus never exit.
    public static String createTmpDir(Set<String> tmpDirs) {
        try {
            String tmpDir = Files.createTempDirectory("aar").toString();
            if (tmpDirs != null)
                tmpDirs.add(tmpDir);
            return tmpDir;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Transforms a set of Java archives: JAR archives are returned,
    // while AARs are searched for JAR entries, which are returned.
    // Parameter 'tmpDirs' collects any temporary directories created.
    public static List<String> toJars(List<String> archives, boolean ignore,
                                      Set<String> tmpDirs)
            throws IOException {
        List<String> jars = new ArrayList<>();
        archives.forEach(s -> toJar(s, jars, archives, ignore, tmpDirs));
        return jars;
    }

    private static void toJar(String ar, List<String> jars, List<String> archives,
                              boolean ignore, Set<String> tmpDirs) {
        if (ar.endsWith(".jar")) {
            jars.add(ar);
        } else if (ar.endsWith(".aar")) {
            try {
                String tmpDir = createTmpDir(tmpDirs);
                String jar = tmpDir + "/" + basename(ar, ".aar") + ".jar";
                Set<String> extraJars = new HashSet<>();
                unpackClassesJarFromAAR(new File(ar), jar, extraJars, tmpDirs);
                // println "Extracted ${jar} from ${ar}"
                jars.add(jar);
                if (extraJars.size() > 0) {
                    // println "Extracted ${extraJars} from ${ar}"
                    jars.addAll(extraJars);
                }
            } catch (Exception ex) {
                System.out.println("Error while handling " + ar);
                ex.printStackTrace();
            }
        } else {
            if (ignore)
                System.out.println("Ignoring file of unknown type: " + ar);
            else
                jars.add(ar);
        }
    }

    private static String basename(String path, String ext) {
        int sIdx = path.lastIndexOf(File.separator);
        int extSz = ext.length();
        return path.substring(sIdx == -1 ? 0 : sIdx, path.length() - extSz);
    }

}
