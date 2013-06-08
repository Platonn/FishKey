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
	protected QuizState quizState;
	
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
	 * konstruktor sedziego pobiera pierwsza fiszke do przepytania
	 * @param context
	 * @throws LoadFlashcardSetException gdy nieudalo sie wczytac zestawu fiszek
	 * @throws EmptyQuizException gdy wczytany zestaw fiszek jest pusty
	 * @throws EndOfQuizRoundException 
	 * @throws EndOfQuizException 
	 */
	public Umpire(Context context) throws LoadFlashcardSetException, EmptyQuizException, EndOfQuizException, EndOfQuizRoundException {
		quizState = new QuizState(context);
		getNextFlashcard();							// TODO: tu nie powinien byc rzucany wyjatek konca rundy ani quizu!
	}
	
	/**
	 * sprawdza, czy podana odpowiedz jest zgodna z prawidlowa i zgodnie ze swoim werdyktem kaze
	 * wlozyc fiszke do odpowiedniego zestawu 
	 * @param answer	odpowiedz do sprawdzenia
	 * @throws EndOfQuizRoundException gdy koniec rundy
	 * @throws EndOfQuizException gdy koniec quizu
	 */
	public void adjudicate(String answer) throws EndOfQuizException, EndOfQuizRoundException{
		boolean verdict = answer.equals(currentFlashcard.getAnswer());
		quizState.putAnswered(currentFlashcard,verdict);
		getNextFlashcard();
	}
	
	/**
	 * zwraca tresc pytania biezacej fiszki
	 * @return	tresc pytania biezacej fiszki
	 */
	public String getQuestion() {
		return currentFlashcard.getQuestion();
	}
	
	/**
	 * zwraca poprawna odpowiedz biezacej fiszki
	 * @return	poprawna odpowiedz biezacej fiszki
	 */
	public String getAnswer() {
		return currentFlashcard.getAnswer();
	}
	
	/**
	 * aktualizuje biezaca fiszke
	 * @throws EndOfQuizException gdy nie mozna pobrac fiszki, bo koniec quizu
	 * @throws EndOfQuizRoundException gdy nie mozna pobrac fiszki, bo koniec rundy 
	 */
	protected void getNextFlashcard() throws EndOfQuizException, EndOfQuizRoundException {
		try {
			currentFlashcard = quizState.popFlashcard();
		} catch (EndOfQuizRoundException eofQuizRound) {
			quizState.startNextRound();						// Rozpocznij nowa runde. Jesli nie ma nastepnej rundy, zostanie rzucony wyjatek EOFQuiz
			currentFlashcard = quizState.popFlashcard();	// W przeciwnym przypadku rozpocznie sie nowa runda. Wiec Pobierz nowa fiszke // TODO: tu NIE powinien byc rzucany wyjatek eofQuizRound! 
			throw eofQuizRound;								// i rzuc dalej wyjatek EOFQuizRound
		}
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
