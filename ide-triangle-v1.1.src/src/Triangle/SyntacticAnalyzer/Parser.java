/*
 * @(#)Parser.java                        2.1 2003/10/07
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

/*
Modificaciones realizadas por Brayan Marín  y José Ramírez
Última modificación: 28/04/2022
*/

package Triangle.SyntacticAnalyzer;

import Triangle.ErrorReporter;
import Triangle.AbstractSyntaxTrees.ActualParameter;
import Triangle.AbstractSyntaxTrees.ActualParameterSequence;
import Triangle.AbstractSyntaxTrees.ArrayAggregate;
import Triangle.AbstractSyntaxTrees.ArrayExpression;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoterOptional;
import Triangle.AbstractSyntaxTrees.AssignCommand;
import Triangle.AbstractSyntaxTrees.BinaryExpression;
import Triangle.AbstractSyntaxTrees.CallCommand;
import Triangle.AbstractSyntaxTrees.CallExpression;
import Triangle.AbstractSyntaxTrees.CaseCharLiteralCommand;
import Triangle.AbstractSyntaxTrees.CaseIntLiteralCommand;
import Triangle.AbstractSyntaxTrees.CasesCommand;
import Triangle.AbstractSyntaxTrees.CharacterExpression;
import Triangle.AbstractSyntaxTrees.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.Command;
import Triangle.AbstractSyntaxTrees.CondRestOfIfCommand;
import Triangle.AbstractSyntaxTrees.ConstActualParameter;
import Triangle.AbstractSyntaxTrees.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.ConstFormalParameter;
import Triangle.AbstractSyntaxTrees.Declaration;
import Triangle.AbstractSyntaxTrees.DotVname;
import Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
import Triangle.AbstractSyntaxTrees.EmptyCommand;
import Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.EndRestOfIFCommand;
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
import Triangle.AbstractSyntaxTrees.RecordAggregate;
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
import Triangle.AbstractSyntaxTrees.TypeDenoter;
import Triangle.AbstractSyntaxTrees.UnaryExpression;
import Triangle.AbstractSyntaxTrees.VarActualParameter;
import Triangle.AbstractSyntaxTrees.VarDeclaration;
import Triangle.AbstractSyntaxTrees.VarDeclarationOptional;
import Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.Vname;
import Triangle.AbstractSyntaxTrees.VnameExpression;
import Triangle.AbstractSyntaxTrees.WhileCommand;

public class Parser {

  private Scanner lexicalAnalyser;
  private ErrorReporter errorReporter;
  private Token currentToken;
  private SourcePosition previousTokenPosition;

  public Parser(Scanner lexer, ErrorReporter reporter) {
    lexicalAnalyser = lexer;
    errorReporter = reporter;
    previousTokenPosition = new SourcePosition();
  }

// accept checks whether the current token matches tokenExpected.
// If so, fetches the next token.
// If not, reports a syntactic error.

  void accept (int tokenExpected) throws SyntaxError {
    if (currentToken.kind == tokenExpected) {
      previousTokenPosition = currentToken.position;
      currentToken = lexicalAnalyser.scan();
    } else {
      syntacticError("\"%\" expected here", Token.spell(tokenExpected));
    }
  }

  void acceptIt() {
    previousTokenPosition = currentToken.position;
    currentToken = lexicalAnalyser.scan();
  }

// start records the position of the start of a phrase.
// This is defined to be the position of the first
// character of the first token of the phrase.

  void start(SourcePosition position) {
    position.start = currentToken.position.start;
  }

// finish records the position of the end of a phrase.
// This is defined to be the position of the last
// character of the last token of the phrase.

  void finish(SourcePosition position) {
    position.finish = previousTokenPosition.finish;
  }

  void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
    SourcePosition pos = currentToken.position;
    errorReporter.reportError(messageTemplate, tokenQuoted, pos);
    throw(new SyntaxError());
  }

