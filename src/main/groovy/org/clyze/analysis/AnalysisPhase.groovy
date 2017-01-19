package org.clyze.analysis

abstract class AnalysisPhase implements Runnable {
	String name
	abstract void stop()
}
