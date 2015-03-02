package com.sqlajt_orajt;

import java.util.Random;

import android.database.Cursor;
import android.widget.Button;
import android.widget.TextView;

public class MillionaireMan {
	private final MainActivity context;
	private int currentQuestionId;
	public int score;
	private enum ANSWER {
		A, B, C, D
	};
	private int goodQueston;
	private Random generator = new Random();
	private Button A,B,C,D;
	private int numberOfQuestions;
	
	public MillionaireMan(MainActivity context, Button A, Button B, Button C, Button D) {
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
	}
	public void setCurrentQuestionId(int i){
		currentQuestionId = i;
	}

	public void setupCurrentQuestion(int i){
		TextView quest = (TextView)context.findViewById(R.id.question);
		
	}

	public void onA() {
//		if (currentQuestionId == 1)
	}
	public void onB() {
	}
	public void onC() {
	}
	public void onD() {
	}
	public void previousQuestion() {
		--currentQuestionId;
		setupAll();
	}
	private void setupAll(){
		TestAdapter mDbHelper = new TestAdapter(context);
		mDbHelper.createDatabase();
		mDbHelper.open();
		
		if (currentQuestionId == numberOfQuestions)
			currentQuestionId = 1;
		Cursor testdata = mDbHelper.getTestData(currentQuestionId);

		String name = Utility.GetColumnValue(testdata, "PYTANIE");
		String email = Utility.GetColumnValue(testdata, "ODPOWIEDZ");
		TextView que = (TextView)context.findViewById(R.id.question);

		
		Cursor badAnswer1, badAnswer2, badAnswer3;
		int random1 = generator.nextInt(numberOfQuestions);
		int random2 = generator.nextInt(numberOfQuestions);
		int random3 = generator.nextInt(numberOfQuestions);
		while(random1 == currentQuestionId ||
				random2 == currentQuestionId ||
				random3 == currentQuestionId ||
				random1 == random2 ||
				random2 == random3 ||
				random1 == random3){
			random1 = generator.nextInt(numberOfQuestions);
			random2 = generator.nextInt(numberOfQuestions);
			random3 = generator.nextInt(numberOfQuestions);
		}
		badAnswer1 = mDbHelper.getTestData(random1);
		badAnswer2 = mDbHelper.getTestData(random2);
		badAnswer3 = mDbHelper.getTestData(random3);

		String sBadAnswer1 = Utility.GetColumnValue(badAnswer1, "ODPOWIEDZ");
		String sBadAnswer2 = Utility.GetColumnValue(badAnswer2, "ODPOWIEDZ");
		String sBadAnswer3 = Utility.GetColumnValue(badAnswer3, "ODPOWIEDZ");
		
		
		//		if(random1 != currentQuestionId)
		//			badAnswer1 = mDbHelper.getTestData(random1);
		
		goodQueston = generator.nextInt(4);
		switch (goodQueston) {
		case 0:
			A.setText(email);
			B.setText(sBadAnswer1);
			C.setText(sBadAnswer2);
			D.setText(sBadAnswer3);
			break;
		case 1:
			B.setText(email);
			A.setText(sBadAnswer1);
			C.setText(sBadAnswer2);
			D.setText(sBadAnswer3);
			break;
		case 2:
			C.setText(email);
			B.setText(sBadAnswer1);
			A.setText(sBadAnswer2);
			C.setText(sBadAnswer3);
			break;
		case 3:
			D.setText(email);
			B.setText(sBadAnswer1);
			C.setText(sBadAnswer2);
			A.setText(sBadAnswer3);
			break;
			
		default:
			break;
		}
		
		que.setText(name);
		mDbHelper.close();
		
	}
	public void nextQuestion() {
		// TODO Auto-generated method stub
		++currentQuestionId;
		setupAll();
		
	}
	
}
