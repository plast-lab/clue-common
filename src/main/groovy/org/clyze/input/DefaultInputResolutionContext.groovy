package org.clyze.input

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j
import org.clyze.analysis.InputType

/**
 * The default implementation of the input resolution mechanism.
 */
@Log4j
@CompileStatic
class DefaultInputResolutionContext implements InputResolutionContext {

	// The default resolver. Needs temporary directory for downloaded files.
	static ChainResolver defaultResolver(File tmpDir) {
		return new ChainResolver(
			// The order matters
			new FileResolver(),
			new DirectoryResolver(),
			new URLResolver(tmpDir, true),
			new IvyResolver())
	}

	static ChainResolver pythonResolver(File tmpDir) {
		return new ChainResolver(
			// The order matters
			new FileResolver(),
			new URLResolver(tmpDir, true),
			new PythonDirectoryResolver())
	}

	// The input resolver
	ChainResolver resolver

	// Always true, but this may change in the future
	boolean transitive = true

	Map<InputType, List<String>> files = [:].withDefault { [] }
	Map<InputType, Map<String, ResolvedInput>> resolvedFiles = [:].withDefault { [:] as Map<String, ResolvedInput>}

	DefaultInputResolutionContext(ChainResolver resolver) {
		this.resolver = resolver
	}

	@Override
	void add(String input, InputType inputType) { files[inputType] << input }

	@Override
	void add(List<String> inputs, InputType inputType) { files[inputType].addAll(inputs) }

	@Override
	void set(String input, File file, InputType inputType) {
		resolvedFiles[inputType][input] = new ResolvedInput(input, file)
	}

	@Override
	void set(String input, List<File> files, InputType inputType) {
		resolvedFiles[inputType][input] = new ResolvedInput(input, files)
	}

	@Override
	Set<File> get(String input, InputType inputType) { resolvedFiles[inputType][input]?.files() ?: [] as Set }

	@Override
	void resolve() {
		// Iterate over a local immutable copy, since the resolver may
		// modify the contents of the files field.
		Map<InputType, Collection<String>> map = [:]
		files.each { inputType, paths -> map.put(inputType, paths) }
		map.each { inputType, paths ->
			paths.each { path ->
				log.debug "Resolving $path ($inputType)"
				def resolvedFile = resolvedFiles[inputType][path]
				if (!resolvedFile)
					resolver.resolve(path, this, inputType)
			}
		}
	}

	@Override
	List<String> inputs() { files[InputType.INPUT] + files[InputType.INPUT_LOCAL] }

	@Override
	List<String> libraries() { files[InputType.LIBRARY] }

	@Override
	List<String> platformFiles() { files[InputType.PLATFORM] }

	@Override
	List<String> heapDLs() { files[InputType.HEAPDL] }

	@Override
	List<String> sourceFiles() { files[InputType.SOURCES] }

	private List<File> get0(InputType inputType) {
		List<File> resolvedList = []
		files[inputType].each { path ->
			log.debug "Getting $path ($inputType)"
			def resolved = resolvedFiles[inputType][path]
			if (resolved) {
				log.debug "$path ($inputType) is resolved -> ${resolved.files()}"
				resolvedList += resolved.files()
			} else {
				throw new RuntimeException("Unresolved $path ($inputType)")
			}
		}
		return resolvedList
	}

	@Override
	List<File> getAllInputs() { get0(InputType.INPUT) + get0(InputType.INPUT_LOCAL) }

	@Override
	List<File> getAllLibraries() { get0(InputType.LIBRARY) }

	@Override
	List<File> getAllPlatformFiles() { get0(InputType.PLATFORM) }

	@Override
	List<File> getAllHeapDLs() { get0(InputType.HEAPDL) }

	@Override
	List<File> getAllSourceFiles() { get0(InputType.SOURCES) }

	@Override
	List<File> getAll() { files.keySet().collect { inputType -> get0(inputType) }.flatten() as List<File> }

	@Override
	String toString() { files.collect { inputType, paths -> "$inputType: $paths" }.join(", ") }
}
