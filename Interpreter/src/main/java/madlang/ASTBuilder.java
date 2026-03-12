package madlang;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends ProgramBaseVisitor<Object> {

    @Override
    @SuppressWarnings("unchecked")
    public Object visitProgram(ProgramParser.ProgramContext ctx) {
        List<Stmt> statements = new ArrayList<>();
        for (ProgramParser.DeclContext declCtx : ctx.decl()) {
            statements.add((Stmt) visit(declCtx));
        }
        return statements;
    }

    @Override
    public Object visitGlobalVarDecl(ProgramParser.GlobalVarDeclContext ctx) {
        String name = ctx.IDENT().getText();
        VarType type = parseType(ctx.type());

        Expr initializer = null;
        if (ctx.expr() != null) {
            initializer = (Expr) visit(ctx.expr());
        }
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Stmt.Var(name, type, initializer, line, col);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visitFunDecl(ProgramParser.FunDeclContext ctx) {
        String name = ctx.IDENT().getText();
        VarType returnType = parseType(ctx.type());

        List<Stmt.Parameter> parameters = new ArrayList<>();
        if (ctx.params() != null) {
            parameters = (List<Stmt.Parameter>) visit(ctx.params());
        }

        Stmt.Block block = (Stmt.Block) visit(ctx.block());

        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Stmt.Function(name, returnType, parameters, block.statements, line, col);
    }

    @Override
    public Object visitParams(ProgramParser.ParamsContext ctx) {
        List<Stmt.Parameter> params = new ArrayList<>();
        for (ProgramParser.ParamContext paramCtx : ctx.param()) {
            params.add((Stmt.Parameter) visit(paramCtx));
        }
        return params;
    }

    @Override
    public Object visitParam(ProgramParser.ParamContext ctx) {
        String name = ctx.IDENT().getText();
        VarType type = parseType(ctx.type());
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Stmt.Parameter(name, type, line, col);
    }

    @Override
    public Object visitBlock(ProgramParser.BlockContext ctx) {
        List<Stmt> statements = new ArrayList<>();
        for (ProgramParser.StmtContext stmtCtx : ctx.stmt()) {
            statements.add((Stmt) visit(stmtCtx));
        }
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Stmt.Block(statements, line, col);
    }

    @Override
    public Object visitVarDef(ProgramParser.VarDefContext ctx) {
        String name = ctx.IDENT().getText();
        VarType type = parseType(ctx.type());

        Expr initializer = null;
        if (ctx.expr() != null) {
            initializer = (Expr) visit(ctx.expr());
        }
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Stmt.Var(name, type, initializer, line, col);
    }

    @Override
    public Object visitFunDef(ProgramParser.FunDefContext ctx) {
        return visit(ctx.funDecl());
    }

    @Override
    public Object visitAssignStmt(ProgramParser.AssignStmtContext ctx) {
        String name = ctx.IDENT().getText();
        Expr value = (Expr) visit(ctx.expr());
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Stmt.Assign(name, value, line, col);
    }

    @Override
    public Object visitIfStmt(ProgramParser.IfStmtContext ctx) {
        Expr condition = (Expr) visit(ctx.expr());
        Stmt thenBranch = (Stmt) visit(ctx.stmt(0));

        Stmt elseBranch = null;
        if (ctx.stmt().size() > 1) {
            elseBranch = (Stmt) visit(ctx.stmt(1));
        }
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Stmt.If(condition, thenBranch, elseBranch, line, col);
    }

    @Override
    public Object visitWhileStmt(ProgramParser.WhileStmtContext ctx) {
        Expr condition = (Expr) visit(ctx.expr());
        Stmt body = (Stmt) visit(ctx.block());
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Stmt.While(condition, body, line, col);
    }

    @Override
    public Object visitReturnStmt(ProgramParser.ReturnStmtContext ctx) {
        Expr value = (Expr) visit(ctx.expr());
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Stmt.Return(value, line, col);
    }

    @Override
    public Object visitExprStmt(ProgramParser.ExprStmtContext ctx) {
        Expr expr = (Expr) visit(ctx.expr());
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Stmt.Expression(expr, line, col);
    }

    @Override
    public Object visitMultDiv(ProgramParser.MultDivContext ctx) {
        Expr left = (Expr) visit(ctx.expr(0));
        Expr right = (Expr) visit(ctx.expr(1));
        Operator op = getOperator(ctx.getChild(1).getText());
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Binary(left, op, right, line, col);
    }

    @Override
    public Object visitAddSub(ProgramParser.AddSubContext ctx) {
        Expr left = (Expr) visit(ctx.expr(0));
        Expr right = (Expr) visit(ctx.expr(1));
        Operator op = getOperator(ctx.getChild(1).getText());
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Binary(left, op, right, line, col);
    }

    @Override
    public Object visitRelation(ProgramParser.RelationContext ctx) {
        Expr left = (Expr) visit(ctx.expr(0));
        Expr right = (Expr) visit(ctx.expr(1));
        Operator op = getOperator(ctx.getChild(1).getText());
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Binary(left, op, right, line, col);
    }

    @Override
    public Object visitEquality(ProgramParser.EqualityContext ctx) {
        Expr left = (Expr) visit(ctx.expr(0));
        Expr right = (Expr) visit(ctx.expr(1));
        Operator op = getOperator(ctx.getChild(1).getText());
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Binary(left, op, right, line, col);
    }

    @Override
    public Object visitAnd(ProgramParser.AndContext ctx) {
        Expr left = (Expr) visit(ctx.expr(0));
        Expr right = (Expr) visit(ctx.expr(1));
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Binary(left, Operator.AND, right, line, col);
    }

    @Override
    public Object visitOr(ProgramParser.OrContext ctx) {
        Expr left = (Expr) visit(ctx.expr(0));
        Expr right = (Expr) visit(ctx.expr(1));
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Binary(left, Operator.OR, right, line, col);
    }

    @Override
    public Object visitUnary(ProgramParser.UnaryContext ctx) {
        Operator op = getOperator(ctx.getChild(0).getText());
        Expr right = (Expr) visit(ctx.expr());
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Unary(op, right, line, col);
    }

    @Override
    public Object visitPrimaryExpr(ProgramParser.PrimaryExprContext ctx) {
        return visit(ctx.primary());
    }

    @Override
    public Object visitIntLit(ProgramParser.IntLitContext ctx) {
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Literal(Integer.parseInt(ctx.INT_LIT().getText()), line, col);
    }

    @Override
    public Object visitTrueLit(ProgramParser.TrueLitContext ctx) {
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Literal(true, line, col);
    }

    @Override
    public Object visitFalseLit(ProgramParser.FalseLitContext ctx) {
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Literal(false, line, col);
    }

    @Override
    public Object visitIdent(ProgramParser.IdentContext ctx) {
        String name = ctx.IDENT().getText();
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Variable(name, line, col);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visitFunCall(ProgramParser.FunCallContext ctx) {
        String name = ctx.IDENT().getText();
        List<Expr> arguments = new ArrayList<>();
        if (ctx.args() != null) {
            arguments = (List<Expr>) visit(ctx.args());
        }
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        return new Expr.Call(name, arguments, line, col);
    }

    @Override
    public Object visitParens(ProgramParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Object visitArgs(ProgramParser.ArgsContext ctx) {
        List<Expr> arguments = new ArrayList<>();
        for (ProgramParser.ExprContext exprCtx : ctx.expr()) {
            arguments.add((Expr) visit(exprCtx));
        }
        return arguments;
    }

    private VarType parseType(ProgramParser.TypeContext ctx) {
        String typeStr;
        if (ctx.getChildCount() == 3) {
            typeStr = ctx.baseType(1).getText();
        } else {
            typeStr = ctx.baseType(0).getText();
        }
        return typeStr.equals("int") ? VarType.INT : VarType.BOOL;
    }

    private Operator getOperator(String opText) {
        switch (opText) {
            case "+": return Operator.PLUS;
            case "-": return Operator.MINUS;
            case "*": return Operator.MULTIPLY;
            case "/": return Operator.DIVIDE;
            case "%": return Operator.MODULO;
            case "==": return Operator.EQUAL;
            case "!=": return Operator.NOT_EQUAL;
            case "<": return Operator.LESS;
            case "<=": return Operator.LESS_EQUAL;
            case ">": return Operator.GREATER;
            case ">=": return Operator.GREATER_EQUAL;
            case "!": return Operator.NOT;
            default: throw new RuntimeException("Unknown operator: " + opText);
        }
    }
}