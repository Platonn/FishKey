package com.fishkey;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.fishkey.exceptions.QuizInitException;
import com.fishkey.model.Flashcard;
import com.fishkey.model.AnswerCorrectness;
import com.fishkey.model.AnswerCorrectnessList;
import com.fishkey.model.FlashcardIdList;
import com.fishkey.model.FlashcardSet;
import com.fishkey.model.KnowledgeIndexSet;
import com.fishkey.model.ShuffleLinkedList;
import com.fishkey.model.KnowledgeIndex;

/**
 * Asystent uczenia sie odpowiada za efektywna nauke.
 * <p>
 * Podejmuje decyzje na podstawie statystyk - z ktorych fiszek warto, a z ktorych nie warto
 * przepytywac uzytkownika
 * @author Platon
 *
 */
public class LearnAssistant {
	
	/** stala wartosc, o jaka zwieksza sie wspolczynniki */
	private static final int INCREASE_KI_VALUE = 1;
	
	/** stala wartosc, o jaka zmniejsza sie wspolczynniki */
	private static final int DECREASE_KI_VALUE = 3;
		
	/** zestaw fiszek do przepytania */
	FlashcardSet toAskFlashcardSet;
	
	/** zestaw fiszek "zawieszonych" */
	FlashcardSet suspendedFlashcardSet;
	
	/** zestaw wspolczynnikow znajomosci fiszek */
	KnowledgeIndexSet knowledgeIndexSet;
	
	/** obiekt Context */ 
	Context context;
	
	public LearnAssistant(Context ctx) throws QuizInitException {
		context = ctx;
		toAskFlashcardSet		= FlashcardSetProvider.getFlashcardSet(ctx);
		knowledgeIndexSet		= KnowledgeIndexSetProvider.getKnowledgeIndexSet(ctx);
		suspendedFlashcardSet	= null;
		prepareToAskAndSuspendedFlashcardSet();
	}
	
	/**
	 * przygotowuje zestaw fiszek do przepytania i zawieszonych
	 */
	private void prepareToAskAndSuspendedFlashcardSet() {
		//TODO: tu przejrzec zestaw fiszek i podjac decyzje, ktore z nich wstrzymac.
		// zmodyfikowac takze statystyki dotyczace wstrzymanych fiszek
		Long susFSid = toAskFlashcardSet.getId();
		String susFSname = toAskFlashcardSet.getName() + " - zawieszone";
		suspendedFlashcardSet = new FlashcardSet(null, null);
		for (Map.Entry<Long, KnowledgeIndex> entry : knowledgeIndexSet.entrySet()) {
			Long id = entry.getKey();
			KnowledgeIndex ki = entry.getValue();
			int value = ki.getValue();
			if (value>0) { // TEMP: - nie robic tego tak - jesli value >0 , to usun z zestawu fiszke
				suspendedFlashcardSet.put(id,toAskFlashcardSet.get(id));
				toAskFlashcardSet.remove(id);
			}
		}
	}
	
	/**
	 * zwraca zestaw fiszek do przepytania
	 * @return zestaw fiszek do przepytania
	 */
	public FlashcardSet getFlashcardSetToAsk(){
		return toAskFlashcardSet;
	}
	
	/**
	 * zwraca zestaw wszystkich fiszek: do przepytania + zawieszone
	 * @return zestaw wszystkich fiszek: do przepytania + zawieszone
	 */
	public FlashcardSet getBaseToAskFlascardSet(){
		Long id 	= toAskFlashcardSet.getId();
		String name	= toAskFlashcardSet.getName();
		FlashcardSet baseFlashcardSet = new FlashcardSet(id, name);
		baseFlashcardSet.putAll(toAskFlashcardSet);
		baseFlashcardSet.putAll(suspendedFlashcardSet);
		return baseFlashcardSet;
	}
	
	/** 
	 * zwraca liste id fiszek do przepytania
	 * @return
	 */
	public FlashcardIdList getIdToAskList() {
		FlashcardIdList listToReturn = new FlashcardIdList();
		for (Map.Entry<Long, Flashcard> entry : toAskFlashcardSet.entrySet()) {
			Long id = entry.getKey();
			listToReturn.offer(id);
		}
		return listToReturn;
	}
	
	/** 
	 * zwraca liste id wszystkich fiszek: do przepytania + zawieszone
	 * @return lista id wszystkich fiszek: do przepytania + zawieszone
	 */
	public FlashcardIdList getIdBaseToAskList() {
		FlashcardIdList listToReturn = new FlashcardIdList();
		for (Map.Entry<Long, Flashcard> entry : toAskFlashcardSet.entrySet()) {
			Long id = entry.getKey();
			listToReturn.offer(id);
		}
		for (Map.Entry<Long, Flashcard> entry : suspendedFlashcardSet.entrySet()) {
			Long id = entry.getKey();
			listToReturn.offer(id);
		}
		return listToReturn;
	}
	
	/**
	 * TODO: zrobic to rzeczywiscie dla listy raportow z konca rund - teraz to jest robione z listy calych Rund
	 * 
	 * aktualizuje w zewnetrznym zrodle wartosci wspolczynnikow znajomosci fiszek
	 * na podstawie otrzymanej listy raportow z koncow rund quizu - zawierajacych informacje
	 * o poprawnosci badz blednosci odpowiedzi na kolejne zadawane pytania z fiszek
	 * @param roundsList	lista raportow z koncow rund quizu
	 */
	public void updateKnowledgeIndex(LinkedList<QuizRound> roundsList) {
		for(QuizRound round : roundsList){
			AnswerCorrectnessList fsCorrect = round.getAnsweredCorrectList();
			AnswerCorrectnessList fsWrong = round.getAnsweredWrongIdList();
			for (AnswerCorrectness f : fsCorrect){
				Long id = f.getId();
				KnowledgeIndex ki = knowledgeIndexSet.get(id);
				int kiValue = ki.getValue();
				ki.setValue(kiValue + INCREASE_KI_VALUE);
				ki.setCurrentDate();
			}
			for (AnswerCorrectness f : fsWrong){
				Long id = f.getId();
				KnowledgeIndex ki = knowledgeIndexSet.get(id);
				int kiValue = ki.getValue();
				ki.setValue(kiValue - DECREASE_KI_VALUE);
				ki.setCurrentDate();
			}
		}
		KnowledgeIndexSetProvider.archiveKnowledgeIndexSet(context, knowledgeIndexSet);
	}
}
