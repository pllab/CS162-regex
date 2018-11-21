# Assignment 5: Ambiguity type system for regular expressions

### Soft deadline: November 29, 11:59PM
### Hard deadline: December 4, 11:59PM

We went over ambiguous regular expressions and presented a type system
to check whether a regular expression is ambiguous. In this assignment
you will implement:
    1. The ambiguity type system we talked about in class
    2. Useful error messages for ambiguous regexes consisting of
        - the subexpression that is the root cause of ambiguity (first
         one encountered if there are more than one).
        - a string that exposes the ambiguity in the subexpression.

**Note about the deadlines:** We will post the next assignment on the
29th but we will not start counting late days for this assignment
until December 4th so that you can partition your time among these two
assignments depending on your schedule during Thanksgiving and the
finals week.

## Project Structure

Files that contain the methods you need to implement:

 - `src/main/scala/`
   - `RichRegex.scala`: You need to implement `unambiguous` in this
     file which checks whether the regex is ambiguous and returns the
     ambiguous subexpression and a string that exposes the ambiguity
     in the subexpression.
   - `Dfa.scala`: You need to implement `getString` in this file which
     will return a string that this DFA accepts if there is one. You
     will use this to generate strings that expose ambiguity.

Files that extend the functionality from the previous assignments (you
need to use your implementation of these files but there is some
additional functionality we give for this assignment so you need to
merge your version with this):

  - `src/main/scala/`
    - `DerivativeAnalysis.scala` - there is an additional method in
      this file that we give called `derivativeClosure`. Make sure to
      preserve this method when you add your version of this file.
    - `RichRegex.scala` - we extended `nullable` and `lessThanEq` to
      handle `Capture` and added some helpers () that help calculating
      overlap. You need to merge these methods to your
      `RichRegex.scala`. Also, note that there is a method in this
      file you need to implement for this assignment: `unambiguous`.
 
   
Files that are used in this assignment you need to copy over (you can
copy over these files verbatim from your previous assignment):

 - `src/main/scala/`
   - `DerivativeMachine.scala` is used by `DerivativeAnalysis` for DFA
     construction, which you will need to compute the string that
     causes the ambiguity.

**Note about grading:** Since the derivative analysis, regex builders,
and the derivative machine are not parts of this assignment, we will
use our own correct version of these so you don't need to worry about
the bugs you have in these.

## Part 1: The ambiguity type system

The judgment rules for the type system we described in class are below:

```
c ∈ Σ
-------------------------------- [Char]
⊢ c : unamb

-------------------------------- [Empty]
⊢ ∅ : unamb

-------------------------------- [Epsilon]
⊢ ε : unamb

⊢ r₁ : unamb   ⊢ r₂ : unamb   L(r₁ & r₂) = ∅
-------------------------------------------- [Choice]
⊢ r₁ | r₂ : unamb

⊢ r₁ : unamb   ⊢ r₂ : unamb   L(r₁ ⊓ r₂) = ∅
-------------------------------------------- [Concat]
⊢ r₁ ~ r₂ : unamb

⊢ r : unamb    L(r ⊓ r*) = ∅
----------------------------- [Star]
⊢ r* : unamb
```

Here, `L(r)` is the language of regular expression `r`, `⊓` is the
overlap operator that we discussed in class.

Note that ambiguity matters only for parsing, and complement and
intersection are non-constructive (i.e., cannot contain capture
groups) hence not used in parsing. So, you we don't have any typing
rules for them.

You need to implement a type checker that uses the typing rules above
to check if the regex is ambiguous, and to find the first ambiguous
subexpression.

## Part 2: The string exposing ambiguity

After finding the ambiguous subexpression in the type checker, you can
build a regex that will describe a non-empty sublanguage of language
of the input regex and is ambiguous for all strings it
recognizes. Then, you can use the `getString` method you implement to find a
string that exposes the ambiguity.

## Part 3: The Tests

You will need to write your own unit tests. Testing your own code *is
part of the grade*. We are looking for the following:
  - Non-trivial tests, i.e., a test that demonstrates that creating a
    charset creates a charset is not useful.
  - Good coverage - we don't expect you to go for a 100% test
    coverage, but we do expect you to cover edge cases. You should
    handle at least the different cases of constructing regexes, the
    cases that exercise different parts of the type system, and
    some edge cases like empty charsets, large charsets, etc.

Ultimately, the quality of the tests is as important as the quantity. You
need to test:
  - Whether the type checker can distinguish the ambiguous and
    unambiguous regexes.
  - Whether the produced ambiguous subexpression is indeed the first
    ambiguous subexpression.
  - Whether the strings produced expose the ambiguity.
  - Correctness of  `getString`.

You can keep the tests you wrote for the previous assignment but you
will not be graded on those for this assignment. For grading purposes,
we will consider only the ambiguity tests at the end of `RegexSpec.scala` and
`getString` tests in `DfaSpec.scala`.

See
[OptionValues](http://www.scalatest.org/user_guide/using_OptionValues)
trait in ScalaTest to see how you can handle the result of the type
checker in your tests. Note that we use that trait in
`RegexSpec.scala` we provide you so you need to make sure that
`RegexSpec` extends `OptionValues` if you want to use the way of
testing optional values we demonstrate in that file.

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
  2. run `turnin assign5@cs162 src`.
  3. Read the instructions on the screen and the list of files you are
       submitting carefully and submit the assignment only if you are
       sure that you are submitting all the files.

Good luck!
