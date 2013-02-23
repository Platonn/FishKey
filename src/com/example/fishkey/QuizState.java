package com.example.fishkey;

import java.util.LinkedList;
import java.util.Queue;

/*  
 * Klasa odpowiedzialna za obsluge stanu quizu
 */
class QuizState {
	Queue<Flashcard> flashcardSet;
	Queue<Flashcard> correctSet;
	Queue<Flashcard> wrongSet;
	int flashcardSetSize;
	
	/*  
	 * Kostruktor wczytuje zestaw fiszek i ustawia liczniki poprawnych oraz z³ych odpowiedzi na 0;
	 * 
	 * TODO: Wczytywanie fiszek z zewnetrznego zrodla
	 */
	public QuizState() {
		flashcardSet = new LinkedList<Flashcard>();
		
		
		/* spike */
		flashcardSet.offer(new Flashcard("przypominac sobie","recollect"));
		flashcardSet.offer(new Flashcard("uczyc sie","learn"));
		flashcardSet.offer(new Flashcard("przywolywac wspomnienia","bring back"));
		flashcardSet.offer(new Flashcard("lubic","care for"));
		flashcardSet.offer(new Flashcard("odniesc sukces, miec powodzenie","succeed"));
		
		flashcardSetSize=flashcardSet.size();
	}
	
	/* 
	 * Funkcja dodaje podana fiszke do listy dobrze odgadnietych fiszek 
	 * 
	 * @param	f	Dobrze odgadnieta fiszka
	 */
	public void answerCorrect(Flashcard f) {
		correctSet.offer(f);
	}
	
	/* 
	 * Funkcja dodaje podana fiszke do listy zle odgadnietych fiszek 
	 * 
	 * @param	f	Zle odgadnieta fiszka
	 */
	public void answerWrong(Flashcard f) {
		wrongSet.offer(f);
	}
	
	/* 
	 * Funkcja zdejmuje fiszke z kolejki i zwraca ja w return 
	 * 
	 * @return	Flashcard	Pobierana z kolejki fiszka
	 */
	public Flashcard getFlashcard(){
		return flashcardSet.poll();
	}
	
	/* TODO: Funkcja pokazuje ekran z wynikiem koncowym */
	// public showResults();
	
	
}