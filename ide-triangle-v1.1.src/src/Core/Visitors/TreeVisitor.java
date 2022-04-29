/*
 * IDE-Triangle v1.0
 * TreeVisitor.java
 */

package Core.Visitors;
import Triangle.AbstractSyntaxTrees.AST;
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
import Triangle.AbstractSyntaxTrees.DotVname;
import Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
import Triangle.AbstractSyntaxTrees.EmptyCommand;
import Triangle.AbstractSyntaxTrees.EmptyExpression;
import Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.EndRestOfIFCommand;
import Triangle.AbstractSyntaxTrees.ErrorTypeDenoter;
import Triangle.AbstractSyntaxTrees.ForVarDeclaration;
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
import Triangle.AbstractSyntaxTrees.TypeDeclaration;
import Triangle.AbstractSyntaxTrees.UnaryExpression;
import Triangle.AbstractSyntaxTrees.UnaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.VarActualParameter;
import Triangle.AbstractSyntaxTrees.VarDeclaration;
import Triangle.AbstractSyntaxTrees.VarDeclarationOptional;
import Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.Visitor;
import Triangle.AbstractSyntaxTrees.VnameExpression;
import Triangle.AbstractSyntaxTrees.WhileCommand;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Implements the Triangle Visitor interface, which is used to
 * visit an entire AST. 
 *
 * Generates DefaultMutableTreeNodes, used to draw a JTree.
 *
 * @author Luis Leopoldo Pérez <luiperpe@ns.isi.ulatina.ac.cr>
 */
public class TreeVisitor implements Visitor {
      
