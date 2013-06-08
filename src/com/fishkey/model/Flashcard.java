package com.fishkey.model;

/**  
 * klasa reprezentujaca pojedyncza fiszke
 */
public class Flashcard {
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
	public Flashcard(String q, String a) {
		question = q;
		answer	 = a;
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