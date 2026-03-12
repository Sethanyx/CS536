package madlang;

import java.util.ArrayList;
import java.util.List;

class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Object> {

  interface MadLangCallable {
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments, int line, int col);
  }

  class MadLangFunction implements MadLangCallable {
    final Stmt.Function func;
    final Environment env;

    MadLangFunction(Stmt.Function func, Environment env) {
      this.func = func;
      this.env = env;
    }

    @Override
    public int arity() {
      return func.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments, int line, int col) {
      Environment localEnv = env.createChild();
      if (arguments.size() != arity()) {
        throw new MadLangRuntimeError("Inconsistent number of arguments and parameters", line, col);
      }

      for (int i = 0; i < arity(); i++) {
        if (Environment.checkType(arguments.get(i), func.params.get(i).type())) {
          localEnv.define(func.params.get(i).name(), arguments.get(i), line, col);
        } else {
          throw new MadLangRuntimeError("type mismatch in argument passing", line, col);
        }
      }

      Environment previous = interpreter.environment;
      try{
        interpreter.environment = localEnv;
        for (Stmt stmt : func.body) {
          stmt.accept(interpreter);
        }
      } catch(ReturnException returnValue) {
        if (!Environment.checkType(returnValue.value, func.returnType)) {
          throw new MadLangRuntimeError("type mismatch! Return value does not match function return type", returnValue.line, returnValue.col); // use call site location
        }
        return returnValue.value;
      } finally {
        interpreter.environment = previous;
      }

      return interpreter.initialize(func.returnType);
    }
  }

  class ReturnException extends RuntimeException {
    final Object value;
    final int line;
    final int col;

    ReturnException(Object value, int line, int col) {
      super(null, null, false, false);
      this.value = value;
      this.line = line;
      this.col = col;
    }
  }

  Environment globals = new Environment();
  Environment environment = globals;

  Interpreter() {
    globals.define("input", new MadLangCallable() {
      @Override public int arity() { return 0; }
      @Override public Object call(Interpreter interpreter, List<Object> arguments, int line, int col) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        try {
          return scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
          throw new MadLangRuntimeError("type mismatch! Expected integer input", line, col);
        }
      }
    }, 0, 0);

    globals.define("output", new MadLangCallable() {
      @Override public int arity() { return 1; }
      @Override public Object call(Interpreter interpreter, List<Object> arguments, int line, int col) {
        Object arg = arguments.get(0);
        if (!(arg instanceof Integer)) {
          throw new MadLangRuntimeError("type mismatch! output requires an int argument", line, col);
        }
        System.out.println((Integer) arg);
        return 0;
      }
    }, 0, 0);
  }

  void interpret(List<Stmt> program) {
    for (Stmt s : program) {
      s.accept(this);
    }

    Object mainFunc = null;
    try {
      mainFunc = environment.get("main", 0, 0);
    } catch (MadLangRuntimeError ignore) {
    }

    if (mainFunc instanceof MadLangCallable) {
      ((MadLangCallable) mainFunc).call(this, new ArrayList<>(), 0, 0);
    }
  }

  public Object initialize(Object type) {
    switch ((VarType)type) {
      case BOOL: return false;
      case INT: return 0;
      default: throw new RuntimeException("Unknown type for initialization");
    }
  }

  @Override
  public Void visitFunctionStmt(Stmt.Function stmt) {
    if (stmt.name.equals("main")) {
      if (stmt.returnType != VarType.INT) {
        throw new MadLangRuntimeError("type mismatch! The main function must have return type int", stmt.line, stmt.col);
      }
      if (!stmt.params.isEmpty()) {
        throw new MadLangRuntimeError("The main function must take no parameters", stmt.line, stmt.col);
      }
    }
    environment.define(stmt.name, new MadLangFunction(stmt, environment.createChild()), stmt.line, stmt.col);
    return null;
  }

  @Override
  public Void visitIfStmt(Stmt.If stmt) {
    Object conditionB = stmt.condition.accept(this);
    if (!(conditionB instanceof Boolean)) {
      throw new MadLangRuntimeError("type mismatch! Condition of if statement requires Boolean", stmt.line, stmt.col);
    }

    if ((Boolean)conditionB) {
      stmt.thenBranch.accept(this);
    } else if (stmt.elseBranch != null) {
      stmt.elseBranch.accept(this);
    }

    return null;
  }

  @Override
  public Void visitReturnStmt(Stmt.Return stmt) {
    if (stmt.value == null) {
      throw new MadLangRuntimeError("Return statement without an expression is not allowed", stmt.line, stmt.col);
    }
    Object value = stmt.value.accept(this);
    throw new ReturnException(value, stmt.value.line, stmt.value.col);
  }

  @Override
  public Void visitBlockStmt(Stmt.Block stmt) {
    Environment previous = this.environment;
    this.environment = new Environment(previous);
    try {
      for (Stmt s : stmt.statements) {
        s.accept(this);
      }
    } finally {
      this.environment = previous;
    }
    return null;
  }

  @Override
  public Void visitVarStmt(Stmt.Var stmt) {
    if (stmt.type == null) {
      throw new MadLangRuntimeError("Variable declaration requires variable type", stmt.line, stmt.col);
    }

    Object value = (stmt.initializer != null) ? stmt.initializer.accept(this) : initialize(stmt.type);

    if (Environment.checkType(value, stmt.type)) {
      this.environment.define(stmt.name, value, stmt.line, stmt.col);
    } else {
      throw new MadLangRuntimeError("type mismatch in variable declaration", stmt.line, stmt.col);
    }
    return null;
  }

  @Override
  public Void visitWhileStmt(Stmt.While stmt) {
    while (true) {
      Object condition = stmt.condition.accept(this);
      if (!Environment.checkType(condition, VarType.BOOL)) {
        throw new MadLangRuntimeError("type mismatch! Condition requires boolean expression", stmt.line, stmt.col);
      }

      if (!(Boolean) condition) {
        break;
      }
      stmt.body.accept(this);
    }
    return null;
  }

  @Override
  public Void visitExpressionStmt(Stmt.Expression stmt) {
    stmt.expression.accept(this);
    return null;
  }

  @Override
  public Void visitAssignStmt(Stmt.Assign stmt) {
    environment.assign(stmt.name, stmt.value.accept(this), stmt.line, stmt.col);
    return null;
  }

  @Override
  public Object visitBinaryExpr(Expr.Binary expr) {
    Expr leftE = expr.left;
    Expr rightE = expr.right;
    Object leftValue, rightValue;

    if (expr.operator == Operator.OR) {
      leftValue = leftE.accept(this);
      if (!(leftValue instanceof Boolean)) {
        throw new MadLangRuntimeError("type mismatch! Logical operators require Boolean operands", expr.line, expr.col);
      }
      if ((Boolean) leftValue) return true;

      rightValue = rightE.accept(this);
      if (!(rightValue instanceof Boolean)) {
        throw new MadLangRuntimeError("type mismatch! Logical operators require Boolean operands", expr.line, expr.col);
      }
      return rightValue;
    }

    if (expr.operator == Operator.AND) {
      leftValue = leftE.accept(this);
      if (!(leftValue instanceof Boolean)) {
        throw new MadLangRuntimeError("type mismatch! Logical operators require Boolean operands", expr.line, expr.col);
      }
      if (!(Boolean) leftValue) return false;

      rightValue = rightE.accept(this);
      if (!(rightValue instanceof Boolean)) {
        throw new MadLangRuntimeError("type mismatch! Logical operators require Boolean operands", expr.line, expr.col);
      }
      return rightValue;
    }

    if (expr.operator == Operator.EQUAL || expr.operator == Operator.GREATER_EQUAL
            || expr.operator == Operator.GREATER || expr.operator == Operator.LESS
            || expr.operator == Operator.LESS_EQUAL || expr.operator == Operator.NOT_EQUAL) {

      leftValue = leftE.accept(this);
      rightValue = rightE.accept(this);

      if (!(leftValue instanceof Integer) || !(rightValue instanceof Integer)) {
        throw new MadLangRuntimeError("type mismatch! Comparison operators require Integer operands", expr.line, expr.col);
      }

      int left = (Integer)leftValue;
      int right = (Integer)rightValue;

      switch (expr.operator) {
        case EQUAL: return left == right;
        case GREATER: return left > right;
        case GREATER_EQUAL: return left >= right;
        case LESS: return left < right;
        case LESS_EQUAL: return left <= right;
        case NOT_EQUAL: return left != right;
        default: throw new MadLangRuntimeError("Unknown comparison operator", expr.line, expr.col);
      }
    }

    if (expr.operator == Operator.PLUS || expr.operator == Operator.MINUS
            || expr.operator == Operator.MULTIPLY || expr.operator == Operator.DIVIDE
            || expr.operator == Operator.MODULO) {

      leftValue = leftE.accept(this);
      rightValue = rightE.accept(this);

      if (!(leftValue instanceof Integer) || !(rightValue instanceof Integer)) {
        throw new MadLangRuntimeError("type mismatch! Arithmetic operators require Integer operands", expr.line, expr.col);
      }

      int left = (Integer)leftValue;
      int right = (Integer)rightValue;

      switch (expr.operator) {
        case PLUS: return left + right;
        case MINUS: return left - right;
        case MULTIPLY: return left * right;
        case DIVIDE:
          if (right != 0) return left / right; else throw new MadLangRuntimeError("arithmetic error! Divide by zero error", expr.line, expr.col);
        case MODULO:
          if (right != 0) return left % right; else throw new MadLangRuntimeError("arithmetic error! Modulo by zero error", expr.line, expr.col);
        default: throw new MadLangRuntimeError("Unknown arithmetic operator", expr.line, expr.col);
      }
    }
    throw new MadLangRuntimeError("Unknown binary operator", expr.line, expr.col);
  }

  @Override
  public Object visitLiteralExpr(Expr.Literal expr) {
    return expr.value;
  }

  @Override
  public Object visitUnaryExpr(Expr.Unary expr) {
    Object value = expr.right.accept(this);

    switch (expr.operator) {
      case NOT:
        if (!(value instanceof Boolean)) {
          throw new MadLangRuntimeError("type mismatch! NOT operator requires Boolean operand", expr.line, expr.col);
        }
        return !(Boolean)value;
      case MINUS:
        if (!(value instanceof Integer)) {
          throw new MadLangRuntimeError("type mismatch! MINUS operator requires Integer operand", expr.line, expr.col);
        }
        return -(Integer)value;
      default:
        throw new MadLangRuntimeError("Unknown unary operator", expr.line, expr.col);
    }
  }

  @Override
  public Object visitVariableExpr(Expr.Variable expr) {
    return environment.get(expr.name, expr.line, expr.col);
  }

  @Override
  public Object visitCallExpr(Expr.Call expr) {
    Object callee = environment.get(expr.name, expr.line, expr.col);

    if (!(callee instanceof MadLangCallable)) {
      throw new MadLangRuntimeError("Not callable expression is not allowed to use call expression", expr.line, expr.col);
    }

    MadLangCallable function = (MadLangCallable) callee;

    if (expr.arguments.size() != function.arity()) {
      throw new MadLangRuntimeError("Inconsistent number of arguments and parameters", expr.line, expr.col);
    }

    List<Object> arguments = new ArrayList<>();
    for (int i = 0; i < expr.arguments.size(); i++) {
      if (expr.arguments.get(i) != null) {
        arguments.add(expr.arguments.get(i).accept(this));
      } else {
        throw new MadLangRuntimeError("Null is not allowed as an argument", expr.line, expr.col);
      }
    }

    return function.call(this, arguments, expr.line, expr.col);
  }
}