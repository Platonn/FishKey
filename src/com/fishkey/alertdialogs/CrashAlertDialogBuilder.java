package com.fishkey.alertdialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.fishkey.R;
import com.fishkey.QuizActivity;

/**
 * tworzy AlertDialog, ktory po wcisnieciu "pozytywnego przycisku" badz 
 * przy wcisnieciu sprzetowego Cofnij, wykona akcje:
 * zakmnie podana aktywnosc
 * @author Platon
 *
 */
public class CrashAlertDialogBuilder extends AlertDialog.Builder {
	/**
	 * aktywnosc, ktora ma byc zamknieta po wcisnieciu odpowiedniego przycisku
	 * badz po nacisnieciu sprzetowego Cofnij.
	 */
	Activity activityToClose;
	
	public CrashAlertDialogBuilder(final Activity activity, String title, String message) {  // Kompilator prosi zeby w argumencie dac final
		super(activity);
		activityToClose = activity;
		this.setTitle(title);
		this.setMessage(message);
		String alertButtonText = (String) activity.getResources().getText(R.string.finish);
		this.setPositiveButton(alertButtonText, null);
	}
	
	/**
	 * zwraca gotowy do wyswietlenia alert dialog
	 */
	@Override
	public AlertDialog create(){
		AlertDialog alertDialog = super.create();
		alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				activityToClose.finish();
			}
		});
		return alertDialog;
	}
}
