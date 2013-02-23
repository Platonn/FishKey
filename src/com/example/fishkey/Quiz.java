package com.example.fishkey;


import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Quiz extends Activity {
	
	private QuizState quizState;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz);
		
		Button buttonDontKnow = (Button) findViewById(R.id.button_dont_know);
		Button buttonKnow 	  = (Button) findViewById(R.id.button_know);
		Button showAnswer 	  = (Button) findViewById(R.id.button_show_answer);
		TextView state	 	  = (TextView) findViewById(R.id.state);
		TextView question 	  = (TextView) findViewById(R.id.question);
		TextView answer	 	  = (TextView) findViewById(R.id.answer);
		
		/* spike 
		 * TODO: Progres Bar */
		TextView countGood 	  = (TextView) findViewById(R.id.count_good);
		TextView countWrong	  = (TextView) findViewById(R.id.count_bad);
	}
	
	
	
	
}

