package com.fishkey;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import com.fishkey.exceptions.EmptyQuizException;
import com.fishkey.exceptions.EndOfQuizException;
import com.fishkey.exceptions.EndOfQuizRoundException;
import com.fishkey.exceptions.QuizInitException;
import com.fishkey.model.Flashcard;
import com.fishkey.model.FlashcardSet;

import android.R.integer;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**  
 * odpowiada za obsluge stanu quizu
 */
class QuizState implements IQuizInformator, IFlashcardPassOn {
	
	/**
	 * lista z rundami QuizRound
	 * <p>
	 * reprezentuje historie przebiegu rund quizu
	 * 
	 * @see QuizRound
	 */
	private LinkedList<QuizRound> roundsList;
	
	/**
	 * liczba wszystkich fiszek w quizie
	 */
	public final int QUIZ_SIZE;
	
	/**
	 * liczba dobrze odgadnietych fiszek w poprzednich rundach
	 */
	private int countCorrectPastRounds;
	
	/**
	 * tag to oznaczania logow
	 */
	private static final String LOG_TAG = QuizState.class.getName();
	
	/**
	 * przygotowuje quiz do rozpoczecia
	 * <p>
	 * Wczytuje zestaw fiszek i ustawia liczniki poprawnych oraz zzych odpowiedzi na 0 oraz numer rundy na 1.
	 * Posiada wlasne statystyki, ktore udostepnia
	 * 
	 * @param	 context	obiekt <code>Context</code>
	 * @throws QuizInitException
	 * @throws EmptyQuizException 
	 */
	public QuizState(Context context) throws QuizInitException, EmptyQuizException {
		QuizRound.resetRoundsCounter();
		roundsList = new LinkedList<QuizRound>();
		
		FlashcardSet fs = FlashcardSetProvider.getFlashcardSet(context);
		if(fs.isEmpty()) throw new EmptyQuizException();
		
		fs.shuffle();
		roundsList.add(new QuizRound(fs));
		
		QUIZ_SIZE = fs.size();
		countCorrectPastRounds = 0;
	}
	
	/* Implementacja metod interfejsu IQuizInformator */
	@Override
	public int getNumPastCorrect() {
		return countCorrectPastRounds;
	}

	@Override
	public int getNumCorrect() {
		return getCurrentRound().getNumCorrect();
	}

	@Override
	public int getNumWrong() {
		return getCurrentRound().getNumWrong();
	}

	@Override
	public int getCurrentRoundNumber() {
		return getCurrentRound().NUMBER;
	}

	@Override
	public int getCurrentRoundSize() {
		return getCurrentRound().SIZE;
	}

	@Override
	public int getQuizSize() {
		return roundsList.getFirst().SIZE;
	}
	
	/* Implementacja metod interfejsu IFlashcardPassOn */
	
	public Flashcard popFlashcard() throws EndOfQuizRoundException {
		return getCurrentRound().popFlashcard();
	}
	
	public void putAnswered(Flashcard f, boolean isCorrectAnswered) {
		getCurrentRound().putAnswered(f, isCorrectAnswered);
	}
	
	/* Implementacja metod wlasnych */
	
	/**
	 * zwraca obiekt QuizRound biezacej rundy
	 * @return	QuizRound obiekt biezacej rundy
	 */
	private QuizRound getCurrentRound() {
		return roundsList.getLast();
	}
	
	/**
	 * rozpoczyna nowa runde
	 * <p>
	 * tworzy nowa runde, ktora bedzie od teraz biezaca
	 * @throws EndOfQuizException gdy nastapi koniec quizu
	 */
	public void startNextRound() throws EndOfQuizException {
		countCorrectPastRounds += getCurrentRound().getNumCorrect();
		roundsList.add(new QuizRound(getCurrentRound()));
	}	
}