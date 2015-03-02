package com.sqlajt_orajt;

import java.util.Random;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * TODO: naprawić, by apka działała poprawnie losowy wybór pytań fałszywych
 * 
 * @author lukasz
 * 
 */

public class MainActivity extends Activity {
	boolean currentLayout = false;
	boolean RegexAvailable = false;
	public String last_ID = new String("0");
	public String lastQuery = new String();

	private Random generator = new Random();
	private int index = 0;
	private MillionaireMan hubert;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.millionaire_layout);
		// setContentView(R.layout.database_layout);
		final Button A = (Button) findViewById(R.id.buttonTL);
		final Button B = (Button) findViewById(R.id.button_TR);
		final Button C = (Button) findViewById(R.id.button_DL);
		final Button D = (Button) findViewById(R.id.button_DR);

		hubert = new MillionaireMan(this, A, B, C, D);
		setupButtons();
		
	}
	private void setupButtons() {
		final Button A = (Button) findViewById(R.id.buttonTL);
		final Button B = (Button) findViewById(R.id.button_TR);
		final Button C = (Button) findViewById(R.id.button_DL);
		final Button D = (Button) findViewById(R.id.button_DR);
		final Button new_game = (Button) findViewById(R.id.new_game_button);
//		final Button prev_q = (Button) findViewById(R.id.button1);
//		final Button next_q = (Button) findViewById(R.id.btnSearch);
		
		final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.millionaire_game_start);
		A.setEnabled(!true);
		B.setEnabled(!true);
		C.setEnabled(!true);
		D.setEnabled(!true);
//		next_q.setEnabled(!true);
//		prev_q.setEnabled(!true);

		new_game.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
//				if(hubert!= null)
				hubert.newGame();
				mediaPlayer.start();
				A.setEnabled(true);
				B.setEnabled(true);
				C.setEnabled(true);
				D.setEnabled(true);
			}
		});
		A.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hubert.onA();
			}
		});
		D.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hubert.onD();
			}
		});
		B.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hubert.onB();
			}
		});
		C.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hubert.onC();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_info) {
			setContentView(R.layout.about_layout);

			final RelativeLayout lay = (RelativeLayout) findViewById(R.id.aboutlayout);
			lay.setBackgroundColor(Color.rgb(204, 255, 153));
			TextView txt = (TextView) findViewById(R.id.About);
			txt.setText(getString(R.string.about_author));
			return true;
		} else if (id == R.id.action_regexp) {
			setContentView(R.layout.about_layout);
			final RelativeLayout lay = (RelativeLayout) findViewById(R.id.aboutlayout);
			lay.setBackgroundColor(Color.rgb(153, 204, 255));
			TextView txt = (TextView) findViewById(R.id.About);
			txt.setText(getString(R.string.description));

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void SwitchRegex(View v) {
		EditText editable = (EditText) findViewById(R.id.findEdit);
		Button Btn = (Button) findViewById(R.id.RegexButton);
		RegexAvailable = (RegexAvailable == true) ? false : true;
		if (RegexAvailable) {
			Btn.setText("fragment");
			editable.setHint("wyrażenie regularne");
		} else {
			Btn.setText("regex");
			editable.setHint("fragment pytania...");
		}
	}
	/**
	 * unused
	 * @param v - used View
	 */
//	public void SaveEmployee(View v) {
//
//		EditText txtName = (EditText) findViewById(R.id.txtName);
//		EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
//
//		String pyt = txtName.getText().toString();
//		String odp = txtEmail.getText().toString();
//
//		TestAdapter mDbHelper = new TestAdapter(this);
//		mDbHelper.createDatabase();
//		mDbHelper.open();
//
//		if (mDbHelper.SaveRecord(pyt, odp)) {
//			Utility.ShowMessageBox(this, "Data saved.");
//		} else {
//			Utility.ShowMessageBox(this, "OOPS try again!");
//		}
//	}

	public void FindMatches(View v) {
		TextView question = (TextView) findViewById(R.id.CurrentQuestion);
		TextView answer = (TextView) findViewById(R.id.CurrentAnswer);

		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();
		EditText sourcetext = (EditText) findViewById(R.id.findEdit);
		// aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
		int parsedInt = Integer.parseInt(last_ID);

		String fragment = sourcetext.getText().toString();
		if (!lastQuery.equals(fragment))
			last_ID = "0";

		lastQuery = fragment;
		if (!RegexAvailable
				|| ((fragment.toLowerCase().contains("id")
						|| fragment.toLowerCase().contains("pytanie") || fragment
						.toLowerCase().contains("odpowiedz")) && RegexAvailable)) {
			if (fragment.toLowerCase().contains("drop")
					|| fragment.toLowerCase().contains("alter")
					|| fragment.toLowerCase().contains("1=1")
					|| fragment.toLowerCase().contains(";")) {
				question.setText("Próba ataku powstrzymana.");
				answer.setText("Musisz być naprawdę dobry żeby się włamać.");

				return;
			}
			Cursor testdata = null;
			testdata = mDbHelper.getAnswerByQuest(fragment, RegexAvailable,
					last_ID);
			if (testdata == null) {
				question.setText("Nic nie znaleziono");
				answer.setText("Spróbuj ponownie");
				last_ID = "0";
				return;
			}

			String pyt = Utility.GetColumnValue(testdata, "PYTANIE");
			String odp = Utility.GetColumnValue(testdata, "ODPOWIEDZ");

			last_ID = Utility.GetColumnValue(testdata, "ID");
			question.setText(pyt);
			answer.setText(odp);

			if (pyt == "" || odp == "") {
				question.setText("Nic nie znaleziono");
				answer.setText("Spróbuj ponownie");
				last_ID = "0";
				return;
			}

			mDbHelper.close();
		} else {
			question.setText("Wpisz poprawne wyrażenie regularne!");
			answer.setText("Przykłady: \nid='15'\npytanie like '%ospf%'");
		}

	}

	public void showRandomRecord(View v) {
		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();
		Cursor cur = mDbHelper.getAnswerByQuest("1=1", true, "0");
		index = generator.nextInt(cur.getCount());
		Cursor testdata = mDbHelper.getTestData(index);

		String pyt = Utility.GetColumnValue(testdata, "PYTANIE");
		String odp = Utility.GetColumnValue(testdata, "ODPOWIEDZ");

		TextView quest = (TextView) findViewById(R.id.CurrentQuestion);
		TextView ans = (TextView) findViewById(R.id.CurrentAnswer);
		quest.setText(pyt);
		ans.setText(odp);

		mDbHelper.close();
	}

	public void LoadPreviousEmployee(View v) {
		hubert.previousQuestion();
		//
		// index -= 2;
		// if (index < 0)
		// index = 222;
		// LoadEmployee(v);
	}

	public void LoadEmployee(View v) {
		hubert.nextQuestion();
		// TestAdapter mDbHelper = new TestAdapter(this);
		// mDbHelper.createDatabase();
		// mDbHelper.open();
		// ++index;
		// if (index == 224)
		// index = 1;
		// Cursor testdata = mDbHelper.getTestData(index);
		//
		// String name = Utility.GetColumnValue(testdata, "PYTANIE");
		// String email = Utility.GetColumnValue(testdata, "ODPOWIEDZ");
		//
		// Utility.ShowMessageBox(this, name + "\n>>:" + email);
		// mDbHelper.close();
	}

	public void SwitchLayout(View v) {
		currentLayout = !currentLayout;
		if (!currentLayout)
			setContentView(R.layout.millionaire_layout);
		else
			setContentView(R.layout.database_layout);
	}
}
