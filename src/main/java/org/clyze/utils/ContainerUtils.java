package org.clyze.utils;

import org.apache.commons.io.IOUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.clyze.utils.JHelper.throwRuntimeException;

/**
 * This class provides functionality for handling archives containing bytecode
 * such as AAR files.
 */
public final class ContainerUtils {

    private ContainerUtils() {}

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

    /**
     * Create temporary directory and add it to 'tmpDirs' (so that it
     * can be later deleted). Preferred to File.deleteOnExit(), since
     * this code may be used in a server context and thus never exit.
     * @param tmpDirs  an optional set to receive the new path
     * @return         the new temporary path
     */
    public static String createTmpDir(Set<String> tmpDirs) {
        try {
            Path tmpDirPath = Files.createTempDirectory("ar");
            tmpDirPath.toFile().deleteOnExit();
            String tmpDir = tmpDirPath.toString();
            if (tmpDirs != null)
                tmpDirs.add(tmpDir);
            return tmpDir;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Transforms a set of Java archives: JAR archives are returned,
     * while AARs are searched for JAR entries, which are returned.
     * @param archives   a list of archive paths
     * @param ignore     if true, non-JAR/AAR files will be ignored
     * @param tmpDirs    collects any temporary directories created
     * @return           a list of paths
     */
    public static List<String> toJars(List<String> archives, boolean ignore,
                                      Set<String> tmpDirs) {
        List<String> jars = new ArrayList<>();
        archives.forEach(s -> toJar(s, jars, ignore, tmpDirs));
        return jars;
    }

    /**
     * Transforms an AAR file to a JAR.
     * @param ar           a file path
     * @param jars         the list of JAR file paths to update
     * @param ignore       if true, archives that are not in JAR/AAR/WAR format will
     *                     be omitted; if false, these archives still end up in
     *                     the "jars" list
     * @param tmpDirs      an optional list of temporary directories (for manual
     *                     resource control)
     */
    private static void toJar(String ar, List<String> jars,
                              boolean ignore, Set<String> tmpDirs) {
        if (ar.endsWith(".jar")) {
            jars.add(ar);
        } else if (ar.endsWith(".aar"))
            processFatArchive(ar, ".aar", jars, tmpDirs);
        else if (ar.endsWith(".war"))
            processFatArchive(ar, ".war", jars, tmpDirs);
        else {
            if (ignore)
                System.out.println("Ignoring file of unknown type: " + ar);
            else
                jars.add(ar);
        }
    }

    private static void processFatArchive(String ar, String ext, List<String> jars,
                                          Set<String> tmpDirs) {
        try {
            String tmpDir = createTmpDir(tmpDirs);
            String jar = tmpDir + "/" + basename(ar, ext) + ".jar";
            Set<String> extraJars = new HashSet<>();
            // Use a simple if-then-else instead of configurable lambdas/processors
            // since the processor logic may throw exceptions.
            if (ext.equals(".aar"))
                unpackClassesJarFromAAR(new File(ar), jar, extraJars, tmpDirs);
            else if (ext.equals(".war"))
                unpackClassesAndJarsFromWAR(new File(ar), jar, extraJars, tmpDirs);
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
    }

    private static void unpackClassesAndJarsFromWAR(File war, String jar,
                                                    Set<String> extraJars, Set<String> tmpDirs) {
        File tmpDir = new File(createTmpDir(tmpDirs));
        ZipUtil.unpack(war, tmpDir);
        File webInfClasses = new File(new File(tmpDir, "WEB-INF"), "classes");
        if (webInfClasses.exists())
            ZipUtil.pack(webInfClasses, new File(jar));
        else
            System.out.println("WARNING: no path " + webInfClasses);
        File webInfLibs = new File(new File(tmpDir, "WEB-INF"), "lib");
        if (webInfLibs.exists()) {
            File[] files = webInfLibs.listFiles();
            if (files != null)
                for (File file : files)
                    try {
                        if (file.getName().endsWith(".jar"))
                            extraJars.add(file.getCanonicalPath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
        } else
            System.out.println("WARNING: no path " + webInfClasses);
    }

    private static String basename(String path, String ext) {
        int sIdx = path.lastIndexOf(File.separator);
        int extSz = ext.length();
        return path.substring(sIdx == -1 ? 0 : sIdx, path.length() - extSz);
    }

}
