package Triangle.AbstractSyntaxTrees;
/**
 *
 * @author José Ramírez
 */
import Triangle.SyntacticAnalyzer.SourcePosition;

public class SingleDoUntilCommand extends Command {


  
    public SingleDoUntilCommand (Expression eAST, Command c1AST,
                    SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    C1 = c1AST;
  }

  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitSingleDoUntilCommand(this, o);
  }

  public Expression E;
  public Command C1;
}