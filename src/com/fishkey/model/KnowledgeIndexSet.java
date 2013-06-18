package com.fishkey.model;

import java.util.LinkedHashMap;

/**
 * zestaw wspolczynnikow znajomosci fiszek zwiazany z nimi po id
 * @author Platon
 *
 */
public class KnowledgeIndexSet extends LinkedHashMap<Long, KnowledgeIndex> {
	private static final long serialVersionUID = 3726907896583249801L;
	
	/** id zestawu fiszek, ktorego dotyczy ten Set ze wspolczynnikami znajomosci fiszek */
	Long id;
	
	public KnowledgeIndexSet(Long id) {
		this.id = id;
	}
	
	/** getter id zestawu fiszek, ktorego dotyczy ten Set ze wspolczynnikami znajomosci fiszek */
	public Long getId() {
		return id;
	}
}
