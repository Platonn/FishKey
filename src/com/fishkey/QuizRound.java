package com.fishkey;

import android.content.Context;
import android.util.Log;

import com.fishkey.exceptions.EndOfQuizException;
import com.fishkey.exceptions.EndOfQuizRoundException;
import com.fishkey.model.Flashcard;
import com.fishkey.model.FlashcardSet;

public class QuizRound implements IFlashcardPassOn {
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
	public final int SIZE;
	
	/**
	 * numer rundy
	 * <p>
	 * w konstruktorze numer rundy jest ustalany na podstawie <code>roundCounter</code>
	 * @see #roundCounter
	 */
	public final int NUMBER;
	
	/**
	 * licznik rund
	 * <p>
	 * licznik utworzonych instancji obiektu QuizRound
	 * @see QuizRound
	 */
	private static int roundsCounter = 0;
	
	/**
	 * tag to oznaczania logow
	 */
	private static final String LOG_TAG = QuizRound.class.getName();
	
	/**
	 * przygotowuje runde do przeprowadzenia
	 * <p>
	 * ustawia podany zestaw fiszek jako zestaw do przepytania
	 * @param fs			zestaw fiszek do przepytania w rundzie
	 */
	public QuizRound(FlashcardSet fs) {
		toAskSet 				= fs;
		answeredCorrectSet		= new FlashcardSet();
		answeredWrongSet		= new FlashcardSet();
		SIZE					= toAskSet.size();
		NUMBER 					= ++roundsCounter;
		Log.v(LOG_TAG,"Rozpoczeto runde nr " + NUMBER);
	}
	
	/**
	 * przygotowuje kolejna runde do przeprowadzenia na podstawie poprzedniej
	 * <p>
	 * kopiuje z poprzedniej rundy zestaw fiszek nieodgadnietych do zestawu fiszek do przepytania
	 * biezacej rundzie
	 * @param prevRound		poprzednia runda
	 * @throws EndOfQuizException rzucany, gdy w konstruktorze rundy zostanie podany pusty zestaw fiszek
	 */
	public QuizRound(QuizRound prevRound) throws EndOfQuizException {
		FlashcardSet fs = prevRound.answeredWrongSet;
		if(fs.isEmpty()) {
			Log.v(LOG_TAG,"Koniec quizu");
			throw new EndOfQuizException();
		}
		toAskSet				= new FlashcardSet();
		toAskSet.addAllFrom(fs);
		answeredCorrectSet		= new FlashcardSet();
		answeredWrongSet		= new FlashcardSet();
		SIZE					= toAskSet.size();
		NUMBER 					= ++roundsCounter;
		Log.v(LOG_TAG,"Rozpoczeto nastepna runde nr " + NUMBER);
	}
	
	/* Implementacja metod interfejsu IFlashcardPassOn */
	
	public Flashcard popFlashcard() throws EndOfQuizRoundException {
		if (isEnd()) {
			Log.v(LOG_TAG,"Koniec rundy nr "+NUMBER);
			throw new EndOfQuizRoundException(getReport());
		}
		else
			return toAskSet.pop();
	}
	
	public void putAnswered(Flashcard f, boolean isCorrectAnswered) {
		if (isCorrectAnswered)
			answeredCorrectSet.add(f);
		else
			answeredWrongSet.add(f);
		Log.v(LOG_TAG,"Dodano do zestawu " + (isCorrectAnswered ? "poprawnych" : "blednych") + ": " + f.getQuestion());
	}
	
	/* Implementacja metod wlasnych */
	
	/**
	 * wyzerowuje licznik rund
	 */
	public static void resetRoundsCounter() {
		roundsCounter = 0;
	}
	
	/**
	 * zwraca info, czy jest juz rundy quizu
	 * <p>
	 * zwraca info, czy skonczyly sie juz fiszki do przepytania w tej rundzie
	 * 
	 * @return	true, jesli zestaw koniec rundy, false w przeciwnym przypadku
	 */
	private boolean isEnd(){
		return toAskSet.isEmpty();
	}
	
	/* Implementacja metod interfejsu IQuizRoundInformator */
	
	/**
	 * zwraca liczbe poprawnych odpowiedzi w tej rundzie
	 * @return	liczba poprawnych odpowiedzi w tej rundzie
	 */
	public int getNumCorrect(){
		return answeredCorrectSet.size();
	}
	
	/**
	 * zwraca liczbe blednych odpowiedzi w tej rundzie
	 * @return	liczba blednych odpowiedzi w tej rundzie
	 */
	public int getNumWrong(){
		return answeredWrongSet.size();
	}
	
	/**
	 * Dodaje fiszke do zestawu dobrze ogadnietych
	 * @param	fiszka do dodania do zestawu dobrze odgadnietych
	 */
	public void answeredCorrect(Flashcard f){
		answeredCorrectSet.add(f);
	}
	
	/**
	 * Dodaje fiszke do zestawu dobrze ogadnietych
	 * @param	fiszka do dodania do zestawu dobrze odgadnietych
	 */
	public void answeredWrong(Flashcard f){
		answeredWrongSet.add(f);
	}
	
	/**
	 * raport stanu rundy
	 * @author Platon
	 *
	 */
	public class Report {
		int correctNum;
		int wrongNum;
		int roundSize;
		int roundNumber;
		
		public Report(){
			correctNum	= QuizRound.this.getNumCorrect();
			wrongNum 	= QuizRound.this.getNumWrong();
			roundSize	= QuizRound.this.SIZE;
			roundNumber	= QuizRound.this.NUMBER;
			Log.v(LOG_TAG,"Utworzono raport CWSN: " + correctNum + " " + wrongNum + " " + roundSize + " " + roundNumber);
		}
		
		/* gettery */
		public int getNumCorrect() { return correctNum; }
		public int getNumWrong() { return wrongNum; }
		public int getRoundSize() { return roundSize; }
		public int getRoundNumber() { return roundNumber; }
	}
	
	/**
	 * zwraca raport z aktualnego stanu rundy
	 * @return raport z aktualnego stanu rundy
	 */
	public Report getReport(){
		return new Report();
	}
}
