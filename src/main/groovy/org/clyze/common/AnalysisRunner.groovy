package org.clyze.common

class AnalysisRunner implements Runnable {

	private final Analysis analysis

	AnalysisRunner(Analysis analysis) {
		this.analysis = analysis
	}

	public void run() {
		AnalysisPhase currentPhase = null
		try {
			for(AnalysisPhase phase: analysis.phases()) {
					currentPhase = phase
					currentPhase.run()
			}
			currentPhase = null
		}
		catch(InterruptedException ie) {
			if (currentPhase) {
				currentPhase.stop()
			}
		}
	}
}
