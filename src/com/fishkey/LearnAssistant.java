package com.fishkey;

import android.content.Context;

import com.fishkey.exceptions.QuizInitException;
import com.fishkey.model.FlashcardSet;
import com.fishkey.utils.FlashcardSetProvider;

/**
 * odpowiada za efektywna nauke
 * <p>
 * podejmuje decyzje na podstawie statystyk - z ktorych fiszek warto, a z ktorych nie warto
 * przepytywac uzytkownika
 * @author Platon
 *
 */
public class LearnAssistant {
	StatisticsManager statsManager;
	
	public LearnAssistant(){
		statsManager = new StatisticsManager();
	}
	
	public static FlashcardSet getFlashcardSet(Context context) throws QuizInitException {
		FlashcardSet fs = FlashcardSetProvider.getFlashcardSet(context);
		//TODO: tu przejrzec zestaw fiszek i podjac decyzje, ktore z nich wstrzymac.
		// zmodyfikowac takze statystyki dotyczace wstrzymanych fiszek
		return fs;
	}
}
