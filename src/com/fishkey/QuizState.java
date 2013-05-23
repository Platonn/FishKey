package com.fishkey;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import com.fishkey.model.Flashcard;
import com.fishkey.model.FlashcardSet;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**  
 * obsluga stanu quizu
 */
class QuizState {
	FlashcardSet flashcardSet;
	FlashcardSet correctSet;
	FlashcardSet wrongSet;
	int countCorrectLastRounds;
	int flashcardSetSize;
	int currentSetSize;
	private int roundNumber;
	
	/**  
	 * przygotowuje quiz do rozpoczecia
	 * <p>
	 * Wczytuje zestaw fiszek i ustawia liczniki poprawnych oraz zzych odpowiedzi na 0 oraz numer rundy na 1.
	 * Posiada wlasne statystyki, ktore udostepnia
	 * 
	 * @param	 context	obiekt <code>Context</code>
	 */
	public QuizState(Context context) {
		flashcardSet 			= new FlashcardSet();
		correctSet 				= new FlashcardSet();
		wrongSet 				= new FlashcardSet();
		countCorrectLastRounds	= 0;
		roundNumber				= 1;
		
		FlashcardSetProvider.importDataFromAssetsFile(context, "slowka.txt", flashcardSet);
		
		currentSetSize=flashcardSetSize=flashcardSet.size();
		flashcardSet.shuffle();									// Tasuj zestaw fiszek do przepytania
	}
	
	/** 
	 * usuwa ostatnia fiszke z zestawu do odpytywania i wrzuca podana fiszke do listy dobrze odgadnietych fiszek 
	 * 
	 * @param	f	fiszka dobrze odgadnieta
	 */
	public void answerCorrect(Flashcard f) {
		correctSet.add(f);					// Dodanie podanej fiszki do listy dobrze odgadnietych
		//++countCorrect;						// Zwiekszenie licznika dobrze odgadnietych fiszek
	}
	
	/** 
	 * usuwa ostatnia fiszke z zestawu do odpytywania i wrzuca podana fiszke do listy dobrze odgadnietych fiszek 
	 * 
	 * @param	f	fiszka zle odgadnieta
	 */
	public void answerWrong(Flashcard f) {
		wrongSet.add(f);					// Dodanie podanej fiszki do listy zle odgadnietych
		//++countWrong;						// Zwiekszenie licznika zle odgadnietych fiszek
	}
	
	/** 
	 * pobiera i usuwa fiszke z zestawu oraz zwraca ja w return 
	 * 
	 * @return	f	fiszka pobierana z kolejki
	 */
	public Flashcard getFlashcard(){
		return flashcardSet.getNext();
	}
	
	/** 
	 * zwraca liczbe udzielonych dobrych odpowiedzi 
	 * 
	 * @return	liczba dobrych odpowiedzi
	 * */
	int getStateCorrect() {
		//return countCorrect;
		return correctSet.size();
	}
	
	/** 
	 * zwraca liczbe poprawionych odpowiedzi udzielonych w poprzednich rundach
	 * 
	 * @return	liczba odpowiedzi udzielonych w poprzednich rundach 
	 */
	int getStateCorrectLastRounds() {
		return countCorrectLastRounds;
	}
	
	/** 
	 * zwraca liczbe udzielonych zlych odpowiedzi
	 * 
	 * @return	liczba zlych odpowiedzi w aktualnej rundzie
	 */
	int getStateWrong() {
		//return countWrong;
		return wrongSet.size();
	}
	
	/**
	 * zwraca liczbe wszystkich fiszek w zestawie
	 * 
	 * @return 	ilosc wszystkich odpowiedzi w aktualnej rundzie
	 */
	int getStateAllCurrentSet() {
		return currentSetSize;
	}
	
	int getStateAll() {
		return flashcardSetSize;
	}
	
	/** 
	 * zwraca numer rundy
	 * 
	 * @return	numer rundy
	 */
	int getRoundNumber() {
		return roundNumber;
	}
	
	/** 
	 * rozpoczyna nowa runde i podmienia zestaw slowek quizu z tymi zle odgadnietymi w poprzedniej rundzie
	 */
	void startNextRound() {
		++roundNumber;								// Zwieksz numer rundy
		countCorrectLastRounds += correctSet.size();// Dodaj do licznika poprawnych odpowiedzi fiszki poprawione w ostatniej rundzie
		currentSetSize			= wrongSet.size();	// Ustaw jako liczbe fiszek w nastepnej rundzie - liczbe zle odgadnietych fiszek
		correctSet.clear();							// Wyrzuc wszystkie fiszki dobrze odgadniete w poprzedniej rundzie
		flashcardSet.moveAllFrom(wrongSet);			// Przerzuc fiszki z zestawu zlych odpowiedzi do zestawu przepytywanych (w nastepnej rundzie)
	}
	
}