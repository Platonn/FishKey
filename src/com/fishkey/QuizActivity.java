package com.fishkey;


import com.example.fishkey.R;
import com.fishkey.exceptions.EmptyQuizException;
import com.fishkey.exceptions.EndOfQuizException;
import com.fishkey.exceptions.EndOfQuizRoundException;
import com.fishkey.exceptions.LoadFlashcardSetException;
import com.fishkey.model.Flashcard;

import dialogs.CrashAlertDialogBuilder;
import dialogs.QuizRoundResultAlertDialogBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

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
	
	private Flashcard currentFlashcard;
	private static final String answerReplacement = "?";	// zamiast odpowiedzi najpierw wyswietla sie znak zapytania
	
	/**
	 * tag to oznaczania logow
	 */
	private static final String LOG_TAG = QuizActivity.class.getName();
	
	/**
	 * sedzia, z ktorym bedzie sie komunikowac uzytkownik
	 */
	private Umpire umpire;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_activity);
		
		// Stworzenie sedziego
		try {
			umpire = new UmpireConscience(this);
		} catch (Exception e) {
			String title 	= (String) this.getResources().getText(R.string.quiz_empty_title);
			String message 	= (String) this.getResources().getText(R.string.quiz_empty);
			new CrashAlertDialogBuilder(QuizActivity.this, title, message).show();
		}
		
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
		
		askUser();
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
	public void showRoundResultAlert() {
		new QuizRoundResultAlertDialogBuilder(QuizActivity.this).show();
	}
	
	/**
	 * wyswietla wynik koncowy rundy i zamyka aktywnosc
	 */
	public void showFinishedAlert() {
		String title 	= (String) this.getResources().getText(R.string.quiz_finished_title);
		String message 	= (String) this.getResources().getText(R.string.quiz_finished);
		new CrashAlertDialogBuilder(QuizActivity.this, title, message).show();
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
	 * @throws EndOfQuizException 
	 * @throws EndOfQuizRoundException 
	 */
	public void askUser() {
		try {
			question.setText(umpire.getQuestion());				// Wyswietlenie pytania badz rzucenie wyjatku o koncu rundy albo quizu
			answer.setText(Umpire.ANSWER_REPLACEMENT);			// Wyswietlenie zamiennika odpowiedzi
			buttonShowAnswer.setEnabled(true);
			buttonKnow.setEnabled(false);
			buttonDontKnow.setEnabled(false);
		} catch (EndOfQuizException e) {
			showFinishedAlert();
		} catch (EndOfQuizRoundException e) {
			showRoundResultAlert();
		}
	}
	
	/**
	 * udzielenie przez uzytkownika informacji, czy wiedzial, jaka jest poprawna odpowiedz
	 *
	 * @param	 informacja udzielona przez uzytkownika, czy wiedzial, jaka jest poprawna odpowiedz
	*/
	public void userAnswer(boolean answer) {
		((UmpireConscience) umpire).adjudicate(answer);
		updateState();
		askUser();
	}
	
	/**
	 * Funkcja wywolywana po kliknieciu przycisku "Pokaz odpowiedz" (definicja w XMLu)
	 * 
	 * @param	View	przycisk, kotry zostal klikniety
	 */
	public void showCorrectAnswer() {
		answer.setText(currentFlashcard.getAnswer());			// Wyswietlenie poprawnej odpowiedzi
		buttonShowAnswer.setEnabled(false);
		buttonKnow.setEnabled(true);
		buttonDontKnow.setEnabled(true);
	}
	
}

