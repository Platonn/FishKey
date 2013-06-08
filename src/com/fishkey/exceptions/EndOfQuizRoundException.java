package com.fishkey.exceptions;

import com.fishkey.QuizRound;

/**
 * wyjatek rzucany przy zakonczeniu rundy quizu
 * <p>
 * powinien zostac rzucony, gdy zabraknie fiszek do przepytywania w danej rundzie quizu
 * @author Platon
 *
 */
public class EndOfQuizRoundException extends Exception {
	private static final long serialVersionUID = 939757981209543083L;
	
	/**
	 * raport z zakonczonej rundy
	 */
	QuizRound.Report report;
	
	public EndOfQuizRoundException(QuizRound.Report r) {
		super();
		report = r;
	}
	
	/**
	 * zwraca raport
	 * @return	raport
	 */
	public QuizRound.Report getReport(){
		return report;
	}
}
