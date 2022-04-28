package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;
/**
 *
 * @author José Ramírez
 */
public class SingleRepeatUntilCommand  extends Command {
    
 public SingleRepeatUntilCommand (Expression eAST, Command c1AST,
                    SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    C1 = c1AST;
    
  } 
  
 
  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitSingleRepeatUntilCommand (this, o);
  }
  public Expression E;
  public Command C1;
}