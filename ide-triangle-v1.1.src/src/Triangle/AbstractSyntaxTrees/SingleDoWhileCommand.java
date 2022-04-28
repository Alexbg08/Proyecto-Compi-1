package Triangle.AbstractSyntaxTrees;
/**
 *
 * @author José Ramírez
 */
import Triangle.SyntacticAnalyzer.SourcePosition;

public class SingleDoWhileCommand extends Command {

    public SingleDoWhileCommand (Expression eAST, Command c1AST,
                    SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    C1 = c1AST;
  }

  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitSingleDoWhileCommand(this, o);
  }

  public Expression E;
  public Command C1;
}