package local.test;

import eu.excitementproject.eop.core.component.lexicalknowledge.CanonicalRelationSpecifier;
import eu.excitementproject.eop.core.component.lexicalknowledge.OwnRelationSpecifier;
import eu.excitementproject.eop.core.component.lexicalknowledge.TERuleRelation;

public class GermaNetRelation implements CanonicalRelationSpecifier, OwnRelationSpecifier<WordNetRelation> {

	public GermaNetRelation(TERuleRelation canonicalRelation, WordNetRelation ownRelation)
	{
		this.canonicalRel = canonicalRelation; 
		this.ownRelation = ownRelation; 
	}
	
	public GermaNetRelation(TERuleRelation canonicalRelation)
	{
		this(canonicalRelation, null);
	}
	
	public GermaNetRelation(WordNetRelation ownRelation)
	{
		this(null, ownRelation); 
	}
	
	@Override
	public TERuleRelation getCanonicalRelation() {
		return canonicalRel;  
	}
	
	@Override
	public WordNetRelation getOwnRelation() {
		return ownRelation; 
	}
	
	private final TERuleRelation canonicalRel; 
	private final WordNetRelation ownRelation; 
	
}
