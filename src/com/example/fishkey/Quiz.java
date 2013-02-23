package com.example.fishkey;


import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Quiz extends Activity {
	
	private QuizState quizState;
	Button buttonDontKnow;
	Button buttonKnow;
	Button showAnswer;
	TextView state;;
	TextView question;
	TextView answer;
	
	/* spike */
	TextView countGood;
	TextView countWrong;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz);
		
		buttonDontKnow = (Button) findViewById(R.id.button_dont_know);
		buttonKnow 	  = (Button) findViewById(R.id.button_know);
		showAnswer 	  = (Button) findViewById(R.id.button_show_answer);
		
		state	 	  = (TextView) findViewById(R.id.state);
		question 	  = (TextView) findViewById(R.id.question);
		answer	 	  = (TextView) findViewById(R.id.answer);
		
		/* spike 
		 * TODO: Progres Bar */
		countGood 	  = (TextView) findViewById(R.id.count_good);
		countWrong	  = (TextView) findViewById(R.id.count_bad);
		
		/* spike */
		state.setText("12/80 w rundzie 1");
		question.setText("pytanie");
		answer.setText("odpowiedz");
		countGood.setText("8");
		countWrong.setText("4");
	}
	
	
	
	
}

