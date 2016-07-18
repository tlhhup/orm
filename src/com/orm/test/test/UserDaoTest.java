package com.orm.test.test;

import java.util.List;

import org.junit.Test;

import com.orm.test.dao.UserDao;
import com.orm.test.entity.User;

public class UserDaoTest {

	@Test
	public void saveUser() {
		UserDao userDao=new UserDao();
		try {
			User t = new User();
			t.setAddress("成都");
			t.setPassword("5445466554faasdfasdfa");
			t.setUsername("藏式");
			t.setTel("115465655");
			
			User user = userDao.saveEntityReturnID(t);
			System.out.println(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deleteUser() {
		UserDao userDao=new UserDao();
		try {
			User t = new User();
			int saveEntity = userDao.deleteById(t);
			System.out.println(saveEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateUser(){
		UserDao userDao=new UserDao();
		try {
			User t = new User();
			t.setId(1);
			t.setAddress("成都");
			t.setTel("110");
			int saveEntity = userDao.updateEntity(t);
			System.out.println(saveEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void findUser(){
		UserDao dao=new UserDao();
		try {
			List<User> users = dao.findEntity(new User());
			if(users!=null){
				for(User user:users){
					System.out.println(user);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void findUserById(){
		UserDao dao=new UserDao();
		try {
			User user = dao.findEntityById(1);
			System.out.println(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
