package com.fishkey.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;


public class FlashcardSet {
	/**
	 * lista z fiszkami
	 */
	LinkedList<Flashcard> list;
	
	/**
	 * rozmiar listy fiszek
	 */
	int size;
	
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
	public Flashcard getNext() {
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
		list.add(f);
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
	 * wstawia fiszke na koniec listy fiszek
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
	    Random random = new Random();
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
	public void copyAllFrom(FlashcardSet fs) {
		list.addAll(fs.list);
	}
	
	/**
	 * przenosi do zestawu wszystkie fiszki z zestawu podanego jako argument
	 * <p>
	 * @param fs	zestaw fiszek do przeniesienia
	 */
	public void moveAllFrom(FlashcardSet fs) {
		list.addAll(fs.list);
		fs.clear();
	}
	
}
