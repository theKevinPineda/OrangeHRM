package com.nerubia.orangehrm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.nerubia.orangehrm.handlers.DatabaseHandler;
import com.nerubia.orangehrm.objects.NerubiaHRM;
import com.nerubia.orangehrm.objects.User;
import com.nerubia.orangehrm.utilities.CommonUtilities;
import com.nerubia.orangehrm.utilities.JsonClient;

public class ScreenActivity extends SherlockActivity {

	final DatabaseHandler db = new DatabaseHandler(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen);
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	private void initialize() {
		Button login = (Button) findViewById(R.id.loginButton);
		
		if(checkDatabase()){
			login(false);
		}
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login(true);
			}
		});
	}
	private boolean checkDatabase(){
		boolean value = (db.getUsersCount()>0)?true:false;
		return value;
	}
	private void login(boolean isNew) {
		// Check if Internet present
		if (CommonUtilities.checkConnection(getApplicationContext(), ScreenActivity.this)) {
			if(isNew)
				checkCredentials(getUsername().toLowerCase(), getPassword(),"1");
			else
				getCredentials();
		}
			
	}
	private void getCredentials(){
		User account=null;
		for(User user : db.getAllContacts()){
			account = user;
		}
		if(account!=null)
			checkCredentials(account.getUsername(), account.getPassword(),"2");
	}
	private String getUsername() {
		EditText ed = (EditText) findViewById(R.id.loginUsername);
		return ed.getText().toString().trim().replace(" ", "");
	}

	private String getPassword() {
		EditText ed = (EditText) findViewById(R.id.loginPassword);
		return ed.getText().toString().trim().replace(" ", "");
	}

	private boolean checkCredentials(String username, String password,String indicator) {
		boolean value = false;
		LoginWebTask task = new LoginWebTask();
		task.execute(CommonUtilities.URL_INIT, CommonUtilities.URL_ISEXIST,
				username, password,indicator);
		return value;
	}

	public void storeInfo(JSONObject jsonObject) {
		NerubiaHRM app = (NerubiaHRM) getApplicationContext();
		try {
			app.initialize(jsonObject.getInt("id"),
					0,
					jsonObject.getString("user_name"),
					jsonObject.getString("user_password"),
					jsonObject.getInt("deleted"), jsonObject.getInt("status"),
					0);
			db.addContact(app.getUser());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		redirect();
	}

	public void alert() {
		Toast.makeText(getBaseContext(), "Failed to Login", Toast.LENGTH_LONG).show();
	}

	public void erasePassword() {
		EditText ed = (EditText) findViewById(R.id.loginPassword);
		ed.setText("");
	}

	protected class LoginWebTask extends AsyncTask<String, Void, Boolean> {
		ProgressDialog pd;
		JSONObject jsonObject = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(ScreenActivity.this);
			pd.setTitle("Logging in..");
			pd.setMessage("Please Wait");
			pd.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Boolean result = false;
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", params[2]);
			map.put("password", params[3]);
			map.put("ind", params[4]);
			JsonClient client = new JsonClient(params[0]);
			try {
				JSONArray json = client.getArray(params[1], map);
				if (json.length() > 0) {
					jsonObject = json.getJSONObject(0);
					result = true;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd.dismiss();
			erasePassword();
			if (result == true) {
				storeInfo(jsonObject);
			} else
				alert();

		}

	}
	
	private void redirect(){
		Intent i = new Intent(this, PunchInOut.class);
		startActivity(i);
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
}
