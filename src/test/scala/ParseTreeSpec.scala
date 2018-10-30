package edu.ucsb.cs.cs162.regex.parse_tree

import org.scalatest._

class ParseTreeSpec extends FlatSpec with Matchers {
  //----------------------------------------------------------------------------
  // Fixtures and helpers.
  // ---------------------------------------------------------------------------

  val tree = ConcatNode(
    StarNode(Seq()),
    StarNode(Seq(
      CaptureNode("group", CharLeaf('a')),
      RightNode(CharLeaf('b')),
      LeftNode(CharLeaf('c'))
    ))
  )

  //----------------------------------------------------------------------------
  // Tests.
  // ---------------------------------------------------------------------------

  behavior of "a parse tree"

  it should "flatten to the correct string" in {
    tree.flatten should equal ("cba")
  }

  it should "pretty-print correctly" in {
    tree.prettyPrint should equal (s"""ConcatNode\n├─ StarNode\n│  └─ ε\n└─ StarNode\n   ├─ CaptureNode (group)\n   │  └─ a\n   ├─ RightNode\n   │  └─ b\n   └─ LeftNode\n      └─ c\n""")
  }
}
