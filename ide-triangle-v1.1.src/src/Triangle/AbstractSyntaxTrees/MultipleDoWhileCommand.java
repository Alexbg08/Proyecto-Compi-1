package Triangle.AbstractSyntaxTrees;
/**
 *
 * @author José Ramírez
 */
import Triangle.SyntacticAnalyzer.SourcePosition;

public class MultipleDoWhileCommand extends Command {

 public MultipleDoWhileCommand (Expression eAST, Command c1AST, Command c2AST,
                    SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    C1 = c1AST;
    C2 = c2AST;
  } 

  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitMultipleDoWhileCommand(this, o);
  }

  public Expression E;
  public Command C1, C2;
}