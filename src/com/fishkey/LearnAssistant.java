package com.fishkey;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;

import android.content.Context;
import android.util.Log;

import com.fishkey.exceptions.QuizInitException;
import com.fishkey.model.Flashcard;
import com.fishkey.model.AnswerCorrectness;
import com.fishkey.model.AnswerCorrectnessList;
import com.fishkey.model.FlashcardIdList;
import com.fishkey.model.FlashcardSet;
import com.fishkey.model.KnowledgeIndexSet;
import com.fishkey.model.QuizRoundsHistory;
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
	
	/** tag do oznaczania logow */
	private static final String LOG_TAG = LearnAssistant.class.getName();
	
	/** wartosc, o jaka zwieksza sie wspolczynniki */
	private static final int INCREASE_KI_VALUE = 1;
	
	/** wartosc, o jaka zmniejsza sie wspolczynniki */
	private static final int DECREASE_KI_VALUE = 3;
	
	
	// Cele do osiagniecia, by wejsc na wyzszy stopien zawieszenia fiszki
	private static final int TARGET_0		= 0;
	private static final int TARGET_1		= 1;
	private static final int TARGET_2		= 2;

	// Ilosc stopni zawieszenia: 3
	
	// Numer dnia od ostatniego przepytania z fiszki, po ktorym zostanie
	// ostanie ona odwieszona. Numer dnia zalezy od stopnia zawieszenia.
	private static final int DAY_PERIOD_1	= 2;
	private static final int DAY_PERIOD_2	= 3;
	private static final int DAY_PERIOD_3	= 5;
	
	/**
	 * Numer dnia od ostatniego przepytania z fiszki, po ktorym zostana
	 * zresetowane wszystkie wspolczynniki znajomosci fiszek w zestawie
	 */
	final int PERIOD_TO_RESET 	= 30;
	
	/** zestaw fiszek do przepytania */
	private FlashcardSet toAskFlashcardSet;
	
	/** zestaw fiszek "zawieszonych" */
	private FlashcardSet suspendedFlashcardSet;
	
	/** zestaw wspolczynnikow znajomosci fiszek */
	private KnowledgeIndexSet knowledgeIndexSet;
	
	/** obiekt Context */ 
	private Context context;
	
	/** data dnia, w ktorym utworzono obiekt */
	private final DateMidnight currentDate;
	
	public LearnAssistant(Context ctx) throws QuizInitException {
		context 				= ctx;
		currentDate 			= new DateMidnight();
		toAskFlashcardSet		= FlashcardSetProvider.getFlashcardSet(ctx);
		knowledgeIndexSet		= KnowledgeIndexSetProvider.getKnowledgeIndexSet(ctx);
		suspendedFlashcardSet	= null;
		prepareToAskAndSuspendedFlashcardSet();
	}
	
	/**
	 * przygotowuje zestaw fiszek do przepytania i zawieszonych
	 */
	private void prepareToAskAndSuspendedFlashcardSet() {
		Long susFSid = toAskFlashcardSet.getId();
		String susFSname = toAskFlashcardSet.getName() + " - zawieszone";
		suspendedFlashcardSet 		= new FlashcardSet(null, null);
		
		DateMidnight lastUseDate 	= knowledgeIndexSet.getDate();
		DateMidnight current		= new DateMidnight();
		Integer dateDifference 			= Days.daysBetween(new DateMidnight(lastUseDate), new DateMidnight(current)).getDays();
		if (dateDifference >= PERIOD_TO_RESET) {
			Log.i(LOG_TAG,"Minelo 30 dni od ostatniego quizu. KnowledgeIndex do zresetowania."); //spike - debug
			this.resetAndArchiveKnowledgeIndex();
		} else {
			for (Map.Entry<Long, KnowledgeIndex> entry : knowledgeIndexSet.entrySet()) {
				Long id = entry.getKey();
				KnowledgeIndex ki = entry.getValue();
				if(isSuspended(ki)) {
					suspendedFlashcardSet.put(id,toAskFlashcardSet.get(id));
					toAskFlashcardSet.remove(id);
				}
			}
		}
	}
	
	/**
	 * zwraca informacje, czy fiszka jest zawieszona. Niech roznica dni =
	 * (def.) roznica dni miedzy dniem dzisiejszym a dniem ostatniego przepytania z fiszki.
	 * Fiszka jest zawieszona, jesli ma dodatni wspolczynnik oraz:
	 * <p>
	 * - wspolczynnik jest niewiekszy niz 1 i roznica dni jest nie mniejsza niz 2
	 * <p>
	 * - wspolczynnik jest niewiekszy niz 2 i roznica dni jest nie mniejsza niz 3
	 * <p>
	 * - wspolczynnik jest wiekszy niz 2 i roznica dni jest nie mniejsza niz 5
	 * @param ki 	wspolczynnik znajomosci fiszki wraz z data ostatniego
	 * 				przepytania z niej
	 * @return		informacja, czy fiszka jest zawieszona
	 */
	private boolean isSuspended(KnowledgeIndex ki){
		Integer value 			= ki.getValue();
		Log.d(LOG_TAG,"Wartosc " + value.toString()); //spike - debug
		int dateDifference 	= ki.dayDifferenceTo(currentDate);
		
		if (value <= TARGET_0) {
			Log.d(LOG_TAG,"idSuspended returns false 0"); //spike - debug
			return false;
		} if (value <= TARGET_1 && dateDifference >= DAY_PERIOD_1) {
			Log.d(LOG_TAG,"idSuspended returns false 1"); //spike - debug
			return false;
		} if (value <= TARGET_2 && dateDifference >= DAY_PERIOD_2) {
			Log.d(LOG_TAG,"idSuspended returns false 2"); //spike - debug
			return false;
		} if (value > TARGET_2 && dateDifference >= DAY_PERIOD_3) {
			Log.d(LOG_TAG,"idSuspended returns false 3"); //spike - debug
			return false;
		}
		Log.d(LOG_TAG,"idSuspended returns true"); //spike - debug
		return true;
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
	 * aktualizuje w zewnetrznym zrodle wartosci wspolczynnikow znajomosci fiszek
	 * na podstawie otrzymanej historii quizu - zawierajacych informacje
	 * o poprawnosci badz blednosci odpowiedzi na kolejne zadawane pytania
	 * @param quizHistory	historia rund quizu 
	 */
	public void updateKnowledgeIndex(QuizRoundsHistory quizHistory) {
		LinkedList<QuizRound> roundsList = quizHistory.getAllRoundsList();
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
		KnowledgeIndexSetProvider.archiveKnowledgeIndexSet(context, knowledgeIndexSet, getCurrentDateAsString());
	}
	
	/**
	 * resetuje wspolczynniki znajomosci fiszek - ustawia wartosci na 0, a date na biezaca 
	 */
	private void resetAndArchiveKnowledgeIndex(){
		knowledgeIndexSet.setCurrentDate();
		for (Map.Entry<Long, KnowledgeIndex> entry : knowledgeIndexSet.entrySet()) {
			KnowledgeIndex ki = entry.getValue();
			ki.setValue(0);
			ki.setCurrentDate();
		}
		KnowledgeIndexSetProvider.archiveKnowledgeIndexSet(context, knowledgeIndexSet, getCurrentDateAsString());
	}
	
	/**
	 * zwraca jako string date utworzenia tego obiektu
	 * @return String - data utworzenia tego obiektu
	 */
	public String getCurrentDateAsString(){
		return currentDate.toString(KnowledgeIndex.dateFormat);
	}
	
	//spike
	public void resetKnowledgeIndex(){
		this.resetAndArchiveKnowledgeIndex();
	}
}
