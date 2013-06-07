package com.fishkey;

import android.content.Context;

import com.fishkey.exceptions.EmptyQuizException;
import com.fishkey.exceptions.LoadFlashcardSetException;

/**
 * Sedzia sumienie ocenia "naiwnie" poprawnosc odpowiedi,
 * bazujac na informacji, czy odpowiadajacy znal poprawna odpowiedz
 * @author Platon
 *
 */
public class UmpireConscience extends Umpire {
	public UmpireConscience(Context context) throws LoadFlashcardSetException, EmptyQuizException {
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
	 */
	public boolean adjudicate(boolean knew){
		return knew;
	}
}
