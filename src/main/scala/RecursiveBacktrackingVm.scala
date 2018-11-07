package edu.ucsb.cs.cs162.regex.vm

import edu.ucsb.cs.cs162.range_set._
import edu.ucsb.cs.cs162.regex._
import edu.ucsb.cs.cs162.regex.parse_tree._

class RecursiveBacktrackingVm(program: Program) extends VirtualMachine(program) {
  override def eval(str: String): Option[ParseTree] = {
    def run(pc: Int, sp: Int, progress: Set[Int], parse: Seq[ParseTree])
        : Option[ParseTree] = program(pc) match {
      case `Accept` =>
        if (sp == str.length) {
          assert(parse.size == 1)
          Some(parse.head)
        }
        else None

      case `Reject` =>
        None

      case `CheckProgress` =>
        if (progress.contains(pc)) None
        else run(pc + 1, sp, progress + pc, parse)

      case MatchSet(chars) =>
        if (sp != str.length && chars.contains(str(sp))) {
          run(pc + 1, sp + 1, progress = Set(), parse)
        }
        else None

      case Jump(offset) =>
        run(pc + offset, sp, progress, parse)

      case Fork(offset1, offset2) =>
        run(pc + offset1, sp, progress, parse) match {
          case parsed: Some[ParseTree] => parsed
          case None => run(pc + offset2, sp, progress, parse)
        }

      case `PushEmpty` =>
        run(pc + 1, sp, progress, EmptyLeaf +: parse)

      case `PushChar` =>
        run(pc + 1, sp, progress, CharLeaf(str(sp - 1)) +: parse)

      case `PushConcat` =>
        val right = parse.head
        val left = parse.tail.head
        val rest = parse.tail.tail
        run(pc + 1, sp, progress, ConcatNode(left, right) +: rest)

      case `PushLeft` =>
        run(pc + 1, sp, progress, LeftNode(parse.head) +: parse.tail)

      case `PushRight` =>
        run(pc + 1, sp, progress, RightNode(parse.head) +: parse.tail)

      case `InitStar` =>
        run(pc + 1, sp, progress, StarNode(Seq()) +: parse)

      case `PushStar` =>
        val body = parse.head
        val star = parse.tail.head
        val rest = parse.tail.tail
        star match {
          case StarNode(seq) =>
            run(pc + 1, sp, progress, StarNode(body +: seq) +: rest)
          case _ =>
            assert(false, "should be unreachable")
            None
        }

      case PushCapture(name) =>
        run(pc + 1, sp, progress, CaptureNode(name, parse.head) +: parse.tail)
    }

    run(pc = 0, sp = 0, progress = Set(), parse = Seq())
  }
}
