package com.iii.irocchi.account;

import android.content.Context;

public class Account_detail {
	private int _id;
	private String User_Name;
	private String Password;
	private String Sex;
	private String Age;
	private String Email;
	private String UpdateTime;
	private String Create_time;
	private String Flag;
	private String TimeZone_ID;
	private String City_ID;
	private String Country_ID;
	Context context;

	public Account_detail(Context context) {
		this.context = context;
	}

	public Account_detail() {
		_id = 0;
		User_Name = "";
		Password = "";
		Sex = "";
		Age = "";
		Email = "";
		UpdateTime = "";
		Create_time = "";
		Flag = "";
		TimeZone_ID = "";
		City_ID = "";
		Country_ID = "";
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getUser_Name() {
		return User_Name;
	}

	public void setUser_Name(String user_Name) {
		User_Name = user_Name;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getAge() {
		return Age;
	}

	public void setAge(String age) {
		Age = age;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}

	public String getCreate_time() {
		return Create_time;
	}

	public void setCreate_time(String create_time) {
		Create_time = create_time;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getTimeZone_ID() {
		return TimeZone_ID;
	}

	public void setTimeZone_ID(String timeZone_ID) {
		TimeZone_ID = timeZone_ID;
	}

	public String getCity_ID() {
		return City_ID;
	}

	public void setCity_ID(String city_ID) {
		City_ID = city_ID;
	}

	public String getCountry_ID() {
		return Country_ID;
	}

	public void setCountry_ID(String country_ID) {
		Country_ID = country_ID;
	}

}