    /**
     * Creates a new instance of TreeVisitor.
     */
    public TreeVisitor() {
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Commands ">    
    // Commands  
    public Object visitAssignCommand(AssignCommand ast, Object o) {
        return(createBinary("Assign Command", ast.V, ast.E));
    }
    
    public Object visitCallCommand(CallCommand ast, Object o) {
        return(createBinary("Call Command", ast.I, ast.APS));
    }
    
    public Object visitEmptyCommand(EmptyCommand ast, Object o) {
        return(createNullary("Empty Command"));
    }
    
    public Object visitIfCommand(IfCommand ast, Object obj) {
        return(createTernary("If Command", ast.E, ast.C1, ast.C2));
    }
    
    public Object visitLetCommand(LetCommand ast, Object obj) {
        return(createBinary("Let Command", ast.D, ast.C));
    }
    
    public Object visitSequentialCommand(SequentialCommand ast, Object obj) {
        return(createBinary("Sequential Command", ast.C1, ast.C2));
    }
    
    public Object visitWhileCommand(WhileCommand ast, Object obj) {
        return(createBinary("While Command", ast.E, ast.C));
    }
    
  // <editor-fold defaultstate="collapsed" desc=" Extend ">
  // extend del lenguje triángulo 
  // Todos lo métodos del extend del triángulo los agrego José Ramírez 
    
    public Object visitMultipleRepeatWhileCommand(MultipleRepeatWhileCommand ast, Object obj) { 
      return(createTernary("Multiple Repeat While Command", ast.E, ast.C1, ast.C2));
  }
  
  public Object visitSingleRepeatWhileCommand(SingleRepeatWhileCommand ast, Object obj) { 
      return(createBinary("Single Repeat While Command", ast.E, ast.C1));
      
  }
  
    public Object visitMultipleRepeatUntilCommand(MultipleRepeatUntilCommand ast, Object obj) { 
      return(createTernary("Multiple Repeat until Command", ast.E, ast.C1, ast.C2));
  }
  
  public Object visitSingleRepeatUntilCommand(SingleRepeatUntilCommand ast, Object obj) { 
      return(createBinary("Single Repeat Until Command", ast.E, ast.C1));
  }
  
  public Object visitMultipleDoWhileCommand(MultipleDoWhileCommand  ast, Object obj) { 
      return(createTernary("Multiple Do While Command", ast.E, ast.C1, ast.C2));
  }
  
  public Object visitSingleDoWhileCommand(SingleDoWhileCommand  ast, Object obj) { 
      return(createBinary("Single Do While Command", ast.E, ast.C1));
  }
  
  public Object visitMultipleDoUntilCommand(MultipleDoUntilCommand  ast, Object obj) { 
      return(createTernary("Multiple Do Until Command", ast.E, ast.C1, ast.C2));
  }
  
  public Object visitSingleDoUntilCommand(SingleDoUntilCommand   ast, Object obj) { 
      return(createBinary("Single Do Until Command", ast.E, ast.C1));
  }
  
  public Object visitMultipleForDoCommand(MultipleForDoCommand   ast, Object obj) { 
      return(createQuinary("Multiple For Do Command",ast.I, ast.E1,ast.E2, ast.C1,ast.C2));
  }
  
  public Object visitSingleForDoCommand(SingleForDoCommand    ast, Object obj) { 
      return(createQuaternary("Single For Do Command",ast.I, ast.E1,ast.E2, ast.C1));
  }
  
  public Object visitMultipleForWhileCommand(MultipleForWhileCommand   ast, Object obj) { 
      return(createQuinary("Multiple For While Command",ast.D, ast.E1,ast.E2, ast.C1,ast.C2));
  }
  
  public Object visitSingleForWhileCommand(SingleForWhileCommand    ast, Object obj) { 
      return(createQuaternary("Single For While Command",ast.D, ast.E1,ast.E2, ast.C1));
  }
  
   public Object visitMultipleForUntilCommand(MultipleForUntilCommand  ast, Object obj) { 
      return(createQuinary("Multiple For Until Command",ast.D, ast.E1,ast.E2, ast.C1,ast.C2));
  }
  
  public Object visitSingleForUntilCommand(SingleForUntilCommand    ast, Object obj) { 
      return(createQuaternary("Single For Until Command",ast.D, ast.E1,ast.E2, ast.C1));
  }
  
     
   public Object visitCondRestOfIfCommand(CondRestOfIfCommand ast, Object o) { 
      return(createTernary("CondRestOf If Command", ast.E, ast.C1, ast.C2));
   }
   
   public Object visitEndRestOfIFCommand(EndRestOfIFCommand ast, Object o) { 
     return(createUnary("EndRestOf IF Command", ast.C1));
   }
   
   public Object visitCasesCommand(CasesCommand ast, Object o) { 
       return(createUnary("Cases Command", ast.C1));
   }
   public Object visitSingleCaseCommand(SingleCaseCommand ast, Object o) { 
      return(createBinary("Single Case Command", ast.C1, ast.C2));
   }
   public Object visitMultipleCaseCommand(MultipleCaseCommand ast, Object o) { 
       return(createTernary("Multiple Case Command", ast.C1, ast.C1, ast.C3));
   }
   public Object visitCaseIntLiteralCommand(CaseIntLiteralCommand ast, Object o) { 
       return(createUnary("CaseIntLiteral Command", ast.IN));
   }
   public Object visitCaseCharLiteralCommand(CaseCharLiteralCommand ast, Object o) { 
      return(createUnary("CaseCharLiteral Command", ast.CH));
   }
   
   public Object visitRecursiveDeclaration(RecursiveDeclaration ast, Object o) {   
       return(createUnary("Recursive Declaration", ast.D));
  }
   
   public Object visitPrivateDeclaration(PrivateDeclaration ast, Object o) { 
      return(createBinary("Private Declaration", ast.D1, ast.D2));   
  }
   public Object visitProcFuncsDeclaration(ProcFuncsDeclaration ast, Object o) { 
      return(createBinary("ProcFuncs Declaration", ast.D1, ast.D2));     
  }
    public Object  visitForVarDeclaration ( ForVarDeclaration ast, Object o) {   
     return(createBinary("For Var Declaration", ast.I, ast.E)); 
  }
    public Object visitVarDeclarationOptional(VarDeclarationOptional ast, Object obj) {
        return(createBinary("Var Declaration Optional", ast.I, ast.E));
  }
   public Object  visitArrayTypeDenoterOptional (ArrayTypeDenoterOptional ast, Object o) {   
      return(createTernary("Array Type Denoter Optional", ast.IL1, ast.IL2, ast.T)); 
  }
   
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Expressions ">
    // Expressions
    public Object visitArrayExpression(ArrayExpression ast, Object obj) {
        return(createUnary("Array Expression", ast.AA));
    }
    
    public Object visitBinaryExpression(BinaryExpression ast, Object obj) {
        return(createTernary("Binary Expression", ast.E1, ast.O, ast.E2));
    }
    
    public Object visitCallExpression(CallExpression ast, Object obj) {
        return(createBinary("Call Expression", ast.I, ast.APS));
    }
    
    public Object visitCharacterExpression(CharacterExpression ast, Object obj) {
        return(createUnary("Character Expression", ast.CL));
    }
    
    public Object visitEmptyExpression(EmptyExpression ast, Object obj) {
        return(createNullary("Empty Expression"));
    }
    
    public Object visitIfExpression(IfExpression ast, Object obj) {
        return(createTernary("If Expression", ast.E1, ast.E2, ast.E3));
    }
    
    public Object visitIntegerExpression(IntegerExpression ast, Object obj) {
        return(createUnary("Integer Expression", ast.IL));
    }
    
    public Object visitLetExpression(LetExpression ast, Object obj) {
        return(createBinary("Let Expression", ast.D, ast.E));
    }
    
    public Object visitRecordExpression(RecordExpression ast, Object obj) {
        return(createUnary("Record Expression", ast.RA));
    }
    
    public Object visitUnaryExpression(UnaryExpression ast, Object obj) {
        return(createBinary("Unary Expression", ast.O, ast.E));
    }
    
    public Object visitVnameExpression(VnameExpression ast, Object obj) {
        return(createUnary("Vname Expression", ast.V));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Declarations ">
    // Declarations
    public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object obj) {
        return(createQuaternary("Binary Operator Declaration", ast.O, ast.ARG1, ast.ARG2, ast.RES));
    }
    
    public Object visitConstDeclaration(ConstDeclaration ast, Object obj) {
        return(createBinary("Constant Declaration", ast.I, ast.E));
    }
    
    public Object visitFuncDeclaration(FuncDeclaration ast, Object obj) {
        return(createQuaternary("Function Declaration", ast.I, ast.FPS, ast.T, ast.E));
    }
    
    public Object visitProcDeclaration(ProcDeclaration ast, Object obj) {
        return(createTernary("Procedure Declaration", ast.I, ast.FPS, ast.C));        
    }
    
    public Object visitSequentialDeclaration(SequentialDeclaration ast, Object obj) {
        return(createBinary("Sequential Declaration", ast.D1, ast.D2));
    }
    
    public Object visitTypeDeclaration(TypeDeclaration ast, Object obj) {
        return(createBinary("Type Declaration", ast.I, ast.T));
    }
    
    public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object obj) {
        return(createTernary("Unary Operator Declaration", ast.O, ast.ARG, ast.RES));
    }
    
