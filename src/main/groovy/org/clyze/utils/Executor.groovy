package org.clyze.utils

import groovy.transform.TupleConstructor
import groovy.transform.TypeChecked
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import java.util.concurrent.Executors

@TupleConstructor
@TypeChecked
class Executor {

	static final Closure STDOUT_PRINTER = { String line -> println line }

	protected Log logger = LogFactory.getLog(getClass())

	File currWorkingDir = new File(".")
	Map<String, String> environment
	boolean isMonitoringEnabled = false
	long monitoringInterval = 5000L

	void execute(List<String> command, Closure outputLineProcessor = STDOUT_PRINTER) {
		def process = startProcess(command)

		def executorService = Executors.newSingleThreadExecutor()
		// Add a shutdown hook in case the JVM terminates during the execution of the process
		def shutdownActions = {
			logger.debug "Destroying process: $command"
			process.destroy()
			logger.debug "Process destroyed: $command"
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

			isMonitoringEnabled ? monitor(process) : future.get()
		}
		catch (InterruptedException e) {
			Runtime.runtime.removeShutdownHook(shutdownThread)
			shutdownActions()
			throw e
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
	}

	Process startProcess(List<String> command) {
		def pb = new ProcessBuilder(command)
		pb.directory(currWorkingDir)
		pb.redirectErrorStream(true)
		pb.environment().clear()
		pb.environment().putAll(this.environment)
		pb.start()
	}

	void monitor(Process process) {
		def fld = process.class.getDeclaredField("pid")
		fld.setAccessible(true)
		def pid = fld.get(process)

		def monitorFile = new File(currWorkingDir,"monitoring.txt")
		logger.info "Runtime info monitored in $monitorFile.absolutePath"
		monitorFile.withWriterAppend { writer ->
			writer << "$monitoringInterval\n"
			while (process.alive) {
				startProcess("top -b -n 1 -p $pid".split().toList())
						.inputStream.newReader(). withReader { reader ->
					// If pid is still valid (e.g. process has not ended) the last line has the actual information
					def lastLine = reader.readLines().last()
					if (lastLine.startsWith(pid as String)) {
						// PID USER PR NI VIRT RES SHR S %CPU %MEM TIME+ COMMAND
						def parts = lastLine.split()
						def mem = parts[5].endsWith("g") ? (parts[5][0..-2]).toDouble() * 1024 : parts[5].toDouble()
						def info = "$pid\t${mem}MB\t${parts[8]}\t${parts[11]}\n"
						writer << info
						// Delete previous contents
						new File(monitorFile.parentFile, "monitoring.latest.txt").withWriter { it.write info }
					}
					null
				}
				Thread.currentThread().sleep(monitoringInterval)
			}
		}
	}
}
