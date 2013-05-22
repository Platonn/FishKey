package com.fishkey;

/**
 * Sedzia sprawdza poprawnosc udzielanych odpowiedzi.
 * <p>
 * Pobiera od <code>QuizManager</code>'a fiszke. Na jej podstawie
 * potrafi zadac pytanie i ocenic odpowiedz.
 * 
 * @author Platon
 * @see QuizManager
 *
 */
public class Referee {
	/**
	 * aktualnie przepytywana fiszka
	 * <p>
	 * gdy rowne null, to znaczy, ze koniec quizu
	 */
	Flashcard currentFlashcard;
	
	/**
	 * pobiera pierwsza fiszke w konstruktorze
	 * @param f		pierwsza fiszka z zestawu 	
	 */
	public Referee(Flashcard f) {
		currentFlashcard = f;
	}
	
	/**
	 * zwraca informacje, czy quiz sie skonczyl
	 * @return 	true, jesli koniec quizu, false w przeciwnym przypadku
	 */
	public boolean isEnd() {
		return currentFlashcard==null;
	}
	
	/**
	 * zwraca, czy podana odpowiedz jest zgodna z prawidlowa
	 * @param answer	odpowiedz do sprawdzenia
	 * @return 			true, jesli poprawna odpowiedz, false w przeciwnym przypadku
	 */
	public boolean adjudicate(String answer){
		return answer.equals(currentFlashcard.getAnswer());
	}
	
	
}
