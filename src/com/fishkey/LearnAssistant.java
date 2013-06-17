package com.fishkey;

import java.util.LinkedList;
import java.util.Map;

import android.content.Context;

import com.fishkey.exceptions.QuizInitException;
import com.fishkey.model.Flashcard;
import com.fishkey.model.FlashcardSet;
import com.fishkey.model.KnowledgeIndexSet;

/**
 * Asystent uczenia sie odpowiada za efektywna nauke.
 * <p>
 * Podejmuje decyzje na podstawie statystyk - z ktorych fiszek warto, a z ktorych nie warto
 * przepytywac uzytkownika
 * @author Platon
 *
 */
public class LearnAssistant {
	FlashcardSet flashcardSet;
	FlashcardSet hangedFlashcardSet;
	KnowledgeIndexSet knowledgeIndexSet;
	
	private static final int INCREASE_KI_VALUE = 1;
	private static final int DECREASE_KI_VALUE = -3;
	
	Context context;
	
	public LearnAssistant(Context ctx) throws QuizInitException {
		context = ctx;
		flashcardSet 		= FlashcardSetProvider.getFlashcardSet(ctx);
		knowledgeIndexSet		= KnowledgeIndexSetProvider.getKnowledgeIndexSet(ctx);
	}
	
	public FlashcardSet getFlashcardSet() {
		//TODO: tu przejrzec zestaw fiszek i podjac decyzje, ktore z nich wstrzymac.
		// zmodyfikowac takze statystyki dotyczace wstrzymanych fiszek
		hangedFlashcardSet = new FlashcardSet();
		for (Map.Entry<Long, KnowledgeIndexSet.KnowledgeIndex> entry : knowledgeIndexSet.entrySet()) {
			Long id = entry.getKey();
			KnowledgeIndexSet.KnowledgeIndex ki = entry.getValue();
			int value = ki.getValue();
			if (value>0) { // TEMP: - nie robic tego tak - jesli value >0 , to usun z zestawu fiszke
				for (Flashcard f : flashcardSet) {
					if (f.getId()==id)
						hangedFlashcardSet.add(f);
				}
			}
			flashcardSet.removeAll(hangedFlashcardSet);
		}
		return flashcardSet;
	}
	
	/* TODO: zrobić to dla listy raportow z konca rund */
	public void updateKnowledgeIndex(LinkedList<QuizRound> roundsList) {
		for(QuizRound round : roundsList){
			FlashcardSet fsCorrect = round.getAnsweredCorrectSet();
			FlashcardSet fsWrong = round.getAnsweredWrongSet();
			for (Flashcard f : fsCorrect){
				Long id = f.getId();
				KnowledgeIndexSet.KnowledgeIndex ki = knowledgeIndexSet.get(id);
				int kiValue = ki.getValue();
				ki.setValue(kiValue+INCREASE_KI_VALUE);
				ki.setCurrentDate();
			}
			for (Flashcard f : fsWrong){
				Long id = f.getId();
				KnowledgeIndexSet.KnowledgeIndex ki = knowledgeIndexSet.get(id);
				int kiValue = ki.getValue();
				ki.setValue(kiValue+DECREASE_KI_VALUE);
				ki.setCurrentDate();
			}
		}
		KnowledgeIndexSetProvider.archiveKnowledgeIndexSet(context, knowledgeIndexSet);
	}
}
