package madlang;

import java.util.List;

abstract class Expr {
  interface Visitor<R> {
    R visitBinaryExpr(Binary expr);
    R visitLiteralExpr(Literal expr);
    R visitUnaryExpr(Unary expr);
    R visitVariableExpr(Variable expr);
    R visitCallExpr(Call expr);
  }

  // Common fields for location tracking
  final int line;
  final int col;

  Expr(int line, int col) {
    this.line = line;
    this.col = col;
  }

  static class Binary extends Expr {
    Binary(Expr left, Operator operator, Expr right, int line, int col) {
      super(line, col);
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBinaryExpr(this);
    }

    final Expr left;
    final Operator operator;
    final Expr right;
  }

  static class Literal extends Expr {
    Literal(Object value, int line, int col) {
      super(line, col);
      this.value = value;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitLiteralExpr(this);
    }

    final Object value;
  }

  static class Unary extends Expr {
    Unary(Operator operator, Expr right, int line, int col) {
      super(line, col);
      this.operator = operator;
      this.right = right;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitUnaryExpr(this);
    }

    final Operator operator;
    final Expr right;
  }

  static class Variable extends Expr {
    Variable(String name, int line, int col) {
      super(line, col);
      this.name = name;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitVariableExpr(this);
    }

    final String name;
  }

  static class Call extends Expr {
    Call(String name, List<Expr> arguments, int line, int col) {
      super(line, col);
      this.name = name;
      this.arguments = arguments;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitCallExpr(this);
    }

    final String name;
    final List<Expr> arguments;
  }

  abstract <R> R accept(Visitor<R> visitor);
}