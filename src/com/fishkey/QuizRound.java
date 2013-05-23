package com.fishkey;

import android.content.Context;

import com.fishkey.model.FlashcardSet;

public class QuizRound {
	/**
	 * zestaw fiszek do przepytania
	 */
	FlashcardSet toAskSet;
	
	/**
	 * zestaw fiszek, na ktore odpowiedziano poprawnie
	 */
	FlashcardSet answeredCorrectSet;
	
	/**
	 * zestaw fiszek, na ktore odpowiedziano blednie
	 */
	FlashcardSet answeredWrongSet;
	
	/**
	 * ilosc fiszek do przepytania w rundzie
	 */
	public final int ROUND_SET_SIZE;
	
	/**
	 * numer rundy
	 * <p>
	 * w konstruktorze numer rundy jest ustalany na podstawie <code>roundCounter</code>
	 * @see #roundCounter
	 */
	public final int ROUND_NUMBER;
	
	/**
	 * licznik rund
	 * <p>
	 * przechowuje liczbe utworzonych instancji obiektu QuizRound
	 * @see QuizRound
	 */
	private static int roundCounter = 0;
	
	/**
	 * przygotowuje runde do przeprowadzia
	 * @param fs			lista do przepytania w rundzie
	 * @param roundNumber	numer rundy
	 */
	public QuizRound(FlashcardSet fs) {
		toAskSet 				= fs;
		answeredCorrectSet		= new FlashcardSet();
		answeredWrongSet		= new FlashcardSet();
		
		ROUND_SET_SIZE			= fs.size();
		ROUND_NUMBER 			= ++roundCounter;
	}
}
