package org.clyze.analysis

import groovy.transform.CompileStatic

import java.util.concurrent.ConcurrentHashMap

/**
 * A simple registry of the the available analysis families.
 * The class supports family registration and lookup in a case-insensitive fashion.
 */
@CompileStatic
class AnalysisFamilies {

	private static final Map<String, AnalysisFamily> families = new ConcurrentHashMap()

	static Map<String, AnalysisFamily> getRegisteredFamilies() {
		return families
	}

	/**
	 * Registers the given family, performing its initialization.
	 * This method throws an exception if the given family or its name is null, if a family with the same name already exists,
	 * or the initialization of the given family fails.
	 */
	static void register(AnalysisFamily family) throws RuntimeException {
		if (!family) {
			throw new RuntimeException("Cannot register a null family")
		}

		String name = family.getName()?.toLowerCase()
		if (!name) {
			throw new RuntimeException("Cannot register a family with a null name")
		}

		if (families.containsKey(name)) {
			throw new RuntimeException("A family with the same name is already registered: $name")
		}
		family.init()
		families.put(name, family)
	}

	/**
	 * Gets the AnalysisFamily identified by the given name (case insensitive).
	 * @param name - the case insensitive name of the analysis family.
	 * @return an AnalysisFamily object or null if none was found
	 */
	static AnalysisFamily get(String name) {
		String lookup = name?.toLowerCase()
		return families.get(lookup)
	}

	/**
	 * Returns true if a family with the given name is already registered.
	 */
	static boolean isRegistered(String name) {
		return get(name)
	}

	/**
	 * Gets the supported options of the given family.
	 * Throws an error if the family is not registered.
	 */
	static List<AnalysisOption<?>> supportedOptionsOf(String name) throws RuntimeException {
		AnalysisFamily family = get(name)
		if (family) {
			return family.supportedOptions()
		} else {
			throw new RuntimeException("Family not registered: $name")
		}
	}
}
