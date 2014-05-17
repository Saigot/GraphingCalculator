/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package equationevaluator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class EquationEvaluator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // expr = "(((+1-+3)*(-3/+4)-abs(-5x)))";
        //expr = "(1+1)";
        //expr = "(log(x))";
        //expr = "2^2";
        //expr = "(((+1-+3)*(-3/+4)-abs(x)))";
        //expr = "(2) + (17*2-30) * (5)+2 - (8/2)*4";
        //expr = "(( ((2)) + 4))*((5))";
        //expr = "I + Like + sinpie^0";
        //expr = "1.5x^2";
        String expr;
        //expr = "2 + 3";
        //System.out.println(expr);
        StringParser str = new StringParser();
        //Equation eq;
        char q = 'n';
        while (q != 'y') {
            String vars = "";
            String vals = "";
            try {
                System.out.println("Equation:");
                expr = br.readLine();
                System.out.println("Varaibles:");
                vars = br.readLine();
                System.out.println("Values (comma separated):");
                vals = br.readLine();
            } catch (IOException ex) {
                expr = "Error";
            }
            String[] splitted = vals.split(",");
            double[] values = new double[splitted.length];
            for (int i = 0; i <= splitted.length - 1; i++) {
                values[i] = Double.parseDouble(splitted[i]);
            }
            Equation eq = str.ParseString(expr);
            System.out.println(eq.peekAt(vars, values));
            eq.PrintRepresentation();
            //eq.PrintTreeRepresentation();
            
            try {
                System.out.println("\nQuit? y/n");
                q = (char)br.read();
                br.readLine();
            } catch (IOException ex){
                
            }
        }

    }
}