    public Object visitVarDeclaration(VarDeclaration ast, Object obj) {
        return(createBinary("Variable Declaration", ast.I, ast.T));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Aggregates ">
    // Array Aggregates
    public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object obj) {
        return(createBinary("Multiple Array Aggregate", ast.E, ast.AA));
    }
    
    public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object obj) {
        return(createUnary("Single Array Aggregate", ast.E));
    }
    
    // Record Aggregates
    public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object obj) {
        return(createTernary("Multiple Record Aggregate", ast.I, ast.E, ast.RA));
    }
    
    public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object obj) {
        return(createBinary("Single Record Aggregate", ast.I, ast.E));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Parameters ">
    // Formal Parameters   
    public Object visitConstFormalParameter(ConstFormalParameter ast, Object obj) {
        return(createBinary("Constant Formal Parameter", ast.I, ast.T));
    }
    
    public Object visitFuncFormalParameter(FuncFormalParameter ast, Object obj) {
        return(createTernary("Function Formal Parameter", ast.I, ast.FPS, ast.T));
    }
    
    public Object visitProcFormalParameter(ProcFormalParameter ast, Object obj) {
        return(createBinary("Procedure Formal Parameter", ast.I, ast.FPS));
    }
    
    public Object visitVarFormalParameter(VarFormalParameter ast, Object obj) {
        return(createBinary("Variable Formal Parameter", ast.I, ast.T));
    }
    
    public Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object obj) {
        return(createNullary("Empty Formal Parameter Sequence"));
    }
    
    public Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object obj) {
        return(createBinary("Multiple Formal Parameter Sequence", ast.FP, ast.FPS));
    }
    
    public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object obj) {
        return(createUnary("Single Formal Parameter Sequence", ast.FP));
    }
    
    // Actual Parameters
    public Object visitConstActualParameter(ConstActualParameter ast, Object obj) {
        return(createUnary("Constant Actual Parameter", ast.E));
    }
    
    public Object visitFuncActualParameter(FuncActualParameter ast, Object obj) {
        return(createUnary("Function Actual Parameter", ast.I));
    }
    
    public Object visitProcActualParameter(ProcActualParameter ast, Object obj) {
        return(createUnary("Procedure Actual Parameter", ast.I));
    }
    
    public Object visitVarActualParameter(VarActualParameter ast, Object obj) {
        return(createUnary("Variable Actual Parameter", ast.V));
    }
    
    public Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object obj) {
        return(createNullary("Empty Actual Parameter Sequence"));
    }
    
    public Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object obj) {
        return(createBinary("Multiple Actual Parameter Sequence", ast.AP, ast.APS));
    }
    
    public Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object obj) {
        return(createUnary("Single Actual Parameter Sequence", ast.AP));
    }
    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc=" Type Denoters ">
    // Type Denoters
    public Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object obj) {
        return(createNullary("any"));
    }
    
    public Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object obj) {
        return(createBinary("Array Type Denoter", ast.IL, ast.T));
    }
    
    public Object visitBoolTypeDenoter(BoolTypeDenoter ast, Object obj) {
        return(createNullary("bool"));
    }
    
    public Object visitCharTypeDenoter(CharTypeDenoter ast, Object obj) {
        return(createNullary("char"));
    }
    
    public Object visitErrorTypeDenoter(ErrorTypeDenoter ast, Object obj) {
        return(createNullary("error"));
    }
    
    public Object visitSimpleTypeDenoter(SimpleTypeDenoter ast, Object obj) {
        return(createUnary("Simple Type Denoter", ast.I));
    }
    
    public Object visitIntTypeDenoter(IntTypeDenoter ast, Object obj) {
        return(createNullary("int"));
    }
    
    public Object visitRecordTypeDenoter(RecordTypeDenoter ast, Object obj) {
        return(createUnary("Record Type Denoter", ast.FT));
    }
    
    public Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Object obj) {
        return(createTernary("Multiple Field Type Denoter", ast.I, ast.T, ast.FT));
    }
    
    public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Object obj) {
        return(createBinary("Single Field Type Denoter", ast.I, ast.T));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Literals, Identifiers and Operators ">
    // Literals, Identifiers and Operators
    public Object visitCharacterLiteral(CharacterLiteral ast, Object obj) {
        return(createNullary(ast.spelling));
    }
    
    public Object visitIdentifier(Identifier ast, Object obj) {
        return(createNullary(ast.spelling));
    }
    
    public Object visitIntegerLiteral(IntegerLiteral ast, Object obj) {
        return(createNullary(ast.spelling));
    }
    
    public Object visitOperator(Operator ast, Object obj) {
        return(createNullary(ast.spelling));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Values or Variable Names ">
    // Values or Variable Names
    public Object visitDotVname(DotVname ast, Object obj) {
        return(createBinary("Dot Vname", ast.I, ast.V));
    }
    
    public Object visitSimpleVname(SimpleVname ast, Object obj) {
        return(createUnary("Simple Vname", ast.I));
    }
    
    public Object visitSubscriptVname(SubscriptVname ast, Object obj) {
        return(createBinary("Subscript Vname", ast.V, ast.E));
    }
    
    public Object visitProgram(Program ast, Object obj) {
        return(createUnary("Program", ast.C));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Tree Creation Methods ">
    
    /**
     * Creates a nullary tree node (doesn't have any children).
     * @param caption The tree's caption (text to be shown when the tree is drawn).
     * @return The tree node.
     */
    public DefaultMutableTreeNode createNullary(String caption) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        
        return(t);
    }
    
    /**
     * Creates an unary tree node.
     * @param caption The tree's caption (text to be shown when the tree is drawn).
     * @param child1 The first children node.
     * @return The tree node.
     */
    public DefaultMutableTreeNode createUnary(String caption, AST child1) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        t.add((DefaultMutableTreeNode)child1.visit(this, null));
        
        return(t);
    }
    
    /**
     * Creates a binary tree node.
     * @param caption The tree's caption (text to be shown when the tree is drawn).
     * @param child1 The first children node.
     * @param child2 The second children node.
     * @return The tree node.
     */
    public DefaultMutableTreeNode createBinary(String caption, AST child1, AST child2) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        t.add((DefaultMutableTreeNode)child1.visit(this, null));
        t.add((DefaultMutableTreeNode)child2.visit(this, null));
        
        return(t);
    }
    
    /**
     * Creates a ternary tree node.
     * @param caption The tree's caption (text to be shown when the tree is drawn).
     * @param child1 The first children node.
     * @param child2 The second children node.
     * @param child3 The third children node.
     * @return The tree node.
     */
    public DefaultMutableTreeNode createTernary(String caption, AST child1, AST child2, AST child3) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        t.add((DefaultMutableTreeNode)child1.visit(this, null));
        t.add((DefaultMutableTreeNode)child2.visit(this, null));
        t.add((DefaultMutableTreeNode)child3.visit(this, null));
        
        return(t);        
    }
    
    /**
     * Creates a quaternary tree node.
     * @param caption The tree's caption (text to be shown when the tree is drawn).
     * @param child1 The first children node.
     * @param child2 The second children node.
     * @param child3 The third children node.
     * @param child4 The fourth children node.
     * @return The tree node.
     */
    public DefaultMutableTreeNode createQuaternary(String caption, AST child1, AST child2, AST child3, AST child4) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        t.add((DefaultMutableTreeNode)child1.visit(this, null));
        t.add((DefaultMutableTreeNode)child2.visit(this, null));
        t.add((DefaultMutableTreeNode)child3.visit(this, null));
        t.add((DefaultMutableTreeNode)child4.visit(this, null));
        
        return(t);             
    }
    
    public DefaultMutableTreeNode createQuinary(String caption, AST child1, AST child2, AST child3, AST child4,AST child5) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        t.add((DefaultMutableTreeNode)child1.visit(this, null));
        t.add((DefaultMutableTreeNode)child2.visit(this, null));
        t.add((DefaultMutableTreeNode)child3.visit(this, null));
        t.add((DefaultMutableTreeNode)child4.visit(this, null));
        t.add((DefaultMutableTreeNode)child5.visit(this, null));
        
        return(t);             
    }
    // </editor-fold>
}
