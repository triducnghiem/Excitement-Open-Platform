package eu.excitementproject.fallschool;

public class StudentAnswer {
	
	private enum Accuracy{
		correct("correct"),
		incorrect("incorrect"),
		contradictory("contradictory");
		
		private String val;
		
		private Accuracy(String val){
			this.val=val;
		}
		
		public String getValue(){
			return val;
		}
		
	};
	
	private String id;
	private int count;
	private String answerMatch;
	private Accuracy accuracy;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public String getAnswerMatch() {
		return answerMatch;
	}
	
	public void setAnswerMatch(String answerMatch) {
		this.answerMatch = answerMatch;
	}
	
	public Accuracy getAccuracy() {
		return accuracy;
	}
	
	public void setAccuracy(String accuracy) {
		this.accuracy = Accuracy.valueOf(accuracy);
	}
	
}
