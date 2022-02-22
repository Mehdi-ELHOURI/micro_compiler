public class Parser {

    //Analyseur lexicale
    final private Lexer lex;

    //Table des symboles
    private final SymbolTable table;

    //VIC
    private TargetInstructionsVector tiv;


    //id courant
    private String currentId;
    //type de l 'id courant
    private String currentType;
    //valeur de l 'id courant
    private String currentValue;
    //id courant est constant
    private boolean currentIsConst;
    //type expression courante
    private String currentExpressionType;

    //initialiser entree
    private void init(){
        currentId = "";
        currentType = "";
        currentValue = "";
        currentIsConst = false;
        currentExpressionType = "";
    }
    //implementation procedurale de la grammaire
    public Parser(Lexer lex) {
        this.lex = lex;
        this.lex.makeTokenTable();
        this.table = new SymbolTable();
        this.tiv = new TargetInstructionsVector();
    }

    private int index = -1;

    //valeur actuelle du lexem
    private String currentLexem(){
        if (index < this.lex.getTokenTable().size())
            return this.lex.getTokenTable().get(index).getLexem();
        else return "";
    }

    //valeur actuelle du token
    private String currentToken() {
        if (index < this.lex.getTokenTable().size())
            return this.lex.getTokenTable().get(index).getValue();
        else return "";
    }

    //avancer dans le lexeme
    private void advance() {
        this.index++;
        System.out.println(currentToken());
    }

    //reculer dans le lexeme
    private void goback() {
        this.index--;
    }

    //public parse
    public boolean parse() {
        advance();

        if (currentToken().equals("declare_keyword"))
            if (!declare_body())
                return false;

        if (currentToken().equals("begin_keyword")) {
            tiv.addTInstruction(new TargetInstruction("START",null));
            return body();
        }
        return false;
    }


    //corps de la partie declaration
    private boolean declare_body() {
        advance();

        if ( currentToken().equals("identifier") ){
            init();
            currentId = currentLexem();
            if ( table.hasEntry(currentId) )
                return false;
            if (declaration())
                return declare_body();
            else return false;
        }
        return true;
    }

    //implementation procedurale d'une declaration
    private boolean declaration() {
        advance();
        //declarer une constante (optionnel)
        if (currentToken().equals("const_keyword")) {
            advance();
            currentIsConst = true;
        }
        if (currentToken().contains("type")){
            currentType = currentToken().split("_")[0];
            advance();
        }else return false;

        if (currentToken().equals("assignment_op")) {
            advance();
            if( currentToken().contains("value") ) {
                if ( !currentType.equals(currentToken().split("_")[0]) )
                    return false;
                else
                    currentValue = currentLexem();
                advance();
            }
            else return false;
        }

        if (!currentToken().equals("separator"))
            return false;
        else {
            switch (currentType){
                case"number":
                    table.addEntry(new Entry<Double>(currentId,"number",Double.parseDouble(currentValue),currentIsConst));
                    break;
                case"string":
                    table.addEntry(new Entry<String>(currentId,"string",currentValue,currentIsConst));
                    break;
                case"boolean":
                    if (currentValue.equalsIgnoreCase("false"))
                        table.addEntry(new Entry<Boolean>(currentId,"boolean",false,currentIsConst));
                    else
                        table.addEntry(new Entry<Boolean>(currentId,"boolean",true,currentIsConst));
                    break;

            }

            return true;
        }

    }

    //corps du programme
    private boolean body() {
        advance();

        if ( currentToken().equals("end_keyword") ){
            advance();
            if ( currentToken().equals("separator"))
                advance();
            else return false;
            return !currentToken().equals("");
        }

        if (statements()){
            if ( currentToken().equals("end_keyword") ){
                advance();
                if ( currentToken().equals("separator")){
                    tiv.addTInstruction(new TargetInstruction("END",null));
                    advance();
                }
                else return false;
                return !currentToken().equals("");
            }else return false;
        }
        else return false;
    }

    //ensemble d'instructions
    private boolean statements(){

        while (statement())
            advance();
        if (currentToken().equals("end_keyword"))
            return true;
        else return false;

    }

    //une instruction (affectation ou condition if)
    private boolean statement() {
        if (currentToken().equals("identifier")){
            if (!table.hasEntry(currentLexem()))
                return false;
            if (table.getEntry(currentLexem()).isConst())
                return false;
            currentType = table.getEntry(currentLexem()).getType();
            return assignment();
        }
        if ( currentToken().equals("if_keyword") )
            return condition();
        return false;
    }

    //affectation
    private boolean assignment() {
        int adr = table.getEntry(currentLexem()).getAddress();
        advance();
        if (currentToken().equals("assignment_op"))
            advance();
        else return false;

        if (arithmetic_expression()) {
            advance();
            if (!currentToken().equals("separator"))
                return false;
            else
                tiv.addTInstruction(new TargetInstruction("STORE",String.format("Ox%x",adr)));
                return currentType.equals(currentExpressionType);
        }else return false;

    }

    //expression arithmetique
    private boolean arithmetic_expression() {
        String tmp1,tmp2;
        String opt;
        if ( factor() ){
            tmp1 = currentExpressionType;
            advance();
            if ( arithmetic_operator() ){
                if ( currentExpressionType.equals("string") && !currentToken().contains("plus") )
                    return false;
                opt = TargetInstruction.translateOperation(currentLexem());
                advance();
                if (factor()){
                    tmp2 = currentExpressionType;
                    tiv.addTInstruction(new TargetInstruction(opt,null));
                    return tmp1.equals(tmp2) && !tmp2.equals("boolean");
                }
            }
        }else return false;
        return true;
    }

    //facteur (identifiant ou valeur)
    private boolean factor() {
        if (currentToken().equals("identifier") ){
            if (!table.hasEntry(currentLexem()))
                return false;
            Entry id = table.getEntry(currentLexem());
            currentExpressionType = id.getType();
            if (id.isConst()) {
                System.out.println("test " + id.getValue());
                tiv.addTInstruction(new TargetInstruction("LOADC", id.getValue() + ""));
            }
                else
                tiv.addTInstruction(new TargetInstruction("LOAD",String.format("0x%x",id.getAddress())));
            return true;
        }
        if (currentToken().contains("value")){
            currentExpressionType = currentToken().split("_")[0];
            tiv.addTInstruction(new TargetInstruction("LOADC",currentLexem() + ""));
            return true;
        }
        return false;
    }

    //operateur arithmetique
    private boolean arithmetic_operator() {
        return currentToken().contains("ar_op");
    }

    //condition
    private boolean condition() {
        int e1,e2;
        advance();

        if ( logical_expression() )
            advance();
        else return false;
        e1 = tiv.size();
        tiv.addTInstruction(new TargetInstruction("JZERO",e1+""));

        if ( currentToken().equals("then_keyword") )
            advance();
        else return false;

        if ( statement() )
            advance();
        else return false;

        if ( statements() )
            advance();
        else {
            e2 = tiv.size();
            tiv.addTInstruction(new TargetInstruction("JUMP",e2 + ""));
            if ( currentToken().equals("else_keyword") ){
                advance();
                tiv.getTInstruction(e1).setOperand(tiv.size()+"");
                e1 = -1;
                if ( statement() )
                    advance();
                else return false;

                if (statements())
                    advance();
            }
            if ( currentToken().equals("endif_keyword") )
                advance();
            else return false;
            if ( e1 > 0 )
                tiv.getTInstruction(e1).setOperand(tiv.size()+"");
            tiv.getTInstruction(e2).setOperand(tiv.size()+"");
            return currentToken().equals("separator");
        }
        return true;
    }

    //expression logique
    private boolean logical_expression() {
        String tmp1,tmp2,opt;
        if ( factor() ){
            tmp1 = currentExpressionType;
            advance();
            if ( comparison_operator() ){
                opt = TargetInstruction.translateOperation(currentLexem());
                advance();
                if (factor()){
                    tmp2 = currentExpressionType;
                    tiv.addTInstruction(new TargetInstruction(opt,null));
                    return tmp1.equals(tmp2) && !tmp1.equals("boolean");
                }else return false;
            } else{
                init();
                goback();
                return tmp1.equals("boolean");
            }
        }else return false;
    }

    //operateur de comparaison
    private boolean comparison_operator() {
        return currentToken().contains("comp_op");
    }


    public SymbolTable getTable() {
        return table;
    }

    public TargetInstructionsVector getTiv() {
        return tiv;
    }
}