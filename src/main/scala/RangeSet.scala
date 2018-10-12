// Provides an implementation for dense sets based on intervals.

package edu.ucsb.cs.cs162.range_set

import scala.annotation.tailrec
import edu.ucsb.cs.cs162.util._

// A RangeSet of Chars.
case class CharSet(contents: IndexedSeq[(Char, Char)])
    extends RangeSet[Char, CharSet](contents) {
  override val minValue = Char.MinValue
  override val maxValue = Char.MaxValue
  override def construct(ranges: IndexedSeq[(Char, Char)]) =
    new CharSet(ranges)
}

object CharSet {
  def apply(ranges: IndexedSeq[(Char, Char)]) =
    new CharSet(ranges)

  def apply(chars: Char*): CharSet =
    new CharSet(chars.map(char => (char, char)).toIndexedSeq)

  def apply(range: (Char, Char), ranges: (Char, Char)*): CharSet =
    new CharSet(range +: ranges.toIndexedSeq)
}

// An abstract base class for dense sets implemented as an ordered sequence of
// intervals of Numeric type. Derived classes must instantiate 'minValue',
// 'maxValue', and 'construct' to get a concrete class.
abstract class RangeSet[T, Self <: RangeSet[T, Self]](
    contents: IndexedSeq[(T, T)])(implicit ev: Numeric[T]) extends CachedHash {
  this: Self =>
  import ev._

  //----------------------------------------------------------------------------
  // Public API.
  //----------------------------------------------------------------------------

  // Returns true iff the set contains 'el'.
  def contains(el: T): Boolean =
    elements.exists { case (min, max) => el >= min && el <= max }

  // Union this set with 'other'.
  def ++(other: Self): Self =
    construct(elements ++ other.elements)

  // Intersect this set with 'other'.
  def &(other: Self): Self = {
    @tailrec
    def collectInCommon(
        rangeA: IndexedSeq[Interval],
        rangeB: IndexedSeq[Interval],
        soFar: IndexedSeq[Interval]): IndexedSeq[Interval] =
      if (rangeA.isEmpty || rangeB.isEmpty) soFar
      else {
        val (minA, maxA) = rangeA.head
        val (minB, maxB) = rangeB.head

        if (maxA < minB) collectInCommon(rangeA.tail, rangeB, soFar)
        else if (maxB < minA) collectInCommon(rangeA, rangeB.tail, soFar)
        else {
          val newMin = if (ev.lt(minA, minB)) minB else minA
          val newMax = if (ev.gt(maxA, maxB)) maxB else maxA

          if (ev.gteq(newMax, maxA)) {
            collectInCommon(rangeA.tail, rangeB, soFar :+ (newMin, newMax))
          }
          else {
            collectInCommon(rangeA, rangeB.tail, soFar :+ (newMin, newMax))
          }
        }
      }

    construct(collectInCommon(elements, other.elements, IndexedSeq()))
  }

  // Complement this set, yielding all elements between minValue and maxValue
  // that aren't contained in this set.
  def unary_! : Self =
    if (elements.isEmpty) construct(IndexedSeq(minValue -> maxValue))
    else {
      // Compute the new least interval (empty if the current least interval
      // reaches the minimum value).
      val newFirst = elements.head match {
        case (min, _) if min == minValue => IndexedSeq()
        case (min, _) => IndexedSeq(minValue -> (min - one))
      }

      // Compute the new greatest interval (empty if the current greatest
      // interval reaches the maximum value).
      val newLast = elements.last match {
        case (_, max) if max == maxValue => IndexedSeq()
        case (_, max) => IndexedSeq((max + one) -> maxValue)
      }

      // Compute the new middle intervals, i.e., all intervals that fall
      // in-between the current intervals.
      val newMiddle =
        if (elements.size == 1) IndexedSeq()
        else elements.sliding(2).foldLeft(IndexedSeq[Interval]()) {
          case (acc, Seq((_, leftMax), (rightMin, _))) =>
            assert(leftMax != maxValue && rightMin != minValue)
            acc :+ (leftMax + one, rightMin - one)
        }

      construct(newFirst ++ newMiddle ++ newLast)
    }

  // Return the smallest element contained in the set, or None if the set is
  // empty.
  def minElement: Option[T] =
    if (elements.isEmpty) None
    else Some(elements.head._1)

  // Returns true iff the set is empty.
  def isEmpty: Boolean =
    elements.isEmpty

  // Set equality.
  override def equals(other: Any): Boolean = other match {
    case set: RangeSet[T, _] => elements == set.elements
    case _ => false
  }

  override def toString =
    elements.map(interval =>
      if (interval._1 == interval._2) interval._1.toString
      else interval.toString
    ).mkString(", ")

  //----------------------------------------------------------------------------
  // Protected details.
  //----------------------------------------------------------------------------

  // The minimum and maximum values possible for type T.
  protected def minValue: T
  protected def maxValue: T

  // Construct an object of type Self.
  protected def construct(contents: IndexedSeq[Interval]): Self

  //----------------------------------------------------------------------------
  // Private details.
  //----------------------------------------------------------------------------

  type Interval = (T, T)

  // The elements of this set as an ordered sequence of intervals that do not
  // touch or overlap.
  private val elements: IndexedSeq[Interval] =
    // Sort the intervals lexicographically and coalesce any touching or
    // overlapping intervals.
    contents.sorted.foldLeft(IndexedSeq[Interval]())(
      (acc, range) => {
        if (acc.isEmpty) IndexedSeq(range)
        else {
          val (min, max) = range
          val (prevMin, prevMax) = acc.last

          if (prevMax >= max) acc
          else if (prevMax >= saturatingDecrement(min)) {
            acc.init :+ (prevMin, max)
          }
          else acc :+ range
        }})

  // Increment the given value but saturate at the maximum value.
  private def saturatingIncrement(element: T): T =
    if (element == maxValue) element else element + one

  // Decrement the given value but saturate at the minimum value.
  private def saturatingDecrement(element: T): T =
    if (element == minValue) element else element - one

  // A convenient shorthand for the value 1 in type T. If it isn't a lazy val
  // then 'one' gets the value 'null'.
  private lazy val one = ev.fromInt(1)
}
