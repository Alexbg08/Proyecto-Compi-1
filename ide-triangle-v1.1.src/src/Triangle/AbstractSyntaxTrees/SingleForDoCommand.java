package Triangle.AbstractSyntaxTrees;
/**
 *
 * @author José Ramírez
 */
import Triangle.SyntacticAnalyzer.SourcePosition;

public class SingleForDoCommand extends Command {

 
   public SingleForDoCommand (Declaration d1AST, Expression e2AST, Command c1AST,
                    SourcePosition thePosition) {
    super (thePosition);
    D = d1AST;
    E2 = e2AST;
    C1 = c1AST;
    
  }

  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitSingleForDoCommand(this, o);
    
  }

  public Declaration D;
  public Expression E2;
  public Command C1;

}