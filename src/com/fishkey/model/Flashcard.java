package com.fishkey.model;

/**  
 * klasa reprezentujaca pojedyncza fiszke
 */
public class Flashcard {
	
	/**
	 * id fiszki
	 */
	private long id;
	
	/**
	 * pytanie fiszki
	 */
	private String question;
	
	/**
	 * prawidlowa odpowiedz fiszki
	 */
	private String answer;
	
	/** 
	 * inicjalizuje pola fiszki
	 * 
	 * @param	q	pytanie fiszki
	 * @param	a	pdpowiedz fiszki 
	 */
	public Flashcard(long i, String q, String a) {
		id		 = i;
		question = q;
		answer	 = a;
	}
	
	/** 
	 * zwraca id fiszki
	 * 
	 * @return	String	id fiszki
	 */
	public long getId() {
		return id;
	}
	
	/** 
	 * zwraca pytanie fiszki
	 * 
	 * @return	String	pytanie fiszki 
	 */
	public String getQuestion() {
		return question;
	}
	
	/** 
	 * zwraca prawidlowa odpowiedz fiszki
	 * 
	 * @return	String	prawidlowa odpowiedz fiszki 
	 */
	public String getAnswer() {
		return answer;
	}
	
}