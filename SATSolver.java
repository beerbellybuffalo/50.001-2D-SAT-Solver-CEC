package sat;

import java.util.Iterator;

import immutable.EmptyImList;
import immutable.ImList;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.PosLiteral;
import sun.tools.java.Environment;
import sun.tools.jstat.Literal;

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
        return solve(formula.getClauses(), new Environment());
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
        if (clauses.size() == 0) {
            return env;
        }

        int smallestSize = clauses.first().size(); //this is the clause size of the smallest clause, counted by the number of literals. we initialise this to the size of the first clause.
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
        Literal l = smallestClause.chooseLiteral();
        Environment alltrue;
        if (smallestClause.isUnit()) {
            if (l instanceof PosLiteral){ //@Brandon need code here to determine if a literal is positive or negative
                alltrue = solve(substitute(clauses, l), env.putTrue(l.getVariable()));
            } else{
                alltrue = solve(substitute(clauses, l), env.putFalse(l.getVariable()));
            }
        }else{
            env = env.putTrue(l.getVariable());
            ImList<Clause> temp = substitute(clauses, l);

            Environment potential = solve(temp, env);
            if (potential == null){
                env = env.putFalse(l.getVariable());
                return solve(substitute(clauses, l.getNegation()), env);
            } else{
                return potential;
            }
        }
        return alltrue;
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
        ImList<Clause> result = new EmptyImList<Clause>();
        Iterator<Clause> iter = clauses.iterator();
        while (iter.hasNext()){
            Clause c = iter.next(); //check for l
            if (c.contains(l) || c.contains(l.getNegation())){
                c = c.reduce(l); // l is found, reduce it
                if (c == null) {
                    break;
                }
                result.add(c); //add it to the new ImList
            }
        }
        return result; //return list with l set to true
    }

}
