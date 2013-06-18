package com.fishkey.model;

/**
 * id fiszki wraz z informacja o poprawnosci odpowiedzi na te fiszke.
 * @author Platon
 */
public class AnswerCorrectness {
	
	/** stale oznaczajace poprawnosc odpowiedzi */
	public static enum Correctness {
		UNKNOWN,
		CORRECT,
		WRONG
	}
	
	/** id fiszki, ktorej dotyczy odpowiedz */
	private Long id;
	
	
	private Correctness correctness;
	
	public AnswerCorrectness(Long i, Correctness c){
		this.id 			= i;
		this.correctness	= c;
	}
	
	/** getter id */
	public Long getId() { return id; }
	
	/** getter odpowiedzi */
	public Correctness getAnswer() { return correctness; }
	
	/** setter odpowiedzi */
	public void setAnswer(Correctness c) { correctness = c; }
}
