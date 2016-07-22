package com.orm.core;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

public abstract class JdbcWapper {

	//TODO 1、处理连接是否需要然子类获取到? 2、封装一个方法将对象转换成集合
	protected Connection connection;
	protected PreparedStatement statement;
	protected ResultSet resultSet;

	public JdbcWapper() {
		// 默认的配置文件
		// 1、当前项目的根目录加载配置文件
		InputStream is = JdbcWapper.class.getClassLoader().getResourceAsStream("jdbc.properties");
		if (is == null) {
			is = JdbcWapper.class.getResourceAsStream("jdbc.properties");
		}
		Properties properties = new Properties();
		try {
			properties.load(is);

			// 加载驱动
			Class.forName(properties.getProperty("jdbc.driver"));
			// 获取数据库连接对象
			connection = DriverManager.getConnection(properties.getProperty("jdbc.url"),
					properties.getProperty("jdbc.user"), properties.getProperty("jdbc.password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected int executeUpdate(String sql, List<Object> values) throws Exception {
		try {
			statement = connection.prepareStatement(sql);
			// 绑定数据
			for (int i = 1; i <= values.size(); i++) {
				statement.setObject(i, values.get(i - 1));
			}
			return statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询数据
	 * 
	 * @param sql
	 *            执行的sql
	 * @param values
	 *            查询条件的值
	 * @return
	 * @throws Exception
	 */
	protected ResultSet executeQurey(String sql, List<Object> values) throws Exception {
		try {
			statement = connection.prepareStatement(sql);
			if (values != null) {// 排除没有查询条件
				// 绑定数据
				for (int i = 1; i <= values.size(); i++) {
					statement.setObject(i, values.get(i - 1));
				}
			}
			return statement.executeQuery();
		} catch (Exception e) {
			throw e;
		}

	}

	protected void closeResouce() throws Exception {
		if (resultSet != null) {
			resultSet.close();
		}
		if (statement != null) {
			statement.close();
		}
		if (connection != null) {
			connection.close();
		}
	}

}
