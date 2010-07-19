package com.ak6l.MorseTrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MorseTrainer extends Activity {
	private Button trainingButton;
	private Button practiceLetterGroupsButton;
	private Button practiceWordsButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        trainingButton = (Button)findViewById(R.id.button_training);
        trainingButton.setOnClickListener(trainingClickListener);
        
        practiceLetterGroupsButton = (Button)findViewById(R.id.button_practice_letter_groups);
        practiceLetterGroupsButton.setOnClickListener(practiceLetterGroupsClickListener);

        practiceWordsButton = (Button)findViewById(R.id.button_practice_words);
        practiceWordsButton.setOnClickListener(practiceWordsClickListener);
    }
    
    private View.OnClickListener trainingClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(MorseTrainer.this, PracticeLettersActivity.class);
			startActivity(intent);
		}
	};
    private View.OnClickListener practiceLetterGroupsClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(MorseTrainer.this, PracticeLettersActivity.class);
			startActivity(intent);
		}
	};
    private View.OnClickListener practiceWordsClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(MorseTrainer.this, PracticeLettersActivity.class);
			startActivity(intent);
		}
	};
}