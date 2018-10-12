// This is the derivative-based regular expression recognition algorithm.

package edu.ucsb.cs.cs162.regex.derivative

import edu.ucsb.cs.cs162.regex._

object Derive {
  import Regex._

  //----------------------------------------------------------------------------
  // Public API.
  //----------------------------------------------------------------------------

  // Uses the recursive derivative algorithm to compute whether 're' recognizes
  // 'str'.
  def matches(re: Regex, str: String): Boolean =
    str.foldLeft(re)((currentRe, char) => derive(currentRe, char)).nullable == ε

  //----------------------------------------------------------------------------
  // Private details.
  //----------------------------------------------------------------------------

  // Computes the derivative of 're' w.r.t. 'char' using a recursive algorithm.
  private def derive(re: Regex, char: Char): Regex = re match {
    case `∅` | `ε` => ∅
    case Chars(chars) => if (chars.contains(char)) ε else ∅
    case Concatenate(re1, re2) => {
      (derive(re1, char) ~ re2) | (re1.nullable ~ derive(re2, char))
    }
    case Union(re1, re2) => derive(re1, char) | derive(re2, char)
    case rek @ KleeneStar(re1) => derive(re1, char) ~ rek
    case Complement(re1) => !derive(re1, char)
    case Intersect(re1, re2) => derive(re1, char) & derive(re2, char)
  }
}
