package org.clyze.common

enum AnalysisFamily {
	DOOP {
		@Override
		List<AnalysisOption> supportedOptions() {
			//placeholder
			return []
		}
	},
	CCLYZER {
		@Override
		List<AnalysisOption> supportedOptions() {
			//placeholder
			return []
		}
	}

	abstract List<AnalysisOption> supportedOptions()
}
