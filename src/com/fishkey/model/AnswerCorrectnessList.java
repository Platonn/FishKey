package com.fishkey.model;

import java.util.LinkedList;

/**
 * lista uporzadkowana id fiszek wraz z informacja o odpowiedzi na nia
 * @author Platon
 *
 */
public class AnswerCorrectnessList extends LinkedList<AnswerCorrectness>{
	private static final long serialVersionUID = -5077990913112152508L;

	public AnswerCorrectnessList(){
		super();
	}
	
	/**
	 * wypelnia liste obiektami z poprawnoscia odpowiedzi na fiszki o id podanych 
	 * w innej instancji AnswerCorrectnessList
	 * @param fIdList	lista id fiszek do przepytania
	 */
	public AnswerCorrectnessList(AnswerCorrectnessList sourceAnswCorrectnessList){
		super();
		for(AnswerCorrectness sourceAnswCorrectness : sourceAnswCorrectnessList) {
			Long sourceId = sourceAnswCorrectness.getId();
			this.add(new AnswerCorrectness(sourceId, AnswerCorrectness.Correctness.UNKNOWN));
		}
	}
}
