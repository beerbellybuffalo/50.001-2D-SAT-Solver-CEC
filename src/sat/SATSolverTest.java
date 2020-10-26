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
        String fileName = args[0];

        // This will reference one line at a time
        String line = null;
        FileReader fileReader = null;
        // FileReader reads text files in the default encoding.
        fileReader = 
            new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = 
            new BufferedReader(fileReader);
        
        FileWriter fileWriter = null;
        // FileWriter writes to text files in the default encoding.
        fileWriter = 
            new FileWriter("BoolAssignment.txt");

        // Always wrap FileWriter in BufferedWriter.
        BufferedWriter bufferedWriter = 
            new BufferedWriter(fileWriter);

        try {
            
            boolean commentCheck = false;
            Formula currFormula = new Formula();
            Clause currClause = new Clause();
            
            while((line = bufferedReader.readLine()) != null && !line.isEmpty()) {

                // Clauses start after line with p cnf ...
                if(line.startsWith("p cnf")) {
                	commentCheck = true;
                	line = bufferedReader.readLine();
                }
                if(commentCheck) {
                	String[] literalArr = line.trim().split("\\s+");
                	//System.out.println(line);
                	
                	for (String literalStr:literalArr) {
                		// new clause if 0
                		if(Integer.parseInt(literalStr) == 0) {
                            currFormula = currFormula.addClause(currClause);
                            currClause = new Clause();
                        }
                        // negative literal
                        if((Integer.parseInt(literalStr)) < 0) {
                            currClause = currClause.add(NegLiteral.make(literalStr.substring(1)));
                        }
                        // positive literal
                        else if((Integer.parseInt(literalStr)) > 0) {
                        	currClause = currClause.add(PosLiteral.make(literalStr));
                        } 
                	}
                }
            }
            
            System.out.println("Starting SAT solver...");
            long started = System.nanoTime();
            Environment result = null;
            // Only parse if correct format (dimacs)
            if(commentCheck == true) {
            	result = SATSolver.solve(currFormula);
            }
            
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Time: " + timeTaken/1000000.0 + "ms");
            
            if(result == null) {
                System.out.println("Unsatisfiable");
            }
            else {
                System.out.println("Satisfiable");
                
                // Write to BoolAssignment.txt since Satisfiable
                String output = result.toString();
                System.out.println(output);
                
                // Formatting output
                String str1 = output.replace("Environment:[", "");
                String str2 = str1.replace("->", ":");
                String str3 = str2.replace("]", "");
                String str4 = str3.replace(", ", "\n");
                bufferedWriter.write(str4);
                
            }
            
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
            if(fileReader != null) {
               // Always close files.
               bufferedReader.close();            
            }
            
            if(fileWriter != null) {
                bufferedWriter.close();
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
