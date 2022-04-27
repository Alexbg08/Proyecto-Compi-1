/*

 */

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class RepeatWhileCommand extends Command {

  public RepeatWhileCommand (Expression eAST, Command c1AST, Command c2AST,
                    SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    C1 = c1AST;
    C2 = c2AST;
  }
  
    public RepeatWhileCommand (Expression eAST, Command c1AST,
                    SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    C1 = c1AST;
  }

  public Object visit(Visitor v, Object o) {
    //return v.visitRepeatWhileCommand(this, o);
    return null;
  }

  public Expression E;
  public Command C1, C2;
}
