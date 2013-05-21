package com.fishkey;

/**  
 * Klasa reprezentujaca pojedyncza fiszke
 */
class Flashcard {
	/** Pytanie fiszki */
	private String question;
	
	/** Prawidlowa odpowiedz fiszki */
	private String answer;
	
	/** Konstruktor przypisuje pytanie i odpowiedz fiszki do pol klasy 
	 * 
	 * @param	q	Pytanie fiszki
	 * @param	a	Odpowiedz fiszki 
	 */
	public Flashcard(String q, String a) {
		question = q;
		answer	 = a;
	}
	
	/** Funkcja zwraca pytanie fiszki
	 * 
	 * @return	string	Pytanie fiszki 
	 */
	String getQuestion() {
		return question;
	}
	
	/** Funkcja zwraca prawidlowa odpowiedz fiszki
	 * 
	 * @return	string	Odpowiedz fiszki 
	 */
	String getAnswer() {
		return answer;
	}
	
}