///////////////////////////////////////////////////////////////////////////////
//
// PROGRAMS
//
///////////////////////////////////////////////////////////////////////////////

  public Program parseProgram() {

    Program programAST = null;

    previousTokenPosition.start = 0;
    previousTokenPosition.finish = 0;
    currentToken = lexicalAnalyser.scan();

    try {
      Command cAST = parseCommand();
      programAST = new Program(cAST, previousTokenPosition);
      if (currentToken.kind != Token.EOT) {
        syntacticError("\"%\" not expected after end of program",
          currentToken.spelling);
      }
    }
    catch (SyntaxError s) { return null; }
    return programAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// LITERALS
//
///////////////////////////////////////////////////////////////////////////////

// parseIntegerLiteral parses an integer-literal, and constructs
// a leaf AST to represent it.

  IntegerLiteral parseIntegerLiteral() throws SyntaxError {
    IntegerLiteral IL = null;

    if (currentToken.kind == Token.INTLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      IL = new IntegerLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      IL = null;
      syntacticError("integer literal expected here", "");
    }
    return IL;
  }

// parseCharacterLiteral parses a character-literal, and constructs a leaf
// AST to represent it.

  CharacterLiteral parseCharacterLiteral() throws SyntaxError {
    CharacterLiteral CL = null;

    if (currentToken.kind == Token.CHARLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      CL = new CharacterLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      CL = null;
      syntacticError("character literal expected here", "");
    }
    return CL;
  }

// parseIdentifier parses an identifier, and constructs a leaf AST to
// represent it.

  Identifier parseIdentifier() throws SyntaxError {
    Identifier I = null;

    if (currentToken.kind == Token.IDENTIFIER) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      I = new Identifier(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      I = null;
      syntacticError("identifier expected here", "");
    }
    return I;
  }

// parseOperator parses an operator, and constructs a leaf AST to
// represent it.

  Operator parseOperator() throws SyntaxError {
    Operator O = null;

    if (currentToken.kind == Token.OPERATOR) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      O = new Operator(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      O = null;
      syntacticError("operator expected here", "");
    }
    return O;
  }

///////////////////////////////////////////////////////////////////////////////
//
// COMMANDS
//
///////////////////////////////////////////////////////////////////////////////

// parseCommand parses the command, and constructs an AST
// to represent its phrase structure.

  Command parseCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();

    start(commandPos);
    commandAST = parseSingleCommand();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Command c2AST = parseSingleCommand();
      finish(commandPos);
      commandAST = new SequentialCommand(commandAST, c2AST, commandPos);
    }
    return commandAST;
  }

  Command parseSingleCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(commandPos);
          commandAST = new CallCommand(iAST, apsAST, commandPos);

        } else {

          Vname vAST = parseRestOfVname(iAST);
          accept(Token.BECOMES);
          Expression eAST = parseExpression();
          finish(commandPos);
          commandAST = new AssignCommand(vAST, eAST, commandPos);
        }
      }
      break;
    
     //Nueva alternativa. Brayan Marín Quirós
    case Token.NOTHING:
      {
       acceptIt();
       finish(commandPos);
       commandAST = new EmptyCommand(commandPos);
      }
    break;
    
    //Alternativa modificada. Brayan Marín Quirós
    case Token.LET:
      {
        acceptIt(); 
        Declaration dAST = parseDeclaration(); 
        accept(Token.IN);
        Command cAST = parseCommand();
        accept(Token.END);
        finish(commandPos);
        commandAST = new LetCommand(dAST, cAST, commandPos);
      }
      break;
    
    //Alternativa modificada para utilizar recursividad. Brayan Marín Quirós 
    case Token.IF:
      {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.THEN);
        Command c1AST = parseCommand(); 
        Command restIfAST = parseRestOfIf();
        finish(commandPos);
        commandAST = new IfCommand(eAST, c1AST, restIfAST, commandPos);
      }
      break;
    
    //Alternativa agregada. Brayan Marín Quirós 
    case Token.REPEAT:
      {
        acceptIt();
        Expression eAST;
        Command cAST;
        
        switch(currentToken.kind){
            
            case Token.WHILE:
            {
                acceptIt();
                Command c2AST; 
                eAST = parseExpression();
                accept(Token.DO);
                cAST = parseCommand();
                
                switch(currentToken.kind){
                    case Token.LEAVE:
                        //Si el while tiene ("leave" command) en el do
                        c2AST = parseCommand();
                        break;
                    default:
                        //Si el while no tiene ("leave" command) en el do
                        c2AST = null;
                        break;
                }
                
                accept(Token.END);
                finish(commandPos);
                
                if(c2AST == null){
                    commandAST = new SingleRepeatWhileCommand(eAST, cAST, commandPos);               
                }else{
                    commandAST = new MultipleRepeatWhileCommand(eAST, cAST, c2AST, commandPos);
                }  
            }
            break;
            
            case Token.UNTIL:
            {
                acceptIt();
                Command c2AST; 
                eAST = parseExpression();
                accept(Token.DO);
                cAST = parseCommand();
                
                switch(currentToken.kind){
                    case Token.LEAVE:
                        //Si el until tiene ("leave" command) en el do
                        c2AST = parseCommand();
                        break;
                    default:
                        //Si el until no tiene ("leave" command) en el do
                        c2AST = null;
                        break;
                }
                accept(Token.END);
                finish(commandPos);
                
                if(c2AST == null){
                    commandAST = new SingleRepeatUntilCommand(eAST, cAST, commandPos);               
                }else{
                    commandAST = new MultipleRepeatUntilCommand(eAST, cAST, c2AST, commandPos);
                }  
            }
            break;
            
            case Token.DO:
            {
                acceptIt();
                cAST = parseCommand();
                
                switch(currentToken.kind)
                {
                    case Token.WHILE:
                    {
                        acceptIt();
                        Command c2AST; 
                        eAST = parseExpression();
                        
                        switch(currentToken.kind)
                        {
                            case Token.LEAVE:
                            {
                                //Si el while tiene ("leave" command)
                                c2AST = parseCommand();
                                break;
                            }
                            default:
                            {
                                //Si el while no tiene ("leave" command)
                                c2AST = null;
                                break;
                            }
                        }
                        
                        accept(Token.END);
                        finish(commandPos);
                        
                        if(c2AST == null){
                            commandAST = new SingleDoWhileCommand(eAST, cAST, commandPos);               
                        }else{
                            commandAST = new MultipleDoWhileCommand(eAST, cAST, c2AST, commandPos);
                        }  
                    }
                    break;
                    
                    case Token.UNTIL:
                    {
                        acceptIt();
                        Command c2AST; 
                        eAST = parseExpression();
                        
                        switch(currentToken.kind)
                        {
                            case Token.LEAVE:
                            {
                                //Si el until tiene ("leave" command)
                                c2AST = parseCommand();
                                break;
                            }
                            default:
                            {
                                //Si el until no tiene ("leave" command)
                                c2AST = null;
                                break;
                            }
                        }
                        
                        accept(Token.END);
                        finish(commandPos);
                        
                        if(c2AST == null){
                            commandAST = new SingleDoUntilCommand(eAST, cAST, commandPos);               
                        }else{
                            commandAST = new MultipleDoUntilCommand(eAST, cAST, c2AST, commandPos);
                        }  
                    }
                    break;
                }
            }
            break;

            
            default:
            {
                syntacticError("\"%\" cannot follow a repeat command",
        currentToken.spelling);
            }
            break;   
        }
     }
     break;
    
    case Token.FOR:
    {
        acceptIt();
        
        Command cAST;
        Command c2AST;
        
        Identifier iAST = parseIdentifier();
        accept(Token.FROM);
        Expression eAST = parseExpression();
        accept(Token.DOUBLEDOT);
        Expression e2AST = parseExpression();
        
        switch(currentToken.kind){
            case Token.DO:
            {
                acceptIt();
                cAST = parseCommand();
                
                switch(currentToken.kind){
                    case Token.LEAVE:
                    {
                        //Si el do tiene ("leave" command)
                        c2AST = parseCommand();
                        break;
                    }
                    default:
                    {
                        //Si el do no tiene ("leave" command)
                        c2AST = null;
                        break;
                    }
                }
                
                accept(Token.END);
                finish(commandPos);

                if(c2AST == null){
                    commandAST = new SingleForDoCommand(iAST, eAST, e2AST, cAST, commandPos);               
                }else{
                    commandAST = new MultipleForDoCommand(iAST, eAST, e2AST, cAST, c2AST, commandPos);
                }
            }
            
            case Token.WHILE:
            {
                acceptIt();
                Expression e3AST = parseExpression();
                accept(Token.DO);
                cAST = parseCommand();
                
                switch(currentToken.kind){
                    case Token.LEAVE:
                        //Si el while tiene ("leave" command) en el do
                        c2AST = parseCommand();
                        break;
                    default:
                        //Si el while no tiene ("leave" command) en el do
                        c2AST = null;
                        break;
                }
                accept(Token.END);
                finish(commandPos);
                
                if(c2AST == null){
                    Declaration dAST = new ForVarDeclaration(iAST,eAST, commandPos);
                    commandAST = new SingleForWhileCommand(dAST, e2AST, e3AST, cAST, commandPos);               
                }else{
                    Declaration dAST = new ForVarDeclaration(iAST,eAST, commandPos);
                    commandAST = new MultipleForWhileCommand(dAST, e2AST, e3AST, cAST, c2AST, commandPos);
                }  
            }
            break;
            
            case Token.UNTIL:
            {
                acceptIt(); 
                Expression e3AST = parseExpression();
                accept(Token.DO);
                cAST = parseCommand();
                
                switch(currentToken.kind){
                    case Token.LEAVE:
                        //Si el until tiene ("leave" command) en el do
                        c2AST = parseCommand();
                        break;
                    default:
                        //Si el until no tiene ("leave" command) en el do
                        c2AST = null;
                        break;
                }
                accept(Token.END);
                finish(commandPos);
                
                if(c2AST == null){
                    Declaration dAST = new ForVarDeclaration(iAST,eAST, commandPos);
                    commandAST = new SingleForUntilCommand(dAST, e2AST, e3AST, cAST, commandPos);               
                }else{
                    Declaration dAST = new ForVarDeclaration(iAST,eAST, commandPos);
                    commandAST = new MultipleForUntilCommand(dAST, e2AST, e3AST, cAST, c2AST, commandPos);
                }  
            }
            break;
            
            default:
            {
                syntacticError("\"%\" cannot follow a for command",
        currentToken.spelling);
            }
            break;
        }
        
    }
    break;

    

