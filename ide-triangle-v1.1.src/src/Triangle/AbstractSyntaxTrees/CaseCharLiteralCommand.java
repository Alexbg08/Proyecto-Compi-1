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
public class CaseCharLiteralCommand extends Command {

    public CaseCharLiteralCommand(CharacterLiteral chAST, SourcePosition thePosition) {
         super (thePosition);
         CH = chAST;  
    }
     @Override
    public Object visit(Visitor v, Object o) {
        return v.visitCaseCharLiteralCommand(this, o);
       

    }
    public CharacterLiteral CH;
}
