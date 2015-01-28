package com.sqlajt_orajt;

import java.util.Random;


import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	boolean currentLayout = false;
	boolean RegexAvailable = false;
	public String last_ID = new String("0");
	public String lastQuery = new String();
	
	private Random generator = new Random();
    private int index =0 ;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.database_layout);
        
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
            //test
        	final RelativeLayout lay =(RelativeLayout)findViewById(R.id.aboutlayout);
        	lay.setBackgroundColor(Color.rgb(204,255,153));
        	TextView txt = (TextView)findViewById(R.id.About);
        	txt.setText("Aplikację stworzył Lukasyno, przerabiając gotowca od Hindusa, który miał bazę danych z nazwiskami i e-mailami\n"
        			+ "Apka zawiera ponad 200 pytań z materiału dot. sieci komputerowych. \n\nJeśli chcesz aplikację "
        			+ "z twoją bazą pytań, pisz: luk_15@yahoo.com");
        	return true;
        }
        else if (id == R.id.action_regexp) {
        	setContentView(R.layout.about_layout);
        	final RelativeLayout lay =(RelativeLayout)findViewById(R.id.aboutlayout);
        	lay.setBackgroundColor(Color.rgb(153, 204,255));
        	TextView txt = (TextView)findViewById(R.id.About);
        	txt.setText("Regexp, czyli wyrażenia regularne to wzorce opisujące łańcuchy symboli\n"+
        	"Przykłady:\n\t id = '5' \twypisuje pytanie piąte\n"+
        	"\todpowiedz like 'ACL%' \t-wypisuje rekord gdzie odpowiedź zaczyna się na ACL\n\t"+
        	"\tpytanie like '%bgp%' \t-wypisuje rekord gdzie pytanie zawiera 'bgp' \n\t"+
        	"\tpytanie like '%cisco' \t-wypisuje pytanie kończące się na 'cisco' \n\t"
        	+ "Wybór fragment działa analogicznie do wyrażenia (pytanie like '%bgp%'), wystarczy tylko wpisać bgp"
        	+ " jako fragment pytania");
        	
        	return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    public void SwitchRegex(View v){
    	EditText editable = (EditText)findViewById(R.id.findEdit);
    	Button Btn = (Button)findViewById(R.id.RegexButton);
    	RegexAvailable = (RegexAvailable==true) ? false:true;
    	if(RegexAvailable){
    		Btn.setText("fragment");
    		editable.setHint("wyrażenie regularne");
    	}else{
    		Btn.setText("regex");
    		editable.setHint("fragment pytania...");
    	}
    }
    public void SaveEmployee(View v)
    {
    	EditText txtName = (EditText)findViewById(R.id.txtName);
    	EditText txtEmail = (EditText)findViewById(R.id.txtEmail);
    	
    	String pyt = txtName.getText().toString();
    	String odp = txtEmail.getText().toString();
    	
    	
    	TestAdapter mDbHelper = new TestAdapter(this);         
    	mDbHelper.createDatabase();       
    	mDbHelper.open(); 
    	
    	if(mDbHelper.SaveRecord(pyt,odp))
    	{
    		Utility.ShowMessageBox(this,"Data saved.");
    	}
    	else
    	{
    		Utility.ShowMessageBox(this,"OOPS try again!");
    	}
    }
    public void FindMatches(View v){
    	TextView question = (TextView)findViewById(R.id.CurrentQuestion);
    	TextView answer = (TextView)findViewById(R.id.CurrentAnswer);
    	
    	TestAdapter mDbHelper = new TestAdapter(this);         
    	mDbHelper.createDatabase();       
    	mDbHelper.open();
    	EditText sourcetext = (EditText)findViewById(R.id.findEdit);
    	//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    	int parsedInt = Integer.parseInt(last_ID);
    	  	
    	
    	String fragment = sourcetext.getText().toString();
    	if(!lastQuery.equals(fragment))
    		last_ID = "0";
    		
    	lastQuery = fragment;
    	if(!RegexAvailable || ((fragment.toLowerCase().contains("id") ||
    	   fragment.toLowerCase().contains("pytanie") ||
    	   fragment.toLowerCase().contains("odpowiedz")  ) && RegexAvailable) ){
    	if(fragment.toLowerCase().contains("drop") ||
    			fragment.toLowerCase().contains("alter") || 
    			fragment.toLowerCase().contains("1=1") || fragment.toLowerCase().contains(";")){
    		question.setText("Próba ataku powstrzymana.");
    		answer.setText("Musisz być naprawdę dobry żeby się włamać.");
    		
    		return;
    	}
   		Cursor testdata = null;
    	testdata = mDbHelper.getAnswerByQuest(fragment,RegexAvailable, last_ID);
    	if(testdata==null)
    	{
    		question.setText("Nic nie znaleziono");
    		answer.setText("Spróbuj ponownie");
    		return;
    	}
    	 
    	String pyt = Utility.GetColumnValue(testdata, "PYTANIE");
    	//tak było
    	String odp = Utility.GetColumnValue(testdata, "ODPOWIEDZ");
    	
    	last_ID = Utility.GetColumnValue(testdata, "ID");
    	question.setText(pyt);
    	answer.setText(odp);
    	
    	if(pyt=="" || odp==""){
    		question.setText("Nic nie znaleziono");
    		answer.setText("Spróbuj ponownie");
    		return;
    	}
    	//Utility.ShowMessageBox(this, "pytanie:\n"+ name + "odpowiedź:\n"+ email);
    	
    	mDbHelper.close();
    	}
    	else{
    		question.setText("Wpisz poprawne wyrażenie regularne!");
        	answer.setText("Przykłady: \nid='15'\npytanie like '%ospf%'");
        }
    		
    }
    public void showRandomRecord(View v){
    	TestAdapter mDbHelper = new TestAdapter(this);         
    	mDbHelper.createDatabase();       
    	mDbHelper.open(); 
    	index = generator.nextInt(224);
    	Cursor testdata = mDbHelper.getTestData(index); 

    	String pyt = Utility.GetColumnValue(testdata, "PYTANIE");
    	String odp = Utility.GetColumnValue(testdata, "ODPOWIEDZ");
    		
    	TextView quest = (TextView)findViewById(R.id.CurrentQuestion);
    	TextView ans = (TextView)findViewById(R.id.CurrentAnswer);
    	quest.setText(pyt);
    	ans.setText(odp);
    	
    	mDbHelper.close();
    }
    
    
    public void LoadPreviousEmployee(View v){
    	index-=2;
    	if(index<0)
    		index = 222;
    	LoadEmployee(v);
    }
    public void LoadEmployee(View v)
    {
    	TestAdapter mDbHelper = new TestAdapter(this);         
    	mDbHelper.createDatabase();       
    	mDbHelper.open(); 
    	++index;
    	if(index==224)
    		index=1;
    	Cursor testdata = mDbHelper.getTestData(index ); 
    	 
    	String name = Utility.GetColumnValue(testdata, "PYTANIE");
    	String email = Utility.GetColumnValue(testdata, "ODPOWIEDZ");
    	
    	Utility.ShowMessageBox(this, name + "\n>>:"+ email);
    	mDbHelper.close();
    }
    public void SwitchLayout(View v){
    	currentLayout = currentLayout==false ? true : false;
    	if(!currentLayout)
            setContentView(R.layout.activity_main);
    	else
          setContentView(R.layout.database_layout);
    }
}
