package sat;

/*
import static org.junit.Assert.*;
import org.junit.Test;
*/

import java.io.*;

import sat.env.*;
import sat.formula.*;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();


    
	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static void main(String args[]) throws IOException {
    	
    	// The name of the file to open.
        String fileName = "D:\\javawork\\ps4-starting\\src\\sat\\test_2020.cnf";
        int counter = 0;

        // This will reference one line at a time
        String line = null;
        FileReader fileReader = null;
        // FileReader reads text files in the default encoding.
        fileReader = 
            new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = 
            new BufferedReader(fileReader);

        try {
            
            boolean commentCheck = false;
            Formula currFormula = new Formula();
            Clause currClause = new Clause();
            
            while((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                counter++;
                // Clauses start after line with p cnf ...
                if(line.startsWith("p cnf")) {
                	commentCheck = true;
                	line = bufferedReader.readLine();
                }
                if(commentCheck) {
                	String[] literalArr = line.trim().split(" ");
                	//System.out.println(line);
                	
                	for (String literalStr:literalArr) {
                		if (currClause == null) {
                        	currClause = new Clause();
                        }
                		if (Integer.parseInt(literalStr) == 0) {
                            break;
                        }
                	// literal instance
                        Literal literal = PosLiteral.make(Integer.toString(Math.abs(Integer.parseInt(literalStr))));
                        // negative literal
                        if((Integer.parseInt(literalStr)) < 0) {
                            currClause = currClause.add(literal.getNegation());
                        }
                        // positive literal
                        else if ((Integer.parseInt(literalStr)) > 0) {
                        	currClause = currClause.add(literal);
                        } 
                	}
                }
            }
            
            
            System.out.println("Starting SAT solver...");
            long started = System.nanoTime();
            Environment result = null;
            if (commentCheck) {
                result = SATSolver.solve(currFormula);
            }

            long time = System.nanoTime();
            long timeTaken= time - started;
            System.out.println("Time: " + timeTaken/1000000.0 + "ms");
            
            // Write to BoolAssignment.txt
            
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        finally
        {
            if(fileReader != null){
               // Always close files.
               bufferedReader.close();            
            }
        }
    	
    }
	
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}
