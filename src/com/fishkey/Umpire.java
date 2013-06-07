package com.fishkey;

import android.content.Context;
import android.util.Log;

import com.fishkey.exceptions.EmptyQuizException;
import com.fishkey.exceptions.EndOfQuizException;
import com.fishkey.exceptions.EndOfQuizRoundException;
import com.fishkey.exceptions.LoadFlashcardSetException;
import com.fishkey.model.Flashcard;

/**
 * Sedzia sprawdza poprawnosc udzielanych odpowiedzi.
 * <p>
 * Pobiera od <code>QuizManager</code>'a fiszke. Na jej podstawie
 * potrafi zadac pytanie i ocenic odpowiedz.
 * 
 * @author Platon
 * @see QuizManager
 *
 */
public class Umpire implements IQuizInformator {
	
	/**
	 * tag to oznaczania logow
	 */
	private static final String LOG_TAG = Umpire.class.getName();
	
	/**
	 * manager quizu
	 * @see QuizState
	 */
	private QuizState quizState;
	
	/**
	 * aktualnie przepytywana fiszka
	 * <p>
	 * gdy rowne null, to znaczy ze nie ma juz fiszek w zestawie do odpytywania
	 */
	Flashcard currentFlashcard;
	
	/**
	 * tekst, ktory pojawi sie w miejscu poprawnej odpowiedzi przed ujawnieniem prawdziwej
	 * poprawnej odpowiedzi
	 */
	public static final String ANSWER_REPLACEMENT = "?";
	
	/**
	 * konstruktor sedziego
	 * @param context
	 * @throws LoadFlashcardSetException
	 * @throws EmptyQuizException
	 */
	public Umpire(Context context) throws LoadFlashcardSetException, EmptyQuizException {
		quizState = new QuizState(context);
	}
	
	/**
	 * zwraca, czy podana odpowiedz jest zgodna z prawidlowa
	 * @param answer	odpowiedz do sprawdzenia
	 * @return 			czy podano poprawna odpowiedz
	 */
	public boolean adjudicate(String answer){
		boolean verdict = answer.equals(currentFlashcard.getAnswer());
		quizState.putAnswered(currentFlashcard,verdict);
		return verdict;	
	}
	
	/**
	 * zwraca tresc pytania (string) biezacej fiszki
	 * @return	jesli istnieje fiszka do odpytania, to zwroc tresc pytania tej fiszki,
	 * 			w przeciwnym przypadku zwroc tekst zastepczy z informacja o braku fiszki
	 * @throws EndOfQuizRoundException 
	 * @throws EndOfQuizException 
	 */
	public String getQuestion() throws EndOfQuizRoundException, EndOfQuizException{
		String question = currentFlashcard.getQuestion();
		try {
			quizState.popFlashcard();
		} catch (EndOfQuizRoundException eofQuizRound) {
			quizState.startNextRound();			// Jesli nie ma nastepnej rundy, zostanie rzucony wyjatek EOFQuiz
			throw eofQuizRound;					// W przeciwnym przypadku zostanie rzucony dalej wyjatek EOFQuizRound
		}
		return question;
	}
	
	/* Implementacja metod interfejsu */
	
	@Override
	public int getNumPastCorrect() {
		return quizState.getNumPastCorrect();
	}

	@Override
	public int getNumCorrect() {
		return quizState.getNumCorrect();
	}

	@Override
	public int getNumWrong() {
		return quizState.getNumWrong();
	}

	@Override
	public int getCurrentRoundNumber() {
		return quizState.getCurrentRoundNumber();
	}

	@Override
	public int getCurrentRoundSize() {
		return quizState.getCurrentRoundSize();
	}

	@Override
	public int getQuizSize() {
		return quizState.getQuizSize();
	}
	
}
