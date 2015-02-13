package com.sqlajt_orajt;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TestAdapter {
	protected static final String TAG = "DataAdapter";
	private static int ID = 224;
	private final Context mContext;
	private SQLiteDatabase mDb;
	private DataBaseHelper mDbHelper;

	public TestAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
	}

	public TestAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public TestAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public Cursor getAnswerByQuest(String begin_letters, boolean regex,
			String indexik) {
		try {
			// String sql
			// ="SELECT Email FROm Employees where Name LIKE '"+begin_letters+"%' ";
			String sql = "";
			if (regex)
				sql = "SELECT ID,PYTANIE, ODPOWIEDZ FROM SIECI where "
						+ begin_letters;
			else
				sql = "SELECT ID,PYTANIE, ODPOWIEDZ FROM SIECI where PYTANIE LIKE '%"
						+ begin_letters + "%'" + " and ID > " + indexik;
			Cursor mCur = mDb.rawQuery(sql, null);
			if (mCur != null) {
				mCur.moveToNext();
			}
			return mCur;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}

	}

	public Cursor getTestData(int id) {
		try {
			// String sql
			// ="SELECT EmployeeId, Name, Email FROm Employees where EmployeeId="+id;
			String sql = "SELECT ID, PYTANIE, ODPOWIEDZ FROm SIECI where ID="
					+ id;

			Cursor mCur = mDb.rawQuery(sql, null);
			if (mCur != null) {
				mCur.moveToNext();
			}
			return mCur;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public boolean SaveRecord(String question, String answer) {
		try {
			ContentValues cv = new ContentValues();
			cv.put("ID", ++ID);
			cv.put("PYTANIE", question);
			cv.put("ODPOWIEDZ", answer);

			mDb.insert("SIECI", null, cv);

			Log.d("SaveSIECI", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("SaveSIECI", ex.toString());
			return false;
		}
	}

	public boolean SaveEmployee(String name, String email) {
		try {
			ContentValues cv = new ContentValues();
			cv.put("Name", name);
			cv.put("Email", email);

			mDb.insert("Employees", null, cv);

			Log.d("SaveEmployee", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("SaveEmployee", ex.toString());
			return false;
		}
	}

}
