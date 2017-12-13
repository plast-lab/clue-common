package org.clyze.analysis

interface AnalysisFactory<A extends Analysis> {
	A newAnalysis(AnalysisFamily family,
				  String id,
				  String name,
				  Map<String, AnalysisOption> options,
				  List<String> inputFiles,
				  List<String> libraryFiles)
}
