## Interpreter
## Compiler
a program that takes a program in some language as input and generates an equivalent machine-code program

gcc

clang

javac -- java can be thought of as a hybrid compiled/interpreted language

rustc

## Interpreter vs compiler
**execution speed:** compiled code is fast; interpreted code is slow

**development iteration:** compilation can be slow; interpreters start executing immediately

**error checking:** compilers catch (some) bugs at "compile time"

**target platform:** interpreted languages are usually platform independent

## Procedure

source program => tokenizer => parser => semantic analyzer => intermediate code generator => optimizer => code generator

The tokenizer

input source program as a 
