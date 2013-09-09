package eu.excitementproject.fallschool;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import eu.excitementproject.eop.common.component.lexicalknowledge.LexicalResource;
import eu.excitementproject.eop.common.component.lexicalknowledge.LexicalResourceException;
import eu.excitementproject.eop.common.component.lexicalknowledge.LexicalRule;
import eu.excitementproject.eop.common.representation.partofspeech.ByCanonicalPartOfSpeech;
import eu.excitementproject.eop.common.representation.partofspeech.UnsupportedPosTagStringException;
import eu.excitementproject.eop.common.utilities.Utils;
import eu.excitementproject.eop.core.component.lexicalknowledge.verb_ocean.RelationType;
import eu.excitementproject.eop.core.component.lexicalknowledge.verb_ocean.VerbOceanLexicalResource;
import eu.excitementproject.eop.core.component.lexicalknowledge.verb_ocean.VerbOceanRuleInfo;
import eu.excitementproject.eop.core.component.lexicalknowledge.wordnet.WordnetLexicalResource;
import eu.excitementproject.eop.core.component.lexicalknowledge.wordnet.WordnetRuleInfo;
import eu.excitementproject.eop.core.utilities.dictionary.wordnet.WordNetRelation;

/**
 * This code introduces the LexicalResource interface, and LexicalRule. 
 * 
 * @author Gil
 */

public class Ex3 {

	public static void main(String[] args) {
		//ex3_1();
		//ex3_2(); 
	}
	