/*COIDGO ELIMINADO SOLICITADO POR EL PROFESOR  
  Realizado por Brayan Marín Quirós   
 */  
      
//    case Token.BEGIN:
//      acceptIt();
//      commandAST = parseCommand();
//      accept(Token.END);
//      break;
//
//    case Token.LET:
//      {
//        acceptIt();
//        Declaration dAST = parseDeclaration();
//        accept(Token.IN);
//        Command cAST = parseSingleCommand();
//        finish(commandPos);
//        commandAST = new LetCommand(dAST, cAST, commandPos);
//      }
//      break;
//
//    case Token.IF:
//      {
//        acceptIt();
//        Expression eAST = parseExpression();
//        accept(Token.THEN);
//        Command c1AST = parseSingleCommand();
//        accept(Token.ELSE);
//        Command c2AST = parseSingleCommand();
//        finish(commandPos);
//        commandAST = new IfCommand(eAST, c1AST, c2AST, commandPos);
//      }
//      break;
//
//    case Token.WHILE:
//      {
//        acceptIt();
//        Expression eAST = parseExpression();
//        accept(Token.DO);
//        Command cAST = parseSingleCommand();
//        finish(commandPos);
//        commandAST = new WhileCommand(eAST, cAST, commandPos);
//      }
//      break;

    case Token.SEMICOLON:
    case Token.END:
    case Token.ELSE:
    case Token.IN:
    case Token.EOT:

      finish(commandPos);
      commandAST = new EmptyCommand(commandPos);
      break;

    default:
      syntacticError("\"%\" cannot start a command",
        currentToken.spelling);
      break;

    }

    return commandAST;
  }
  
