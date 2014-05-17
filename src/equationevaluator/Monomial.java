
package equationevaluator;
/**
 * A recursive class which represents an equation tree, contains either an operator, variable or value.
 * If  Monomial is a leaf it is either a value or variable, if the Monomial is a node, then is it a operator with left and right Monomials
 * @author Michael Scovell
 */
public class Monomial {
    Operation op;
    
    Monomial left = null;
    Monomial right = null;
    double val;
    char var;
    boolean sign;
    
    boolean isLeaf;
    boolean isVar = false;
    /**
     * Initializes this Monomial as a node
     * @param l The left side of the tree
     * @param r The right side of the tree
     * @param o The operator this monomial represents
     */
    public Monomial(Monomial l, Monomial r, Operation o){
        op = o;
        left = l;
        right = r;
        isLeaf = false;
    }
    
    /**
     *  
     */
    public Monomial(){
        
    }
    /**
     * Makes Monomial a leaf node with the value <d>
     * @param d The value this Monomial will represent
     */
    public Monomial(double d){
        isLeaf = true;
        isVar = false;
        val = d;
    }
    
    /**
     * Makes this Monomial a leaf node with a variable <var>
     * @param var The variable this Monomial will represent
     */
    public Monomial(char var, boolean sign) {
        isLeaf = true;
        isVar = true;
        this.sign = sign;
        this.var = var;
    }
    
   
    /**
     * Evaluates the monomial below with each instance of a variable replaced with a corresponding value
     * @param vars The variables that will be replaced by the corresponding <vals>
     * @param vals The values that will replaces the varaibes in <vars>
     * @return the value if every child of this object had it's <vars> replaced with the corresponding <vals>
     */
    public double PeekAt(String vars, double ... vals){
        boolean debug = true;
        if(isLeaf){
            if(isVar){
                int index = vars.indexOf(var);
                if(index == -1){
                    val = 0;
                }else{
                    int s = sign ? 1 : -1;
                    val = s*vals[index];
                }
            }
        }else{
            val = op.Eval(left.PeekAt(vars,vals), right.PeekAt(vars,vals));
        }
        if(debug){
            String res = PrintStrRepresentation();
            System.out.printf("%-50s Evaluated to: %f\n", res, val);
        }
        return val;
    }
    
    
    /**
     * Prints <depth> followed by the value represented by this object and then 
     * runs this function to all children starting with <left>
     * @param depth How deeply this tree has been recursed
     */
    public void PrintTreeRepresentation(int depth){
        String print = Integer.toString(depth) + ". ";
        if(isLeaf){
            if(isVar){
                print += var;
            }else{
                print += Double.toString(val);
            }
        }else{
            print += op.toString();
        }
        System.out.println(print);
        if(!isLeaf){
            left.PrintTreeRepresentation(depth+1);
            right.PrintTreeRepresentation(depth+1);
        }
    }
    
    /**
     * Prints this and all it's childrens values iwth brackets on either side of the form (left this right)
     */
    public void PrintRepresentation(){
        if(isLeaf){
            if(isVar){
                if(sign)
                    System.out.print(var);
                else
                    System.out.print("-" + var);
            }else{
                System.out.print(val);
            }
        }else{
            System.out.print("(");
            if(left != null)
            left.PrintRepresentation();
            if(op != null)
            System.out.print(op.toString());
            if(right != null)
            right.PrintRepresentation();
            
            System.out.print(")");
        }
        
    }
    /**
     * Same as PrintTreeRepresentation but returns  a string instead of printing
     * @return The string of form ( left this right )
     */
    public String PrintStrRepresentation(){
        String res = "";
        if(isLeaf){
            if(isVar){
                res +=(var);
            }else{
                res +=(val);
            }
        }else{
            res +=("(");
            if(left != null)
            res +=left.PrintStrRepresentation();
            if(op != null)
            res +=(op.toString());
            if(right != null)
            res+=right.PrintStrRepresentation();
            
            res +=(")");
        }
        return res;
    }
}
