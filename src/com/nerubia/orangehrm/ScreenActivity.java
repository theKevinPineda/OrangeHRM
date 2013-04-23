package com.nerubia.orangehrm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nerubia.orangehrm.utilities.CommonUtilities;
import com.nerubia.orangehrm.utilities.JsonClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen);
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_screen, menu);
		return true;
	}

	private void initialize() {
		Button login = (Button) findViewById(R.id.loginButton);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login();
			}
		});
	}

	private void login() {
		checkCredentials(getUsername(), getPassword());
	}

	private String getUsername() {
		EditText ed = (EditText) findViewById(R.id.loginUsername);
		return ed.getText().toString().trim().replace(" ", "");
	}

	private String getPassword() {
		EditText ed = (EditText) findViewById(R.id.loginPassword);
		return ed.getText().toString().trim().replace(" ", "");
	}

	private boolean checkCredentials(String username, String password) {
		boolean value = false;
		LoginWebTask task = new LoginWebTask();
		task.execute(CommonUtilities.URL_INIT, CommonUtilities.URL_ISEXIST,
				username, password);
		return value;
	}

	public void storeInfo(JSONObject jsonObject) {
		Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
	}

	public void alert() {
		Toast.makeText(getBaseContext(), "Fail", Toast.LENGTH_LONG).show();
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
			if (result == true)
				storeInfo(jsonObject);
			else
				alert();
		}

	}
}
