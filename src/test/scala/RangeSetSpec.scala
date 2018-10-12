package edu.ucsb.cs.cs162.range_set

import org.scalatest._

class RangeSetSpec extends FlatSpec with Matchers {
  //----------------------------------------------------------------------------
  // Fixtures and helpers.
  // ---------------------------------------------------------------------------

  val s1 = CharSet('a', 'b', 'd')
  val s2 = CharSet('b' -> 'd', 'a' -> 'c')
  val s3 = CharSet(IndexedSeq('x' -> 'z', 'a' -> 'b'))

  //----------------------------------------------------------------------------
  // Tests.
  // ---------------------------------------------------------------------------

  behavior of "a range set"

  it should "be constructable with arbitrary type-correct inputs 1" in {
    s1.contains('a') should be (true)
    s1.contains('b') should be (true)
    s1.contains('d') should be (true)

    s1.contains('c') should be (false)
    s1.contains('e') should be (false)
  }

  it should "be constructable with arbitrary type-correct inputs 2" in {
    s2.contains('a') should be (true)
    s2.contains('b') should be (true)
    s2.contains('c') should be (true)
    s2.contains('d') should be (true)

    s2.contains('e') should be (false)
  }

  it should "be constructable with arbitrary type-correct inputs 3" in {
    s3.contains('a') should be (true)
    s3.contains('b') should be (true)
    s3.contains('x') should be (true)
    s3.contains('y') should be (true)
    s3.contains('z') should be (true)

    s3.contains('c') should be (false)
    s3.contains('w') should be (false)
  }

  it should "compute set union correctly 1" in {
    val s2_or_s3 = s2 ++ s3

    s2_or_s3.contains('a') should be (true)
    s2_or_s3.contains('b') should be (true)
    s2_or_s3.contains('c') should be (true)
    s2_or_s3.contains('d') should be (true)
    s2_or_s3.contains('x') should be (true)
    s2_or_s3.contains('y') should be (true)
    s2_or_s3.contains('z') should be (true)

    s2_or_s3.contains('e') should be (false)
    s2_or_s3.contains('w') should be (false)
  }

  it should "compute set union correctly 2" in {
    val s1_plus_c = s1 ++ CharSet('c')

    s1_plus_c.contains('a') should be (true)
    s1_plus_c.contains('b') should be (true)
    s1_plus_c.contains('c') should be (true)
    s1_plus_c.contains('d') should be (true)

    s1_plus_c.contains('e') should be (false)
  }

  it should "compute set union correctly 3" in {
    val sA = !CharSet('A' -> 'C', 'a' -> 'c', 'h' -> 'k', 'n' -> 'r')
    val sB = !CharSet('b' -> 'i', 'k' -> 's', 'x' -> 'z')
    val expected = CharSet(Char.MinValue -> 'a', 'd' -> 'g', 'j' -> 'j',
      'l' -> 'm', 's' -> Char.MaxValue)

    (sA ++ sB) should equal (expected)
  }

  it should "compute set intersection correctly" in {
    val s2_and_s3 = s2 & s3

    s2_and_s3.contains('a') should equal (true)
    s2_and_s3.contains('b') should equal (true)

    s2_and_s3.contains('c') should equal (false)
    s2_and_s3.contains('d') should equal (false)
    s2_and_s3.contains('x') should equal (false)
    s2_and_s3.contains('y') should equal (false)
    s2_and_s3.contains('z') should equal (false)
  }

  it should "compute set intersection correctly 2" in {
    val sA = CharSet('A' -> 'C', 'a' -> 'c', 'h' -> 'k', 'n' -> 'r')
    val sB = CharSet('b' -> 'i', 'k' -> 's', 'x' -> 'z')

    (sA & sB) should equal (!((!sA) ++ (!sB)))
  }

  it should "compute set complement correctly" in {
    val not_s3 = !s3

    not_s3.contains('c') should be (true)
    not_s3.contains('d') should be (true)
    not_s3.contains('w') should be (true)

    not_s3.contains('a') should be (false)
    not_s3.contains('b') should be (false)
    not_s3.contains('x') should be (false)
    not_s3.contains('y') should be (false)
    not_s3.contains('z') should be (false)
  }

  it should "compute set equality correctly" in {
    ((s1 ++ CharSet('c')) == s2) should be (true)
    ((s2 ++ CharSet('x', 'y', 'z')) == (s3 ++ CharSet('c', 'd'))) should be (true)

    (s1 == s2) should be (false)
    (s1 == s3) should be (false)
    (s2 == s3) should be (false)
  }

  it should "handle extreme ranges correctly" in {
    val all = CharSet(Char.MinValue -> Char.MaxValue)
    val min = CharSet(Char.MinValue -> '0')
    val max = CharSet('2' -> Char.MaxValue)
    val both = min ++ max

    !all should equal (CharSet())
    (min & all) should equal (min)
    (max & all) should equal (max)
    !both should equal (CharSet('1'))
  }
}
