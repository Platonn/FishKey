package com.fishkey;

import android.content.Context;

import com.fishkey.exceptions.EmptyQuizException;
import com.fishkey.exceptions.EndOfQuizException;
import com.fishkey.exceptions.EndOfQuizRoundException;
import com.fishkey.exceptions.QuizInitException;
import com.fishkey.model.Flashcard;
import com.fishkey.model.AnswerCorrectness;
import com.fishkey.model.FlashcardIdList;
import com.fishkey.model.FlashcardSet;
import com.fishkey.model.FlashcardWithId;
import com.fishkey.model.QuizRoundsHistory;

/**  
 * odpowiada za obsluge stanu quizu
 */
public class QuizState implements IQuizInformator {
	
	/**
	 * historia rund quizu
	 */
	private QuizRoundsHistory quizHistory;
	
	/**
	 * liczba wszystkich fiszek w quizie
	 */
	public final int QUIZ_SIZE;
	
	/**
	 * liczba dobrze odgadnietych fiszek w poprzednich rundach
	 */
	private int countCorrectPastRounds;
	
	/**
	 * Asystent uczenia sie
	 */
	LearnAssistant learnAssistant;
	
	/**
	 * zestaw wszystkich fiszek w quizie indeksowane po id
	 */
	FlashcardSet flashcardSet;
	
	/**
	 * tag to oznaczania logow
	 */
	private static final String LOG_TAG = QuizState.class.getName();
	
	/**
	 * przygotowuje quiz do rozpoczecia
	 * <p>
	 * Wczytuje zestaw fiszek i ustawia liczniki poprawnych oraz zlych odpowiedzi na 0 oraz numer rundy na 1.
	 * Posiada wlasne statystyki, ktore udostepnia
	 * 
	 * @param	 context	obiekt <code>Context</code>
	 * @throws QuizInitException
	 * @throws EmptyQuizException 
	 */
	public QuizState(Context context) throws QuizInitException, EmptyQuizException {
		quizHistory = new QuizRoundsHistory();
		
		learnAssistant = new LearnAssistant(context);
		flashcardSet = learnAssistant.getFlashcardSetToAsk();
		FlashcardIdList idToAskList = learnAssistant.getIdToAskList();
		
		if(idToAskList.isEmpty())
			throw new EmptyQuizException();
		
		idToAskList.shuffle();
		quizHistory.startFirstRound(idToAskList);
		
		QUIZ_SIZE = idToAskList.size();
		countCorrectPastRounds = 0;
	}
	
	/* Implementacja metod interfejsu IQuizInformator */
	@Override
	public int getNumPastCorrect() {
		return countCorrectPastRounds;
	}

	@Override
	public int getNumCorrect() {
		return quizHistory.getCurrentRound().getNumCorrect();
	}

	@Override
	public int getNumWrong() {
		return quizHistory.getCurrentRound().getNumWrong();
	}

	@Override
	public int getCurrentRoundNumber() {
		return quizHistory.getCurrentRound().NUMBER;
	}

	@Override
	public int getCurrentRoundSize() {
		return quizHistory.getCurrentRound().SIZE;
	}

	@Override
	public int getQuizSize() {
		return QUIZ_SIZE;
	}
	
	/* Implementacja metod wlasnych */
	
	public FlashcardWithId getFlashcardWithId() throws EndOfQuizRoundException {
		Long id 	= quizHistory.getCurrentRound().popFlashcardId();
		Flashcard f	= flashcardSet.get(id); 
		return new FlashcardWithId(id,f);
	}
	
	/**
	 * wklada podane id fiszki do odpowiedniego zestawu fiszek w zalenosci od tego,
	 * czy poprawnie odpowiedziano na fiszke
	 * @param idWithAnsw	id fiszki wraz z infomacja o poprawnosci odpowiedzi
	 */
	public void putAnswered(AnswerCorrectness idWithAnsw) {
		quizHistory.getCurrentRound().putAnswered(idWithAnsw);
	}
	
	/**
	 * rozpoczyna nowa runde
	 * <p>
	 * tworzy nowa runde, ktora bedzie od teraz biezaca
	 * @throws EndOfQuizException gdy nastapi koniec quizu
	 */
	public void startNextRound() throws EndOfQuizException {
		countCorrectPastRounds += quizHistory.getCurrentRound().getNumCorrect();
		try {
			quizHistory.startNextRound();	
		} catch (EndOfQuizException eofQuiz) {
			learnAssistant.updateKnowledgeIndex(quizHistory);
			throw eofQuiz;
		}
	}	
}