package com.fishkey;

import com.fishkey.model.Flashcard;

/**
 * Sedzia sumienie ocenia "naiwnie" poprawnosc odpowiedi,
 * bazujac na informacji, czy odpowiadajacy znal poprawna odpowiedz
 * @author Platon
 *
 */
public class RefereeConscience extends Referee {
	public RefereeConscience(Flashcard f) {
		super(f);
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
