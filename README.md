# Assignment 4: Regex to NFA compiler and powerset VM

### Deadline: November 20th, 11:59PM

This week we talked about compiling regular expressions to NFA.
In this assignment, you will be implementing:

  - The regex to NFA compiler
  - Powerset VM
  - Tests for your code

## Project Structure

 - `src/main/scala/Compiler.scala` - Contains the skeleton of the compiler. Your code goes here.
 - `src/main/scala/VirtualMachine.scala` - Contains the instruction case classes and objects, and an abstract vm interface. Do not alter this.
 - `src/main/scala/RecursiveBacktrackingVm.scala` - Contains the recursive backtracking VM implemented in class. You can use it to check what your compiler does while developing it.

### Note: This repository will be updated after class on Thursday (Nov 8th) to add skeleton code for Part 2 of the assignment.

## Part 1: The Regex to NFA Compiler

The first part of the assignment is to implement the compiler. Your compiler will take a `Regex`, and produce a `Program`, a sequence of `Instruction`s. Fill out the skeleton method in Compiler.scala.

For each case of `Regex`, your compiler will return a sequence of instructions:

 - `∅` - Produces a program that immediately rejects

 - `ε` - Produces a program that will push an empty leaf node onto the output stack

 - `cs ⊂ Σ` - Produces a program which matches a character in the given charset, and pushes the mached character onto the output stack

 - `r ~ s` - Produces a program that tries to match r and s in sequence, and push a concat node onto the output stack

 - `r | s` -> It produces a program that forks the thread so that one executes r, and the other s. The forked threads should emit a PushLeft and PushRight as their last instructions respectively.

 - `r*` -> Produces a program that forks into two threads - one jumps out of the Kleene star (decides to stop matching), the other executes the body of `r` and jumps back to the start of the Kleene star. If `r` is nullable, your compiler should emit a `CheckProgress` at the appropriate place in the loop to avoid falling into infinite loops.

## Part 2: Powerset VM

You will be implementing a virtual machine that uses Thompson's powerset strategy to implement a non-backtracking algorithm for regular expression matching.
The VM will be described in class on Nov 8th, and this description and the repo will be updated after the lecture. We encourage you to start work on part 1 as soon as possible.

## Part 3: The Tests

You will need to write your own unit tests. Testing your own code *is
part of the grade*. We are looking for the following:
  - Non-trivial tests, i.e., a test that demonstrates that creating a
    charset creates a charset is not very useful.
  - Decent coverage - we don't expect you to go for a 100% test
    coverage, but we do expect you to cover edge cases.

Ultimately, the quality of the tests is as important as the quantity. You
need to test:
 - The compiler you implemented should be thoroughly tested. Put those tests in `CompilerSpec.scala.`. Make sure you cover the most basic cases as well as the more complicated and weird ones. 
 - The VM you implemented - make sure it behaves correctly for base cases, more complex cases and edge cases you can think of. Put these tests in `PowersetVmSpec.scala`, once it becomes available in the repo.

You can keep the tests you wrote for the previous assignment but you
will not be graded on those for this assignment. For grading purposes,
we will consider only the tests in `CompilerSpec.scala` and
`PowersetVmSpec.scala`.

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
  2. run `turnin assign4@cs162 src`.
  3. Read the instructions on the screen and the list of files you are
       submitting carefully and submit the assignment only if you are
       sure that you are submitting all the files.

Good luck!
