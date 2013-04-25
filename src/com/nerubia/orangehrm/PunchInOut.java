package com.nerubia.orangehrm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.nerubia.orangehrm.handlers.DatabaseHandler;
import com.nerubia.orangehrm.objects.NerubiaHRM;
import com.nerubia.orangehrm.utilities.CommonUtilities;
import com.nerubia.orangehrm.utilities.JsonClient;

public class PunchInOut extends SherlockActivity {
	Button punchButton, logoutButton;
	NerubiaHRM app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_punch);
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	private void initialize() {
		app = (NerubiaHRM) getApplicationContext();
		punchButton = (Button) findViewById(R.id.pButton);
		logoutButton = (Button)findViewById(R.id.logButton);
		if (CommonUtilities.checkConnection(getApplicationContext(),
				PunchInOut.this)) {
			getPunchStatus();
		}
		setButtonListeners();
	}
	
	private void setButtonListeners(){
		logoutButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logout();
			}
		});
		
		punchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callPunch();
			}
		});
	}
	private void logout(){
		DatabaseHandler db = new DatabaseHandler(this);
		db.deleteUser(app.getUser());
		db.close();
		app.setUser(null);
		finish();
		
	}
	private void getPunchStatus() {
		PunchStatusTask task = new PunchStatusTask();
		task.execute(CommonUtilities.URL_INIT, CommonUtilities.URL_hPUNCH);
	}

	private void callPunch() {
		if (CommonUtilities.checkConnection(getApplicationContext(),
				PunchInOut.this)) {
			PunchTask task = new PunchTask();
			task.execute(CommonUtilities.URL_INIT, CommonUtilities.URL_PUNCH);
		}

	}

	private void setButtonName(String str) {
		punchButton.setText((str.equals("in")) ? "In" : "Out");
	}

	private void changeButton() {
		String temp = punchButton.getText().toString();
		punchButton.setText((temp.equals("In")) ? "Out" : "In");
	}

	protected class PunchTask extends AsyncTask<String, Void, Boolean> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(PunchInOut.this);
			pd.setTitle("Punching..");
			pd.setMessage("Please Wait");
			pd.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Boolean result = false;
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", Integer.toString(app.getUser().getId()));
			JsonClient client = new JsonClient(params[0]);
			try {
				JSONArray json = client.getArray(params[1], map);
				if (json.length() > 0) {

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
				changeButton();
		}

	}

	protected class PunchStatusTask extends AsyncTask<String, Void, Boolean> {
		ProgressDialog pd;
		String res = "in";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(PunchInOut.this);
			pd.setTitle("Checking Status..");
			pd.setMessage("Please Wait");
			pd.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Boolean result = false;
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", Integer.toString(app.getUser().getId()));
			JsonClient client = new JsonClient(params[0]);
			try {
				JSONArray json = client.getArray(params[1], map);
				if (json.length() > 0) {
					res = json.getJSONObject(0).getString("result");
					
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
				setButtonName(res);
		}

	}
}
