package org.clyze.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.zeroturnaround.zip.ZipUtil;

import static org.clyze.utils.JHelper.throwRuntimeException;

/**
 * This class provides functionality for handling archives containing bytecode
 * such as AAR/WAR files.
 */
public final class ContainerUtils {

    private ContainerUtils() {}

    /**
     * Decompress AAR contents.
     * @param localAAR   the AAR file
     * @param targetJar  the file path to use for saving classes.jar
     * @param jarLibs    the bundled library JAR entries
     * @param tmpDirs    collection that receives temporary directories created
     */
    private static void unpackClassesJarFromAAR(File localAAR, String targetJar,
                                                Collection<String> jarLibs,
                                                Collection<String> tmpDirs)
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
                jarLibs.add(ej.getCanonicalPath());
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
     * @param tmpDirs  an optional collection to receive the new temporary path
     * @return         the new temporary path
     */
    public static String createTmpDir(Collection<String> tmpDirs) {
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
     * Transforms a set of Java archives: JAR archives are returned as-is,
     * while other formats (AAR/WAR) are replaced by their app bytecode being
     * repackaged as JAR files. Any additional code entries (e.g. bundled JAR
     * libraries in WAR files) is stored in parameter jarLibs, so that the
     * caller can handle them.
     *
     * @param archives   a list of archive paths
     * @param ignore     if true, non-JAR/AAR files will be ignored
     * @param jarLibs    a collection to receives extra bundled libraries
     * @param tmpDirs    collects any temporary directories created
     * @return           a list of paths
     */
    public static List<String> toJars(List<String> archives, boolean ignore,
                                      Collection<String> jarLibs, Collection<String> tmpDirs) {
        List<String> jars = new ArrayList<>();
        archives.forEach(s -> toJar(s, jars, jarLibs, ignore, tmpDirs));
        return jars;
    }

    /**
     * Transforms an AAR file to a JAR.
     * @param ar           a file path
     * @param jars         the (input) JAR file paths to update
     * @param jarLibs the (library) JAR file paths to update
     * @param ignore       if true, archives that are not in JAR/AAR/WAR format will
     *                     be omitted; if false, these archives still end up in
     *                     the "jars" list
     * @param tmpDirs      an optional collection of temporary directories (for manual
     *                     resource control)
     */
    private static void toJar(String ar, Collection<String> jars,
                              Collection<String> jarLibs,
                              boolean ignore, Collection<String> tmpDirs) {
        if (ar.endsWith(".jar")) {
            jars.add(ar);
        } else if (ar.endsWith(".aar"))
            processFatArchive(ar, ".aar", jars, jarLibs, tmpDirs);
        else if (ar.endsWith(".war"))
            processFatArchive(ar, ".war", jars, jarLibs, tmpDirs);
        else if (ignore)
            System.out.println("Ignoring file of unknown type: " + ar);
        else
            jars.add(ar);
    }

    private static void processFatArchive(String ar, String ext, Collection<String> jars,
                                          Collection<String> jarLibs, Collection<String> tmpDirs) {
        try {
            String tmpDir = createTmpDir(tmpDirs);
            String jar = tmpDir + "/" + basename(ar, ext) + ".jar";
            // Use a simple if-then-else instead of configurable lambdas/processors
            // since the processor logic may throw exceptions.
            if (ext.equals(".aar"))
                unpackClassesJarFromAAR(new File(ar), jar, jarLibs, tmpDirs);
            else if (ext.equals(".war"))
                unpackClassesAndJarsFromWAR(new File(ar), jar, jarLibs, tmpDirs);
            // println "Extracted ${jar} from ${ar}"
            jars.add(jar);
        } catch (Exception ex) {
            System.out.println("Error while handling " + ar);
            ex.printStackTrace();
        }
    }

    private static void unpackClassesAndJarsFromWAR(File war, String jar,
                                                    Collection<String> jarLibs,
                                                    Collection<String> tmpDirs) {
        String tmpDirPath = createTmpDir(tmpDirs);
        if (tmpDirPath == null) {
            System.out.println("ERROR: null temporary directory path");
            return;
        }
        File tmpDir = new File(tmpDirPath);
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
                            jarLibs.add(file.getCanonicalPath());
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
