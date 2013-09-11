package eu.excitementproject.fallschool;

public class ReferenceAnswer {
	
	public enum CAT {
		BEST("BEST"),
		GOOD("GOOD"),
		KEYWORD("KEYWORD"),
		MINIMAL("MINIMAL");
		
		private String val;
		
		private CAT(String val){
			this.val=val;
		}
		
		
		public String getValue(){
			return val;
		}
	};
	
	private CAT category;
	private String referenceAnswer;
	private String id;
	
}
