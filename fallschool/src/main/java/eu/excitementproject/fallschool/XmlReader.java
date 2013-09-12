package eu.excitementproject.fallschool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlReader {
	
	public QuestionAnswers read(String xmlInputFile) throws ParserConfigurationException, SAXException, IOException{
		QuestionAnswers qa=new QuestionAnswers();
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        
        Document doc = docBuilder.parse (new File(xmlInputFile));
        
        //get Question Text
        NodeList listOfQuestions = doc.getElementsByTagName("questionText"); 
        qa.setQuestionText(listOfQuestions.item(0).getTextContent());
        
        //get Reference Answers
        Node refAnswer= doc.getElementsByTagName("referenceAnswers").item(0);
        NodeList refAnswersNodeList=refAnswer.getChildNodes();
        List<ReferenceAnswer> ras= new ArrayList<ReferenceAnswer>();
        
        for (int i=0; i< refAnswersNodeList.getLength();i++){
        	Element refA= (Element)refAnswersNodeList.item(i);
        	ReferenceAnswer ra=new ReferenceAnswer();
        	
        	ra.setCategory(refA.getAttribute("category"));
        	ra.setFileId(refA.getAttribute("fileID"));
        	ra.setId(refA.getAttribute("id"));
        	ra.setReferenceAnswer(refA.getTextContent());
        	
        	ras.add(ra); //add to the list
        }
        qa.setReferenceAnswers(ras);
        
        
        //get Student Answers
        Node studAnswer= doc.getElementsByTagName("studentAnswers").item(0);
        NodeList studAnswersNodeList=studAnswer.getChildNodes();
        List<StudentAnswer> sas= new ArrayList<StudentAnswer>();
        
        for (int i=0; i< studAnswersNodeList.getLength();i++){
        	Element studA= (Element)studAnswersNodeList.item(i);
        	StudentAnswer sa=new StudentAnswer();
        	
        	sa.setAccuracy(studA.getAttribute("accuracy"));
        	sa.setId(studA.getAttribute("id"));
        	sa.setAnswerMatch(studA.getAttribute(""));
        	sa.setCount(Integer.valueOf(studA.getAttribute("count")));
        	sa.setAnswerText(studA.getTextContent());
        	sas.add(sa); //add to the list
        }
        qa.setStudentAnswers(sas);
        return qa;        
	}
	
	public static void main(String []args) throws ParserConfigurationException, SAXException, IOException{
		XmlReader reader=new XmlReader();
		QuestionAnswers qa=reader.read("/fallschool/home/fs19/resources/semeval2013-Task7-2and3way/training/2way/beetle/SwitchesBulbsParallel-BURNED_BULB_PARALLEL_EXPLAIN_Q2.xml");
		System.out.println(qa.getQuestionText());
	}	
}
