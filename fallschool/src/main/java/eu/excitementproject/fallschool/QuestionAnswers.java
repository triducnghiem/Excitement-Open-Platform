package eu.excitementproject.fallschool;

import java.util.List;

public class QuestionAnswers {
	private String questionText;
	private List<ReferenceAnswer> referenceAnswers;
	private List<StudentAnswer> studentAnswers;
	
	public String getQuestionText() {
		return questionText;
	}
	
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	public List<ReferenceAnswer> getReferenceAnswers() {
		return referenceAnswers;
	}
	
	public void setReferenceAnswers(List<ReferenceAnswer> referenceAnswers) {
		this.referenceAnswers = referenceAnswers;
	}
	
	public List<StudentAnswer> getStudentAnswers() {
		return studentAnswers;
	}
	
	public void setStudentAnswers(List<StudentAnswer> studentAnswers) {
		this.studentAnswers = studentAnswers;
	}
	
	
	
}
