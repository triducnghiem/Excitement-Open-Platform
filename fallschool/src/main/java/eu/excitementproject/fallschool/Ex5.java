package eu.excitementproject.fallschool;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 
 * @author fs19 triduc-nghiem
 * 
 *
 */
public class Ex5 {
	//Path to corpora
	private final static String ResourcesPath = "/fallschool/home/fs19/resources/semeval2013-Task7-2and3way";
	
	private final static String train=ResourcesPath+"/training/";
	private final static String test=ResourcesPath+"/test/";
			
	public static void main(String [] args){
		//Configuration
		BasicConfigurator.resetConfiguration();
    	BasicConfigurator.configure();
    	Logger.getRootLogger().setLevel(Level.WARN); 
	}
	
	/**
	 * reading the xmlformat
	 */
	private void xmlReader(String data){
		
	}
	
	
	
	
}
