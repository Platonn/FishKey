package com.fishkey;


import com.fishkey.R;
import com.fishkey.alertdialogs.CrashAlertDialogBuilder;
import com.fishkey.alertdialogs.CrashWeakAlertDialogBuilder;
import com.fishkey.alertdialogs.QuizRoundResultAlertDialogBuilder;
import com.fishkey.exceptions.EmptyQuizException;
import com.fishkey.exceptions.EndOfQuizException;
import com.fishkey.exceptions.EndOfQuizRoundException;
import com.fishkey.exceptions.QuizInitException;
import com.fishkey.model.Flashcard;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * aktywnosc quizu. Odpowiada za ekran i sterowanie interakcja z uzytkownikiem.
 * @author Platon
 *
 */
public class QuizActivity extends Activity {
	
	private Button buttonDontKnow;
	private Button buttonKnow;
	private Button buttonShowAnswer;
	private TextView state;
	private TextView question;
	private TextView answer;
	private View progressBarCorrectPastRounds;
	private View progressBarCorrect;
	private View progressBarWrong;
	private View progressBarEmpty;
	
	/**
	 * tag to oznaczania logow
	 */
	private static final String LOG_TAG = QuizActivity.class.getName();
	
	/**
	 * sedzia, z ktorym bedzie sie komunikowac uzytkownik
	 */
	private Umpire umpire;
	
	/**
	 * metoda wywolywana przy stworzeniu aktywnoci
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_activity);
		
		try {
			// Stworzenie sedziego
			umpire = new UmpireConscience(this);
			
			// Pobranie referencji do zasobow widoku
			buttonKnow 	  					= (Button) findViewById(R.id.button_know);
			buttonDontKnow					= (Button) findViewById(R.id.button_dont_know);
			buttonShowAnswer				= (Button) findViewById(R.id.button_show_answer);
			state	 	  					= (TextView) findViewById(R.id.state);
			question 	  					= (TextView) findViewById(R.id.question);
			answer	 	  					= (TextView) findViewById(R.id.answer);
			progressBarCorrectPastRounds	= (View) findViewById(R.id.progress_bar_green);
			progressBarCorrect		 		= (View) findViewById(R.id.progress_bar_green_light);
			progressBarWrong		 		= (View) findViewById(R.id.progress_bar_red);
			progressBarEmpty		 		= (View) findViewById(R.id.progress_bar_white);
			
			// Ustawienie listnerow dla przyciskow
			buttonKnow.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	userAnswer(true);
		        }
			});
			buttonDontKnow.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	userAnswer(false);
		        }
			});
			buttonShowAnswer.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	showCorrectAnswer();
		        }
			});
			
			askUser();	// Zadaj pierwsze pytanie
		} catch (EmptyQuizException e) {
			String title 	= (String) this.getResources().getText(R.string.quiz_empty_title);
			String message 	= (String) this.getResources().getText(R.string.quiz_empty);
			new CrashWeakAlertDialogBuilder(QuizActivity.this, title, message).show(); //spike: zrobic to w przyszlosci CrashAlert, bo teraz po kliknieciu sprzetowego Cofnij nic sie nie dzieje
		} catch (QuizInitException e) {
			Log.e(LOG_TAG,"Blad rozpoczecia quizu");
			showUnexpectedBehaviourAlertDialog();
		} catch (EndOfQuizException e) {
			Log.e(LOG_TAG,"Nieoczekiwany koniec quizu");
			showUnexpectedBehaviourAlertDialog();
		} catch (EndOfQuizRoundException e) {
			Log.e(LOG_TAG,"Nieoczekiwany koniec rundy");
			showUnexpectedBehaviourAlertDialog();
		} catch (Exception e) {
			Log.e(LOG_TAG,"Nieoczekiwany wyjatek quizu: " + e.getMessage());
			showUnexpectedBehaviourAlertDialog();
		}
	}
	
	/** 
	 * wyswielta alert dialog z informacja o nieoczekiwanym zachowaniu quizu. Po zamknieciu
	 * alertu zamykana jest aktywnosc
	 */
	private void showUnexpectedBehaviourAlertDialog() {
		String title 	= (String) this.getResources().getText(R.string.quiz_error_title);
		String message 	= (String) this.getResources().getText(R.string.quiz_error);
		new CrashAlertDialogBuilder(QuizActivity.this, title, message).show();
	}
	
