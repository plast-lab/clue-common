package org.clyze.common

abstract class AnalysisPhase implements Runnable {
	String name
	abstract void stop()
}
