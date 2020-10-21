package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

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
    	
    	long readTime = System.nanoTime();
    	Scanner scan = null;
    	File fin = null;
    	
    	try {
    		//The entire name of the cnf file (e.g. "testcase.cnf") to run 
    		//is passed to the main() method via a command line argument, 
    		//thus you read the filename from the args array. 
    		//eg. java SATSolverTest testcase.cnf
    		System.out.print("Reading file");
    		
    		//assign Scanner input to File f
    		fin = new File("D:\\javawork\\ps4-starting\\src\\sat\\largeUnsat.cnf");
    		scan = new Scanner(fin);
    		
    		//remove comments "c"
    		boolean commentCheck = true;
			while (commentCheck != false) {
    			String[] commentRemove = scan.nextLine().split(" ");
    			if (commentRemove[0]!="c"||commentRemove[0]!="C") {
    				commentCheck = false;
    			}
    		}
			
			String[] format = scan.nextLine().split(" ");
			String testF = format[format.length-1];
			System.out.println(testF);
			int numberOfClauses = Integer.parseInt(testF);
			Formula f = new Formula();
			String line = null;
			while (f.getSize() != numberOfClauses) {
				line = scan.nextLine();
				if (line.length() > 0) {
                    String[] tempLine = line.split(" ");
                    Clause c = new Clause();
                    for (String i:tempLine) {
                    	
                        if(Integer.parseInt(i) == 0) {
                            break;
                        }
                        
                        //literal instance
                        Literal literal = PosLiteral.make(Integer.toString(Math.abs(Integer.parseInt(i))));
                        
                        //add negated Integer to clause if string is negative
                        if((Integer.parseInt(i)) < 0){
                            c = c.add(literal.getNegation());
                        }
                        
                        //add positive Integer to clause if string is positive
                        else if ((Integer.parseInt(i))>0){
                            c = c.add(literal);
                        }
                        
                        if (c == null) {
                            c = new Clause();
                        }
                        
                    }
                    //add clause to formula
                    f = f.addClause(c);
                }
			}
			
			String fileName = "D:\\javawork\\ps4-starting\\src\\sat\\BoolAssignment.txt";
            PrintWriter write = new PrintWriter(fileName, "UTF-8");
            long endReadTime = System.nanoTime();
            long tReadTime = endReadTime - readTime;
            System.out.println("Reading Time: " + tReadTime/1000000000.0 + "s");
            System.out.println("SAT Solver starts");
            long started = System.nanoTime();
            Environment env = SATSolver.solve(f);
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Solving Time: " + timeTaken/1000000.0 + "ms");
            System.out.println("Total Time: " + (timeTaken+tReadTime)/1000000000.0 + "s");
            if (env == null) {
            	System.out.println("Formula Unsatisfiable");
            } else {
            	System.out.println("Formula Satisfiable");
            	String bindings = env.toString();
            	System.out.println(bindings);
            	bindings = bindings.substring(bindings.indexOf("[")+1, bindings.indexOf("]"));
            	String[] bindingNew = bindings.split(", ");
            	
            	for (String binding : bindingNew) {
            		String[] bind = binding.split("->");
            		write.println(bind[0] + ":" + bind[1]);
            	}
            }
            write.close();
			
    	} finally {
    		
    		if (scan != null) {
                scan.close();
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