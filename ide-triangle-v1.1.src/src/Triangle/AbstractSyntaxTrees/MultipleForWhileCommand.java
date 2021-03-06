package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author Jos? Ram?rez
 */
import Triangle.SyntacticAnalyzer.SourcePosition;

public class MultipleForWhileCommand extends Command{

    public MultipleForWhileCommand(Declaration dAST, Expression e1AST, Expression e2AST, Command c1AST,Command c2AST, SourcePosition thePosition) {
       super (thePosition);
    D = dAST;
    E1 = e1AST;
    E2 = e2AST;
    C1 = c1AST;
    C2 = c2AST;
 
    }
     @Override
  public Object visit(Visitor v, Object o) {
   return v.visitMultipleForWhileCommand(this, o);
  }
    
  public Declaration D;
  public Expression E1, E2;
  public Command C1, C2;
}