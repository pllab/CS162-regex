package edu.ucsb.cs.cs162.regex.attack_synthesis

import edu.ucsb.cs.cs162.regex._
import edu.ucsb.cs.cs162.regex.derivative._

// An attack string is a prefix that leads to the vulnerable part of the
// expression, a pumpable string that can be repeated an arbitrary number of
// times, and a suffix string that causes the match to fail. The time for a
// backtracking-style match of the attack string is exponential in the number of
// times the pumpable string is repeated.
case class Attack(prefix: String, pumpable: String, suffix: String) {
  // Return an attack string with 'pumpable' repeated the given number of times.
  def attack(repeat: Int): String = prefix + (pumpable * repeat) + suffix
}

object AttackSynthesis {
  //----------------------------------------------------------------------------
  // Public API.
  //----------------------------------------------------------------------------

  // Given a regular expression, return a synthesized attack if one exists.
  def synthesizeAttack(re: Regex): Option[Attack] = ???

  //----------------------------------------------------------------------------
  // Private details.
  //----------------------------------------------------------------------------

  import Regex._

  // Building an attack requires a Regex describing valid prefixes, a Regex
  // describing valid pumpable strings and a specific pumpable string that
  // exposes the ambiguity of the pumpable Regex, and a Regex describing valid
  // suffixes.
  private case class AttackPrep(prefix: Regex, pumpString: String,
    pumpRe: KleeneStar, suffix: Regex)

  // Compute a suitable AttackPrep for 're'. Since complement and intersect
  // aren't handled by the backtracking matchers, we don't bother looking for
  // attacks involving them.
  private def computeAttack(re: Regex): Option[AttackPrep] = ???
  /*
   concatenate:
     look for attack on left side, then right side, then give up
     if found attackPrep, append left/right side to prefix/suffix as appropriate

   union:
     look for attack on left side, then right side, then give up

   kleene star:
     look for ambiguous string, else give up
     if found, generate base AttackPrep
   */

  // Compute an ambiguous string recognized by 're' if one exists.
  private def getPumpable(re: Regex): Option[String] = ???
  /*
   concatenate:
     look for pumpable string on left, then right, then this expression
     if found in left or right, append right/left string as appropriate

   union:
     look for pumpable string on left, then right, then this expression

   kleene star:
     look for pumpable string in child, then this expression
   */
}
