package com.fishkey;


/**
 * metody informujace o stanie quizu i biezacej rundy
 * @author Platon
 *
 */
public interface IQuizInformator {
	
	/** 
	 * zwraca liczbe poprawnych odpowiedzi udzielonych w poprzednich rundach
	 * 
	 * @return	liczba poprawnych odpowiedzi udzielonych w poprzednich rundach 
	 */
	public int getNumPastCorrect();
	
	/**
	 * zwraca liczbe wszystkich fiszek quizu
	 * @return	liczba wszystkich fiszek quizu
	 */
	public int getQuizSize();
	
	/** 
	 * zwraca liczbe udzielonych poprawnych odpowiedzi w biezacej rundzie
	 * 
	 * @return	liczba udzielonych poprawnych odpowiedzi w biezacej rundzie
	 */
	public int getNumCorrect();
	
	
	/** 
	 * zwraca liczbe udzielonych zlych odpowiedzi w biezacej rundzie
	 * 
	 * @return	liczba udzielonych zlych odpowiedzi w biezacej rundzie
	 */
	public int getNumWrong();
	
	
	/**
	 * zwraca numer biezacej rundy
	 * @return numer biezacej rundy
	 */
	public int getCurrentRoundNumber();
	
	
	/**
	 * zwraca liczbe fiszek w biezacej rundzie
	 * 
	 * @return 	liczba fiszek w biezacej rundzie
	 */
	public int getCurrentRoundSize();
}
