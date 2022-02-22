public class TargetInstruction {

    //etiquette ,operation et operand
    private String operation;
    private String operand;

    //constructeur
    public TargetInstruction(String operation, String operand) {
        this.operation = operation;
        if (operand == null )
            this.operand = "";
        else this.operand = operand;
    }

    //tradOP
    public static String translateOperation(String op){
        switch (op) {
            case "+": return "ADD";
            case "-": return "SUB";
            case "*": return "MUL";
            case "/": return "DIV";
            case "%": return "MOD";
            case "!=": return "DIF";
            case "<=": return "LTE";
            case ">=": return "GTE";
            case "<": return "LT";
            case ">": return "GT";
            case "=": return "EQ";
            default:return "";
        }
    }

    //getters et setters
    public String getOperation() {
        return operation;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    //

    @Override
    public String toString() {
            return getOperation() + " " + getOperand();
    }
}
