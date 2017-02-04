package org.clyze.analysis

/**
 * An analysis family (e.g. doop, cclyzer, etc.).
 *
 * The init() method should be called to initialize the underlying machinery (e.g. in order to produce the supported options
 * dynamically, if required).
 *
 * The supportedOptions() method should be idempotent, once the init() call has succeeded.
 */
interface AnalysisFamily {

	/** The name of the AnalysisFamily. */
	String getName()

	/** Initialize the family. Required to be called once per instance. */
	void init() throws RuntimeException

	/** The list of options supported by the analysis. */
	List<AnalysisOption> supportedOptions()

	/**
	 * The manager for Analysis Families, placed here for convenience.
	 *
	 * The manager is thread-safe and supports family registration and lookup in a case-insensitive fashion.
     * 
	 **/
	static class Manager {

		private static final Map<String, AnalysisFamily> families = [:]

		/**
		 * Registers the given family, performing its initialization.
		 * This method throws an exception if the given family or its name is null, if a family with the same name already exists,
		 * or the initialization of the given family fails.
		 */
		synchronized void register(AnalysisFamily family) throws RuntimeException {
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
		synchronized AnalysisFamily get(String name) {
			String lookup = name?.toLowerCase()
			return families.get(lookup)
		}

        /**
         * Returns true if a family with the given name is already registered.
         */
        synchronized boolean isRegistered(String name) {
            return get(name)
        }
	}
}
