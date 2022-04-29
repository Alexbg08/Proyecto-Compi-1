/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 * 
 * @author Jose Ramirez
 */
public class ArrayTypeDenoterOptional extends TypeDenoter {

    public ArrayTypeDenoterOptional(IntegerLiteral ilAST, IntegerLiteral il2AST, TypeDenoter tAST, SourcePosition thePosition) {
        super (thePosition);
        IL1 = ilAST;
        IL2 = il2AST;
        T = tAST;
    }
    public Object visit(Visitor v, Object o) {
    return v.visitArrayTypeDenoterOptional(this, o);
   }
    
    public boolean equals (Object obj) {
    if (obj != null && obj instanceof ErrorTypeDenoter)
      return true;
    else if (obj != null && obj instanceof ArrayTypeDenoter)
      return this.IL1.spelling.compareTo(((ArrayTypeDenoter) obj).IL.spelling) == 0 &&
             this.T.equals(((ArrayTypeDenoter) obj).T);
    else
      return false;
  }

  public IntegerLiteral IL1,IL2;
  public TypeDenoter T;
}
    

