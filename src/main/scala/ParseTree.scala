// Defines regular expression parse trees.

package edu.ucsb.cs.cs162.regex.parse_tree

sealed abstract class ParseTree {
  //----------------------------------------------------------------------------
  // Public API.
  //----------------------------------------------------------------------------

  // Returns the character string corresponding to the leaves of this parse tree.
  def flatten: String = this match {
    case `EmptyLeaf` => ""
    case CharLeaf(char) => char.toString
    case ConcatNode(left, right) => left.flatten ++ right.flatten
    case LeftNode(child) => child.flatten
    case RightNode(child) => child.flatten
    case StarNode(children) => children.map(_.flatten).mkString("").reverse
    case CaptureNode(_, child) => child.flatten
  }

  override def toString = this match {
    case `EmptyLeaf` => "ε"
    case CharLeaf(char) => char.toString
    case ConcatNode(left, right) => s"(${left.toString} ~ ${right.toString})"
    case LeftNode(child) => s"L(${child.toString})"
    case RightNode(child) => s"R(${child.toString})"
    case StarNode(children) => children.map(_.toString).mkString("[", ", ", "]")
    case CaptureNode(name, child) => s"<${name} = ${child.toString}>"
  }

  def prettyPrint = asciiTree(this, prefix = "")

  //----------------------------------------------------------------------------
  // Private details.
  //----------------------------------------------------------------------------

  // Translate the parse tree into an ASCII tree structure.
  private def asciiTree(tree: ParseTree, prefix: String): String = {
    // The pieces that the ASCII tree structure are composed of.
    val (tee, bar, cap, end) = ("├─ ", "│  ", "└─ ", "   ")

    tree match {
      case `EmptyLeaf` => "ε\n"
      case CharLeaf(char) => char.toString + "\n"
      case ConcatNode(left, right) => {
        "ConcatNode\n" + prefix + tee + asciiTree(left, prefix + bar) +
        prefix + cap + asciiTree(right, prefix + end)
      }
      case LeftNode(child) => {
        "LeftNode\n" + prefix + cap + asciiTree(child, prefix + end)
      }
      case RightNode(child) => {
        "RightNode\n" + prefix + cap + asciiTree(child, prefix + end)
      }
      case StarNode(children) => {
        if (children.isEmpty) "StarNode\n" + prefix + cap + "ε\n"
        else {
          val init = children.init.map(prefix + tee + asciiTree(_, prefix + bar))
          val last = prefix + cap + asciiTree(children.last, prefix + end)

          "StarNode\n" + (
            if (children.size == 1) last
            else init.mkString("") + last
          )
        }
      }
      case CaptureNode(name, child) => {
        s"CaptureNode ($name)\n" + prefix + cap + asciiTree(child, prefix + end)
      }
    }
  }
}

// EmptyLeft corresponds to ε.
// CharLeaf corresponds to character 'char'.
// ConcatNode corresponds to Concatenate('right', 'left').
// LeftNode corresponds to the left side of a Union.
// RightNode corresponds to the right side of a Union.
// StarNode corresponds to a KleeneStar.
// CaptureNode corresponds to a Capture.
//
// Note that for efficiency reasons the Seq in StarNode is in reverse order; to
// get the correct order for the input string being matched it needs to be
// reversed.
case object EmptyLeaf extends ParseTree
case class CharLeaf(char: Char) extends ParseTree
case class ConcatNode(left: ParseTree, right: ParseTree) extends ParseTree
case class LeftNode(child: ParseTree) extends ParseTree
case class RightNode(child: ParseTree) extends ParseTree
case class StarNode(children: Seq[ParseTree]) extends ParseTree
case class CaptureNode(name: String, child: ParseTree) extends ParseTree
