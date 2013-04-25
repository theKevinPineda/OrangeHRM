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
		setUser(new User(id, empNumber, username, password, deleted, status,
				createdBy));

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
