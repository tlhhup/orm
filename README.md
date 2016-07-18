# orm
通过反射+注解的方式对JDBC的操作进行简单的封装，引入泛型。使用时只需继承BaseDao即可，在需要持久化的对象中加上注解即可。

1. 使用：添加注解

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
2. 继承BaseDao

		public class UserDao extends BaseDao<User> {

		}

3. 测试

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