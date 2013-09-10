package eu.excitementproject.fallschool;

import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.uima.jcas.JCas;

import eu.excitementproject.eop.common.component.distance.DistanceCalculation;
import eu.excitementproject.eop.common.component.distance.DistanceComponentException;
import eu.excitementproject.eop.common.component.distance.DistanceValue;
import eu.excitementproject.eop.common.component.lexicalknowledge.LexicalResourceException;
import eu.excitementproject.eop.common.component.scoring.ScoringComponent;
import eu.excitementproject.eop.common.component.scoring.ScoringComponentException;
import eu.excitementproject.eop.common.exception.ConfigurationException;
import eu.excitementproject.eop.core.component.distance.FixedWeightLemmaEditDistance;
import eu.excitementproject.eop.core.component.scoring.BagOfLexesScoringEN;
import eu.excitementproject.eop.core.component.scoring.TreeSkeletonScoring;
import eu.excitementproject.eop.lap.LAPAccess;
import eu.excitementproject.eop.lap.LAPException;
import eu.excitementproject.eop.lap.dkpro.MaltParserEN;
import eu.excitementproject.eop.lap.dkpro.TreeTaggerEN;

/**
 * This code introduces Scoring components, and distance components. 
 * 
 * @author Gil
 */

public class Ex4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
    	// init logs
    	BasicConfigurator.resetConfiguration();
    	BasicConfigurator.configure();
    	Logger.getRootLogger().setLevel(Level.INFO);  

    	
    	// As usual, read and run each of the code sections separately.   
    	
        //ex4_1(); // scoring component example.  
		//ex4_2(); // distance component example.

	}
	
	
	/**
	 * This code fragment briefly introduces Scoring component interface
	 */
	public static void ex4_1()
	{
		try 
		{
			// Two examples of Scoring Component. 
			
			// TreeSkeleton Scoring component implements feature-extraction 
			// outlined in the paper "Rui Wang and Günter Neumann. 2007. Recognizing Textual Entailment Using a Subsequence Kernel Method "
			ScoringComponent c1 = new TreeSkeletonScoring(); 
			
			// BagOfLexesScoring is a Scoring component compares the given T-H on lemma level, 
			// but this comparison is done with expansion by lexical resource of VerbOcean and WordNet. 
			ScoringComponent c2 = new BagOfLexesScoringEN(true, new String[]{"HYPERNYM", "SYNONYM"}, true, false, false, "/fallschool/resources/eop-resources-1.0.2/ontologies/EnglishWordNet-dict/", true, new String[]{"STRONGER_THAN", "SIMILAR"}, true, "/fallschool/resources/eop-resources-1.0.2/VerbOcean/verbocean.unrefined.2004-05-20.txt" ); 
			
			// Let's prepare a pair by calling LAP. 
			// first component requires parse tree: so let's call MaltParser pipeline. 
				        
	        JCas THPair = null; 
	        String text = "The sale was made to pay Yukos' US$ 27.5 billion tax bill, Yuganskneftegaz was originally sold for US$ 9.4 billion to a little known company Baikalfinansgroup which was later bought by the Russian state-owned oil company Rosneft."; 
	        String hypothesis = "Baikalfinansgroup was sold to Rosneft."; 
	        try {
	        	LAPAccess lap = new MaltParserEN(); 
	        	THPair = lap.generateSingleTHPairCAS(text,  hypothesis); // ask it to process this T-H. 
	        } catch (LAPException e)
	        {
	        	System.err.print("LAP annotation failed:" + e.getMessage()); 
	        	System.exit(1); 
	        }

			// and we can now ask scoring component to report their observation (features)  
	        // .calculateScores(JCas) is the main method of Scoring Component. 
	        // it returns a vector of Double (so the features are ordered). 
	        Vector<Double> feature_vector1 = c1.calculateScores(THPair); 
	        Vector<Double> feature_vector2 = c2.calculateScores(THPair); 
	       
	        // and let's print them -- although they are not very meaningful but ... 
	        System.out.println("Features extracted by the first component: "); 
	        for(Double d : feature_vector1)
	        	System.out.print(d + "\t"); 
	        System.out.println(); 

	        System.out.println("Features extracted by the second component: "); 
	        for(Double d : feature_vector2)
	        	System.out.print(d + "\t"); 
	        System.out.println(); 
	        
		} catch (ScoringComponentException|LexicalResourceException|ConfigurationException e)
		{
			System.out.println("Failed to run two Scoring components: " + e.getMessage()); 
			System.exit(1); 
		}
		
		// TODO task 4_1
		// briefly check current list of scoring components in the EOP, by visiting 
		// Type Hierarchy. To open "Type Hierarchy", put the cursor on the 
		// target class name (here, class name ScoringComponent), and press F4 button. 
		// 
		// Check that DistanceCalulation (which we will shortly) is a subtype 
		// interface of this type. 
	}
	
	/**
	 * This code fragment shows Distance Component interface. 
	 */
	static public void ex4_2()
	{
		
		// And now, let's briefly check the method of distance component. 
		// initializing one DistanceCalculation component... 
		DistanceCalculation d1 = new FixedWeightLemmaEditDistance();
		
		// Before calling, we need to prepare one T-H pair ... 		
        JCas THPair = null; 
        String text = "The sale was made to pay Yukos' US$ 27.5 billion tax bill, Yuganskneftegaz was originally sold for US$ 9.4 billion to a little known company Baikalfinansgroup which was later bought by the Russian state-owned oil company Rosneft."; 
        String hypothesis = "Baikalfinansgroup was sold to Rosneft.";
        try {
        	LAPAccess lap = new TreeTaggerEN(); 
        	THPair = lap.generateSingleTHPairCAS(text,  hypothesis); 
        } catch (LAPException e)
        {
        	System.err.print("LAP annotation failed:" + e.getMessage()); 
        	System.exit(1); 
        }

        // And let's call distance component. 
        try {
        	// .calculation(JCas) is the main method. it returns a specialized
        	// class DistanceValue, which naturally holds standardized "distance". 
        	DistanceValue v = d1.calculation(THPair); 
        	
        	// lets check the returned values. 
        	System.out.println("Distance component returned final (normalized) distance as: " + v.getDistance()); 
        	System.out.println("Its un-normalized value was: "+ v.getUnnormalizedValue()); 
        	
        	// note that it is still a type of Scroing Component, so it does support the
        	// following method, too
        	System.out.println("Distance component's scoring vector has the following values: ");
        	for(Double d: d1.calculateScores(THPair))
        	{
        		System.out.print("\t" + d ); 
        	}
        	System.out.println(); 
        	// as you will see in the output, this vector simply holds [ normalized dist, unnormalized dist ]. 
        } catch (DistanceComponentException|ScoringComponentException e)
        {
        	System.out.println("calling Distance Component failed: " + e.getMessage()); 
        }
        
        // TODO task 4_2 
        // test a few other sentences on the above code. check how the distance is normalized 
        // by giving it roughly equal sentences, or almost completely different sentences. 
	}
}
