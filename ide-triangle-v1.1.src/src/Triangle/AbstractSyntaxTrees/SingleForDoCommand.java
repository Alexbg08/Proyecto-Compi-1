package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class SingleForDoCommand extends Command {

 
   public SingleForDoCommand (Identifier iAST,Expression e1AST, Expression e2AST, Command c1AST,
                    SourcePosition thePosition) {
    super (thePosition);
    I = iAST;
    E1 = e1AST;
    E2 = e2AST;
    C1 = c1AST;
    
  }

  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitSingleForDoCommand(this, o);
    
  }

  public Identifier I;
  public Expression E1, E2;
  public Command C1;
}