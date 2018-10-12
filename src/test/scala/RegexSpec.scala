package edu.ucsb.cs.cs162.regex

import org.scalatest._

class RegexSpec extends FlatSpec with Matchers {
  //----------------------------------------------------------------------------
  // Fixtures and helpers.
  // ---------------------------------------------------------------------------

  import Regex._

  val b = Chars('b')
  val c = Chars('c')
  val d = Chars('d')

  //----------------------------------------------------------------------------
  // Tests.
  // ---------------------------------------------------------------------------

  behavior of "a regex"

  it should "be buildable using convenience methods 1" in {
    (b ~ c) should equal (Concatenate(b, c))
  }

  it should "be buildable using convenience methods 2" in {
    (b | (b ~ c)) should equal (Union(b, Concatenate(b, c)))
  }

  it should "be buildable using convenience methods 3" in {
    b.* should equal (KleeneStar(b))
  }

  it should "be buildable using convenience methods 4" in {
    !b should equal (Complement(b))
  }

  it should "be buildable using convenience methods 5" in {
    (b & (b ~ c)) should equal (Intersect(b, Concatenate(b, c)))
  }

  it should "be buildable using convenience methods 6" in {
    b.+ should equal (Concatenate(b, KleeneStar(b)))
  }

  it should "be buildable using convenience methods 7" in {
    b.? should equal (Union(ε, b))
  }

  it should "be buildable using convenience methods 8" in {
    b^3 should equal (Concatenate(Concatenate(b, b), b))
  }

  it should "be buildable using convenience methods 9" in {
    (b >= 2) should equal (Concatenate(Concatenate(b, b), KleeneStar(b)))
  }

  it should "be buildable using convenience methods 10" in {
    (b <= 2) should equal (Union(Union(ε, b), Concatenate(b, b)))
  }

  it should "be buildable using convenience methods 11" in {
    (b <> (1, 3)) should equal (Intersect(Concatenate(b, KleeneStar(b)), Union(Union(Union(ε, b), Concatenate(b, b)), Concatenate(Concatenate(b, b), b))))
  }

  it should "be buildable from strings" in {
    "ab".charset ~ "cd".concatenate should equal (Concatenate(Chars('a', 'b'),
      Concatenate(Chars('c'), Chars('d'))))
  }

  it should "pretty-print correctly" in {
    (b.? | (c >= 1)).prettyPrint should equal (s"""Union\n├─ Union\n│  ├─ ε\n│  └─ b\n└─ Concatenate\n   ├─ c\n   └─ KleeneStar\n      └─ c\n""")
  }


  behavior of "nullable"

  it should "recognize a nullable regex 1" in { pending }

  // more tests...

  it should "recognize a non-nullable regex 1" in { pending }

  // more tests...
}
