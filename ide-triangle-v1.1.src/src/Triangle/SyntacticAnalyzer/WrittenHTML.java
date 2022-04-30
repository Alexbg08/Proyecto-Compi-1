package Triangle.SyntacticAnalyzer;

import java.io.FileWriter;
import java.io.IOException;


public class WrittenHTML {

    private String fileName;
    private Scanner lexicalAnalyser;
    private Token currentToken;
    private String textHTML="";

    public WrittenHTML (String fileName, Scanner lexicalAnalyser) {
        this.fileName = fileName;
        this.lexicalAnalyser=lexicalAnalyser;

    }
    public void write(){
        try {
            //Abre el archivo HTML para escribir
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(textHTML);
            fileWriter.write(FormatHTML.startHTML());
            //HTML header
            //textHTML="<p style=\"font-family: 'DejaVu Sans', monospace;\">";

            //scan begins
            currentToken = lexicalAnalyser.scan();

            while (currentToken.kind != Token.EOT) {
                
                switch (currentToken.kind){
                    case Token.INTLITERAL:
                    case Token.CHARLITERAL:
                        fileWriter.write(FormatHTML.blueHTMLString(currentToken.spelling));
                        break;

                    case 'a'://es un comentario
                        fileWriter.write(FormatHTML.commentHTMLString(currentToken.spelling));
                        break;
                    case 'b'://es un comentario pero con espacio
                        fileWriter.write(FormatHTML.commentHTMLString(currentToken.spelling));
                        fileWriter.write(FormatHTML.addLineBreak());
                        break;
                    case 'c'://if the text is a space
                        textHTML=textHTML+"<font style='padding-left:1em'></font>";
                        break;
                    case 'd'://if the text is a \n
                        fileWriter.write(FormatHTML.addLineBreak());
                        break;
                    case 'e'://\t
                        fileWriter.write(FormatHTML.t(currentToken.spelling));
                        break;
                    case 'f'://\r
                        break;


                    default://if is a reserved word or the other token
                        if (currentToken.kind>Token.OPERATOR && currentToken.kind<Token.DOT){ //reserved word
                            fileWriter.write(FormatHTML.boldHTMLString(currentToken.spelling));
                            //textHTML=textHTML+"<b>"+currentToken.spelling+"</b>";
                            
                        }else if(currentToken.kind>-1 && currentToken.kind<45) {//the other token
                            
                            textHTML=textHTML+currentToken.spelling;
                        }else{//Token.ERROR = Lexical Error
                            return;
                        }
                        break;
                    
                }
                currentToken = lexicalAnalyser.scan();
            }
            //close the main tag
            fileWriter.write(FormatHTML.finishHTML());
            //textHTML=textHTML+"</p>";

            
            //Cierra el escritor de HTML
            fileWriter.close();

        } catch (IOException e) {
            System.err.println("Error con el archivo del HTML");
            e.printStackTrace();
        }

    }


}
