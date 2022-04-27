package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class SingleRepeatWhileCommand extends Command {
    
 public SingleRepeatWhileCommand (Expression eAST, Command c1AST,
                    SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    C1 = c1AST;
    
  } 
  
 
  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitSingleRepeatWhileCommand(this, o);
  }
  public Expression E;
  public Command C1;
}