package dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.fishkey.R;
import com.fishkey.QuizActivity;

/**
 * tworzy AlertDialog z informacja o krytycznym bledzie i zamykajacy podana aktywnosc:
 * po nacisnieciu odpowiedniego przycisku badz po nacisnieciu sprzetowego Cofnij.
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
		
		this.setPositiveButton(alertButtonText, new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog,int which) {
				 activityToClose.finish();
			 }
		});
	}
	
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
