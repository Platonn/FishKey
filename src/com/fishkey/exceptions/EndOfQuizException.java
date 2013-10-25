package com.fishkey.exceptions;

/**
 * wyjatek rzucany przy zakonczeniu quizu
 * <p>
 * powinien zostac rzucony, gdy zabraknie fiszek do przepytywania w quizie
 * @author Platon
 *
 */
public class EndOfQuizException extends QuizException {
	private static final long serialVersionUID = -234955486677863840L;
}
