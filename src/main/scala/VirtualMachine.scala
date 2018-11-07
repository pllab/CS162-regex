package edu.ucs.cs.cs162.regex.vm

import edu.ucs.cs.cs162.range_set._
import edu.ucs.cs.cs162.regex.parse_tree._

object `package` {
  type Program = IndexedSeq[Instruction]
}

sealed abstract class Instruction
case object Accept extends Instruction
case object Reject extends Instruction
case object CheckProgress extends Instruction
case class MatchSet(chars: CharSet) extends Instruction
case class Jump(offset: Int) extends Instruction
case class Fork(offset1: Int, offset2: Int) extends Instruction
case object PushEmpty extends Instruction
case object PushChar extends Instruction
case object PushConcat extends Instruction
case object PushLeft extends Instruction
case object PushRight extends Instruction
case object InitStar extends Instruction
case object PushStar extends Instruction
case class PushCapture(name: String) extends Instruction

abstract class VirtualMachine(program: Program) {
  def eval(str: String): Option[ParseTree]
}
