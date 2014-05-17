package equationevaluator;

import com.sun.xml.internal.ws.api.pipe.NextAction;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class that converts a human readable string into an equation type
 * @author michael
 */
public class StringParser {
    
    
    /**
     *
     * @param expr the string to convert to an equation
     * @return
     */
    public Equation ParseString(String expr){
        boolean debug = true;
        expr = ReplaceConstants(expr);
        ArrayList<MathObject> bare = getBareRep(expr);
        //while(bare.get(0).type == 3){
        //    bare.remove(0);
        //    bare.remove(bare.size()-1);
        //}
        Equation eq = BareToEq(bare);
        if(debug){
            System.out.println("Start Bare Print:");
            PrintBareBones(bare);
            
            System.out.println("\nStart Equation Print");
            eq.PrintRepresentation();
        }
        return eq;
    }
    
    private String ReplaceConstants(String expr){
        expr = expr.replaceAll("e", Double.toString(Math.E));
        expr = expr.replaceAll("(?i)pi", Double.toString(Math.PI));
        return expr;
    }
    
    private Equation BareToEq(ArrayList<MathObject> b){
        String vars = "";
        for(MathObject m : b){
            if(m.type == MathObject.VAR_TYPE && vars.indexOf(m.var) != -1){
                vars += m.var;
            }
        }
        int start = 0;
        int end = b.size()-1;
        return new Equation(BareToEq_aux(b,start,end),vars);
    }
    private MathObject GetNthTerm(ArrayList<MathObject> b, int start, int end, int n){
        for(int i = start; i <= end; i++){
            if(b.get(i).type == MathObject.VAL_TYPE || b.get(i).type == MathObject.VAR_TYPE){
                n--;
                if(n <= 0){
                    return b.get(i);
                }
            }else if(b.get(i).type != MathObject.BRAC_TYPE){
                return null;
            }
        }
        return null;
    }
    private Monomial BareToEq_aux(ArrayList<MathObject> b, int start, int end){
        boolean debug = true;
        if(debug){
            System.out.printf("Printing from %d to %d\n",start,end);
            PrintBareBones(b,start,end);
            System.out.println();
        }
        boolean error = false;
        Monomial root = new Monomial();
        int pri = 0;
        int lvl = 0;
        int piv = GetPivot(b,start,end,pri,lvl);
        MathObject pivobj;
        if(piv != -1){
            pivobj = b.get(piv);
        }else{
            pivobj = new MathObject();
        }
        if(piv != -1){
            root.op = pivobj.Operator;
            Monomial m = BareToEq_aux(b, start, piv-1);
            root.left = m;
            m = BareToEq_aux(b, piv+1, end);
            root.right = m;
            
        }else{
            MathObject m = GetNthTerm(b, start, end, 1);
            if (m.type == MathObject.VAL_TYPE) {
                return new Monomial(m.val);
            } else if (m.type == MathObject.VAR_TYPE) {
                return new Monomial(m.var,m.sign);
            }
            
        }
        if(root == null){
            root = new Monomial(1);
        }
        
        return root;
    }
    private int GetPivot(ArrayList<MathObject> b, int start, int priority, int level){
        return GetPivot(b, start, b.size()-1,priority, level);
    }
    private int GetPivot(ArrayList<MathObject> b, int start, int end, int priority, int level){
        boolean debug = true;
        if(debug){
                System.out.println("Starting Pivot search");}
        if(b.size() <= 1){
            if(debug){
                System.out.println("Starting Pivot search");
                System.out.printf("Size is %d\n",b.size());
            }
            return -1;
        }
        int index = -1;
        int pri = -1;
        int lvl = -1;
        for(int i = start; i <= end; i++){
            MathObject obj = b.get(i);
            switch(obj.type){
                case MathObject.VAR_TYPE:
                    break;
                case MathObject.VAL_TYPE:
                    break;
                case MathObject.OP_TYPE:
                    System.out.println("I Found " + obj.Operator.toString());
                    int thispri = obj.Operator.GetPriority();
                    if((thispri >= priority && level <= 0)
                            && (index == -1 || level > lvl || (thispri <= pri && level == lvl))){
                        System.out.printf("I Selected %s at %d %d\n",
                                obj.Operator.toString(),i,level);
                        index = i;
                        pri = thispri;
                        lvl = level;
                    }
                    break;
                case MathObject.BRAC_TYPE:
                    if(isOpenBracket(obj.bracket)){
                        level--;
                        //System.out.println("Level Down, is now " + level);
                    }else{
                        level++;
                        //System.out.println("Level Up, is now " + level);
                    }
                    break;
            }
        }
        System.out.println("Returning the Pivot:" + index);
        return index;
    }
    private boolean isIgnored(char c){
        return c == ' ';
    }
    private boolean isBracket(char c){
        return c == '(' || c == ')' || c == '[' || c == ']'
                || c == '{' || c == '}';
    }
    private boolean BracketsClose(char o, char c){
        switch(o){
            case '(':
                return c == ')';
            case '[':
                return c == ']';
            case '{':
                return c == '}';
            default:
                return false;
        }
    }
    private boolean isOpenBracket(char b){
        return b == '(' || b == '[' || b == '{';
    }
    private ArrayList<MathObject> getBareRep(String expr){     
        boolean error = false;//false;
        boolean debug = true;
        ArrayList<MathObject> raw = new ArrayList<>();
        Stack<Character> lastbrac =  new Stack<>();
        lastbrac.add(new Character('\0'));
        for(int i = 0; i < expr.length(); i++){
            char c = expr.charAt(i);
            if (debug) System.out.print("\n\"" + c + "\"");
            //Evaluates Sign as opposed to addition/subtraction, numbers
            if(((raw.size() <= 0 || 
                    raw.get(raw.size()-1).type == MathObject.OP_TYPE
                    || (raw.get(raw.size()-1).type == MathObject.BRAC_TYPE &&
                    isOpenBracket(raw.get(raw.size()-1).bracket))) 
                    && (c == '+' || c == '-')) || (Character.isDigit(c))){
                if (debug) {
                    System.out.print(": ");
                    if ((raw.size() != 0 && (raw.get(raw.size() - 1).type == 2
                            || raw.get(raw.size() - 1).type == 3))) {
                        raw.get(raw.size() - 1).PrintRepresentation();
                        System.out.print(" is type " + raw.get(raw.size() - 1).type);
                    }
                    System.out.println(", Treated as Number");
                }
                MathObject num = new MathObject();
                MathObject op = new MathObject();
                int temp;
                i = scanNumber(i,expr,num);
                temp = scanFunctions(i, expr, op);
                if (num.type == -1 && op.type == -1 && i < expr.length() &&
                        Character.isAlphabetic(expr.charAt(i+1))){
                    i++;
                    boolean sign = c == '-' ? false : true;
                    num.setVar(expr.charAt(i),sign);
                }else if(num.type == -1){
                    num.setOperation(Operation.GetType(Character.toString(c)));
                } 
                raw.add(num);
            //Continues if ignorable character
            }else if(isIgnored(c)){
                if(debug) System.out.print(":Ignored");
                continue;
            //Variables & functions
            }else if(Character.isAlphabetic(c)){
                MathObject ADD = new MathObject();
                i = scanFunctions(i, expr, ADD);
                if(ADD.Operator == Operation.NONE){
                    if(debug) System.out.print(":Treated as Var");
                    int lasttype;
                    if(raw.size() > 0){
                        lasttype = raw.get(raw.size()-1).type;
                    }else{
                        lasttype = -1;
                    }
                    if(lasttype == MathObject.VAR_TYPE || lasttype == MathObject.VAL_TYPE)
                    {raw.add(new MathObject(Operation.MULT));}
                    ADD.setVar(c, true);
                }else{
                    if(ADD.Operator.isFunction() && 
                            (raw.size() <= 0 || raw.get(raw.size()-1).type == MathObject.OP_TYPE 
                            ||raw.get(raw.size()-1).type == MathObject.BRAC_TYPE )){
                    raw.add(new MathObject(1));
                    }
                    if(debug) System.out.print(":Treated as Function");
                }
                raw.add(ADD);
            //Brackets
            }else if(isBracket(c)){
                if(debug) System.out.print(":Treated as Brac");
                if(!isOpenBracket(c)){
                   char last = lastbrac.pop().charValue();
                   if(last == '\0'){
                       error = true;
                       break;
                   }else if(!BracketsClose(last, c)){
                       error = true;
                   }else{
                       raw.add(new MathObject(c,true,true));
                   }
                }else{
                    if(raw.size() > 0 && raw.get(raw.size()-1).type == MathObject.BRAC_TYPE
                            &&  !isOpenBracket(raw.get(raw.size()-1).bracket)){
                        raw.add(new MathObject(Operation.MULT));
                    }
                    raw.add(new MathObject(c,true,true));
                    lastbrac.add(new Character(c));
                }
             
            //Operations (single characters)
            }else{
                if(debug) System.out.print(":Treated as Operator");
                MathObject m = new MathObject(Operation.GetType(
                        Character.toString(c)));
                if(m.Operator.isFunction() && (raw.size() <= 0 
                        ||raw.get(raw.size()-1).type == MathObject.OP_TYPE
                        ||raw.get(raw.size()-1).type == MathObject.BRAC_TYPE )){
                    raw.add(new MathObject(1));
                }
                raw.add(m);
            }
        }
        if(error){
            System.out.printf("Error!\n");
        }
        
        
        return raw;
    }
    
