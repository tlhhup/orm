package com.orm.test.entity;

import com.orm.annotation.Column;
import com.orm.annotation.ID;
import com.orm.annotation.Table;

@Table(name = "users")
public class User {

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "address")
	private String address;

	@Column(name = "tel")
	private String tel;

	@ID(name = "id", isAutoIncrement = true)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", address=" + address + ", tel=" + tel
				+ ", id=" + id + "]";
	}

}
