package com.fishkey.alertdialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.example.fishkey.R;
import com.fishkey.QuizActivity;
import com.fishkey.QuizRound;
import com.fishkey.Umpire;
import com.fishkey.exceptions.EndOfQuizException;
import com.fishkey.exceptions.EndOfQuizRoundException;

/**
 * AlertDialog.Builder tworzacy AlertDialog, ktory po wcisnieciu 
 * "pozytywnego przycisku" badz przy wcisnieciu sprzetowego Cofnij, wykona akcje:
 * zaktualizuje stan quizu i zada pytanie uzytkownikowi
 * @author Platon
 *
 */
public class QuizRoundResultAlertDialogBuilder extends AlertDialog.Builder {
	/**
	 * aktywnosc, ktora ma byc zamknieta po wcisnieciu odpowiedniego przycisku
	 * badz po nacisnieciu sprzetowego Cofnij.
	 */
	QuizActivity quizActivity;
	
	/**
	 * tag to oznaczania logow
	 */
	private static final String LOG_TAG = QuizRoundResultAlertDialogBuilder.class.getName();
	
	public QuizRoundResultAlertDialogBuilder(QuizActivity activity, QuizRound.Report report) {  // Kompilator prosi zeby przy argumencie dac final - bo uzywam ich w instancjach innych klas byc moze w momencie, gdy juz sie zmienia
		super(activity);
		quizActivity = activity;
		String alertButtonText;
		int roundNumber		= report.getRoundNumber();
		int roundSize		= report.getRoundSize();
		int correctNum		= report.getNumCorrect();
		int wrongNum		= report.getNumWrong();
		this.setTitle(activity.getResources().getText(R.string.round_finished) + " " + String.valueOf(roundNumber));
		this.setMessage(activity.getResources().getText(R.string.here_is_your_score) 
	    		 				+ String.valueOf(correctNum)
	    						+ "/"
	    		 				+ String.valueOf(roundSize)
	    		 				+ " - "
	    		 				+ String.valueOf(100*correctNum/roundSize)
	    		 				+ "% "
	    		 				+ activity.getResources().getText(R.string.correct)
	    		 				+"\n"
	    		 				
	    		 				+ String.valueOf(wrongNum)
	    						+ "/"
	    		 				+ String.valueOf(roundSize)
	    		 				+ " - "
	    		 				+ String.valueOf(100*wrongNum/roundSize)
	    		 				+ "% "
	    		 				+ activity.getResources().getText(R.string.wrong)
	    );
		alertButtonText = (String) activity.getResources().getText(R.string.next);
		
	}
	
	/**
	 * zwraca gotowy do wyswietlenia alert dialog
	 */
	@Override
	public AlertDialog create() {
		AlertDialog alertDialog = super.create();
		alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				quizActivity.updateState();
				quizActivity.askUser();
			}
		});
		return alertDialog;
	}
}
