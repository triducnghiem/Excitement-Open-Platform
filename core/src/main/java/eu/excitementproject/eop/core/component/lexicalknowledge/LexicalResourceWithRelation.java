package eu.excitementproject.eop.core.component.lexicalknowledge;

import java.util.List;

import eu.excitementproject.eop.core.representation.parsetree.PartOfSpeech;

/**
 * 
 * 
 * @author Gil 
 *
 * @param <I>
 * @param <R>
 */
public interface LexicalResourceWithRelation<I extends RuleInfo, R extends RelationSpecifier> extends LexicalResource<I> {
	
	/**
	 * Returns a list of lexical rules whose left side matches the given lemma, POS and relation. 
	 * An empty list means that no rules were matched. null POS is permitted, just as LexicalResource.
	 * @param lemma
	 * @param pos
	 * @param originalRelation
	 * @return
	 * @throws LexicalResourceException
	 */
	List<LexicalRule<? extends I>> getRulesForLeft(String lemma, PartOfSpeech pos, R relation) throws LexicalResourceException;
	
	/**
	 * Returns a list of lexical rules where right side matches the given lemma, POS and relation. 
	 * An empty list means that no rules were matched. null POS is permitted, just as LexicalResource.
	 * @param lemma
	 * @param pos
	 * @param originalRelation
	 * @return
	 * @throws LexicalResourceException
	 */
	List<LexicalRule<? extends I>> getRulesForRight(String lemma, PartOfSpeech pos, R relation) throws LexicalResourceException;

	/**
	 *  This method returns a list of lexical rules whose left and right sides 
	 *  match the given conditions. 
	 * @param leftLemma
	 * @param leftPos
	 * @param rightLemma
	 * @param rightPos
	 * @param originalRelation
	 * @return
	 * @throws LexicalResourceException
	 */
	List<LexicalRule<? extends I>> getRules(String leftLemma, PartOfSpeech leftPos, String rightLemma, PartOfSpeech rightPos, R relation) throws LexicalResourceException;	

}
