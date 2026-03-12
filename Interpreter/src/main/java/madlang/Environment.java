package madlang;

import java.util.HashMap;
import java.util.Map;

class Environment {
  private final Map<String, Object> values = new HashMap<>();
  private final Environment parent;

  Environment() {
    parent = null;
  }

  Environment(Environment parent) {
    this.parent = parent;
  }

  public Environment createChild() {
    return new Environment(this);
  }

  public Object get(String name, int line, int col) {
    Environment localEnv = this;

    while (true) {
      if ((!localEnv.values.containsKey(name))) {
        if (localEnv.parent == null) {
          throw new MadLangRuntimeError("unbound reference! Identifier '" + name + "' is not defined", line, col);
        } else {
          localEnv = localEnv.parent;
        }
      } else {
        return localEnv.values.get(name);
      }
    }
  }

  public void define(String name, Object value, int line, int col) {
    if (!values.containsKey(name)) {
      values.put(name, value);
    } else {
      throw new MadLangRuntimeError("duplicate declaration on the same identifier '" + name + "'", line, col);
    }
  }

  public void assign(String name, Object value, int line, int col) {
    Environment env = this;
    while (true) {
      if (!env.values.containsKey(name)) {
        if (env.parent == null) {
          throw new MadLangRuntimeError("unbound reference! Undefined variable '" + name + "' cannot be assigned", line, col);
        } else {
          env = env.parent;
        }
      } else {
        break;
      }
    }

    if (value instanceof Interpreter.MadLangFunction) {
      throw new MadLangRuntimeError("type mismatch! Function identifier assignment is not allowed", line, col);
    }

    if (!checkValueType(value, env.values.get(name))) {
      throw new MadLangRuntimeError("type mismatch! Invalid assignment type for variable '" + name + "'", line, col);
    }

    env.values.put(name, value);
  }

  public static boolean checkType(Object o1, Object o2) {
    switch ((VarType)o2) {
      case INT: return o1 instanceof Integer;
      case BOOL: return o1 instanceof Boolean;
      default: return false;
    }
  }

  public static boolean checkValueType(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
      return false;
    }
    if (!(o1 instanceof Integer || o1 instanceof Boolean) ||
            !(o2 instanceof Integer || o2 instanceof Boolean)) {
      return false;
    }
    return o1.getClass() == o2.getClass();
  }
}