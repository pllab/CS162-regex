// Provides some useful utilities.

package edu.ucsb.cs.cs162.util

object `package` {
  // Time the given block of code and call the given logger with the elapsed
  // time in nanoseconds.
  def time[T](logger: Long => Unit)(block: => T): T = {
    val initTime = System.nanoTime()
    val result = block
    logger(System.nanoTime() - initTime)
    result
  }
}

// Any class extending this trait will compute its hashcode once and cache the
// result rather than computing it over and over. This is only sound if the
// class is immutable. Note that case classes automatically extend Product
// already; non-case classes may have to implement some additional methods.
trait CachedHash extends Product {
  override val hashCode = scala.util.hashing.MurmurHash3.productHash(this)
}
