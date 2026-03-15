# Madlang Parser

This project implements a lexer and parser for the `madlang` programming language using Java and ANTLR4. 

## Prerequisites

To build and run this project, ensure you have the following installed on your system:
* Java Development Kit (JDK)
* Make
* `curl` (used automatically by the Makefile to download the ANTLR dependency)

## Project Structure

* `src/main/antlr4/madlang/Program.g4`: The ANTLR4 grammar file defining the lexer and parser rules for `madlang`.
* `src/main/java/madlang/`: The Java source directory containing your `Main` class and custom visitor implementations.
* `Makefile`: Contains build automation targets for generating parser code, compiling, and running the project.
* `tools/`: Automatically created to store the downloaded `antlr-4.13.2-complete.jar`.
* `out/`: Automatically created to store the compiled `.class` files.

## Build and Run Instructions

This project uses `make` to automate the build process. All commands should be run from the root directory of the project.

### 1. Generate Parser and Compile

To download the ANTLR tool, generate the Lexer, Parser, and Visitor classes from your grammar, and compile all Java source files, run:

```bash
make build

make run FILE=path/to/your/test_file.mad

make clean
```