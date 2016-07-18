package com.orm.test.entity;

import com.orm.annotation.Column;
import com.orm.annotation.ID;
import com.orm.annotation.Table;

@Table(name = "students")
public class Student {

	@ID(name = "id")
	private String id;

	@Column(name = "score")
	private float score;

	@Column(name = "name")
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", score=" + score + ", name=" + name + "]";
	}

}
