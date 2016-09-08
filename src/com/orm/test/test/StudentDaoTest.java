package com.orm.test.test;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.orm.test.dao.StudentDao;
import com.orm.test.entity.Student;

public class StudentDaoTest {

	@Before
	public void before(){
		System.out.println("预处理");
	}
	
	@After
	public void after(){
		System.out.println("执行之后");
	}
	
	@Test//单元测试的方法
	public void savaStudent(){
		StudentDao studentDao=new StudentDao();
		try {
			Student t = new Student();
			t.setId(UUID.randomUUID().toString());
			t.setName("lisi");
			t.setScore(50.6f);
			
			Student student = studentDao.saveEntityReturnID(t);
			System.out.println(student);
			studentDao.deleteById(student);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deleteStudent(){
		StudentDao dao=new StudentDao();
		try {
			Student t = new Student();
			t.setId("8fdc6571-3a03-4190-b966-7e91bf2cc66c");
			dao.deleteById(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void queryAll(){
		StudentDao dao=new StudentDao();
		try {
			List<Student> students = dao.findEntity(new Student());
			if(students!=null){
				for(Student student:students){
					System.out.println(student);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void findStudent(){
		StudentDao dao=new StudentDao();
		try {
			Student student = dao.findEntityById("684b4fb2-bd25-434b-987a-8638eeb14ffd");
			System.out.println(student);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
