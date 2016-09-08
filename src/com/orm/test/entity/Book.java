package com.orm.test.entity;

import java.io.Serializable;

import com.orm.annotation.Column;
import com.orm.annotation.ID;
import com.orm.annotation.Table;

@Table(name = "books")
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;

	@ID(isAutoIncrement = true, name = "id")
	private int id;// ;int primary key auto_increment,

	@Column(name = "name")
	private String name;// ;`varchar(100),

	@Column(name = "author")
	private String author;// ;varchar(200)

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", author=" + author + "]";
	}

}
