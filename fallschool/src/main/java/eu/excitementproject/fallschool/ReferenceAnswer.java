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
	
	private String fileId;
	private String id;
	
	public CAT getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = CAT.valueOf(category);
	}
	
	public String getReferenceAnswer() {
		return referenceAnswer;
	}
	
	public void setReferenceAnswer(String referenceAnswer) {
		this.referenceAnswer = referenceAnswer;
	}
	
	public String getFileId() {
		return fileId;
	}
	
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}		
}
