/*
 * @(#)Checker.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.ContextualAnalyzer;

import Triangle.ErrorReporter;
import Triangle.StdEnvironment;
import Triangle.AbstractSyntaxTrees.AnyTypeDenoter;
import Triangle.AbstractSyntaxTrees.ArrayExpression;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoterOptional;
import Triangle.AbstractSyntaxTrees.AssignCommand;
import Triangle.AbstractSyntaxTrees.BinaryExpression;
import Triangle.AbstractSyntaxTrees.BinaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.BoolTypeDenoter;
import Triangle.AbstractSyntaxTrees.CallCommand;
import Triangle.AbstractSyntaxTrees.CallExpression;
import Triangle.AbstractSyntaxTrees.CaseCharLiteralCommand;
import Triangle.AbstractSyntaxTrees.CaseIntLiteralCommand;
import Triangle.AbstractSyntaxTrees.CasesCommand;
import Triangle.AbstractSyntaxTrees.CharTypeDenoter;
import Triangle.AbstractSyntaxTrees.CharacterExpression;
import Triangle.AbstractSyntaxTrees.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.CondRestOfIfCommand;
import Triangle.AbstractSyntaxTrees.ConstActualParameter;
import Triangle.AbstractSyntaxTrees.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.ConstFormalParameter;
import Triangle.AbstractSyntaxTrees.Declaration;
import Triangle.AbstractSyntaxTrees.DotVname;
import Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
import Triangle.AbstractSyntaxTrees.EmptyCommand;
import Triangle.AbstractSyntaxTrees.EmptyExpression;
import Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.EndRestOfIFCommand;
import Triangle.AbstractSyntaxTrees.ErrorTypeDenoter;
import Triangle.AbstractSyntaxTrees.Expression;
import Triangle.AbstractSyntaxTrees.FieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.ForVarDeclaration;
import Triangle.AbstractSyntaxTrees.FormalParameter;
import Triangle.AbstractSyntaxTrees.FormalParameterSequence;
import Triangle.AbstractSyntaxTrees.FuncActualParameter;
import Triangle.AbstractSyntaxTrees.FuncDeclaration;
import Triangle.AbstractSyntaxTrees.FuncFormalParameter;
import Triangle.AbstractSyntaxTrees.Identifier;
import Triangle.AbstractSyntaxTrees.IfCommand;
import Triangle.AbstractSyntaxTrees.IfExpression;
import Triangle.AbstractSyntaxTrees.IntTypeDenoter;
import Triangle.AbstractSyntaxTrees.IntegerExpression;
import Triangle.AbstractSyntaxTrees.IntegerLiteral;
import Triangle.AbstractSyntaxTrees.LetCommand;
import Triangle.AbstractSyntaxTrees.LetExpression;
import Triangle.AbstractSyntaxTrees.MultipleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleArrayAggregate;
import Triangle.AbstractSyntaxTrees.MultipleCaseCommand;
import Triangle.AbstractSyntaxTrees.MultipleDoUntilCommand;
import Triangle.AbstractSyntaxTrees.MultipleDoWhileCommand;
import Triangle.AbstractSyntaxTrees.MultipleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.MultipleForDoCommand;
import Triangle.AbstractSyntaxTrees.MultipleForUntilCommand;
import Triangle.AbstractSyntaxTrees.MultipleForWhileCommand;
import Triangle.AbstractSyntaxTrees.MultipleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleRecordAggregate;
import Triangle.AbstractSyntaxTrees.MultipleRepeatUntilCommand;
import Triangle.AbstractSyntaxTrees.MultipleRepeatWhileCommand;
import Triangle.AbstractSyntaxTrees.Operator;
import Triangle.AbstractSyntaxTrees.PrivateDeclaration;
import Triangle.AbstractSyntaxTrees.ProcActualParameter;
import Triangle.AbstractSyntaxTrees.ProcDeclaration;
import Triangle.AbstractSyntaxTrees.ProcFormalParameter;
import Triangle.AbstractSyntaxTrees.ProcFuncsDeclaration;
import Triangle.AbstractSyntaxTrees.Program;
import Triangle.AbstractSyntaxTrees.RecordExpression;
import Triangle.AbstractSyntaxTrees.RecordTypeDenoter;
import Triangle.AbstractSyntaxTrees.RecursiveDeclaration;
import Triangle.AbstractSyntaxTrees.SequentialCommand;
import Triangle.AbstractSyntaxTrees.SequentialDeclaration;
import Triangle.AbstractSyntaxTrees.SimpleTypeDenoter;
import Triangle.AbstractSyntaxTrees.SimpleVname;
import Triangle.AbstractSyntaxTrees.SingleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleArrayAggregate;
import Triangle.AbstractSyntaxTrees.SingleCaseCommand;
import Triangle.AbstractSyntaxTrees.SingleDoUntilCommand;
import Triangle.AbstractSyntaxTrees.SingleDoWhileCommand;
import Triangle.AbstractSyntaxTrees.SingleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.SingleForDoCommand;
import Triangle.AbstractSyntaxTrees.SingleForUntilCommand;
import Triangle.AbstractSyntaxTrees.SingleForWhileCommand;
import Triangle.AbstractSyntaxTrees.SingleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleRecordAggregate;
import Triangle.AbstractSyntaxTrees.SingleRepeatUntilCommand;
import Triangle.AbstractSyntaxTrees.SingleRepeatWhileCommand;
import Triangle.AbstractSyntaxTrees.SubscriptVname;
import Triangle.AbstractSyntaxTrees.Terminal;
import Triangle.AbstractSyntaxTrees.TypeDeclaration;
import Triangle.AbstractSyntaxTrees.TypeDenoter;
import Triangle.AbstractSyntaxTrees.UnaryExpression;
import Triangle.AbstractSyntaxTrees.UnaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.VarActualParameter;
import Triangle.AbstractSyntaxTrees.VarDeclaration;
import Triangle.AbstractSyntaxTrees.VarDeclarationOptional;
import Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.Visitor;
import Triangle.AbstractSyntaxTrees.VnameExpression;
import Triangle.AbstractSyntaxTrees.WhileCommand;
import Triangle.SyntacticAnalyzer.SourcePosition;

public final class Checker implements Visitor {

  // Commands

  // Always returns null. Does not use the given object.

  public Object visitAssignCommand(AssignCommand ast, Object o) {
    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, null);
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    if (!ast.V.variable)
      reporter.reportError ("LHS of assignment is not a variable", "", ast.V.position);
    if (! eType.equals(vType))
      reporter.reportError ("assignment incompatibilty", "", ast.position);
    return null;
  }


  public Object visitCallCommand(CallCommand ast, Object o) {

    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null)
      reportUndeclared(ast.I);
    else if (binding instanceof ProcDeclaration) {
      ast.APS.visit(this, ((ProcDeclaration) binding).FPS);
    } else if (binding instanceof ProcFormalParameter) {
      ast.APS.visit(this, ((ProcFormalParameter) binding).FPS);
    } else
      reporter.reportError("\"%\" is not a procedure identifier",
                           ast.I.spelling, ast.I.position);
    return null;
  }

  public Object visitEmptyCommand(EmptyCommand ast, Object o) {
    return null;
  }

  public Object visitIfCommand(IfCommand ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    if (! eType.equals(StdEnvironment.booleanType))
      reporter.reportError("Boolean expression expected here", "", ast.E.position);
    ast.C1.visit(this, null);
    ast.C2.visit(this, null);
    return null;
  }

  public Object visitLetCommand(LetCommand ast, Object o) {
    idTable.openScope();
    ast.D.visit(this, null);
    ast.C.visit(this, null);
    idTable.closeScope();
    return null;
  }

  public Object visitSequentialCommand(SequentialCommand ast, Object o) {
    ast.C1.visit(this, null);
    ast.C2.visit(this, null);
    return null;
  }

  public Object visitWhileCommand(WhileCommand ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    if (! eType.equals(StdEnvironment.booleanType))
      reporter.reportError("Boolean expression expected here", "", ast.E.position);
    ast.C.visit(this, null);
    return null;
  }
   // extend del lenguje tri??ngulo 
   // Todos lo m??todos del extend del tri??ngulo los agrego Jos?? Ram??rez  
   @Override
    public Object visitSingleRepeatWhileCommand(SingleRepeatWhileCommand ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E.position);
        ast.C1.visit(this, null);
        return null;
    }

    @Override
    public Object visitMultipleRepeatWhileCommand(MultipleRepeatWhileCommand ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E.position);
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        return null;
    }

    @Override
    public Object visitSingleRepeatUntilCommand(SingleRepeatUntilCommand ast, Object o) {
       TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E.position);
        ast.C1.visit(this, null);
        return null;
    }

    @Override
    public Object visitMultipleRepeatUntilCommand(MultipleRepeatUntilCommand ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E.position);
        
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        return null;
    }

    @Override
    public Object visitSingleDoWhileCommand(SingleDoWhileCommand ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E.position);
        ast.C1.visit(this, null);
        return null;
    }

    @Override
    public Object visitMultipleDoWhileCommand(MultipleDoWhileCommand ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E.position);
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        return null;
    }

    @Override
    public Object visitSingleDoUntilCommand(SingleDoUntilCommand ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E.position);
        ast.C1.visit(this, null);
        return null;
    }

    @Override
    public Object visitMultipleDoUntilCommand(MultipleDoUntilCommand ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E.position);
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        return null;
    }
    @Override
    public Object visitForVarDeclaration(ForVarDeclaration ast, Object o) {
        return null;
    }
    @Override
    
    public Object visitSingleForDoCommand(SingleForDoCommand ast, Object o) {
        ForVarDeclaration forVar = (ForVarDeclaration) ast.D;
        TypeDenoter eType = (TypeDenoter) forVar.E.visit(this, null);
        
        if (! eType.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", forVar.E.position);
        
        TypeDenoter e2Type = (TypeDenoter) ast.E2.visit(this, null);
         if (! e2Type.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", ast.E2.position);
        
         idTable.openScope();
         idTable.enter(forVar.I.spelling, forVar);
         ast.C1.visit(this, null);
         idTable.closeScope();
         return null; 
    }

    @Override
    public Object visitMultipleForDoCommand(MultipleForDoCommand ast, Object o) {
        ForVarDeclaration forVar = (ForVarDeclaration) ast.D;
        TypeDenoter eType = (TypeDenoter) forVar.E.visit(this, null);
        
         if (! eType.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", forVar.E.position);
         
        TypeDenoter e2Type = (TypeDenoter) ast.E2.visit(this, null);
         if (! e2Type.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", ast.E2.position);
         
         idTable.openScope();
         idTable.enter(forVar.I.spelling, forVar);
         ast.C1.visit(this, null);
         ast.C2.visit(this, null);
         idTable.closeScope();
         
         return null; 
    }
    @Override
    public Object visitSingleForWhileCommand(SingleForWhileCommand ast, Object o) {
        ForVarDeclaration forVar = (ForVarDeclaration) ast.D;
        TypeDenoter eType = (TypeDenoter) forVar.E.visit(this, null);
        
        if (! eType.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", forVar.E.position);
        
        TypeDenoter e2Type = (TypeDenoter) ast.E1.visit(this, null);
         if (! e2Type.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", ast.E1.position);
         
        
         idTable.openScope();
         idTable.enter(forVar.I.spelling, forVar);
         
         TypeDenoter e3Type = (TypeDenoter) ast.E2.visit(this, null);
         if (! e3Type.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E2.position);
         
         ast.C1.visit(this, null);
         idTable.closeScope();
         return null; 
    }

    @Override
    public Object visitMultipleForWhileCommand(MultipleForWhileCommand ast, Object o) {
        ForVarDeclaration forVar = (ForVarDeclaration) ast.D;
        TypeDenoter eType = (TypeDenoter) forVar.E.visit(this, null);
        
        if (! eType.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", forVar.E.position);
        
        TypeDenoter e2Type = (TypeDenoter) ast.E1.visit(this, null);
         if (! e2Type.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", ast.E1.position);
         
        
         idTable.openScope();
         idTable.enter(forVar.I.spelling, forVar);
         
         TypeDenoter e3Type = (TypeDenoter) ast.E2.visit(this, null);
         if (! e3Type.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E2.position);
         
         ast.C1.visit(this, null);
         ast.C2.visit(this, null);
         idTable.closeScope();
         return null; 
    }

    @Override
    public Object visitSingleForUntilCommand(SingleForUntilCommand ast, Object o) {
        ForVarDeclaration forVar = (ForVarDeclaration) ast.D;
        TypeDenoter eType = (TypeDenoter) forVar.E.visit(this, null);
        
        if (! eType.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", forVar.E.position);
        
        TypeDenoter e2Type = (TypeDenoter) ast.E1.visit(this, null);
         if (! e2Type.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", ast.E1.position);
         
        
         idTable.openScope();
         idTable.enter(forVar.I.spelling, forVar);
         
         TypeDenoter e3Type = (TypeDenoter) ast.E2.visit(this, null);
         if (! e3Type.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E2.position);
         
         ast.C1.visit(this, null);
         idTable.closeScope();
         return null; 
    }

    @Override
    public Object visitMultipleForUntilCommand(MultipleForUntilCommand ast, Object o) {
        ForVarDeclaration forVar = (ForVarDeclaration) ast.D;
        TypeDenoter eType = (TypeDenoter) forVar.E.visit(this, null);
        
        if (! eType.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", forVar.E.position);
        
        TypeDenoter e2Type = (TypeDenoter) ast.E1.visit(this, null);
         if (! e2Type.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", ast.E1.position);
         
        
         idTable.openScope();
         idTable.enter(forVar.I.spelling, forVar);
         
         TypeDenoter e3Type = (TypeDenoter) ast.E2.visit(this, null);
         if (! e3Type.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E2.position);
         
         ast.C1.visit(this, null);
         ast.C2.visit(this, null);
         idTable.closeScope();
         return null; 
    }
    
    @Override
    public Object visitCondRestOfIfCommand(CondRestOfIfCommand ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
            reporter.reportError("Boolean expression expected here", "", ast.E.position);
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
   
        return null;
      
    }

    @Override
    public Object visitEndRestOfIFCommand(EndRestOfIFCommand ast, Object o) {
        ast.C1.visit(this, null);
        return null;
    }

    @Override
    public Object visitCasesCommand(CasesCommand ast, Object o) {
        idTable.openScope();
        ast.C1.visit(this, null);
        idTable.closeScope();
        return null;
    }

    @Override
    public Object visitSingleCaseCommand(SingleCaseCommand ast, Object o) {
        idTable.openScope();
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        idTable.closeScope();
        return null;
    }

    @Override
    public Object visitMultipleCaseCommand(MultipleCaseCommand ast, Object o) {
        idTable.openScope();
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        ast.C3.visit(this, null);
        idTable.closeScope();
        return null;
    }

    @Override
    public Object visitCaseIntLiteralCommand(CaseIntLiteralCommand ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.IN.visit(this, null);
        if (! eType.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", ast.IN.position);
        
        return null;
    }

    @Override
    public Object visitCaseCharLiteralCommand(CaseCharLiteralCommand ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.CH.visit(this, null);
        if (! eType.equals(StdEnvironment.charType))
            reporter.reportError("Char expression expected here", "", ast.CH.position);
        
        return null;
    }
    
 

    @Override/** 
   * Checker visitor for class "Triangle.AbstractSyntaxTrees.PrivateDeclaration".
   * @author Jhonny Picado Vega, Jocxan Sand?, Fabio Calder?n
   */
    
    public Object visitPrivateDeclaration(PrivateDeclaration ast, Object o) {
          //Save the pointer before Private
        idTable.setLatests();
        ast.D1.visit(this, null);
        idTable.setLatests();           //Save the last D1 pointer
        ast.D2.visit(this, null);
        idTable.closePrivate();
         return null;
    }
           @Override
           
     /** 
   * Checker visitor for class "Triangle.AbstractSyntaxTrees.RecursiveDeclaration".
   * @author Jhonny Picado Vega
   */       
    public Object visitRecursiveDeclaration(RecursiveDeclaration ast, Object o) {  
    ast.D.visit(this, null);
    return null;
    }
	

    @Override
    /**
   * Checker visitor for class "Triangle.AbstractSyntaxTrees.ProcFuncsDeclaration".
   * @author Esteban Fonseca Montoya
   */
    public Object visitProcFuncsDeclaration(ProcFuncsDeclaration ast, Object o) {
        // Add all the identifiers in the Proc/Func declarations in the subtrees of the AST.
    addProcFuncsDeclarationIds(ast, null);

    // Perform contextual analysis in the subtrees of the AST.
    checkRecursiveProcFuncs(ast, null);

    return null;
       
    }
    /**
   * Traverse the ProcFuncsDeclaration AST to enter the identifiers of its Procedures/Functions in the idTable.
   * This allows the Procedures and Functions to be mutually recursive
   * (e.g. PF1 can call PF2 and vice-versa, regardless of declaration order).
   * @author Esteban Fonseca Montoya, Jhonny Picado
   */
  private Object addProcFuncsDeclarationIds(Declaration ast, Object o) {

    if (ast instanceof ProcFuncsDeclaration) {
      addProcFuncsDeclarationIds(((ProcFuncsDeclaration) ast).D1, null);
      addProcFuncsDeclarationIds(((ProcFuncsDeclaration) ast).D2, null);
    }
    if (ast instanceof FuncDeclaration) {
      ((FuncDeclaration) ast).T = (TypeDenoter) ((FuncDeclaration) ast).T.visit(this, null);
      idTable.enter(((FuncDeclaration) ast).I.spelling, ast);
      if (ast.duplicated)
        reporter.reportError("identifier \"%\" already declared",
                ((FuncDeclaration) ast).I.spelling, ast.position);

      //Visit the FPS to set the correct type, for each parameter
      idTable.openScope();
      ((FuncDeclaration) ast).FPS.visit(this, null);
      idTable.closeScope();
    }
    if (ast instanceof ProcDeclaration) {
      idTable.enter(((ProcDeclaration) ast).I.spelling, ast);
      if (ast.duplicated)
        reporter.reportError("identifier \"%\" already declared",
                ((ProcDeclaration) ast).I.spelling, ast.position);

      //Visit the FPS to set the correct type, for each parameter
      idTable.openScope();
      ((ProcDeclaration) ast).FPS.visit(this, null);
      idTable.closeScope();
    }

    return null;
  }

  /**
   * Perform contextual analysis of the ProcFuncsDeclaration AST and its subtrees.
   * Traversal is done recursively.
   * @author Esteban Fonseca Montoya, Jhonny Picado
   */
  private Object checkRecursiveProcFuncs(Declaration ast, Object o) {

    if (ast instanceof ProcFuncsDeclaration) {
      checkRecursiveProcFuncs(((ProcFuncsDeclaration) ast).D1, null);
      checkRecursiveProcFuncs(((ProcFuncsDeclaration) ast).D2, null);
    }
    if (ast instanceof FuncDeclaration) {
      idTable.openScope();
      ((FuncDeclaration) ast).FPS.visit(this, null);
      TypeDenoter eType = (TypeDenoter) ((FuncDeclaration) ast).E.visit(this, null);
      idTable.closeScope();
      if (! ((FuncDeclaration) ast).T.equals(eType))
        reporter.reportError ("body of function \"%\" has wrong type",
                ((FuncDeclaration) ast).I.spelling, ((FuncDeclaration) ast).E.position);
    }
    if (ast instanceof ProcDeclaration) {
      idTable.openScope();
      ((ProcDeclaration) ast).FPS.visit(this, null);
      ((ProcDeclaration) ast).C.visit(this, null);
      idTable.closeScope();
    }

    return null;
  }
 
        @Override
    public Object visitVarDeclarationOptional(VarDeclarationOptional ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
        idTable.enter(ast.I.spelling, ast);
        if (ast.duplicated)
            reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    return null;
    }

    @Override
    public Object visitArrayTypeDenoterOptional(ArrayTypeDenoterOptional ast, Object o) {
        ast.T = (TypeDenoter) ast.T.visit(this, null);
        TypeDenoter il1Type = (TypeDenoter) ast.IL1.visit(this, null);
        TypeDenoter il2pe = (TypeDenoter) ast.IL2.visit(this, null);
        
        if (! il1Type.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", ast.IL1.position);
        if (! il2pe.equals(StdEnvironment.integerType))
            reporter.reportError("Integer expression expected here", "", ast.IL2.position);
        
        if (Integer.parseInt(ast.IL2.spelling) > Integer.parseInt(ast.IL1.spelling))
             reporter.reportError("Lower bound is greater than upper bound", "", ast.IL2.position);
        
        return null;
    }

  // Expressions

  // Returns the TypeDenoter denoting the type of the expression. Does
  // not use the given object.

  public Object visitArrayExpression(ArrayExpression ast, Object o) {
    TypeDenoter elemType = (TypeDenoter) ast.AA.visit(this, null);
    IntegerLiteral il = new IntegerLiteral(new Integer(ast.AA.elemCount).toString(),
                                           ast.position);
    ast.type = new ArrayTypeDenoter(il, elemType, ast.position);
    return ast.type;
  }

  public Object visitBinaryExpression(BinaryExpression ast, Object o) {

    TypeDenoter e1Type = (TypeDenoter) ast.E1.visit(this, null);
    TypeDenoter e2Type = (TypeDenoter) ast.E2.visit(this, null);
    Declaration binding = (Declaration) ast.O.visit(this, null);

    if (binding == null)
      reportUndeclared(ast.O);
    else {
      if (! (binding instanceof BinaryOperatorDeclaration))
        reporter.reportError ("\"%\" is not a binary operator",
                              ast.O.spelling, ast.O.position);
      BinaryOperatorDeclaration bbinding = (BinaryOperatorDeclaration) binding;
      if (bbinding.ARG1 == StdEnvironment.anyType) {
        // this operator must be "=" or "\="
        if (! e1Type.equals(e2Type))
          reporter.reportError ("incompatible argument types for \"%\"",
                                ast.O.spelling, ast.position);
      } else if (! e1Type.equals(bbinding.ARG1))
          reporter.reportError ("wrong argument type for \"%\"",
                                ast.O.spelling, ast.E1.position);
      else if (! e2Type.equals(bbinding.ARG2))
          reporter.reportError ("wrong argument type for \"%\"",
                                ast.O.spelling, ast.E2.position);
      ast.type = bbinding.RES;
    }
    return ast.type;
  }

  public Object visitCallExpression(CallExpression ast, Object o) {
    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null) {
      reportUndeclared(ast.I);
      ast.type = StdEnvironment.errorType;
    } else if (binding instanceof FuncDeclaration) {
      ast.APS.visit(this, ((FuncDeclaration) binding).FPS);
      ast.type = ((FuncDeclaration) binding).T;
    } else if (binding instanceof FuncFormalParameter) {
      ast.APS.visit(this, ((FuncFormalParameter) binding).FPS);
      ast.type = ((FuncFormalParameter) binding).T;
    } else
      reporter.reportError("\"%\" is not a function identifier",
                           ast.I.spelling, ast.I.position);
    return ast.type;
  }

  public Object visitCharacterExpression(CharacterExpression ast, Object o) {
    ast.type = StdEnvironment.charType;
    return ast.type;
  }

  public Object visitEmptyExpression(EmptyExpression ast, Object o) {
    ast.type = null;
    return ast.type;
  }

  public Object visitIfExpression(IfExpression ast, Object o) {
    TypeDenoter e1Type = (TypeDenoter) ast.E1.visit(this, null);
    if (! e1Type.equals(StdEnvironment.booleanType))
      reporter.reportError ("Boolean expression expected here", "",
                            ast.E1.position);
    TypeDenoter e2Type = (TypeDenoter) ast.E2.visit(this, null);
    TypeDenoter e3Type = (TypeDenoter) ast.E3.visit(this, null);
    if (! e2Type.equals(e3Type))
      reporter.reportError ("incompatible limbs in if-expression", "", ast.position);
    ast.type = e2Type;
    return ast.type;
  }

  public Object visitIntegerExpression(IntegerExpression ast, Object o) {
    ast.type = StdEnvironment.integerType;
    return ast.type;
  }

  public Object visitLetExpression(LetExpression ast, Object o) {
    idTable.openScope();
    ast.D.visit(this, null);
    ast.type = (TypeDenoter) ast.E.visit(this, null);
    idTable.closeScope();
    return ast.type;
  }

  public Object visitRecordExpression(RecordExpression ast, Object o) {
    FieldTypeDenoter rType = (FieldTypeDenoter) ast.RA.visit(this, null);
    ast.type = new RecordTypeDenoter(rType, ast.position);
    return ast.type;
  }

  public Object visitUnaryExpression(UnaryExpression ast, Object o) {

    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    Declaration binding = (Declaration) ast.O.visit(this, null);
    if (binding == null) {
      reportUndeclared(ast.O);
      ast.type = StdEnvironment.errorType;
    } else if (! (binding instanceof UnaryOperatorDeclaration))
        reporter.reportError ("\"%\" is not a unary operator",
                              ast.O.spelling, ast.O.position);
    else {
      UnaryOperatorDeclaration ubinding = (UnaryOperatorDeclaration) binding;
      if (! eType.equals(ubinding.ARG))
        reporter.reportError ("wrong argument type for \"%\"",
                              ast.O.spelling, ast.O.position);
      ast.type = ubinding.RES;
    }
    return ast.type;
  }

  public Object visitVnameExpression(VnameExpression ast, Object o) {
    ast.type = (TypeDenoter) ast.V.visit(this, null);
    return ast.type;
  }

  // Declarations

  // Always returns null. Does not use the given object.
  public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object o) {
    return null;
  }

  public Object visitConstDeclaration(ConstDeclaration ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    idTable.enter(ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitFuncDeclaration(FuncDeclaration ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter (ast.I.spelling, ast); // permits recursion
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    idTable.openScope();
    ast.FPS.visit(this, null);
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    idTable.closeScope();
    if (! ast.T.equals(eType))
      reporter.reportError ("body of function \"%\" has wrong type",
                            ast.I.spelling, ast.E.position);
    return null;
  }

  public Object visitProcDeclaration(ProcDeclaration ast, Object o) {
    idTable.enter (ast.I.spelling, ast); // permits recursion
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    idTable.openScope();
    ast.FPS.visit(this, null);
    ast.C.visit(this, null);
    idTable.closeScope();
    return null;
  }

  public Object visitSequentialDeclaration(SequentialDeclaration ast, Object o) {
    ast.D1.visit(this, null);
    ast.D2.visit(this, null);
    return null;
  }

  public Object visitTypeDeclaration(TypeDeclaration ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object o) {
    return null;
  }

  public Object visitVarDeclaration(VarDeclaration ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);

    return null;
  }

  // Array Aggregates

  // Returns the TypeDenoter for the Array Aggregate. Does not use the
  // given object.

  public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    TypeDenoter elemType = (TypeDenoter) ast.AA.visit(this, null);
    ast.elemCount = ast.AA.elemCount + 1;
    if (! eType.equals(elemType))
      reporter.reportError ("incompatible array-aggregate element", "", ast.E.position);
    return elemType;
  }

  public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object o) {
    TypeDenoter elemType = (TypeDenoter) ast.E.visit(this, null);
    ast.elemCount = 1;
    return elemType;
  }

  // Record Aggregates

  // Returns the TypeDenoter for the Record Aggregate. Does not use the
  // given object.

  public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    FieldTypeDenoter rType = (FieldTypeDenoter) ast.RA.visit(this, null);
    TypeDenoter fType = checkFieldIdentifier(rType, ast.I);
    if (fType != StdEnvironment.errorType)
      reporter.reportError ("duplicate field \"%\" in record",
                            ast.I.spelling, ast.I.position);
    ast.type = new MultipleFieldTypeDenoter(ast.I, eType, rType, ast.position);
    return ast.type;
  }

  public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    ast.type = new SingleFieldTypeDenoter(ast.I, eType, ast.position);
    return ast.type;
  }

  // Formal Parameters

  // Always returns null. Does not use the given object.

  public Object visitConstFormalParameter(ConstFormalParameter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter(ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitFuncFormalParameter(FuncFormalParameter ast, Object o) {
    idTable.openScope();
    ast.FPS.visit(this, null);
    idTable.closeScope();
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitProcFormalParameter(ProcFormalParameter ast, Object o) {
    idTable.openScope();
    ast.FPS.visit(this, null);
    idTable.closeScope();
    idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitVarFormalParameter(VarFormalParameter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object o) {
    return null;
  }

  public Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object o) {
    ast.FP.visit(this, null);
    ast.FPS.visit(this, null);
    return null;
  }

  public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object o) {
    ast.FP.visit(this, null);
    return null;
  }

  // Actual Parameters

  // Always returns null. Uses the given FormalParameter.

  public Object visitConstActualParameter(ConstActualParameter ast, Object o) {
    FormalParameter fp = (FormalParameter) o;
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);

    if (! (fp instanceof ConstFormalParameter))
      reporter.reportError ("const actual parameter not expected here", "",
                            ast.position);
    else if (! eType.equals(((ConstFormalParameter) fp).T))
      reporter.reportError ("wrong type for const actual parameter", "",
                            ast.E.position);
    return null;
  }

  public Object visitFuncActualParameter(FuncActualParameter ast, Object o) {
    FormalParameter fp = (FormalParameter) o;

    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null)
      reportUndeclared (ast.I);
    else if (! (binding instanceof FuncDeclaration ||
                binding instanceof FuncFormalParameter))
      reporter.reportError ("\"%\" is not a function identifier",
                            ast.I.spelling, ast.I.position);
    else if (! (fp instanceof FuncFormalParameter))
      reporter.reportError ("func actual parameter not expected here", "",
                            ast.position);
    else {
      FormalParameterSequence FPS = null;
      TypeDenoter T = null;
      if (binding instanceof FuncDeclaration) {
        FPS = ((FuncDeclaration) binding).FPS;
        T = ((FuncDeclaration) binding).T;
      } else {
        FPS = ((FuncFormalParameter) binding).FPS;
        T = ((FuncFormalParameter) binding).T;
      }
      if (! FPS.equals(((FuncFormalParameter) fp).FPS))
        reporter.reportError ("wrong signature for function \"%\"",
                              ast.I.spelling, ast.I.position);
      else if (! T.equals(((FuncFormalParameter) fp).T))
        reporter.reportError ("wrong type for function \"%\"",
                              ast.I.spelling, ast.I.position);
    }
    return null;
  }

  public Object visitProcActualParameter(ProcActualParameter ast, Object o) {
    FormalParameter fp = (FormalParameter) o;

    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null)
      reportUndeclared (ast.I);
    else if (! (binding instanceof ProcDeclaration ||
                binding instanceof ProcFormalParameter))
      reporter.reportError ("\"%\" is not a procedure identifier",
                            ast.I.spelling, ast.I.position);
    else if (! (fp instanceof ProcFormalParameter))
      reporter.reportError ("proc actual parameter not expected here", "",
                            ast.position);
    else {
      FormalParameterSequence FPS = null;
      if (binding instanceof ProcDeclaration)
        FPS = ((ProcDeclaration) binding).FPS;
      else
        FPS = ((ProcFormalParameter) binding).FPS;
      if (! FPS.equals(((ProcFormalParameter) fp).FPS))
        reporter.reportError ("wrong signature for procedure \"%\"",
                              ast.I.spelling, ast.I.position);
    }
    return null;
  }

  public Object visitVarActualParameter(VarActualParameter ast, Object o) {
    FormalParameter fp = (FormalParameter) o;

    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, null);
    if (! ast.V.variable)
      reporter.reportError ("actual parameter is not a variable", "",
                            ast.V.position);
    else if (! (fp instanceof VarFormalParameter))
      reporter.reportError ("var actual parameter not expected here", "",
                            ast.V.position);
    else if (! vType.equals(((VarFormalParameter) fp).T))
      reporter.reportError ("wrong type for var actual parameter", "",
                            ast.V.position);
    return null;
  }

  public Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object o) {
    FormalParameterSequence fps = (FormalParameterSequence) o;
    if (! (fps instanceof EmptyFormalParameterSequence))
      reporter.reportError ("too few actual parameters", "", ast.position);
    return null;
  }

  public Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object o) {
    FormalParameterSequence fps = (FormalParameterSequence) o;
    if (! (fps instanceof MultipleFormalParameterSequence))
      reporter.reportError ("too many actual parameters", "", ast.position);
    else {
      ast.AP.visit(this, ((MultipleFormalParameterSequence) fps).FP);
      ast.APS.visit(this, ((MultipleFormalParameterSequence) fps).FPS);
    }
    return null;
  }

  public Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object o) {
    FormalParameterSequence fps = (FormalParameterSequence) o;
    if (! (fps instanceof SingleFormalParameterSequence))
      reporter.reportError ("incorrect number of actual parameters", "", ast.position);
    else {
      ast.AP.visit(this, ((SingleFormalParameterSequence) fps).FP);
    }
    return null;
  }

  // Type Denoters

  // Returns the expanded version of the TypeDenoter. Does not
  // use the given object.

  public Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object o) {
    return StdEnvironment.anyType;
  }

  public Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    if ((Integer.valueOf(ast.IL.spelling).intValue()) == 0)
      reporter.reportError ("arrays must not be empty", "", ast.IL.position);
    return ast;
  }

  public Object visitBoolTypeDenoter(BoolTypeDenoter ast, Object o) {
    return StdEnvironment.booleanType;
  }

  public Object visitCharTypeDenoter(CharTypeDenoter ast, Object o) {
    return StdEnvironment.charType;
  }

  public Object visitErrorTypeDenoter(ErrorTypeDenoter ast, Object o) {
    return StdEnvironment.errorType;
  }

  public Object visitSimpleTypeDenoter(SimpleTypeDenoter ast, Object o) {
    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null) {
      reportUndeclared (ast.I);
      return StdEnvironment.errorType;
    } else if (! (binding instanceof TypeDeclaration)) {
      reporter.reportError ("\"%\" is not a type identifier",
                            ast.I.spelling, ast.I.position);
      return StdEnvironment.errorType;
    }
    return ((TypeDeclaration) binding).T;
  }

  public Object visitIntTypeDenoter(IntTypeDenoter ast, Object o) {
    return StdEnvironment.integerType;
  }

  public Object visitRecordTypeDenoter(RecordTypeDenoter ast, Object o) {
    ast.FT = (FieldTypeDenoter) ast.FT.visit(this, null);
    return ast;
  }

  public Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    ast.FT.visit(this, null);
    return ast;
  }

  public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    return ast;
  }

  // Literals, Identifiers and Operators
  public Object visitCharacterLiteral(CharacterLiteral CL, Object o) {
    return StdEnvironment.charType;
  }

  public Object visitIdentifier(Identifier I, Object o) {
    Declaration binding = idTable.retrieve(I.spelling);
    if (binding != null)
      I.decl = binding;
    return binding;
  }

  public Object visitIntegerLiteral(IntegerLiteral IL, Object o) {
    return StdEnvironment.integerType;
  }

  public Object visitOperator(Operator O, Object o) {
    Declaration binding = idTable.retrieve(O.spelling);
    if (binding != null)
      O.decl = binding;
    return binding;
  }

  // Value-or-variable names

  // Determines the address of a named object (constant or variable).
  // This consists of a base object, to which 0 or more field-selection
  // or array-indexing operations may be applied (if it is a record or
  // array).  As much as possible of the address computation is done at
  // compile-time. Code is generated only when necessary to evaluate
  // index expressions at run-time.
  // currentLevel is the routine level where the v-name occurs.
  // frameSize is the anticipated size of the local stack frame when
  // the object is addressed at run-time.
  // It returns the description of the base object.
  // offset is set to the total of any field offsets (plus any offsets
  // due to index expressions that happen to be literals).
  // indexed is set to true iff there are any index expressions (other
  // than literals). In that case code is generated to compute the
  // offset due to these indexing operations at run-time.

  // Returns the TypeDenoter of the Vname. Does not use the
  // given object.

  public Object visitDotVname(DotVname ast, Object o) {
    ast.type = null;
    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, null);
    ast.variable = ast.V.variable;
    if (! (vType instanceof RecordTypeDenoter))
      reporter.reportError ("record expected here", "", ast.V.position);
    else {
      ast.type = checkFieldIdentifier(((RecordTypeDenoter) vType).FT, ast.I);
      if (ast.type == StdEnvironment.errorType)
        reporter.reportError ("no field \"%\" in this record type",
                              ast.I.spelling, ast.I.position);
    }
    return ast.type;
  }

  public Object visitSimpleVname(SimpleVname ast, Object o) {
    ast.variable = false;
    ast.type = StdEnvironment.errorType;
    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null)
      reportUndeclared(ast.I);
    else
      if (binding instanceof ConstDeclaration) {
        ast.type = ((ConstDeclaration) binding).E.type;
        ast.variable = false;
      } else if (binding instanceof VarDeclaration) {
        ast.type = ((VarDeclaration) binding).T;
        ast.variable = true;
       } else if (binding instanceof VarDeclarationOptional) {
        ast.type = ((VarDeclarationOptional) binding).E.type;
        ast.variable = true;
      } else if (binding instanceof ConstFormalParameter) {
        ast.type = ((ConstFormalParameter) binding).T;
        ast.variable = false;
      } else if (binding instanceof VarFormalParameter) {
        ast.type = ((VarFormalParameter) binding).T;
        ast.variable = true;
        } else if (binding instanceof ForVarDeclaration) {
        ast.type = ((ForVarDeclaration) binding).E.type;
        ast.variable = false;
      } else
        reporter.reportError ("\"%\" is not a const or var identifier",
                              ast.I.spelling, ast.I.position);
    return ast.type;
  }

  public Object visitSubscriptVname(SubscriptVname ast, Object o) {
    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, null);
    ast.variable = ast.V.variable;
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    if (vType != StdEnvironment.errorType) {
      if (! (vType instanceof ArrayTypeDenoter))
        reporter.reportError ("array expected here", "", ast.V.position);
      else {
        if (! eType.equals(StdEnvironment.integerType))
          reporter.reportError ("Integer expression expected here", "",
				ast.E.position);
        ast.type = ((ArrayTypeDenoter) vType).T;
      }
    }
    return ast.type;
  }

  // Programs

  public Object visitProgram(Program ast, Object o) {
    ast.C.visit(this, null);
    return null;
  }

  // Checks whether the source program, represented by its AST, satisfies the
  // language's scope rules and type rules.
  // Also decorates the AST as follows:
  //  (a) Each applied occurrence of an identifier or operator is linked to
  //      the corresponding declaration of that identifier or operator.
  //  (b) Each expression and value-or-variable-name is decorated by its type.
  //  (c) Each type identifier is replaced by the type it denotes.
  // Types are represented by small ASTs.

  public void check(Program ast) {
    ast.visit(this, null);
  }

  /////////////////////////////////////////////////////////////////////////////

  public Checker (ErrorReporter reporter) {
    this.reporter = reporter;
    this.idTable = new IdentificationTable ();
    establishStdEnvironment();
  }

  private IdentificationTable idTable;
  private static SourcePosition dummyPos = new SourcePosition();
  private ErrorReporter reporter;

  // Reports that the identifier or operator used at a leaf of the AST
  // has not been declared.

  private void reportUndeclared (Terminal leaf) {
    reporter.reportError("\"%\" is not declared", leaf.spelling, leaf.position);
  }


  private static TypeDenoter checkFieldIdentifier(FieldTypeDenoter ast, Identifier I) {
    if (ast instanceof MultipleFieldTypeDenoter) {
      MultipleFieldTypeDenoter ft = (MultipleFieldTypeDenoter) ast;
      if (ft.I.spelling.compareTo(I.spelling) == 0) {
        I.decl = ast;
        return ft.T;
      } else {
        return checkFieldIdentifier (ft.FT, I);
      }
    } else if (ast instanceof SingleFieldTypeDenoter) {
      SingleFieldTypeDenoter ft = (SingleFieldTypeDenoter) ast;
      if (ft.I.spelling.compareTo(I.spelling) == 0) {
        I.decl = ast;
        return ft.T;
      }
    }
    return StdEnvironment.errorType;
  }


  // Creates a small AST to represent the "declaration" of a standard
  // type, and enters it in the identification table.

  private TypeDeclaration declareStdType (String id, TypeDenoter typedenoter) {

    TypeDeclaration binding;

    binding = new TypeDeclaration(new Identifier(id, dummyPos), typedenoter, dummyPos);
    idTable.enter(id, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a standard
  // type, and enters it in the identification table.

  private ConstDeclaration declareStdConst (String id, TypeDenoter constType) {

    IntegerExpression constExpr;
    ConstDeclaration binding;

    // constExpr used only as a placeholder for constType
    constExpr = new IntegerExpression(null, dummyPos);
    constExpr.type = constType;
    binding = new ConstDeclaration(new Identifier(id, dummyPos), constExpr, dummyPos);
    idTable.enter(id, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a standard
  // type, and enters it in the identification table.

  private ProcDeclaration declareStdProc (String id, FormalParameterSequence fps) {

    ProcDeclaration binding;

    binding = new ProcDeclaration(new Identifier(id, dummyPos), fps,
                                  new EmptyCommand(dummyPos), dummyPos);
    idTable.enter(id, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a standard
  // type, and enters it in the identification table.

  private FuncDeclaration declareStdFunc (String id, FormalParameterSequence fps,
                                          TypeDenoter resultType) {

    FuncDeclaration binding;

    binding = new FuncDeclaration(new Identifier(id, dummyPos), fps, resultType,
                                  new EmptyExpression(dummyPos), dummyPos);
    idTable.enter(id, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a
  // unary operator, and enters it in the identification table.
  // This "declaration" summarises the operator's type info.

  private UnaryOperatorDeclaration declareStdUnaryOp
    (String op, TypeDenoter argType, TypeDenoter resultType) {

    UnaryOperatorDeclaration binding;

    binding = new UnaryOperatorDeclaration (new Operator(op, dummyPos),
                                            argType, resultType, dummyPos);
    idTable.enter(op, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a
  // binary operator, and enters it in the identification table.
  // This "declaration" summarises the operator's type info.

  private BinaryOperatorDeclaration declareStdBinaryOp
    (String op, TypeDenoter arg1Type, TypeDenoter arg2type, TypeDenoter resultType) {

    BinaryOperatorDeclaration binding;

    binding = new BinaryOperatorDeclaration (new Operator(op, dummyPos),
                                             arg1Type, arg2type, resultType, dummyPos);
    idTable.enter(op, binding);
    return binding;
  }

  // Creates small ASTs to represent the standard types.
  // Creates small ASTs to represent "declarations" of standard types,
  // constants, procedures, functions, and operators.
  // Enters these "declarations" in the identification table.

  private final static Identifier dummyI = new Identifier("", dummyPos);

  private void establishStdEnvironment () {

    // idTable.startIdentification();
    StdEnvironment.booleanType = new BoolTypeDenoter(dummyPos);
    StdEnvironment.integerType = new IntTypeDenoter(dummyPos);
    StdEnvironment.charType = new CharTypeDenoter(dummyPos);
    StdEnvironment.anyType = new AnyTypeDenoter(dummyPos);
    StdEnvironment.errorType = new ErrorTypeDenoter(dummyPos);

    StdEnvironment.booleanDecl = declareStdType("Boolean", StdEnvironment.booleanType);
    StdEnvironment.falseDecl = declareStdConst("false", StdEnvironment.booleanType);
    StdEnvironment.trueDecl = declareStdConst("true", StdEnvironment.booleanType);
    StdEnvironment.notDecl = declareStdUnaryOp("\\", StdEnvironment.booleanType, StdEnvironment.booleanType);
    StdEnvironment.andDecl = declareStdBinaryOp("/\\", StdEnvironment.booleanType, StdEnvironment.booleanType, StdEnvironment.booleanType);
    StdEnvironment.orDecl = declareStdBinaryOp("\\/", StdEnvironment.booleanType, StdEnvironment.booleanType, StdEnvironment.booleanType);

    StdEnvironment.integerDecl = declareStdType("Integer", StdEnvironment.integerType);
    StdEnvironment.maxintDecl = declareStdConst("maxint", StdEnvironment.integerType);
    StdEnvironment.addDecl = declareStdBinaryOp("+", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.integerType);
    StdEnvironment.subtractDecl = declareStdBinaryOp("-", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.integerType);
    StdEnvironment.multiplyDecl = declareStdBinaryOp("*", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.integerType);
    StdEnvironment.divideDecl = declareStdBinaryOp("/", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.integerType);
    StdEnvironment.moduloDecl = declareStdBinaryOp("//", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.integerType);
    StdEnvironment.lessDecl = declareStdBinaryOp("<", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.booleanType);
    StdEnvironment.notgreaterDecl = declareStdBinaryOp("<=", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.booleanType);
    StdEnvironment.greaterDecl = declareStdBinaryOp(">", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.booleanType);
    StdEnvironment.notlessDecl = declareStdBinaryOp(">=", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.booleanType);

    StdEnvironment.charDecl = declareStdType("Char", StdEnvironment.charType);
    StdEnvironment.chrDecl = declareStdFunc("chr", new SingleFormalParameterSequence(
                                      new ConstFormalParameter(dummyI, StdEnvironment.integerType, dummyPos), dummyPos), StdEnvironment.charType);
    StdEnvironment.ordDecl = declareStdFunc("ord", new SingleFormalParameterSequence(
                                      new ConstFormalParameter(dummyI, StdEnvironment.charType, dummyPos), dummyPos), StdEnvironment.integerType);
    StdEnvironment.eofDecl = declareStdFunc("eof", new EmptyFormalParameterSequence(dummyPos), StdEnvironment.booleanType);
    StdEnvironment.eolDecl = declareStdFunc("eol", new EmptyFormalParameterSequence(dummyPos), StdEnvironment.booleanType);
    StdEnvironment.getDecl = declareStdProc("get", new SingleFormalParameterSequence(
                                      new VarFormalParameter(dummyI, StdEnvironment.charType, dummyPos), dummyPos));
    StdEnvironment.putDecl = declareStdProc("put", new SingleFormalParameterSequence(
                                      new ConstFormalParameter(dummyI, StdEnvironment.charType, dummyPos), dummyPos));
    StdEnvironment.getintDecl = declareStdProc("getint", new SingleFormalParameterSequence(
                                            new VarFormalParameter(dummyI, StdEnvironment.integerType, dummyPos), dummyPos));
    StdEnvironment.putintDecl = declareStdProc("putint", new SingleFormalParameterSequence(
                                            new ConstFormalParameter(dummyI, StdEnvironment.integerType, dummyPos), dummyPos));
    StdEnvironment.geteolDecl = declareStdProc("geteol", new EmptyFormalParameterSequence(dummyPos));
    StdEnvironment.puteolDecl = declareStdProc("puteol", new EmptyFormalParameterSequence(dummyPos));
    StdEnvironment.equalDecl = declareStdBinaryOp("=", StdEnvironment.anyType, StdEnvironment.anyType, StdEnvironment.booleanType);
    StdEnvironment.unequalDecl = declareStdBinaryOp("\\=", StdEnvironment.anyType, StdEnvironment.anyType, StdEnvironment.booleanType);

  }

/*
  --------------------------------------------------------------
  
                        RECURSIVIDAD
  
  --------------------------------------------------------------
  */

  /* Funciones especiales para la recursividad*/

  // Ejecuta la primera pasada sobre una funci??n recursiva
  public Object visitRecursiveFuncDeclaration1(FuncDeclaration ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter (ast.I.spelling, ast); // permite la recursividad
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared", ast.I.spelling, ast.position);
    idTable.openScope();
    // Manda el string para evitar el mensaje de error la primera pasada
    ast.FPS.visit(this, "No error message"); 
    idTable.closeScope();
    return null;
  }
  
  // Ejecuta la segunda pasada sobre una funci??n recursiva
  public Object visitRecursiveFuncDeclaration2(FuncDeclaration ast, Object o) {
    idTable.openScope();
    ast.FPS.visit(this, null);
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null); 
    idTable.closeScope();
    if (! ast.T.equals(eType))
    reporter.reportError ("body of function \"%\" has wrong type", ast.I.spelling, ast.E.position);
    return null;
  }

  // Ejecuta la primera pasada sobre un procedimiento recursivo
  public Object visitRecursiveProcDeclaration1(ProcDeclaration ast, Object o) {
    idTable.enter (ast.I.spelling, ast); // permite la recursividad
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared", ast.I.spelling, ast.position);
    idTable.openScope();
    // Manda el string para evitar el mensaje de error la primera pasada
    ast.FPS.visit(this, "No error message");  
    idTable.closeScope();
    return null;
  }

  // Ejecuta la segunda pasada sobre un procedimiento recursivo
  public Object visitRecursiveProcDeclaration2(ProcDeclaration ast, Object o) {
    idTable.openScope();
    ast.FPS.visit(this, null);
    ast.C.visit(this, null);
    idTable.closeScope();
    return null;
  }


  // Determina si hay que hacer la primera pasadad sobre una funcion o un procedimiento
  public void determineProcFuncForFirstPass(Declaration ast, Object o) {
    if (ast.getClass() == FuncDeclaration.class) {
      visitRecursiveFuncDeclaration1((FuncDeclaration) ast, o);
    } else if (ast.getClass() == ProcDeclaration.class){
      visitRecursiveProcDeclaration1((ProcDeclaration) ast, o);
    }
  }

  // Determina si hay que hacer la segunda pasada sobre una funcion o un procedimiento
  public void determineProcFunForSecondPass(Declaration ast, Object o) {
    if (ast.getClass() == FuncDeclaration.class) {
      visitRecursiveFuncDeclaration2((FuncDeclaration) ast, o);
    } else if (ast.getClass() == ProcDeclaration.class){
      visitRecursiveProcDeclaration2((ProcDeclaration) ast, o);
    }
  }


    
   
}
