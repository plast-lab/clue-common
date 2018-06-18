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

	Map<String, String> environment

	void execute(List<String> command, Closure outputLineProcessor = STDOUT_PRINTER) {
		execute(null, command, false, outputLineProcessor)
	}

	void execute(String workingDir, List<String> command, Closure outputLineProcessor = STDOUT_PRINTER) {
		execute(workingDir, command, false, outputLineProcessor)
	}

	void execute(String workingDir, List<String> command, boolean enableMonitoring, Closure outputLineProcessor = STDOUT_PRINTER) {
		def process = startProcess(workingDir, command)

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

			enableMonitoring ? monitor(workingDir, process) : future.get()
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
		if (returnCode != 0) {
			throw new RuntimeException("Command exited with non-zero status:\n $command")
		}
	}

	Process startProcess(String workingDir, List<String> command) {
		def pb = new ProcessBuilder(command)
		if (workingDir) {
			def cwd = FileOps.findDirOrThrow(workingDir, "Working directory is invalid: $workingDir")
			pb.directory(cwd)
		}
		pb.redirectErrorStream(true)
		pb.environment().clear()
		pb.environment().putAll(this.environment)
		pb.start()
	}

	void monitor(String workingDir, Process process) {
		def interval = 5000
		def monitorFile = new File(workingDir ?: ".", "monitoring.txt")
		monitorFile << "$interval\n"

		logger.info "Runtime info monitored in $monitorFile.canonicalPath"

		def fld = process.class.getDeclaredField("pid")
		fld.setAccessible(true)
		def pid = fld.get(process)

		while (process.alive) {
			// "ps -p $pid -o %cpu,%mem,cmd --noheader".split().toList() /// %cpu is not current but avg
			startProcess(workingDir, "top -b -n 1 -p $pid".split().toList())
					.inputStream.newReader(). withReader { reader ->
				// If pid is still valid (e.g. process has not ended) the last line has the actual information
				def lastLine = reader.readLines().last()
				if (lastLine.startsWith(pid as String)) {
					// PID USER PR NI VIRT RES SHR S %CPU %MEM TIME+ COMMAND
					def parts = lastLine.split()
					def mem = parts[5].endsWith("g") ? (parts[5][0..-2]).toDouble() * 1024 : parts[5].toDouble()
					def info = "${mem}MB\t${parts[8]}\n"
					monitorFile << info
					new File(monitorFile.parentFile, "monitoring.latest.txt").withWriter { it.write info }
				}
				null
			}
			Thread.currentThread().sleep(interval)
		}
	}
}