	/**
	 * wyswietla pytania na ekranie i blokuje/odblokowuje kontrolki
	 * 
	 * wyswietla pytanie, jako odpowiedz ustawia "?" oraz wyswietla przycisk "Pokaz odpowiedz".
	 */
	public void updateState(){
		int correctPastNum	= umpire.getNumPastCorrect();
		int correctNum		= umpire.getNumCorrect();
		int wrongNum		= umpire.getNumWrong();
		int leftNum			= umpire.getQuizSize() - (correctPastNum + correctNum + wrongNum);
		
		// Zmiana tekstu z biezacym stanem rundy
		state.setText(String.valueOf(correctNum + wrongNum)
						+ "/" 
						+ String.valueOf(umpire.getCurrentRoundSize())
						+ " w rundzie "
						+ String.valueOf(umpire.getCurrentRoundNumber())
						);													// Komunikat o calosciowym stanie i numerze rundy 
		
		// Zmiana szerokosci progres barow: 
		progressBarCorrectPastRounds.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT, 	correctPastNum));
		progressBarCorrect.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT, 			correctNum));
		progressBarWrong.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT, 				wrongNum));
		progressBarEmpty.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT,				leftNum));

	}
	
	/**
	 * wyswietla wynik koncowy rundy
	 */
	public void showRoundResultAlert(QuizRound.Report report) {
		new QuizRoundResultAlertDialogBuilder(QuizActivity.this, report).show();
		Log.v(LOG_TAG,"Wyswietlono alert konca rundy quizu");
	}
	
	/**
	 * wyswietla wynik koncowy rundy i zamyka aktywnosc
	 */
	public void showFinishedAlert() {
		String title 	= (String) this.getResources().getText(R.string.quiz_finished_title);
		String message 	= (String) this.getResources().getText(R.string.quiz_finished);
		new CrashAlertDialogBuilder(QuizActivity.this, title, message).show();
		Log.v(LOG_TAG,"Wyswietlono alert konca quizu");
	}
	
	/**
	 * zwraca sedziego
	 * @return	sedzia
	 */
	public Umpire getUmpire(){
		return umpire;
	}
	
	/**
	 * wyswietla na ekranie pytanie oraz: blokuje przyciski know i dontKnow, odblokowuje przycisk showAnswer
	 */
	public void askUser() {
		updateState();
		String questionText = umpire.getQuestion(); 
		question.setText(questionText);						// Wyswietlenie pytania
		answer.setText(Umpire.ANSWER_REPLACEMENT);			// Wyswietlenie zamiennika odpowiedzi
		buttonShowAnswer.setEnabled(true);
		buttonKnow.setEnabled(false);
		buttonDontKnow.setEnabled(false);
		Log.v(LOG_TAG,"Wyswietlono pytanie: " + questionText);
	}
	
	/**
	 * udzielenie przez uzytkownika informacji, czy wiedzial, jaka jest poprawna odpowiedz
	 *
	 * @param	 informacja udzielona przez uzytkownika, czy wiedzial, jaka jest poprawna odpowiedz
	*/
	public void userAnswer(boolean answer) {
		try {
			((UmpireConscience) umpire).adjudicate(answer);
			askUser();
		} catch (EndOfQuizException e) {
			showFinishedAlert();
		} catch (EndOfQuizRoundException e) {
			updateState();
			showRoundResultAlert(e.getReport());
		}
	}
	
	/**
	 * wyswietla na ekranie poprawna odpowiedz biezacej fiszki oraz:
	 * odblokowuje przyciski know i dontKnow, blokuje przycisk showAnswer
	 */
	public void showCorrectAnswer() {
		String answerText = umpire.getAnswer();
		answer.setText(answerText);							// Wyswietlenie poprawnej odpowiedzi
		buttonShowAnswer.setEnabled(false);
		buttonKnow.setEnabled(true);
		buttonDontKnow.setEnabled(true);
		Log.v(LOG_TAG,"Wyswietlono odpowiedz: " + answerText);
	}
	
	/**
	 * Metoda wywolywana przy stworzeniu menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bottom_menu_activity, menu);	
		// ustawienie listnera dla glownego przycisku menu
		menu.findItem(R.id.reset_knowledge_index).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				LearnAssistant learnAssistant;
				try {
					learnAssistant = new LearnAssistant(QuizActivity.this);
					learnAssistant.resetKnowledgeIndex();		
				} catch (QuizInitException e) {/*zignnoruj*/}
						
				// zrestartuj ta aktywnosc - wylaczajac animacje zamkniecia i startu aktywnosci:
				Intent intent = getIntent();
			    overridePendingTransition(0, 0);
			    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			    finish();
			    overridePendingTransition(0, 0);
			    startActivity(intent);
				return false;
			}
		});
		super.onCreateOptionsMenu(menu);
		return true;
	}
}

