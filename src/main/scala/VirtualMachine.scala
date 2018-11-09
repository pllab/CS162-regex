package edu.ucsb.cs.cs162.regex.vm

import edu.ucsb.cs.cs162.range_set._
import edu.ucsb.cs.cs162.regex.parse_tree._

object `package` {
  type Program = IndexedSeq[Instruction]
}

// Virtual machine instructions:
//
// - Accept: if we are at the end of the string being matched then accept it,
//   otherwise reject it.
// - Reject: unconditionally reject.
// - CheckProgress: terminate the current thread if we have seen this
//   instruction before and have not encountered a match instruction since. This
//   check is necessary to prevent infinite loops due to ε-cycles.
//   appended inside of it.
// - MatchSet: if the current character is contained in 'chars' then advance the
//   program counter, otherwise reject the string being matched.
// - Jump: unconditionally set the program counter to the provided offset from
//   its current value.
// - Fork: kill the current thread and create two new ones, each at the provided
//   offset from the current program counter and at the current position in the
//   string being matched.
// - PushEmpty: push EmptyLeaf onto the output stack.
// - PushChar: push CharLeaf with the just-matched character onto the output stack.
// - PushConcat: pop the top two elements of the output stack and push their
//   combination as a ConcatNode on the output stack.
// - PushLeft: pop the top of the output stack and push it back wrapped in a
//   LeftNode.
// - PushRight: pop the top of the output stack and push it back wrapped in a
//   RightNode.
// - InitStar: push a StarNode on top of the output stack containing an
//   EmptyLeaf.
// - PushStar: pop the top two elements of the output stack, the second of which
//   should be a StarNode, and push back the StarNode with the top element
// - PushCapture: pop the top of the output stack and push it backed wrapped
//   inside a CaptureNode.
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
