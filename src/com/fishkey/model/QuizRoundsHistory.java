package com.fishkey.model;

import java.util.LinkedList;

import android.util.Log;

import com.fishkey.QuizRound;
import com.fishkey.exceptions.EndOfQuizException;

/**
 * historia rund quizu
 * @author Platon
 *
 */
public class QuizRoundsHistory {
	
	/** tag do oznaczania logow */
	private static final String LOG_TAG = QuizRoundsHistory.class.getName();
	
	/** lista uporzadkowana (historia) rund quizu */
	private LinkedList<QuizRound> roundsList;
	
	/**
	 * zwraca obiekt QuizRound biezacej rundy
	 * @return	QuizRound obiekt biezacej rundy
	 */
	public QuizRound getCurrentRound() {
		return roundsList.getLast();
	}
	
	/**
	 * zwraca liste uporzadkowana (historie) rund quizu 
	 * @return lista uporzadkowana (historia) rund quizu 
	 */
	public LinkedList<QuizRound> getAllRoundsList(){
		return roundsList;
	}
	
	public void startFirstRound(FlashcardIdList idToAskList){
		roundsList.add(new QuizRound(idToAskList));
	}
	
	public void startNextRound() throws EndOfQuizException {
		if(getCurrentRound()!=null)
			roundsList.add(new QuizRound(getCurrentRound()));	// Do stworzenia nowej rundy uzyj jako parametr biezacej, wlasnie zakonczonej rundy
		else {
			Log.e(LOG_TAG, "Nie mozna rozpoczac kolejnej rundy na podstawie poprzedniej, bo poprzednia == null");
		}
			
	}
}
