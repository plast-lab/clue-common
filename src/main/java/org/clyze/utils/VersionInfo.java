package org.clyze.utils;

import java.io.*;

public class VersionInfo {
	/**
	 * Returns the version of the JAR where a class is defined. This
	 * version is configured by two pieces of information in the JAR:
	 * field 'Implementation-Version' in the manifest (software
	 * release version) and file git-hash.txt (HEAD hash).
	 *
	 * @param c	 A class object to use for finding the manifest
	 *			 information. Different classes may retrieve manifests
	 *			 from different JARs.
	 * @return	 A version string that may contain a release tag or a
	 *			 git hash (or both).
	 */
	public static String getVersionInfo(Class<?> c) {
		// Read "version" (may be empty if run via './gradlew run').
		String version = c.getPackage().getImplementationVersion();

		// Read Git commit hash.
		String hash = null;
		InputStream gitHashIS = c.getClassLoader().getResourceAsStream("git-hash.txt");
		if (gitHashIS != null) {
			String line;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(gitHashIS))) {
				while ((line = br.readLine()) != null)
					if (!line.equals(""))
						hash = line;
			} catch (IOException ignored) { }
		}

		if (hash == null && version == null)
			return "No version information available.";
		else if (hash != null && version != null)
			return "Version: " + version + " (Git hash: " + hash + ")";
		else if (version != null)
			return "Version: " + version;
		else // (hash != null)
			return "Git hash: " + hash;
	}
}
