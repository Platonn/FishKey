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
	int countCorrect;
	int countCorrectLastRounds;
	int countWrong;
	int flashcardSetSize;
	int currentSetSize;
	private int roundNumber;
	
	/**  
	 * przygotowuje quiz do rozpoczecia
	 * 
	 * Wczytuje zestaw fiszek i ustawia liczniki poprawnych oraz zzych odpowiedzi na 0 oraz numer rundy na 1.
	 * Posiada wlasne statystyki, ktore udostepnia
	 * 
	 * @param	 context	obiekt <code>Context</code>
	 */
	public QuizState(Context context) {
		flashcardSet 			= new FlashcardSet();
		correctSet 				= new FlashcardSet();
		wrongSet 				= new FlashcardSet();
		countCorrect			= 0;
		countCorrectLastRounds	= 0;
		countWrong				= 0;
		roundNumber				= 1;
		
		this.importDataFromAssetsFile(context, "slowka.txt");

		currentSetSize=flashcardSetSize=flashcardSet.size();
		flashcardSet.shuffle();									// Tasuj zestaw fiszek do przepytania
	}
	
	/** 
	 * wczytuje z pliku assets zestaw fiszek 
	 * <p>
	 * TODO: Robic to w tle - watku/usludze. A moze nawet wczytywac z inernetu badz lokalnej bazy danych.  
	 * 
	 * @param	context		obiekt klasy context - do obslugi plikow
	 * @param	fileString	nazwa pliku do importu fiszek
	 * @deprecated
	 */
	public void importDataFromAssetsFile(Context context, String fileName) {
		try{
			InputStream in = context.getAssets().open(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));	// Plik musi posiadac poprawne kodowanie UTF-8!
			String line;																// Tu beda wczytywane kolejne linie
			String[] text;																// Tablica bedzie przechowywac dwa stringi: odpowiedz oraz pytanie fiszki (w podanej kolejnosci)
			do {
				line = reader.readLine();												// Wczytanie nastepnej linii
				if(line==null)															// Trzeba bylo tak zrobic, zeby mozna bylo uzyc potem funkcji split na nie-null'owym stringu
					break;
				text = line.split(" - ");												// Dwie spacje obok umozliwiaja podanie slowka zawierajacego "gêst¹ spacjê", np. semi-detached								// Rozdzielenie pytania od odpowiedzi przez separator (myslnik)
				flashcardSet.add(new Flashcard(text[1].trim(),text[0].trim()));		// Wczytuje wg schematu: "PYTANIE - ODPOWIEDZ" (musza byc spacje wokol myslnika)
			} while(true);
			in.close();
		} catch (IOException e) {
			Log.i("Quiz", "QuizState - cannot open a file");
		}
	}
	
	/** 
	 * usuwa ostatnia fiszke z zestawu do odpytywania i wrzuca podana fiszke do listy dobrze odgadnietych fiszek 
	 * 
	 * @param	f	fiszka dobrze odgadnieta
	 */
	public void answerCorrect(Flashcard f) {
		correctSet.add(f);					// Dodanie podanej fiszki do listy dobrze odgadnietych
		++countCorrect;						// Zwiekszenie licznika dobrze odgadnietych fiszek
	}
	
	/** 
	 * usuwa ostatnia fiszke z zestawu do odpytywania i wrzuca podana fiszke do listy dobrze odgadnietych fiszek 
	 * 
	 * @param	f	fiszka zle odgadnieta
	 */
	public void answerWrong(Flashcard f) {
		wrongSet.add(f);					// Dodanie podanej fiszki do listy zle odgadnietych
		++countWrong;						// Zwiekszenie licznika zle odgadnietych fiszek
	}
	
	/** 
	 * pobiera ale nie usuwa(!) fiszke z listy i zwraca ja w return 
	 * 
	 * @return	f	fiszka pobierana z kolejki
	 */
	public Flashcard getFlashcard(){
		return flashcardSet.getNext();
	}
	
	/** 
	 * zwraca liczbe udzielonych dobrych odpowiedzi 
	 * 
	 * @return	int	liczba dobrych odpowiedzi
	 * */
	int getStateCorrect() {
		return countCorrect;
	}
	
	/** 
	 * zwraca liczbe poprawionych odpowiedzi udzielonych w poprzednich rundach
	 * 
	 * @return	int	liczba odpowiedzi udzielonych w poprzednich rundach 
	 */
	int getStateCorrectLastRounds() {
		return countCorrectLastRounds;
	}
	
	/** 
	 * zwraca liczbe udzielonych zlych odpowiedzi
	 * 
	 * @return	int	liczba zlych odpowiedzi w aktualnej rundzie
	 */
	int getStateWrong() {
		return countWrong;
	}
	
	/**
	 * zwraca liczbe wszystkich fiszek w zestawie
	 * 
	 * @return 	int	ilosc wszystkich odpowiedzi w aktualnej rundzie
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
	 * @return	int	numer rundy
	 */
	int getRoundNumber() {
		return roundNumber;
	}
	
	/** 
	 * rozpoczyna nowa runde i podmienia zestaw slowek quizu z tymi zle odgadnietymi w poprzedniej rundzie
	 */
	void startNextRound() {
		++roundNumber;								// Zwieksz numer rundy
		countCorrectLastRounds += countCorrect;		// Dodaj do licznika poprawnych odpowiedzi fiszki poprawione w ostatniej rundzie
		currentSetSize			= countWrong;		// Ustaw jako liczbe fiszek w nastepnej rundzie - liczbe zle odgadnietych fiszek
		countCorrect	  		= 0;				// Wyzeruj licznik poprawianych odpowiedzi
		countWrong 				= 0;				// Wyzeruj licznik zlych odpowiedzi
		flashcardSet = wrongSet;					// Przerzuc fiszki z zestawu zlych odpowiedzi do zestawu przepytywanych (w nastepnej rundzie)
		wrongSet.clear();							// Wyrzuc wszystkie fiszki zle odgadniete w poprzedniej rundzie
	}
	
}