/*
  Nueva función agregada: parseRestOfIf()
  Esta función se encarga de procesar todos los elsif de manera recursiva.
  Creada por Brayan Marín Quirós utilizando la explicación del profesor con 
  respecto al RestOfIf
*/
  Command parseRestOfIf() throws SyntaxError {

    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);

    switch (currentToken.kind) {

      case Token.ELSIF: {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.THEN);
        Command cAST = parseCommand();
        Command elsAST = parseRestOfIf();
        finish(commandPos);
        commandAST = new CondRestOfIfCommand(eAST, cAST, elsAST, commandPos);
      }
      break;

      case Token.ELSE:
      {
        acceptIt();
        Command cAST = parseCommand();
        accept(Token.END);
        finish(commandPos);
        commandAST = new EndRestOfIFCommand(cAST, commandPos);
      }
      break;

      default:
        syntacticError("\"%\" elsif or else expected here", currentToken.spelling);
        break;

    }

    return commandAST;

  }
    
/*
  Nueva función agregada: parseCases()
  Encargada de procesar uno o más case
  Creada por Brayan Marín Quirós
*/
  Command parseCases() throws SyntaxError {
      
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);
    
      do {
          
        Command cAST = parseCase();
        finish(commandPos);
        commandAST = new CasesCommand(cAST, commandPos);

      } while (currentToken.kind == Token.WHEN);
      
    return commandAST;
  }
  
