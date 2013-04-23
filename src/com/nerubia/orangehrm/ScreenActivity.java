package com.nerubia.orangehrm;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

public class ScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_screen, menu);
		return true;
	}
	
	public String getUsername(){
		EditText ed = (EditText)findViewById(R.id.loginUsername);
		return ed.getText().toString().trim().replace(" ", "");
	}
	
	public boolean checkCredentials(String username, String password){
		boolean value = false;
		
		return value;
	}

}
