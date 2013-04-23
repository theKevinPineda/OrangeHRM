package com.nerubia.orangehrm.objects;

import android.app.Application;

public class NerubiaHRM extends Application {
	private User user;

	@Override
	public void onCreate() {

		super.onCreate();
	}

	public void initialize(int id, int empNumber, String username,
			String password, int deleted, int status, int createdBy) {
		user = new User(id, empNumber, username, password, deleted, status,
				createdBy);

	}
}
