package com.example.fishkey;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Quiz extends Activity {
	
	private QuizState quizState;
	private Button buttonDontKnow;
	private Button buttonKnow;
	private Button buttonShowAnswer;
	private TextView state;;
	private TextView question;
	private TextView answer;
	private View progressBarCorrectLastRounds;
	private View progressBarCorrect;
	private View progressBarWrong;
	private View progressBarEmpty;
	
	/* spike */
	private TextView countCorrect;
	private TextView countWrong;
	
	private Flashcard currentFlashcard;
	private static final String answerReplacement = "?";	// zamiast odpowiedzi najpierw wyswietla sie znak zapytania
	
	int screenWidthDpi;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz);
		
		quizState = new QuizState(); //spike TODO: pobieranie zestawu fiszek z zewnetrznego zrodla
		currentFlashcard = null;
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidthDpi = (int) dm.xdpi;
		
		buttonKnow 	  	= (Button) findViewById(R.id.button_know);
		buttonDontKnow	= (Button) findViewById(R.id.button_dont_know);
		buttonShowAnswer= (Button) findViewById(R.id.button_show_answer);
		
		state	 	  	= (TextView) findViewById(R.id.state);
		question 	  	= (TextView) findViewById(R.id.question);
		answer	 	  	= (TextView) findViewById(R.id.answer);
		
		progressBarCorrectLastRounds	= (View) findViewById(R.id.progress_bar_green);
		progressBarCorrect		 		= (View) findViewById(R.id.progress_bar_green_light);
		progressBarWrong		 		= (View) findViewById(R.id.progress_bar_red);
		progressBarEmpty		 		= (View) findViewById(R.id.progress_bar_white);
		
		/* spike 
		 * TODO: Progres Bar */
		countCorrect  	= (TextView) findViewById(R.id.count_good);
		countWrong	  	= (TextView) findViewById(R.id.count_bad);
		
		/* spike */
		state.setText("12/80 w rundzie 1");
		question.setText("pytanie");
		answer.setText("odpowiedz");
		countCorrect.setText("8");
		countWrong.setText("4");
		
		updateState();											// Zaktualizowanie stanu licznikow
		askQuestion(); 											// Zadaj pytanie
	}
	
	/*
	 * Funkcja odpowieda za wyswietlenie pytania na ekranie i skonfigurowanie kontrolek.
	 * 
	 * Funkcja wyswietla pytanie, jako odpowiedz ustawia "?" oraz wyswietla przycisk "Pokaz odpowiedz".
	 * 
	 * @return void
	 */
	public void updateState(){
		countCorrect.setText(String.valueOf(quizState.getStateCorrect()));	// Aktualizowanie licznika dobrych odpowiedzi
		countWrong.setText(String.valueOf(quizState.getStateWrong()));		// Aktualizowanie zlych dobrych odpowiedzi
		state.setText(String.valueOf(quizState.getStateCorrect()+quizState.getStateWrong())
						+ "/" 
						+ String.valueOf(quizState.getStateAllCurrentSet())
						+ " w rundzie "
						+ String.valueOf(quizState.getRoundNumber())
						);													// Komunikat o calosciowym stanie i numerze rundy 

		progressBarCorrectLastRounds.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT, quizState.getStateCorrectLastRounds()));
		progressBarCorrect.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT, quizState.getStateCorrect()));
		progressBarWrong.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT, quizState.getStateWrong()));
		progressBarEmpty.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT, quizState.getStateAll() - (quizState.getStateCorrectLastRounds() + quizState.getStateCorrect() +  quizState.getStateWrong())));

	}
	
	/* Funkcja pokazuje chmurke z wynikiem koncowym i ewentualnie rozpoczyna nowy korus ze zle odgadnietymi fiszkami */
	public void showResults() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Quiz.this);
		String alertButtonText;
		if(quizState.getStateWrong()==0) {
			alertDialog.setTitle(this.getResources().getText(R.string.congratulations));
			alertDialog.setMessage(this.getResources().getText(R.string.quiz_finished));
			alertButtonText = (String) this.getResources().getText(R.string.finish);
		}
		else {
			alertDialog.setTitle(this.getResources().getText(R.string.round_finished) +
									String.valueOf(quizState.getRoundNumber())); 				// Ustawienie tytulu rundy
			alertDialog.setMessage(this.getResources().getText(R.string.here_is_your_score)		// Wyswietlenie wyniku 
		    		 				+ String.valueOf(quizState.getStateCorrect())
		    						+ "/"
		    		 				+ String.valueOf(quizState.getStateAllCurrentSet())
		    		 				+ " - "
		    		 				+ String.valueOf(100*quizState.getStateCorrect()/quizState.getStateAllCurrentSet())
		    		 				+ "% "
		    		 				+ this.getResources().getText(R.string.correct)
		    		 				+"\n"
		    		 				
		    		 				+ String.valueOf(quizState.getStateWrong())
		    						+ "/"
		    		 				+ String.valueOf(quizState.getStateAllCurrentSet())
		    		 				+ " - "
		    		 				+ String.valueOf(100*quizState.getStateWrong()/quizState.getStateAllCurrentSet())
		    		 				+ "% "
		    		 				+ this.getResources().getText(R.string.wrong)
		    );
			alertButtonText = (String) this.getResources().getText(R.string.next);
		}
        alertDialog.setPositiveButton(alertButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	/*spike - TODO: dalsze dzialania powinny byc niezalezne od wcisniecia przycisku; uzytkownik moze tez wyjsc do menu bad kliknac Cofnij */
            	if(quizState.getStateWrong()!=0) {
        			quizState.startNextRound();
        			updateState();
        			askQuestion();
        		}
                else {
                	finish(); // TODO: w tym miejscu powrocic do glownego menu, gdy juz zostanie zrobione 
                }
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
        
        
        
	}
	
	public void askQuestion() {
		currentFlashcard = quizState.getFlashcard(); 			// Pobranie fiszki z zestawu 
		if(currentFlashcard==null) {							// Jezeli nie ma juz fiszek w zestawie, wyswietlany jest ekran koncowy quizu; w przeciwnym przypadku zadawane jest pytanie
			this.showResults();
		}
		else {
			question.setText(currentFlashcard.getQuestion());	// Wyswietlenie pytania
			answer.setText(answerReplacement);					// Wyswietlenie zamiennika odpowiedzi
			buttonShowAnswer.setEnabled(true);					// Aktywacja przycisku
			buttonKnow.setEnabled(false);						// Dezaktywacja przycisku
			buttonDontKnow.setEnabled(false);					// Dezaktywacja przycisku
		}
	}
	
	/*
	 * Funkcja wywo³ywana po kliknieciu przycisku "Pokaz odpowiedz" (definicja w XMLu)
	 * 
	 * @return void
	 */
	public void showAnswer(View v) {
		answer.setText(currentFlashcard.getAnswer());			// Wyswietlenie poprawnej odpowiedzi
		buttonShowAnswer.setEnabled(false);						// Dezaktywacja przycisku
		buttonKnow.setEnabled(true);							// Aktywacja przycisku
		buttonDontKnow.setEnabled(true);						// Aktywacja przycisku
	}
	
	
	/*
	 * Funkcja wywo³ywana po kliknieciu przycisku "Wiedzialem" (definicja w XMLu)
	 * 
	 * @return void
	 */
	public void know(View v) {
		quizState.answerCorrect(currentFlashcard);
		this.updateState();
		this.askQuestion();
	}
	
	/*
	 * Funkcja wywo³ywana po kliknieciu przycisku "Nie wiedzialem" (definicja w XMLu)
	 * 
	 * @return void
	 */
	public void dontKnow(View v) {
		quizState.answerWrong(currentFlashcard);
		this.updateState();
		this.askQuestion();
	}
	
}

