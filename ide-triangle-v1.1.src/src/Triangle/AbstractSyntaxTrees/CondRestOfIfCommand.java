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
public class CondRestOfIfCommand extends Command {

    public CondRestOfIfCommand(Expression eAST, Command cAST, Command elsAST, SourcePosition thePosition) {
    
        super (thePosition);
        E = eAST;
        C1 = cAST;
        C2 = elsAST;
    }
      @Override
    public Object visit(Visitor v, Object o) {
        return v.visitCondRestOfIfCommand(this, o);
    }

    public Expression E;
    public Command C1,C2;
       
}
