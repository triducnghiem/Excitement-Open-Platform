package eu.excitementproject.fallschool;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.uima.jcas.JCas;

import eu.excitementproject.eop.common.DecisionLabel;
import eu.excitementproject.eop.common.EDABasic;
import eu.excitementproject.eop.common.EDAException;
import eu.excitementproject.eop.common.TEDecision;
import eu.excitementproject.eop.common.configuration.CommonConfig;
import eu.excitementproject.eop.common.exception.ComponentException;
import eu.excitementproject.eop.common.exception.ConfigurationException;
import eu.excitementproject.eop.core.ImplCommonConfig;
import eu.excitementproject.eop.core.MaxEntClassificationEDA;
import eu.excitementproject.eop.lap.LAPAccess;
import eu.excitementproject.eop.lap.LAPException;
//import eu.excitementproject.eop.lap.dkpro.MaltParserEN;
import eu.excitementproject.eop.lap.dkpro.TreeTaggerEN;

/**
 * This example code shows how you can initiate and use EDA. 
 * 
 * It deals two modes (process mode, training mode) of EDAs, and covers how you can 
 * train an EDA and use the trained model with that EDA. 
 * 
 * @author Gil 
 *
 */

public class Ex2 {

	public static void main(String[] args) {

    	// init logs
    	BasicConfigurator.resetConfiguration();
    	BasicConfigurator.configure();
    	Logger.getRootLogger().setLevel(Level.INFO);  

    	// read and each of the code sections, one by one.
    	
        //ex2_1(); // initialize() and process() of EDA 
		//ex2_2(); // start_training() of EDA 
	}
	
	/**
	 * This method shows initializing an EDA with one existing (already trained) model. 
	 */
	public static void ex2_1() {
    	// All EDAs are implementing EDABasic interface. Here, we will visit 
    	// "process mode" of an EDA. 
    	
		///////
		/// Step #1: initialize an EDA
		///////
    	// First we need an instance of an EDA. We will use a TIE instance.  
		// (MaxEntClassificationEDA) 
		@SuppressWarnings("rawtypes") // why this? will be explained later.
		EDABasic eda = null; 
		try {
			eda = new MaxEntClassificationEDA();  
			// To start "process mode" we need to initialize the EDA. 
			// We have two TIE configuration in /src/main/resource/config/ 
			// let's use "lexical one": MaxEntClassificationEDA_Base+WN+VO_EN.xml 
			File configFile = new File("src/main/resources/config/MaxEntClassificationEDA_Base+WN+VO_EN.xml");        

			CommonConfig config = new ImplCommonConfig(configFile);
			eda.initialize(config); 
		} 
		catch (EDAException|ConfigurationException|ComponentException e)
		{
			System.out.println("Failed to init the EDA: "+ e.getMessage()); 
			System.exit(1); 
		}		
		
		// TODO Task ex2_1_a: Take a look at the configuration file. 
		// ( XML file that holds the above configuration. src/main/resources/MaxEntClassificationEDA_Base+WN+VO_EN.xml) 
		// 
		// It is TIE configuration with lexical features (without parse trees), 
		// and the configuration uses WordNet and VerbOcean. 
		// First note the "model path", since we load already existing model. 
		// /fallshool/ code already holds that model in the given path. 
		// 
		// Note that there are two file paths (for WordNet and VerbOcean), and 
		// One model path. When you install EOP on your own computer, you probably 
		// need to update those paths. 
		// (Note that /eop-resources-1.0.2/ can be downloaded from project webpage, 
		// as described in Ex0 exercise sheet) 
		
		// Full list of configurations and model files can be found in 
		// /Excitement-Open-Platform/core/src/main/resources/configuration-files
		// and 
		// /Excitement-Open-Platform/core/src/main/resources/model 
		
		///////
		/// Step #2: call process(), and check the result. 
		///////
		
		// Okay. now the EDA is ready. Let's prepare one T-H pair and use it. 
    	// simple Text and Hypothesis. 
		// Note that (as written in the configuration file), current configuration 
		// needs TreeTaggerEN Annotations 
        String text = "The sale was made to pay Yukos' US$ 27.5 billion tax bill, Yuganskneftegaz was originally sold for US$ 9.4 billion to a little known company Baikalfinansgroup which was later bought by the Russian state-owned oil company Rosneft."; 
        String hypothesis = "Baikalfinansgroup was sold to Rosneft."; 
        
        JCas thPair = null; 
        try {
        	LAPAccess lap = new TreeTaggerEN(); 
        	thPair = lap.generateSingleTHPairCAS(text,  hypothesis); // ask it to process this T-H. 
        } catch (LAPException e)
        {
        	System.err.print("LAP annotation failed:" + e.getMessage()); 
        	System.exit(1); 
        }
        
        // Now the pair is ready in the CAS. call process() method to get 
        // Entailment decision. 
        
        // Entailment decisions are represented with "TEDecision" 
        // class. 
        TEDecision decision = null;  
        try {
        	decision = eda.process(thPair); 
        } catch (EDAException|ComponentException e)
        {
        	System.err.print("EDA reported exception" + e.getMessage()); 
        	System.exit(1); 
        }
        
        // And let's look at the result. 
        DecisionLabel r = decision.getDecision(); 
        System.out.println("The result is: " + r.toString()); 
        
        
        // and you can call process() multiple times as much as you like. 
        // ... 
        // once all is done, we can call this. 
        eda.shutdown(); 
        
        // TODO Task ex2_1_b 
        // Try to ask some more T-H pairs from RTE3 English data. 
        // You can find the RTE3 data in /fallschool/src/main/resources/RTE3-dataset
        // Just randomly pick a few pairs from the data, type them in the code of this 
        // file (as String t = "xxx", String h = "yyy), and annotate them with the lap,  
        // get JCas data, and call EDA process() to get the result. 
         
	}
	
