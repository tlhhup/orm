package com.orm.test.entity;

import com.orm.annotation.Column;
import com.orm.annotation.ID;
import com.orm.annotation.Table;

@Table(name = "teachers")
public class Teacher {

	@ID(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