    /**
     * Scans a number into <val>
     * @param i the index of the starting point within the <expr>
     * @param expr the expression being parsed
     * @param val will be mutated to contain the number that is scanned, if a number is found, no change otherwise
     * @return the index that is greater than <i> which is after the first digit of the scanned number
     */
    public int scanNumber(int i, String expr, MathObject val){
        char first = expr.charAt(i);
        String eval = ""; 
        if(first == '-' || first == '+'){
            i++;
        }
        for(;i<expr.length();i++){
            if(Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.'){
                eval+=expr.charAt(i);
            }else{
                if(eval.isEmpty()){
                    return i-1;
                }
                double d= Double.parseDouble(eval); 
                if(first == '-') d *= -1;
                val.setVal(d);
                return i-1;
            }
        }
        if (eval.isEmpty()) {
            return i - 1;
        }
        double d = Double.parseDouble(eval);
        if (first == '-') {
            d *= -1;
        }
        val.setVal(d);
        return i - 1;
    }
    /**
     * scans a function into <val>
     * @param i the index of the starting point within the <expr>
     * @param expr the expression being parsed
     * @param op will be mutated to contain the function that is scanned, if a function is found, no change otherwise
     * @return the index that is greater than <i> which is after the first digit of the scanned number
     */
    public int scanFunctions(int i, String expr, MathObject op){
        int org = i;
        String func = "";
        for(; i < expr.length(); i++){
            if(Character.isAlphabetic(expr.charAt(i))){
                func += expr.charAt(i);
                Operation temp = Operation.GetType(func);
                if(temp != Operation.NONE){
                    op.setOperation(temp);
                    return i;
                }
            }else{
               return org; 
            }
        }
        return org;
    }
    
    /**
     * Debug only, prints  a list of mathobjects
     * @param m the mathobject list to be printed
     */
    public void PrintBareBones(ArrayList<MathObject> m){
        for(MathObject math : m){
            math.PrintRepresentation();
        }
    }
    /**
     * Debug only, prints the Represntation of a list of math objects.
     * @param m the mathobject list to be printed
     * @param start the start index
     * @param end the end index
     */
    public void PrintBareBones(ArrayList<MathObject> m, int start, int end){
        if (start > end) return;
        for(int i = start; i <= end; i++){
            m.get(i).PrintRepresentation();
        }
    }
}
