package com.fishkey.model;

/**
 * id fiszki wraz z informacja o poprawnosci odpowiedzi na te fiszke.
 * @author Platon
 */
public class FlashcardWithId {
	private Long id;
	private Flashcard flashcard;
	
	public FlashcardWithId(Long i, Flashcard f){
		this.id 		= i;
		this.flashcard	= f;  
	}
	
	/** getter id */
	public Long getId() { return id; }
	
	/** getter odpowiedzi */
	public Flashcard getFlashcard() { return flashcard; }
}

