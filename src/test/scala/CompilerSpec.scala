package edu.ucsb.cs.cs162.regex.vm.compiler

import org.scalatest._
import edu.ucsb.cs.cs162.regex._

class DeriveSpec extends FlatSpec with Matchers {
  //----------------------------------------------------------------------------
  // Fixtures and helpers.
  // ---------------------------------------------------------------------------

  import Regex._

  //----------------------------------------------------------------------------
  // Tests.
  // ---------------------------------------------------------------------------

  behavior of "compile"

  it should "correctly compile the empty language" in { pending }

  it should "correctly compile ε" in { pending }

  it should "correctly compile concatenation" in  { pending }

  it should "correctly compile union" in  { pending }

  it should "correctly compile kleene star" in  { pending }
  // more tests...

  it should "correctly compile complex regexes 1" in { pending }

  it should "correctly compile complex regexes 2" in { pending }

  // more tests...
}
