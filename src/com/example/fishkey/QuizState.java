package com.example.fishkey;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/*  
 * Klasa odpowiedzialna za obsluge stanu quizu
 */
class QuizState {
	Queue<Flashcard> flashcardSet;
	Queue<Flashcard> correctSet;
	Queue<Flashcard> wrongSet;
	int countCorrect;
	int countCorrectLastRounds;
	int countWrong;
	int flashcardSetSize;
	int currentSetSize;
	private int roundNumber;
	
	/*  
	 * Kostruktor wczytuje zestaw fiszek i ustawia liczniki poprawnych oraz z³ych odpowiedzi na 0 oraz numer rundy na 1
	 * 
	 * TODO: Wczytywanie fiszek z zewnetrznego zrodla
	 */
	public QuizState() {
		flashcardSet 			= new LinkedList<Flashcard>();
		correctSet 				= new LinkedList<Flashcard>();
		wrongSet 				= new LinkedList<Flashcard>();
		countCorrect			= 0;
		countCorrectLastRounds	= 0;
		countWrong				= 0;
		roundNumber				= 1;
		
		/* spike */
		flashcardSet.offer(new Flashcard("przypominac sobie","recollect"));
		flashcardSet.offer(new Flashcard("uczyc sie","learn"));
		flashcardSet.offer(new Flashcard("przywolywac wspomnienia","bring back"));
		flashcardSet.offer(new Flashcard("lubic","care for"));
		flashcardSet.offer(new Flashcard("odniesc sukces, miec powodzenie","succeed"));
		
		currentSetSize=flashcardSetSize=flashcardSet.size();
	}
	
	/* 
	 * Funkcja dodaje podana fiszke do listy dobrze odgadnietych fiszek 
	 * 
	 * @param	f	Dobrze odgadnieta fiszka
	 */
	public void answerCorrect(Flashcard f) {
		correctSet.offer(f);
		++countCorrect;
	}
	
	/* 
	 * Funkcja dodaje podana fiszke do listy zle odgadnietych fiszek 
	 * 
	 * @param	f	Zle odgadnieta fiszka
	 */
	public void answerWrong(Flashcard f) {
		wrongSet.offer(f);
		++countWrong;
	}
	
	/* 
	 * Funkcja zdejmuje fiszke z kolejki i zwraca ja w return 
	 * 
	 * @return	Flashcard	Pobierana z kolejki fiszka
	 */
	public Flashcard getFlashcard(){
		return flashcardSet.poll();
	}
	
	/* Funkcja zwraca liczbe udzielonych dobrych odpowiedzi */
	int getStateCorrect() {
		return countCorrect;
	}
	
	/* Funkcja zwraca liczbe poprawionych odpowiedzi w tej rundzie (w poprzedniej byly bledne) */
	int getStateCorrectLastRounds() {
		return countCorrectLastRounds;
	}
	
	/* Funkcja zwraca liczbe udzielonych zlych odpowiedzi */
	int getStateWrong() {
		return countWrong;
	}
	
	/* Funkcja zwraca liczbe wszystkich fiszek w zestawie */
	int getStateAllCurrentSet() {
		return currentSetSize;
	}
	
	int getStateAll() {
		return flashcardSetSize;
	}
	
	/* Funkcja zwraca numer rundy */
	int getRoundNumber() {
		return roundNumber;
	}
	
	/* 
	 * Funkcja rozpoczyna nowa runde i podmienia zestaw slowek quizu z tymi zle odgadnietymi w poprzedniej rundzie
	 */
	void startNextRound() {
		++roundNumber;								// Zwieksz numer rundy
		countCorrectLastRounds += countCorrect;		// Dodaj do licznika poprawnych odpowiedzi fiszki poprawione w ostatniej rundzie
		currentSetSize			= countWrong;		// Ustaw jako liczbe fiszek w nastepnej rundzie - liczbe zle odgadnietych fiszek
		countCorrect	  		= 0;				// Wyzeruj licznik poprawianych odpowiedzi
		countWrong 				= 0;				// Wyzeruj licznik zlych odpowiedzi
		flashcardSet.clear();						// Wyrzuc wszystkie fiszki z zestawu
		for(Flashcard wrongFlashcard: wrongSet){	// Przerzuc zle odgadniete fiszki do zestawu odgadywanych w nastepnej rundzie
			flashcardSet.offer(wrongFlashcard);
		}
		wrongSet.clear();							// Wyrzuc wszystkie fiszki zle odgadniete w poprzedniej rundzie
	}
	
	/* TODO: oczyscic pamiec - koniec quizu */
	void endQuiz(){
		
	}
	
	
	
}