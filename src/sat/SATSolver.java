package sat;

import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // TODO: implement this.
        return solve(formula.getClauses(), new Environment()); //creates new immutable list
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // TODO: implement this.
        if (clauses.size() == 0) {
            return env; //backtrack
        }
        
        int smallestSize = clauses.first().size(); //this is the clause size of the smallest clause, counted by the number of literals. we intialise this to the size of the first clause.
        Clause smallestClause = clauses.first();
        for (Clause c:clauses) {
            if (c.isEmpty()) {
                return null;
            }
            else {
                if (c.size()<smallestSize) {
                    smallestSize = c.size();
                    smallestClause = c;
                }
            }
        }

        if (smallestClause.isUnit()) {
            Variable V = smallestClause.chooseLiteral().getVariable();
            String s = V.getName();  //this is the variable number as a String
            try {
              // convert String to int
              int i = Integer.parseInt(s.trim());
              if (i<0) {
                  env.putFalse(V);
                  
              }
              else if (i>0) {
                  env.putTrue(V);
                  
              }        
              //// print out the value after the conversion
              //System.out.println("int i = " + i);
            }
            catch (NumberFormatException nfe) {
              System.out.println("NumberFormatException: " + nfe.getMessage());
            }            
        }

        throw new RuntimeException("not yet implemented.");
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        // TODO: implement this.
        for (Clause c:clauses) {
            for (Literal k:c) {
                if (k.equals(l)) ;
            }
        }
        throw new RuntimeException("not yet implemented.");
    }

}
