/*

 */

package Triangle.AbstractSyntaxTrees;
/**
 *
 * @author Jos? Ram?rez
 */
import Triangle.SyntacticAnalyzer.SourcePosition;

public class MultipleRepeatWhileCommand extends Command {
    
 public MultipleRepeatWhileCommand (Expression eAST, Command c1AST, Command c2AST,
                    SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    C1 = c1AST;
    C2 = c2AST;
  } 
  
 
  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitMultipleRepeatWhileCommand(this, o);
  }
  public Expression E;
  public Command C1, C2;
}
