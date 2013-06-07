package com.fishkey;

import com.fishkey.exceptions.EndOfQuizRoundException;
import com.fishkey.model.Flashcard;

/**
 * metody do przekazywania fiszki tam i z powrotem (z werdyktem sedziego)
 * @author Platon
 *
 */
public interface IFlashcardPassOn {
	/**
	 * wyjmuje (usuwa) i zwraca aktualnie pierwsza fiszke z zestawu 
	 * @return	aktualnie pierwsza fiszka z zestawu
	 */
	public Flashcard popFlashcard() throws EndOfQuizRoundException;
	
	/**
	 * wklada podana fiszke do odpowiedniego zestawu fiszek w zalenosci od tego,
	 * czy poprawnie odpowiedziano na fiszke
	 * @param f					fiszka do wlozenia ktoregos z zestawow
	 * @param isCorrectAnswered	czy poprawnie odpowiedziano na te fiszke
	 */
	public void putAnswered(Flashcard f, boolean isCorrectAnswered);
	
}
