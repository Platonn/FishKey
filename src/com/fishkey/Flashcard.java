package com.fishkey;

/**  
 * klasa reprezentujaca pojedyncza fiszke
 */
class Flashcard {
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
	String getQuestion() {
		return question;
	}
	
	/** 
	 * zwraca prawidlowa odpowiedz fiszki
	 * 
	 * @return	String	prawidlowa odpowiedz fiszki 
	 */
	String getAnswer() {
		return answer;
	}
	
}