/*
Nueva función agregada: parseCases()
Encargada de procesar un comando cuando haya un CaseLiteral
Creada por Brayan Marín Quirós  
*/
  Command parseCase() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);
    
    if (currentToken.kind == Token.WHEN){
        
        acceptIt();
        Command cAST = parseCaseLiteral();
        Command c2AST;
        
        switch(currentToken.kind){
          case Token.DOUBLEDOT:
             //Si el when tiene (".." Case-Literal)
             c2AST = parseCaseLiteral();
             break;
          default:
              //Si el when no tiene (".." Case-Literal)
              c2AST = null;
              break;
        }
        
        accept(Token.THEN);
        Command c3AST = parseCaseLiteral();
        finish(commandPos);

        if(c2AST == null){
            commandAST = new SingleCaseCommand(cAST, c2AST, commandPos);               
        }else{
            commandAST = new MultipleCaseCommand(cAST, c2AST, c3AST, commandPos); 
        }
        
        
    } else {   
     syntacticError("\"%\" when expected here", currentToken.spelling);
    }
    
    return commandAST;
  }
  
/*
Nueva función agregada: parseCaseLiteral()
  
*/
  Command parseCaseLiteral() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);
    
    switch(currentToken.kind){
        case Token.INTLITERAL:
        {
            IntegerLiteral intAST = parseIntegerLiteral();
            finish(commandPos);
            commandAST = new CaseIntLiteralCommand(intAST, commandPos); 
        }
        break;
        case Token.CHARLITERAL:
        {
            CharacterLiteral chAST = parseCharacterLiteral();
            finish(commandPos);
            commandAST = new CaseCharLiteralCommand(chAST, commandPos); 
        }
        break;
        
        default:
        syntacticError("\"%\" <int> or <char> "
                + "expected here", currentToken.spelling);
        break;
    }      
    return commandAST;
  }
  
  

