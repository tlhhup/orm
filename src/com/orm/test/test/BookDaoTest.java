package com.orm.test.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.orm.test.dao.BookDao;
import com.orm.test.entity.Book;

public class BookDaoTest {
	
	@Before
	public void before(){
		System.out.println("执行之前");
	}
	
	@After
	public void after(){
		System.out.println("执行之后");
	}

	@Test
	public void save(){
		try {
			BookDao bookDao=new BookDao();
			Book t=new Book();
			t.setAuthor("张三");
			t.setName("语文");
			System.out.println("保存之前的数据："+t);
			bookDao.saveEntity(t);
			System.out.println("保存之前的数据："+t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void delete() throws Exception{
		BookDao dao=new BookDao();
		Book t=new Book();
		t.setId(1);
		dao.deleteById(t);
	}
	
	@Test
	public void find() throws Exception{
		BookDao bookDao=new BookDao();
		Book t=new Book();
		/*List<Book> books = bookDao.findEntity(t);
		for(Book book:books){
			System.out.println(book);
		}*/
		System.out.println(bookDao.findEntityById(3));
	}
	
	@Test
	public void update() throws Exception{
		BookDao bookDao=new BookDao();
		Book t=new Book();
		t.setId(2);
		t.setName("数学");
		bookDao.updateEntity(t);
	}

	@Test
	public void getId() throws Exception{
		BookDao bookDao=new BookDao();
		Book t=new Book();
		t.setAuthor("张三");
		t.setName("语文");
		System.out.println("保存之前的数据："+t);
		bookDao.saveEntityReturnID(t);
		System.out.println("保存之前的数据："+t);
	}
	
}
