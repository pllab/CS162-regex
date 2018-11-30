# Assignment 6: Attack synthesis

### Hard deadline: December 14, 11:59PM
### Note: You cannot use slip days for this assignment!

We talked about synthesizing attack strings that will trigger exponential behaviour in backtracking parsers. For this assignment, you will be implementing attack synthesis.

An attack for a regular expression `r` consists of a prefix string that gets the parser to the ambiguous subxpression, a pumpable string that can be repeated an arbitrary number of times, and and a suffix that causes parsing to fail, triggering backtracking.

## Project Structure

Files that contain the methods you need to implement:

 - `src/main/scala/`
   - `AttackSynthesis.scala` - Contains the `AttackSynthesis` object and the `Attack` case class. You will be implementing methods in `AttackSynthesis`.
    
Files that are used in this assignment you need to copy over (you can
copy over these files verbatim from your previous assignment):

 - `src/main/scala/`
   - `DerivativeAnalysis.scala` - is used in `AttackSynthesis` to construct a DFA for a regex. This will be useful for getting the prefix, suffix and pumpable string in attack synthesis.
   - `DerivativeMachine.scala` is used by `DerivativeAnalysis` for DFA
     construction
   - `RecursiveBacktrackingVm.scala.scala` and `PowersetVM.scala` - use them to convince youselves that one is succeptible to the exponential backtracking attack, and the other isn't.

**Note about grading:** Since the derivative analysis, regex builders,
and the derivative machine are not parts of this assignment, we will
use our own correct version of these so you don't need to worry about
the bugs you have in these.

## Part 1: Attack synthesis

You will need to complete the `AttacSynthesis` object. 
You will implement the following methods:

  - `synthesizeAttack`, which takes a regular expression and returns a synthesized attack, if one exists
  - `computeAttack`, which prepares an attack (if one exists) by finding the:
    - prefix regex - a regex describing strings that lead to the ambiguous subexpression
    - pumpable regex - a regex of valid pumpable strings
    - suffix regex - a regex describing strings that will cause matching to fail when 
    - pumpable string - the string that exposes the ambiguity of the pumpable regex
  - `getPumpable` - which computes an ambiguous string recognized by a regular expression, if one exists

Note: Like in the previous assignment, because our capturing parser doesn't hande intersection and complement, you don't need to handle those cases in attack synthesis.

## Part 2: The Tests

You will need to write your own unit tests. Testing your own code *is part of the grade*. We are looking for the following:
  - Non-trivial tests, i.e., a test that demonstrates that creating a
    charset creates a charset is not useful.
  - Good coverage - we don't expect you to go for a 100% test
    coverage, but we do expect you to cover edge cases. You should
    handle test that your attack synthesis will generate an appropriate attack for a vulnerable regex, and nothing for a non-pumpable regex. You should cover edge cases you can think of.

Ultimately, the quality of the tests is as important as the quantity. You
need to test:
  - Whether your attack synthesis can distinguish pumpable and non-pumpable regexes
  - Whether it returns a correct prefix string
  - Whether it returns a correct pumpable string
  - Whether it returns a correct suffix

You can keep the tests you wrote for the previous assignment but you
will not be graded on those for this assignment. For grading purposes,
we will consider only the tests in `AttackSynthesisSpec.scala`.

See
[OptionValues](http://www.scalatest.org/user_guide/using_OptionValues)
trait in ScalaTest to see how you can handle optional results in your tests. Note that we use that trait in
`RegexSpec.scala` we provide you so you need to make sure that
`RegexSpec` extends `OptionValues` if you want to use that way of
testing optional values.

## Rules

You must use only the purely functional subset of Scala. This means
that you are not allowed to use mutations; more explicitly you must
not use any of:
  - Mutable variables, i.e., those created using the `var` keyword,
  - Mutable collections, e.g. anything under
    `scala.collection.mutable`,
  - The `Array` data type.

If you use any mutation, you will automatically fail the assignment.

Your code must compile. Invoking `compile` and `test:compile` in the
SBT shell (as described in the first tutorial) must
succeed. Otherwise, you will automatically fail the assignment.

## Submission

We will use only the contents of the `src` directory for grading so
make sure that all your code is in the proper directories under it.
You will use `turnin` on CSIL to submit your assignment.  To submit
your assignment, on the root directory of the repository you cloned,

  1. Make sure that you run the unit tests on CSIL and get the result
       you expect.
  2. run `turnin assign6@cs162 src`.
  3. Read the instructions on the screen and the list of files you are
       submitting carefully and submit the assignment only if you are
       sure that you are submitting all the files.

Good luck!