///////////////////////////////////////////////////////////////////////////////
//
// EXPRESSIONS
//
///////////////////////////////////////////////////////////////////////////////

  Expression parseExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();

    start (expressionPos);

    switch (currentToken.kind) {

    case Token.LET:
      {
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Expression eAST = parseExpression();
        finish(expressionPos);
        expressionAST = new LetExpression(dAST, eAST, expressionPos);
      }
      break;

    case Token.IF:
      {
        acceptIt();
        Expression e1AST = parseExpression();
        accept(Token.THEN);
        Expression e2AST = parseExpression();
        accept(Token.ELSE);
        Expression e3AST = parseExpression();
        finish(expressionPos);
        expressionAST = new IfExpression(e1AST, e2AST, e3AST, expressionPos);
      }
      break;

    default:
      expressionAST = parseSecondaryExpression();
      break;
    }
    return expressionAST;
  }

  Expression parseSecondaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    expressionAST = parsePrimaryExpression();
    while (currentToken.kind == Token.OPERATOR) {
      Operator opAST = parseOperator();
      Expression e2AST = parsePrimaryExpression();
      expressionAST = new BinaryExpression (expressionAST, opAST, e2AST,
        expressionPos);
    }
    return expressionAST;
  }

  Expression parsePrimaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    switch (currentToken.kind) {

    case Token.INTLITERAL:
      {
        IntegerLiteral ilAST = parseIntegerLiteral();
        finish(expressionPos);
        expressionAST = new IntegerExpression(ilAST, expressionPos);
      }
      break;

    case Token.CHARLITERAL:
      {
        CharacterLiteral clAST= parseCharacterLiteral();
        finish(expressionPos);
        expressionAST = new CharacterExpression(clAST, expressionPos);
      }
      break;

    case Token.LBRACKET:
      {
        acceptIt();
        ArrayAggregate aaAST = parseArrayAggregate();
        accept(Token.RBRACKET);
        finish(expressionPos);
        expressionAST = new ArrayExpression(aaAST, expressionPos);
      }
      break;

    case Token.LCURLY:
      {
        acceptIt();
        RecordAggregate raAST = parseRecordAggregate();
        accept(Token.RCURLY);
        finish(expressionPos);
        expressionAST = new RecordExpression(raAST, expressionPos);
      }
      break;

    case Token.IDENTIFIER:
      {
        Identifier iAST= parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(expressionPos);
          expressionAST = new CallExpression(iAST, apsAST, expressionPos);

        } else {
          Vname vAST = parseRestOfVname(iAST);
          finish(expressionPos);
          expressionAST = new VnameExpression(vAST, expressionPos);
        }
      }
      break;

    case Token.OPERATOR:
      {
        Operator opAST = parseOperator();
        Expression eAST = parsePrimaryExpression();
        finish(expressionPos);
        expressionAST = new UnaryExpression(opAST, eAST, expressionPos);
      }
      break;

    case Token.LPAREN:
      acceptIt();
      expressionAST = parseExpression();
      accept(Token.RPAREN);
      break;

    default:
      syntacticError("\"%\" cannot start an expression",
        currentToken.spelling);
      break;

    }
    return expressionAST;
  }

  RecordAggregate parseRecordAggregate() throws SyntaxError {
    RecordAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Identifier iAST = parseIdentifier();
    accept(Token.IS);
    Expression eAST = parseExpression();

    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      RecordAggregate aAST = parseRecordAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleRecordAggregate(iAST, eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleRecordAggregate(iAST, eAST, aggregatePos);
    }
    return aggregateAST;
  }

  ArrayAggregate parseArrayAggregate() throws SyntaxError {
    ArrayAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Expression eAST = parseExpression();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ArrayAggregate aAST = parseArrayAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleArrayAggregate(eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleArrayAggregate(eAST, aggregatePos);
    }
    return aggregateAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// VALUE-OR-VARIABLE NAMES
//
///////////////////////////////////////////////////////////////////////////////

  Vname parseVname () throws SyntaxError {
    Vname vnameAST = null; // in case there's a syntactic error
    Identifier iAST = parseIdentifier();
    vnameAST = parseRestOfVname(iAST);
    return vnameAST;
  }

  Vname parseRestOfVname(Identifier identifierAST) throws SyntaxError {
    SourcePosition vnamePos = new SourcePosition();
    vnamePos = identifierAST.position;
    Vname vAST = new SimpleVname(identifierAST, vnamePos);

    while (currentToken.kind == Token.DOT ||
           currentToken.kind == Token.LBRACKET) {

      if (currentToken.kind == Token.DOT) {
        acceptIt();
        Identifier iAST = parseIdentifier();
        vAST = new DotVname(vAST, iAST, vnamePos);
      } else {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.RBRACKET);
        finish(vnamePos);
        vAST = new SubscriptVname(vAST, eAST, vnamePos);
      }
    }
    return vAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// DECLARATIONS
//
///////////////////////////////////////////////////////////////////////////////

  Declaration parseDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    declarationAST = parseCompoundDeclaration();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Declaration d2AST = parseCompoundDeclaration();
      finish(declarationPos);
      declarationAST = new SequentialDeclaration(declarationAST, d2AST,
        declarationPos);
    }
    return declarationAST;
  }

  Declaration parseSingleDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);

    switch (currentToken.kind) {

    case Token.CONST:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new ConstDeclaration(iAST, eAST, declarationPos);
      }
      break;
      /*
      Se agregó nueva alternativa para declarar variables
      Autor: Brayan Marín Quirós
      */
    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        
        switch(currentToken.kind){
            case Token.COLON:
            {
             acceptIt();
             TypeDenoter tAST = parseTypeDenoter();
             finish(declarationPos);
             declarationAST = new VarDeclaration(iAST, tAST, declarationPos);
            } 
            break;
            case Token.BECOMES:
            {
             acceptIt();
             Expression eAST = parseExpression();
             finish(declarationPos);
             declarationAST = new VarDeclarationOptional(iAST, eAST, declarationPos);
            } 
            break;
            
            default:
              syntacticError("\"%\" ':' or ':=' expected here",
                      currentToken.spelling);
            break;
        }
      }
      break;
      
     /*
      Se cambió el singleCommand por un Command
      Autor: Brayan Marín Quirós
     */
    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.IS);
        Command cAST = parseCommand();
        accept(Token.END);
        finish(declarationPos);
        declarationAST = new ProcDeclaration(iAST, fpsAST, cAST, declarationPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new FuncDeclaration(iAST, fpsAST, tAST, eAST,declarationPos);
      }
      break;

    case Token.TYPE:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        TypeDenoter tAST = parseTypeDenoter();
        finish(declarationPos);
        declarationAST = new TypeDeclaration(iAST, tAST, declarationPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a declaration",
        currentToken.spelling);
      break;

    }
    return declarationAST;
  }
  
  /*
  Función agregada: parseCompoundDeclaration
  Se encarga de manejar las declaraciones compuestas
  Autor: Brayan Marín Quirós
  */
  Declaration parseCompoundDeclaration() throws SyntaxError {
    
    Declaration compoundDeclarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    
    switch(currentToken.kind){
        case Token.RECURSIVE:
        {
           acceptIt();
           Declaration pfAST = parseProcFuncs();
           accept(Token.END);
           finish(declarationPos);
           compoundDeclarationAST = new RecursiveDeclaration(pfAST, declarationPos);
        }
        break;
        
        case Token.PRIVATE:
        {
           acceptIt();
           Declaration dAST = parseDeclaration();
           accept(Token.IN);
           Declaration d2AST = parseDeclaration();
           accept(Token.END);
           finish(declarationPos);
           compoundDeclarationAST = new PrivateDeclaration(dAST, d2AST, declarationPos);
        }
        break;
        
        default: 
           compoundDeclarationAST = parseSingleDeclaration();
           break;
    }
    
    return compoundDeclarationAST;
  }
  
  /*
  Función agregada: parseProcFuncs
  Procesa una o más proc-func
  Autor: Brayan Marín Quirós
  */
  Declaration parseProcFuncs() throws SyntaxError{
        
    Declaration procFuncsDeclarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    
    Declaration pfAST = parseProcFunc();

      do {
        if (currentToken.kind == Token.AND) {
            acceptIt();
            Declaration pf2AST = parseProcFunc();
            finish(declarationPos);
            procFuncsDeclarationAST = new ProcFuncsDeclaration(pfAST, pf2AST, declarationPos);
            
        }
      } while (currentToken.kind == Token.AND);
    
    return procFuncsDeclarationAST;
  }
  
  /*
  Función agregada: parseProcFunc
  Se utiliza para crear un proc o un func
  Autor: Brayan Marín Quirós
  */
  Declaration parseProcFunc() throws SyntaxError{
      
    Declaration procFuncDeclarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    
    switch(currentToken.kind){
        case Token.PROC:
        {
            acceptIt();
            Identifier iAST = parseIdentifier();
            accept(Token.LPAREN);
            FormalParameterSequence fpsAST = parseFormalParameterSequence();
            accept(Token.RPAREN);
            accept(Token.IS);
            Command cAST = parseCommand();
            accept(Token.END);
            finish(declarationPos);
            procFuncDeclarationAST = new ProcDeclaration(iAST, fpsAST, cAST, 
                    declarationPos);
            
        }
        break;
        
        case Token.FUNC:
        {
            acceptIt();
            Identifier iAST = parseIdentifier();
            accept(Token.LPAREN);
            FormalParameterSequence fpsAST = parseFormalParameterSequence();
            accept(Token.RPAREN);
            accept(Token.COLON);
            TypeDenoter tAST = parseTypeDenoter();
            accept(Token.IS);
            Expression eAST = parseExpression();
            finish(declarationPos);
            procFuncDeclarationAST = new FuncDeclaration(iAST, fpsAST, tAST, 
                    eAST, declarationPos);
        }
        break;
        
        default:
            syntacticError("\"%\" proc or func expected here",
                    currentToken.spelling);
    }
    
    return procFuncDeclarationAST;


  }
