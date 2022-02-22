import java.util.ArrayList;

public class Lexer {


    //code source
    private String text;

    //position dans texte
    private int position;

    //unite actuelle
    private char currentUnit;

    //table des tokens
    private ArrayList<Token> tokenTable;

    public ArrayList<Token> getTokenTable() {
        return (ArrayList<Token>) tokenTable.clone();
    }

    //Erreurs
    private ArrayList<Token> errors;

    public ArrayList<Token> getErrors() {
        return (ArrayList<Token>) errors.clone();
    }

    public Lexer(String text) {
        this.text = text;
        this.tokenTable = new ArrayList<Token>();
        this.errors = new ArrayList<Token>();
    }

    /*
    //lire unite
    private void readUnit(){
        if(position < text.length()) {
            this.currentUnit = this.text.charAt(position);
            position++;
        }
    }
    */

    //op arithmetique
    private boolean isArOp(char c){
        return c=='+' || c=='-' || c=='*' || c=='/' || c=='%';
    }

    //creer la table des lexems
    public String[] makeLexems(){
        String str = this.text.trim();
        String currentLexem= "";
        ArrayList<String> lexems = new ArrayList<>() ;
        for (int i = 0; i < str.length(); i++) {
            if ( str.charAt(i) == '\'' || str.charAt(i) == '\"'){
                currentLexem += str.charAt(i);
                for (int j = i+1; j < str.length() ; j++) {
                    currentLexem += str.charAt(j);
                    if (str.charAt(j) == str.charAt(i)){
                        lexems.add(currentLexem);
                        currentLexem = "";
                        i = j+1;
                        break;
                    }
                }
            }
            if (str.charAt(i) == ' ' || str.charAt(i) == '\t' || str.charAt(i) == '\n') {
                if ( !currentLexem.isEmpty() ){
                    lexems.add(currentLexem);
                    currentLexem = "";
                }
                continue;
            }
            else{
                if ( isArOp(str.charAt(i)) || str.charAt(i) == ';'){
                    if ( !currentLexem.isEmpty() ){
                        lexems.add(currentLexem);
                        currentLexem = "";
                    }
                    lexems.add("" + str.charAt(i));
//                    this.tokenTable.add(new Token("" + str.charAt(i)));
                    continue;
                }
                if (str.charAt(i) == ':' || str.charAt(i) == '!'){
                    if ( !currentLexem.isEmpty() ){
                        lexems.add(currentLexem);
                        currentLexem = "" + str.charAt(i);
                    }
                    if ( i+1 < str.length() ){
                        if ( str.charAt(i+1) == '=')
                            currentLexem += str.charAt(i+1);
                        lexems.add(currentLexem);
                        currentLexem = "";
                        i++;
                        continue;
                    }
                }
                if (str.charAt(i) == '<' || str.charAt(i) == '>'){
                    if ( !currentLexem.isEmpty() )
                        lexems.add(currentLexem);
                    currentLexem = "" + str.charAt(i);

                    if ( i+1 < str.length() ){
                        if ( str.charAt(i+1) == '=') {
                            currentLexem += str.charAt(i + 1);
//                        System.out.println(currentLexem + "testing again");
                            lexems.add(currentLexem);
                            currentLexem = "";
                            i++;
                            continue;
                        }
                    }
                    lexems.add(currentLexem);
                    currentLexem = "";
                    continue;

                }
            }
            currentLexem+= str.charAt(i);
            }

        return lexems.toArray(new String[0]);
    }


    //creer la table des tokens
    public void makeTokenTable(){
        //String lines[] =
        //String lexems[] = this.text.trim().split("[ |\n|\t|;|]+");
//        String lexems[] = this.text.trim().split("[ |\n|\t]+");
        String[] lexems = this.makeLexems();
        for (String lexem:lexems) {
            Token newToken = new Token(lexem);
            if(newToken.isError())
                this.errors.add(newToken);
            else
                this.tokenTable.add(newToken);
        }
    }

}
