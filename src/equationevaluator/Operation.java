
package equationevaluator;
/**
 * Represents the list of valid function and operators allowed
 * @author Michael17
 */
public enum Operation {
    
   
    EXP ("^",3),
    LOG("log",3),
    LN("ln",3),
    FAC("fac",3),
    ABS("abs",3),
    SIN("sin",3),
    COS("cos",3),
    TAN("tan",3),
    SIH("sih", 3),
    COH("coh",3),
    TAH("tah", 3),
    MULT ("*",2),
    DIV ("/",2),
    DIF("%",2),
    ADD ("+",1),
    SUB("-",1),
    NONE (" ",Integer.MIN_VALUE);
    
    private String value;
    private int Priority;
    Operation(String c, int p){
        value = c;
        Priority = p;
    }
    /**
     * Creates an Operator
     * @param op the operation to set this operator to
     */
    public void setValue(Operation op){
        value = op.GetSymbol();
    }
    /**
     * returns the priority of this operator
     * @return Priority is the order of operations goes from highest to lowest
     */
    public int GetPriority(){
        return Priority;
    }
    /**
     * Compares this operator to a string
     * @param c the string to be compared
     * @return True if the string matches the string representation of this operation
     */
    public boolean isEqual(String c){
        c = c.toLowerCase();
        return c.equals(value);
    }
    /**
     * Given a char returns the corresponding Operation
     * @param c the character to be searched for
     * @return The oepration that corresponds to <c>
     */
    static public Operation GetValue(char c){
         switch(c){
            case '^':
                return EXP;
            case '*':
                return MULT;
            case '%':
                return DIF;
            case '/':
               return DIV;
            case '+':
                return ADD;
            case '-':
                return ADD;
            case ' ':
            default:
                //error
                return NONE;
    }}
    /**
     * Given a string returns the corresponding Operation
     * @param c string to be compared
     * @return the operation that corresponds to this string
     */
    static public Operation GetType(String c){
        c = c.toLowerCase();
        switch(c){
            case "fac":
                return FAC;
            case "abs":
                return ABS;
            case "sin":
                return SIN;
            case "cos":
                return COS;
            case "tan":
                return TAN;
            case "sih":
                return SIH;
            case "coh":
                return COH;
            case "tah":
                return TAH;
            case "log":
                return LOG;
            case "ln":
                return LN;
            case "^":
                return EXP;
            case "mod":
            case "%":
                return DIF;
            case "*":
                return MULT;
            case "/":
               return DIV;
            case "+":
                return ADD;
            case "-":
                return SUB;
            default:
                //error
                return NONE;
        }
    }
    
    /**
     * A function is a operation that takes arguements in the form Function(param)
     *  as opposed to a operator of the form (param Operator param)
     * @return True if this is a function and false otherwise
     */
    public boolean isFunction(){
        return  this==SIN||this==COS||this==TAN||this==FAC||this==ABS||this==LOG
                ||this==LN||this==COH||this==SIH||this==TAH;
    }
    /**
     *
     * @return the symbol corresponding to this Operation
     */
    public String GetSymbol(){
        return value;
    }
    /**
     * Evaluates such that (val1 operator val2), if this is a function then val1 will be multiplied by func(param)
     * @param val1 the left of the operator 
     * @param val2 the right of the operator
     * @return the result of the operation of val1 if an error occurs
     */
    public double Eval(double val1, double val2){
        switch(this){
            case EXP:
                return Math.pow(val1, val2);
            case FAC:
                double result = 1;
                for(int i = 1; i <= val2; i++){
                    result = result*i;
                }
                return result*val1;
            case SIN:
                return val1 * Math.sin(val2);
            case COS:
                return val1 * Math.cos(val2);
            case TAN:
                return val1 * Math.tan(val2);
            case SIH:
                return val1 * Math.sinh(val2);
            case COH:
                return val1 * Math.cosh(val2);
            case TAH:
                return val1 * Math.tanh(val2);
            case DIF:
                return val1%val2;
            case MULT:
                return val1 * val2;
            case DIV:
               return val1 / val2;
            case ADD:
                return val1 + val2;
            case SUB:
                return val1-val2;
            case ABS:
                return val1 * Math.abs(val2);
            case LOG:
                return val1*Math.log10(val2);
            case LN:
                return val1*Math.log(val2);
            case NONE:
            default:
                //error
                return val1;   
        }
    }
}

