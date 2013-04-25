package com.nerubia.orangehrm.handlers;

import java.util.ArrayList;

import com.nerubia.orangehrm.objects.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "OrangeHRM";

	private static final String TABLE_SESSION = "session";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "username";
	private static final String KEY_PASS = "password";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SESSION + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PASS + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);

		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public void addContact(User user) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from "+TABLE_SESSION+" where "+KEY_ID+" = "+user.getId(), null);
		if(cursor.getCount() == 0){
			ContentValues values = new ContentValues();
			values.put(KEY_ID, user.getId());
			values.put(KEY_NAME, user.getUsername()); 
			values.put(KEY_PASS, user.getPassword()); 
	
			db.insert(TABLE_SESSION, null, values);
		}
		db.close(); // Closing database connection
	}

	public User getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SESSION, new String[] { KEY_ID,
				KEY_NAME, KEY_PASS }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		User contact = new User(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		return contact;
	}
	
	public ArrayList<User> getAllContacts() {
		ArrayList<User> userList = new ArrayList<User>();
		String selectQuery = "SELECT  * FROM " + TABLE_SESSION;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				User user = new User();
				user.setId(Integer.parseInt(cursor.getString(0)));
				user.setUsername(cursor.getString(1));
				user.setPassword(cursor.getString(2));
				userList.add(user);
			} while (cursor.moveToNext());
		}

		return userList;
	}

	public int updateUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, user.getUsername());
		values.put(KEY_PASS, user.getPassword());

		return db.update(TABLE_SESSION, values, KEY_ID + " = ?",
				new String[] { String.valueOf(user.getId()) });
	}

	public void deleteUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SESSION, KEY_ID + " = ?",
				new String[] { String.valueOf(user.getId()) });
		db.close();
	}


	public int getUsersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_SESSION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);


		// return count
		return cursor.getCount();
	}

}