	/**
	 *  This code fragment introduces 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" }) 	// for the sake of clarity, for now, we ignore parameterization of LexicalResource. 
	public static void ex3_1()
	{
		// Let's initialize WordNet, and VerbOcean,  
		// and they query something. 
		
		
		// First, initialization. 
		LexicalResource lexRes1 = null; 
		@SuppressWarnings("unused")
		LexicalResource lexRes2 = null; 
		try {
			// Note that each lexical resource has different constructor arguments, naturally. 
			// You have to check the Javadoc of each to know them. For now, do not much 
			// are about the parameters. 
			// NOTE: when you work on your own computer, you have to change the "filePath". 
			
			// WordNet (initialize with file path, and configure it to use Hypernym and Synonym relation as LHS -> RHS) 
			//Set<WordNetRelation> wnRelSet = Utils.arrayToCollection(new WordNetRelation[]{WordNetRelation.HYPERNYM, WordNetRelation.SYNONYM, WordNetRelation.PART_HOLONYM }, new LinkedHashSet<WordNetRelation>() ); 	
			Set<WordNetRelation> wnRelSet = Utils.arrayToCollection(new WordNetRelation[]{WordNetRelation.HYPERNYM, WordNetRelation.SYNONYM}, new LinkedHashSet<WordNetRelation>() ); 	
			lexRes1 = new WordnetLexicalResource(new File("/fallschool/resources/eop-resources-1.0.2/ontologies/EnglishWordNet-dict/"), wnRelSet);
			// VerbOcean (initialize with file path, and configure it to assign relations to be used as LHS -> RHS.) 
			Set<RelationType> allowedRelationTypes = Utils.arrayToCollection(new RelationType[]{RelationType.STRONGER_THAN, RelationType.HAPPENS_BEFORE, RelationType.CAN_RESULT_IN}, new LinkedHashSet<RelationType>());
			lexRes2 = new VerbOceanLexicalResource(1, new File("/fallschool/resources/eop-resources-1.0.2/VerbOcean/verbocean.unrefined.2004-05-20.txt"), allowedRelationTypes);					
		} 
		catch (LexicalResourceException e)
		{
			System.out.println("Failed to initialize the two lexical resources. Check file paths, etc. : " + e.getMessage()); 
			System.exit(1); 
		}
		
		// Let's test LexicalResource interface. 
		try {
			// interface #1: getRulesForLeft()
			// This query asks for terms that "dog (as noun)" entails. 
			List<LexicalRule> list1 = lexRes1.getRulesForLeft("dog", new ByCanonicalPartOfSpeech("N")); // canonical Part of Speech list is defined in class CanonicalPosTag
			
			// Let's iterate what they are. 
			System.out.println("dog entails: "); 
			for(LexicalRule r : list1)
			{
				System.out.println("\t" + r.getRLemma()); // NOTE: Here we print *R* Lemma! (RHS side of rule). We already know what LHS is.  
			}

			// interface #2: getRulesForRight() 
			// This query asks for terms that entails "dog (N)". 
			List<LexicalRule> list2 = lexRes1.getRulesForRight("dog", new ByCanonicalPartOfSpeech("N")); 

			System.out.println("\nthe terms that entails dog: "); 
			for(LexicalRule r : list2)
			{
				System.out.println("\t" + r.getLLemma()) ; // NOTE: Here we print *L* Lemma! (LHS side of rule). We already know what is RHS. 
			}			
			
			// interface #3: getRules() 
			// This query asks (poodle,N) -> (dog,N). 
			List<LexicalRule> list3 = lexRes1.getRules("poodle", new ByCanonicalPartOfSpeech("N"), "dog", new ByCanonicalPartOfSpeech("N")); 
			List<LexicalRule> list4 = lexRes1.getRules("West highland white terrier", new ByCanonicalPartOfSpeech("N"), "dog", new ByCanonicalPartOfSpeech("N")); 
			
			// Okay. WordNet do have a rule that says "poodle -> dog".  
			if(list3.size() > 0)
			{
				System.out.println("WordNet LexicalResource returned a rule for \"poodle -> dog\""); 
			}
			
			// But it does not know "West highland white terrier". So here, we have no rule. 
			// Note that, if no such rule exists, it still returns a list (empty list), never null. 
			if(list4.size() == 0)
			{
				System.out.println("WordNet does not have \"West highland white terrier --> dog\". Unbelievable. It knows Welsh corgi, poodle, Mexican hairless, spitz, Brussels griffon... but no Westy! The most adorable dogs in this world!"); 
			}	
			
		} 
		catch (LexicalResourceException|UnsupportedPosTagStringException e)
		{
			System.out.println("getRules query got exception: "+e.getMessage()); 
			System.exit(1); 
		}
		
		// TODO task 3_1a   
		// As you saw in the above code, getRulesForLeft work as 
		// getRulesForLeft(String lemma, PartOfSpeech pos)
		// If you give pos as "null": it means, "match any POS". 
		// try to query getRulesForLeft("dog", null), and compare the result with 
		// previous "list1". what other POS the term "dog" also has?  
		
		// TODO task 3_1b 
		// - ask VerbOcean (in the above code, LexicalResource lexRes2) to get any lexical rule that 
		// covers "dog (as verb) -> X". Does it has any? 
		// - ask VerbOcean and WordNet to provides rules for (hunt/V -> )
		//   compare them. 
		// Note that you can specify verb by (new ByCanonicalPartOfSpeech("V")) 		
	}
	
	/**
	 *  This code fragment shows a bit more about LexicalRule 
	 */
	public static void ex3_2()
	{
		
		// First, initialization. Again, let's open WordNet and VerbOcean. 
		LexicalResource<WordnetRuleInfo> wordNetRes = null; 
		LexicalResource<VerbOceanRuleInfo> verbOceanRes = null; 

		try {
			Set<WordNetRelation> wnRelSet = Utils.arrayToCollection(new WordNetRelation[]{WordNetRelation.HYPERNYM, WordNetRelation.SYNONYM}, new LinkedHashSet<WordNetRelation>() ); 	
			wordNetRes = new WordnetLexicalResource(new File("/fallschool/resources/eop-resources-1.0.2/ontologies/EnglishWordNet-dict/"), wnRelSet);
			Set<RelationType> allowedRelationTypes = Utils.arrayToCollection(new RelationType[]{RelationType.STRONGER_THAN, RelationType.HAPPENS_BEFORE, RelationType.CAN_RESULT_IN}, new LinkedHashSet<RelationType>());
			verbOceanRes = new VerbOceanLexicalResource(1, new File("/fallschool/resources/eop-resources-1.0.2/VerbOcean/verbocean.unrefined.2004-05-20.txt"), allowedRelationTypes);					
		} catch (LexicalResourceException e)
		{
			System.out.println("Lexical Resource initialization failed: " + e.getMessage()); 
		}
		
		// Note that the above two variable of "LexicalResource" are now 
		// now explicitly parameterized with "<Info>" type. 
		// 	  LexicalResource<WordNetRuleInfo> ...   
		//    LexicalResource<VerbOceanRuleInfo> ... 
		// 
		// What are those two "Info" types? To see this, please check the figure in the 
		// following section. (open URL in browser) 
		// http://hltfbk.github.io/Excitement-Open-Platform/specification/spec-1.1.3.html#sec-4.5.1
		
		// Note that Both rules in the Figure 8. has "info" field, that points to 
		// two different information objects. On left, it has WordNetInfo object, on right 
		// it has WikipediaInfo object. 
		
		// This is how EOP LexicalRule permits "resource-specific" additional information 
		// to be included in the LexicalRule. In the previous example, we used rawtype-LexicalResource 
		// and ignored any additional info. But on this example, Let's briefly check them. 
		
		try {
			String term1 = "dog"; 
			String term2 = "kick"; 
			List<LexicalRule<? extends WordnetRuleInfo>> list1 = wordNetRes.getRulesForLeft(term1, null);  
			List<LexicalRule<? extends VerbOceanRuleInfo>> list2 = verbOceanRes.getRulesForLeft(term2, null); 
			
			System.out.println(term1 + " entails ===> "); 
			for (LexicalRule<? extends WordnetRuleInfo> r : list1)
			{
				System.out.println("\t" + r.getRLemma());
				WordnetRuleInfo wi = r.getInfo(); 
				System.out.println("\t\t relation: " + r.getRelation()); 
				System.out.println("\t\t " + term1 + " Sense ID " + wi.getLeftSenseNo() + " ---entails--> " + r.getRLemma() + " Sense ID " + wi.getRightSenseNo()); 
				
				// We can also get/print Synset itself, ( but it's too long to print out, )
				//List<SensedWord> lSynset= wi.getLeftSense().getAllSensedWords(); 
				//List<SensedWord> rSynset = wi.getRightSense().getAllSensedWords(); 
			}
			
			System.out.println(term2 + " entails ===> "); 			
			for (LexicalRule<? extends VerbOceanRuleInfo> r : list2)
			{
				System.out.println("\t" + r.getRLemma());
				VerbOceanRuleInfo vi = r.getInfo();
				// VerbOceanRuleInfo has score, and relation. 
				System.out.println("\t\t relation: " + vi.getRelationType().toString()); 
				System.out.println("\t\t score: " + vi.getScore()); 
			}
		
		} catch (LexicalResourceException e)
		{
			System.out.println("getRules returned exception: "+e.getMessage()); 
		}
		
		// TODO task 3_2a 
		// Briefly check project "core"; see the packages 
		// eu.excitementproject.eop.core.component.lexicalknowledge.*
		// it would give you a brief idea about currently existing knowledge resources in EOP. 
		
		// TODO [OPTIONAL] task 3_2b 
		// briefly think about how you can make a lexical resource with 
		// a distributional semantics data. Say, if you have a (rather reliable) list of 
		// term-similarity of 10k terms of a domain ... (say, 10k by 10k similarity table...) 
		
	}
}
