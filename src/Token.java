import java.util.HashMap;
import java.util.Locale;

public class Token {

    //class attribute
    private static HashMap<String,String> tokens;

    //fields
    private final String lexem;
    private final String value;

    //getters

    public String getLexem() {
        return lexem;
    }

    public String getValue() {
        return value;
    }


    private static void init(){
        tokens = new HashMap<String,String>();

        //declaration
        tokens.put("declare","declare_keyword");
        tokens.put("number","number_type");
        tokens.put("string","string_type");
        tokens.put("boolean","boolean_type");
        tokens.put("constant","const_keyword");

        //block keywords
        tokens.put("begin","begin_keyword");
        tokens.put("if","if_keyword");
        tokens.put("then","then_keyword");
        tokens.put("else","else_keyword");
        tokens.put("endif","endif_keyword");
        tokens.put("end","end_keyword");

        //space
        tokens.put("\n","newline");
        tokens.put("\t","tab");

        //separator
        tokens.put(";","separator");

        //operators
        tokens.put("+","ar_op_plus");
        tokens.put("-","ar_op_minus");
        tokens.put("*","ar_op_mul");
        tokens.put("/","ar_op_div");
        tokens.put("%","ar_op_mod");

        //affect
        tokens.put(":=","assignment_op");

        //comp
        tokens.put("!=", "comp_op_diff");
        tokens.put("=","comp_op_equal");
        tokens.put("<","comp_op_lt");
        tokens.put("<=","comp_op_lte");
        tokens.put(">","comp_op_gt");
        tokens.put(">=","comp_op_gte");

        //boolean values
        tokens.put("false","boolean_value");
        tokens.put("true","boolean_value");

        //logical operators
        tokens.put("or","op_or");
        tokens.put("and","op_and");
        tokens.put("not","op_not");
    }

    private static boolean isDigit(char c){
        return c > 47 && c < 58;
    }
    private static boolean isLetter(char c){
        return c > 64 && c < 123;
    }


    //constructeur
    public Token(String lexem) {
        init();
        this.lexem = lexem;
        if ( tokens.containsKey(this.lexem.toLowerCase())  )
            this.value = tokens.get(this.lexem.toLowerCase());
        else
            this.value = state0();
    }

    //affichage
    @Override
    public String toString() {
        return "{" + lexem + " : " + value + "}";
    }

    //erreur
    public boolean isError(){
        return this.value.contains("Lexical Error") ;
    }

    //retourner l'erreur
    private String error(){
        String lexemError = lexem;
        lexemError = lexem.substring(0,index+1) + "^" + lexem.substring(index+1);
        return "Lexical Error : invalid character : '" + lexemError + ".";
    }
    /*
    final private String[] characters = {
            "_",
            ";",
            ":=",
            "=",
            "+",
            "-",
            "/",
            "*",
            "%",
            ".",
            "<",
            ">"
    };
*/


    //implementation directe de l'automate
    private int index = -1;

    //unite courante
    private char currentUnit(){
        if ( index < this.lexem.length() )
            return  this.lexem.charAt(index);
        return 0;
    }

    //avancer dans le lexeme
    private void advance(){
        this.index++;
    }
    //reculer dans le lexeme
    private void goback(){
        this.index--;
    }
    private String state0(){
        advance();
        if(isDigit(currentUnit()))
            return state2();
        if(isLetter(currentUnit()))
            return state5();
        switch ( currentUnit() ){
            case '+' | '-' : return state1();
            case '"' | '\'' : return state7();
            default: goback();
                     return error();
        }
    }

    private String state1(){
        advance();
        if(isDigit(currentUnit()))
            return state2();
        else {
            goback();
            return error();
        }
    }

    private String state2(){
        advance();
        if( currentUnit() == 0 )
            return "number_value";
        if(isDigit(currentUnit()))
            return state2();
        if ( currentUnit() == '.')
            return state3();
        goback();
        return error();
    }

    private String state3(){
        advance();
        if(isDigit(currentUnit()))
            return state4();
        else {
            goback();
            return error();
        }
    }

    private String state4(){
        advance();
        if( currentUnit() == 0 )
            return "number_value";
        if (isDigit(currentUnit()))
            return state4();
        goback();
        return error();
    }

    private String state5(){
        advance();
        if( currentUnit() == 0 )
            return "identifier";
        if( isDigit(currentUnit()) ||isLetter(currentUnit()) )
            return state5();
        if (currentUnit() == '_')
            return state6();
        goback();
        return error();
    }

    private String state6(){
        advance();
        if( isDigit(currentUnit()) ||isLetter(currentUnit()) )
            return state5();
        goback();
        return error();
    }

    private String state7(){
        advance();
        if ( currentUnit() == 0 ) {
            goback();
            return error();
        }
        if( currentUnit() != '"' && currentUnit() != '\'' )
            return state7();
        if (currentUnit() == '"' || currentUnit() == '\'')
            return state8();
        goback();
        return error();
    }

    private String state8(){
        advance();
        if( currentUnit() == 0 )
            return "string_value";
        goback();
        return error();
    }

}
