package eu.excitementproject.fallschool;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eu.excitementproject.eop.lap.LAPAccess;
import eu.excitementproject.eop.lap.dkpro.OpenNLPTaggerEN;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	// Initializing log4j log 
		BasicConfigurator.resetConfiguration(); 
		BasicConfigurator.configure(); 
		Logger.getRootLogger().setLevel(Level.WARN);  // set INFO or lower to see log outputs 

        System.out.println( "Hello Excitement!" );
        
        try {
        	LAPAccess lap = new OpenNLPTaggerEN(); 
        	lap.generateSingleTHPairCAS("This is a text", "And, this is a hypothesis"); 
        }
        catch (Exception e)
        {	
        	System.err.print(e.getMessage()); 
        	System.exit(1); 
        }
        
        // part #1 
        
        // According to OpenNLP, the text has, the hypothesis has, 
        
        // According to TreeTagger, the text has, the hypothesis has, tokens and lemmas. 
        
        // According to pre-trained model of EDITS, fixed weight .. 
        
        // According to pre-trained model of TIE, 
        
    }
}
