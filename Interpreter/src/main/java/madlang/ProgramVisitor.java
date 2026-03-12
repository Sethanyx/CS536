// Generated from Program.g4 by ANTLR 4.13.2
package madlang;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ProgramParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ProgramVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ProgramParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(ProgramParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#baseType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseType(ProgramParser.BaseTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(ProgramParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(ProgramParser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#globalVarDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalVarDecl(ProgramParser.GlobalVarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#funDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunDecl(ProgramParser.FunDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParams(ProgramParser.ParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(ProgramParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(ProgramParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(ProgramParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#varDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDef(ProgramParser.VarDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#funDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunDef(ProgramParser.FunDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#assignStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStmt(ProgramParser.AssignStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(ProgramParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#whileStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(ProgramParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#returnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(ProgramParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#exprStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStmt(ProgramParser.ExprStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Relation}
	 * labeled alternative in {@link ProgramParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(ProgramParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link ProgramParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(ProgramParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link ProgramParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(ProgramParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code And}
	 * labeled alternative in {@link ProgramParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(ProgramParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimaryExpr}
	 * labeled alternative in {@link ProgramParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr(ProgramParser.PrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Equality}
	 * labeled alternative in {@link ProgramParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality(ProgramParser.EqualityContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unary}
	 * labeled alternative in {@link ProgramParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary(ProgramParser.UnaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultDiv}
	 * labeled alternative in {@link ProgramParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultDiv(ProgramParser.MultDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntLit}
	 * labeled alternative in {@link ProgramParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLit(ProgramParser.IntLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueLit}
	 * labeled alternative in {@link ProgramParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueLit(ProgramParser.TrueLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseLit}
	 * labeled alternative in {@link ProgramParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseLit(ProgramParser.FalseLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunCall}
	 * labeled alternative in {@link ProgramParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunCall(ProgramParser.FunCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Ident}
	 * labeled alternative in {@link ProgramParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(ProgramParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link ProgramParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(ProgramParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by {@link ProgramParser#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs(ProgramParser.ArgsContext ctx);
}