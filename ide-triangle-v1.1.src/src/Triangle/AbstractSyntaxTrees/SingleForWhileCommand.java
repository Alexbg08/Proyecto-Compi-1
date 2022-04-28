/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author José Ramírez
 */
import Triangle.SyntacticAnalyzer.SourcePosition;

public class SingleForWhileCommand extends Command{

    public SingleForWhileCommand(Declaration dAST, Expression e1AST, Expression e2AST, Command c1AST, SourcePosition thePosition) {
       super (thePosition);
    D = dAST;
    E1 = e1AST;
    E2 = e2AST;
    C1 = c1AST;
 
    }
     @Override
  public Object visit(Visitor v, Object o) {
   return v.visitSingleForWhileCommand(this, o);
    
  }
    
  public Declaration D;
  public Expression E1, E2;
  public Command C1;
}
