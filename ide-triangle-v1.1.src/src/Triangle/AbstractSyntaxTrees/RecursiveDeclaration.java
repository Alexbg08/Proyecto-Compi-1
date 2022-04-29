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
public class RecursiveDeclaration extends Declaration{
    public RecursiveDeclaration (Declaration pfAST, SourcePosition thePosition) {
    super (thePosition);
    D = pfAST;
  
  }
    public Object visit (Visitor v, Object o) {
        return v.visitRecursiveDeclaration(this, o);
  }
  public Declaration D;
}
