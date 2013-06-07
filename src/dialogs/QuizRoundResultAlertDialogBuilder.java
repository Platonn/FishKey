package dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.example.fishkey.R;
import com.fishkey.QuizActivity;
import com.fishkey.Umpire;
import com.fishkey.exceptions.EndOfQuizException;
import com.fishkey.exceptions.EndOfQuizRoundException;

/**
 * AlertDialog.Builder tworzacy AlertDialog wyswietlajacy informacje 
 * o krytycznym bledzie i zamykajacy podana aktywnosc: po nacisnieciu odpowiedniego przycisku
 * badz po wcisnieciu sprzetowego Cofnij.
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
	 * sedzia dostarczajacy informacji o stanie quizu
	 */
	Umpire umpire;
	
	/**
	 * tag to oznaczania logow
	 */
	private static final String LOG_TAG = QuizRoundResultAlertDialogBuilder.class.getName();
	
	public QuizRoundResultAlertDialogBuilder(final QuizActivity activity) {  // Kompilator prosi zeby przy argumencie dac final - bo uzywam ich w instancjach innych klas byc moze w momencie, gdy juz sie zmienia
		super(activity);
		quizActivity = activity;
		umpire = quizActivity.getUmpire();
		String alertButtonText;
		int roundNumber		= umpire.getCurrentRoundNumber();
		int roundSize		= umpire.getCurrentRoundSize();
		int correctPastNum	= umpire.getNumPastCorrect();
		int correctNum		= umpire.getNumCorrect();
		int wrongNum		= umpire.getNumWrong();
		this.setTitle(activity.getResources().getText(R.string.round_finished) + " " + String.valueOf(roundNumber));
		this.setMessage(activity.getResources().getText(R.string.here_is_your_score) 
	    		 				+ String.valueOf(correctNum)
	    						+ "/"
	    		 				+ String.valueOf(umpire.getCurrentRoundSize())
	    		 				+ " - "
	    		 				+ String.valueOf(100*correctNum/roundSize)
	    		 				+ "% "
	    		 				+ activity.getResources().getText(R.string.correct)
	    		 				+"\n"
	    		 				
	    		 				+ String.valueOf(wrongNum)
	    						+ "/"
	    		 				+ String.valueOf(umpire.getCurrentRoundSize())
	    		 				+ " - "
	    		 				+ String.valueOf(100*wrongNum/roundSize)
	    		 				+ "% "
	    		 				+ activity.getResources().getText(R.string.wrong)
	    );
		alertButtonText = (String) activity.getResources().getText(R.string.next);
	
		this.setPositiveButton(alertButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
				try {
					umpire.getQuestion();
				} catch (EndOfQuizRoundException e) {
					String title 	= (String) quizActivity.getResources().getText(R.string.quiz_error_title);
					String message 	= (String) quizActivity.getResources().getText(R.string.quiz_error);
					new CrashAlertDialogBuilder(quizActivity, title, message).show();
					Log.e(LOG_TAG, "Nastapil koniec rundy quizu po wyswietleniu wynikow rundy.");
				} catch (EndOfQuizException e) {
					String title 	= (String) quizActivity.getResources().getText(R.string.quiz_error_title);
					String message 	= (String) quizActivity.getResources().getText(R.string.quiz_error);
					new CrashAlertDialogBuilder(quizActivity, title, message).show();
					Log.e(LOG_TAG, "Nastapil koniec quizu po wyswietleniu wynikow rundy.");
				} 
            }
        });
	}
	
	@Override
	public AlertDialog create() {
		AlertDialog alertDialog = super.create();
		alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				try {
					umpire.getQuestion();
				} catch (EndOfQuizRoundException e) {
					String title 	= (String) quizActivity.getResources().getText(R.string.quiz_error_title);
					String message 	= (String) quizActivity.getResources().getText(R.string.quiz_error);
					new CrashAlertDialogBuilder(quizActivity, title, message).show();
					Log.e(LOG_TAG, "Nastapil koniec rundy quizu po wyswietleniu wynikow rundy.");
				} catch (EndOfQuizException e) {
					String title 	= (String) quizActivity.getResources().getText(R.string.quiz_error_title);
					String message 	= (String) quizActivity.getResources().getText(R.string.quiz_error);
					new CrashAlertDialogBuilder(quizActivity, title, message).show();
					Log.e(LOG_TAG, "Nastapil koniec quizu po wyswietleniu wynikow rundy.");
				} 
			}
		});
		return alertDialog;
	}
}
