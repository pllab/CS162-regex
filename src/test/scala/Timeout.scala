// Provides a way to run code within a timeout.

package edu.ucsb.cs.cs162.util

import java.util.concurrent._
import org.scalatest._

trait Timeout { this: Suite =>
  // Runs 'code', interrupting after 'timeout' milliseconds if necessary. Throws
  // a TestCanceledException if the code times out.
  //
  // IMPORTANT: This does not guarantee that the thread running 'code' will
  // actually stop, since there is no way to forcefully kill a thread in the
  // JVM. However, if you put 'Test / fork := true' in build.sbt then the tests
  // will be run in a separate process which is killed once the tests are
  // complete, so the threads will be terminated then.
  def timeoutAfter[T](timeout: Long)(code: => T): T = {
    val executor = Executors.newSingleThreadExecutor

    val future = executor.submit(new Callable[T]() {
      override def call: T = code
    })

    try { future.get(timeout, TimeUnit.MILLISECONDS) }
    catch {
      case _: TimeoutException => cancel(s"timed out after $timeout milliseconds")
      case e: ExecutionException => throw e.getCause()
      case e: Throwable => throw e
    }
    finally { executor.shutdownNow() }
  }
}
