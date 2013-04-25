package com.nerubia.orangehrm.objects;

import java.sql.Date;

public class User {
	private int id, userRoleId, emp_number, modifiedUserId;
	private String username, password;
	private boolean deleted, status;
	private Date dateModified;
	private int createdBy;
	
	public User(int id, int empNumber, String username,
			String password, int deleted, int status, int createdBy){
		this.id = id;
		this.emp_number = empNumber;
		this.username = username;
		this.password = password;
		this.deleted = (deleted==0)?false:true;
		this.status = (status==0)?false:true;
		this.createdBy = createdBy;
		
	}
	public User(int id, String username, String password){
		this.id = id;
		this.username = username;
		this.password = password;
	}
	public User(){
		
	}
	public int getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmp_number() {
		return emp_number;
	}

	public void setEmp_number(int emp_number) {
		this.emp_number = emp_number;
	}

	public int getModifiedUserId() {
		return modifiedUserId;
	}

	public void setModifiedUserId(int modifiedUserId) {
		this.modifiedUserId = modifiedUserId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
}
