package org.clyze.utils

import groovy.transform.TupleConstructor
import groovy.transform.TypeChecked
import groovy.util.logging.Log4j

import java.util.concurrent.Executors

@Log4j
@TupleConstructor
@TypeChecked
class Executor {

	static final Closure STDOUT_PRINTER = { String line -> println line }

	File currWorkingDir = new File(".")
	Map<String, String> environment

	boolean isMonitoringEnabled
	long monitoringInterval
	Closure monitorClosure

	Executor execute(List<String> command, Closure outputLineProcessor = STDOUT_PRINTER) {
		def process = startProcess(command)

		def executorService = Executors.newSingleThreadExecutor()
		// Add a shutdown hook in case the JVM terminates during the execution of the process
		def shutdownActions = {
			log.debug "Destroying process: $command"
			process.destroy()
			log.debug "Process destroyed: $command"
			executorService.shutdownNow()
		}
		def shutdownThread = new Thread(shutdownActions as Runnable)
		Runtime.getRuntime().addShutdownHook(shutdownThread)

		/*
		 * Put the use of readline in a separate thread because it ignores
		 * thread interrupts. When an interrupt occurs, the "parent" thread
		 * will handle it and destroy the process so that the underlying socket
		 * is closed and readLine will fail. Otherwise if when a timeout
		 * occurs, the process will continue to run ignoring any attempt to
		 * stop it.
		 */
		try {
			def future = executorService.submit(new Runnable() {
				@Override
				void run() {
					process.inputStream.newReader().withReader { reader ->
						String line
						while ((line = reader.readLine()) != null)
							outputLineProcessor(line.trim())
					}
				}
			})

			isMonitoringEnabled ? doSampling(process) : future.get()
		}
		catch (all) {
			Runtime.runtime.removeShutdownHook(shutdownThread)
			shutdownActions()
			throw all
		}
		finally {
			executorService.shutdownNow()
		}
		Runtime.runtime.removeShutdownHook(shutdownThread)

		// Wait for process to terminate
		def returnCode = process.waitFor()

		// Create an error string that contains everything in the stderr stream
		//def errorMessages = process.errorStream.getText()
		//if (!errorMessages.isAllWhitespace()) {
		//    System.err.print(errorMessages)
		//}

		// Check return code and raise exception at failure indication
		if (returnCode != 0)
			throw new RuntimeException("Command exited with non-zero status:\n $command")

		return this
	}

	Process startProcess(List<String> command) {
		def pb = new ProcessBuilder(command)
		pb.directory(currWorkingDir)
		pb.redirectErrorStream(true)
		pb.environment().clear()
		pb.environment().putAll(this.environment)
		pb.start()
	}

	Executor enableMonitor(long monitoringInterval, Closure monitorClosure = null) {
		this.monitoringInterval = monitoringInterval
		this.monitorClosure = monitorClosure
		isMonitoringEnabled = true
		return this
	}

	Executor disableMonitor() {
		isMonitoringEnabled = false
		return this
	}

	void doSampling(Process process) {
		// Get PID via "reflection" hack
		def fld = process.class.getDeclaredField("pid")
		fld.setAccessible(true)
		def pid = fld.get(process)

		def monitorFile = new File(currWorkingDir, "monitoring.txt")
		def monitorWriter = monitorFile.newWriter()
		def monitorFileLatest = new File(currWorkingDir, "monitoring.latest.txt")
		log.info "Runtime info monitored in $monitorFile.absolutePath"
		monitorWriter.writeLine "$monitoringInterval"
		while (process.alive) {
			startProcess("top -b -n 1 -p $pid".split().toList()).inputStream.newReader().withReader { reader ->
				// If pid is still valid (e.g. process has not ended) the last line has the actual information
				def lastLine = reader.readLines().last()
				if (lastLine.startsWith(pid as String)) {
					def parts = lastLine.split()
					def values = [
							"PID"      : parts[0],
							"USER"     : parts[1],
							"PR"       : parts[2],
							"NI"       : parts[3],
							"VIRT"     : parts[4],
							"RES"      : parts[5],
							"SHR"      : parts[6],
							"S"        : parts[7],
							"CPU_PER"  : parts[8],
							"MEM_PER"  : parts[9],
							"TIME_PLUS": parts[10],
							"COMMAND"  : parts[11]
					]

					// If RES ends with "g" it's measured in GB, with "t" in TB, with "m" in MB, otherwise in KB. Convert to MB.
					if (values.RES.endsWith("g")) values.RES = values.RES[0..-2].toDouble() * 1024
					else if (values.RES.endsWith("t")) values.RES = values.RES[0..-2].toDouble() * 1024 * 1024
					else if (values.RES.endsWith("m")) values.RES = values.RES[0..-2].toDouble()
					else values.RES = values.RES.toDouble() / 1024
					def info = "${values.PID}\t${values.RES.toLong()}MB\t${values.CPU_PER.toDouble()}\t${values.COMMAND}"

					monitorWriter.writeLine info
					// Delete previous contents
					monitorFileLatest.withWriter { it.writeLine info }

					monitorClosure?.call values
				}
				null
			}
			Thread.currentThread().sleep(monitoringInterval)
		}
	}
}
