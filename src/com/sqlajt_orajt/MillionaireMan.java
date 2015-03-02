package com.sqlajt_orajt;

import java.util.Random;

import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.TextView;

public class MillionaireMan {
	private final MainActivity context;
	private int currentQuestionId;
	public int score;
	// private enum ANSWER {
	// A, B, C, D
	// };
	private int goodQueston = 0;
	private Random generator = new Random();
	private Button A, B, C, D;
	private int numberOfQuestions;
	private final TextView current_question;
	private final TextView game_status;
	private SoundProvider soundProvider;
	final MediaPlayer correctPlayer;
	final MediaPlayer failPlayer;
	final MediaPlayer nextQuestionPlayer;
	
	private int kasiura = 1;
	public void newGame() {
//		soundProvider.start(context);
		currentQuestionId = generator.nextInt(numberOfQuestions);
		setupAnswersAndQuestion(currentQuestionId);
	}

	public void setupAnswersAndQuestion(int question_id) {
		TestAdapter mDbHelper = new TestAdapter(context);
		mDbHelper.createDatabase();
		mDbHelper.open();
		// Cursor cur = mDbHelper.getAnswerByQuest("1=1", true, "0");
		Cursor testdata = mDbHelper.getTestData(question_id);
		int random1 = generator.nextInt(numberOfQuestions);
		int random2 = generator.nextInt(numberOfQuestions);
		int random3 = generator.nextInt(numberOfQuestions);
		while (random1 == currentQuestionId || random2 == currentQuestionId
				|| random3 == currentQuestionId || random1 == random2
				|| random2 == random3 || random1 == random3) {
			random1 = generator.nextInt(numberOfQuestions);
			random2 = generator.nextInt(numberOfQuestions);
			random3 = generator.nextInt(numberOfQuestions);
		}
		Cursor faildata1 = mDbHelper.getTestData(random1);
		Cursor faildata2 = mDbHelper.getTestData(random2);
		Cursor faildata3 = mDbHelper.getTestData(random3);

		String pyt = Utility.GetColumnValue(testdata, "PYTANIE");
		String odp = Utility.GetColumnValue(testdata, "ODPOWIEDZ");
		String odp1 = Utility.GetColumnValue(faildata1, "ODPOWIEDZ");
		String odp2 = Utility.GetColumnValue(faildata2, "ODPOWIEDZ");
		String odp3 = Utility.GetColumnValue(faildata3, "ODPOWIEDZ");
		mDbHelper.close();
		current_question.setText(pyt);
		goodQueston = generator.nextInt(4);
		
		switch (goodQueston) {
		case 0:
			A.setText(odp);
			B.setText(odp1);
			C.setText(odp2);
			D.setText(odp3);
			break;
		case 1:
			B.setText(odp);
			A.setText(odp1);
			C.setText(odp2);
			D.setText(odp3);
			break;
		case 2:
			C.setText(odp);
			A.setText(odp1);
			B.setText(odp2);
			D.setText(odp3);
			break;
		case 3:
			D.setText(odp);
			A.setText(odp1);
			B.setText(odp2);
			C.setText(odp3);
			break;
		}

		
	}

	public MillionaireMan(MainActivity context, Button A, Button B, Button C,
			Button D) {
		this.context = context;
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;
		TestAdapter mDbHelper = new TestAdapter(context);
		mDbHelper.createDatabase();
		mDbHelper.open();
		Cursor cur = mDbHelper.getAnswerByQuest("1=1", true, "0");
		numberOfQuestions = cur.getCount();
		current_question = (TextView) context.findViewById(R.id.question);
		game_status = (TextView) context.findViewById(R.id.information);
		soundProvider = new SoundProvider();
		correctPlayer = MediaPlayer.create(context, R.raw.millionaire_correct);
		failPlayer = MediaPlayer.create(context, R.raw.millionaire_wrong);
		nextQuestionPlayer = MediaPlayer.create(context, R.raw.millionaire_next);
		
		
	}

	// public void setCurrentQuestionId(int i){
	// currentQuestionId = i;
	// }

	// public void setupCurrentQuestion(int i){
	// TextView quest = (TextView)context.findViewById(R.id.question);
	// }
	private boolean onX(int x) {
		
		if (goodQueston == x) {
			game_status.setText(context.getString(R.string.onCorrect_string));
			correctPlayer.start();
		} else {
			game_status.setText(context.getString(R.string.onFail_string));
			failPlayer.start();
		}
		return goodQueston ==x;
	}

	public void onA() {
		boolean b = onX(0);
		if(b)
			A.setBackgroundColor(Color.GREEN);
		else
			A.setBackgroundColor(Color.RED);
	}

	public void onB() {
		boolean b = onX(1);
		if(b)
			B.setBackgroundColor(Color.GREEN);
		else
			B.setBackgroundColor(Color.RED);
	}

	public void onC() {
		boolean b = onX(2);
		Button Cc = (Button)context.findViewById(R.id.button_DL);
		if(b)
			Cc.setBackgroundColor(Color.GREEN);
		else
			Cc.setBackgroundColor(Color.RED);
	}

	public void onD() {
		boolean b = onX(3);
		if(b)
			D.setBackgroundColor(Color.GREEN);
		else
			D.setBackgroundColor(Color.RED);
	
	}

	public void previousQuestion() {
		--currentQuestionId;
		if(currentQuestionId <= 0)
			currentQuestionId = numberOfQuestions-1;
		
		setupAnswersAndQuestion(currentQuestionId);
		
	}
	public void nextQuestion() {
		++currentQuestionId;
		if(currentQuestionId >=numberOfQuestions)
			currentQuestionId = 0;
		A.setBackgroundColor(Color.GRAY);
		B.setBackgroundColor(Color.GRAY);
		C.setBackgroundColor(Color.GRAY);
		D.setBackgroundColor(Color.GRAY);
		++kasiura;
		nextQuestionPlayer.start();
		game_status.setText("Pytanie za "+ kasiura  + " milionów:");
		setupAnswersAndQuestion(currentQuestionId);
	}
}