///////////////////////////////////////////////////////////////////////////////
//
// PARAMETERS
//
///////////////////////////////////////////////////////////////////////////////

  FormalParameterSequence parseFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST;

    SourcePosition formalsPos = new SourcePosition();

    start(formalsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(formalsPos);
      formalsAST = new EmptyFormalParameterSequence(formalsPos);

    } else {
      formalsAST = parseProperFormalParameterSequence();
    }
    return formalsAST;
  }

  FormalParameterSequence parseProperFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST = null; // in case there's a syntactic error;

    SourcePosition formalsPos = new SourcePosition();
    start(formalsPos);
    FormalParameter fpAST = parseFormalParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FormalParameterSequence fpsAST = parseProperFormalParameterSequence();
      finish(formalsPos);
      formalsAST = new MultipleFormalParameterSequence(fpAST, fpsAST,
        formalsPos);

    } else {
      finish(formalsPos);
      formalsAST = new SingleFormalParameterSequence(fpAST, formalsPos);
    }
    return formalsAST;
  }

  FormalParameter parseFormalParameter() throws SyntaxError {
    FormalParameter formalAST = null; // in case there's a syntactic error;

    SourcePosition formalPos = new SourcePosition();
    start(formalPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new ConstFormalParameter(iAST, tAST, formalPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new VarFormalParameter(iAST, tAST, formalPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        finish(formalPos);
        formalAST = new ProcFormalParameter(iAST, fpsAST, formalPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new FuncFormalParameter(iAST, fpsAST, tAST, formalPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a formal parameter",
        currentToken.spelling);
      break;

    }
    return formalAST;
  }


  ActualParameterSequence parseActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST;

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(actualsPos);
      actualsAST = new EmptyActualParameterSequence(actualsPos);

    } else {
      actualsAST = parseProperActualParameterSequence();
    }
    return actualsAST;
  }

  ActualParameterSequence parseProperActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST = null; // in case there's a syntactic error

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    ActualParameter apAST = parseActualParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ActualParameterSequence apsAST = parseProperActualParameterSequence();
      finish(actualsPos);
      actualsAST = new MultipleActualParameterSequence(apAST, apsAST,
        actualsPos);
    } else {
      finish(actualsPos);
      actualsAST = new SingleActualParameterSequence(apAST, actualsPos);
    }
    return actualsAST;
  }

  ActualParameter parseActualParameter() throws SyntaxError {
    ActualParameter actualAST = null; // in case there's a syntactic error

    SourcePosition actualPos = new SourcePosition();

    start(actualPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
    case Token.INTLITERAL:
    case Token.CHARLITERAL:
    case Token.OPERATOR:
    case Token.LET:
    case Token.IF:
    case Token.LPAREN:
    case Token.LBRACKET:
    case Token.LCURLY:
      {
        Expression eAST = parseExpression();
        finish(actualPos);
        actualAST = new ConstActualParameter(eAST, actualPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Vname vAST = parseVname();
        finish(actualPos);
        actualAST = new VarActualParameter(vAST, actualPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new ProcActualParameter(iAST, actualPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new FuncActualParameter(iAST, actualPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start an actual parameter",
        currentToken.spelling);
      break;

    }
    return actualAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// TYPE-DENOTERS
//
///////////////////////////////////////////////////////////////////////////////

  TypeDenoter parseTypeDenoter() throws SyntaxError {
    TypeDenoter typeAST = null; // in case there's a syntactic error
    SourcePosition typePos = new SourcePosition();

    start(typePos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        finish(typePos);
        typeAST = new SimpleTypeDenoter(iAST, typePos);
      }
      break;
      
     //Cambios realizados: Se agregaron el doubledot y un IntegerLiteral mas
     //Autor: Brayan Marín Quirós
    case Token.ARRAY:
      {
          
        acceptIt();
        IntegerLiteral ilAST = parseIntegerLiteral();
          
        switch(currentToken.kind){
            
            case Token.OF:
            {
              acceptIt();
              TypeDenoter tAST = parseTypeDenoter();
              finish(typePos);
              typeAST = new ArrayTypeDenoter(ilAST, tAST, typePos);
            }
            case Token.DOUBLEDOT:{
                
              acceptIt();
              IntegerLiteral il2AST = parseIntegerLiteral();
              accept(Token.OF);
              TypeDenoter tAST = parseTypeDenoter();
              finish(typePos);
              typeAST = new ArrayTypeDenoterOptional(ilAST, il2AST, tAST, typePos);
            }
            default:
              syntacticError("\"%\" of or '..' expected here",
                      currentToken.spelling);
            break;
        }

      }
      break;

    case Token.RECORD:
      {
        acceptIt();
        FieldTypeDenoter fAST = parseFieldTypeDenoter();
        accept(Token.END);
        finish(typePos);
        typeAST = new RecordTypeDenoter(fAST, typePos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a type denoter",
        currentToken.spelling);
      break;

    }
    return typeAST;
  }

  FieldTypeDenoter parseFieldTypeDenoter() throws SyntaxError {
    FieldTypeDenoter fieldAST = null; // in case there's a syntactic error

    SourcePosition fieldPos = new SourcePosition();

    start(fieldPos);
    Identifier iAST = parseIdentifier();
    accept(Token.COLON);
    TypeDenoter tAST = parseTypeDenoter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FieldTypeDenoter fAST = parseFieldTypeDenoter();
      finish(fieldPos);
      fieldAST = new MultipleFieldTypeDenoter(iAST, tAST, fAST, fieldPos);
    } else {
      finish(fieldPos);
      fieldAST = new SingleFieldTypeDenoter(iAST, tAST, fieldPos);
    }
    return fieldAST;
  }
}
