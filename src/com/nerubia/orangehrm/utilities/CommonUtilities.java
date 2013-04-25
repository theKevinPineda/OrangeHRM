package com.nerubia.orangehrm.utilities;

import android.content.Context;

public class CommonUtilities {
	//public final static String URL = "http://10.0.2.2/";
	public final static String URL = "http://ohrmKevin.zz.mu/";
	//public final static String URL_INIT = URL+"ohrmKevin/webapi/";
	public final static String URL_INIT = URL+"webapi/";
	public final static String URL_ISEXIST = "isExist.php";
	public final static String URL_PUNCH = "punch.php";
	public final static String URL_hPUNCH = "hasPunched.php";
	
	public static boolean checkConnection(Context appContext,Context context){
		boolean value = false;
		// Alert dialog manager
		AlertDialogManager alert = new AlertDialogManager();
		// Connection detector
		ConnectionDetector cd;
		cd = new ConnectionDetector(appContext);

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(context,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			
		}
		else value = true;
		
		return value;
	}
	
}
