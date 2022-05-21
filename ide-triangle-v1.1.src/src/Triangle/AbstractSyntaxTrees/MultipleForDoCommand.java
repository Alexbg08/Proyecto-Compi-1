package Triangle.AbstractSyntaxTrees;
/**
 *
 * @author José Ramírez
 */
import Triangle.SyntacticAnalyzer.SourcePosition;

public class MultipleForDoCommand extends Command {

 
   public MultipleForDoCommand (Declaration dAST, Expression e2AST, Command c1AST, Command c2AST,
                    SourcePosition thePosition) {
    super (thePosition);
    D = dAST;
    E2 = e2AST;
    C1 = c1AST;
    C2 = c2AST;
  } 
  

  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitMultipleForDoCommand(this, o);
    
  }

  public Declaration D;
  public Expression E2;
  public Command C1,C2;
}