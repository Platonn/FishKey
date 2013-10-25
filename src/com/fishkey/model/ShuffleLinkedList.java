package com.fishkey.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * lista dziedziczaca z LinkedList, posiadajaca metode do mieszania kolejnosci swoich elementow
 * @author Platon
 *
 * @param <T>	typ elementow do przechowywania na liscie
 * @see LinkedList
 */
public class ShuffleLinkedList<T> extends LinkedList<T> {
	private static final long serialVersionUID = -7351042828068144420L;

	/**
	 * generator losowy
	 */
	private static Random random = new Random();
	
	/**
	 * ustawia elementy listy w losowej kolejnosci
	 */
	public void shuffle() {
		int n = this.size();
	    random.nextInt();
	    for (int i = 0; i < n; i++) {
	    	int change = i + random.nextInt(n - i);
	    	Collections.swap(this, i, change);
	    }
	}
}
