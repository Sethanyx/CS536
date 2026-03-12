package madlang;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Main {

  // Helper function to print beautiful GCC-like error outputs
  private static void printGccStyleError(String filename, int line, int col, String message, String source) {
    System.err.println(filename + ":" + line + ":" + col + ": error: " + message);
    String[] lines = source.split("\\r?\\n");
    if (line > 0 && line <= lines.length) {
      String errorLine = lines[line - 1];
      System.err.println(String.format("%5d | %s", line, errorLine));
      System.err.print("      | ");
      for (int i = 0; i < col; i++) {
        System.err.print(" ");
      }
      System.err.println("^");
    }
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Error: missing input file.");
      System.err.println("usage: make run FILE=filename.madl");
      System.exit(1);
      return;
    }

    String filename = args[0];
    String source;
    try {
      source = Files.readString(Path.of(filename));
    } catch (IOException e) {
      System.err.println("Error: failed to read input file: " + filename);
      System.err.println("Reason: " + e.getMessage());
      System.exit(1);
      return;
    }

    if (source.isEmpty()) {
      System.err.println("Warning: input file is empty: " + filename);
      return;
    }

    try {
      // Lexical Analysis
      CharStream charStream = CharStreams.fromString(source);
      ProgramLexer lexer = new ProgramLexer(charStream);
      CommonTokenStream tokens = new CommonTokenStream(lexer);

      // Syntax Analysis
      ProgramParser parser = new ProgramParser(tokens);

      // A mutable flag to stop parsing and error reporting after the very first error.
      // This prevents ANTLR's default recovery mechanism from producing cascading, nonsensical errors.
      final boolean[] hasParseError = {false};

      // Bonus: Custom listener for Parse-time errors with clear human-readable explanations
      parser.removeErrorListeners();
      parser.addErrorListener(new BaseErrorListener() {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
          // If we already caught an error, suppress subsequent cascaded errors
          if (hasParseError[0]) {
            return;
          }
          hasParseError[0] = true;

          String humanReadableMsg = msg; // Default to the original ANTLR message

          // Translate raw ANTLR mismatched/viable alternative errors into MadLang-specific instructions
          if (msg.contains("mismatched input '->' expecting ':'")) {
              humanReadableMsg = "Invalid function return type syntax. Use ':' instead of '->' (e.g., 'fn main() : int').";
          } else if (msg.contains("no viable alternative at input") && (msg.contains("'int'") || msg.contains("'bool'"))) {
              humanReadableMsg = "Invalid variable declaration. MadLang requires 'name : type = value;' (e.g., 'a : int = 5;').";
          } else if (msg.contains("mismatched input '=' expecting ':'") || msg.contains("expecting ':'")) {
              humanReadableMsg = "Missing type declaration. Variables and parameters must be declared with a colon (e.g., 'name : type').";
          } else if (msg.contains("expecting ';'")) {
              humanReadableMsg = "Missing semicolon. Statements must end with ';'.";
          } else if (msg.contains("expecting '{'")) {
              humanReadableMsg = "Missing opening brace '{' for the block.";
          }

          printGccStyleError(filename, line, charPositionInLine, "Syntax Error: " + humanReadableMsg, source);
        }
      });

      ParseTree tree = parser.program();

      // Exit immediately if there was a syntax error so the ASTBuilder doesn't try to visit a broken tree
      if (hasParseError[0] || parser.getNumberOfSyntaxErrors() > 0) {
        System.exit(1);
      }

      // AST Construction
      ASTBuilder astBuilder = new ASTBuilder();
      @SuppressWarnings("unchecked")
      List<Stmt> programAst = (List<Stmt>) astBuilder.visit(tree);

      // Interpretation
      Interpreter interpreter = new Interpreter();
      interpreter.interpret(programAst);

    } catch (MadLangRuntimeError e) {
      // Bonus: Catch Runtime Semantics Error and format
      printGccStyleError(filename, e.line, e.col, e.getMessage(), source);
      System.exit(1);
    } catch (Exception e) {
      System.err.println("Runtime Error: " + e.getMessage());
      System.exit(1);
    }
  }
}