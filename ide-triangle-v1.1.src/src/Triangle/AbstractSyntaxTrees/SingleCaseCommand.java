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
public class SingleCaseCommand extends Command {

    public SingleCaseCommand(Command cAST, Command c2AST, SourcePosition thePosition) {
        super (thePosition);
         C1 = cAST;
         C2 = c2AST;
    }
    
    @Override
    public Object visit(Visitor v, Object o) {
        return v.visitSingleCaseCommand(this, o);
       

    }
    public Command C1,C2;
    
    
}
