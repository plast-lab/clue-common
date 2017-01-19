package org.clyze.analysis

interface AnalysisFactory {
	Analysis newAnalysis(AnalysisFamily family,
	                     String id,
	                     String name,
	                     Map<String, AnalysisOption> options,
	                     List<String> inputFiles)
}
