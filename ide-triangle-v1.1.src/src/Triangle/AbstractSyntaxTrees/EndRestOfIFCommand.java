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
public class EndRestOfIFCommand extends Command {

    public EndRestOfIFCommand(Command cAST, SourcePosition thePosition) {
         super (thePosition);
         C1 = cAST;
    }
         @Override
    public Object visit(Visitor v, Object o) {
        return v.visitEndRestOfIFCommand(this, o);

    }
    public Command C1;
    
    
}
