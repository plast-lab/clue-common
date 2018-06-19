package org.clyze.utils

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

import java.util.zip.*

import java.nio.file.*

class FileOps {

	private static final FileFilter ALL_FILES_AND_DIRECTORIES = [
			accept: { File f -> true }
	] as FileFilter

	static FilenameFilter extensionFilter(String extension) {
		def filter = [
				accept: { File file, String name ->
					String ext = FilenameUtils.getExtension(name)
					return ext == extension
				}
		] as FilenameFilter

		return filter
	}

	static Properties loadProperties(String file) {
		File f = findFileOrThrow(file, "Not a valid file: $file")
		return loadProperties(f)
	}

	static Properties loadProperties(File f) {
		Properties props = new Properties()
		f.withReader { BufferedReader r -> props.load(r) }
		return props
	}

	static Properties loadPropertiesFromClasspath(String path) {
		Properties props = new Properties()
		InputStream s
		try {
			s = ClassLoader.getSystemResourceAsStream(path)
			if (s == null) throw new RuntimeException("$path not found in classpath")
			props.load(s)
		}
		finally {
			if (s) s.close()
		}
		return props
	}

	static File findFileOrThrow(String file, String message) {
		if (!file) throw new RuntimeException(message)
		return findFileOrThrow(new File(file), message)
	}

	static File findFileOrThrow(File file, String message) {
		if (!file) throw new RuntimeException(message)
		if (!file.exists() || !file.isFile() || !file.canRead())
			throw new RuntimeException(message)

		return file
	}

	static List<String> findFiles(List<String> files) {
		files.each { String file ->
			findFileOrThrow(file, "Not a valid file: $file")
		}
		return files
	}

	static File findDirOrThrow(String dir, String message) {
		if (!dir) throw new RuntimeException(message)
		return findDirOrThrow(new File(dir), message)
	}

	static File findDirOrThrow(File dir, String message) {
		if (!dir) throw new RuntimeException(message)
		if (!dir.exists() || !dir.isDirectory())
			throw new RuntimeException(message)

		return dir
	}

	static File ensureDirExistsOrThrow(String dir, String message) {
		if (!dir) throw new RuntimeException(message)
		return ensureDirExistsOrThrow(new File(dir), message)
	}

	static File ensureDirExistsOrThrow(File dir, String message) {
		dir.mkdirs()
		return findDirOrThrow(dir, message)
	}

	/**
	 * Copies the contents of the src directory to dest (as in: cp -R src/* dest).
	 */
	static void copyDirContents(File src, File dest) {
		FileUtils.copyDirectory(src, dest, ALL_FILES_AND_DIRECTORIES)
	}

	/**
	 * Writes the given string to the given file.
	 */
	static File writeToFile(File file, String s) {
		file.withWriter { Writer w -> w.write s }
		return file
	}


	/**
	 * Extracts of the contents of the given zipFile to the dest directory.
	 */
	static void unzip(File zipFile, File dest) {

		findFileOrThrow(zipFile, "Not a valid file: $zipFile")
		findDirOrThrow(dest, "Not a directory: $dest")
		Path destPath = dest.toPath()
		ZipInputStream zipInput = new ZipInputStream(new FileInputStream(zipFile))
		zipInput.withStream { 
			ZipEntry entry
			while(entry = zipInput.getNextEntry()) { //until null is returned
				Path target = destPath.resolve(entry.getName())
				if (entry.isDirectory()) {
					Files.createDirectories(target)
				}
				else {
					Path parent = target.getParent()
					if (parent) {
						Files.createDirectories(parent)
					}	
					Files.copy(zipInput, target)
				}
			}
		}
	}

	/**
	 * Extracts of the contents of the given zipFile to the dest directory.
	 */
	static void unzip(String zipFile, File dest) {
		unzip(new File(zipFile), dest)
	}
}
