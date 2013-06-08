package com.fishkey;

import android.content.Context;

import com.fishkey.exceptions.LoadFlashcardSetException;
import com.fishkey.model.FlashcardSet;

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
	
	public static FlashcardSet getFlashcardSet(Context context) throws LoadFlashcardSetException {
		FlashcardSet fs = FlashcardSetProvider.importDataFromAssetsFile(context, "slowka.txt");
		//TODO: tu przejrzec zestaw fiszek i podjac decyzje, ktore z nich wstrzymac.
		// zmodyfikowac takze statystyki dotyczace wstrzymanych fiszek
		return fs;
	}
}
