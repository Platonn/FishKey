package com.fishkey;

import android.content.Context;

import com.fishkey.exceptions.EmptyQuizException;
import com.fishkey.exceptions.EndOfQuizException;
import com.fishkey.exceptions.EndOfQuizRoundException;
import com.fishkey.exceptions.QuizInitException;

/**
 * Sedzia sumienie ocenia "naiwnie" poprawnosc odpowiedi,
 * bazujac na informacji, czy odpowiadajacy znal poprawna odpowiedz
 * @author Platon
 *
 */
public class UmpireConscience extends Umpire {
	/**
	 * konstruktor t.j Umpire
	 * @see Umpire
	 * @param context
	 * @throws QuizInitException
	 * @throws EmptyQuizException
	 * @throws EndOfQuizException
	 * @throws EndOfQuizRoundException
	 */
	public UmpireConscience(Context context) throws QuizInitException, EmptyQuizException, EndOfQuizException, EndOfQuizRoundException {
		super(context);
	}
	
	/**
	 * zwraca info, czy odpowiadajacy znal poprawna odpowiedz
	 * <p>
	 * ocenia poprawnosc odpowiedzi, bazujac "ufnie" na informacji,
	 * czy odpowiadajacy znal poprawna odpowiedz
	 * @param knew	informacja, czy odpowiadajacy znal poprawna odpowiedz
	 * @return 		true, jesli odpowiadajacy znal poprawna odpowiedz,
	 * 				false w przeciwnym przypadku
	 * @throws EndOfQuizRoundException 
	 * @throws EndOfQuizException 
	 */
	public void adjudicate(boolean knew) throws EndOfQuizException, EndOfQuizRoundException{
		quizState.putAnswered(currentFlashcard,knew);
		getNextFlashcard();
	}
}
