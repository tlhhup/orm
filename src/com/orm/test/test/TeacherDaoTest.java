package com.orm.test.test;

import org.junit.Test;

import com.orm.test.dao.TeacherDao;
import com.orm.test.entity.Teacher;

public class TeacherDaoTest {

	@Test
	public void updateTeacher(){
		TeacherDao dao=new TeacherDao();
		try {
			Teacher t = new Teacher();
			t.setName("укио");
			t.setId(10);
			dao.updateEntity(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
