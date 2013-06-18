package com.fishkey.model;

import java.util.LinkedHashMap;

/**
 * zestaw fiszek o unikalnym id
 * @author Platon
 *
 */
public class FlashcardSet extends LinkedHashMap<Long,Flashcard>{
	private static final long serialVersionUID = -5077990913112152508L;
	
	/**
	 * id zestawu fiszek
	 */
	private Long id;
	
	/**
	 * nazwa zestawu fiszek
	 */
	private String name; 
		
	/**
	 * inicjalizuje liste fiszek
	 */
	public FlashcardSet(Long i, String n){
		super();
		id		= i;
		name	= n;
	}
	
	/**
	 * zwraca id zestawu fiszek
	 * @return	id zestawu fiszek
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * zwraca nazwe zestawu fiszek
	 * @return	nazwa zestawu fiszek
	 */
	public String getName() {
		return name;
	}
}
