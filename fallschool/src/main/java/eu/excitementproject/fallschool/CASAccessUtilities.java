package eu.excitementproject.fallschool;

import static org.uimafit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLSerializer;
import org.uimafit.component.xwriter.CASDumpWriter;
import org.uimafit.factory.AggregateBuilder;
import org.xml.sax.SAXException;

import eu.excitementproject.eop.lap.LAPException;

/**
 * This class holds some static methods that helps you to access JCas 
 * data structure that holds LAP output (Linguistic annotations) 
 * 
 * @author Tae-Gil Noh 
 *
 */
public final class CASAccessUtilities {
	
	/**
	 * This method creates an empty JCas that can hold any type that is 
	 * recognized in Excitement Open Platform. 
	 * 
	 * @return JCas an empty, newly created JCas object. 
	 */
	public static JCas createNewJCas() throws LAPException 
	{
		JCas a = null;
		AnalysisEngine typeAE = null;
		try {
		InputStream s = CASAccessUtilities.class.getResourceAsStream("/desc/DummyAE.xml"); // This AE does nothing, but holding all types.
		XMLInputSource in = new XMLInputSource(s, null);
		ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);	
		typeAE = UIMAFramework.produceAnalysisEngine(specifier);
		}
		catch (InvalidXMLException e)
		{
		throw new LAPException("AE descriptor is not a valid XML", e);	
		}
		catch (ResourceInitializationException e)
		{
		throw new LAPException("Unable to initialize the AE", e);
		}	

		try {
		a = typeAE.newJCas();
		}
		catch (ResourceInitializationException e)
		{
		throw new LAPException("Unable to create new JCas", e);
		}
		return a; 
	}

	/**
	 * This utility method dumps the content of CAS for human readers. 
	 * It dumps the content of the given aJCas into a new text file with fileName. 
	 * If a file exists, it will be overwritten. 
	 * 
	 * @param aJCas CAS to be dumped into a file 
	 * @param fileName the new text file that will holds the content for human readers. 
	 * @throws LAPException 
	 */
	public static void dumpJCasToTextFile(JCas aJCas, String fileName) throws LAPException
	{
		try {
			AnalysisEngineDescription cc = createPrimitiveDescription(
					CASDumpWriter.class,
					CASDumpWriter.PARAM_OUTPUT_FILE, fileName);
			AggregateBuilder builder = new AggregateBuilder();
			builder.add(cc); 
			AnalysisEngine dumper = builder.createAggregate(); 
			dumper.process(aJCas); 
		}
		catch (ResourceInitializationException e)
		{
			throw new LAPException("Unable to initialize CASDumpWriter AE"); 
		}
		catch (AnalysisEngineProcessException e)
		{
			throw new LAPException("CASDumpWriter returned an Exception."); 
		}
	}
	/**
	 * This static method serializes the given JCAS into an XMI file.
	 *
	 * @param JCas aJCas: the JCas to be serialized
	 * @param File f: file path, where XMI file will be written
	 */
	static public void serializeJCasToXmi(JCas aJCas, File f) throws LAPException
	{
		// Serializing formula.
		try {
			FileOutputStream out;
			out = new FileOutputStream(f);
			XmiCasSerializer ser = new XmiCasSerializer(aJCas.getTypeSystem());
			XMLSerializer xmlSer = new XMLSerializer(out, false);
			ser.serialize(aJCas.getCas(), xmlSer.getContentHandler());
			out.close();
		}
		catch (FileNotFoundException e)
		{
			throw new LAPException("Unable to open the file for the serialization", e);
		}
		catch(IOException e)
		{
			throw new LAPException("IOException while closing the serialization file ", e);
		}
		catch(SAXException e)
		{
			throw new LAPException("Failed in generating XML for the serialization", e);
		}
	}
	
	/**
	 * This static method loads a serialized XMI file and fill up the JCAS.
	 * Note that this method will first clear (by calling .reset()) the given CAS, and will fill it with given File, assuming the File is an XMI-zed CAS. If not, it will raise an exception.
	 *
	 * @param aJCas The CAS container, that will be cleaned and filled by the data on the XMI file. 
	 * @param f the XMI file that holds previously serialized CAS data. 
	 */
	static public void deserializeFromXmi(JCas aJCas, File f) throws LAPException
	{
		aJCas.reset();
		// if (!f.canRead())
		// {
		// throw new LAPException("Unable to open the file for deserialization" + f.toString());
		// }

		try {
			FileInputStream inputStream = new FileInputStream(f);
			XmiCasDeserializer.deserialize(inputStream, aJCas.getCas());
			inputStream.close();
		}
		catch(FileNotFoundException e)
		{
			throw new LAPException("Unable to open file for deserialization", e);
		}
		catch(IOException e)
		{
			throw new LAPException("IOException happenes while accessing XMI file",e);
		}
		catch(SAXException e)
		{
			throw new LAPException("XML parsing failed while reading XMI file", e);
		}
	}	
	
}
