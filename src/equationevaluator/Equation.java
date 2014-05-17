package equationevaluator;

import java.util.ArrayList;

/** Equation is a wrapper containing monomial and a list of variables contained in the equation.
 *
 * @author Michael Scovell
 * @version 6.0z Build 9000 Jan 3, 1970.
 */
public class Equation {
    private Monomial root;
    private String vars;
    
    /**
     *
     * @param ROOT The first operator in a bst representing the equation
     * @param VARS A Stringw hich contains exactly one occurance of every variable in <ROOT>
     */
    public Equation(Monomial ROOT, String VARS){
        root = ROOT;
        vars = VARS;
    }
    
    /**
     * returns the value of the equation if each variable in <var> is replaced with the 
     * corresponding value in <val>
     * 
     * @param vars A list of variables to replace, all not mentioned variables 
     * are not in vars are defaulted to 0 
     * @param val A list of values to replace variables with, must be the same length as <vars>
     * @return
     */
    public double peekAt(String vars, double ... val){
        return root.PeekAt(vars, val);
    }
    
    /**
     * Prints A human readable version of the equation with brackets separating each operation
     */
    public void PrintRepresentation(){
        root.PrintRepresentation();
    }
    /**
     * Prints the depth of the monomial  followed by the monomial
     * for all monomials in the equation.
     */
    public void PrintTreeRepresentation(){
        root.PrintTreeRepresentation(1);
    }
}
