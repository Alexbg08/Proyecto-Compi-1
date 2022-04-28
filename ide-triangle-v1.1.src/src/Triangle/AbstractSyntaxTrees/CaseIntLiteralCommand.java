/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author Pavilion
 */
public class CaseIntLiteralCommand extends Command {

    public CaseIntLiteralCommand(IntegerLiteral intAST, SourcePosition thePosition) {
         super (thePosition);
         IN = intAST;  
    }
    
     @Override
    public Object visit(Visitor v, Object o) {
        return v.visitCaseIntLiteralCommand(this, o);
    }
    public IntegerLiteral IN;
    
}
