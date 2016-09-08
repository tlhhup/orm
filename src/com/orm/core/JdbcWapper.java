package com.orm.core;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public abstract class JdbcWapper {
	
	private Logger logger=LoggerFactory.getLogger(getClass());

	//TODO 1、处理连接是否需要然子类获取到? 2、封装一个方法将对象转换成集合
	private Connection connection;
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	private ComboPooledDataSource dataSource;

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
			String driver = properties.getProperty("jdbc.driver");
			String url = properties.getProperty("jdbc.url");
			String user = properties.getProperty("jdbc.user");
			String password = properties.getProperty("jdbc.password");

			/*
			Class.forName(driver);
			
			// 获取数据库连接对象
			connection = DriverManager.getConnection(url,user, password);
			*/
			this.dataSource=new ComboPooledDataSource();
			dataSource.setDriverClass(driver);
			dataSource.setUser(user);
			dataSource.setJdbcUrl(url);
			dataSource.setPassword(password);
			logger.debug("数据源初始化完成");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	protected int executeUpdate(String sql, List<Object> values) throws Exception {
		logger.debug(sql);
		try {
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
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
	 * @param sql 执行的sql
	 * @param values 查询条件的值
	 * @return
	 * @throws Exception
	 */
	protected ResultSet executeQurey(String sql, List<Object> values) throws Exception {
		try {
			connection = this.dataSource.getConnection();
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

	/**
	 * 关闭数据库连接
	 * @throws Exception
	 */
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
		logger.debug("释放数据库连接完毕！");
	}

	/**
	 * 释放数据源
	 */
	protected void releaseDataSource(){
		if(dataSource!=null){
			dataSource.close();
			logger.debug("释放数据源资源");
		}
	}
	
}
