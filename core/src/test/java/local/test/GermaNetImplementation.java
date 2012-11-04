package local.test;

import java.util.List;

import eu.excitementproject.eop.common.configuration.CommonConfig;
import eu.excitementproject.eop.common.exception.ComponentException;
import eu.excitementproject.eop.common.exception.ConfigurationException;
import eu.excitementproject.eop.core.component.lexicalknowledge.LexicalResourceException;
import eu.excitementproject.eop.core.component.lexicalknowledge.LexicalResourceWithRelation;
import eu.excitementproject.eop.core.component.lexicalknowledge.LexicalRule;
import eu.excitementproject.eop.core.representation.parsetree.PartOfSpeech;

public class GermaNetImplementation implements
		LexicalResourceWithRelation<GermaNetInfo, GermaNetRelation> {

	@Override
	public List<LexicalRule<? extends GermaNetInfo>> getRulesForLeft(
			String lemma, PartOfSpeech pos) throws LexicalResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LexicalRule<? extends GermaNetInfo>> getRulesForRight(
			String lemma, PartOfSpeech pos) throws LexicalResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LexicalRule<? extends GermaNetInfo>> getRules(String leftLemma,
			PartOfSpeech leftPos, String rightLemma, PartOfSpeech rightPos)
			throws LexicalResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize(CommonConfig config) throws ConfigurationException,
			ComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getComponentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInstanceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LexicalRule<? extends GermaNetInfo>> getRulesForLeft(
			String lemma, PartOfSpeech pos, GermaNetRelation relation)
			throws LexicalResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LexicalRule<? extends GermaNetInfo>> getRulesForRight(
			String lemma, PartOfSpeech pos, GermaNetRelation relation)
			throws LexicalResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LexicalRule<? extends GermaNetInfo>> getRules(String leftLemma,
			PartOfSpeech leftPos, String rightLemma, PartOfSpeech rightPos,
			GermaNetRelation relation) throws LexicalResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
