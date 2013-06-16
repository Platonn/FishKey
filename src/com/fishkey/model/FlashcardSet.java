package com.fishkey.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * kolekcja fiszek
 * @author Platon
 *
 */
public class FlashcardSet {
	/**
	 * lista z fiszkami
	 */
	private LinkedList<Flashcard> list;
	
	/**
	 * rozmiar listy fiszek
	 */
	private int size;
	
	/**
	 * generator losowy
	 * <p>
	 * sluzy do tasowania zestawu fiszek za kazdym razem inaczej
	 */
	private static Random random = new Random();
	
	/**
	 * nazwa zestawu fiszek, domyslnie null
	 */
	private String name = null; 
	
	/**
	 * inicjalizuje liste fiszek
	 */
	public FlashcardSet(){
		list = new LinkedList<Flashcard>();
		size = 0;
	}

	/**
	 * wyjmuje i zwraca aktualnie pierwsza fiszke z zestawu
	 * 
	 * @return	Flashcard	ostatnia fiszka na liscie,
	 */
	public Flashcard pop() {
		if(!isEmpty()) --size;
		return list.poll();
	}
	
	/**
	 * wstawia fiszke na koniec
	 * 
	 * @param	f	fiszka do wstawienia do listy
	 */
	public void add(Flashcard f) {
		++size;
		list.offer(f);
	}
	
	/**
	 * wstawia fiszke na koniec listy fiszek
	 * 
	 * @return	rozmiar listy fiszek
	 */
	public int size() {
		return size;
	}
	
	/**
	 * zwraca info, czy zestaw jest pusty
	 * 
	 * @return	true, jesli zestaw jest pusty, false w przeciwnym przypadku
	 */
	public boolean isEmpty() {
		return size==0;
	}
	
	/**
	 * oproznia liste fiszek
	 */
	public void clear() {
		list.clear();
		size=0;
	}
	
	/**
	 * tasuje zestaw fiszek
	 * <p>
	 * ustawia fiszki w losowej kolejnosci
	 */
	public void shuffle() {
		int n = list.size();
	    random.nextInt();
	    for (int i = 0; i < n; i++) {
	    	int change = i + random.nextInt(n - i);
	    	Collections.swap(list, i, change);
	    }
	}
	
	/**
	 * kopiuje do zestawu wszystkie fiszki z zestawu podanego jako argument 
	 * @param fs	zestaw fiszek do skopiowania
	 */
	public void addAllFrom(FlashcardSet fs) {
		list.addAll(fs.list);
		size += fs.size();
	}
	
	/**
	 * zwraca nazwe zestawu fiszek badz null, jesli brak nazwy quizu.
	 * <p>
	 * Nazwe zestawu fiszek mozna ustawic za pomoca metody <code>setName(String)</code>
	 * @return	nazwa quizu badz null, jesli brak nazwy quizu
	 * @see #setName(String)
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * ustawia nazwe dla zestawu fiszek
	 * @param n	nazwa zestawu fiszek
	 */
	public void setName(String name){
		this.name = name;
	}
}
