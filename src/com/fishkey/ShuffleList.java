package com.fishkey;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Klasa odpowiadajaca za mieszanie elementow na liscie - ustawianie w losowej kolejnosci
 */
class ShuffleList {
	/**
	 * Konstruktor miesza elementy w podanej jako argument liscie
	 * 
	 * @param	a	Lista do wymieszania
	 */
	public ShuffleList(List a) {
	    int n = a.size();
	    Random random = new Random();
	    random.nextInt();
	    for (int i = 0; i < n; i++) {
	    	int change = i + random.nextInt(n - i);
	    	Collections.swap(a, i, change);
	    }
	  }
} 