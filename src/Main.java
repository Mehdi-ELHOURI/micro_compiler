import java.io.IOException;

/*
        "DECLARE \n" +
        "x number := 3.14 ;\n" +
        "message constant string := 'Hello_World!' ;\n" +
        "BEGIN\n" +
        "IF x >= 10 THEN \n" +
        "x := x + 1 ;\n" +
        "ELSE \n" +
        "x := x - 1 ;\n" +
        "ENDIF ;\n" +
        "END ;\n"

*/
public class Main {
    public static void main(String[] args) {
        Lexer lex = null;
        try {
            lex = new Lexer(
                 Reader.getSourceCode()
            );
        } catch (IOException e) {
            System.out.println("Failed");
            System.exit(-1);
        }

        lex.makeTokenTable();
        for (Token t:
                lex.getTokenTable()) {
            System.out.println(t);
        }
        System.out.println("Errors :" + lex.getErrors());
        Parser parser = new Parser(lex);
        System.out.println(parser.parse());
        System.out.println(parser.getTable());
        System.out.println(parser.getTiv());
    }

}
