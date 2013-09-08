package eu.excitementproject.fallschool;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.uima.jcas.JCas;

import eu.excitementproject.eop.common.EDABasic;
import eu.excitementproject.eop.common.TEDecision;
import eu.excitementproject.eop.common.configuration.CommonConfig;
import eu.excitementproject.eop.core.ClassificationTEDecision;
import eu.excitementproject.eop.core.ImplCommonConfig;
import eu.excitementproject.eop.core.MaxEntClassificationEDA;
import eu.excitementproject.eop.lap.LAPAccess;
import eu.excitementproject.eop.lap.LAPException;
import eu.excitementproject.eop.lap.dkpro.MaltParserEN;

/**
 * A simple, minimal code that runs one LAP & EDA to check all environment is Okay. 
 * 
 * @author Gil 
 *
 */
public class Ex0 
{
    public static void main( String[] args )
    {

    	// init logs
    	BasicConfigurator.resetConfiguration();
    	BasicConfigurator.configure();
    	Logger.getRootLogger().setLevel(Level.WARN);  

        System.out.println( "Hello Excitement!" );
        
        // Here's T-H of this welcome code. 
        String text = "The students had 15 hours of lectures and practice sessions on the topic of Textual Entailment."; 
        String hypothesis = "The students must have learned quite a lot about Textual Entailment."; 
        // Minimal "running" example for Excitement open platform EDAs (Entailment Decision Algorithms) 
        // Basically, it is two step: 
        // 1) Do pre-processing (linguistic analysis) needed for the EDA, by calling an LAP 
        // 2) Initialize an EDA with configuration & pre-trained model. (consider a model & config is a pair)
        // 3) Ask EDA to decide Entailment. 
        
        
        // 1) Do pre-processing, via an LAP. 
        // We will learn more about LAPs (Linguistic Analysis Pipeline), in Exercise #1 
        // Here, it runs one pipeline based on TreeTagger and MaltParser.  
        System.out.println( "Running LAP for the T-H pair." );
        JCas annotated_THpair = null; 
        try {
        	LAPAccess lap = new MaltParserEN(); // make a new MaltParser based LAP 
        	annotated_THpair = lap.generateSingleTHPairCAS(text,  hypothesis); // ask it to process this T-H. 
        } catch (LAPException e)
        {
        	System.err.print(e.getMessage()); 
        	System.exit(1); 
        }

        // 2) Initialize an EDA with a configuration (& corresponding model)  
        // (Model path is given in the configuration file.) 
        System.out.println("Initializing the EDA.");
        EDABasic<ClassificationTEDecision> eda = null; 
        try {
        	// TIE (MaxEntClassificationEDA): a simple configuration with no knowledge resource. 
        	// extracts features from lemma, tokens and parse tree and use them as features. 
        	File configFile = new File("src/main/resources/MaxEntClassificationEDA_Base+TP_EN.xml");        
        	CommonConfig config = new ImplCommonConfig(configFile);
        	eda = new MaxEntClassificationEDA(); 
        	eda.initialize(config); 
        } catch (Exception e)
        {
        	System.err.print(e.getMessage()); 
        	System.exit(1); 	
        }
        
        // 3) Now, one input data is ready, and the EDA is also ready. 
        // Call the EDA.  
        System.out.println("Calling the EDA for decision.");
        TEDecision decision = null; // the generic type that holds Entailment decision result 
        try {
        	decision = eda.process(annotated_THpair); 
        } catch (Exception e)
        {
        	System.err.print(e.getMessage()); 
        	System.exit(1); 	
        }        
        System.out.println("Run complete: EDA returned decision: " + decision.getDecision().toString());         
    }
}
