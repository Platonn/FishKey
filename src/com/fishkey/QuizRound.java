package com.fishkey;

import android.util.Log;

import com.fishkey.exceptions.EndOfQuizException;
import com.fishkey.exceptions.EndOfQuizRoundException;
import com.fishkey.model.AnswerCorrectness;
import com.fishkey.model.AnswerCorrectnessList;
import com.fishkey.model.AnswerCorrectness;
import com.fishkey.model.FlashcardIdList;
import com.fishkey.model.ShuffleLinkedList;

/**
 * runda quizu
 * @author Platon
 *
 */
public class QuizRound {
	/**
	 * lista id fiszek do przepytania
	 */
	FlashcardIdList idToAskList;
	
	/**
	 * lista id fiszek, na ktore udzielono poprawnej odpowiedzi
	 */
	AnswerCorrectnessList answeredCorrectList;
	
	/**
	 * lista id fiszek, na ktore udzielono blednej odpowiedzi
	 */
	AnswerCorrectnessList answeredWrongList;
	
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
	 * tag to oznaczania logow
	 */
	private static final String LOG_TAG = QuizRound.class.getName();
	
	/**
	 * przygotowuje runde do przeprowadzenia, ustawia jej numer na 1
	 * <p>
	 * ustawia podany zestaw fiszek jako zestaw do przepytania
	 * @param fs			zestaw fiszek do przepytania w rundzie
	 */
	public QuizRound(FlashcardIdList idToAskList) {
		this.idToAskList		= idToAskList;
		answeredCorrectList		= new AnswerCorrectnessList();
		answeredWrongList		= new AnswerCorrectnessList();
		SIZE					= idToAskList.size();
		NUMBER 					= 1;
		Log.v(LOG_TAG,"Rozpoczeto runde nr " + NUMBER);
	}
	
	/**
	 * przygotowuje kolejna runde do przeprowadzenia na podstawie poprzedniej,
	 * ustawia jej numer na o 1 wiekszy niz poprzedniej
	 * <p>
	 * kopiuje z poprzedniej rundy zestaw fiszek nieodgadnietych do zestawu fiszek do przepytania
	 * biezacej rundzie
	 * @param prevRound		poprzednia runda
	 * @throws EndOfQuizException rzucany, gdy w konstruktorze rundy zostanie podany pusty zestaw fiszek
	 */
	public QuizRound(QuizRound prevRound) throws EndOfQuizException {
		AnswerCorrectnessList prevAnsweredWrong = prevRound.getAnsweredWrongIdList();
		if(prevAnsweredWrong.isEmpty()) {
			Log.v(LOG_TAG,"Koniec quizu");
			throw new EndOfQuizException();
		}
		idToAskList				= new FlashcardIdList();
		for(AnswerCorrectness idWithAnsw : prevAnsweredWrong) {
			idToAskList.add(idWithAnsw.getId());
		}
		answeredCorrectList		= new AnswerCorrectnessList();
		answeredWrongList		= new AnswerCorrectnessList();
		SIZE					= idToAskList.size();
		NUMBER 					= prevRound.NUMBER + 1;
		Log.v(LOG_TAG,"Rozpoczeto nastepna runde nr " + NUMBER);
	}
	
	/* Implementacja metod interfejsu IFlashcardPassOn */
	
	public Long popFlashcardId() throws EndOfQuizRoundException {
		if (isEnd()) {
			Log.v(LOG_TAG,"Koniec rundy nr "+NUMBER);
			throw new EndOfQuizRoundException(getReport());
		}
		else
			return idToAskList.poll();
	}
	
	public void putAnswered(AnswerCorrectness idWithAnsw) {
		if (idWithAnsw.getAnswer().equals(AnswerCorrectness.Correctness.CORRECT)){
			answeredCorrectList.offer(idWithAnsw);
			Log.v(LOG_TAG,"Dodano do listy poprawnych id fiszki: " + idWithAnsw.getId());
		} else if (idWithAnsw.getAnswer().equals(AnswerCorrectness.Correctness.WRONG)) {
			answeredWrongList.offer(idWithAnsw);
			Log.v(LOG_TAG,"Dodano do listy blednych id fiszki: " + idWithAnsw.getId());
		} else {
			Log.w(LOG_TAG,"Nie mozna dodac do listy poprawnych ani blednych id fiszki: "+ idWithAnsw.getId());
		}
		
	}
	
	/* Implementacja metod wlasnych */
	
	/**
	 * zwraca info, czy jest juz rundy quizu
	 * <p>
	 * zwraca info, czy skonczyly sie juz fiszki do przepytania w tej rundzie
	 * 
	 * @return	true, jesli zestaw koniec rundy, false w przeciwnym przypadku
	 */
	private boolean isEnd(){
		return idToAskList.isEmpty();
	}
	
	/** 
	 * zwraca zestaw dobrze odpowiedzianych fiszek, jesli nastapil juz koniec quizu,
	 * w przeciwnym przypadku zwraca false
	 * 
	 * @return	zestaw dobrze odpowiedzianych fiszek, jesli nastapil juz koniec quizu,
	 * 			w przeciwnym przypadku zwraca false
	 */
	public AnswerCorrectnessList getAnsweredCorrectList(){
		if(isEnd())
			return this.answeredCorrectList;
		else
			return null;
	}
	
	/** 
	 * zwraca zestaw zle odpowiedzianych fiszek, jesli nastapil juz koniec quizu,
	 * w przeciwnym przypadku zwraca false
	 * 
	 * @return	zestaw zle odpowiedzianych fiszek, jesli nastapil juz koniec quizu,
	 * 			w przeciwnym przypadku false
	 */
	public AnswerCorrectnessList getAnsweredWrongIdList(){
		if(isEnd())
			return this.answeredWrongList;
		else
			return null;
	}
	
	/**
	 * Dodaje fiszke do zestawu dobrze ogadnietych
	 * @param	fiszka do dodania do zestawu dobrze odgadnietych
	 */
	public void answeredCorrect(AnswerCorrectness idWithAnsw){
		answeredCorrectList.offer(idWithAnsw);
	}
	
	/**
	 * Dodaje fiszke do zestawu dobrze ogadnietych
	 * @param	fiszka do dodania do zestawu dobrze odgadnietych
	 */
	public void answeredWrong(AnswerCorrectness idWithAnsw){
		answeredWrongList.offer(idWithAnsw);
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
	
	/**
	 * zwraca liczbe poprawnych odpowiedzi w tej rundzie
	 * @return	liczba poprawnych odpowiedzi w tej rundzie
	 */
	public int getNumCorrect(){
		return answeredCorrectList.size();
	}
	
	/**
	 * zwraca liczbe blednych odpowiedzi w tej rundzie
	 * @return	liczba blednych odpowiedzi w tej rundzie
	 */
	public int getNumWrong(){
		return answeredWrongList.size();
	}
}