	/**
	 * This method shows how to train a EDA, with a given configuration & training data.  
	 *
	 * The example training process takes a fair amount of time. 
	 * (around 10-15 minutes). 
	 * It is recommended to read / follow the code first to its end, 
	 * before actually running it. 
	 */
	public static void ex2_2()
	{
		// The other mode of the EDA is training mode. Let's check how this is done 
		// with one training example. 
		
		// Training also requires the configuration file. 
		// We will load a configuration file first. 		
		CommonConfig config = null; 
		try {
			// the configuration uses only WordNet (no VerbOcean) 
			File configFile = new File("src/main/resources/config/MaxEntClassificationEDA_Base+WN_EN.xml");        
			config = new ImplCommonConfig(configFile);
		}
		catch (ConfigurationException e)
		{
			System.out.println("Failed to read configuration file: "+ e.getMessage()); 
			System.exit(1); 
		}
		
		// TODO task ex2_2_a
		// Check the above configuration XML file by opening and reading it.  
		// Check the following values under the section 
		// "eu.excitementproject.eop.core.MaxEntClassificationEDA" (last section). 
		// 		modelFile: the new model will be generated here.  
		//      trainDir: the configuration expects here pre-processed RTE training data as a set of XMI Files. 
		// Where the new model will be generated? Where the configuration 
		// expects to read pre-processed training data? 
		// Also check the first section: 
		// What LAP it requires? (top section, "activatedLAP") 
		
			// WARNING: each EDA has different procedures for Training. 
			// So other EDAs like BIUTEE might expect different parameters 
			// for training. One needs to consult EDA-specific documentations 
			// to check this. 
		
		// Before calling start_training() we have to provide 
		// pre-processed training data. This EDA will train itself with 
		// the provided data that is pointed by trainDir. 
					
		try {
			LAPAccess ttLap =  new TreeTaggerEN(); 
			// Prepare input file, and output directory. 
			File f = new File("./src/main/resources/RTE3-dataset/English_dev.xml"); 
			File outputDir = new File("./target/training/"); // as written in configuration!  
			if (!outputDir.exists())
			{
				outputDir.mkdirs();  
			}
			ttLap.processRawInputFormat(f, outputDir); 
		} catch (LAPException e)
		{
			System.out.println("Training data annotation failed: " + e.getMessage()); 			
			System.exit(1); 
		}
			
		// Okay, now RTE3 data are all tagged and stored in the 
		// trainDir. Let's ask EDA to train itself. 
		try {
			@SuppressWarnings("rawtypes") 
			EDABasic eda = null; 
			eda = new MaxEntClassificationEDA();  
			eda.startTraining(config); // This *MAY* take a some time. 
	    } 
		catch (EDAException|ConfigurationException|ComponentException e)
		{
			System.out.println("Failed to do the training: "+ e.getMessage()); 
			System.exit(1); 
		}	

		System.out.print("Training completed."); 
		
		// TODO task ex2_2b 
		// Go to "modelFile" path and check that the newly trained model 
		// has been generated. 
		
		// TODO task ex2_2c 
		// modify ex2_1 to use this configuration and its newly trained model. 
		// Check it actually works. 
	
		// TODO [OPTIONAL] task ex2_2_c 
		// (You can skip optional tasks without affecting the final mini project) 
		// 
		// The best configuration known for current EOP-TIE EDA is 
		// "MaxEntClassificationEDA_Base+WN+VO+TP+TPPos+TS_EN.xml"
		// Try to train a model based on this configuration. 
		// You can find the configuration in Excitement-Open-Platform/config/ 
		// 
		// You have to edit the configuration to update file paths, 
		// output dir, etc. 
		// you also need to provide the proper LAP pre-processing. 
	}		
}
